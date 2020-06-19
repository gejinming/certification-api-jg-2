package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import jodd.http.HttpResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestEM00712 extends CertificationAddBeforeJUnit {
	
	/**
	 * 教师需要编写大纲的课程列表
	 */
	@Test
	public void testExecute() {

		Map<String, Object> data = new HashMap<>();
		data = new HashMap<>();
		HttpResponse httpResponse = super.api("EM00712", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);

	}
}
