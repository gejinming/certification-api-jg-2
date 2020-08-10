package com.gnet.certification;


import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import jodd.http.HttpResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestEM00763 extends CertificationAddBeforeJUnit {

	/**
	 * 考核分析法课程目标评价接口
	 */
	@Test
	public void testExecute() {
	
		Map<String, Object> data = new HashMap<>();

		data.put("id", 452904);
		HttpResponse httpResponse = super.api("EM00763", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		System.out.println(object);
		
		
	}
	
}
