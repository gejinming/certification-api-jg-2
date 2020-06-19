package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00273 extends CertificationAddBeforeJUnit {

	/**
	 * 检查课程开课Edit正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Long indicationCourseId = ConfigUtils.getLong("junitid", "indication.course.id");
		Map<String, Object> data = new HashMap<>();
		data.put("id", indicationCourseId);
		data.put("courseId", 100);
		data.put("indicationId", 100);
		data.put("weight", 0.273);
		data.put("eduAim", "eduAimEdit");
		data.put("means", "means");
		data.put("source", "source");
		data.put("way", "way");
		HttpResponse httpResponse = super.api("EM00273", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		ResponseHeader header = response.getHeader();
		if(object != null) {
			Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
			System.out.println(object);
		} else {
			System.out.println("返回错误码：" + header.getErrorcode());
		}
	}
	
}
