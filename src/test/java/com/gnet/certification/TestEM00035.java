package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

public class TestEM00035 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
        data.put("code", "admin:user:add");
//       data.put("originValue", "admin:user:add");
        HttpResponse httpResponse = super.api("EM00035", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		ResponseHeader header = response.getHeader();
		if(object != null) {
			Assert.assertEquals(Boolean.FALSE, object.getBooleanValue("isExists"));
			System.out.println(object);
		} else {
			System.out.println("返回错误码：" + header.getErrorcode());
		}

	}
	
}
