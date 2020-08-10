package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00070_74 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Long gradecomposeId = ConfigUtils.getLong("junitid", "gradecompose.id");
		String nameName = "name";
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
		data.put(nameName, "add");
		data.put("remark", "remarkTime:" + date);
		HttpResponse httpResponse = super.api("EM00072", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
		// 2. 查看one测试
		data = new HashMap<>();
		data.put(idName, gradecomposeId);
		httpResponse = super.api("EM00071", token, data).send();
		
		this.isOk(httpResponse);
		
		response = this.parse(httpResponse);
		object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
		
		// 3. 查看list测试
		data = new HashMap<>();
		data.put("majorId", 162313);
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		httpResponse = super.api("EM00070", token, data).send();
		this.isOk(httpResponse);
		
		response = this.parse(httpResponse);
		object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
		
		// 4. 编辑测试
		data = new HashMap<>();
		data.put(idName, gradecomposeId);
		data.put(nameName, "edit");
		data.put("remark", "remarkTime" + date);
		httpResponse = super.api("EM00073", token, data).send();
		this.isOk(httpResponse);
		
		response = this.parse(httpResponse);
		object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
		// 5. 删除测试
		data = new HashMap<>();
		Long[] ids = new Long[1];
		ids[0] = gradecomposeId;
		data.put("ids", ids);
		httpResponse = super.api("EM00074", token, data).send();
		this.isOk(httpResponse);
		
		response = this.parse(httpResponse);
		object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
	}
	
}
