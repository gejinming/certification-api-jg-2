package com.gnet.certification;


import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM00570 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
	
		Map<String, Object> data = new HashMap<>();

		data.put("pageNumber", 1);
		data.put("pageSize", 20);
//		data.put("planId", 162408);
//		data.put("status", 2);
/*		data.put("majorId", 165877);
		data.put("grade", 2013);*/
        data.put("planId", 777);
		
		HttpResponse httpResponse = super.api("EM00570", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
		
		
	}
	
}
