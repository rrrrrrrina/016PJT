package com.model2.mvc.service.domain;

import java.sql.Date;


public class WishList {
	
	private String customerId;
	private int productNo;
	private boolean isDuplicate;
	private int numberOfPeople;
	private Product wishedProd; 
	private Date wishedDate;
	
	public Date getWishedDate() {
		return wishedDate;
	}

	public void setWishedDate(Date wishedDate) {
		this.wishedDate = wishedDate;
	}

	public boolean isDuplicate() {
		return isDuplicate;
	}

	public void setDuplicate(boolean isDuplicate) {
		this.isDuplicate = isDuplicate;
	}

	public WishList(){
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public int getProductNo() {
		return productNo;
	}

	public void setProductNo(int productNo) {
		this.productNo = productNo;
	}

	public int getNumberOfPeople() {
		return numberOfPeople;
	}

	public void setNumberOfPeople(int numberOfPeople) {
		this.numberOfPeople = numberOfPeople;
	}

	public Product getWishedProd() {
		return wishedProd;
	}

	public void setWishedProd(Product wishedProd) {
		this.wishedProd = wishedProd;
	}

	@Override
	public String toString() {
		return "WishList [customerId=" + customerId + ", productNo=" + productNo + ", isDuplicate=" + isDuplicate
				+ ", numberOfPeople=" + numberOfPeople + ", wishedProd=" + wishedProd + ", wishedDate=" + wishedDate
				+ "]";
	}
	

	
}