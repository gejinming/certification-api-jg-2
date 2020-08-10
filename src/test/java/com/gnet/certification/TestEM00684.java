package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

/**
 * 教师我的课程列表
 * 
 * @author 
 *
 */
public class TestEM00684 extends CertificationAddBeforeJUnit{
	
	@Test
	public void testExecute() {
		

		Map<String, Object> data = new HashMap<>();
		data = new HashMap<>();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("orderProperty", "courseName");
		data.put("orderDirection", "asc");
		HttpResponse httpResponse = super.api("EM00684", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		System.out.println(object);

	}

}
