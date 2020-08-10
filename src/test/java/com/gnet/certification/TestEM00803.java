package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import jodd.http.HttpResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 *  教学班课程目标达成度报表
 * 
 * @author xzl
 * @date 2017年11月28日15:17:40
 */
public class TestEM00803 extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("eduClassId", 471791);

		HttpResponse httpResponse = super.api("EM00803", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		ResponseHeader header = response.getHeader();
		JSONObject object = (JSONObject) response.getData();
		System.out.println(object);
        System.out.println(header.getErrorcode());
		System.out.println(header.getErrormessage());
	}
}
