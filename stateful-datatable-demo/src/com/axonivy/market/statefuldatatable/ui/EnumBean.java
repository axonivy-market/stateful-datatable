package com.axonivy.market.statefuldatatable.ui;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import com.axonivy.market.statefuldatatable.enums.Availability;
import com.axonivy.market.statefuldatatable.enums.ProductStatus;
import com.axonivy.market.statefuldatatable.enums.Quality;

@ManagedBean(name="enumBean")
@ApplicationScoped
public class EnumBean {
	public ProductStatus[] getProductStatusValues() {
		return ProductStatus.values();
	}
	
	public Quality[] getQualityValues() {
		return Quality.values();
	}

	public Availability[] getAvailabilitylValues() {
		return Availability.values();
	}
}
