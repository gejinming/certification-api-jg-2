package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM00001 extends CertificationJUnit {
	
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("loginName", "mngzsf");
		data.put("password", "1234");
		data.put("schoolId", "-1");
		HttpResponse httpResponse = super.api("EM00001", data).send();
		
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
}
