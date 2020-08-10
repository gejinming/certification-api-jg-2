package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM00293 extends CertificationAddBeforeJUnit {

	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		
		Map<String, Object> data = new HashMap<>();
//		Long id = ConfigUtils.getLong("junitid", "teacher.id");
		Long[] roleIds = new Long[1];
		roleIds[0] = 1L;
		data.put("id", 999);
		data.put("code", "O(∩_∩)O哈哈~");
		data.put("name", "O(∩_∩)O哈哈~");
		data.put("birthday", "1955-06-23");
		data.put("country", "O(∩_∩)O哈哈~");
		data.put("nativePlace", "O(∩_∩)O哈哈~");
		data.put("nation", "O(∩_∩)O哈哈~");
		data.put("politics", "O(∩_∩)O哈哈~");
		data.put("idCard", "O(∩_∩)O哈哈~");
		data.put("highestEducation", "O(∩_∩)O哈哈~");
		data.put("highestDegrees", 444);
		data.put("bachelorSchool", "O(∩_∩)O哈哈~");
		data.put("bachelorMajor", "O(∩_∩)O哈哈~");
		data.put("masterSchool", "O(∩_∩)O哈哈~");
		data.put("masterMajor", "O(∩_∩)O哈哈~");
		data.put("doctorateSchool", "O(∩_∩)O哈哈~");
		data.put("doctorateMajor", "O(∩_∩)O哈哈~");
		data.put("jobTitle", 444);
		data.put("administrative", "O(∩_∩)O哈哈~");
		data.put("mobilePhone", "O(∩_∩)O哈哈~");
		data.put("mobilePhoneSec", "O(∩_∩)O哈哈~");
		data.put("officePhoneSec", "O(∩_∩)O哈哈~");
		data.put("officePhone", "O(∩_∩)O哈哈~");
		data.put("qq", "O(∩_∩)O哈哈~");
		data.put("wechat", "O(∩_∩)O哈哈~");
		data.put("email", "O(∩_∩)O哈哈~");
		data.put("officeAddress", "O(∩_∩)O哈哈~");
		data.put("sex", "1");
		data.put("majorId", 162261L);
		data.put("comeSchoolTime", "2016-7-29 15:12:03");
		data.put("startEducationYear", "2016-7-29 15:12:03");
		data.put("isLeave", Boolean.FALSE);
		data.put("roleIds", roleIds);
		
		// 放入User信息
//		Long roleId = ConfigUtils.getLong("junitid", "role.id");
		data.put("isEnabled", Boolean.TRUE);
		data.put("departmentId", 162263L);
		data.put("password", "O(∩_∩)O哈哈~");
		HttpResponse httpResponse = super.api("EM00293", token, data).send();
		System.out.println(httpResponse);
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
	}
}
