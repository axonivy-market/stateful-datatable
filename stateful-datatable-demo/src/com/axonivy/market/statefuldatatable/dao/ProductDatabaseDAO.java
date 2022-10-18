package com.axonivy.market.statefuldatatable.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;

import com.axonivy.market.statefuldatatable.entity.Product;

public class ProductDatabaseDAO extends AbstractEntityDAO<Product> implements IProductDAO  {
	@Override
	public Class<Product> getType() {
		return Product.class;
	}
	
	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		return super.getCriteriaBuilder();
	}

	@Override
	public long getQueryRowCount(ch.ivyteam.ivy.business.data.store.search.Query<Product> query) {
		return 0;
	}

	@Override
	public List<Product> callQueryWithLimit(ch.ivyteam.ivy.business.data.store.search.Query<Product> query, int first, int pageSize) {
		return null;
	}
}
