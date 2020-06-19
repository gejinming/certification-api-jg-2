package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

/**
 * 报表查看
 * 
 * @author SY
 * @date 2017年12月27日
 */
public class TestEM00851 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查
	 */
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("courseId", 472106);
		data.put("indicatorPointId", 472160);
		data.put("grade", 2015);
//		data.put("eduClassId", 471791);
//		data.put("indicatorPointId", 471412);
//		data.put("eduClassId", 283719);
//		data.put("indicationId", 282759);
//		data.put("eduClassId", 210862L);
//		data.put("indicationId", 210833L);
//		"eduClassId":286164,"indicationId":282759
//		{"eduClassId":286164,"indicationId":282778}
		
		HttpResponse httpResponse = super.api("EM00851", token, data).send();
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
