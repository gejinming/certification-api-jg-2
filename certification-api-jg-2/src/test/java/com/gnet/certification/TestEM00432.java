package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

public class TestEM00432 extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("year", 2016);
		data.put("score", 10);
		data.put("enrolNum", 200);
		data.put("provinceDivision", 500);
		data.put("majorDivision", 500);
		data.put("lowestLine", 500);
		data.put("firstVoluntaryEnrollmentRatio", 99.99);
		data.put("majorId", 2L);
		data.put("remark", "remark");
		HttpResponse httpResponse = super.api("EM00432", token, data).send();
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
