package com.gnet.certification;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

public class TestEM00623  extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		Long courseGroupId = 171750L;
		Map<String, Object> data = new HashMap<>();
		Long[] courseIds = new Long[2];
		courseIds[0] = 165958L;
		courseIds[1] = 165973L;
		data.put("courseIds", courseIds);
		data.put("id", courseGroupId);
		data.put("planId", 165889L);
		data.put("allHours", new BigDecimal(0));
		data.put("remark", "22");
		HttpResponse httpResponse = super.api("EM00623", token, data).send();
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
