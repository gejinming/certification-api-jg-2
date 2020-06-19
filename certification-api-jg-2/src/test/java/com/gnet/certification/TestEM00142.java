package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM00142 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		
		Map<String, Object> data = new HashMap<>();
		data.put("code", "code1");
		data.put("name", "name4");
		data.put("schoolId", 162243);
		HttpResponse httpResponse = super.api("EM00142", token, data).send();
		this.isOk(httpResponse);
		System.out.println(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		if(object != null) {
			Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		}
		
		System.out.println(object);
		
	}
	
}
