package com.model2.mvc.web.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.domain.WishList;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.user.UserService;


//==> 회원관리 Controller
@Controller
public class ProductController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method 구현 않음
		
	public ProductController(){
		System.out.println(this.getClass());
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml 참조 할것
	//==> 아래의 두개를 주석을 풀어 의미를 확인 할것
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	@RequestMapping("/addProduct.do")
	public String addProduct( @ModelAttribute("product") Product product, Model model ) throws Exception {

		System.out.println("/addProduct.do");
		productService.addProduct(product);
		System.out.println("/addProductControll내:"+product);
		model.addAttribute("product", product);
		
		return "forward:/product/addProduct.jsp";
	}
	
	@RequestMapping("/deleteWishList.do")
	public String deleteWishList( @ModelAttribute("wishList") WishList wishList, HttpSession session, Model model ) throws Exception {

		System.out.println("/deleteWishList.do");
		System.out.println(wishList);
		String userId=((User)session.getAttribute("user")).getUserId();
		wishList.setCustomerId(userId);

		
		productService.deleteWishList(wishList);
		
		return "forward:/product/listWishList.do?";
	}
	
	@RequestMapping("/addWishList.do")
	public String addWishList( @ModelAttribute("wishList") WishList wishList, HttpSession session, Model model ) throws Exception {
		
		
		boolean isDuplicate=true;
		String userId=((User)session.getAttribute("user")).getUserId();
		String destination="forward:/product/readProduct.jsp?isDuplicate="+isDuplicate;
		
		wishList.setCustomerId(userId);
		
		System.out.println("/addWishList.do");
		System.out.println("addWishList 내 wishList"+wishList);
		if(!productService.checkWishList(wishList)){
			productService.addWishList(wishList);
			isDuplicate=false;
			destination="forward:/product/readProduct.jsp";
		}
		
		Product product=new Product();
		product=productService.getProduct(wishList.getProductNo());
		model.addAttribute("wishList", wishList);
		model.addAttribute("product", product);
		return destination;
	}
	
	@RequestMapping("/getProduct.do")
	public String getProduct( @ModelAttribute("product") Product product, @RequestParam(value="menu", defaultValue="no") String menu, HttpSession session, Model model ) throws Exception {
		
		System.out.println("/getProduct.do");
		String destination="readProduct.jsp";
		boolean isDuplicate=true;
		
		Product product2=productService.getProduct(product.getProdNo());
		product2.setProTranCode(product.getProTranCode());
		
		if(menu.equals("manage")){
			destination="updateProductView.jsp";
		}else if(menu.equals("search")){
			session.getAttribute("user");
			List<Integer> history=(ArrayList<Integer>)session.getAttribute("history");
			history.add(product.getProdNo());
			session.setAttribute("history", history);
		}
		

		WishList wishList=new WishList();
		wishList.setCustomerId(((User)session.getAttribute("user")).getUserId());
		wishList.setProductNo(product.getProdNo());
		if(!productService.checkWishList(wishList)){
			isDuplicate=false;
		}
		
		model.addAttribute("product", product2);
		model.addAttribute("isDuplicate", isDuplicate);
		
		return "forward:/product/"+destination;
	}
	
	@RequestMapping("/updateProductView.do")
	public String updateProductView( @RequestParam("prodNo") int prodNo , Model model ) throws Exception{

		System.out.println("/updateProductView.do");
		
		Product product = productService.getProduct(prodNo);
		// Model 과 View 연결
		model.addAttribute("product", product);
		
		return "forward:/product/updateProductView.jsp";
	}
	
	@RequestMapping("/updateProduct.do")
	public String updateProduct( @ModelAttribute("product") Product product , Model model ) throws Exception{

		System.out.println("/updateProduct.do");
		//Business Logic
		productService.updateProduct(product);
		
		return "redirect:/getProduct.do?prodNo="+product.getProdNo();
	}
	
	@RequestMapping("/listProduct.do")
	public String listProduct( @ModelAttribute("search") Search search , @RequestParam("menu") String menu, Model model , HttpServletRequest request, HttpSession session) throws Exception{
		
		System.out.println("/listProduct.do");
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		
		search.setPageSize(pageSize);
		Map<String , Object> map=productService.getProductList(search);
		User user=(User)session.getAttribute("user");
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(map.get("list"));
		model.addAttribute("list", map.get("list"));
		model.addAttribute("role", user.getRole());
		model.addAttribute("menu", menu);
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		return "forward:/product/listProduct.jsp";
	}
	
	@RequestMapping("/listWishList.do")
	public ModelAndView listWishList( @ModelAttribute("search") Search search , Model model , HttpSession session) throws Exception{
		
		System.out.println("/listWishList.do?");
		System.out.println("listWishListController내:"+search);
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		
		search.setPageSize(pageSize);
		String userId=((User)session.getAttribute("user")).getUserId();
		search.setSearchKeyword(userId);
		Map<String , Object> map =productService.getWishList(search);
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);

		ModelAndView modelAndView=new ModelAndView();
		
		modelAndView.addObject("list",map.get("list"));
		modelAndView.addObject("resultPage", resultPage);
		modelAndView.addObject("search", search);
		modelAndView.setViewName("/product/listWishList.jsp");
		
		
		return modelAndView;
	}
}