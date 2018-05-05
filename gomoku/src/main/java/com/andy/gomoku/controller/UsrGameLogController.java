package com.andy.gomoku.controller;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.andy.gomoku.base.PageUtil;
import com.andy.gomoku.base.PageVO;
import com.andy.gomoku.base.RespVO;
import com.andy.gomoku.base.table.PageStructure;
import com.andy.gomoku.base.table.TableHeader;
import com.andy.gomoku.base.table.Tool;
import com.andy.gomoku.dao.DaoUtils;
import com.andy.gomoku.entity.ConfTitle;
import com.andy.gomoku.entity.UsrGameLog;
import com.andy.gomoku.entity.UsrUser;

@Controller
@RequestMapping("admin/playLog")
public class UsrGameLogController extends BaseController{

	/**
	 * 用户输赢记录列表：结构
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="list")
	public ModelAndView list() throws Exception {
		TableHeader tableHeader = new TableHeader();
		tableHeader.setNames(new String[]{"id", "nick_name", "coin", "add_coin","result","create_time"});
		tableHeader.setTexts(new String[]{"ID", "玩家名称","原金币","增加金币","结果","结算时间"});
		
		PageStructure data = PageUtil.createTablePageStructure("admin/playLog/listData", "id", tableHeader,null,null);
		return createMV("tableList","输赢日志", Collections.singletonMap("formStruct", data));
	}
	
	/**
	 * 用户输赢记录：数据
	 * @param page
	 * @param limit
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="listData")
	public RespVO listData(Integer page,Integer limit) throws Exception {
		PageVO logs = DaoUtils.getPageForMap(UsrGameLog.table(), null,page,limit);
		this.idToName(logs.getItems(), UsrUser.table(), "uid:nick_name");
		for(Map<String, Object> item:logs.getItems()){
			item.put("result", MapUtils.getInteger(item, "result") == 1?"赢":"输");
		}
        return RespVO.createSuccessJsonResonse(logs);
	}
	
	
}
