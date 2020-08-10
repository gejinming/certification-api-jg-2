package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00354 extends CertificationAddBeforeJUnit {

	/**
	 * 检查数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Long termId = ConfigUtils.getLong("junitid", "term.id");
		Map<String, Object> data = new HashMap<>();
		Long[] ids = new Long[1];
		ids[0] = termId;
		data.put("ids", ids);
		HttpResponse httpResponse = super.api("EM00354", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
	}
	
}
