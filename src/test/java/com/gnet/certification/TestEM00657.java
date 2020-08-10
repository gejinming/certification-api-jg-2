package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

/**
 * 编辑课程区域测试用例
 * 
 * @author wct
 * @date 2016年8月8日
 */
public class TestEM00657 extends CertificationAddBeforeJUnit {
	
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("zoneId", 162514L);
		data.put("credit", 3.2);
		data.put("allHours", 64);
		data.put("theoryHours", 20);
		data.put("experimentHours", 28);
		data.put("practiceHours", 16);
		JSONObject term = new JSONObject();
		term.put("10001", 3.6);
		data.put("termObject", term);
		HttpResponse httpResponse = super.api("EM00657", token, data).send();
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
