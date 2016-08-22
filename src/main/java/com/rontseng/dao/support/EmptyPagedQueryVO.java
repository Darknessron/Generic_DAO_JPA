package com.rontseng.dao.support;

import com.rontseng.dao.annotation.QueryColumnMapping;

/**
 * Describe the type here.
 *
 * @author Ron
 * @date Mar 21, 2016 11:53:18 AM
 */
public class EmptyPagedQueryVO<T> extends PagedQueryVO<T> {

	@QueryColumnMapping(ignore = true)
	private static final long serialVersionUID = 1L;

}
