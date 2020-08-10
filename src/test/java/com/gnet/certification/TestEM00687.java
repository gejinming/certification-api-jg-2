package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

/**
 * 某道题目学生的最大得分
 * 
 * @author xzl
 *
 */
public class TestEM00687 extends CertificationAddBeforeJUnit{
	
	@Test
	public void testExecute() {
		

		Map<String, Object> data = new HashMap<>();
		data = new HashMap<>();
		data.put("id", 215733);
		HttpResponse httpResponse = super.api("EM00687", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		System.out.println(object);

	}

}
