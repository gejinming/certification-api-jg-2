package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM00543 extends CertificationAddBeforeJUnit {
	
	
	@Test
	public void testExecute() {
		Long courseModuleId = 162393L;
		Map<String, Object> data = new HashMap<>();
		Long[] ids = new Long[1];
		ids[0] = courseModuleId;
		data.put("ids", ids);
		HttpResponse httpResponse = super.api("EM00543", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
	}
}
