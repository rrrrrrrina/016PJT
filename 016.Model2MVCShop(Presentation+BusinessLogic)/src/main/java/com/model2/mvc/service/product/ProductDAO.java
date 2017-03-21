package com.model2.mvc.service.product;

import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.WishList;

public interface ProductDAO {
	
	public void insertProduct(Product product) throws Exception;

	public Product getProduct(int prodNo) throws Exception;

	public List<Product> getProductList(Search search) throws Exception;
	
	public Map<String , Object> getWishList(Search search) throws Exception;

	public void updateProduct(Product product) throws Exception;
	
	public int getTotalCount(Search search) throws Exception ;

	public void addWishList(WishList wishList) throws Exception;
	
	public boolean checkWishList(WishList wishList) throws Exception;

	public int deleteWishList(WishList wishList);

}
