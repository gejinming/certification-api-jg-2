package com.gnet.certification;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.google.common.collect.Maps;

import jodd.http.HttpResponse;

public class TestEM00782 extends CertificationAddBeforeJUnit {
	
//	public TestEM00742() {
//		super("http://127.0.0.1:8080/certification-api/app/api?fileType=file");
//	}
//	
//	/**
//	 * 导出学生的成绩模板
//	 */
//	@Test
//	public void testImportStudentExecute() {
//		Map<String, Object> data = Maps.newHashMap();
//		data.put("appointSchoolId", 165869L);
//		File file = new File("D:/program/project/localProject/certification-api/src/main/resources/excel/studentImportTemplate.xls");
//		HttpResponse httpResponse = super.api("EM00742", token, data).form("upfile", file).send();
//		this.isOk(httpResponse);
//		Response response = this.parse(httpResponse);
//		JSONObject object = (JSONObject) response.getData();
//		Assert.assertNotNull(object);
//		System.out.println(object);
//	}
	@Test
	public void testExecute() {
		
		Map<String, Object> data = new HashMap<>();
		data = new HashMap<>();
		// 考核
//		data.put("eduClassId", 301846);
		data.put("eduClassId", 303249);
		// 考评点
//		data.put("eduClassId", 302649);
		HttpResponse httpResponse = super.api("EM00743", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);

	}
}
