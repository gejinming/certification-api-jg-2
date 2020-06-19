package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import jodd.http.HttpResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestEM00708 extends CertificationAddBeforeJUnit {
	
	/**
	 * 删除大纲类型
	 */
	@Test
	public void testExecute() {

		Map<String, Object> data = new HashMap<>();
		data = new HashMap<>();
		data.put("ids", new Long[]{282572L});
		HttpResponse httpResponse = super.api("EM00708", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);

	}
}
