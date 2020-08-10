package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00353 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查课程开课Edit正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Long termId = ConfigUtils.getLong("junitid", "term.id");
		Map<String, Object> data = new HashMap<>();
		data.put("id", termId);
		data.put("startYear", 2017);
		data.put("endYear", 2018);
		data.put("term", 2);
		data.put("term_type", 2);
		data.put("schoolId", "1");
		data.put("sort", 20);
		data.put("remark", "remark");
		HttpResponse httpResponse = super.api("EM00353", token, data).send();
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
