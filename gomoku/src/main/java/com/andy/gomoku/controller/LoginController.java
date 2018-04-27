package com.andy.gomoku.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.andy.gomoku.base.RequestUtil;
import com.andy.gomoku.base.RespVO;
import com.andy.gomoku.dao.DaoUtils;
import com.andy.gomoku.dao.Where;
import com.andy.gomoku.entity.UsrAdmin;
import com.andy.gomoku.utils.SecurityUtil;

/**
 * @Description: 用户登录、登出功能
 * @author cuiwm
 */
@Controller
@RequestMapping("admin")
public class LoginController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("login")
	public @ResponseBody RespVO login(String userName,String password) {
		password = SecurityUtil.sha1(password, userName);
		UsrAdmin admin = DaoUtils.getOne(UsrAdmin.class, Where.eq("user_name", userName),Where.eq("password", password));
		if(admin == null){
			return RespVO.createErrorJsonResonse("用户名或密码错误");
		}
		
		RequestUtil.getSession().setAttribute(RequestUtil.SESSION_USER, admin);
		
		return RespVO.createSuccessJsonResonse("登录成功");
	}
	
	@RequestMapping("logout")
	public @ResponseBody RespVO logout(HttpSession session) {
		session.removeAttribute(RequestUtil.SESSION_USER);
		session.invalidate();
		
		return RespVO.createSuccessJsonResonse("登出成功");
	}
}
