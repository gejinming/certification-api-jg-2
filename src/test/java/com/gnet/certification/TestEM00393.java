package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

/**
 * 检查考评点得分层级编辑能否成功
 * 
 * @author sll
 *
 */
public class TestEM00393 extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		Long evaluteLevelId = ConfigUtils.getLong("junitid", "evaluteLevel.id");
		Map<String, Object> data = new HashMap<>();
		data.put("id", evaluteLevelId);
		data.put("levelName", "levelName");
		data.put("score", 10);
		data.put("requirement", "requirement");
		data.put("teacherCourseId", 1L);
		data.put("indicationId", 1L);
		
		HttpResponse httpResponse = super.api("EM00393", token, data).send();
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
