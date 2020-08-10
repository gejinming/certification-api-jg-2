package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00099 extends CertificationAddBeforeJUnit {

	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Long majorId = ConfigUtils.getLong("junitid", "major.id");
		String name = "name";
		String originValue = null;
		
		Map<String, Object> data = new HashMap<>();
		data.put("majorId", majorId);
		data.put("name", name);
		data.put("originValue", originValue);
		HttpResponse httpResponse = super.api("EM00099", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
		
	}
	
}
