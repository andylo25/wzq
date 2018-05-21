package com.andy.gomoku.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.andy.gomoku.GomokuApplication;
import com.andy.gomoku.base.HttpClientUtil;
import com.andy.gomoku.base.PageUtil;
import com.andy.gomoku.base.PageVO;
import com.andy.gomoku.base.RespVO;
import com.andy.gomoku.base.form.FormField;
import com.andy.gomoku.base.table.PageStructure;
import com.andy.gomoku.base.table.TableHeader;
import com.andy.gomoku.base.table.Tool;
import com.andy.gomoku.dao.DaoUtils;
import com.andy.gomoku.dao.vo.Where;
import com.andy.gomoku.entity.ConfCommon;
import com.andy.gomoku.entity.UsrGameInfo;
import com.andy.gomoku.game.Global;
import com.andy.gomoku.utils.GameConf;
import com.google.common.collect.Maps;

@Controller
@RequestMapping("admin/confCommon")
public class ConfCommonController extends BaseController{

	/**
	 * 参数配置列表：结构
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="list")
	public ModelAndView list() throws Exception {
		TableHeader tableHeader = new TableHeader();
		tableHeader.setNames(new String[]{"id", "nid", "value", "remark"});
		tableHeader.setTexts(new String[]{"ID", "参数名","参数值","参数描述"});
		
		Tool tool = new Tool();
		tool.setUrls("admin/confCommon/edit","admin/confCommon/refreshCache");
		tool.setTexts("编辑","配置生效");
		tool.setTypes("edit","confirm");
		
		PageStructure data = PageUtil.createTablePageStructure("admin/confCommon/listData", "id", tableHeader,tool,null);
		return createMV("tableList","参数配置", Collections.singletonMap("formStruct", data));
	}
	
	/**
	 * 参数配置：数据
	 * @param page
	 * @param limit
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="listData")
	public RespVO listData(Integer page,Integer limit) throws Exception {
		PageVO cc = DaoUtils.getPageForMap(ConfCommon.table(), null,page,limit);
		
        return RespVO.createSuccessJsonResonse(cc);
	}
	
	/**
	 * 编辑
	 * @param current_page
	 * @param areas
	 * @return
	 */
	@RequestMapping(value="edit")
	public ModelAndView edit(Long id) throws Exception {
		List<FormField> formFieldList = new ArrayList<>();
		formFieldList.add(FormField.builder().name("nid").text("参数名").type("span").build());
		formFieldList.add(FormField.builder().name("value").text("参数值").build());
		formFieldList.add(FormField.builder().name("remark").text("参数描述").build());
		ConfCommon confCommon = DaoUtils.getOne(ConfCommon.class,Where.eq("id", id));
		
		Map<String, Object> data = PageUtil.createFormPageStructure("admin/confCommon/update", formFieldList,confCommon);
		
		return createCustMV("window/add",data);
	}
	
	/**
	 * 更新
	 * @param current_page
	 * @param areas
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="update")
	public RespVO update(ConfCommon confCommon) throws Exception {
		DaoUtils.update(confCommon);
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
		if(GomokuApplication.gameServerEnable()){
			GameConf.initCommons();
		}else{
			ConfCommon conf = DaoUtils.getOne(ConfCommon.class, Where.eq("nid", "game_server_url"));
			ConfCommon secKey = DaoUtils.getOne(ConfCommon.class, Where.eq("nid", "commu_seckey"));
			Map<String,Object> params = Maps.newHashMap();
			params.put("seckey", secKey.getValue());
			HttpClientUtil.post(conf.getValue()+"admin/cmd/refreshCache/2", params, null);
		}
        return RespVO.createSuccessJsonResonse("配置已生效，需重新登录客户端验证");
	}
}
