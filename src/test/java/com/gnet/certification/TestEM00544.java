package com.gnet.certification;


import java.util.Map;

import org.junit.Test;


import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import com.google.common.collect.Maps;

import jodd.http.HttpResponse;

public class TestEM00544 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		
		Map<String, Object> data = Maps.newHashMap();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
//		data.put("teacherCourseId", 162341);
		data.put("courseGradecomposeId", 303244);
//		{"data":{"courseGradecomposeId":"303244"} 无
//		{"courseGradecomposeId":"302558"} 有
		HttpResponse httpResponse = super.api("EM00544", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
		}
		
	}
	
