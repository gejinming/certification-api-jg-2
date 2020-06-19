package com.gnet.certification;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM10102 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		// 专业id
		Long majorId = 300133L; // 机械设计制造及其自动化专业2017版
//		Long majorId = 300135L; // 汽车服务工程
//		Long parentId = 10000L;
//		String no = "thisIsNu";
		String name = "机械设计制造及其自动化专业2017版";
		String description = null;
		String remark = null;
		// 启用年级
		Integer enableGrade = 2015;
		// 适用年级
//		String applyGrade = "2015";
		String applyGrade = "2015,2016";
		
		String planName = name;
		String planCourseVersionName = name;
		String graduateName = name;
		String graduateIndicationVersionName = name;
		BigDecimal pass = new BigDecimal(0.61);
		
		Map<String, Object> data = new HashMap<>();
//		data.put("no", no);
		data.put("name", name);
		data.put("remark", remark);
		data.put("majorId", majorId);
//		data.put("parentId", parentId);
		data.put("enableGrade", enableGrade);
		data.put("applyGrade", applyGrade);
		data.put("description", description);
		data.put("planName", planName);
		data.put("planCourseVersionName", planCourseVersionName);
		data.put("graduateName", graduateName);
		data.put("graduateIndicationVersionName", graduateIndicationVersionName);
		data.put("pass", pass);
		HttpResponse httpResponse = super.api("EM10102", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
	}
	
}
