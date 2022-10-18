package com.axonivy.market.statefuldatatable.entity;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

/**
 * Base database entity object
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

	private static final long serialVersionUID = 4128302279946094433L;
	
	protected static final String COLUMN_ID = "ID";

	/**
	 * ID of database entity - getter
	 *
	 * @return
	 */
	public abstract String getId();

	/**
	 * ID of database entity - setter
	 *
	 * @param id
	 */
	public abstract void setId(String id);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !AbstractEntity.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		return getId().equals(((AbstractEntity) obj).getId());
	}
}