package com.andy.gomoku.controller;

import org.springframework.web.servlet.ModelAndView;

import com.andy.gomoku.utils.JsonUtils;

public class BaseController {

	public static ModelAndView createSuccessModelAndView(String viewName, Object data) {
		ModelAndView modelAndView = new ModelAndView(viewName);
		modelAndView.addObject("status", 200);
		modelAndView.addObject("description", "OK");
		if(data instanceof String){
			modelAndView.addObject("data", data);
		}else{
			modelAndView.addObject("data", JsonUtils.object2JsonString(data));
		}
		return modelAndView;
	}
	
	
	
}
