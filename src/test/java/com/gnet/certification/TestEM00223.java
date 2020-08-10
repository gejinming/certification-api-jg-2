package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00223 extends CertificationAddBeforeJUnit {

	/**
	 * 检查课程开课Edit正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Long teacherCourseId = ConfigUtils.getLong("junitid", "teacher.course.id");
		Map<String, Object> data = new HashMap<>();
		data.put("id", teacherCourseId);
		data.put("courseId", 100);
		data.put("teacherId", 100);
		data.put("termId", 100);
		data.put("resultType", 2);
		data.put("grade", 1);
		HttpResponse httpResponse = super.api("EM00223", token, data).send();
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
