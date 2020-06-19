package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

/**
 * 生成课程下的所有教学班的报表
 * 
 * @author wct
 * @date 2016年7月11日
 */
public class TestEM00551 extends CertificationAddBeforeJUnit {
	
	@Test
	public void execute() {
		Map<String, Object> data = new HashMap<>();
		data.put("teacherCourseId", 10001L);
		
		HttpResponse httpResponse = super.api("EM00551", token, data).send();
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
