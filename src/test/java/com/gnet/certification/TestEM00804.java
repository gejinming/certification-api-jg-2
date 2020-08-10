package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import jodd.http.HttpResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 *  课程目标期望值修改
 * 
 * @author xzl
 * @date 2017年11月28日15:17:40
 */
public class TestEM00804 extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("id", 471439);
		data.put("expectedValue", 1);

		HttpResponse httpResponse = super.api("EM00804", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		ResponseHeader header = response.getHeader();
		JSONObject object = (JSONObject) response.getData();
		System.out.println(object);
        System.out.println(header.getErrorcode());
		System.out.println(header.getErrormessage());
	}
}
