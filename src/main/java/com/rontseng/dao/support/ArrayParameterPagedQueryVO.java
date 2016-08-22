/*
 * Copyright (c), Audatex GmbH, Switzerland. This is UNPUBLISHED
 * PROPRIETARY SOURCE CODE of Audatex GmbH; the contents of this file
 * may not be disclosed to third parties, copied or duplicated in any form, in
 * whole or in part, without the prior written permission of Audatex 
 * GmbH. ALL RIGHTS RESERVED.
 */
package com.rontseng.dao.support;

import java.util.Map;

/**
 * Describe the type here.
 *
 * @author Ron
 * @see AXN-
 * @date Mar 21, 2016 11:53:18 AM
 */
public class ArrayParameterPagedQueryVO<T> extends PagedQueryVO<T> {

  private static final long serialVersionUID = 1L;

  /**
   * 查詢參數名稱陣列
   */
  private String[] propertyNames;

  /**
   * 查詢參數陣列
   */
  private Object[] propertyValues;

  public String[] getPropertyNames() {
    return propertyNames;
  }

  /* (non-Javadoc)
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
