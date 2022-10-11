package com.axonivy.market.statefuldatatable.ui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.primefaces.model.Visibility;
import org.primefaces.model.filter.FilterConstraint;

import com.axonivy.market.statefuldatatable.dao.DaoServiceRegistry;
import com.axonivy.market.statefuldatatable.entity.BusinessObject;
import com.axonivy.market.statefuldatatable.enums.BusinessObjectStatus;
import com.axonivy.market.statefuldatatable.enums.Quality;
import com.axonivy.market.statefuldatatable.service.DateService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ch.ivyteam.ivy.business.data.store.search.Filter;
import ch.ivyteam.ivy.business.data.store.search.OrderByOperation.Direction;
import ch.ivyteam.ivy.business.data.store.search.Query;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.DateTime;
import ch.ivyteam.ivy.security.IUser;
/**
 * This is the Lazy Data Model used in the primefaces datatable. The model has additional fields to store sorting and visibility data. 
 * There are also filters that are set for the first time when the datatable will load. After each load the sorting, filtering and visibility data are saved
 * into IUser data. 
 * 
 * @author david.merunko
 *
 */
public class BusinessObjectRepoLazyDataModel extends LazyDataModel<BusinessObject> implements BusinessObjectLazyDataModel {
	private static final long serialVersionUID = 1L;
	public static final String TABLE_LAZY_PREFIX = "form:businessObjectTable:";
	public static final String TABLE_UI_PATH = "form:businessObjectTable";
	//Adding Column STEP 3
	//Dropdown filters
	public static final String QUALITY_FILTER = "quality";
	public static final String BUSINESS_OBJECT_STATUS_FILTER = "businessObjectStatus";
	public static final String AVAILABILITY_FILTER = "availability";
	//Date filters
	public static final String VALID_THROUGH_FILTER = "validThrough";
	public static final String ORDER_DATE_FILTER = "orderDate";
	public static final String DELIVERY_DATE_FILTER = "deliveryDate";
	//Number range filter
	public static final String QUANTITY_FILTER = "quantity";
	//Text filter
	public static final String PRODUCT_NAME_FILTER = "productName";
	//Boolean filter
	public static final String ON_SALE_FILTER = "onSale";

	
	//Map that holds the initial filters loaded from IUser
	private Map<String, FilterMeta> filtersFromIUser;
	//List that holds the sorting data
	private List<SortMeta> sortBy = new ArrayList<>();
	//Map that holds the visibility of columns
	private Map<String, Boolean> columnVisibility = new HashMap<>();
	private int tableRows;
	
	private boolean useSavedFilters = true;
	private boolean useSavedSorting = true;
	private boolean showNotApplicable = true;
	private boolean tableDefault = true;
	
	@Override
    public BusinessObject getRowData(String rowKey) {
		return DaoServiceRegistry.getBusinessObjectRepoDAO().getById(rowKey);
		//return Ivy.repo().search(BusinessObject.class).textField("id").containsPhrase(rowKey).execute().getFirst();
    }

    @Override
    public String getRowKey(BusinessObject businessObject) {
        return String.valueOf(businessObject.getId());
    }
    
    /**
	 * Get filter Text from IUser filter data to show up in the UI
	 */
	public Map<String, Object> getFilterText() {
		Map<String, Object> filterFromIUserMap = new HashMap<>();
		if (filtersFromIUser != null) {
			//For each filter from the IUser we set the value of the column filter to its value. To work correctly we need the full DOM path
			for (Entry<String, FilterMeta> filter : filtersFromIUser.entrySet()) {
				filterFromIUserMap.put(filter.getKey(), filter.getValue().getFilterValue());
			}
		}
		
		return filterFromIUserMap;
	}
	


	@Override
	public int count(Map<String, FilterMeta> filterBy) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * Main load method for the lazy data model. At the begining we set the filters from IUser the first time tabl is loaded. At the end, we save
	 * filter and sorting.
	 */
	@Override
	public List<BusinessObject> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
		//First time the table is loaded, we use the filters from IUser
		if (filterBy.isEmpty() && sortBy.size() == 2 && !columnVisibility.containsValue(false)
				&& sortBy.get(PRODUCT_NAME_FILTER) != null
				&& sortBy.get(PRODUCT_NAME_FILTER).getOrder() == SortOrder.DESCENDING
				&& sortBy.get(VALID_THROUGH_FILTER) != null
				&& sortBy.get(VALID_THROUGH_FILTER).getOrder() == SortOrder.DESCENDING) {
			tableDefault = true;
		} else {
			tableDefault = false;
		}

		//First time the table is loaded, we use the filters from IUser
		if(useSavedFilters && filtersFromIUser != null) {
			filterBy = filtersFromIUser;
			useSavedFilters = false;
		}
		
		if(this.sortBy != null && useSavedSorting) {
			sortBy.clear();
			for(SortMeta sortMeta : this.sortBy) {
				sortBy.put(sortMeta.getField(), sortMeta);
			}
			useSavedSorting = false;
		}
		
		//Setup for business data repo query
		Query<BusinessObject> query = Ivy.repo().search(BusinessObject.class);
		//Filtering based on the table columns
		//Adding Column STEP 4
		addStringContainsQueryFilter(query, PRODUCT_NAME_FILTER, filterBy);

		addNumberRangeQueryFilter(query, QUANTITY_FILTER, filterBy);
		
		addDateRangeQueryFilter(query, VALID_THROUGH_FILTER, filterBy, false);
		addDateRangeQueryFilter(query, ORDER_DATE_FILTER, filterBy, false);
		addDateRangeQueryFilter(query, DELIVERY_DATE_FILTER, filterBy, false);
		
		addSelectOneMenuQueryFilter(query, AVAILABILITY_FILTER, filterBy);
		addSelectOneMenuQueryFilter(query, QUALITY_FILTER, filterBy);
		addSelectOneMenuQueryFilter(query, BUSINESS_OBJECT_STATUS_FILTER, filterBy);
		
		addBooleanQueryFilter(query, ON_SALE_FILTER, filterBy);
		
		//Ordering with Sort Meta
		if(sortBy != null) {
			for(Entry<String, SortMeta> sortMeta : sortBy.entrySet()) {
				if(sortMeta.getValue().getOrder() == SortOrder.UNSORTED) {
					continue;
				}
				if(PRODUCT_NAME_FILTER.equals(sortMeta.getValue().getField())) {
					query.orderBy().textField(sortMeta.getValue().getField()).direction(Direction.valueOf(sortMeta.getValue().getOrder().name()));
				} else {
					query.orderBy().field(sortMeta.getValue().getField()).direction(Direction.valueOf(sortMeta.getValue().getOrder().name()));
				}
			}
		}
		
		List<BusinessObject> result = DaoServiceRegistry.getBusinessObjectRepoDAO().callQueryWithLimit(query, first, pageSize);
		setRowCount((int) DaoServiceRegistry.getBusinessObjectRepoDAO().getQueryRowCount(query));
		//Saving the filters and sorting
		saveFilterStateAndvalueIntoIUser(filterBy);
		saveSortMetaIntoIUser(sortBy);
		saveTableRowsIntoIUser();
		
		filtersFromIUser = filterBy;
		for(Entry<String, SortMeta> sortMeta : sortBy.entrySet()) {
			this.sortBy.add(sortMeta.getValue());
		}
		
		return result;
	}
	
	public BusinessObject updateBusinessObject(BusinessObject businessObject, List<Object> newObjects,
			String field) throws InterruptedException {
		//Adding Column STEP 5
		if (field.contains("productName")) {
			businessObject.setProductName((String) newObjects.get(0));
		} else if (field.contains("businessObjectStatus")) {
			businessObject.setBusinessObjectStatus((BusinessObjectStatus) newObjects.get(0));
		} else if (field.contains("validThrough")) {
			DateTime dateTime = (DateTime) newObjects.get(0);
			businessObject.setValidThrough(dateTime.toJavaDate());
		} else if (field.contains("orderDate")) {
			DateTime dateTime = (DateTime) newObjects.get(0);
			//Example validation
			if(dateTime.toJavaDate().after(businessObject.getDeliveryDate())) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Ivy.cms().co("/Dialogs/com/axonivy/market/statefuldatatable/ui/StatefulDatatable/Error/deliveryDateBeforeOrderDate"), null));
				return businessObject;
			}
			businessObject.setOrderDate(dateTime.toJavaDate());
		} else if (field.contains("deliveryDate")) {
			DateTime dateTime = (DateTime) newObjects.get(0);
			//Example validation
			if(dateTime.toJavaDate().before(businessObject.getOrderDate())) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Ivy.cms().co("/Dialogs/com/axonivy/market/statefuldatatable/ui/StatefulDatatable/Error/deliveryDateBeforeOrderDate"), null));
				return businessObject;
			}
			businessObject.setDeliveryDate(dateTime.toJavaDate());
		} else if (field.contains("quantity")) {
			businessObject.setQuantity((Integer) newObjects.get(0));
		} else if (field.contains("quality")) {
			businessObject.setQuality((Quality) newObjects.get(0));
		} else if (field.contains("onSale")) {
			businessObject.setOnSale((Boolean) newObjects.get(0));
		}

		DaoServiceRegistry.getBusinessObjectRepoDAO().save(businessObject);

		Thread.sleep(1000);

		return businessObject;
	}
	
	public void deleteOrder(BusinessObject businessObject) {
		DaoServiceRegistry.getBusinessObjectRepoDAO().delete(businessObject);
	}
	
	public void saveBusinessObject(BusinessObject businessObject) {
		DaoServiceRegistry.getBusinessObjectRepoDAO().save(businessObject);
	}	
	
	private void addSelectOneMenuQueryFilter(Query<BusinessObject> query, String filterName, Map<String, FilterMeta> filters) {
		if(filters.get(filterName) != null) {
			if(filters.get(filterName).getFilterValue() instanceof ArrayList) {
				ArrayList<String> statusArray = (ArrayList<String>) filters.get(filterName).getFilterValue();
				if(!statusArray.isEmpty()) {
					String words = "";
					for(String status : statusArray) {
						words += "*" + status + "* ";
					}
					query.textField(filterName).containsAnyWords(words);
				}
			}
			if(filters.get(filterName).getFilterValue() instanceof Object[]) {
				Object[] statusArray = (Object[]) filters.get(filterName).getFilterValue();
				if(statusArray.length != 0 && statusArray[0] != null) {
					String words = "";
					for(Object state : statusArray) {
						words += "*" + ((Enum<?>) state).name() + "* ";
					}
					query.textField(filterName).containsAnyWords(words);
				}
			}
			
		}
	}
	
	private void addBooleanQueryFilter(Query<BusinessObject> query, String filterName, Map<String, FilterMeta> filters) {
		if(filters.get(filterName) != null) {
			if(filters.get(filterName).getFilterValue() instanceof Object[]) {
				Object[] statusArray = (Object[]) filters.get(filterName).getFilterValue();
				if(statusArray.length != 0 && statusArray[0] != null) {
					Filter<BusinessObject> userFilter = null;
					for(Object state : statusArray) {
						if(userFilter == null) {
							userFilter = Ivy.repo().search(BusinessObject.class).booleanField(filterName).isEqualTo((Boolean) state);
						} else {
							userFilter.or().booleanField(filterName).isEqualTo((Boolean) state);
						}
					}
					query.filter(userFilter);
				}
			}
			
		}
	}
	
	private void addNumberRangeQueryFilter(Query<BusinessObject> query, String filterName, Map<String, FilterMeta> filters) {
		if(filters.get(filterName) != null) {
			String[] amounts = filters.get(filterName).getFilterValue().toString().split("-");
			if(amounts.length > 1 && amounts[0].length() > 0 && amounts[1].length() > 0) {
				Double amountFrom = Double.parseDouble(amounts[0]);
				Double amountTo = Double.parseDouble(amounts[1]);
				query.numberField(filterName).isGreaterOrEqualTo(amountFrom).and().numberField(filterName).isLessOrEqualTo(amountTo);
			} else {
				try {
					Double number = Double.parseDouble(filters.get(filterName).getFilterValue().toString().replace("-", ""));
					query.numberField(filterName).isBetween(number-0.01, number+0.01);
				} catch(NumberFormatException e) {
					Ivy.log().error("Filterwert ist keine Zahl");
				}
			}
		}
	}
	
	private void addStringContainsQueryFilter(Query<BusinessObject> query, String filterName, Map<String, FilterMeta> filters) {
		if(filters.get(filterName) != null) {
			query.textField(filterName).containsAllWordPatterns("*" + filters.get(filterName).getFilterValue().toString() + "*");
		}
	}
	
	private void addDateRangeQueryFilter(Query<BusinessObject> query, String filterName, Map<String, FilterMeta> filters, boolean checkStatus) {
		if(filters.get(filterName) != null) {			
			List<LocalDate> dateRange = (ArrayList<LocalDate>) filters.get(filterName).getFilterValue();
			if(dateRange.size() > 1) {
				Filter<BusinessObject> dateFilter = Ivy.repo().search(BusinessObject.class).dateTimeField(filterName).isAfterOrEqualTo(DateService.setTimeZero(dateRange.get(0))).and().dateTimeField(filterName).isBeforeOrEqualTo(DateService.setTimeMidnight(dateRange.get(1)));
				query.filter(dateFilter);
			}
		}
	}
	
	/**
	 * Save column visibility Values into IUser as JSON
	 */
	public void saveColumnVisibilityToIUser(ToggleEvent e) {
		if(e != null) {
			DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(TABLE_UI_PATH);
			columnVisibility.put(dataTable.getColumns().get((Integer) e.getData()).getColumnKey().replace(TABLE_LAZY_PREFIX, ""), e.getVisibility() == Visibility.VISIBLE);
		}
		
		Gson objGson = new GsonBuilder().setPrettyPrinting().create();
		String visibilityToJson = objGson.toJson(columnVisibility);
		
		IUser currentUser = Ivy.session().getSessionUser();
		currentUser.setProperty("COLUMN_VISIBILITY", visibilityToJson);
	}
	
	public abstract class FilterMixin {
		@JsonIgnore
		ValueExpression filterBy;
		
		@JsonIgnore
		FilterConstraint constraint;
		
		@JsonIgnore
		abstract boolean isActive();
		
		@JsonIgnore
		abstract boolean isGlobalFilter();
	}
	
	/**
	 * Save filter values into IUser as JSON
	 * @throws JsonProcessingException 
	 */
	private void saveFilterStateAndvalueIntoIUser(Map<String, FilterMeta> filters) {
		Map<String, Map<String, FilterMeta>> searchFilterMap = new HashMap<>();
		searchFilterMap.put("searchFilters", filters);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());		
		mapper.addMixIn(FilterMeta.class, FilterMixin.class);
		JsonNode jsonNode = mapper.valueToTree(filters);
		String mapToJson = "";
		
		try {
			mapToJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
		} catch (JsonProcessingException e) {
			Ivy.log().error("Couldn't parse filter map to json");
		}
		
		IUser currentUser = Ivy.session().getSessionUser();
		currentUser.setProperty("FILTER_SEARCH", mapToJson);
	}
	
	/**
	 * Save rows number into IUser as property
	 */
	private void saveTableRowsIntoIUser() {
		IUser currentUser = Ivy.session().getSessionUser();
		currentUser.setProperty("TABLE_ROWS", "" + tableRows);
	}
	
	public abstract class SortMixin {
		@JsonIgnore
		ValueExpression sortBy;
		
		@JsonIgnore
		abstract boolean isActive();
	}
	
	/**
	 * Save sorting values into IUser as JSON
	 */
	private void saveSortMetaIntoIUser(Map<String, SortMeta> multiSortMeta) {
		Map<String, Map<String, SortMeta>> sortMap = new HashMap<>();
		sortMap.put("searchSort", multiSortMeta);

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.addMixIn(SortMeta.class, SortMixin.class);
		JsonNode jsonNode = mapper.valueToTree(multiSortMeta);
		String mapToJson = "";
		
		try {
			mapToJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
		} catch (JsonProcessingException e) {
			Ivy.log().error("Couldn't parse filter map to json");
		}

		IUser currentUser = Ivy.session().getSessionUser();
		currentUser.setProperty("SORT_SEARCH", mapToJson);
	}

	/**
	 * @return the defaultFilter
	 */
	public Map<String, FilterMeta> getFiltersFromIUser() {
		return filtersFromIUser;
	}

	/**
	 * @param filtersFromIUser the defaultFilter to set
	 */
	public void setFiltersFromIUser(Map<String, FilterMeta> filtersFromIUser) {
		this.filtersFromIUser = filtersFromIUser;
	}

	/**
	 * @return the sortBy
	 */
	public List<SortMeta> getSortBy() {
		return sortBy;
	}

	/**
	 * @param sortBy the sortBy to set
	 */
	public void setSortBy(List<SortMeta> sortBy) {
		this.sortBy = sortBy;
	}

	/**
	 * @return the columnVisibility
	 */
	public Map<String, Boolean> getColumnVisibility() {
		return columnVisibility;
	}

	/**
	 * @param columnVisibility the columnVisibility to set
	 */
	public void setColumnVisibility(Map<String, Boolean> columnVisibility) {
		this.columnVisibility = columnVisibility;
	}

	/**
	 * @return the useSavedFilters
	 */
	public boolean isUseSavedFilters() {
		return useSavedFilters;
	}

	/**
	 * @param useSavedFilters the useSavedFilters to set
	 */
	public void setUseSavedFilters(boolean useSavedFilters) {
		this.useSavedFilters = useSavedFilters;
	}

	/**
	 * @return the useSavedSorting
	 */
	public boolean isUseSavedSorting() {
		return useSavedSorting;
	}

	/**
	 * @param useSavedSorting the useSavedSorting to set
	 */
	public void setUseSavedSorting(boolean useSavedSorting) {
		this.useSavedSorting = useSavedSorting;
	}

	/**
	 * @return the showNotApplicable
	 */
	public boolean isShowNotApplicable() {
		return showNotApplicable;
	}

	/**
	 * @param showNotApplicable the showNotApplicable to set
	 */
	public void setShowNotApplicable(boolean showNotApplicable) {
		this.showNotApplicable = showNotApplicable;
	}

	/**
	 * @return the tableDefault
	 */
	public boolean isTableDefault() {
		return tableDefault;
	}

	/**
	 * @param tableDefault the tableDefault to set
	 */
	public void setTableDefault(boolean tableDefault) {
		this.tableDefault = tableDefault;
	}

	/**
	 * @return the tableRows
	 */
	public int getTableRows() {
		return tableRows;
	}

	/**
	 * @param tableRows the tableRows to set
	 */
	public void setTableRows(int tableRows) {
		this.tableRows = tableRows;
	}
}
