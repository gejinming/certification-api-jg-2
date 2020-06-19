package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM00004 extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
//		data.put("name", "s");
		HttpResponse httpResponse = super.api("EM00004",token, data).send();
		
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		System.out.println(object);
	}
	
}
