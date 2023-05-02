package com.axonivy.demo.statefuldatatable.dao;

import java.util.List;

import com.axonivy.demo.statefuldatatable.entity.Product;

import ch.ivyteam.ivy.business.data.store.search.Query;
import ch.ivyteam.ivy.environment.Ivy;

public class ProductRepoDAO implements IProductDAO {

	@Override
	public Product save(Product product) {
		Ivy.repo().save(product);
		return product;
	}

	@Override
	public Product getById(String id) {
		return Ivy.repo().find(id, Product.class);
	}

	@Override
	public List<Product> getAll(){
		return Ivy.repo().search(Product.class).limit(10000).execute().getAll();
	}

	@Override
	public void delete(Product product) {
		Ivy.repo().delete(product);
	}

	@Override
	public List<Product> callQueryWithLimit(Query<Product> query, int first, int pageSize) {
		return query.limit(first, pageSize).execute().getAll();
	}

	@Override
	public long getQueryRowCount(Query<Product> query) {
		return query.limit(0, 10000).execute().count();
	}
}
