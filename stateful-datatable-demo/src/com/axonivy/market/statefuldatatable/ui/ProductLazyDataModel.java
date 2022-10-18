package com.axonivy.market.statefuldatatable.ui;

import java.util.List;
import java.util.Map;

import org.primefaces.event.ToggleEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;

import com.axonivy.market.statefuldatatable.entity.Product;

public interface ProductLazyDataModel {
	public Product getRowData(String rowKey);
	public String getRowKey(Product product);
	public Map<String, Object> getFilterText();
	public List<Product> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy);
	public int count(Map<String, FilterMeta> filterBy);
	public void saveColumnVisibilityToIUser(ToggleEvent e);
	public Product updateProduct(Product product, List<Object> newObjects, String field) throws InterruptedException;
	public void deleteOrder(Product product);
	public void saveProduct(Product product);
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
