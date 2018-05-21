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
import com.andy.gomoku.entity.ConfTitle;
import com.andy.gomoku.utils.GameConf;
import com.google.common.collect.Maps;

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
		tableHeader.setNames(new String[]{"id", "title_sort","title", "min_scr", "max_scr"});
		tableHeader.setTexts(new String[]{"ID","段位等级", "段位名称","大于等于","小于积分"});
		
		Tool tool = new Tool();
		tool.setUrls("admin/confTitle/add","admin/confTitle/edit","admin/confTitle/delete","admin/confTitle/refreshCache");
		tool.setTexts("新增","编辑","删除","配置生效");
		tool.setTypes("add","edit","del","confirm");
		
		PageStructure data = PageUtil.createTablePageStructure("admin/confTitle/listData","id", tableHeader,tool,null);
		data.setExportTable(ConfTitle.table());
		return createMV("tableList","称号配置", Collections.singletonMap("formStruct", data));
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
		PageVO titles = DaoUtils.getPageForMap(ConfTitle.table(), null,"title_sort",page,limit);
		
        return RespVO.createSuccessJsonResonse(titles);
	}
	
	/**
	 * 编辑
	 * @param current_page
	 * @param areas
	 * @return
	 */
	@RequestMapping(value="add")
	public ModelAndView add() throws Exception {
		List<FormField> formFieldList = new ArrayList<>();
		formFieldList.add(FormField.builder().name("title").text("段位名称").build());
		formFieldList.add(FormField.builder().name("titleSort").text("段位等级").build());
		formFieldList.add(FormField.builder().name("minScr").text("大于等于").build());
		formFieldList.add(FormField.builder().name("maxScr").text("小于").build());
		
		Map<String, Object> data = PageUtil.createFormPageStructure("admin/confTitle/save", formFieldList);
		
		return createCustMV("window/add",data);
	}
	
	/**
	 * 新增
	 * @param current_page
	 * @param areas
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="save")
	public RespVO save(ConfTitle confTitle) throws Exception {
		DaoUtils.insert(confTitle);
		
		return RespVO.createSuccessJsonResonse("配置已保存");
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
		formFieldList.add(FormField.builder().name("id").text("ID").type("span").build());
		formFieldList.add(FormField.builder().name("title").text("段位名称").build());
		formFieldList.add(FormField.builder().name("titleSort").text("段位等级").build());
		formFieldList.add(FormField.builder().name("minScr").text("大于等于").build());
		formFieldList.add(FormField.builder().name("maxScr").text("小于").build());
		ConfTitle confTitle = DaoUtils.getOne(ConfTitle.class,Where.eq("id", id));
		
		Map<String, Object> data = PageUtil.createFormPageStructure("admin/confTitle/update", formFieldList,confTitle);
		
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
	public RespVO update(ConfTitle confTitle) throws Exception {
		DaoUtils.update(confTitle);
		
		return RespVO.createSuccessJsonResonse("配置已保存");
	}
	
	/**
	 * 删除
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="delete")
	public RespVO delete(Long id) throws Exception {
		DaoUtils.delete(ConfTitle.table(), id);
		return RespVO.createSuccessJsonResonse("删除成功");
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
			GameConf.initTitles();
		}else{
			ConfCommon conf = DaoUtils.getOne(ConfCommon.class, Where.eq("nid", "game_server_url"));
			ConfCommon secKey = DaoUtils.getOne(ConfCommon.class, Where.eq("nid", "commu_seckey"));
			Map<String,Object> params = Maps.newHashMap();
			params.put("seckey", secKey.getValue());
			HttpClientUtil.post(conf.getValue()+"admin/cmd/refreshCache/1", params, null);
		}
        return RespVO.createSuccessJsonResonse("配置已生效，需重新登录客户端验证");
	}
}
