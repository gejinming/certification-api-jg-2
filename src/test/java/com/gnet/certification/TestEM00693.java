package com.gnet.certification;

import java.io.File;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.google.common.collect.Maps;

import jodd.http.HttpResponse;

public class TestEM00693 extends CertificationAddBeforeJUnit {
	
	public TestEM00693() {
		super("http://127.0.0.1:8080/certification-api/app/api?fileType=file");
	}
	
	/**
	 * 导入学生
	 */
	@Test
	public void testImportStudentExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("appointSchoolId", 165869L);
		File file = new File("F:/workspaces/Eclipse_All/Eclipse_mars/certification-api/src/main/resources/excel/teacherImportTemplate.xls");
		HttpResponse httpResponse = super.api("EM00693", token, data).form("upfile", file).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
}
