package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

/**
 * 检查教学班列表的是否正确输出
 * 
 * @author SY
 *
 */
public class TestEM00673 extends CertificationAddBeforeJUnit{
	
	@Test
	public void testExecute() {
		
		Map<String, Object> data = new HashMap<>();
		data = new HashMap<>();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
        data.put("majorId", 165877);
        data.put("versionId", 211891);
        data.put("courseName", null);
        data.put("grade", 2011);
		
		HttpResponse httpResponse = super.api("EM00673", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);

	}

}
