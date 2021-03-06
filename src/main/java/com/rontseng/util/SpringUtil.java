package com.rontseng.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Ron
 *
 */
public class SpringUtil implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringUtil.applicationContext = applicationContext;
	}

	public static Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}

	public static <T> T getBean(String beanName, Class<T> clazz) {
		return applicationContext.getBean(beanName, clazz);
	}
}
