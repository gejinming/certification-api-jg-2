package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

/**
 * 学生考评点成绩删除测试
 * 
 * @author SY
 * @date 2016年12月21日15:20:22
 */
public class TestEM00402 extends CertificationAddBeforeJUnit {
	
	@Test
	public void testExecute() {
		
		Map<String, Object> data = new HashMap<>();
		data = new HashMap<>();
		data.put("evaluteId", 182701);
		data.put("studentId", 182686);
//		data.put("evaluteId", 211424);
//		data.put("studentId", 210119);
		HttpResponse httpResponse = super.api("EM00402", token, data).send();
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
