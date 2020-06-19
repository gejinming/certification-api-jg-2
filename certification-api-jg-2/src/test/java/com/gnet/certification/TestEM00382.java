package com.gnet.certification;

import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.google.common.collect.Maps;

import jodd.http.HttpResponse;

/**
 * 查看教学班学生列表测试用例
 * 
 * @author xzl
 * @Date 2016年7月5日
 */
public class TestEM00382 extends CertificationAddBeforeJUnit {
	
	/**
	 * 分页
	 */
	@Test
	public void testPageExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("classId", 162302L);
		HttpResponse httpResponse = super.api("EM00382", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	/**
	 * 不分页
	 *//*
	@Test
	public void testListExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("classId", 162302L);
		HttpResponse httpResponse = super.api("EM00382", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	*//**
	 * 分页漏传pageSize参数
	 *//*
	@Test
	public void testOnlyPageNumberExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("pageNumber", 1);
		data.put("classId", 162302L);
		HttpResponse httpResponse = super.api("EM00382", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	*//**
	 * 分页漏传pageNumber参数
	 *//*
	@Test
	public void testOnlyPageSizeExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("pageSize", 10);
		data.put("classId", 162302L);
		HttpResponse httpResponse = super.api("EM00382", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	*//**
	 * 分页排序参数(创建日期)
	 *//*
	@Test
	public void testPageOrderCDExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("classId", 162302L);
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("orderProperty", "createDate");
		data.put("orderDirection", "asc");
		HttpResponse httpResponse = super.api("EM00382", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	*//**
	 * 分页排序参数(毕业日期)
	 *//*
	@Test
	public void testPageOrderGDExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("classId", 162302L);
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("orderProperty", "graduateDate");
		data.put("orderDirection", "asc");
		HttpResponse httpResponse = super.api("EM00382", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	*//**
	 * 分页排序参数(入学日期)
	 *//*
	@Test
	public void testPageOrderMDExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("classId", 162302L);
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("orderProperty", "matriculateDate");
		data.put("orderDirection", "asc");
		HttpResponse httpResponse = super.api("EM00382", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	*//**
	 * 分页排序只有排序方向
	 *//*
	@Test
	public void testOnlyOrderDirectionExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("classId", 162302L);
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("orderDirection", "asc");
		HttpResponse httpResponse = super.api("EM00382", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	*//**
	 * 分页排序只有排序参数
	 *//*
	@Test
	public void testOnlyOrderPropertyExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("classId", 162302L);
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("orderProperty", "createDate");
		HttpResponse httpResponse = super.api("EM00382", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	*//**
	 * 不分页（查询学生姓名）
	 *//*
	@Test
	public void testListSearchNameExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("classId", 162302L);
		data.put("name", "xxx");
		HttpResponse httpResponse = super.api("EM00382", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	*//**
	 * 不分页（查询学生学号）
	 *//*
	@Test
	public void testListSearchStudentNoExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("classId", 162302L);
		data.put("studentNo", 10001L);
		HttpResponse httpResponse = super.api("EM00382", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
		
	*//**
	 * 分页（查询学生姓名）
	 *//*
	@Test
	public void testPageSearchNameExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("classId", 162302L);
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("name", "lll");
		HttpResponse httpResponse = super.api("EM00382", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	*//**
	 * 分页（查询学生学号）
	 *//*
	@Test
	public void testPageSearchStudentNoExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("classId", 162302L);
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("studentNo", 100003L);
		HttpResponse httpResponse = super.api("EM00382", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	*/
}
