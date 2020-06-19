package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import jodd.http.HttpResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回大纲详情
 * 
 * @author xzl
 *
 */
public class TestEM00703 extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		
		Map<String, Object> data = new HashMap<>();
		data = new HashMap<>();
		data.put("courseOutlineId", 282624);
		HttpResponse httpResponse = super.api("EM00703", token, data).send();
		this.isOk(httpResponse);

		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		ResponseHeader header = response.getHeader();
		Assert.assertNotNull(String.format("返回错误码：%s",header.getErrorcode()) ,object);
		Assert.assertNotNull(String.format("返回错误信息：%s", header.getErrormessage()) ,object);
		System.out.println(object);
	}
	
}
