package com.axonivy.market.statefuldatatable.service;

import com.axonivy.market.statefuldatatable.entity.Product;

public class ProductService {
	public static Product createCopy(Product originalProduct) {
		Product product = new Product();
		product.setAvailability(originalProduct.getAvailability());
		product.setProductStatus(originalProduct.getProductStatus());
		product.setDeliveryDate(originalProduct.getDeliveryDate());
		product.setOnSale(originalProduct.getOnSale());
		product.setOrderDate(originalProduct.getOrderDate());
		product.setProductName(originalProduct.getProductName());
		product.setQuality(originalProduct.getQuality());
		product.setQuantity(originalProduct.getQuantity());
		product.setValidThrough(originalProduct.getValidThrough());
		
		return product;
	}
}
