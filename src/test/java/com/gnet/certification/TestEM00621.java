package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

/**
 * 测试查看课程组表能否正常输出
 * 
 * @author SY
 *
 */
public class TestEM00621 extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		Long courseGroupId = 100L;
		Map<String, Object> data = new HashMap<>();
		data.put("id", courseGroupId);
		HttpResponse httpResponse = super.api("EM00621", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		ResponseHeader header = response.getHeader();
		if(object != null) {
			System.out.println(object);
		} else {
			System.out.println("返回错误码：" + header.getErrorcode());
		}
	}
	
}
