package com.axonivy.demo.statefuldatatable.dao;

public class DaoServiceRegistry {

	private static ProductRepoDAO productRepoDAO = null;
	private static ProductDatabaseDAO productDatabaseDAO = null;

	public static ProductRepoDAO getProductRepoDAO() {
		if(productRepoDAO == null) {
			productRepoDAO = new ProductRepoDAO();
		}
		return productRepoDAO;
	}
	
	public static ProductDatabaseDAO getProductDatabaseDAO() {
		if(productDatabaseDAO == null) {
			productDatabaseDAO = new ProductDatabaseDAO();
		}
		return productDatabaseDAO;
	}
}
