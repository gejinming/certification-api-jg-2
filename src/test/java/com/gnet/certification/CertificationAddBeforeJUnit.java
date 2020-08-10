package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.plugin.configLoader.ConfigLoader;

import jodd.http.HttpResponse;

public class CertificationAddBeforeJUnit extends CertificationJUnit{
	
	protected String token;
	
	public CertificationAddBeforeJUnit() {
	}
	
	public CertificationAddBeforeJUnit(String url) {
		super(url);
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
