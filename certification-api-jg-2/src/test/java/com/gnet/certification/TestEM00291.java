package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00291 extends CertificationAddBeforeJUnit  {

	/**
	 * 检查课程开课view正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Long teacherId = ConfigUtils.getLong("junitid", "teacher.id");
		Map<String, Object> data = new HashMap<>();
		data.put("id", teacherId);
		HttpResponse httpResponse = super.api("EM00291", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		ResponseHeader header = response.getHeader();
		if(object != null) {
			System.out.println(object);
		} else {
			System.out.println("返回错误码：" + header.getErrorcode());
		}
	}
	
}
