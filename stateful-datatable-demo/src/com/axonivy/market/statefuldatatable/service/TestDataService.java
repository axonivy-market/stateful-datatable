package com.axonivy.market.statefuldatatable.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.axonivy.market.statefuldatatable.dao.DaoServiceRegistry;
import com.axonivy.market.statefuldatatable.entity.BusinessObject;
import com.axonivy.market.statefuldatatable.enums.Availability;
import com.axonivy.market.statefuldatatable.enums.BusinessObjectStatus;
import com.axonivy.market.statefuldatatable.enums.Quality;

public class TestDataService {
	private static List<BusinessObject> createTestData() throws ParseException {
		List<BusinessObject> businessObjectList = new ArrayList<>();
		
		BusinessObject businessObject = new BusinessObject();
		businessObject.setAvailability(Availability.UNAVAILABLE);
		businessObject.setBusinessObjectStatus(BusinessObjectStatus.REQUESTED);
		businessObject.setDeliveryDate(DateService.stringAsDate("20.10.2022"));
		businessObject.setOnSale(true);
		businessObject.setOrderDate(DateService.stringAsDate("12.08.2022"));
		businessObject.setProductName("Boots");
		businessObject.setQuality(Quality.HIGH);
		businessObject.setQuantity(2);
		businessObject.setValidThrough(DateService.stringAsDate("22.11.2022"));
		
		businessObjectList.add(businessObject);
		
		businessObject = new BusinessObject();
		businessObject.setAvailability(Availability.IN_STOCK);
		businessObject.setBusinessObjectStatus(BusinessObjectStatus.IN_PRODUCTION);
		businessObject.setDeliveryDate(DateService.stringAsDate("20.09.2022"));
		businessObject.setOnSale(true);
		businessObject.setOrderDate(DateService.stringAsDate("12.08.2022"));
		businessObject.setProductName("T-Shirt");
		businessObject.setQuality(Quality.LOW);
		businessObject.setQuantity(1);
		businessObject.setValidThrough(DateService.stringAsDate("22.11.2022"));
		
		businessObjectList.add(businessObject);
		
		businessObject = new BusinessObject();
		businessObject.setAvailability(Availability.IN_STOCK);
		businessObject.setBusinessObjectStatus(BusinessObjectStatus.REQUESTED);
		businessObject.setDeliveryDate(DateService.stringAsDate("20.09.2022"));
		businessObject.setOnSale(false);
		businessObject.setOrderDate(DateService.stringAsDate("05.08.2022"));
		businessObject.setProductName("Pants");
		businessObject.setQuality(Quality.MEDIUM);
		businessObject.setQuantity(2);
		businessObject.setValidThrough(DateService.stringAsDate("19.11.2022"));
		
		businessObjectList.add(businessObject);
		
		businessObject = new BusinessObject();
		businessObject.setAvailability(Availability.IN_ORDER);
		businessObject.setBusinessObjectStatus(BusinessObjectStatus.SHIPPED);
		businessObject.setDeliveryDate(DateService.stringAsDate("20.10.2022"));
		businessObject.setOnSale(false);
		businessObject.setOrderDate(DateService.stringAsDate("06.06.2022"));
		businessObject.setProductName("Shirt");
		businessObject.setQuality(Quality.MEDIUM);
		businessObject.setQuantity(10);
		businessObject.setValidThrough(DateService.stringAsDate("30.01.2022"));
		
		businessObjectList.add(businessObject);
		
		return businessObjectList;
	}
	
	public static void createAndSaveBusinessTestData() throws ParseException {
		for(BusinessObject businessObject : createTestData()) {
			DaoServiceRegistry.getBusinessObjectRepoDAO().save(businessObject);
		}
	}
	
	public static void createAndSaveDatabaseTestData() throws ParseException {
		for(BusinessObject businessObject : createTestData()) {
			DaoServiceRegistry.getBusinessObjectDatabaseDAO().save(businessObject);
		}
	}
}
