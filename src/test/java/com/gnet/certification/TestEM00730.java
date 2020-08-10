package com.gnet.certification;


import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import jodd.http.HttpResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestEM00730 extends CertificationAddBeforeJUnit {

	/**
	 * 负责人强制审核通过的大纲改为待审核大纲
	 */
	@Test
	public void testExecute() {
	
		Map<String, Object> data = new HashMap<>();

		data.put("courseOutlineIds", new Long[]{312312L});
		HttpResponse httpResponse = super.api("EM00730", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
		
	}
	
}
