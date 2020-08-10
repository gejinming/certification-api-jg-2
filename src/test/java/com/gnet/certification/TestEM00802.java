package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import jodd.http.HttpResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 *  某门课程下已关联课程指标点的课程目标列表
 * 
 * @author xzl
 * @date 2017年11月22日15:02:19
 */
public class TestEM00802 extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("courseId", 471395);

		HttpResponse httpResponse = super.api("EM00802", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		ResponseHeader header = response.getHeader();
		JSONObject object = (JSONObject) response.getData();
		System.out.println(object);
        System.out.println(header.getErrorcode());
		System.out.println(header.getErrormessage());
	}
}
