package com.gnet.certification;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00104 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Integer enableGrade = 2014;
		Long versionId = ConfigUtils.getLong("junitid", "version.id");
		String no = "thisIsNu";
		String name = "thisIsName";
		String description = "thisIsDescription";
		String remark = "thisIsRemark";
		String planName = "planName";
		String planCourseVersionName = "planCourseVersionName";
		String graduateName = "graduateName";
		String graduateIndicationVersionName = "graduateIndicationVersionName";
		BigDecimal pass = new BigDecimal(0.211);
		
		Map<String, Object> data = new HashMap<>();
		data.put("id", versionId);
		data.put("no", no);
		data.put("name", name);
		data.put("remark", remark);
		data.put("enableGrade", enableGrade);
		data.put("description", description);
		data.put("planName", planName);
		data.put("planCourseVersionName", planCourseVersionName);
		data.put("graduateName", graduateName);
		data.put("graduateIndicationVersionName", graduateIndicationVersionName);
		data.put("pass", pass);
		HttpResponse httpResponse = super.api("EM00104", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
	}
	
}
