package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM00562 extends CertificationAddBeforeJUnit {
	
	
	@Test
	public void testExecute() {
		
		Map<String, Object> data = new HashMap<>();
//		data.put("scoreStuIndigradeId", 471915);
		data.put("studentId", 471408);
		data.put("gradecomposeIndicationId", 471898);
		data.put("educlassId", 471791);
		data.put("grade", 9);
		HttpResponse httpResponse = super.api("EM00562", token, data).send();
//		this.isOk(httpResponse);
//
//		Response response = this.parse(httpResponse);
//		JSONObject object = (JSONObject) response.getData();
//		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
//		System.out.println(object);
		
	}
}
