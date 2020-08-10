package com.gnet.certification;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import jodd.http.HttpResponse;

public class TestEM00694 extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		Map<String, Object> data = Maps.newHashMap();
		List<Map<String, Object>> list = Lists.newArrayList();
		Map<String, Object> teacher = Maps.newHashMap();
		teacher.put("code", "196006-4");
		teacher.put("name", "杨春亭4");
		teacher.put("sex", "0");
		teacher.put("sexName", "男");
		teacher.put("isEnabled", "1");
		teacher.put("isLeave", "0");
		teacher.put("majorId", "265125");
		teacher.put("instituteId", null);
		teacher.put("departmentName", "计算机专业");
		list.add(teacher);
		data.put("teachers", list);
		HttpResponse httpResponse = super.api("EM00694", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
}
