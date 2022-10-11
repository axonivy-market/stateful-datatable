package com.axonivy.market.statefuldatatable.dao;

import java.util.List;

import com.axonivy.market.statefuldatatable.entity.BusinessObject;

import ch.ivyteam.ivy.business.data.store.search.Query;
import ch.ivyteam.ivy.environment.Ivy;

public class BusinessObjectRepoDAO implements IBusinessObjectDAO {

	public BusinessObject save(BusinessObject businessObject) {				
		Ivy.repo().save(businessObject);
		return businessObject;
	}
	
	public BusinessObject getById(String id) {
		return Ivy.repo().find(id, BusinessObject.class);
	};
	
	public List<BusinessObject> getAll(){
		return Ivy.repo().search(BusinessObject.class).limit(10000).execute().getAll();
	}
	
	public void delete(BusinessObject businessObject) {
		Ivy.repo().delete(businessObject);
	}
	
	public List<BusinessObject> callQueryWithLimit(Query<BusinessObject> query, int first, int pageSize) {
		return query.limit(first, pageSize).execute().getAll();
	}
	
	public long getQueryRowCount(Query<BusinessObject> query) {
		return query.limit(0, 10000).execute().count();
	}
}
