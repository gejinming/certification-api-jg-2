package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM00050_54 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Long hierarchyId = 162326L;
		String nameName = "name";
		String idName = "id";
		/*
		 * 1. 新增测试
		 * 2. 查看one测试
		 * 3. 查看list测试
		 * 4. 编辑测试
		 * 5. 删除测试
		 */
		// 1. 新增测试
		Map<String, Object> data = new HashMap<>();
		data.put(nameName, "专业认证9");
		data.put("planId", 100L);
		data.put("remark", "good");
		HttpResponse httpResponse = super.api("EM00052", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
		// 2. 查看one测试
		data = new HashMap<>();
		data.put(idName, hierarchyId);
	    httpResponse = super.api("EM00051", token, data).send();
		
		this.isOk(httpResponse);
		
		response = this.parse(httpResponse);
		object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
		
		// 3. 查看list测试
		data = new HashMap<>();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("orderProperty", "name");
		data.put("orderDirection", "asc");
		data.put("planId", 100L);
		httpResponse = super.api("EM00050", token, data).send();
		this.isOk(httpResponse);
		
		response = this.parse(httpResponse);
		object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
		
		// 4. 编辑测试
		data = new HashMap<>();
		data.put(idName, hierarchyId);
		data.put(nameName, "专业认证2");
		data.put("remark", "good2");
		httpResponse = super.api("EM00053", token, data).send();
		this.isOk(httpResponse);
		
		response = this.parse(httpResponse);
		object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
		// 5. 删除测试
		data = new HashMap<>();
		Long[] ids = new Long[1];
		ids[0] = hierarchyId;
		data.put("ids", ids);
		httpResponse = super.api("EM00054", token, data).send();
		this.isOk(httpResponse);
		
		response = this.parse(httpResponse);
		object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
	}
	
}
