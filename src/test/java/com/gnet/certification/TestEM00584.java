package com.gnet.certification;


import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM00584 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
	
		Map<String, Object> data = new HashMap<>();

		data.put("pageNumber", 1);
		data.put("pageSize", 20);
//		data.put("teacherId", 165825);
		data.put("versionId", 196378);
		
		HttpResponse httpResponse = super.api("EM00584", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
		
		
	}
	
}
