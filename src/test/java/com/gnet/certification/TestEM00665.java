package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

/**
 * 学生个人专业达成度报表显示
 * 
 * @author wct
 * @date 2016年11月12日
 */
public class TestEM00665 extends CertificationAddBeforeJUnit {
	
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("grade", 2015);
		data.put("studentId", 473156);
		data.put("majorId", 472084);
//		data.put("grade", 2015);
//		data.put("studentId", 286155);
//		data.put("majorId", 282703);

		
		HttpResponse httpResponse = super.api("EM00665", token, data).send();
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
