package com.rontseng.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rontseng.dao.IGenericDAO;
import com.rontseng.dao.support.ArrayParameterPagedQueryVO;
import com.rontseng.dao.support.OrderByEnum;
import com.rontseng.dao.support.PagedQueryVO;
import com.rontseng.dao.support.QueryParameter;
import com.rontseng.util.DateUtil;

/**
 * Describe the type here.
 *
 * @author Ron
 * @date Mar 21, 2016 11:53:18 AM
 */
@Repository("genericDao")
public class GenericDAOImpl<T, PK extends Serializable> implements IGenericDAO<T, PK> {
	private static final String ALIAS = "entity";
	@PersistenceContext
	protected EntityManager entityManager;
	private Class<T> clazz;

	@Override
	public IGenericDAO<T, PK> setClazz(Class<T> clazzToSet) {
		this.clazz = clazzToSet;
		return this;
	}

	@Override
	public List<T> findBy(String[] propertyNames, Object[] values) {
		return findBy(propertyNames, values, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T findOneBy(String[] propertyNames, Object[] values) {
		ArrayParameterPagedQueryVO<T> queryVO = new ArrayParameterPagedQueryVO<T>();
		queryVO.setPropertyNames(propertyNames);
		queryVO.setPropertyValues(values);
		return (T) getQuery(queryVO, false).getSingleResult();
	}

	private Query getQuery(PagedQueryVO<T> queryVO, boolean isGetTotalSize) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		if (isGetTotalSize) {
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<T> entity = cq.from(clazz);
			entity.alias(ALIAS);
			cq.select(cb.count(entity));
			cq.where(getPredicates(cb, cq, entity, queryVO));
			return entityManager.createQuery(cq);
		} else {
			CriteriaQuery<T> cq = cb.createQuery(clazz);
			Root<T> entity = cq.from(clazz);
			entity.alias(ALIAS);
			cq.select(entity);
			cq.where(getPredicates(cb, cq, entity, queryVO));
			cq.orderBy(getOrderBy(queryVO));
			return entityManager.createQuery(cq).setHint("org.hibernate.cacheable", true);
		}
	}

	@SuppressWarnings("unchecked")
	private Predicate[] getPredicates(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<T> entity,
			PagedQueryVO<T> queryVO) {
		Map<String, QueryParameter> queryMap = queryVO.getQueryParameterMap();
		List<Predicate> result = new ArrayList<Predicate>();
		QueryParameter queryParameter = null;
		Path<?> path = null;

		for (String propertyName : queryMap.keySet()) {
			queryParameter = queryMap.get(propertyName);
			path = getPath(entity, propertyName);
			try {
				Object value = queryParameter.getArgs()[0];
				switch (queryParameter.getQueryEnum()) {
				case EQUAL:
					if (value instanceof Date) {
						result.add(cb.equal(path, DateUtil.convertToSQLDate((Date) value)));
					} else if (value == null) {
						result.add(cb.isNull(path));
					} else {
						result.add(cb.equal(path, value));
					}
					break;
				case GREATER_EQUAL_THAN:
					if (value instanceof Date) {
						result.add(cb.greaterThanOrEqualTo((Expression<? extends Date>) path,
								DateUtil.convertToSQLDate((Date) value)));
					} else {
						result.add(cb.ge((Expression<? extends Number>) path, (Number) value));
					}
					break;
				case GREATER_THAN:
					if (value instanceof Date) {
						result.add(cb.greaterThan((Expression<? extends Date>) path,
								DateUtil.convertToSQLDate((Date) value)));
					} else {
						System.out.println("value===" + value);
						result.add(cb.gt((Expression<? extends Number>) path, (Number) value));
					}
					break;
				case LESS_EQUAL_THAN:
					if (value instanceof Date) {
						result.add(cb.lessThanOrEqualTo((Expression<? extends Date>) path,
								DateUtil.convertToSQLDate((Date) value)));
					} else {
						result.add(cb.le((Expression<? extends Number>) path, (Number) value));
					}
					break;
				case LESS_THAN:
					if (value instanceof Date) {
						result.add(cb.lessThan((Expression<? extends Date>) path,
								DateUtil.convertToSQLDate((Date) value)));
					} else {
						result.add(cb.lt((Expression<? extends Number>) path, (Number) value));
					}
					break;
				case NOT_EQUAL:
					result.add(cb.notEqual(path, queryParameter.getArgs()[0]));
					break;
				case RIGHT_LIKE:
					result.add(cb.like((Expression<String>) path, (String) value + "%"));
					break;
				case NOT_RIGHT_LIKE:
					result.add(cb.notLike((Expression<String>) path, (String) value + "%"));
					break;
				case LEFT_LIKE:
					result.add(cb.like((Expression<String>) path, "%" + (String) value));
					break;
				case NOT_LEFT_LIKE:
					result.add(cb.notLike((Expression<String>) path, "%" + (String) value));
					break;
				case ALL_LIKE:
					result.add(cb.like((Expression<String>) path, "%" + (String) value + "%"));
					break;
				case NOT_ALL_LIKE:
					result.add(cb.notLike((Expression<String>) path, "%" + (String) value + "%"));
					break;
				case IN:
					result.add(path.in(queryParameter.getArgs()));
					break;
				case NOT_IN:
					result.add(cb.not(path.in(queryParameter.getArgs())));
					break;
				case BETWEEN:
					Object[] betweenValues = (Object[]) queryParameter.getArgs();
					java.sql.Date sDate = DateUtil.convertToSQLDate((Date) betweenValues[0]);
					java.sql.Date eDate = DateUtil.convertToSQLDate((Date) betweenValues[1]);
					result.add(cb.between((Expression<? extends Date>) path, sDate, eDate));
					break;
				case NOT_BETWEEN:
					Object[] notBetweenValues = (Object[]) queryParameter.getArgs();
					java.sql.Date snDate = DateUtil.convertToSQLDate((Date) notBetweenValues[0]);
					java.sql.Date enDate = DateUtil.convertToSQLDate((Date) notBetweenValues[1]);
					result.add(cb.not(cb.between((Expression<? extends Date>) path, snDate, enDate)));
					break;
				case OR:
					Predicate[] orArray = new Predicate[queryParameter.getArgs().length];
					for (int i = 0; i < queryParameter.getArgs().length; i++) {
						if (queryParameter.getArgs()[i] != null) {
							orArray[i] = cb.equal(path, queryParameter.getArgs()[i]);
						} else {
							orArray[i] = cb.isNull(path);
						}

					}
					result.add(cb.or(orArray));
					break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Predicate[] predicates = new Predicate[result.size()];
		return result.toArray(predicates);
	}

	private Path<?> getPath(Root<?> entity, String propertyName) {
		Path<?> result = entity;
		while (propertyName.contains(".")) {
			result = result.get(propertyName.split("\\.")[0]);
			propertyName = propertyName.substring(propertyName.indexOf(".") + 1);
		}

		return result.get(propertyName);
	}

	private List<Order> getOrderBy(PagedQueryVO<T> queryVO) {
		List<Order> orders = new ArrayList<Order>();
		if (queryVO.getOrderByMap().size() == 0)
			return orders;
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> entity = cq.from(clazz);
		entity.alias(ALIAS);
		for (String propertyName : queryVO.getOrderByMap().keySet()) {
			switch (queryVO.getOrderByMap().get(propertyName)) {
			case ASC:
				orders.add(cb.asc(getPath(entity, propertyName)));
				break;
			case DESC:
				orders.add(cb.desc(getPath(entity, propertyName)));
				break;
			}
		}
		return orders;
	}

	@Override
	public T findOne(PK id) {
		return entityManager.find(clazz, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll() {
		return entityManager.createQuery("from " + clazz.getName()).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll(String orderby) {
		return entityManager.createQuery("from " + clazz.getName() + " order by " + orderby).getResultList();
	}

	@Override
	@Transactional
	public T save(T entity) {
		return entityManager.merge(entity);
	}

	@Override
	@Transactional
	public void delete(T entity) {
		entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void deleteById(PK... ids) {
		T entity = null;
		for (PK id : ids) {
			entity = findOne(id);
			delete(entity);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> nativeQuery(String queryString) {
		return entityManager.createNativeQuery(queryString).getResultList();
	}

	@Override
	public Query createQuery(String jpqlQueryString) {
		return entityManager.createQuery(jpqlQueryString);
	}

	@Override
	public Query createNativeQuery(String queryString) {
		return entityManager.createNativeQuery(queryString);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PagedQueryVO<T> findBy(PagedQueryVO<T> queryVO) {
		if (queryVO.getPageSize() <= 0) {
			Query query = getQuery(queryVO, false);
			queryVO.setResults(query.getResultList());
			return queryVO;
		}
		long totalSize = (Long) getQuery(queryVO, true).getSingleResult();

		Query query = getQuery(queryVO, false);
		queryVO.setTotalSize(totalSize);
		query.setFirstResult((queryVO.getPageNumber() - 1) * queryVO.getPageSize());
		query.setMaxResults(queryVO.getPageSize());
		queryVO.setResults(query.getResultList());
		int pageCounts = 0;
		if (totalSize % queryVO.getPageSize() != 0) {
			pageCounts = (int) (totalSize / queryVO.getPageSize()) + 1;
		} else {
			pageCounts = (int) (totalSize / queryVO.getPageSize());
		}
		queryVO.setPageCounts(pageCounts == 0 ? 1 : pageCounts);
		return queryVO;
	}

	@Override
	public List<T> findBy(String[] propertyNames, Object[] values, Map<String, OrderByEnum> orderBy) {
		ArrayParameterPagedQueryVO<T> queryVO = new ArrayParameterPagedQueryVO<T>();
		queryVO.setPropertyNames(propertyNames);
		queryVO.setPropertyValues(values);
		if (orderBy != null) {
			for (String key : orderBy.keySet()) {
				queryVO.addOrderBy(key, orderBy.get(key));
			}
		}
		return findBy(queryVO).getResults();
	}
}
