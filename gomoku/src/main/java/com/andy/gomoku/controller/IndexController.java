package com.andy.gomoku.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.andy.gomoku.base.PageUtil;
import com.andy.gomoku.base.PageVO;
import com.andy.gomoku.base.RespVO;
import com.andy.gomoku.base.form.FormField;
import com.andy.gomoku.dao.DaoUtils;
import com.andy.gomoku.dao.vo.GenTable;
import com.andy.gomoku.dao.vo.GenTableColumn;
import com.andy.gomoku.entity.UsrGameInfo;
import com.andy.gomoku.entity.UsrUser;
import com.andy.gomoku.utils.excel.ExportExcel;
import com.andy.gomoku.utils.excel.ImportExcel;
import com.google.common.collect.Maps;

@Controller
public class IndexController extends BaseController{

	@RequestMapping("")  
    public ModelAndView index() {  
		Map map = Maps.newHashMap();
		PageVO users = DaoUtils.getPageForMap(UsrUser.table(), null,1,10);
		map.put("users", users.getItems());
		map.put("userCount", users.getTotal_items());
		List<Map<String, Object>> gameInfos = DaoUtils.getListMap(UsrGameInfo.table(),null,10);
		idToName(gameInfos, UsrUser.table(), "uid:nick_name");
		map.put("gameInfos", gameInfos);
        return createMV("dashboard","管理面板", Collections.singletonMap("formData", map));
    }  
	
	/**
	 * 导入excel
	 * @param areas
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="importExcel/{table}")
	public RespVO importExcel(MultipartFile upload,@PathVariable("table") String table) throws Exception {
		if(upload == null){
			return RespVO.createErrorJsonResonse("请选择Excel文件");
		}
		ImportExcel excel = new ImportExcel(upload, 1,0);
		List<Map<String, Object>> list = excel.getDataList();
		if(list != null && !list.isEmpty()){
			DaoUtils.delete(table);
			for(Map entity:list){
				entity.put("table_", table);
				DaoUtils.insert(entity);
			}
		}
		
		return RespVO.createSuccessJsonResonse("导入成功");
	}
	
	/**
	 * 导入界面
	 * @param table
	 * @return
	 */
	@RequestMapping("toImportExcel/{table}")  
    public ModelAndView toExportExcel(@PathVariable("table") String table) {
		List<FormField> formFieldList = new ArrayList<>();
		formFieldList.add(FormField.builder().name("table").text("导入表").type("span").build());
		formFieldList.add(FormField.builder().name("upload").text("选择文件").type("upload").build());
		
		Map<String, Object> data = PageUtil.createFormPageStructure("importExcel/"+table, formFieldList,Collections.singletonMap("table", table));
		
		return createCustMV("window/add",data);
    }  
	
	/**
	 * 导出excel
	 * @param areas
	 * @return
	 */
	@RequestMapping(value="exportExcel/{table}")
	public String exportExcel(HttpServletResponse response,@PathVariable("table") String table) throws Exception {
		GenTable genTable = DaoUtils.getTables(table);
		String fileName = genTable.getComments();
		if(StringUtils.isBlank(fileName)) fileName = table;
		
		
		List<GenTableColumn> columnList = genTable.getColumnList().stream().filter(new Predicate<GenTableColumn>() {
			public boolean test(GenTableColumn t) {
				if("create_uid,create_time,update_uid,update_time,del_flag".indexOf(t.getName()) >= 0){
					return false;
				}
				return true;
			}
		}).collect(Collectors.toList());
		
		String[] colComms = new String[columnList.size()];
		String[] cols = new String[columnList.size()];
		for(int i=0;i<columnList.size();i++){
			GenTableColumn column = columnList.get(i);
			colComms[i] = column.getComments();
			cols[i] = column.getName();
		}
		ExportExcel excel = new ExportExcel(colComms);
		excel.setFieldNames(cols);
		
		List list = DaoUtils.getListSql("select * from "+table);
		excel.setDataList(list);
		
		excel.write(response, fileName+".xlsx");
		
		return null;
	}
	
}
