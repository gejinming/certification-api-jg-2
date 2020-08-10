package com.gnet.certification;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM10101 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		// 专业id
		Long majorId = 300135L; // 汽车服务工程
		Long parentId = 471388L;
		String no = "thisIsNu";
		String name = "thisIsName";
		String description = "thisIsDescription";
		String remark = "thisIsRemark";
		// 启用年级
		Integer enableGrade = 2016;
		// 适用年级
		String applyGrade = "2016";
//		String applyGrade = "2015,2016";
		
		String planName = "planName";
		String planCourseVersionName = "planCourseVersionName";
		String graduateName = "graduateName";
		String graduateIndicationVersionName = "graduateIndicationVersionName";
		BigDecimal pass = new BigDecimal(0.211);
		
		Map<String, Object> data = new HashMap<>();
		data.put("no", no);
		data.put("name", name);
		data.put("remark", remark);
		data.put("majorId", majorId);
		data.put("parentId", parentId);
		data.put("enableGrade", enableGrade);
		data.put("applyGrade", applyGrade);
		data.put("description", description);
		data.put("planName", planName);
		data.put("planCourseVersionName", planCourseVersionName);
		data.put("graduateName", graduateName);
		data.put("graduateIndicationVersionName", graduateIndicationVersionName);
		data.put("pass", pass);
		HttpResponse httpResponse = super.api("EM10101", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
	}
	
}
