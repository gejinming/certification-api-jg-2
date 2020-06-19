package com.gnet.certification;

import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.google.common.collect.Maps;

import jodd.http.HttpResponse;

/**
 * 判断学号唯一性测试用例
 * 
 * @author wct
 * @date 2016年6月30日
 */
public class TestEM00205 extends CertificationAddBeforeJUnit {
	
	/**
	 * 学生学号存在时
	 */
	@Test
	public void testStudentNoExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("studentNo", 100003);
		HttpResponse httpResponse = super.api("EM00205", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	/**
	 * 学生学号不存在时
	 */
	@Test
	public void testNoStudentNoExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("studentNo", 100004);
		HttpResponse httpResponse = super.api("EM00205", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	/**
	 * 学生被删除时
	 */
	@Test
	public void testDelStudentNoExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("studentNo", 100001);
		HttpResponse httpResponse = super.api("EM00205", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	/**
	 * 传空的参数
	 */
	@Test
	public void testNullExecute() {
		Map<String, Object> data = Maps.newHashMap();
		HttpResponse httpResponse = super.api("EM00205", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		ResponseHeader responseHeader = response.getHeader();
		Assert.assertTrue("0332".equals(responseHeader.getErrorcode()));
		System.out.println(responseHeader.getErrorcode());
	}
	
	
}
