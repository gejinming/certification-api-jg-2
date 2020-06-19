package com.gnet.certification;


import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00331 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		
		Long planId = ConfigUtils.getLong("junitid", "plan.id");
		// 1. 新增测试
		Map<String, Object> data = new HashMap<>();
		data.put("moduleName", "专业认证5");
		data.put("remark", "good");
		data.put("planId", planId);
		HttpResponse httpResponse = super.api("EM00331", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
		
	}
	
}
