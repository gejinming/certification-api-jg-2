package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM00060_64 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Long propertyId = 162333L;
		Long planId = 100L;
		String nameName = "propertyName";
		String idName = "id";
		Date date = new Date();
		/*
		 * 1. 新增测试
		 * 2. 查看one测试
		 * 3. 查看list测试
		 * 4. 编辑测试
		 * 5. 删除测试
		 */
		// 1. 新增测试
		Map<String, Object> data = new HashMap<>();
		data.put("planId", planId);
		data.put(nameName, "add" + date);
		data.put("remark", "remarkTime:" + date);
		HttpResponse httpResponse = super.api("EM00062", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
		// 2. 查看one测试
		data = new HashMap<>();
		data.put(idName, propertyId);
		httpResponse = super.api("EM00061", token, data).send();
		
		this.isOk(httpResponse);
		
		response = this.parse(httpResponse);
		object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
		
		// 3. 查看list测试
		data = new HashMap<>();
		data.put("planId", planId);
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		httpResponse = super.api("EM00060", token, data).send();
		this.isOk(httpResponse);
		
		response = this.parse(httpResponse);
		object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
		
		// 4. 编辑测试
		data = new HashMap<>();
		data.put(idName, propertyId);
		data.put(nameName, "edit" + date);
		data.put("remark", "remarkTime" + date);
		httpResponse = super.api("EM00063", token, data).send();
		this.isOk(httpResponse);
		
		response = this.parse(httpResponse);
		object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
		// 5. 删除测试
		data = new HashMap<>();
		Long[] ids = new Long[1];
		ids[0] = propertyId;
		data.put("ids", ids);
		httpResponse = super.api("EM00064", token, data).send();
		this.isOk(httpResponse);
		
		response = this.parse(httpResponse);
		object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
	}
	
}
