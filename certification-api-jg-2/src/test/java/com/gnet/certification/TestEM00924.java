package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import jodd.http.HttpResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestEM00924 extends CertificationAddBeforeJUnit  {

	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		Long[] ids = new Long[1];
		ids[0] = 1L;
		data.put("ids", ids);
		HttpResponse httpResponse = super.api("EM00954", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
	}
	
}
