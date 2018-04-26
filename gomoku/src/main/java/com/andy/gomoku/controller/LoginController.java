package com.andy.gomoku.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.andy.gomoku.utils.GmAction;
import com.andy.gomoku.utils.SendUtil;

/**
 * @Description: 用户登录、登出功能
 * @author cuiwm
 */
@Controller
public class LoginController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/pushVideoListToWeb")
	public @ResponseBody Map<String, Object> pushVideoListToWeb(@RequestBody Map<String, Object> param) {
		Map<String, Object> result = new HashMap<String, Object>();

		SendUtil.broadcast(GmAction.ACTION_200,"有新客户呼入,sltAccountId:" + MapUtils.getString(param, "sltAccountId"));
		result.put("operationResult", true);
		
		return result;
	}
}
