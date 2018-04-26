package com.andy.gomoku.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class JsonUtils {

	public static final Gson gson = new Gson();
	public static final GsonBuilder gb = new GsonBuilder();

	public static <T> T json2Object(Object data, Class<T> clazz) {
		return gson.fromJson(gson.toJson(data), clazz);
	}

	public static String object2JsonString(Object obj) {
		if (obj == null) {
			return null;
		}
		return gson.toJson(obj);
	}

	public static <T> T fromJson(String data, Class<T> clazz) {
		return gson.fromJson(data, clazz);
	}

	/**
	 * @author cuiwm
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> jsonToList(String json, Class<T[]> clazz) {
		Gson gson = new Gson();
		T[] array = gson.fromJson(json, clazz);
		return Arrays.asList(array);
	}

	/**
	 * @author cuiwm
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz) {
		Type type = new TypeToken<ArrayList<JsonObject>>() {
		}.getType();
		ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);

		ArrayList<T> arrayList = new ArrayList<>();
		for (JsonObject jsonObject : jsonObjects) {
			arrayList.add(new Gson().fromJson(jsonObject, clazz));
		}
		return arrayList;
	}

	/**
	 * 转化成Json，不转化hmtl字符及不忽略空值
	 * 
	 * @param obj
	 * @return
	 */
	public static String object2JsonNoEscaping(Object obj) {
		gb.disableHtmlEscaping();
		gb.serializeNulls();
		return gb.create().toJson(obj);
	}
	
	public static Map<String,Object> json2Map(String json){
		return gson.fromJson(json, Map.class);
	}

}
