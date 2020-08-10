package com.gnet.certification;

import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.google.common.collect.Maps;

import jodd.http.HttpResponse;

/**
 * 删除教学班学生
 * 
 * @author xzl
 * @Date 2016-7-4 
 */
public class TestEM00381 extends CertificationAddBeforeJUnit {
	
	/**
	 * 教学班删除一个学生
	 */
	@Test
	public void testPageExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("ids", new Long[]{162306L});
		HttpResponse httpResponse = super.api("EM00381", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
}
