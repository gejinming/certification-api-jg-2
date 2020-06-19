package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestPermission extends CertificationAddBeforeJUnit {
	
	/**
	 * 测试功能权限
	 */
	@Test
	public void testExecute() {
		
		Map<String, Object> data = new HashMap<>();
		data.put("loginName", "admin4");
		data.put("password", "1234");
		HttpResponse httpResponse = super.api("EM00001", data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		System.out.println(object);
		Assert.assertEquals(4, object.getLongValue("id"));
		
//		//权限控制测试
		String token = object.getString("token");
		System.out.println(token);
		data.put("id", 1L);
		httpResponse = super.api("EM00101", token, data).send();
		this.isOk(httpResponse);
		response = this.parse(httpResponse);
	}

}
