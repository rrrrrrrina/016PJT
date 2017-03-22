package com.model2.mvc.web.purchase;

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
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.domain.WishList;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;


//==> 회원관리 Controller
@Controller
@RequestMapping("/purchase/*")

public class PurchaseController {
	
	///Field
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	//setter Method 구현 않음
		
	public PurchaseController(){
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
	
	@RequestMapping(value="addPurchase", method=RequestMethod.POST)
	public String addPurchase( @ModelAttribute("purchase") Purchase purchase, @RequestParam("userId") String userId, @RequestParam("prodNo") int prodNo, Model model ) throws Exception {

		System.out.println("/addPurchase");
		User user=userService.getUser(userId);
		Product product=productService.getProduct(prodNo);
		purchase.setBuyer(user);
		purchase.setPurchaseProd(product);
		purchase.setTranCode("1");
		System.out.println("/addPurchase내:"+purchase);

		purchaseService.addPurchase(purchase);
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/addPurchase.jsp";
	}
	
	@RequestMapping(value="addPurchase", method=RequestMethod.GET)
	public ModelAndView addPurchase( @RequestParam("prodNo") int prodNo , Model model ) throws Exception{

		System.out.println("/addPurchaseView");
		
		Product product = productService.getProduct(prodNo);
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("product",product);
		modelAndView.setViewName("/purchase/addPurchaseView.jsp");
		
		return modelAndView;
	}
	
	@RequestMapping(value="getPurchase", method=RequestMethod.GET)
	public ModelAndView getPurchase(@RequestParam ("tranNo") int tranNo, Model model ) throws Exception {
		
		System.out.println("/getPurchase");
		Purchase purchase=purchaseService.getPurchase(tranNo);
		
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("purchase",purchase);
		modelAndView.setViewName("/purchase/readPurchase.jsp");
		
		return modelAndView;
	}
	
	@RequestMapping(value="updatePurchase", method=RequestMethod.GET)
	public ModelAndView updatePurchase( @RequestParam("tranNo") int tranNo , Model model ) throws Exception{

		System.out.println("/updatePurchaseView");
		
		Purchase purchase=purchaseService.getPurchase(tranNo);
		
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("purchase",purchase);
		modelAndView.setViewName("/purchase/updatePurchaseView.jsp");
		
		return modelAndView;
	}
	
	@RequestMapping(value="updatePurchase", method=RequestMethod.POST)
	public ModelAndView updatePurchase( @ModelAttribute("purchase") Purchase purchase , Model model ) throws Exception{

		System.out.println("/updatePurchase");
		System.out.println("updatePurchase내:"+purchase);
		purchaseService.updatePurcahse(purchase);
		Purchase purchase2=purchaseService.getPurchase(purchase.getTranNo());
		
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("purchase", purchase2);
		modelAndView.setViewName("redirect:/purchase/listPurchase");
		
		return modelAndView;
	}
	
	@RequestMapping(value="updateTranCode", method=RequestMethod.GET)
	public ModelAndView updateTranCode( @ModelAttribute("purchase") Purchase purchase , Model model ) throws Exception{

		System.out.println("/updateTranCode");
		Purchase purchase2=purchaseService.getPurchase(purchase.getTranNo());
		purchaseService.updateTranCode(purchase2);
		
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.setViewName("redirect:/purchase/listPurchase");
		
		return modelAndView;
	}
	
	@RequestMapping(value="updateTranCodeByProd", method=RequestMethod.GET)
	public ModelAndView updateTranCodeByProd( @RequestParam("prodNo") int prodNo , @RequestParam("proTranCode") String proTranCode, Model model ) throws Exception{

		System.out.println("/updateTranCodeByProd");
		Purchase purchase2=purchaseService.getPurchase2(prodNo);
		purchase2.setTranCode(proTranCode);
		purchaseService.updateTranCode(purchase2);
		
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.setViewName("redirect:/product/listProduct?menu=manage");
		
		return modelAndView;
	}
	
	@RequestMapping(value="listPurchase")
	public ModelAndView listPurchase( @ModelAttribute("search") Search search , Model model , HttpSession session) throws Exception{
		
		System.out.println("/listPurchase");
		System.out.println("listProductController내:"+search);
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		
		search.setPageSize(pageSize);
		String userId=((User)session.getAttribute("user")).getUserId();
		Map<String , Object> map =purchaseService.getPurchaseList(search, userId);
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		
		
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("list",map.get("list"));
		modelAndView.addObject("resultPage", resultPage);
		modelAndView.addObject("search", search);
		modelAndView.setViewName("/purchase/listPurchase.jsp");
		
		
		return modelAndView;
	}
	
	
	@RequestMapping(value="listSale", method=RequestMethod.GET)
	public ModelAndView listSale( @ModelAttribute("search") Search search , Model model , HttpSession session) throws Exception{
		
		System.out.println("/listSale");
		System.out.println("listSale내:"+search);
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		
		search.setPageSize(pageSize);
		Map<String , Object> map=purchaseService.getSaleList(search);
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("list",map.get("list"));
		modelAndView.addObject("resultPage", resultPage);
		modelAndView.addObject("search", search);
		modelAndView.setViewName("/purchase/listPurchase.jsp");
		
		
		return modelAndView;
	}
}