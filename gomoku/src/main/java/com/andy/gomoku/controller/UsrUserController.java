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
import com.andy.gomoku.dao.Where;
import com.andy.gomoku.entity.UsrGameInfo;
import com.andy.gomoku.entity.UsrUser;

@Controller
@RequestMapping("admin/user")
public class UsrUserController extends BaseController{

	/**
	 * 用户列表：结构
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="list")
	public ModelAndView list() throws Exception {
		TableHeader tableHeader = new TableHeader();
		tableHeader.setNames(new String[]{"id", "nick_name", "score","coin", "title", "win_count","role"});
		tableHeader.setTexts(new String[]{"ID", "昵称","积分","金币","段位","赢局","角色"});
		
		Tool tool = new Tool();
		tool.setList(buildTools("增加金币","admin/user/toAddCoin"));
		
		Search search = new Search();
		search.setNames(new String[]{"nick_name"});
		search.setTexts(new String[]{"昵称"});
		search.setTypes(new String[]{"text"});
		
		PageStructure data = PageUtil.createTablePageStructure("admin/user/listData", "id", tableHeader,tool,search);
		return createMV("tableList","用户管理", Collections.singletonMap("formStruct", data));
	}
	
	/**
	 * 用户列表：数据
	 * @param page
	 * @param limit
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="listData")
	public RespVO listData(Integer page,Integer limit, String nickName) throws Exception {
		PageVO users = DaoUtils.getPageForMap(UsrUser.table(), null,page,limit);
		
		idToName(users.getItems(), UsrGameInfo.table(), "id#uid:score,title,coin,win_count");
		
        return RespVO.createSuccessJsonResonse(users);
	}
	
	/**
	 * 增加金币:界面
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="toAddCoin")
	public ModelAndView toAddCoin(Long id) throws Exception {
		List<FormField> formFieldList = new ArrayList<>();
		formFieldList.add(FormField.builder().name("coin").text("拥有金币").type("span").build());
		formFieldList.add(FormField.builder().name("addCoin").text("增加金币").build());
		UsrGameInfo gameInfo = DaoUtils.getOne(UsrGameInfo.class,Where.eq("uid", id));
		Map<String, Object> data = PageUtil.createFormPageStructure("admin/user/addCoin", formFieldList,gameInfo);
		
		return createCustMV("window/add",data);
	}
	
	/**
	 * 增加金币：执行
	 * @param current_page
	 * @param areas
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="addCoin")
	public RespVO addCoin(Long id,Integer addCoin) throws Exception {
		UsrGameInfo gameInfo = DaoUtils.get(id, UsrGameInfo.class);
		if(addCoin > 0){
			gameInfo.setCoin(gameInfo.getCoin()+addCoin);
		}
		DaoUtils.update(gameInfo);
        return RespVO.createSuccessJsonResonse("增加成功");
	}
}
