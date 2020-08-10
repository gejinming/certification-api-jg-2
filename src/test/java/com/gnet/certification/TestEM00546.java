package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;


import jodd.http.HttpResponse;

public class TestEM00546 extends CertificationAddBeforeJUnit {

	
	@Test
	public void testExecute() {
		
		Map<String, Object> data = new HashMap<>();
		data.put("teacherCourseId", 162341);
		data.put("indicationId", 162364);
		data.put("weight", 0.8);
		
		HttpResponse httpResponse = super.api("EM00546", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.FALSE, object.getBooleanValue("canSave"));
		System.out.println(object);
	}
	

}
