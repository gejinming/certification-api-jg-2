package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.plugin.configLoader.ConfigUtils;
import jodd.http.HttpResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestEM00704 extends CertificationAddBeforeJUnit {
	
	/**
	 * 增加课程大纲类型
	 */
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("name", "实验大纲1");
		HttpResponse httpResponse = super.api("EM00704", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
	}
	
}
