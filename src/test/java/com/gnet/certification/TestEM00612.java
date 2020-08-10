package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00612 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查能够正常录入数据
	 */
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		
		Long courseGradecomposeDetailId = ConfigUtils.getLong("junitid", "courseGradecomposeDetail.id");
		Long stdudentId = ConfigUtils.getLong("junitid", "student.id");
		
		data.put("detailId", courseGradecomposeDetailId);
		data.put("studentId", stdudentId);
		data.put("score", 100);
		data.put("remark", "remark");
		
		HttpResponse httpResponse = super.api("EM00612", token, data).send();
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
