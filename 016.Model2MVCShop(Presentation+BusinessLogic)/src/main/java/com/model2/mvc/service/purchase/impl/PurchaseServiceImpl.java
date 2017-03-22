package com.model2.mvc.service.purchase.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductDAO;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.purchase.PurchaseDAO;
import com.model2.mvc.service.purchase.PurchaseService;

@Service("purchaseServiceImpl")
public class PurchaseServiceImpl implements PurchaseService {
	
	@Autowired
	@Qualifier("purchaseDAOImpl")
	private PurchaseDAO purchaseDAO;
	
	@Autowired
	@Qualifier("productDAOImpl")
	private ProductDAO productDAO;
	
	public void setPurchaseDAO(PurchaseDAO purchaseDao) {
		this.purchaseDAO = purchaseDao;
	}
	
	public PurchaseServiceImpl() {
		System.out.println(this.getClass());
	}

	public void addPurchase(Purchase purchase) throws Exception {
		System.out.println("----------------"+purchase);
		purchaseDAO.insertPurchase(purchase);
	}

	public Purchase getPurchase(int tranNo) throws Exception {
		return purchaseDAO.findPurchase(tranNo);
	}

	public Purchase getPurchase2(int prodNo) throws Exception {
		Product product=new Product();
		product=productDAO.getProduct(prodNo);
		
		Purchase purchase=new Purchase();
		purchase.setPurchaseProd(product);
		return purchase;
	}

	public Map<String, Object> getPurchaseList(Search search, String buyerId) throws Exception {
		search.setSearchKeyword(buyerId);
		Map<String, Object> map=purchaseDAO.getPurchaseList(search);
		
		return map;
	}
	
	public Map<String, Object> getSaleList(Search search) throws Exception {
		Map<String, Object> map = purchaseDAO.getSaleList(search);
		return map;
	}

	public void updatePurcahse(Purchase purchase) throws Exception {
		purchaseDAO.updatePurchase(purchase);
	}

	public void updateTranCode(Purchase purchase) throws Exception {
		if(purchase.getTranCode().trim().equals("1")){
			purchase.setTranCode("2");
		}else if(purchase.getTranCode().trim().equals("2")){
			purchase.setTranCode("3");
		}
		purchaseDAO.updateTranCode(purchase);
	}

}
