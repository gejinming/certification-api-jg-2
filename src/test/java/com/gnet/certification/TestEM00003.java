package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM00003 extends CertificationAddBeforeJUnit {
	
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("type", 1);
		data.put("schoolId", 165517L);
		data.put("loginName", "33333");
		data.put("password", "1234");
		data.put("rePassword", "1234");
		HttpResponse httpResponse = super.api("EM00003",token, data).send();
		
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		if (object != null) {
			System.out.println(object);
		} else {
			System.out.println("错误编码:" + response.getHeader().getErrorcode());
		}
		
	}
}
