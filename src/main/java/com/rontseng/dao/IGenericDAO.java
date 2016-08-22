/*
 * Copyright (c), Audatex GmbH, Switzerland. This is UNPUBLISHED
 * PROPRIETARY SOURCE CODE of Audatex GmbH; the contents of this file
 * may not be disclosed to third parties, copied or duplicated in any form, in
 * whole or in part, without the prior written permission of Audatex 
 * GmbH. ALL RIGHTS RESERVED.
 */
package com.rontseng.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.rontseng.dao.support.OrderByEnum;
import com.rontseng.dao.support.PagedQueryVO;

/**
 * Describe the type here.
 *
 * @author Ron
 * @see AXN-
 * @date Mar 21, 2016 11:53:18 AM
 */
public interface IGenericDAO<T, PK extends Serializable> {
  
  public IGenericDAO<T, PK> setClazz(final Class<T> clazzToSet);
  public List<T> findBy(String[] propertyNames, Object[] values);
  public List<T> findBy(String[] propertyNames, Object[] values, Map<String, OrderByEnum> orderBy);
  public PagedQueryVO<T> findBy(PagedQueryVO<T> queryVO);
  public T findOneBy(String[] propertyNames, Object[] values);
  public T findOne(final PK id);
  public List<T> findAll();
  public List<T> findAll(String orderby);
  @Transactional
  public T save(final T entity);
  @Transactional
  public void delete(final T entity);
  @SuppressWarnings("unchecked")
  @Transactional
  public void deleteById(final PK... ids);
  public List<Object[]> nativeQuery(String queryString);
  public Query createQuery(String jpqlQueryString);
  public Query createNativeQuery(String queryString);
}
