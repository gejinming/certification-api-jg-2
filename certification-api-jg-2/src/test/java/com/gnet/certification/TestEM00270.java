package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

/**
 * 检查指标点课程关系表列表的是否正确输出
 * 
 * @author SY
 *
 */
public class TestEM00270 extends CertificationAddBeforeJUnit{
	
	@Test
	public void testExecute() {
		
		Long courseId = ConfigUtils.getLong("junitid", "course.id");
		Long indicationId = ConfigUtils.getLong("junitid", "indication.id");

		Map<String, Object> data = new HashMap<>();
		data = new HashMap<>();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("orderProperty", "way");
		data.put("courseId", courseId);
		data.put("indicationId", null);
		HttpResponse httpResponse = super.api("EM00270", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);

	}

}
