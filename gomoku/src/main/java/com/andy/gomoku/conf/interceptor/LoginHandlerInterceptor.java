package com.andy.gomoku.conf.interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.andy.gomoku.base.RequestUtil;
import com.andy.gomoku.base.RespVO;
import com.andy.gomoku.utils.JsonUtils;


public class LoginHandlerInterceptor extends HandlerInterceptorAdapter{
	
	static final String NO_INTERCEPTOR_PATH = ".*/((login)|(public)|(common)|(cmd)).*";
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String path = request.getServletPath();
		
		//判断请求是否需要拦截
		if (path.matches(NO_INTERCEPTOR_PATH)) return true;
		
		//判断用户是否登陆，未登陆就返回到登录页面
		if (request.getSession().getAttribute(RequestUtil.SESSION_USER) == null) {
			sendResult(request, response,(HandlerMethod) handler);
			return false;
		}
		return true;
	}
	
	private void sendResult(HttpServletRequest request, HttpServletResponse response,HandlerMethod handlerMethod) throws IOException {
		if(isAjax(request,handlerMethod)){
			RespVO dyResponse = new RespVO();
			dyResponse.setStatus(RespVO.LOGINERR);
			PrintWriter printWriter = response.getWriter();
			printWriter.print(JsonUtils.object2JsonString(dyResponse));
			printWriter.close();
		}else{
			response.sendRedirect(request.getContextPath() + "/login.html");
		}
	}
	
	private boolean isAjax(HttpServletRequest request,HandlerMethod handlerMethod){
		return ("application/x-www-form-urlencoded".equals(request.getHeader("Content-Type")) 
				&& request.getMethod().equals("POST")) 
				|| (AnnotationUtils.findAnnotation(handlerMethod.getMethod(), ResponseBody.class) != null);
	}
	
}