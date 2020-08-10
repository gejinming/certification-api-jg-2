package com.gnet.certification;

import java.io.File;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.google.common.collect.Maps;

import jodd.http.HttpResponse;

public class TestEM00207 extends CertificationAddBeforeJUnit {
	
	public TestEM00207() {
		super("http://127.0.0.1:8080/certification-api/app/api?fileType=file");
	}
	
	/**
	 * 导入学生
	 */
	@Test
	public void testImportStudentExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("appointSchoolId", 165869L);
		File file = new File("D:/program/project/localProject/certification-api/src/main/resources/excel/studentImportTemplate.xls");
		HttpResponse httpResponse = super.api("EM00207", token, data).form("upfile", file).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
}
