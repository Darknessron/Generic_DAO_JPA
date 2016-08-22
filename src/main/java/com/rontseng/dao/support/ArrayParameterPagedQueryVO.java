package com.rontseng.dao.support;

import java.util.Map;

/**
 * Describe the type here.
 *
 * @author Ron
 * @date Mar 21, 2016 11:53:18 AM
 */
public class ArrayParameterPagedQueryVO<T> extends PagedQueryVO<T> {

	private static final long serialVersionUID = 1L;

	/**
	 * Array of query parameter name
	 */
	private String[] propertyNames;

	/**
	 * Array of query parameter value
	 */
	private Object[] propertyValues;

	public String[] getPropertyNames() {
		return propertyNames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uec.base.service.support.PagedQueryVO#getQueryParameterMap()
	 */
	@Override
	public Map<String, QueryParameter> getQueryParameterMap() {
		for (int i = 0; i < propertyNames.length; i++) {
			this.addWhereCondition(propertyNames[i], QueryEnum.EQUAL, propertyValues[i]);
		}
		return queryParameterMap;
	}

	public void setPropertyNames(String[] propertyNames) {
		this.propertyNames = propertyNames;
	}

	public Object[] getPropertyValues() {
		return propertyValues;
	}

	public void setPropertyValues(Object[] propertyValues) {
		this.propertyValues = propertyValues;
	}

}
