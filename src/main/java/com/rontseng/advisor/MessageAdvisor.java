package com.rontseng.advisor;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rontseng.dao.IGenericDAO;
import com.rontseng.model.MessageLog;
import com.rontseng.util.DateUtil;

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
	
	@Autowired
	private HttpServletRequest httpRequest;

	@Around("execution(* com.rontseng.example.controller..*(..))")
	public Object logAroundMessage(ProceedingJoinPoint joinPoint) throws Throwable {
		ObjectMapper mapper = new ObjectMapper();
		MessageLog log = new MessageLog();
		
		log.setUrl(httpRequest.getRequestURL().append("?").append(httpRequest.getQueryString()).toString());
		log.setHttpMethod(httpRequest.getMethod());

		Object request = joinPoint.getArgs()[0];
		log.setRequestContent(mapper.writeValueAsString(request));
		log.setRequestTime(DateUtil.getCurrentDate());

		Object result = joinPoint.proceed();

		log.setResponseContent(mapper.writeValueAsString(result));
		log.setResponseTime(DateUtil.getCurrentDate());
		

		messageLogDao.save(log);

		return result;
	}
}
