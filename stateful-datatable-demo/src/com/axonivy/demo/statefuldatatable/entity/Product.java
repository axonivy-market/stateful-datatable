/**
 * 
 */
package com.axonivy.demo.statefuldatatable.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import com.axonivy.demo.statefuldatatable.enums.Availability;
import com.axonivy.demo.statefuldatatable.enums.ProductStatus;
import com.axonivy.demo.statefuldatatable.enums.Quality;


@Entity
public class Product extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(length = 32, nullable = false) 
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;
	//Adding Column STEP 1
	@Column
	private Date validThrough;
	
	@Column
	private Date orderDate;
	
	@Column
	private Date deliveryDate;
	
	@Column
	private Integer quantity;

	@Column
	@Enumerated(EnumType.STRING)
	private Quality quality;
	
	@Column
	@Enumerated(EnumType.STRING)
	private Availability availability;
	
	@Column
	@Enumerated(EnumType.STRING)
	private ProductStatus productStatus;
	
	@Column
	private String productName;
	
	@Column
	private Boolean onSale;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the validThrough
	 */
	public Date getValidThrough() {
		return validThrough;
	}

	/**
	 * @param validThrough the validThrough to set
	 */
	public void setValidThrough(Date validThrough) {
		this.validThrough = validThrough;
	}

	/**
	 * @return the quantity
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the quality
	 */
	public Quality getQuality() {
		return quality;
	}

	/**
	 * @param quality the quality to set
	 */
	public void setQuality(Quality quality) {
		this.quality = quality;
	}

	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * @return the availability
	 */
	public Availability getAvailability() {
		return availability;
	}

	/**
	 * @param availability the availability to set
	 */
	public void setAvailability(Availability availability) {
		this.availability = availability;
	}

	/**
	 * @return the productStatus
	 */
	public ProductStatus getProductStatus() {
		return productStatus;
	}

	/**
	 * @param productStatus the productStatus to set
	 */
	public void setProductStatus(ProductStatus productStatus) {
		this.productStatus = productStatus;
	}

	/**
	 * @return the onSale
	 */
	public Boolean getOnSale() {
		return onSale;
	}

	/**
	 * @param onSale the onSale to set
	 */
	public void setOnSale(Boolean onSale) {
		this.onSale = onSale;
	}

	/**
	 * @return the orderDate
	 */
	public Date getOrderDate() {
		return orderDate;
	}

	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	/**
	 * @return the deliveryDate
	 */
	public Date getDeliveryDate() {
		return deliveryDate;
	}

	/**
	 * @param deliveryDate the deliveryDate to set
	 */
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
}
