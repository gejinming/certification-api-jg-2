package com.gnet.certification;

import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.google.common.collect.Maps;

import jodd.http.HttpResponse;

/**
 * 查看学生列表测试用例
 * 
 * @author wct
 * @Date 2016年6月29日
 */
public class TestEM00200 extends CertificationAddBeforeJUnit {
	
	/**
	 * 分页
	 */
	@Test
	public void testPageExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		HttpResponse httpResponse = super.api("EM00200", token, data).send();
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
		HttpResponse httpResponse = super.api("EM00200", token, data).send();
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
		HttpResponse httpResponse = super.api("EM00200", token, data).send();
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
		HttpResponse httpResponse = super.api("EM00200", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	/**
	 * 分页排序参数(创建日期)
	 */
	@Test
	public void testPageOrderCDExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("orderProperty", "createDate");
		data.put("orderDirection", "asc");
		HttpResponse httpResponse = super.api("EM00200", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	/**
	 * 分页排序参数(毕业日期)
	 */
	@Test
	public void testPageOrderGDExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("orderProperty", "graduateDate");
		data.put("orderDirection", "asc");
		HttpResponse httpResponse = super.api("EM00200", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	/**
	 * 分页排序参数(班级)
	 */
	@Test
	public void testPageOrderCIExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("orderProperty", "classId");
		data.put("orderDirection", "asc");
		HttpResponse httpResponse = super.api("EM00200", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	/**
	 * 分页排序参数(入学日期)
	 */
	@Test
	public void testPageOrderMDExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("orderProperty", "matriculateDate");
		data.put("orderDirection", "asc");
		HttpResponse httpResponse = super.api("EM00200", token, data).send();
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
		HttpResponse httpResponse = super.api("EM00200", token, data).send();
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
		data.put("orderProperty", "createDate");
		HttpResponse httpResponse = super.api("EM00200", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	/**
	 * 不分页（查询学生姓名）
	 */
	@Test
	public void testListSearchNameExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("name", "xxx");
		HttpResponse httpResponse = super.api("EM00200", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	/**
	 * 不分页（查询学生学号）
	 */
	@Test
	public void testListSearchStudentNoExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("studentNo", 10001L);
		HttpResponse httpResponse = super.api("EM00200", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	/**
	 * 不分页（查询身份证号）
	 */
	@Test
	public void testListSearchIdCardExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("idCard", 10001);
		HttpResponse httpResponse = super.api("EM00200", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	/**
	 * 不分页（查询班级名称）
	 */
	@Test
	public void testListSearchClassNameExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("className", "计算机科学与技术131");
		HttpResponse httpResponse = super.api("EM00200", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	/**
	 * 不分页（查询学籍状态）
	 */
	@Test
	public void testListSearchStatueExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("className", 1);
		HttpResponse httpResponse = super.api("EM00200", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	/**
	 * 分页（查询学生姓名）
	 */
	@Test
	public void testPageSearchNameExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("name", "xxx");
		HttpResponse httpResponse = super.api("EM00200", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	/**
	 * 分页（查询学生学号）
	 */
	@Test
	public void testPageSearchStudentNoExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("studentNo", 10001L);
		HttpResponse httpResponse = super.api("EM00200", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	/**
	 * 分页（查询身份证号）
	 */
	@Test
	public void testPageSearchIdCardExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("idCard", 10001);
		HttpResponse httpResponse = super.api("EM00200", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	/**
	 * 分页（查询班级名称）
	 */
	@Test
	public void testPageSearchClassNameExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("className", "计算机科学与技术131");
		HttpResponse httpResponse = super.api("EM00200", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	/**
	 * 分页（查询学籍状态）
	 */
	@Test
	public void testPageSearchStatueExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("className", 1);
		HttpResponse httpResponse = super.api("EM00200", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}

}
