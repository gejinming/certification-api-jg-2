package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import jodd.http.HttpResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestEM00711 extends CertificationAddBeforeJUnit {
	
	/**
	 * 指定课程大纲的执笔人和审核人
	 */
	@Test
	public void testExecute() {

		Map<String, Object> data = new HashMap<>();
		data = new HashMap<>();
		data.put("courseOutlineIds", new Long[]{282637L});
		data.put("authorId", 166751);
		data.put("auditorId", 166752);
		HttpResponse httpResponse = super.api("EM00711", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);

	}
}
