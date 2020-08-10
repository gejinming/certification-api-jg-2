package com.gnet.certification;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM00102 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Long majorId = 1L;
		Long parentId = 10000L;
		String no = "thisIsNu";
		String name = "thisIsName";
		String description = "thisIsDescription";
		String remark = "thisIsRemark";
		Integer enableGrade = 2015;
		
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
		data.put("description", description);
		data.put("planName", planName);
		data.put("planCourseVersionName", planCourseVersionName);
		data.put("graduateName", graduateName);
		data.put("graduateIndicationVersionName", graduateIndicationVersionName);
		data.put("pass", pass);
		HttpResponse httpResponse = super.api("EM00102", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
	}
	
}
