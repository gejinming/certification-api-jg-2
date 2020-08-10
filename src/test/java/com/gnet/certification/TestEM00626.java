package com.gnet.certification;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import jodd.http.HttpResponse;

public class TestEM00626 extends CertificationAddBeforeJUnit {
	
	@Test
	public void testExecute() {
		Map<String, Object> data = Maps.newHashMap();
		List<Long> courseIds = Lists.newArrayList();
		courseIds.add(165595L);
		Long targetCourseId = 165580L;
		data.put("courseIds", courseIds);
		data.put("targetCourseId", targetCourseId);

		HttpResponse httpResponse = super.api("EM00626", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSame"));
		System.out.println(object);
	}
}
