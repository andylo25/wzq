package com.andy.gomoku.controller;

import java.util.Collections;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.andy.gomoku.base.PageUtil;
import com.andy.gomoku.base.PageVO;
import com.andy.gomoku.base.RespVO;
import com.andy.gomoku.base.table.PageStructure;
import com.andy.gomoku.base.table.Search;
import com.andy.gomoku.base.table.TableHeader;
import com.andy.gomoku.base.table.Tool;
import com.andy.gomoku.dao.DaoUtils;
import com.andy.gomoku.entity.UsrUser;

@Controller
@RequestMapping("admin/user")
public class UsrUserController extends BaseController{

	/**
	 * 文章管理页面结构渲染
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="list")
	public ModelAndView list() throws Exception {
		TableHeader tableHeader = new TableHeader();
		tableHeader.setNames(new String[]{"id", "title", "author", "category_id", "status", "create_time:datetime","sort_index"});
		tableHeader.setTexts(new String[]{"ID", "标题", "作者", "分类栏目:art_page", "是否发布:common_status", "时间","排序"});
		
		Tool tool = new Tool();
//		tool.setList(buildTools());
		
		Search search = new Search();
		search.setNames(new String[]{"title"});
		search.setTexts(new String[]{"标题"});
		search.setTypes(new String[]{"text"});
		
		PageStructure data = PageUtil.createTablePageStructure("content/article/listData", "id", tableHeader,tool,search);
		return createMV("tableList","用户管理", Collections.singletonMap("formData", data));
	}
	
	/**
	 * 文章管理列表数据检索
	 * @param current_page
	 * @param areas
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="listData")
	public RespVO listData(Integer current_page, String author, String title, Integer category_id, Integer status) throws Exception {
		PageVO users = DaoUtils.getPageForMap(UsrUser.table(), null,1,10);
		
		idToName(users.getItems(), UsrUser.table(), "uid:nick_name");
		
        return RespVO.createSuccessJsonResonse(Collections.singletonMap("formData", users));
	}
	
	
	
}
