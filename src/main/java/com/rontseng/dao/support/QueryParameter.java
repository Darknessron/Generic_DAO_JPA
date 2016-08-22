package com.rontseng.dao.support;

/**
 * Describe the type here.
 *
 * @author Ron
 * @date Mar 21, 2016 11:53:18 AM
 */
public class QueryParameter {

	/**
	 * Query condition
	 */
	private QueryEnum queryEnum;
	/**
	 * Value of query condition
	 */
	private Object[] args;

	public QueryParameter(QueryEnum queryEnum, Object[] args) {
		this.queryEnum = queryEnum;
		this.args = args;
	}

	public QueryEnum getQueryEnum() {
		return queryEnum;
	}

	public void setQueryEnum(QueryEnum queryEnum) {
		this.queryEnum = queryEnum;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}
}
