package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00343 extends CertificationAddBeforeJUnit{
	
	/**
	 * 检查数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Long planTermId = ConfigUtils.getLong("junitid", "planTerm.id");
		Map<String, Object> data = new HashMap<>();
		data.put("id", planTermId);
		data.put("yearName", "year");
		data.put("year", 2);
		data.put("termName", "term");
		data.put("term", 2);
		data.put("termType", 2);
		data.put("planId", 2);
		data.put("weekNums", 16);
		data.put("remark", "remark");
		HttpResponse httpResponse = super.api("EM00343", token, data).send();
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
