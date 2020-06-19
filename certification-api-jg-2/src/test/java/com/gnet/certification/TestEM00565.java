package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM00565 extends CertificationAddBeforeJUnit{
	
	@Test
	public void testExecute(){
		
		Map<String, Object> data = new HashMap<>();

		JSONArray dataArray = new JSONArray();
		Map<String, Object> map3 = new HashMap<>();
		map3.put("scoreStuIndigradeId", 162407);
		map3.put("studentId", 1);
		map3.put("gradecomposeIndicationId", 162393);
		map3.put("grade", 20);
		dataArray.add(map3);
		
		Map<String, Object> map4 = new HashMap<>();
		map4.put("studentId", 765);
		map4.put("gradecomposeIndicationId", 162393);
		map4.put("grade", 40);
		dataArray.add(map4);
		
		data.put("scoreStuIndigradeArray", dataArray);
		
		HttpResponse httpResponse = super.api("EM00565", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
		
	}

}
