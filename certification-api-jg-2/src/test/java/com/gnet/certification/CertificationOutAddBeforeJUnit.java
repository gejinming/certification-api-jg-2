package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.plugin.configLoader.ConfigLoader;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

public class CertificationOutAddBeforeJUnit extends CertificationJUnit{

	private String URL = "http://127.0.0.1:8080/outer/api/v1";
	protected String token;

	public CertificationOutAddBeforeJUnit() {
	}

	public CertificationOutAddBeforeJUnit(String url) {
		super(url);
	}

	protected HttpRequest api(String trcode, String token, Map<String, Object> data) {
		return this.post(trcode, token, URL, data);
	}

	@Before
	public void before() {
		this.InitProperties();
		this.getToken();
	}
	
	/**
	 * 初始化配置文件
	 */
	public void InitProperties() {
		ConfigLoader.load("classpath:config");
	}
	
	/**
	 * 获取token
	 */
	public void getToken() {
		Map<String, Object> data = new HashMap<>();
		data.put("loginName", "admin");
		data.put("password", "1234");
		HttpResponse httpResponse = super.api("EM00001", data).send();
		
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		
		this.token = object.getString("token");
	}
	
}
