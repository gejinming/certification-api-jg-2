package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00086 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Long instituteId = ConfigUtils.getLong("junitid", "institute.id");
		String name = "name";
		String originValue = ConfigUtils.getStr("junitid", "EM00086origin.name.value");
		
		Map<String, Object> data = new HashMap<>();
		data.put("instituteId", instituteId);
		data.put("name", name);
		data.put("originValue", originValue);
		HttpResponse httpResponse = super.api("EM00125", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
		
	}
	
}
