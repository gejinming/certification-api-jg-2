package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM00076 extends CertificationAddBeforeJUnit {

	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		//验证题号唯一性
        data.put("name", "aaa");
        data.put("courseGradecomposeId", 666);
        HttpResponse httpResponse = super.api("EM00076", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.FALSE, object.getBooleanValue("isExisted"));
		System.out.println(object);
	}
	
}
