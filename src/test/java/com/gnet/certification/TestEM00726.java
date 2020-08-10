package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import jodd.http.HttpResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestEM00726 extends CertificationAddBeforeJUnit {
	
	/**
	 * 课程大纲列表导出
	 */
	@Test
	public void testExecute() {

		Map<String, Object> data = new HashMap<>();
		data = new HashMap<>();
		data.put("planId", 355474);
		HttpResponse httpResponse = super.api("EM00726", token, data).send();
	}
}
