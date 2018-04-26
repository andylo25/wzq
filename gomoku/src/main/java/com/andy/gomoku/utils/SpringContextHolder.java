package com.andy.gomoku.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

@Service
@Lazy(false)
public class SpringContextHolder implements ApplicationContextAware,BeanFactoryPostProcessor,Ordered {
	
	private static ApplicationContext _applicationContext;
	private static ConfigurableListableBeanFactory _beanFactory;
	
	/**
	 * 获取WebApplicationContext上下文，主要用于获取ServletContext
	 * @return
	 */
	public static WebApplicationContext getWebApplicationContext(){
		return (WebApplicationContext) _applicationContext;
	}
	
	public static ApplicationContext getApplicationContext(){
		return _applicationContext;
	}

	/**
	 * 通过beanName获取bean
	 * @param beanName
	 * @return
	 */
	public static Object getBean(String beanName) {
		return _beanFactory.containsBean(beanName)?_beanFactory.getBean(beanName):null;
	}
	
	/**
	 * 通过类型获取bean
	 * @param clasz
	 * @return
	 */
	public static <T> T getBean(Class<T> clasz) {
		return _beanFactory.getBean(clasz);
	}
	
	/**
	 * 通过beanName及类型获取bean
	 * @param beanName
	 * @param clasz
	 * @return
	 */
	public static <T> T getBean(String beanName,Class<T> clasz) {
		return _beanFactory.containsBean(beanName)?_beanFactory.getBean(beanName,clasz):null;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		_applicationContext = applicationContext;
	}
	
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		SpringContextHolder._beanFactory = beanFactory;
	}

	@Override
	public int getOrder() {
		return 0;
	}

}