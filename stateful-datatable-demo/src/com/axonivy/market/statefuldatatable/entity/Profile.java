package com.axonivy.market.statefuldatatable.entity;

import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;

import com.axonivy.market.statefuldatatable.enums.ProfileType;

public class Profile {
	private String profileName;
	private ProfileType profileType;
	private String user;
	private Map<String, FilterMeta> filters;
	private Map<String, SortMeta> sorting;
	private Map<String, Boolean> visibility;

	/**
	 * @return the profileName
	 */
	public String getProfileName() {
		return profileName;
	}
	/**
	 * @param profileName the profileName to set
	 */
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}
	/**
	 * @return the profileType
	 */
	public ProfileType getProfileType() {
		return profileType;
	}
	/**
	 * @param profileType the profileType to set
	 */
	public void setProfileType(ProfileType profileType) {
		this.profileType = profileType;
	}
	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}
	/**
	 * @return the filters
	 */
	public Map<String, FilterMeta> getFilters() {
		return filters;
	}
	/**
	 * @param filters the filters to set
	 */
	public void setFilters(Map<String, FilterMeta> filters) {
		this.filters = filters;
	}
	/**
	 * @return the sorting
	 */
	public Map<String, SortMeta> getSorting() {
		return sorting;
	}
	/**
	 * @param sorting the sorting to set
	 */
	public void setSorting(Map<String, SortMeta> sorting) {
		this.sorting = sorting;
	}
	/**
	 * @return the visibility
	 */
	public Map<String, Boolean> getVisibility() {
		return visibility;
	}
	/**
	 * @param visibility the visibility to set
	 */
	public void setVisibility(Map<String, Boolean> visibility) {
		this.visibility = visibility;
	}
	
}
