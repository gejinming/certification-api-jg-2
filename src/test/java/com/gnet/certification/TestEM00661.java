package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

/**
 * 课程达成度历史对比统计接口测试用例
 * 
 * @author wct
 * @date 2016年8月9日
 */
public class TestEM00661 extends CertificationAddBeforeJUnit {
	
	@Test
	public void execute() {
		Map<String, Object> data = new HashMap<>();
		data.put("courseCode", "llkc");
		data.put("majorId", 472084);
//		data.put("courseCode", "02122107");
//		data.put("majorId", 252912);
		HttpResponse httpResponse = super.api("EM00661", token, data).send();
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
