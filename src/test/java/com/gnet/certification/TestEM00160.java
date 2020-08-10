package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00160 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Long indicationId1 = ConfigUtils.getLong("junitid", "indication.id.1");
		Long indicationId2 = ConfigUtils.getLong("junitid", "indication.id.2");
		Long indicationId3 = ConfigUtils.getLong("junitid", "indication.id.3");
		JSONArray returnArray = new JSONArray();
		Map<String, Object> map1 = new HashMap<>();
		map1.put("id", indicationId1);
		map1.put("indexNum", "2");
		returnArray.add(map1);
		Map<String, Object> map2 = new HashMap<>();
		map2.put("id", indicationId2);
		map2.put("indexNum", "3");
		returnArray.add(map2);
		Map<String, Object> map3 = new HashMap<>();
		map3.put("id", indicationId3);
		map3.put("indexNum", "1");
		returnArray.add(map3);
		
		Map<String, Object> data = new HashMap<>();
		data.put("sortArray", returnArray.toString());
		HttpResponse httpResponse = super.api("EM00160", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
	}
	
}
