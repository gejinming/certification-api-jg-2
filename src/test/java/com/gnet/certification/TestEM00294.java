package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM00294 extends CertificationAddBeforeJUnit  {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		Long[] teacherIds = new Long[3];
		teacherIds[0] = 1L;
		teacherIds[1] = 162434L;
		teacherIds[2] = 162447L;
		data.put("teacherIds", teacherIds);
		data.put("versionId", 1L);
		HttpResponse httpResponse = super.api("EM00294", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
	}

}
