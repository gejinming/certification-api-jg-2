package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00330 extends CertificationAddBeforeJUnit{
	
	@Test
	public void testExecute(){
		
		Long planId = ConfigUtils.getLong("junitid", "plan.id");
		
		Map<String, Object> data = new HashMap<>();
		data.put("planId", planId);
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("orderProperty", "moduleName");
		data.put("orderDirection", "asc");
		HttpResponse httpResponse = super.api("EM00330", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
		
	}
	
	@Test
	public void testPageSearchModuleNameExecute(){
		
		Long planId = ConfigUtils.getLong("junitid", "plan.id");
		
		Map<String, Object> data = new HashMap<>();
		data.put("planId", planId);
		data.put("moduleName", "专业认证");
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("orderProperty", "moduleName");
		data.put("orderDirection", "asc");
		HttpResponse httpResponse = super.api("EM00330", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
		
	}

}
