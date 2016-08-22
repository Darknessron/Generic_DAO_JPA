/*
 * Copyright (c), Audatex GmbH, Switzerland. This is UNPUBLISHED
 * PROPRIETARY SOURCE CODE of Audatex GmbH; the contents of this file
 * may not be disclosed to third parties, copied or duplicated in any form, in
 * whole or in part, without the prior written permission of Audatex 
 * GmbH. ALL RIGHTS RESERVED.
 */
package com.rontseng.dao.support;

/**
 * Describe the type here.
 *
 * @author Ron
 * @see AXN-
 * @date Mar 21, 2016 11:53:18 AM
 */
public class QueryParameter {

  /**
   * 查詢條件
   */
  private QueryEnum queryEnum;
  /**
   * 條件參數
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
