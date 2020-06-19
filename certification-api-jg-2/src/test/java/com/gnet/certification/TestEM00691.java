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
public class TestEM00691 extends CertificationAddBeforeJUnit{
	
	@Test
	public void testExecute() {
		
		Map<String, Object> data = new HashMap<>();
		data = new HashMap<>();
        data.put("majorId", 165877L);
		
		HttpResponse httpResponse = super.api("EM00691", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);

	}

}
