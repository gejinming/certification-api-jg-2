package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM00523 extends CertificationAddBeforeJUnit {

	/**
	 * 开课课程成绩组成元素重新排序
	 */
	@Test
	public void testExecute() {	
		
		JSONArray sortArray = new JSONArray();
		Map<String, Object> map1 = new HashMap<>();
		map1.put("id", 162358);
		map1.put("sort", 1);
		sortArray.add(map1);
		Map<String, Object> map2 = new HashMap<>();
		map2.put("id", 162370);
		map2.put("sort", 2);
		sortArray.add(map2);
		
		Map<String, Object> data = new HashMap<>();
		data.put("courseGradecomposeSortArray", sortArray.toString());
		HttpResponse httpResponse = super.api("EM00523", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
	}
	

}
