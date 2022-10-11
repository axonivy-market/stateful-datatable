package com.axonivy.market.statefuldatatable.service;

import com.axonivy.market.statefuldatatable.entity.BusinessObject;

public class BusinessObjectService {
	public static BusinessObject createCopy(BusinessObject originalBusinessObject) {
		BusinessObject businessObject = new BusinessObject();
		businessObject.setAvailability(originalBusinessObject.getAvailability());
		businessObject.setBusinessObjectStatus(originalBusinessObject.getBusinessObjectStatus());
		businessObject.setDeliveryDate(originalBusinessObject.getDeliveryDate());
		businessObject.setOnSale(originalBusinessObject.getOnSale());
		businessObject.setOrderDate(originalBusinessObject.getOrderDate());
		businessObject.setProductName(originalBusinessObject.getProductName());
		businessObject.setQuality(originalBusinessObject.getQuality());
		businessObject.setQuantity(originalBusinessObject.getQuantity());
		businessObject.setValidThrough(originalBusinessObject.getValidThrough());
		
		return businessObject;
	}
}
