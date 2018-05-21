package com.andy.gomoku.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.andy.gomoku.GomokuApplication;
import com.andy.gomoku.base.RespVO;
import com.andy.gomoku.dao.DaoUtils;
import com.andy.gomoku.dao.vo.Where;
import com.andy.gomoku.entity.ConfCommon;
import com.andy.gomoku.utils.CommonUtils;
import com.andy.gomoku.utils.GameConf;

/**
 * @Description: 接受后台命令处理
 * @author cuiwm
 */
@Controller
@RequestMapping("admin/cmd")
public class CmdController extends BaseController{

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("refreshCache/{type}")
	public @ResponseBody RespVO refreshCache(@PathVariable("type") String type,String seckey) {
		if(!checkSafe(seckey)){
			return RespVO.createErrorJsonResonse("非法操作");
		}
		logger.error("收到后台命令："+type);
		if(type.equals("1")){
			// 段位配置
			GameConf.initTitles();
		}else if(type.equals("2")){
			// 参数配置
			GameConf.initCommons();
		}else if(type.equals("3")){
			// 停服
			GomokuApplication.destroy();
		}
		
		return RespVO.createSuccessJsonResonse("执行成功");
	}

	private boolean checkSafe(String seckey) {
		ConfCommon conf = DaoUtils.getOne(ConfCommon.class, Where.eq("nid", "commu_seckey"));
		return (conf != null && conf.getValue().equals(seckey));
	}
	
	@RequestMapping("addCoin/{uid}")
	public @ResponseBody RespVO addCoin(@PathVariable("uid") Long id,Integer addCoin,String seckey) {
		if(!checkSafe(seckey)){
			return RespVO.createErrorJsonResonse("非法操作");
		}
		logger.error("收到后台命令addCoin：[{}]=[{}]",id,addCoin);
		CommonUtils.addCoin(id, addCoin);
		
		return RespVO.createSuccessJsonResonse("执行成功");
	}
}
