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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.domain.WishList;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.user.UserService;


//==> ȸ������ Controller
@Controller
@RequestMapping("/product/*")

public class ProductController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method ���� ����
		
	public ProductController(){
		System.out.println(this.getClass());
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml ���� �Ұ�
	//==> �Ʒ��� �ΰ��� �ּ��� Ǯ�� �ǹ̸� Ȯ�� �Ұ�
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	@RequestMapping(value="addProduct", method=RequestMethod.POST)
	public String addProduct( @ModelAttribute("product") Product product, Model model ) throws Exception {

		System.out.println("/addProduct");
		System.out.println("/addProductControll��:"+product);
		
		productService.addProduct(product);
		
		model.addAttribute("product", product);
		
		return "forward:/product/addProduct.jsp";
	}
	
	@RequestMapping(value="deleteWishList", method=RequestMethod.GET)
	public String deleteWishList( @ModelAttribute("wishList") WishList wishList, HttpSession session, Model model ) throws Exception {

		System.out.println("/deleteWishList");
		System.out.println(wishList);
		
		wishList.setCustomerId(((User)session.getAttribute("user")).getUserId());
		
		productService.deleteWishList(wishList);
		
		return "redirect:/product/listWishList";
	}
	
	@RequestMapping(value="addWishList", method=RequestMethod.GET)
	public String addWishList( @ModelAttribute("wishList") WishList wishList, HttpSession session, Model model ) throws Exception {
		
		wishList.setCustomerId(((User)session.getAttribute("user")).getUserId());
		
		System.out.println("/addWishList.do");
		System.out.println("addWishList �� wishList"+wishList);
		
		if(!productService.checkWishList(wishList)){
			productService.addWishList(wishList);
		}
		
		return "redirect:/product/listWishList";
	}
	
	@RequestMapping(value="getProduct", method=RequestMethod.GET)
	public String getProduct( @ModelAttribute("product") Product product, @RequestParam(value="menu", defaultValue="no") String menu, HttpSession session, Model model ) throws Exception {
		
		System.out.println("/getProduct");
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
	
	@RequestMapping(value="updateProduct", method=RequestMethod.GET)
	public String updateProduct( @RequestParam("prodNo") int prodNo , Model model ) throws Exception{

		System.out.println("/updateProductView");
		
		Product product = productService.getProduct(prodNo);
		
		model.addAttribute("product", product);
		
		return "forward:/product/updateProductView.jsp";
	}
	
	@RequestMapping(value="updateProduct", method=RequestMethod.POST)
	public String updateProduct( @ModelAttribute("product") Product product , Model model ) throws Exception{

		System.out.println("/updateProduct");
		
		productService.updateProduct(product);
		
		return "redirect:/product/getProduct?prodNo="+product.getProdNo();
	}
	
	@RequestMapping(value="listProduct")
	public String listProduct( @ModelAttribute("search") Search search , @RequestParam("menu") String menu, Model model , HttpServletRequest request, HttpSession session) throws Exception{
		
		System.out.println("/listProduct");
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
	
	@RequestMapping(value="listWishList", method=RequestMethod.GET)
	public ModelAndView listWishList( @ModelAttribute("search") Search search , Model model , HttpSession session) throws Exception{
		
		System.out.println("/listWishList");
		System.out.println("listWishListController��:"+search);
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		
		search.setPageSize(pageSize);
		search.setSearchKeyword(((User)session.getAttribute("user")).getUserId());
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