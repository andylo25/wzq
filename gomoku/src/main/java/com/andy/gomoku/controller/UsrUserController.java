package com.andy.gomoku.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.andy.gomoku.base.table.Search;
import com.andy.gomoku.base.table.TableHeader;
import com.andy.gomoku.base.table.Tool;
import com.andy.gomoku.dao.DaoUtils;
import com.andy.gomoku.dao.vo.Where;
import com.andy.gomoku.entity.ConfCommon;
import com.andy.gomoku.entity.UsrGameInfo;
import com.andy.gomoku.entity.UsrUser;
import com.andy.gomoku.game.Global;
import com.andy.gomoku.utils.CommonUtils;
import com.andy.gomoku.utils.GameConf;
import com.andy.gomoku.utils.SendUtil;
import com.andy.gomoku.websocket.MySocketSession;
import com.google.common.collect.Maps;

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
		tableHeader.setNames(new String[]{"id", "nick_name", "coin", "title", "win_count","role"});
		tableHeader.setTexts(new String[]{"ID", "昵称","金币","段位","赢局","角色"});
		
		Tool tool = new Tool();
		tool.setUrls("admin/user/toAddCoin");
		tool.setTexts("增加金币");
		tool.setTypes("edit");
		
		Search search = new Search();
		search.setNames(new String[]{"uid"});
		search.setTexts(new String[]{"玩家ID"});
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
	public RespVO listData(Integer page,Integer limit, String uid) throws Exception {
		PageVO users = null;
		if(StringUtils.isNotBlank(uid)){
			users = DaoUtils.getPageForMap(UsrUser.table(), null,page,limit,Where.eq("id", uid));
		}else{
			users = DaoUtils.getPageForMap(UsrUser.table(), null,page,limit);
		}
		
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
		formFieldList.add(FormField.builder().name("uid").text("玩家ID").type("span").build());
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
		if(GomokuApplication.gameServerEnable()){
			CommonUtils.addCoin(id, addCoin);
		}else{
			ConfCommon conf = DaoUtils.getOne(ConfCommon.class, Where.eq("nid", "game_server_url"));
			ConfCommon secKey = DaoUtils.getOne(ConfCommon.class, Where.eq("nid", "commu_seckey"));
			Map<String,Object> params = Maps.newHashMap();
			params.put("seckey", secKey.getValue());
			params.put("addCoin", addCoin);
			HttpClientUtil.post(conf.getValue()+"admin/cmd/addCoin/"+id, params, null);
		}
		
        return RespVO.createSuccessJsonResonse("增加成功");
	}

}
