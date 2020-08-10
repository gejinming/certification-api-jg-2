package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

/**
 * 增加考评点得分层次切换层次（2级或者5级）
 * 
 * @author SY
 * 
 * @date 2017年8月9日17:46:07
 *
 */
public class TestEM00396 extends CertificationAddBeforeJUnit {
	
	@Test
	public void execute() {
		Map<String, Object> data = new HashMap<>();
		data.put("teacherCourseId", 286163);
		data.put("level", 2);
		HttpResponse httpResponse = super.api("EM00396", token, data).send();
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
