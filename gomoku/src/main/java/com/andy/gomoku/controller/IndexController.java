package com.andy.gomoku.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.andy.gomoku.base.PageVO;
import com.andy.gomoku.dao.DaoUtils;
import com.andy.gomoku.entity.UsrGameInfo;
import com.andy.gomoku.entity.UsrUser;
import com.google.common.collect.Maps;

@Controller
public class IndexController extends BaseController{

	@RequestMapping("")  
    public ModelAndView index() {  
		Map map = Maps.newHashMap();
		PageVO users = DaoUtils.getPageForMap(UsrUser.table(), null,1,10);
		map.put("users", users.getItems());
		map.put("userCount", users.getTotal_items());
		List<Map<String, Object>> gameInfos = DaoUtils.getListMap(UsrGameInfo.table(),null,10);
		idToName(gameInfos, UsrUser.table(), "uid:nick_name");
		map.put("gameInfos", gameInfos);
        return createMV("dashboard","管理面板", Collections.singletonMap("formData", map));
    }  
	
	
	
}
