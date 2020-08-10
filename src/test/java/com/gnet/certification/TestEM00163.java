package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00163 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Integer indexNum = 4;
		String content = "thisIsContent";
		String remark = "thisIsRemark";
		Long graduateId = ConfigUtils.getLong("junitid", "graduate.id");
		
		Map<String, Object> data = new HashMap<>();
		data.put("graduateId", graduateId);
		data.put("indexNum", indexNum);
		data.put("content", content);
		data.put("remark", remark);
		HttpResponse httpResponse = super.api("EM00163", token, data).send();
		System.out.println(httpResponse);
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
	}
	
}
