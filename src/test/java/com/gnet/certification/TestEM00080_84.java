package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00080_84 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Long majorId = ConfigUtils.getLong("junitid", "major.id");
		Long officerId = 1L;
		
		// 查询，学院id 
		Long instituteId = ConfigUtils.getLong("junitid", "indication.id");
		// 专业名字
		String majorName = "name";
		// 专业编码
		String code = "code2312";
		
		Integer educationLength = 2;
		Integer specializedFields = 2;
		Integer awardDegree = 2;
		Integer educationLevel = 2;
		String idName = "majorId";
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
		data.put("educationLength", educationLength);
		data.put("majorId", majorId);
		data.put("officerId", officerId);
		data.put("code", code);
		data.put("majorName", majorName);
		data.put("instituteId", instituteId);
		data.put("specializedFields", specializedFields);
		data.put("awardDegree", awardDegree);
		data.put("educationLevel", educationLevel);
		data.put("description", "description:" + date);
		HttpResponse httpResponse = super.api("EM00082", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
		// 2. 查看one测试
		data = new HashMap<>();
		data.put(idName, majorId);
		httpResponse = super.api("EM00081", token, data).send();
		
		this.isOk(httpResponse);
		
		response = this.parse(httpResponse);
		object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
		
		// 3. 查看list测试
		data = new HashMap<>();
		data.put("instituteName", "xzl");
		data.put("instituteId", instituteId);
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
	    httpResponse = super.api("EM00080", token, data).send();
		this.isOk(httpResponse);
		response = this.parse(httpResponse);
		object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
		
		// 4. 编辑测试
		data = new HashMap<>();
		data.put(idName, majorId);
		data.put("educationLength", educationLength);
		data.put("officerId", officerId);
		data.put("specializedFields", specializedFields);
		data.put("awardDegree", awardDegree);
		data.put("majorName", majorName);
		data.put("code", code);
		data.put("educationLevel", educationLevel);
		data.put("description", "description:" + date);
		httpResponse = super.api("EM00083", token, data).send();
		this.isOk(httpResponse);
		
		response = this.parse(httpResponse);
		object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
		// 5. 删除测试
		data = new HashMap<>();
		Long[] ids = new Long[1];
		ids[0] = majorId;
		data.put("ids", ids);
		httpResponse = super.api("EM00084", token, data).send();
		this.isOk(httpResponse);
		
		response = this.parse(httpResponse);
		object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
	}
	
}
