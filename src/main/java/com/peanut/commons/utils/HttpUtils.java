package com.peanut.commons.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * http请求工具类 ，发送相关get、post请求
 */
public class HttpUtils {
	private static final CloseableHttpClient httpclient = HttpClients.createDefault();

	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					httpclient.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		});
	}

	private HttpUtils() {
	}

	public static String get(String scheme, String host, String path, List<NameValuePair> uriParam,
			List<NameValuePair> header) throws IOException {
		URIBuilder uriBuilder = new URIBuilder().setScheme(scheme).setHost(host).setPath(path);
		if (uriParam != null)
			uriBuilder.setParameters(uriParam);

		try {
			HttpGet httpget = new HttpGet(uriBuilder.build());
			if (header != null) {
				for (NameValuePair nvp : header) {
					httpget.setHeader(nvp.getName(), nvp.getValue());
				}
			}
			ResponseHandler<String> responseHandler = getResponseHandler();

			return httpclient.execute(httpget, responseHandler);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public static String get(HttpGet httpGet) throws IOException {
		ResponseHandler<String> responseHandler = getResponseHandler();
		return httpclient.execute(httpGet, responseHandler);
	}

	public static String post(HttpPost httpPost) throws IOException {
		ResponseHandler<String> responseHandler = getResponseHandler();
		return httpclient.execute(httpPost, responseHandler);
	}

	/**
	 * 将原来get方法中ResponseHandler提成方法，由get/post共用
	 * 
	 * @return
	 */
	private static ResponseHandler<String> getResponseHandler() {
		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

			public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					System.err.println(EntityUtils.toString(response.getEntity()));
					throw new ClientProtocolException("Unexpected response status: " + status);
				}
			}

		};
		return responseHandler;
	}

	public static void main(String[] args) throws Exception {
	        Locale[] list = Locale.getAvailableLocales();  
	        for( int i = 0; i < list.length; i++ ) {  
	            //打印出所支持的国家和语言  
	            System.out.println("    --国家--   " +  
	                    list[i].getDisplayCountry()  
	                    + "=" + list[i].getCountry()  
	                    + "    --语言--   "   
	                    + list[i].getDisplayLanguage()  
	                    + "=" + list[i].getLanguage());  
	        }  
	}
	

}
