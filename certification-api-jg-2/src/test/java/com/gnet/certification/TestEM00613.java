package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00613 extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("id", 279041);
		data.put("score", 3);
		data.put("remark", "remark");
		data.put("courseGradecomposeIndicationId", 267681);
		HttpResponse httpResponse = super.api("EM00613", token, data).send();
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
