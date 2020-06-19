package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

public class TestEM00345 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查培养计划学年学期的唯一性
	 */
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("year", 1L);
		data.put("term", 2L);
		data.put("yearName", "year");
		data.put("termName", "term");
		data.put("yearOriginValue", 1L);
		data.put("termOriginValue", 2L);
		data.put("yearNameOriginValue", "year");
		data.put("termNameOriginValue", "term");
		
		HttpResponse httpResponse = super.api("EM00345", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		ResponseHeader header = response.getHeader();
		if(object != null) {
			Assert.assertEquals(Boolean.FALSE, object.getBooleanValue("isExists"));
			System.out.println(object);
		} else {
			System.out.println("返回错误码：" + header.getErrorcode());
		}
	}

}
