package com.gnet.certification;


import java.util.Map;

import org.junit.Test;


import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import com.google.common.collect.Maps;

import jodd.http.HttpResponse;

public class TestEM00541 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		
		Map<String, Object> data = Maps.newHashMap();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("teacherCourseId", 162341);
		data.put("indicationId", 162364);
		HttpResponse httpResponse = super.api("EM00541", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
		}
		
	}
	
