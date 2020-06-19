package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM00010 extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("name", "1");
		data.put("id", 216008);
		
		JSONArray permissionIds = new JSONArray();
		permissionIds.add("admin:role:view");
		data.put("permissionIds", permissionIds);
		
		JSONArray authorizationScope = new JSONArray();
		authorizationScope.add("213");
		authorizationScope.add("21");
		data.put("authorizationScope", authorizationScope);
		HttpResponse httpResponse = super.api("EM00010",token, data).send();
		
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		System.out.println(object);
	}
	
}
