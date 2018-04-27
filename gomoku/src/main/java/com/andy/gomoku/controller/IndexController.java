package com.andy.gomoku.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Maps;

@Controller
@RequestMapping("admin")
public class IndexController extends BaseController{

	@RequestMapping("thymeleaf")  
    public ModelAndView thymeleaf() {  
		Map map = Maps.newHashMap();
        map.put("host", "http://blog.csdn.net/liaodehong");
        return createSuccessModelAndView("test", map);
    }  
	
}
