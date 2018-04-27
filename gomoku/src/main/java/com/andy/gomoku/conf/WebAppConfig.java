package com.andy.gomoku.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.andy.gomoku.conf.interceptor.LoginHandlerInterceptor;

@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter{
	
	/**
     * 配置拦截器
     * @param registry
     */
    public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(commonInterceptor()).addPathPatterns("/**");
	}
    
    @Bean
    public LoginHandlerInterceptor commonInterceptor(){
        return new LoginHandlerInterceptor();
    }
    
    
}