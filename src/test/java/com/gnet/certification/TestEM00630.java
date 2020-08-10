package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM00630 extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("grade", 2016);
		data.put("year", 2017);
		data.put("majorId", 111);
		data.put("studentNo", 888);
		data.put("type", 1);
		data.put("studentName", "xzl");
		data.put("studentSex", 0);
		data.put("transferInMajorName", "软工");
		data.put("transferOutMajorName", "计算机");
		
		HttpResponse httpResponse = super.api("EM00630", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
	}
	
}
