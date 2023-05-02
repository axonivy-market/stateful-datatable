package com.axonivy.demo.statefuldatatable.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.MappedSuperclass;

/**
 * Base database entity object
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

	private static final long serialVersionUID = 4128302279946094433L;

	protected static final String COLUMN_ID = "ID";

	/**
	 * return: ID of database entity
	 */
	public abstract String getId();

	/**
	 * ID of database entity - setter
	 *
	 * @param id
	 */
	public abstract void setId(String id);

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !AbstractEntity.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		return getId().equals(((AbstractEntity) obj).getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}