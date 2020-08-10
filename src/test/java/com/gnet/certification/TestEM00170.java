package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM00170 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Long planId = 222L;

		Map<String, Object> data = new HashMap<>();
		data = new HashMap<>();
		data.put("planId", planId);
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("orderProperty", "propertyName");
		HttpResponse httpResponse = super.api("EM00170", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);

	}
	
//	/**
//	 * 输出所有状态的数据，包括已经废弃的
//	 */
//	@Test
//	public void testExecuteStatueAll() {
//		Long majorId = ConfigUtils.getLong("junitid", "major.id");
//		Boolean couldStatueClose = Boolean.TRUE;
//
//		Map<String, Object> data = new HashMap<>();
//		data = new HashMap<>();
//		data.put("couldStatueClose", couldStatueClose);
//		data.put("majorId", majorId);
//		data.put("pageNumber", 1);
//		data.put("pageSize", 20);
//		data.put("orderProperty", "name");
//		data.put("orderDirection", "asc");
//		HttpResponse httpResponse = super.api("EM00100", token, data).send();
//		this.isOk(httpResponse);
//		Response response = this.parse(httpResponse);
//		JSONObject object = (JSONObject) response.getData();
//		Assert.assertNotNull(object);
//		System.out.println(object);
//
//	}
	
}
