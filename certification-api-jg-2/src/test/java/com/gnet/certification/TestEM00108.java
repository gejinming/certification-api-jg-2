package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM00108 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Long majorId = 1L;
		Long parentId = 100L;
		Integer type = 2;
		Integer enableGrade = 2014;
		Map<String, Object> data = new HashMap<>();
		data.put("majorId", majorId);
		data.put("parentId", parentId);
		data.put("type", type);
		data.put("enableGrade", enableGrade);
		HttpResponse httpResponse = super.api("EM00108", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
		
	}
	
}
