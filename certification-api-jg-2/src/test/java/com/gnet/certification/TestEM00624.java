package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM00624 extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		Long courseGroupId = 100L;
		Map<String, Object> data = new HashMap<>();
		Long[] ids = new Long[1];
		ids[0] = courseGroupId;
		data.put("ids", ids);
		HttpResponse httpResponse = super.api("EM00624", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
	}
	
}
