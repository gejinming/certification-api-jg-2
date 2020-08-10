package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

/**
 * 检查是否能正常输出 
 * 
 * @author sll
 *
 */
public class TestEM00387 extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		Long[] studentIds = new Long[3];
		studentIds[0] = 1116L;
		studentIds[1] = 162585L;
		studentIds[2] = 163134L;
		data.put("studentIds", studentIds);
		data.put("educlassId", 164962L);
		
		HttpResponse httpResponse = super.api("EM00387", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
	}
	
}
