package com.axonivy.market.statefuldatatable.dao;

import java.util.List;

import com.axonivy.market.statefuldatatable.entity.BusinessObject;

import ch.ivyteam.ivy.business.data.store.search.Query;

public interface IBusinessObjectDAO {
	public BusinessObject save(BusinessObject businessObject);	
	public BusinessObject getById(String id);	
	public List<BusinessObject> getAll();
	public void delete(BusinessObject businessObject);
	public List<BusinessObject> callQueryWithLimit(Query<BusinessObject> query, int first, int pageSize);	
	public long getQueryRowCount(Query<BusinessObject> query);
}
