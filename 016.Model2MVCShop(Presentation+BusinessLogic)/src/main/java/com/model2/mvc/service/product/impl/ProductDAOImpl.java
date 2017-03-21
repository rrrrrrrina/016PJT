package com.model2.mvc.service.product.impl;

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
import com.model2.mvc.service.domain.WishList;
import com.model2.mvc.service.product.ProductDAO;

@Repository("productDAOImpl")
public class ProductDAOImpl implements ProductDAO {
	
	
	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSession sqlSession;
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public ProductDAOImpl() {
		System.out.println(this.getClass());
	}
	public void insertProduct(Product product) throws Exception{
		sqlSession.insert("ProductMapper.insertProduct",product);
	};

	public Product getProduct(int prodNo) throws Exception{
		return sqlSession.selectOne("ProductMapper.getProduct",prodNo);
	};

	public List<Product> getProductList(Search search) throws Exception{
		return sqlSession.selectList("ProductMapper.getProductList",search);
	};
	
	public Map<String, Object> getWishList(Search search) throws Exception{
		Map<String , Object>  map = new HashMap<String, Object>();
		Map<String , Object>  temp = new HashMap<String, Object>();
		
		List<WishList> wishList= sqlSession.selectList("ProductMapper.getWishList",search);
		
		for (int i = 0; i < wishList.size(); i++) {
			Product product=sqlSession.selectOne("ProductMapper.getProduct",wishList.get(i).getProductNo());
			wishList.get(i).setWishedProd(product);

			temp.put("searchCondition", "1");
			temp.put("productNo", wishList.get(i).getProductNo());
			
			wishList.get(i).setLikeCount(sqlSession.selectOne("ProductMapper.getTotalCountForWish",temp));
		}
		
		temp.put("searchCondition", "2");
		temp.put("customerId", search.getSearchKeyword());
		System.out.println("ddddddddddddddddddddddddddddd"+temp);
		map.put("totalCount", sqlSession.selectOne("ProductMapper.getTotalCountForWish", temp));
		map.put("list", wishList);
		
		return map;
	};

	public void updateProduct(Product product) throws Exception{
		sqlSession.update("ProductMapper.updateProduct",product);
	}
	
	public int getTotalCount(Search search) throws Exception{
		return sqlSession.selectOne("ProductMapper.getTotalCount", search);
	};
	
	public void addWishList(WishList wishList) throws Exception {
		sqlSession.insert("ProductMapper.insertWishList", wishList);
	}
	
	public boolean checkWishList(WishList wishList) throws Exception {
		int temp=0;
		temp=sqlSession.selectOne("ProductMapper.checkWishList", wishList);
		System.out.println("temp:+++++++++++++++++++"+temp);
		return (temp==0? false:true);
	}

	@Override
	public int deleteWishList(WishList wishList) {
		return sqlSession.delete("ProductMapper.deleteWishList", wishList);
	}
}
