package com.axonivy.demo.statefuldatatable.ui;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import com.axonivy.demo.statefuldatatable.entity.Product;
import com.axonivy.demo.statefuldatatable.entity.Profile;
import com.axonivy.demo.statefuldatatable.enums.Availability;
import com.axonivy.demo.statefuldatatable.enums.ProductStatus;
import com.axonivy.demo.statefuldatatable.enums.ProfileType;
import com.axonivy.demo.statefuldatatable.enums.Quality;
import com.axonivy.demo.statefuldatatable.service.ProfileService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.security.IUser;
/**
 * This class server as an underlying bean for the table UI. It loads filter, sort and column data from the user and provides them to the lazy data model.
 * 
 * @author david.merunko
 *
 */
public class StateDataTableBean {
	public static final String DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";
	public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);
	
	private ProductLazyDataModel lazyModel = new ProductRepoLazyDataModel();
	private List<Profile> publicProfiles;
	private List<Profile> privateProfiles;
	private Profile selectedProfile = null;
	private Profile newProfile = new Profile();
	private boolean profileChanged = false;
	private Profile profileToDelete;
	private Product selectedProduct = new Product();
	private List<ProductStatus> productStatusList = new ArrayList<ProductStatus>();

	/**
	 * In the postconstruct method filter, sort and column data are loaded
	 */
	public void init() {
		publicProfiles = Ivy.repo().search(Profile.class).textField("profileType").containsPhrase(ProfileType.ALL_USERS.name()).limit(10000).execute().getAll();
		privateProfiles = Ivy.repo().search(Profile.class).textField("profileType").containsPhrase(ProfileType.ONLY_ME.name()).and().textField("user").containsPhrase(Ivy.session().getSessionUserName()).limit(10000).execute().getAll();
		restoreDataFromSessionUser();
	}

	/**
	 * Method that take IUsers "FILTER_SEARCH" property and extract a map of with the column name as a key and filter value as value.
	 * The map value is a String entered into the filter, with the exception of DatePicker, where it is array list of string values
	 * representing Date objects, these need to be parsed to Date object and Status filter which uses checkbox select and also provides
	 * array of values, since these are array of objects, you need to differentiate them by their key.
	 * The same map that is provided as a parameter to the load method of a lazy data model. 
	 * 
	 * @return Map of filters
	 */
	private Map<String, FilterMeta> getFilterStateFromIUser() {
		IUser currentUser = Ivy.session().getSessionUser();
		//Get the property "FILTER_SEARCH" from the IUser which holds the JSON representation of the filters
		String filterSearch = currentUser.getProperty("FILTER_SEARCH");
		Map<String, FilterMeta> filters = null;
		if (StringUtils.isNotEmpty(filterSearch)) {
			//Use Gson to convert JSON into a Map
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			try {
				filters = mapper.readValue(filterSearch, new TypeReference<Map<String, FilterMeta>>(){}); // converts JSON to Map
			} catch(JsonProcessingException e) {
				Ivy.log().error("Couldn't parse JSON into map", e);
			}
			
			if(filters == null || filters.isEmpty()) {
				return null;
			}
			
			//If the value is an arraylist it is the datePicker filter which needs to be parsed to date
			for(Entry<String, FilterMeta> filter : filters.entrySet()) {
				if(filter.getValue().getFilterValue() instanceof ArrayList) {
					//Adding Column STEP 6
					if(ProductRepoLazyDataModel.QUALITY_FILTER.equals(filter.getKey())) {
						setDropdownFilterValue(filter, ((ArrayList<?>) filter.getValue().getFilterValue()).size(), Quality.class);
					} else if(ProductRepoLazyDataModel.BUSINESS_OBJECT_STATUS_FILTER.equals(filter.getKey())) {
						setDropdownFilterValue(filter, ((ArrayList<?>) filter.getValue().getFilterValue()).size(), ProductStatus.class);
					} else if(ProductRepoLazyDataModel.AVAILABILITY_FILTER.equals(filter.getKey())) {
						setDropdownFilterValue(filter, ((ArrayList<?>) filter.getValue().getFilterValue()).size(), Availability.class);
					} else {
						setDateFilterValue(filter, 
								ProductRepoLazyDataModel.VALID_THROUGH_FILTER,
								ProductRepoLazyDataModel.ORDER_DATE_FILTER,
								ProductRepoLazyDataModel.DELIVERY_DATE_FILTER);
					}
				}
			}
		}

		return filters;
	}
	
	private void setDropdownFilterValue(Entry<String, FilterMeta> filter, int filterSize, Class clazz) {
		Object[] resultObjectArray = new Object[filterSize];
		List<String> statusList = (List<String>) filter.getValue().getFilterValue();
		for(int i = 0; i < statusList.size(); i++) {
			resultObjectArray[i] = Enum.valueOf(clazz, statusList.get(i));
		}
		filter.getValue().setFilterValue(resultObjectArray);
	}
	
	private void setDateFilterValue(Entry<String, FilterMeta> filter, String... filterNames) {
		for(String filterName : filterNames) {
			if(filterName.equals(filter.getKey())) {
				List<LocalDate> dateRange = new ArrayList<>();
				for(List<Integer> date : (List<List<Integer>>) filter.getValue().getFilterValue()) {
					dateRange.add(LocalDate.of(date.get(0), date.get(1), date.get(2)));
				}
				filter.getValue().setFilterValue(dateRange);
				break;
			}
		}
	}
	
	class ValueExpressionEntityDeserializer extends JsonDeserializer<ValueExpression> {
	    @Override
	    public ValueExpression deserialize(JsonParser p, DeserializationContext ctxt)
	                throws IOException, JsonProcessingException {
	       return null;
	    }
	}

	/**
	 * Method that take IUsers "SORT_SEARCH" property and extract a list of maps where each map has a sortField and sortOrder keys, that holds the name and sort order as values.
	 * The map is then parsed into a SortMeta object that is used in lazy data model for sorting. 
	 * 
	 * @return List of SortMeta objects
	 */
	private Map<String, SortMeta> getSortMetaFromIUser() {
		IUser currentUser = Ivy.session().getSessionUser();
		//Get the property "SORT_SEARCH" from the IUser which holds the JSON representation of the sorting meta
		String sortSearch = currentUser.getProperty("SORT_SEARCH");
		Map<String, SortMeta> sortMetaMap = new HashMap<>();

		if (StringUtils.isNotEmpty(sortSearch)) {
			// Use Jackson to convert JSON into a Map
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			try {
				sortMetaMap = mapper.readValue(sortSearch, new TypeReference<Map<String, SortMeta>>(){}); // converts JSON to Map
			} catch(JsonProcessingException e) {
				Ivy.log().error("Couldn't parse JSON into map", e);
			}
		}

		return sortMetaMap;
	}
	
	/**
	 * Returns the visibility data for table columns from the IUser property COLUMN_VISIBILITY. The property holds an ordered list of booleans that represent the visibility. 
	 * The order of the list corresponds to the order of the columns. 
	 * 
	 * @return Ordered list of visibility booleans
	 */
	private Map<String, Boolean> getColumnVisibilityFromIUser() {
		IUser currentUser = Ivy.session().getSessionUser();
		String columnVisibilityJson = currentUser.getProperty("COLUMN_VISIBILITY");
		Map<String, Boolean> columnVisibility = new HashMap<>();

		if (StringUtils.isNotEmpty(columnVisibilityJson)) {
			Gson objGson = new GsonBuilder().create();
			Type mapType = new TypeToken<Map<String, Boolean>>() {}.getType();
			columnVisibility = objGson.fromJson(columnVisibilityJson, mapType);
		}
		
		DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(ProductRepoLazyDataModel.TABLE_UI_PATH);
		for(UIColumn column : dataTable.getColumns()) {
			if(!columnVisibility.containsKey(column.getColumnKey().replace(ProductRepoLazyDataModel.TABLE_LAZY_PREFIX, ""))) {
				columnVisibility.put(column.getColumnKey().replace(ProductRepoLazyDataModel.TABLE_LAZY_PREFIX, ""), true);
			}
		}
		
		return columnVisibility;
	}

	/**
	 * Restore data from IUser session and set to model
	 */
	public void restoreDataFromSessionUser() {		
		lazyModel.setFiltersFromIUser(getFilterStateFromIUser());
		
		Map<String, SortMeta> sortMetaMap = getSortMetaFromIUser();
		
		if(sortMetaMap == null || sortMetaMap.isEmpty()) {
			SortMeta customerSort = SortMeta.builder().field(ProductRepoLazyDataModel.PRODUCT_NAME_FILTER).order(SortOrder.DESCENDING).build();
			SortMeta testDateSort = SortMeta.builder().field(ProductRepoLazyDataModel.VALID_THROUGH_FILTER).order(SortOrder.DESCENDING).build();

			sortMetaMap.put(ProductRepoLazyDataModel.PRODUCT_NAME_FILTER, customerSort);
			sortMetaMap.put(ProductRepoLazyDataModel.VALID_THROUGH_FILTER, testDateSort);
		}
		
		List<SortMeta> sortMetaList = new ArrayList<>();
		
		for(Entry<String, SortMeta> sortMeta : sortMetaMap.entrySet()) {
			UIViewRoot viewRoot =  FacesContext.getCurrentInstance().getViewRoot();
	        UIComponent column = viewRoot.findComponent(ProductRepoLazyDataModel.TABLE_LAZY_PREFIX + sortMeta.getValue().getField().replace("\\.", "_") + "Column");
	        SortMeta currentSortMeta = SortMeta.of(FacesContext.getCurrentInstance(), ProductRepoLazyDataModel.TABLE_UI_PATH, (UIColumn) column);
	        currentSortMeta.setOrder(sortMeta.getValue().getOrder());
	        currentSortMeta.setPriority(sortMeta.getValue().getPriority());
	        sortMetaList.add(currentSortMeta);
		}
			
		lazyModel.setSortBy(sortMetaList);
		lazyModel.setColumnVisibility(getColumnVisibilityFromIUser());
		
		String profileName = Ivy.session().getSessionUser().getProperty("CURRENT_PROFILE_NAME");
		if(profileName != null && !profileName.isBlank()) {
			for(Profile profile : publicProfiles) {
				if(profile.getProfileName().equals(profileName)) {
					selectedProfile = profile;
				}
			}
			
			for(Profile profile : privateProfiles) {
				if(profile.getProfileName().equals(profileName)) {
					selectedProfile = profile;
				}
			}
		}
		
		int tableRows = Ivy.session().getSessionUser().getProperty("TABLE_ROWS") != null ? Integer.parseInt(Ivy.session().getSessionUser().getProperty("TABLE_ROWS")) : 10;
		lazyModel.setTableRows(tableRows);
		
		PrimeFaces.current().executeScript("PF('productTable').filter()");
	}
	
	public void onRefresh() {		
		lazyModel.setUseSavedFilters(true);
		lazyModel.setUseSavedSorting(true);
	}
	
	public void resetTableState() {
		publicProfiles = Ivy.repo().search(Profile.class).textField("profileType").containsPhrase(ProfileType.ALL_USERS.name()).execute().getAll();
		privateProfiles = Ivy.repo().search(Profile.class).textField("profileType").containsPhrase(ProfileType.ONLY_ME.name()).and().textField("user").containsPhrase(Ivy.session().getSessionUserName()).execute().getAll();
		
		selectedProfile = null;

		lazyModel.getFiltersFromIUser().clear();
		lazyModel.getSortBy().clear();

		UIViewRoot viewRoot =  FacesContext.getCurrentInstance().getViewRoot();
		DataTable dataTable = (DataTable) viewRoot.findComponent(ProductRepoLazyDataModel.TABLE_UI_PATH);
		//Check
		dataTable.reset();
		
		List<SortMeta> sortMetaList = new ArrayList<>();

		SortMeta customerSort = SortMeta.builder().field(ProductRepoLazyDataModel.PRODUCT_NAME_FILTER).order(SortOrder.DESCENDING).build();
		SortMeta testDateSort = SortMeta.builder().field(ProductRepoLazyDataModel.VALID_THROUGH_FILTER).order(SortOrder.DESCENDING).build();
		
		sortMetaList.add(customerSort);
		sortMetaList.add(testDateSort);
		
		for(SortMeta sortMeta : sortMetaList) {
	        UIComponent column = viewRoot.findComponent(ProductRepoLazyDataModel.TABLE_LAZY_PREFIX + sortMeta.getField().replace("\\.", "_") + "Column");
	        SortMeta currentSortMeta = SortMeta.of(FacesContext.getCurrentInstance(), ProductRepoLazyDataModel.TABLE_UI_PATH, (UIColumn) column);
	        currentSortMeta.setOrder(sortMeta.getOrder());
	        currentSortMeta.setPriority(sortMeta.getPriority());
	        sortMeta = currentSortMeta;
		}
			
		lazyModel.setSortBy(sortMetaList);
	    
		
		Map<String, Boolean> columnVisibility = new HashMap<>();
		for(UIColumn column : dataTable.getColumns()) {
			if(!columnVisibility.containsKey(column.getColumnKey().replace(ProductRepoLazyDataModel.TABLE_LAZY_PREFIX, ""))) {
				columnVisibility.put(column.getColumnKey().replace(ProductRepoLazyDataModel.TABLE_LAZY_PREFIX, ""), true);
			}
		}
		lazyModel.setColumnVisibility(columnVisibility);
		lazyModel.setUseSavedFilters(true);
		lazyModel.setUseSavedSorting(true);
		
		Ivy.session().getSessionUser().setProperty("CURRENT_PROFILE_NAME",  "");
		lazyModel.saveColumnVisibilityToIUser(null);

		PrimeFaces.current().executeScript("PF('productTable').filter()");
	}
	
	/**
	 * Restores filtering, sorting and column visibility from chosen profile
	 * @param profile
	 */
	public void applyProfile(Profile profile) {
		selectedProfile = profile;
		profileChanged = true;
		lazyModel.setFiltersFromIUser(profile.getFilters());
		
		Map<String, SortMeta> sorting = new HashMap<>();
		sorting.putAll(profile.getSorting());
		
		UIViewRoot viewRoot =  FacesContext.getCurrentInstance().getViewRoot();
        
		List<SortMeta> sortingList = new ArrayList<>();
		
		for(Entry<String, SortMeta> sortMeta : sorting.entrySet()) {
	        UIComponent column = viewRoot.findComponent(ProductRepoLazyDataModel.TABLE_LAZY_PREFIX + sortMeta.getValue().getField().replace("\\.", "_") + "Column");
	        sortMeta.setValue(SortMeta.of(FacesContext.getCurrentInstance(), ProductRepoLazyDataModel.TABLE_UI_PATH, (UIColumn) column));
	        sortingList.add(sortMeta.getValue());
		}
		
		//Workaround for sorting not updating after profile apply if the user sorted before it
		DataTable dataTable = (DataTable) viewRoot.findComponent(ProductRepoLazyDataModel.TABLE_UI_PATH);
		//Check
		dataTable.setSortBy(lazyModel.getSortBy());
	    
		lazyModel.setSortBy(sortingList);
		lazyModel.getColumnVisibility().putAll(profile.getVisibility());
		lazyModel.setUseSavedFilters(true);
		lazyModel.setUseSavedSorting(true);
		
		Ivy.session().getSessionUser().setProperty("CURRENT_PROFILE_NAME",  selectedProfile.getProfileName());
		lazyModel.saveColumnVisibilityToIUser(null);
		
		PrimeFaces.current().executeScript("PF('productTable').filter()");
	}
	
	/**
	 * Saves current state of filtering, sorting and column visibility to a profile. The sortMeta must not have column set, as that
	 * causes problem.
	 *
	 */
	public void saveProfile() {
		final String profileName = newProfile.getProfileName();
		if(privateProfiles.stream().anyMatch(p -> p.getProfileName().equals(profileName))
				|| publicProfiles.stream().anyMatch(p -> p.getProfileName().equals(profileName))) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Profil mit gleichem Namen existiert bereits!", null)); 
			FacesContext.getCurrentInstance().validationFailed();
			return;
			
		}
		newProfile.setFilters(lazyModel.getFiltersFromIUser());
		
		Map<String, SortMeta> sorting = new HashMap<>();
		for(SortMeta sortMeta : lazyModel.getSortBy()) {
		sorting.put(sortMeta.getField(), sortMeta);
		}
		for(Entry<String, SortMeta> sortMeta : sorting.entrySet()) {
			sortMeta.getValue().setSortBy(null);
		}
		newProfile.setSorting(sorting);
		newProfile.setVisibility(lazyModel.getColumnVisibility());
		newProfile.setUser(Ivy.session().getSessionUserName());
		
		Ivy.repo().save(newProfile);
		
		if(newProfile.getProfileType() == ProfileType.ONLY_ME) {
			privateProfiles.add(ProfileService.copyProfile(newProfile));
		} else {
			publicProfiles.add(ProfileService.copyProfile(newProfile));
		}
		
		selectedProfile = ProfileService.copyProfile(newProfile);
		
		Ivy.session().getSessionUser().setProperty("CURRENT_PROFILE_NAME",  selectedProfile.getProfileName());
		newProfile = new Profile();
	}
	
	/**
	 * Deletes profile in business data
	 */
	public void deleteProfile() {
		if(profileToDelete != null && selectedProfile != null && profileToDelete.getProfileName().equals(selectedProfile.getProfileName())) {
			selectedProfile = null;
		}
		Ivy.repo().delete(profileToDelete);
		
		publicProfiles.remove(profileToDelete);
		privateProfiles.remove(profileToDelete);		
	}
	
	/**
	 * Saves column visibility to IUser.
	 * @param ToggleEvent
	 */
	public void columnVisibilityChanged(ToggleEvent e) {
		lazyModel.saveColumnVisibilityToIUser(e);
	}
	
	public String showProfileName() {
		if(selectedProfile != null) {
			return selectedProfile.getProfileName();
		} else if(lazyModel.isTableDefault()) {
			return "Default";
		} else {
			return "Benutzerdefiniertes Profil";
		} 
	}
	
	/**
	 * Resets profile shown in the table when filter, sorting or visibility is changed.
	 */
	public void resetProfile() {
		if(selectedProfile == null || profileChanged) {
			profileChanged = false;
			return;
		}
				
		if(selectedProfile.getFilters().size() != lazyModel.getFiltersFromIUser().size()
				|| selectedProfile.getSorting().size() != lazyModel.getSortBy().size()
				|| selectedProfile.getVisibility().size() != lazyModel.getColumnVisibility().size()) {
			selectedProfile = null;
			Ivy.session().getSessionUser().setProperty("CURRENT_PROFILE_NAME",  "");
			return;
		}
		
		for(Entry<String, FilterMeta> entry : selectedProfile.getFilters().entrySet()) {
			Object filter = lazyModel.getFiltersFromIUser().get(entry.getKey());
			if(filter instanceof String) {
				if(filter == null || !((String) filter).equals((String)entry.getValue().getFilterValue())) {
					selectedProfile = null;
					Ivy.session().getSessionUser().setProperty("CURRENT_PROFILE_NAME",  "");
					return;
				}
			}
			if(filter instanceof ArrayList) {
				ArrayList<?> profileFilter = (ArrayList<?>) entry.getValue().getFilterValue();
				ArrayList<?> modelFilter = (ArrayList<?>) filter;
				
				if(filter == null || profileFilter.size() != modelFilter.size()) {
					selectedProfile = null;
					Ivy.session().getSessionUser().setProperty("CURRENT_PROFILE_NAME",  "");
					return;
				}
				
				for(int i = 0; i < profileFilter.size(); i++) {
					if(!profileFilter.get(0).equals(modelFilter.get(0))) {
						selectedProfile = null;
						Ivy.session().getSessionUser().setProperty("CURRENT_PROFILE_NAME",  "");
						return;
					}
				}
			}
		}
		
		for(int i = 0; i < selectedProfile.getSorting().size(); i++) {
			if (!lazyModel.getSortBy().get(i).getField().equals(selectedProfile.getSorting().get(lazyModel.getSortBy().get(i).getField()).getField())
					|| lazyModel.getSortBy().get(i).getOrder() != selectedProfile.getSorting().get(lazyModel.getSortBy().get(i).getField()).getOrder()) {
				selectedProfile = null;
				Ivy.session().getSessionUser().setProperty("CURRENT_PROFILE_NAME",  "");
				return;
			}
		}
		
		for(Entry<String, Boolean> visibility : lazyModel.getColumnVisibility().entrySet()) {
			if (selectedProfile.getVisibility().get(visibility.getKey()) != visibility.getValue()) {
				selectedProfile = null;
				Ivy.session().getSessionUser().setProperty("CURRENT_PROFILE_NAME",  "");
				return;
			}
		}
	}

	/**
	 * @return the lazyModel
	 */
	public ProductLazyDataModel getLazyModel() {
		return lazyModel;
	}

	/**
	 * @param lazyModel the lazyModel to set
	 */
	public void setLazyModel(ProductLazyDataModel lazyModel) {
		this.lazyModel = lazyModel;
	}

	/**
	 * @return the publicProfiles
	 */
	public List<Profile> getPublicProfiles() {
		return publicProfiles;
	}

	/**
	 * @param publicProfiles the publicProfiles to set
	 */
	public void setPublicProfiles(List<Profile> publicProfiles) {
		this.publicProfiles = publicProfiles;
	}

	/**
	 * @return the privateProfiles
	 */
	public List<Profile> getPrivateProfiles() {
		return privateProfiles;
	}

	/**
	 * @param privateProfiles the privateProfiles to set
	 */
	public void setPrivateProfiles(List<Profile> privateProfiles) {
		this.privateProfiles = privateProfiles;
	}

	/**
	 * @return the selectedProfile
	 */
	public Profile getSelectedProfile() {
		return selectedProfile;
	}

	/**
	 * @param selectedProfile the selectedProfile to set
	 */
	public void setSelectedProfile(Profile selectedProfile) {
		this.selectedProfile = selectedProfile;
	}

	/**
	 * @return the profileToDelete
	 */
	public Profile getProfileToDelete() {
		return profileToDelete;
	}

	/**
	 * @param profileToDelete the profileToDelete to set
	 */
	public void setProfileToDelete(Profile profileToDelete) {
		this.profileToDelete = profileToDelete;
	}

	/**
	 * @return the newProfile
	 */
	public Profile getNewProfile() {
		return newProfile;
	}

	/**
	 * @param newProfile the newProfile to set
	 */
	public void setNewProfile(Profile newProfile) {
		this.newProfile = newProfile;
	}

	/**
	 * @return the distributrOrderStatusList
	 */
	public List<ProductStatus> getProductStatusList() {
		return productStatusList;
	}

	/**
	 * @param distributrOrderStatusList the distributrOrderStatusList to set
	 */
	public void setDistributrOrderStatusList(List<ProductStatus> productStatusList) {
		this.productStatusList = productStatusList;
	}

	/**
	 * @return the selectedDistributorOrder
	 */
	public Product getSelectedProduct() {
		return selectedProduct;
	}

	/**
	 * @param selectedDistributorOrder the selectedDistributorOrder to set
	 */
	public void setSelectedDistributorOrder(Product selectedProduct) {
		this.selectedProduct = selectedProduct;
	}

	/**
	 * @return the dateFormat
	 */
	public static String getDateFormat() {
		return DATE_FORMAT;
	}

	/**
	 * @return the dateFormatter
	 */
	public static SimpleDateFormat getDateFormatter() {
		return DATE_FORMATTER;
	}
}
