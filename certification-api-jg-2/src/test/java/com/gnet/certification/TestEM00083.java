package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00083 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Long majorId = ConfigUtils.getLong("junitid", "major.id");
		// 查询，学院id
		Long instituteId = ConfigUtils.getLong("junitid", "institute83.id");
		// 专业名字
		String majorName = "name";
		// 专业编码
		String code = "code2312";
		
		Integer educationLength = 2;
		Integer specializedFields = 2;
		Integer awardDegree = 2;
		Integer educationLevel = 2;
		Date date = new Date();
		Map<String, Object> data = new HashMap<>();
		data.put("educationLength", educationLength);
		data.put("id", majorId);
		data.put("instituteId", instituteId);
		data.put("code", code);
		data.put("majorName", majorName);
		data.put("specializedFields", specializedFields);
		data.put("awardDegree", awardDegree);
		data.put("educationLevel", educationLevel);
		data.put("description", "description:" + date);
		HttpResponse httpResponse = super.api("EM00083", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		System.out.println(object);
	}
	
}
