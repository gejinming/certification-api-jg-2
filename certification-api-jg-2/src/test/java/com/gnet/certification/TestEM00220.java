package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

/**
 * 检查教师课程开课列表的是否正确输出
 * 
 * @author SY
 *
 */
public class TestEM00220 extends CertificationAddBeforeJUnit{
	
	@Test
	public void testExecute() {
		

		Map<String, Object> data = new HashMap<>();
		data = new HashMap<>();
//		data.put("planId", 222);
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("orderProperty", "courseName");
		data.put("orderDirection", "asc");
		data.put("majorId", 165877);
		data.put("grade", 2011);
		HttpResponse httpResponse = super.api("EM00220", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		System.out.println(object);

	}

}
