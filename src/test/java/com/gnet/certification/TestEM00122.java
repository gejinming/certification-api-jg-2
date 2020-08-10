package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00122 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Integer indexNum = 1;
		String content = "thisIsContent";
		String remark = "thisIsRemark";
		Long graduateVerId = ConfigUtils.getLong("junitid", "graduate.ver.id");
		
		Map<String, Object> data = new HashMap<>();
		data.put("graduateVerId", graduateVerId);
		data.put("indexNum", indexNum);
		data.put("content", content);
		data.put("remark", remark);
		HttpResponse httpResponse = super.api("EM00122", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
	}
	
}
