package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;


public class TestEM00012 extends CertificationAddBeforeJUnit{
	
	@Test
	public void testExecute() {
		
		Map<String, Object> data = new HashMap<>();
		data.put("isNeedGroup", true);
		HttpResponse httpResponse = super.api("EM00012", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);

	}

}
