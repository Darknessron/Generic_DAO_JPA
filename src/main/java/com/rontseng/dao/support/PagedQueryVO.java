package com.rontseng.dao.support;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

import com.rontseng.dao.annotation.QueryColumnMapping;

/**
 * Describe the type here.
 *
 * @author Ron
 * @date Mar 21, 2016 11:53:18 AM
 */
public abstract class PagedQueryVO<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Map of QueryParameter
	 */
	protected Map<String, QueryParameter> queryParameterMap = new HashMap<String, QueryParameter>();

	/**
	 * Map of OrderBy
	 */
	protected Map<String, OrderByEnum> orderByMap = new Hashtable<String, OrderByEnum>();

	/**
	 * Current page number
	 */
	protected int pageNumber = 1;

	/**
	 * Size of each page
	 */
	protected int pageSize = 0;

	/**
	 * Size of total records
	 */
	protected long totalSize = 0;

	/**
	 * Number of pages
	 * 
	 * @return
	 */
	protected int pageCounts = 1;

	/**
	 * List of page number, used for dropdown list
	 * 
	 * @return
	 */
	public List<Integer> getPageCountList() {
		List<Integer> rtn = new ArrayList<Integer>();
		for (int i = 1; i <= getPageCounts(); i++) {
			rtn.add(i);
		}
		return rtn;
	}

	/**
	 * Size of each page, used for dropdown list
	 */
	protected int[] pageSizeArray = { 10, 50, 100, 500 };

	/**
	 * Query result
	 */
	protected List<T> results = new ArrayList<T>();

	/**
	 * Query result status
	 */
	protected int resultStatus;

	/**
	 * Query result message
	 */
	protected String resultMessage;

	/**
	 * Add a query parameter
	 * 
	 * @param propertyName
	 *            name of query parameter
	 * @param queryEnum
	 *            query condition
	 * @param args
	 *            value of query parameter
	 * @return
	 */
	public PagedQueryVO<T> addWhereCondition(String propertyName, QueryEnum queryEnum, Object... args) {
		QueryParameter queryParameter = new QueryParameter(queryEnum, args);
		this.queryParameterMap.put(propertyName, queryParameter);
		return this;
	}

	/**
	 * Add a order by condition
	 * 
	 * @param propertyName
	 *            Name of order by parameter
	 * @param orderBy
	 *            ASC or Desc
	 * @return
	 */
	public PagedQueryVO<T> addOrderBy(String propertyName, OrderByEnum orderBy) {
		this.orderByMap.put(propertyName, orderBy);
		return this;
	}

	/**
	 * Get map of query parameters 
	 * 
	 * @return
	 */
	public Map<String, QueryParameter> getQueryParameterMap() {
		Field[] fields = this.getClass().getDeclaredFields();
		QueryParameter queryParameter = null;
		QueryColumnMapping mapping = null;
		boolean isIgnore = false;
		String propertyName = null;
		for (Field field : fields) {
			isIgnore = false;
			propertyName = null;
			mapping = field.getAnnotation(QueryColumnMapping.class);
			if (mapping != null)
				isIgnore = Boolean.valueOf((Boolean) AnnotationUtils.getValue(mapping, "ignore"));
			// ignore this property
			if (isIgnore)
				continue;
			// get mappedEntityProperty
			if (mapping != null)
				propertyName = (String) AnnotationUtils.getValue(mapping, "mappedEntityProperty");
			if (propertyName == null)
				propertyName = field.getName();

			queryParameter = queryParameterMap.get(propertyName);
			// If query condition not specified, use default query condition. 
			if (queryParameter != null)
				continue;
			else {
				try {
					Object value = PropertyUtils.getProperty(this, field.getName());
					if (value == null)
						continue;
					// ignore empty String or null
					if (field.getType() == String.class) {
						String s = (String) value;
						if (StringUtils.isBlank(s) || s.equalsIgnoreCase("null"))
							continue;
					}
					// use equal as default query condition.
					addWhereCondition(propertyName, QueryEnum.EQUAL, value);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		}
		return queryParameterMap;
	}

	/**
	 * get map of Order by 
	 * 
	 * @return
	 */
	public Map<String, OrderByEnum> getOrderByMap() {
		return orderByMap;
	}

	/**
	 * @return the pageNumber
	 */
	public int getPageNumber() {
		return pageNumber;
	}

	/**
	 * @param pageNumber
	 *            the pageNumber to set
	 */
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the totalSize
	 */
	public long getTotalSize() {
		return totalSize;
	}

	/**
	 * @param totalSize
	 *            the totalSize to set
	 */
	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	/**
	 * @return the results
	 */
	public List<T> getResults() {
		return results;
	}

	/**
	 * @param results
	 *            the results to set
	 */
	public void setResults(List<T> results) {
		this.results = results;
	}

	public int[] getPageSizeArray() {
		return pageSizeArray;
	}

	public void setPageSizeArray(int[] pageSizeArray) {
		this.pageSizeArray = pageSizeArray;
	}

	public int getPageCounts() {
		return pageCounts;
	}

	public void setPageCounts(int pageCounts) {
		this.pageCounts = pageCounts;
	}

	public int getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(int resultStatus) {
		this.resultStatus = resultStatus;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

}
