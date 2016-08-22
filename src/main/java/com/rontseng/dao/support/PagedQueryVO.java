/*
 * Copyright (c), Audatex GmbH, Switzerland. This is UNPUBLISHED
 * PROPRIETARY SOURCE CODE of Audatex GmbH; the contents of this file
 * may not be disclosed to third parties, copied or duplicated in any form, in
 * whole or in part, without the prior written permission of Audatex 
 * GmbH. ALL RIGHTS RESERVED.
 */
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
 * @see AXN-
 * @date Mar 21, 2016 11:53:18 AM
 */
public abstract class PagedQueryVO<T> implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * 查詢條件
   */
  protected Map<String, QueryParameter> queryParameterMap = new HashMap<String, QueryParameter>();

  /**
   * 排序條件
   */
  protected Map<String, OrderByEnum> orderByMap = new Hashtable<String, OrderByEnum>();

  /**
   * 目前頁數
   */
  protected int pageNumber = 1;

  /**
   * 每頁資料筆數
   */
  protected int pageSize = 0;

  /**
   * 資料總筆數
   */
  protected long totalSize = 0;

  /**
   * 資料頁數
   * 
   * @return
   */
  protected int pageCounts = 1;

  /**
   * 頁數選單
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
   * 每頁顯示筆數
   */
  protected int[] pageSizeArray = { 10, 50, 100, 500 };

  /**
   * 查詢結果
   */
  protected List<T> results = new ArrayList<T>();

  /**
   * 查詢結果狀態
   */
  protected int resultStatus;

  /**
   * 查詢結果訊息
   */
  protected String resultMessage;

  /**
   * 加入查詢條件
   * 
   * @param propertyName
   *          查詢條件
   * @param queryEnum
   *          比較子
   * @param args
   *          參數
   * @return
   */
  public PagedQueryVO<T> addWhereCondition(String propertyName, QueryEnum queryEnum, Object... args) {
    QueryParameter queryParameter = new QueryParameter(queryEnum, args);
    this.queryParameterMap.put(propertyName, queryParameter);
    return this;
  }

  /**
   * 加入排序條件
   * 
   * @param propertyName
   *          排序欄位
   * @param orderBy
   *          排序條件
   * @return
   */
  public PagedQueryVO<T> addOrderBy(String propertyName, OrderByEnum orderBy) {
    this.orderByMap.put(propertyName, orderBy);
    return this;
  }

  /**
   * 取得查詢條件
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
      // 此property 忽略不進行查詢
      if (isIgnore)
        continue;
      // 取得mappedEntityProperty
      if (mapping != null)
        propertyName = (String) AnnotationUtils.getValue(mapping, "mappedEntityProperty");
      if (propertyName == null)
        propertyName = field.getName();

      queryParameter = queryParameterMap.get(propertyName);
      // 已指定查詢運算子
      if (queryParameter != null)
        continue;
      else {
        try {
          Object value = PropertyUtils.getProperty(this, field.getName());
          if (value == null)
            continue;
          // 如果是空字串或null則忽略
          if (field.getType() == String.class) {
            String s = (String) value;
            if (StringUtils.isBlank(s) || s.equalsIgnoreCase("null"))
              continue;
          }
          // 預設為EQUAL
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
   * 取得排序條件
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
   *          the pageNumber to set
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
   *          the pageSize to set
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
   *          the totalSize to set
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
   *          the results to set
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
