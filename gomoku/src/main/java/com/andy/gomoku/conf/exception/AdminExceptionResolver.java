package com.andy.gomoku.conf.exception;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import com.andy.gomoku.base.RespVO;
import com.andy.gomoku.exception.GoSeviceException;
import com.andy.gomoku.utils.JsonUtils;

@Component
public class AdminExceptionResolver extends ExceptionHandlerExceptionResolver {
	protected Logger logger = Logger.getLogger(AdminExceptionResolver.class);
	
	@Override
	protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception exception) {
		if(handlerMethod == null) return null;
        if(handlerMethod.getMethod() == null) return null;
        
        if(!(exception instanceof GoSeviceException)){
        	exception = new GoSeviceException("内部异常，请联系管理员",exception);
        }
        
        logger.error("error",exception);
        
		//获取错误信息
		String errorMsg = exception.getMessage();
		
		//判断是否配置@ResponseBody
        if(AnnotationUtils.findAnnotation(handlerMethod.getMethod(), ResponseBody.class) != null) {
            response.setContentType("application/json;charset=utf-8");
            try {
            	Map<String, Object> result = new HashMap<String, Object>();
            	result.put("status", RespVO.ERROR);
            	result.put("description", errorMsg);
            	
            	PrintWriter printWriter = response.getWriter();
    			printWriter.print(JsonUtils.object2JsonString(result));
    			printWriter.close();
			} catch (Exception e) {
				logger.error("error",e);
			}
            return new ModelAndView();
        }
        
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("error", errorMsg);
		
    	return modelAndView;
	}
}