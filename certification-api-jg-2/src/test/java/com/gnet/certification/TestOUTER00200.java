package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import jodd.http.HttpResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestOUTER00200 extends CertificationOutAddBeforeJUnit {
	
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("version", 300298);
		data.put("code", "3151A016");

		HttpResponse httpResponse = super.api("OUTER00200", token, data).send();
		
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		System.out.println(object);
	}
	
}
