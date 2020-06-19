package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00274 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查课程开课Del正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Long indicationCourseId = ConfigUtils.getLong("junitid", "indication.course.id");
		Map<String, Object> data = new HashMap<>();
		Long[] ids = new Long[1];
		ids[0] = indicationCourseId;
		data.put("ids", ids);
		HttpResponse httpResponse = super.api("EM00274", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
	}
}
