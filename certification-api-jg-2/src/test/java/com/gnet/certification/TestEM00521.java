package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM00521 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查开课课程成绩组成元素是否删除成功
	 */
	@Test
	public void testExecute() {
		Long courseModuleId = 162374L;
		Map<String, Object> data = new HashMap<>();
		Long[] ids = new Long[1];
		ids[0] = courseModuleId;
		data.put("ids", ids);
		HttpResponse httpResponse = super.api("EM00521", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
	}
}
