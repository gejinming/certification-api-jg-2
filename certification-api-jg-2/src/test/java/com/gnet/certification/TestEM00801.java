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
 *  课程目标列表
 * 
 * @author xzl
 * @date 2017年11月22日15:02:19
 */
public class TestEM00801 extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("courseIndicationId", 471414);

		HttpResponse httpResponse = super.api("EM00801", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		ResponseHeader header = response.getHeader();
		JSONObject object = (JSONObject) response.getData();
		System.out.println(object);
        System.out.println(header.getErrorcode());
		System.out.println(header.getErrormessage());
	}
}
