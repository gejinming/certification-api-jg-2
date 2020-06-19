package com.gnet.certification;

import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.google.common.collect.Maps;

import jodd.http.HttpResponse;

/**
 * 培养计划列表测试用例
 * 
 * @author wct
 * @Date 2016年7月5日
 */
public class TestEM00370 extends CertificationAddBeforeJUnit {
	
	/**
	 * 分页
	 */
	@Test
	public void testPageExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("appointMajorId", 162176L);
		HttpResponse httpResponse = super.api("EM00370", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	/**
	 * 不分页
	 */
	@Test
	public void testListExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("appointMajorId", 162176L);
		HttpResponse httpResponse = super.api("EM00370", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	/**
	 * 分页漏传pageSize参数
	 */
	@Test
	public void testOnlyPageNumberExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("pageNumber", 1);
		data.put("appointMajorId", 162176L);
		HttpResponse httpResponse = super.api("EM00370", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	/**
	 * 分页漏传pageNumber参数
	 */
	@Test
	public void testOnlyPageSizeExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("pageSize", 10);
		data.put("appointMajorId", 162176L);
		HttpResponse httpResponse = super.api("EM00370", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	/**
	 * 分页排序参数(培养计划编号)
	 */
	@Test
	public void testPageOrderNOExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("orderProperty", "no");
		data.put("orderDirection", "asc");
		data.put("appointMajorId", 162176L);
		HttpResponse httpResponse = super.api("EM00370", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}

	
	/**
	 * 分页排序只有排序方向
	 */
	@Test
	public void testOnlyOrderDirectionExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("orderDirection", "asc");
		data.put("appointMajorId", 162176L);
		HttpResponse httpResponse = super.api("EM00370", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	/**
	 * 分页排序只有排序参数
	 */
	@Test
	public void testOnlyOrderPropertyExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("orderProperty", "no");
		data.put("appointMajorId", 162176L);
		HttpResponse httpResponse = super.api("EM00370", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	/**
	 * 不分页（查询培养计划名称）
	 */
	@Test
	public void testListSearchNameExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("name", "2015-2016");
		data.put("appointMajorId", 162176L);
		HttpResponse httpResponse = super.api("EM00370", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	/**
	 * 分页（查询培养计划名称）
	 */
	@Test
	public void testPageSearchNameExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("name", "2015-2016");
		data.put("appointMajorId", 162176L);
		HttpResponse httpResponse = super.api("EM00370", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	/**
	 * 没有专业编号
	 */
	@Test
	public void testNoMajorIdNameExecute() {
		Map<String, Object> data = Maps.newHashMap();
		HttpResponse httpResponse = super.api("EM00370", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNull(object);
	}
	
}
