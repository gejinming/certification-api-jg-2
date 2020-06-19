package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM00055 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("planId", 100L);
		//验证名称唯一性
        data.put("name", "专业认证2");
        HttpResponse httpResponse = super.api("EM00055", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.FALSE, object.getBooleanValue("isExisted"));
		System.out.println(object);
		
	}
	
}
