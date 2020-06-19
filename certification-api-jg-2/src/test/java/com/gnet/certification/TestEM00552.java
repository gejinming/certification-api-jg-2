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
 * @author wct
 * @date 2016年7月12日
 */
public class TestEM00552 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查能否正常增加成绩组成元素明细和指标点的关联
	 */
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("eduClassId", 475447);
		data.put("indicatorPointId", 472160);
		data.put("isCaculate", false);
//		data.put("eduClassId", 473155);
//		data.put("indicatorPointId", 472160);
//		data.put("eduClassId", 471791);
//		data.put("indicatorPointId", 471412);
//		data.put("eduClassId", 283719);
//		data.put("indicationId", 282759);
//		data.put("eduClassId", 210862L);
//		data.put("indicationId", 210833L);
//		"eduClassId":286164,"indicationId":282759
//		{"eduClassId":286164,"indicationId":282778}
		
		HttpResponse httpResponse = super.api("EM00552", token, data).send();
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
