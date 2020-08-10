package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM00366 extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		
		Map<String, Object> data = new HashMap<>();
		JSONArray indexNumArray = new JSONArray();
		Map<String, Object> map1 = new HashMap<>();
		map1.put("id", 1L);
		map1.put("indexNum", "6");
		indexNumArray.add(map1);
		Map<String, Object> map2 = new HashMap<>();
		map2.put("id", 2L);
		map2.put("indexNum", "4");
		indexNumArray.add(map2);
		data.put("indexNumArray", indexNumArray.toString());
		HttpResponse httpResponse = super.api("EM00366", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
		
	}
	
}
