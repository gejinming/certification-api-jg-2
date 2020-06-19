package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

/**
 * 培养计划统计接口测试用例
 * 
 * @author wct
 * @date 2016年8月5日
 */
public class TestEM00655 extends CertificationAddBeforeJUnit {
	
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("planId", 10001L);
		HttpResponse httpResponse = super.api("EM00655", token, data).send();
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
