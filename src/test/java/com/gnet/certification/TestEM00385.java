package com.gnet.certification;

import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.google.common.collect.Maps;

import jodd.http.HttpResponse;

/**
 * 教学班学生加入/取消达成度计算
 * 
 * @author SY
 * 
 * @date 2018年1月29日
 *
 */
public class TestEM00385 extends CertificationAddBeforeJUnit {
	
	/**
	 * 教学班学生加入/取消达成度计算
	 */
	@Test
	public void testPageExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("isCaculate", Boolean.TRUE);
//		data.put("isCaculate", Boolean.FALSE);
		data.put("id", 475489);
		HttpResponse httpResponse = super.api("EM00385", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
}
