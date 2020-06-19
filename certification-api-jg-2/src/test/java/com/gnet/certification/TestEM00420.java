package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

public class TestEM00420 extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		
		Map<String, Object> data = new HashMap<>();
		data = new HashMap<>();
//		data.put("pageNumber", 1);
//		data.put("pageSize", 20);
		data.put("teacherId", 250L);
//		data.put("name", "name");
		HttpResponse httpResponse = super.api("EM00420", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		ResponseHeader header = response.getHeader();
		if(object != null) {
			System.out.println(object);
		} else {
			System.out.println("返回错误码：" + header.getErrorcode());
		}

	}
	
}
