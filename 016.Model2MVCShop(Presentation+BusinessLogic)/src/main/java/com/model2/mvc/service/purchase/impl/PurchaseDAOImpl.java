package com.model2.mvc.service.purchase.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.PurchaseDAO;

@Repository("purchaseDAOImpl")
public class PurchaseDAOImpl implements PurchaseDAO{
	
	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSession sqlSession;
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public PurchaseDAOImpl() {
		System.out.println(this.getClass());
	}
	
	public void insertPurchase(Purchase purchase) throws Exception{
		sqlSession.insert("PurchaseMapper.insertPurchase", purchase);
	}

	public Purchase findPurchase(int tranNo) throws Exception{
		return sqlSession.selectOne("PurchaseMapper.findPurchase", tranNo);
	}

	public Map<String, Object> getPurchaseList(Search search) throws Exception{
		Map<String , Object>  map = new HashMap<String, Object>();
		
		List<Purchase> list= sqlSession.selectList("PurchaseMapper.getPurchaseList", search);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setBuyer((User)sqlSession.selectOne("UserMapper.getUser", search.getSearchKeyword()));
			list.get(i).setPurchaseProd((Product)sqlSession.selectOne("ProductMapper.getProduct", list.get(i).getPurchaseProd().getProdNo()));
		}
		
		map.put("totalCount", sqlSession.selectOne("PurchaseMapper.getTotalCount", search.getSearchKeyword()));
		map.put("list", list);
		
		return map;	
	}
	
	public Map<String, Object> getSaleList(Search search) throws Exception{
		
		Map<String , Object>  map = new HashMap<String, Object>();
		List<Purchase> list= sqlSession.selectList("PurchaseMapper.getSaleList", search);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setBuyer((User)sqlSession.selectOne("UserMapper.getUser", list.get(i).getBuyer().getUserId()));
			list.get(i).setPurchaseProd((Product)sqlSession.selectOne("ProductMapper.getProduct", list.get(i).getPurchaseProd().getProdNo()));
		}
		
		map.put("totalCount", sqlSession.selectOne("PurchaseMapper.getTotalSaleCount",search));
		map.put("list", list);

	return map;
	}

	public void updatePurchase(Purchase purchase) throws Exception{
		sqlSession.update("PurchaseMapper.updatePurchase", purchase);
	}
	
	public void updateTranCode(Purchase purchase) throws Exception{
		sqlSession.update("PurchaseMapper.updateTranCode", purchase);
	}
	
	public int getTotalCount(Search search) throws Exception{
		return sqlSession.selectOne("PurchaseMapper.getTotalCount", search);
	}
	
}
