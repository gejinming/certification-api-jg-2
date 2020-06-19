package com.gnet.certification;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.jgroups.util.UUID;

import com.alibaba.fastjson.JSON;
import com.gnet.api.Request;
import com.gnet.api.RequestHeader;
import com.gnet.api.Response;
import com.gnet.utils.DateUtils;
import com.jfinal.kit.JsonKit;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

public class CertificationJUnit {
	
	protected String ENCODING = "utf-8";
	protected Integer STATUS_OK = 200;
	
	private String URL = "http://127.0.0.1:8080/app/api";
	
	/**
	 * URL默认地址：http://127.0.0.1/certification-api/app/api
	 */
	public CertificationJUnit() {
	}
	
	public CertificationJUnit(String url) {
		this.URL = url;
	}
	
	/**
	 * 不传递token
	 */
	protected HttpRequest api(String trcode, Map<String, Object> data) {
		return this.api(trcode, null, data);
	}
	
	protected HttpRequest api(String trcode, String token, Map<String, Object> data) {
		return this.post(trcode, token, URL, data);
	}

	protected HttpRequest api(String trcode, String token,  String url, Map<String, Object> data) {
		return this.post(trcode, token, url, data);
	}

	protected HttpRequest post(String trcode, String token, String url, Map<String, Object> data) {
		HttpRequest httpRequest = HttpRequest
				.post(url)
				.formEncoding(ENCODING)
				.form("content", this.createContent(trcode, token, data));
		
		return httpRequest;
	}
	
	
	// -----------------------------Response validate
	
	protected CertificationJUnit isOk(HttpResponse response) {
		Assert.assertEquals(STATUS_OK, Integer.valueOf(response.statusCode()));
		return this;
	}
	
	protected Response parse(HttpResponse httpResponse) {
		String body = httpResponse.bodyText();
		Response response = JSON.parseObject(body, Response.class);
		
		return response;
	}
	
	protected File parseFile(String toPath, HttpResponse httpResponse) {
		if (httpResponse.contentType().indexOf("json") > -1) {
			return null;
		}
		
		File file = null;
		byte[] bodyBytes = httpResponse.bodyBytes();
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			file = new File(toPath);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			if (bodyBytes != null && bodyBytes.length > 0) {
				bos.write(bodyBytes);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
				
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return file;
	}
	
	// -----------------------------------------------
	
	
	private String createContent(String trcode, String token, Map<String, Object> data) {
		Request request = new Request();
		request.setData(data);
		request.setHeader(this.createHeader(trcode, token));
		
		return JSON.toJSONString(request);
	}
	
	private RequestHeader createHeader(String trcode, String token) {
		RequestHeader requestHeader = new RequestHeader();
		requestHeader.setAppseq(UUID.randomUUID().toString());
		requestHeader.setTrcode(trcode);
		requestHeader.setToken(token);
		requestHeader.setTrdate(DateUtils.getDate());
		
		return requestHeader;
	}
	
}
