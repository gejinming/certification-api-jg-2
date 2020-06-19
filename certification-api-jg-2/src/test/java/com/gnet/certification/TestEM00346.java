package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

public class TestEM00346 extends CertificationAddBeforeJUnit {
	
	/**
	 * 培养计划学年学期学期类型唯一性验证
	 */
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("year", 1);
		data.put("term", 2);
	    data.put("termType", 3);
		
		HttpResponse httpResponse = super.api("EM00346", token, data).send();
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
