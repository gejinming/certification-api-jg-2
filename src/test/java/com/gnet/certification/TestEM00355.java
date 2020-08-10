package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM00355 extends CertificationAddBeforeJUnit {

	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Long startYear = 2011L;
		Long endYear = 2012L;
		Integer term = 1;
		Integer termType = 1;
		
		Map<String, Object> data = new HashMap<>();
		data.put("startYear", startYear);
		data.put("endYear", endYear);
		data.put("term", term);
		data.put("termType", termType);
		
		HttpResponse httpResponse = super.api("EM00355", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
		
	}
	
}
