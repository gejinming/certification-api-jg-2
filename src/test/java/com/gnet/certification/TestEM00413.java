package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

public class TestEM00413  extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		Long[] indicationIdArray = new Long[2];
		indicationIdArray[0] = 162628L;
		indicationIdArray[1] = 162630L;
		data.put("indicationIdArray", indicationIdArray);
		data.put("id", 162693L);
		data.put("name", "name");
		data.put("score", 20);
		data.put("detail", "detail");
		data.put("remark", "remark");
		data.put("courseGradecomposeId", 1L);
		HttpResponse httpResponse = super.api("EM00413", token, data).send();
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
