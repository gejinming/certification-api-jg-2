package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

/**
 * 学生考评点成绩录入测试
 * 
 * @author wct
 * @date 2016年9月4日
 */
public class TestEM00400 extends CertificationAddBeforeJUnit {
	
	@Test
	public void testExecute() {
		
		Map<String, Object> data = new HashMap<>();
		data = new HashMap<>();
		data.put("evaluteId", 182701);
		data.put("studentId", 182686);
		data.put("levelId", 182713);
//		data.put("evaluteId", 163731L);
//		data.put("studentId", 162730L);
//		data.put("levelId", 163739L);
		HttpResponse httpResponse = super.api("EM00400", token, data).send();
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
