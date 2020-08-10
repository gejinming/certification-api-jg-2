package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

/**
 * 开课课程考评点层次名称唯一性校验接口测试用例
 *
 * @author wct
 * @date 2016年9月3日
 */
public class TestEM00395 extends CertificationAddBeforeJUnit {
	
	@Test
	public void execute() {
		Map<String, Object> data = new HashMap<>();
		data.put("teacherCourseId", 163440L);
		data.put("indicationId", 163020L);
		data.put("levelName", "A");
		HttpResponse httpResponse = super.api("EM00395", token, data).send();
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
