package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

public class TestEM00443 extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("id", 162826);
		data.put("year", 2018);
		data.put("graduateNums", 33);
		data.put("graduateRatio", 33);
		data.put("getDegreeRatio", 33);
		data.put("firsttimeEmployedRatio", 33);
		data.put("masterAndGoabroadRatio", 33);
		data.put("nationAndInstitutionRatio", 33);
		data.put("otherEnterpriseRatio", 33);
		data.put("majorId", 1L);
		data.put("remark", "remark");
		data.put("originValue", null);
		HttpResponse httpResponse = super.api("EM00443", token, data).send();
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
