package com.axonivy.market.statefuldatatable.dao;

import java.util.List;

import com.axonivy.market.statefuldatatable.entity.Product;

import ch.ivyteam.ivy.business.data.store.search.Query;

public interface IProductDAO {
	public Product save(Product product);	
	public Product getById(String id);	
	public List<Product> getAll();
	public void delete(Product product);
	public List<Product> callQueryWithLimit(Query<Product> query, int first, int pageSize);	
	public long getQueryRowCount(Query<Product> query);
}
