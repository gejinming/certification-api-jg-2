package com.gnet.certification;

import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.google.common.collect.Maps;

import jodd.http.HttpResponse;

/**
 * 静态常量列表获取测试用例
 * 
 * @author wct
 * @Date 2016年7月4日
 */
public class TestEM00040 extends CertificationAddBeforeJUnit {
	
	/**
	 * 获得全部静态常量
	 */
	@Test
	public void testAllDictsExecute() {
		Map<String, Object> data = Maps.newHashMap();
		HttpResponse httpResponse = super.api("EM00040", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONArray array = (JSONArray) response.getData();
		Assert.assertNotNull(array);
		System.out.println(array);
	}
	
	/**
	 * 获得全部静态常量
	 */
	@Test
	public void testDictByTypeExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("type", "studentStatue");
		HttpResponse httpResponse = super.api("EM00040", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
}
