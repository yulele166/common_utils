package com.peanut.commons.utils;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 */
public class HttpRequest {

	private String url;
	private Map<String, String> requestParam = new LinkedHashMap<>();
	private Map<String, String> headerParam = new LinkedHashMap<>();

	private HttpRequest(String url) {
		this.url = url;
	}

	/**
	 * 新请求
	 * @param url
	 * @return
	 */
	public static HttpRequest newRequest(String url) {
		return new HttpRequest(url);
	}

	/**
	 * 添加请求参数
	 * @param key
	 * @param value
	 * @return
	 */
	public HttpRequest addParam(String key,Object value) {
		this.requestParam.put(key, value.toString());
		return this;
	}

	/**
	 * 添加请求头
	 * @param key
	 * @param value
	 * @return
	 */
	public HttpRequest addHeader(String key,String value) {
		this.headerParam.put(key, value);
		return this;
	}

	/**
	 * 获取body
	 * @return
	 */
	public String getRequestBodyString(){
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : requestParam.entrySet()) {
			sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
		}
//		try {
//			return URLEncoder.encode(sb.deleteCharAt(0).toString(),"UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			throw new RuntimeException("无法encode",e);
//		}

		System.out.println("此处的是:" + sb.toString());
		return sb.deleteCharAt(0).toString();
	}

	/**
	 * 发送post请求,返回json对象
	 */
	public JSONObject post() throws Exception {
		return JSON.parseObject(postAsString());
	}

	/**
	 * 发送post请求,返回原始字符串
	 * @return
	 * @throws Exception
	 */
	public String postAsString() throws Exception {

		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<>();

		for (Map.Entry<String, String> entry : requestParam.entrySet()) {
			nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		httpPost.setEntity(new UrlEncodedFormEntity(nvps));

		for (Map.Entry<String, String> entry : headerParam.entrySet()) {
			httpPost.setHeader(entry.getKey(), entry.getValue());
		}

		return HttpUtils.post(httpPost);
	}

	/**
	 * 发送get请求
	 * @throws Exception
	 */
	public JSONObject get() throws Exception{

		StringBuilder urlBuilder = new StringBuilder(url);

		if (!requestParam.isEmpty()) {

			List<NameValuePair> uriParam = new ArrayList<>();
			for (Map.Entry<String, String> entry : requestParam.entrySet()) {
				uriParam.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			urlBuilder.append("?").append(URLEncodedUtils.format(uriParam, Consts.UTF_8));

		}

		URI uri = new URI(urlBuilder.toString());
		HttpGet httpGet = new HttpGet(uri);

		for (Map.Entry<String, String> entry : headerParam.entrySet()) {
			httpGet.setHeader(entry.getKey(),entry.getValue());
		}

		return JSON.parseObject(HttpUtils.get(httpGet));
	}


	public static void main(String [] args){
		try {
			JSONObject post = HttpRequest.newRequest("http://www.baidu.com").get();
			System.out.println(post.toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
