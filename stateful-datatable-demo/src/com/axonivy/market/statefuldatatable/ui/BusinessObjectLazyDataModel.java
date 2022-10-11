package com.axonivy.market.statefuldatatable.ui;

import java.util.List;
import java.util.Map;

import org.primefaces.event.ToggleEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;

import com.axonivy.market.statefuldatatable.entity.BusinessObject;

public interface BusinessObjectLazyDataModel {
	public BusinessObject getRowData(String rowKey);
	public String getRowKey(BusinessObject businessObject);
	public Map<String, Object> getFilterText();
	public List<BusinessObject> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy);
	public int count(Map<String, FilterMeta> filterBy);
	public void saveColumnVisibilityToIUser(ToggleEvent e);
	public BusinessObject updateBusinessObject(BusinessObject businessObject, List<Object> newObjects, String field) throws InterruptedException;
	public void deleteOrder(BusinessObject businessObject);
	public void saveBusinessObject(BusinessObject businessObject);
	public Map<String, FilterMeta> getFiltersFromIUser();
	public void setFiltersFromIUser(Map<String, FilterMeta> filtersFromIUser);
	public List<SortMeta> getSortBy();
	public void setSortBy(List<SortMeta> sortBy);
	public Map<String, Boolean> getColumnVisibility();
	public void setColumnVisibility(Map<String, Boolean> columnVisibility);
	public boolean isUseSavedFilters();
	public void setUseSavedFilters(boolean useSavedFilters);
	public boolean isUseSavedSorting();
	public void setUseSavedSorting(boolean useSavedSorting);
	public boolean isShowNotApplicable();
	public void setShowNotApplicable(boolean showNotApplicable);
	public boolean isTableDefault();
	public void setTableDefault(boolean tableDefault);
	public int getTableRows();
	public void setTableRows(int tableRows);
}
