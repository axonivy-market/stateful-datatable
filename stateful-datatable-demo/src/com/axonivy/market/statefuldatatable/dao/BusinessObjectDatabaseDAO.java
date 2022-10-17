package com.axonivy.market.statefuldatatable.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;

import com.axonivy.market.statefuldatatable.entity.BusinessObject;

public class BusinessObjectDatabaseDAO extends AbstractEntityDAO<BusinessObject> implements IBusinessObjectDAO  {
	@Override
	public Class<BusinessObject> getType() {
		return BusinessObject.class;
	}
	
	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		return super.getCriteriaBuilder();
	}

	@Override
	public long getQueryRowCount(ch.ivyteam.ivy.business.data.store.search.Query<BusinessObject> query) {
		return 0;
	}

	@Override
	public List<BusinessObject> callQueryWithLimit(ch.ivyteam.ivy.business.data.store.search.Query<BusinessObject> query, int first, int pageSize) {
		return null;
	}
}
