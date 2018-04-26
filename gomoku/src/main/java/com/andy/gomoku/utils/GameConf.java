package com.andy.gomoku.utils;

import java.util.List;
import java.util.Map;

import com.andy.gomoku.dao.DaoUtils;
import com.andy.gomoku.entity.ConfCommon;
import com.andy.gomoku.entity.ConfTitle;
import com.andy.gomoku.game.Global;
import com.google.common.collect.Maps;

public class GameConf {

	
	private static Map<Integer,ConfTitle> titles;
	private static Map<String,ConfCommon> confCommons;

	
	public static void init() {
	
		titles = Maps.newHashMap();
		List<ConfTitle> cts = DaoUtils.getList(ConfTitle.class,"title_sort",null);
		for(ConfTitle ct:cts){
			titles.put(ct.getTitleSort(), ct);
		}
		
		confCommons = Maps.newHashMap();
		List<ConfCommon> commons = DaoUtils.getList(ConfCommon.class);
		for(ConfCommon comm:commons){
			confCommons.put(comm.getNid(), comm);
			initConf(comm);
		}
		
	}
	
	public static String[] firstNames;
	public static String[] secondNames;
	public static void initConf(ConfCommon comm) {
		if(comm.getNid().equals("first_name")){
			firstNames = comm.getValue().split(",");
		}else if(comm.getNid().equals("second_name")){
			secondNames = comm.getValue().split(",");
		}
	}
	
	public static ConfTitle getTitle(int ts){
		return titles.get(ts);
	}
	
	public static int getConfInt(String nid){
		String v = getConfStr(nid);
		if(v != null){
			return Integer.parseInt(v);
		}
		return 0;
	}
	public static String getConfStr(String nid){
		ConfCommon common = confCommons.get(nid);
		if(common != null){
			return common.getValue();
		}
		return null;
	}

	public static int getTitleSort(int score) {
		for(ConfTitle title:titles.values()){
			if(title.inScr(score)){
				return title.getTitleSort();
			}
		}
		return 0;
	}
	
}
