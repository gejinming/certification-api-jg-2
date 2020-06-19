package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

public class TestEM00433 extends CertificationAddBeforeJUnit {
	
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("id", 1);
		data.put("year", 222);
		data.put("enrolNum", 222);
		data.put("provinceDivision", 222);
		data.put("majorDivision", 222);
		data.put("lowestLine", 222);
		data.put("firstVoluntaryEnrollmentRatio", 22.99);
		data.put("majorId", 1L);
		data.put("remark", "remark");
		
		HttpResponse httpResponse = super.api("EM00433", token, data).send();
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
