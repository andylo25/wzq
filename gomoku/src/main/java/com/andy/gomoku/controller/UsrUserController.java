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
@RequestMapping("admin/user")
public class UsrUserController extends BaseController{

	@RequestMapping("list")  
    public ModelAndView list() {  
		PageVO users = DaoUtils.getPageForMap(UsrUser.table(), null,1,10);
		
		idToName(users.getItems(), UsrUser.table(), "uid:nick_name");
		
        return createMV("tableList","用户管理", Collections.singletonMap("formData", users));
    }  
	
	
	
}
