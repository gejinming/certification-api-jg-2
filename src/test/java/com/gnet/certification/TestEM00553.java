package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

/**
 * 教学班报表显示--显示优良等信息，而不是分数（在EM00552基础上，多做了一步计算）
 * 
 * @author SY
 * @date 2017年8月10日17:20:24
 */
public class TestEM00553 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查能否正常增加成绩组成元素明细和指标点的关联
	 */
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
//		data.put("eduClassId", 251987); // 评分表分析法
//		data.put("eduClassId", 182827); // 考核分析法
//		data.put("eduClassId", 286164);
		data.put("eduClassId", 470274);
//		data.put("indicationId", 210833L);
		
		HttpResponse httpResponse = super.api("EM00553", token, data).send();
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
