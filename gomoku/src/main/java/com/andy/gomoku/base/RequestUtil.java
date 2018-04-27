package com.andy.gomoku.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.andy.gomoku.entity.UsrAdmin;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class RequestUtil {

	protected static Logger logger = Logger.getLogger(RequestUtil.class);
	
	// 静态文件后缀
	private static String[] staticFiles = ".css,.js,.png,.jpg,.gif,.jpeg,.bmp,.ico,.swf,.psd,.htc,.crx,.xpi,.exe,.ipa,.apk".split(",");
	
	public static final String SESSION_USER = "_SESSION_USER_";
	
    /**
     * 获取request
     * @return
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttrs=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes());
        if(requestAttrs!=null)
            return requestAttrs.getRequest();
        return null;
    }
    
    /**
     * 获取用户登陆IP
     * @return
     */
    public static String getRemoteIp() {
        HttpServletRequest request = RequestUtil.getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null) ip = request.getRemoteAddr();
        if("0:0:0:0:0:0:0:1".equals(ip)) ip = "127.0.0.1";
        return ip;
    }
    
	/**
	 * @param request
	 * @param paramName
	 *            参数名称
	 * @return 从request获取参数对应字符串值
	 */
	public static String getString(HttpServletRequest request, String paramName) {
		String value = request.getParameter(paramName);
		return value ;
	}
	
	/**
	 * @param request
	 * @param paramName
	 *            参数名称
	 * @param define 默认值
	 * @return 从request获取参数对应字符串值
	 */
	public static String getString(HttpServletRequest request, String paramName, String define) {
		String value = request.getParameter(paramName);
		return StringUtils.isNotBlank(value) ? value : define;
	}
	
	/**
	 * @param request
	 * @param paramName
	 *            参数名称
	 * @return 从request获取参数对应整型值
	 */
	public static Integer getInteger(HttpServletRequest request, String paramName) {
		String value = request.getParameter(paramName);
		if (StringUtils.isBlank(value))
			return null;
		else {
			try {
				return Integer.parseInt(value);
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				return null;
			}
		}
	}

	/**
	 * @param request
	 * @param paramName
	 *            参数名称
	 * @param define 默认值           
	 * @return 从request获取参数对应整型值
	 */
	public static Integer getInteger(HttpServletRequest request, String paramName, Integer define) {
		String value = request.getParameter(paramName);
		if (StringUtils.isBlank(value))
			return define;
		else {
			try {
				return Integer.parseInt(value);
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				return define;
			}
		}
	}
	
	/**
	 * @param request
	 * @param paramName
	 *            参数名称
	 * @return 从request获取参数对应长整型值
	 */
	public static Long getLong(HttpServletRequest request, String paramName) {
		String value = request.getParameter(paramName);
		if (StringUtils.isBlank(value))
			return null;
		else {
			try {
				return Long.parseLong(value);
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				return null;
			}
		}

	}

	/**
	 * @param request
	 * @param paramName
	 *            参数名称
	 * @param define 默认值
	 * @return 从request获取参数对应长整型值
	 */
	public static Long getLong(HttpServletRequest request, String paramName,Long define) {
		String value = request.getParameter(paramName);
		if (StringUtils.isBlank(value))
			return define;
		else {
			try {
				return Long.parseLong(value);
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				return define;
			}
		}

	}
	
	/**
	 * @param request
	 * @param paramName
	 *            参数名称
	 * @return 从request获取参数对应双精度类型
	 */
	public static Double getDouble(HttpServletRequest request, String paramName) {
		String value = request.getParameter(paramName);
		if (StringUtils.isBlank(value))
			return null;
		else {
			try {
				return Double.parseDouble(value);
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				return null;
			}
		}

	}

	/**
	 * @param request
	 * @param paramName
	 *            参数名称
	 * @param define 默认值
	 * @return 从request获取参数对应双精度类型
	 */
	public static Double getDouble(HttpServletRequest request, String paramName,Double define) {
		String value = request.getParameter(paramName);
		if (StringUtils.isBlank(value))
			return define;
		else {
			try {
				return Double.parseDouble(value);
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				return define;
			}
		}

	}
	/**
	 * @param request
	 * @param paramName
	 *            参数名称
	 * @return 从request获取参数对应布尔值
	 */
	public static boolean getBoolean(HttpServletRequest request,String paramName) {
		String value = request.getParameter(paramName);
		if (StringUtils.isBlank(value))
			return false;
		else
			return Boolean.valueOf(value).booleanValue();
	}
	
	/**
	 * @param request
	 * @param paramName
	 *            参数名称
	 * @return 从request获取属性对应字符串值
	 */
	public static String getAttrString(HttpServletRequest request, String paramName) {
		String value = (String) request.getAttribute(paramName);
		return value ;
	}
	
	/**
	 * @param request
	 * @param paramName
	 *            参数名称
	 * @return 从request获取属性对应字符串值
	 */
	public static String getAttrString(HttpServletRequest request, String paramName, String define) {
		String value = (String) request.getAttribute(paramName);
		return StringUtils.isNotBlank(value) ? value : define;
	}
	
	/**
	 * @param request
	 * @param paramName
	 *            参数名称
	 * @return 从request获取参数对应整型值
	 */
	public static Integer getAttrInteger(HttpServletRequest request, String paramName) {
		String value = request.getAttribute(paramName).toString();
		if (StringUtils.isBlank(value))
			return null;
		else {
			try {
				return Integer.parseInt(value);
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				return null;
			}
		}
	}
	
	/**
	 * @param request
	 * @param paramName
	 *            参数名称
	 * @return 从request获取参数对应整型值
	 */
	public static Integer getAttrInteger(HttpServletRequest request, String paramName, Integer define) {
		String value = request.getAttribute(paramName).toString();
		if (StringUtils.isBlank(value))
			return define;
		else {
			try {
				return Integer.parseInt(value);
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				return define;
			}
		}
	}
	
	/**
	 * 判断ajax请求
	 * @param request
	 * @return
	 */
	public static boolean isAjax(HttpServletRequest request) {
	    return (request.getHeader("X-Requested-With") != null && "XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString()));
	}
	
	/**
	 * 获取登录user
	 * @return
	 */
	public static UsrAdmin getUser(){
		HttpSession session = getSession();
		if(session != null){
			return (UsrAdmin) session.getAttribute(SESSION_USER);
		}else{
			return null;
		}
	}
	
	/**
	 * 获取登录session
	 * @return
	 */
	public static HttpSession getSession(){
		HttpServletRequest request = getRequest();
		return request == null?null:getRequest().getSession();
	}
	
	/**
	 * Controller不绑定参数，从HttpServletRequest获取提交的所有参数，默认情况下String类型的数据会以String[]形式提交
	 * @return
	 */
	public static Map<String, Object> getRequestMap(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		//获取提交的所有参数
		Map<String, String[]> paramMap = request.getParameterMap();
		for(String key : paramMap.keySet()) {
			//参数值
			Object value = paramMap.get(key);
			if(value == null) continue;
			
			//判断是否需要转换
			if(value instanceof String[]) {
				String[] valueArray = (String[]) value;
				if(valueArray != null && valueArray.length == 1)
					result.put(key, valueArray[0]);
				else
					result.put(key, value);
			} else {
				result.put(key, value);
			}
		}
		return result;
	}

	/**
	 * 转换请求参数为map
	 * a[b]=1&a[c]=2 => map
	 * @param map 
	 * @param key
	 * @return
	 */
	public static Map<String,String> getParamByMap(String key) {
		return getParamByMap(getRequestMap(getRequest()),key);
	}
	
	/**
	 * 转换请求参数为map
	 * @param key
	 * @return
	 */
	public static Map<String,String> getParamByMap(Map<String, Object> map,String key) {
		Map<String,String> result = Maps.newHashMap();
		for(Entry<String, Object> entry:map.entrySet()){
			String k = entry.getKey();
			if(k.indexOf(key+"[")==0){
				// a[b]=0
				result.put(StringUtils.substringBetween(k, "[","]"), (String) entry.getValue());
			}
		}
		
		return result;
	}
	
	/**
	 * 转换请求参数为list
	 * @param requestMap
	 * @param key
	 * @return
	 */
	public static List<String> getParamByList(String key) {
		List<String> result = Lists.newArrayList();
		for(Entry<String, Object> entry:getRequestMap(getRequest()).entrySet()){
			String k = entry.getKey();
			if(k.indexOf(key+"[")==0){
				// a[b]=0
				result.add(Integer.parseInt(StringUtils.substringBetween(k, "[","]")),(String) entry.getValue());
			}
		}
		
		return result;
	}
	
	/**
	 * 获取basePath
	 * @return
	 */
	public static String getBasePath() {
		HttpServletRequest request = getRequest();
		
		StringBuffer basePath = new StringBuffer();
		basePath.append(request.getScheme()).append("://").append(request.getServerName());
		if(request.getServerPort() != 80) 
		basePath.append(":").append(request.getServerPort());
		basePath.append(request.getContextPath());
		
		return basePath.toString();
	}
	
	/**
	 * 获取登录用户id
	 */
	public static Long getUserId(){
		UsrAdmin user = getUser();
		if(user != null)return user.getId();
		return null;
	}
	
	/**
	 * 设置客户端缓存过期时间 的Header.
	 */
	public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
		// Http 1.0 header, set a fix expires date.
		response.setDateHeader(HttpHeaders.EXPIRES, System.currentTimeMillis() + expiresSeconds * 1000);
		// Http 1.1 header, set a time after now.
		response.setHeader(HttpHeaders.CACHE_CONTROL, "private, max-age=" + expiresSeconds);
	}

	/**
	 * 设置禁止客户端缓存的Header.
	 */
	public static void setNoCacheHeader(HttpServletResponse response) {
		// Http 1.0 header
		response.setDateHeader(HttpHeaders.EXPIRES, 1L);
		response.addHeader(HttpHeaders.PRAGMA, "no-cache");
		// Http 1.1 header
		response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0");
	}

	/**
	 * 设置LastModified Header.
	 */
	public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate) {
		response.setDateHeader(HttpHeaders.LAST_MODIFIED, lastModifiedDate);
	}

	/**
	 * 设置Etag Header.
	 */
	public static void setEtag(HttpServletResponse response, String etag) {
		response.setHeader(HttpHeaders.ETAG, etag);
	}

	/**
	 * 根据浏览器If-Modified-Since Header, 计算文件是否已被修改.
	 * 如果无修改, checkIfModify返回false ,设置304 not modify status.
	 * @param lastModified 内容的最后修改时间.
	 */
	public static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response,
			long lastModified) {
		long ifModifiedSince = request.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE);
		if ((ifModifiedSince != -1) && (lastModified < ifModifiedSince + 1000)) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return false;
		}
		return true;
	}
	
	/**
     * 判断访问URI是否是静态文件请求
	 * @throws Exception 
     */
    public static boolean isStaticFile(String uri){
		if (StringUtils.endsWithAny(uri, staticFiles)){
			return true;
		}
		return false;
    }
}
