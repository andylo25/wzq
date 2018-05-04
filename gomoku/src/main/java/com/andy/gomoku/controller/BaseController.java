package com.andy.gomoku.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.servlet.ModelAndView;

import com.andy.gomoku.base.BaseUser;
import com.andy.gomoku.base.RequestUtil;
import com.andy.gomoku.base.table.Tool;
import com.andy.gomoku.dao.DaoUtils;
import com.andy.gomoku.dao.vo.Where;
import com.andy.gomoku.utils.JsonUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class BaseController {

	public static ModelAndView createMV(String viewName,String pageTitle, Object data) {
		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.addObject("viewName", viewName);
		modelAndView.addObject("pageTitle", pageTitle);
		modelAndView.addObject("user", RequestUtil.getUser());
		modelAndView.addObject("userJson", JsonUtils.object2JsonString(RequestUtil.getUser()));
		if(data instanceof String){
			modelAndView.addObject("data", data);
		}else{
			modelAndView.addObject("data", JsonUtils.object2JsonString(data));
		}
		return modelAndView;
	}
	
	public static ModelAndView createCustMV(String viewName, Object data) {
		ModelAndView modelAndView = new ModelAndView(viewName);
		modelAndView.addObject("user", getUser());
		if(data instanceof String){
			modelAndView.addObject("data", data);
		}else{
			modelAndView.addObject("data", JsonUtils.object2JsonString(data));
		}
		return modelAndView;
	}
	
	public static BaseUser getUser() {
		return RequestUtil.getUser();
	}
	
	public static Long getUserId() {
		return RequestUtil.getUserId();
	}
	
	/**
	 * 转换id为名称
	 * @param data 待转换的数据
	 * @param module 关联表模块
	 * @param function 关联表名
	 * @param cols 格式：src_col#tar_col:tar_col1,tar_col2
	 * @return
	 * @throws Exception 
	 */
	public static Object idToName(Map<String, Object> data, String table, String cols){
		if(data != null){
			String[] column = cols.split(":");
			String[] cs = column[0].split("#");
			String[] cs1 = cs[0].split(",");
			String field = cs1[0];
			String field1 = null;
			if(cs1.length > 1){
				field1 = cs1[1];
			}
			String tarCol = "id";
			String tarCol1 = null;
			if(cs.length > 1){
				String[] tarCola = cs[1].split(",");
				tarCol = tarCola[0];
				if(tarCola.length > 1){
					tarCol1 = tarCola[1];
				}
			}
			String[] colss = column[1].split(",");
			String id = data.get(field).toString();
			List<Map<String, Object>> maps = null;
			if(field1 != null) {
				maps = DaoUtils.getListMap(table, tarCol+","+column[1],null, Where.eq(tarCol, id),Where.eq(tarCol1, field1));
			}else {
				maps = DaoUtils.getListMap(table, tarCol+","+column[1],null, Where.eq(tarCol, id));
			}
			if(maps != null && !maps.isEmpty()){
				Map ent = maps.get(0);
				for(String col:colss){
					// col_1 as col1
					String[] cola = col.split(" ");
					String colas = cola[cola.length-1];
					data.put(colas, ent.get(colas));
				}
			}
		}
		
		return data;
	}
	
	/**
	 * 转换id为名称
	 * @param data 待转换的列表
	 * @param module 关联表模块
	 * @param function 关联表名
	 * @param cols 格式：src_col#tar_col:tar_col1,tar_col2
	 * @return
	 * @throws Exception 
	 */
	public static Object idToName(List<Map<String, Object>> data, String table, String cols){
		if(data != null && !data.isEmpty()) {
			String[] column = cols.split(":");
			String[] cs = column[0].split("#");
			String[] cs1 = cs[0].split(",");
			String field = cs1[0];
			String field1 = null;
			if(cs1.length > 1){
				field1 = cs1[1];
			}
			String tarCol = "id";
			String tarCol1 = null;
			if(cs.length > 1){
				String[] tarCola = cs[1].split(",");
				tarCol = tarCola[0];
				if(tarCola.length > 1){
					tarCol1 = tarCola[1];
				}
			}
			String[] colss = column[1].split(",");
			Set<String> ids = Sets.newHashSet();
			for(int i=0;i<data.size();i++){
				Object f = data.get(i).get(field);
				if(f != null){
					ids.add(f.toString());
				}
			}
			List<Map<String, Object>> maps = null;
			if(field1 != null){
				maps = DaoUtils.getListMap(table, tarCol+","+column[1],null, Where.in(tarCol, ids),Where.eq(tarCol1, field1));
			}else {
				maps = DaoUtils.getListMap(table, tarCol+","+column[1],null, Where.in(tarCol, ids));
			}
			if(maps != null){
				for(Map map:data){
					Object v = map.get(field);
					if(v != null){
						Map ent = getEntityById(maps,tarCol,v.toString());
						if(ent != null){
							for(String col:colss){
								// col_1 as col1
								String[] cola = col.split(" ");
								String colas = cola[cola.length-1];
								map.put(colas, ent.get(colas));
							}
						}
					}
				}
			}
		}
		
		return data;
		
	}
	
	private static Map getEntityById(List<Map<String, Object>> maps, String key, String id) {
		if(maps!= null && id != null){
			for(Map map:maps){
				Object v = map.get(key);
				if(v != null){
					if(v.toString().equals(id)){
						return map;
					}
				}
			}
		}
		return null;
	}
	
	public static List<Tool> buildTools(String... strs) {
		List<Tool> tools = Lists.newArrayList();
		for(int i=0;i<strs.length;i+=2) {
			Tool tool = new Tool();
			tool.setType("edit");
			tool.setText(strs[i]);
			tool.setUrl(strs[i+1]);
			tools.add(tool);
		}
		return tools;
	}
	
}
