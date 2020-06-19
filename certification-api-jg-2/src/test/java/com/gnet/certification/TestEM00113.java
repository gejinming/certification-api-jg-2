package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00113 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		
		Map<String, Object> data = new HashMap<>();
		Long id = ConfigUtils.getLong("junitid", "teacher.id");
		data.put("id", id);
		data.put("code", "33333");
		data.put("name", "武汉教师-1");
		data.put("country", "country");
		data.put("nativePlace", "nativePlace");
		data.put("nation", "nation");
		data.put("politics", "politics");
		data.put("idCard", "idCard");
		data.put("highestEducation", "highestEducation");
		data.put("highestDegrees", 2);
		data.put("bachelorSchool", "bachelorSchool");
		data.put("bachelorMajor", "bachelorMajor");
		data.put("masterSchool", "masterSchool");
		data.put("masterMajor", "masterMajor");
		data.put("doctorateSchool", "doctorateSchool");
		data.put("doctorateMajor", "doctorateMajor");
		data.put("jobTitle", 2);
		data.put("administrative", "administrative");
		data.put("mobilePhone", "mobilePhone");
		data.put("mobilePhoneSec", "mobilePhoneSec");
		data.put("officePhoneSec", "officePhoneSec");
		data.put("officePhone", "officePhone");
		data.put("qq", "qq");
		data.put("wechat", "wechat");
		data.put("email", "email");
		data.put("officeAddress", "officeAddress");
		data.put("sex", "0");
		data.put("majorId", 165523L);
		data.put("instituteId", 165519L);
		data.put("schoolId", 165517L);
		data.put("birthday", "2016-06-23");
		data.put("comeSchoolTime", "2016-06-23");
		data.put("startEducationYear", "2016-10");
		data.put("isLeave", Boolean.FALSE);
		// 放入User信息
		Long roleId = ConfigUtils.getLong("junitid", "role.id");
		Long[] roleIds = new Long[1];
		roleIds[0] = roleId;
		data.put("isEnabled", Boolean.TRUE);
		data.put("departmentId", "165523");
		data.put("password", "1234");
		data.put("roleIds", roleIds);
		HttpResponse httpResponse = super.api("EM00113", token, data).send();
		System.out.println(httpResponse);
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
	}
	
}
