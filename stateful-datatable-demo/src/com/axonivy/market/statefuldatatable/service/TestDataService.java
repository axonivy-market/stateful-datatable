package com.axonivy.market.statefuldatatable.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.axonivy.market.statefuldatatable.dao.DaoServiceRegistry;
import com.axonivy.market.statefuldatatable.entity.Product;
import com.axonivy.market.statefuldatatable.enums.Availability;
import com.axonivy.market.statefuldatatable.enums.ProductStatus;
import com.axonivy.market.statefuldatatable.enums.Quality;

public class TestDataService {
	private static List<Product> createTestData() throws ParseException {
		List<Product> productList = new ArrayList<>();
		
		Product product = new Product();
		product.setAvailability(Availability.UNAVAILABLE);
		product.setProductStatus(ProductStatus.REQUESTED);
		product.setDeliveryDate(DateService.stringAsDate("20.10.2022"));
		product.setOnSale(true);
		product.setOrderDate(DateService.stringAsDate("12.08.2022"));
		product.setProductName("Boots");
		product.setQuality(Quality.HIGH);
		product.setQuantity(2);
		product.setValidThrough(DateService.stringAsDate("22.11.2022"));
		
		productList.add(product);
		
		product = new Product();
		product.setAvailability(Availability.IN_STOCK);
		product.setProductStatus(ProductStatus.IN_PRODUCTION);
		product.setDeliveryDate(DateService.stringAsDate("20.09.2022"));
		product.setOnSale(true);
		product.setOrderDate(DateService.stringAsDate("12.08.2022"));
		product.setProductName("T-Shirt");
		product.setQuality(Quality.LOW);
		product.setQuantity(1);
		product.setValidThrough(DateService.stringAsDate("22.11.2022"));
		
		productList.add(product);
		
		product = new Product();
		product.setAvailability(Availability.IN_STOCK);
		product.setProductStatus(ProductStatus.REQUESTED);
		product.setDeliveryDate(DateService.stringAsDate("20.09.2022"));
		product.setOnSale(false);
		product.setOrderDate(DateService.stringAsDate("05.08.2022"));
		product.setProductName("Pants");
		product.setQuality(Quality.MEDIUM);
		product.setQuantity(2);
		product.setValidThrough(DateService.stringAsDate("19.11.2022"));
		
		productList.add(product);
		
		product = new Product();
		product.setAvailability(Availability.IN_ORDER);
		product.setProductStatus(ProductStatus.SHIPPED);
		product.setDeliveryDate(DateService.stringAsDate("20.10.2022"));
		product.setOnSale(false);
		product.setOrderDate(DateService.stringAsDate("06.06.2022"));
		product.setProductName("Shirt");
		product.setQuality(Quality.MEDIUM);
		product.setQuantity(10);
		product.setValidThrough(DateService.stringAsDate("30.01.2022"));
		
		productList.add(product);
		
		return productList;
	}
	
	public static void createAndSaveBusinessTestData() throws ParseException {
		for(Product product : createTestData()) {
			DaoServiceRegistry.getProductRepoDAO().save(product);
		}
	}
	
	public static void createAndSaveDatabaseTestData() throws ParseException {
		for(Product product : createTestData()) {
			DaoServiceRegistry.getProductDatabaseDAO().save(product);
		}
	}
}
