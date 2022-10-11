package com.axonivy.market.statefuldatatable.dao;

public class DaoServiceRegistry {

	private static BusinessObjectRepoDAO businessObjectRepoDAO = null;
	private static BusinessObjectDatabaseDAO businessObjectDatabaseDAO = null;

	public static BusinessObjectRepoDAO getBusinessObjectRepoDAO() {
		if(businessObjectRepoDAO == null) {
			businessObjectRepoDAO = new BusinessObjectRepoDAO();
		}
		return businessObjectRepoDAO;
	}
	
	public static BusinessObjectDatabaseDAO getBusinessObjectDatabaseDAO() {
		if(businessObjectDatabaseDAO == null) {
			businessObjectDatabaseDAO = new BusinessObjectDatabaseDAO();
		}
		return businessObjectDatabaseDAO;
	}
}
