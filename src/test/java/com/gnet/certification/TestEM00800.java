package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.google.common.collect.Lists;
import jodd.http.HttpResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  新增课程目标
 * 
 * @author xzl
 * @date 2017年11月22日15:02:19
 */
public class TestEM00800 extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		Map<String, Object> courseTarget1 = new HashMap<>();
		Map<String, Object> courseTarget2 = new HashMap<>();
		List<Map<String, Object>> courseTargets = Lists.newArrayList();

		data.put("courseIndicationId", 471414);

		courseTarget1.put("sort", 1);
		courseTarget1.put("content", "课程目标1");
		courseTarget1.put("isRelate", true);
		courseTargets.add(courseTarget1);

		courseTarget2.put("sort", 2);
		courseTarget2.put("content", "课程目标1");
		courseTarget2.put("isRelate", true);
		courseTargets.add(courseTarget2);

		data.put("courseTargets", courseTargets);

		HttpResponse httpResponse = super.api("EM00800", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		ResponseHeader header = response.getHeader();
        System.out.println(header.getErrorcode());
		System.out.println(header.getErrormessage());
	}
}
