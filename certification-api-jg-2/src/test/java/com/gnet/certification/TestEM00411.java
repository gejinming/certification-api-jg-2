package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

/**
 * 测试查看详细的数据能否正常输出
 * 
 * @author sll
 *
 */
public class TestEM00411 extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		Long courseGradecomposeDetailId = 162752L;
		Map<String, Object> data = new HashMap<>();
		data.put("id", courseGradecomposeDetailId);
		HttpResponse httpResponse = super.api("EM00411", token, data).send();
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
