package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

public class TestEM00276 extends CertificationAddBeforeJUnit {

	/**
	 * 检查课程开课Add正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("courseId", 166020);
		data.put("indicationId", 99999);
		data.put("weight", 0.4);
		data.put("eduAim", "eduAim");
		data.put("means", "means");
		data.put("source", "source");
		data.put("way", "way");
		
		HttpResponse httpResponse = super.api("EM00276", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		ResponseHeader header = response.getHeader();
		if(object != null) {
			Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
			System.out.println(object);
		} else {
			System.out.println("返回错误码：" + header.getErrorcode());
		}
	}
	
	
}
