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
	
		initTitles();
		
		initCommons();
		
	}

	public static void initCommons() {
		Map<String,ConfCommon> confCommonsT = Maps.newHashMap();
		List<ConfCommon> commons = DaoUtils.getList(ConfCommon.class);
		for(ConfCommon comm:commons){
			confCommonsT.put(comm.getNid(), comm);
			initConf(comm);
		}
		confCommons = confCommonsT;
	}

	public static void initTitles() {
		Map<Integer,ConfTitle> titlesT = Maps.newHashMap();
		List<ConfTitle> cts = DaoUtils.getList(ConfTitle.class,"title_sort",null);
		for(ConfTitle ct:cts){
			titlesT.put(ct.getTitleSort(), ct);
		}
		
		titles = titlesT;
		
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
