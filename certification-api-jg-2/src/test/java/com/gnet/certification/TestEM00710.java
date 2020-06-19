package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import jodd.http.HttpResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestEM00710 extends CertificationAddBeforeJUnit {
	
	/**
	 * 可以指定执笔人和审核人的大纲列表
	 */
	@Test
	public void testExecute() {

		Map<String, Object> data = new HashMap<>();
		data = new HashMap<>();
		data.put("courseId", 273558);
		HttpResponse httpResponse = super.api("EM00710", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);

	}
}
