package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00364 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Long evaluteId = ConfigUtils.getLong("junitid", "evalute.id");
		Map<String, Object> data = new HashMap<>();
		Long[] ids = new Long[1];
		ids[0] = evaluteId;
		data.put("ids", ids);
		HttpResponse httpResponse = super.api("EM00364", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
	}
	
	/**
	 * 检查数据是否成功输出
	 */
	@Test
	public void testExecuteTowIds() {
		Map<String, Object> data = new HashMap<>();
		Long[] ids = new Long[2];
		ids[0] = 269927L;
		ids[1] = 269928L;
		data.put("ids", ids);
		HttpResponse httpResponse = super.api("EM00364", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
	}

}
