package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00333 extends CertificationAddBeforeJUnit {

	/**
	 * 检查编辑所属模块是否成功
	 */
	@Test
	public void testExecute() {
		Long cousreModuleId = ConfigUtils.getLong("junitid", "courseModule.id");
				
		Map<String, Object> data = new HashMap<>();
		data.put("id", cousreModuleId);
		data.put("moduleName", "专业认证4");
		
		HttpResponse httpResponse = super.api("EM00333", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
	}
	

}
