package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

public class TestEM00453 extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("id", 1L);
		data.put("teacherId", 1L);
		data.put("educationType", 2);
		data.put("startTime", "2017-07-19 21:29:42");
		data.put("endTime", "2017-07-12 21:29:45");
		data.put("content", "contentEdit");
		data.put("site", "siteEdit");
		data.put("remark", "remarkEdit");
		HttpResponse httpResponse = super.api("EM00453", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		ResponseHeader header = response.getHeader();
		if(object != null) {
			Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
			System.out.println(object);
		} else {
			System.out.println("返回错误码：" + header.getErrorcode());
		}
	}
	
}
