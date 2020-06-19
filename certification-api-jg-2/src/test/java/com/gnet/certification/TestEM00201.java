package com.gnet.certification;

import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.google.common.collect.Maps;

import jodd.http.HttpResponse;

/**
 * 查看学生详情测试用例
 * 
 * @author wct
 * @Date 2016年6月29日
 */
public class TestEM00201 extends CertificationAddBeforeJUnit {
	
	/**
	 * 正确的id
	 */
	@Test
	public void testRightIdExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("id", 10001);
		HttpResponse httpResponse = super.api("EM00201", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
	/**
	 * 错误的id
	 */
	@Test
	public void testErrorIdExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("id", 10002);
		HttpResponse httpResponse = super.api("EM00201", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNull(object);
		System.out.println("错误的id ok");
	}
	
	/**
	 * 不填id
	 */
	@Test
	public void testNoIdExecute() {
		Map<String, Object> data = Maps.newHashMap();
		HttpResponse httpResponse = super.api("EM00201", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNull(object);
		System.out.println("不填id ok");
	}
}
