package com.andy.gomoku.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andy.gomoku.exception.GoSeviceException;

public class HttpClientUtil  {

	private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	
	public static String post(String url, String strBody,String charset) {
		RequestConfig requestConfig = getRequestConfig();
		if(charset == null)charset = "UTF-8";
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpPost request = null;
		try {
			// 根据地址获取请求
			request = new HttpPost(url);// 这里发送post请求

			// 创建默认的httpClient实例.
			httpClient = HttpClients.createDefault();
			StringEntity stringEntity = new StringEntity(strBody, "UTF-8");
			request.setConfig(requestConfig);
			request.setEntity(stringEntity);
			// 重试策略
//			httpClient.setHttpRequestRetryHandler(getRetryHandler(connectPolicy
//					.getRetryTimes()));
			// 执行请求
			response = httpClient.execute(request);

			if(response != null && response.getEntity() != null){
				return EntityUtils.toString(response.getEntity(), charset);
			}
			return "";
		} catch (Exception e) {
			throw new GoSeviceException("发送请求异常",e);
		} finally {
			try {
				// 关闭连接,释放资源
				request.abort();
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				logger.error("发送请求关闭连接异常",e);
			}
		}
	}

	private static RequestConfig getRequestConfig() {
		return RequestConfig.custom().setSocketTimeout(600000).setConnectTimeout(5000)
				.setConnectionRequestTimeout(2000).build();
	}
	
	protected HttpRequestRetryHandler getRetryHandler(int retryTimes) {
		return new DefaultHttpRequestRetryHandler(retryTimes, false);
	}

	public static String post(String url, Map<String, ?> params,String charset) {
		RequestConfig requestConfig = getRequestConfig();
		if(charset == null)charset = "UTF-8";
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpPost request = null;
		try {
			// 根据地址获取请求
			request = new HttpPost(url);// 这里发送post请求
			request.setConfig(requestConfig);

			// 创建默认的httpClient实例.
			httpClient = HttpClients.createDefault();
			
			//设置参数  
            List<NameValuePair> list = new ArrayList<NameValuePair>();  
            for(Entry<String, ?> elem:params.entrySet()){
            	if(elem.getValue() != null){
            		list.add(new BasicNameValuePair(elem.getKey(),elem.getValue().toString()));  
            	}
            }
            if(list.size() > 0){  
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,"UTF-8");  
                request.setEntity(entity);  
            } 
			// 重试策略
//			httpClient.setHttpRequestRetryHandler(getRetryHandler(connectPolicy
//					.getRetryTimes()));
			// 执行请求
			response = httpClient.execute(request);

			if(response != null && response.getEntity() != null){
				return EntityUtils.toString(response.getEntity(), charset);
			}
			return "";
		} catch (Exception e) {
			throw new GoSeviceException("发送请求异常",e);
		} finally {
			try {
				// 关闭连接,释放资源
				request.abort();
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				logger.error("发送请求关闭连接异常",e);
			}
		}
	}
	


}
