package com.rontseng.advisor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import com.rontseng.dao.IGenericDAO;
import com.rontseng.dao.support.AbstractResultVO;
import com.rontseng.dao.support.PagedQueryVO;
import com.rontseng.model.MessageLog;
import com.rontseng.util.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Describe the type here.
 *
 * @author Ron
 * @date May 27, 2016 4:55:11 PM
 */
@Aspect
public class MessageAdvisor {
	@Autowired
	private IGenericDAO<MessageLog, Long> messageLogDao;

	@SuppressWarnings("rawtypes")
	@Around("execution(* com.rontseng.controller..*(..))")
	public Object logAroundMessage(ProceedingJoinPoint joinPoint) throws Throwable {
		ObjectMapper mapper = new ObjectMapper();
		MessageLog log = new MessageLog();

		PagedQueryVO queryVO = (PagedQueryVO) joinPoint.getArgs()[0];
		log.setRequestContent(mapper.writeValueAsString(queryVO));
		log.setRequestTime(DateUtil.getCurrentDate());

		AbstractResultVO result = (AbstractResultVO) joinPoint.proceed();

		log.setResponseContent(mapper.writeValueAsString(result));
		log.setResponseTime(DateUtil.getCurrentDate());
		log.setUserToken(result.getToken());

		messageLogDao.save(log);

		return result;
	}
}
