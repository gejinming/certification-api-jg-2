package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

public class TestEM00112 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		
		Map<String, Object> data = new HashMap<>();
		data.put("code", "7777");
		data.put("name", "name");
		data.put("country", "country");
		data.put("nativePlace", "nativePlace");
		data.put("nation", "nation");
		data.put("politics", "politics");
		data.put("idCard", "idCard");
		data.put("highestEducation", "highestEducation");
		data.put("highestDegrees", 1);
		data.put("bachelorSchool", "bachelorSchool");
		data.put("bachelorMajor", "bachelorMajor");
		data.put("masterSchool", "masterSchool");
		data.put("masterMajor", "masterMajor");
		data.put("doctorateSchool", "doctorateSchool");
		data.put("doctorateMajor", "doctorateMajor");
		data.put("jobTitle", 1);
		data.put("administrative", "administrative");
		data.put("mobilePhone", "mobilePhone");
		data.put("mobilePhoneSec", "mobilePhoneSec");
		data.put("officePhoneSec", "officePhoneSec");
		data.put("officePhone", "officePhone");
		data.put("qq", "qq");
		data.put("wechat", "wechat");
		data.put("email", "email");
		data.put("officeAddress", "officeAddress");
		data.put("sex", 1);
		data.put("majorId", 1L);
		data.put("instituteId", 1L);
		data.put("schoolId", 1L);
		data.put("birthday", "2016-06-23");
		data.put("comeSchoolTime", "2016-06-23");
		data.put("startEducationYear", "2016-06-23");
		data.put("isLeave", Boolean.FALSE);
		// 放入User信息
		data.put("isEnabled", Boolean.TRUE);
		data.put("departmentId", "departmentId");
		data.put("password", "password1234");

		HttpResponse httpResponse = super.api("EM00112", token, data).send();
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
