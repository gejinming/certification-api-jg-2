package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

/**
 * 检查专业方向列表的是否正确输出
 * 
 * @author sll
 *
 */
public class TestEM00190 extends CertificationAddBeforeJUnit{
	
	@Test
	public void testExecute() {
		

		Map<String, Object> data = new HashMap<>();
		data = new HashMap<>();
//		data.put("pageNumber", 1);
//		data.put("pageSize", 20);
//		data.put("pageNumber", 1);
//		data.put("pageSize", 20);
//		data.put("orderProperty", "name");
//		data.put("orderDirection", "asc");
//		data.put("planId", 100L);
		data.put("planId", 300297);
		HttpResponse httpResponse = super.api("EM00190", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);

	}

}
