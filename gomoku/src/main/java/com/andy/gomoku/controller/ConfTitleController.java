package com.andy.gomoku.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.andy.gomoku.base.PageUtil;
import com.andy.gomoku.base.PageVO;
import com.andy.gomoku.base.RespVO;
import com.andy.gomoku.base.form.FormField;
import com.andy.gomoku.base.table.PageStructure;
import com.andy.gomoku.base.table.Search;
import com.andy.gomoku.base.table.TableHeader;
import com.andy.gomoku.base.table.Tool;
import com.andy.gomoku.dao.DaoUtils;
import com.andy.gomoku.dao.vo.Where;
import com.andy.gomoku.entity.ConfTitle;
import com.andy.gomoku.entity.UsrGameInfo;
import com.andy.gomoku.entity.UsrUser;

@Controller
@RequestMapping("admin/confTitle")
public class ConfTitleController extends BaseController{

	/**
	 * 段位配置列表：结构
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="list")
	public ModelAndView list() throws Exception {
		TableHeader tableHeader = new TableHeader();
		tableHeader.setNames(new String[]{"id", "title", "min_scr", "max_scr"});
		tableHeader.setTexts(new String[]{"ID", "段位名称","大于等于","小于积分"});
		
		Tool tool = new Tool();
		tool.setUrls("admin/confTitle/refreshCache");
		tool.setTexts("配置生效");
		tool.setTypes("confirm");
		
		PageStructure data = PageUtil.createTablePageStructure("admin/confTitle/listData","admin/confTitle/update", "id", tableHeader,tool,null);
		return createMV("tableList","段位管理", Collections.singletonMap("formStruct", data));
	}
	
	/**
	 * 段位配置：数据
	 * @param page
	 * @param limit
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="listData")
	public RespVO listData(Integer page,Integer limit) throws Exception {
		PageVO titles = DaoUtils.getPageForMap(ConfTitle.table(), null,page,limit);
		
        return RespVO.createSuccessJsonResonse(titles);
	}
	
	/**
	 * 更新
	 * @param current_page
	 * @param areas
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="update")
	public RespVO update() throws Exception {
		
		return RespVO.createSuccessJsonResonse("配置已保存");
	}
	
	/**
	 * 配置生效
	 * @param current_page
	 * @param areas
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="refreshCache")
	public RespVO refreshCache() throws Exception {
		
        return RespVO.createSuccessJsonResonse("配置已生效，需重新登录客户端验证");
	}
}
