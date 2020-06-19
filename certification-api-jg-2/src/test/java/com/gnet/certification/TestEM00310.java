package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

/**
 * 检查教学班列表的是否正确输出
 * 
 * @author SY
 *
 */
public class TestEM00310 extends CertificationAddBeforeJUnit{
	
	@Test
	public void testExecute() {
		
		String courseName = ConfigUtils.getStr("junitid", "course.name");

		Map<String, Object> data = new HashMap<>();
		data = new HashMap<>();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
//		data.put("courseName", courseName);
		HttpResponse httpResponse = super.api("EM00310", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);

	}

}
