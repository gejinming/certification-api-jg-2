package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00363 extends CertificationAddBeforeJUnit {

	/**
	 * 检查数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Long evaluteId = ConfigUtils.getLong("junitid", "evalute.id");
		Map<String, Object> data = new HashMap<>();
		data.put("id", evaluteId);
		data.put("indicationId", 2);
		data.put("teacherCourseId", 2);
		data.put("content", "content");
		data.put("weight", 0.2);
		data.put("remark", "remark");
		HttpResponse httpResponse = super.api("EM00363", token, data).send();
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
