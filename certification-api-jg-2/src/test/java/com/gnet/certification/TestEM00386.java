package com.gnet.certification;

import java.io.File;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.google.common.collect.Maps;

import jodd.http.HttpResponse;

/**
 * 教学班导入学生
 * 
 * @author wct
 * @date 2016年7月7日
 */
public class TestEM00386 extends CertificationAddBeforeJUnit {
	
	public TestEM00386() {
		super("http://127.0.0.1:8080/certification-api/app/api?fileType=file");
	}
	
	/**
	 * 导入
	 */
	@Test
	public void testImportListExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("eduClassId", 10001L);
		data.put("appointSchoolId", 162173L);
		File file = new File("D:/program/project/localProject/certification-api/src/main/resources/excel/studentImportToClassTemplate.xls");
		HttpResponse httpResponse = super.api("EM00386", token, data).form("upfile", file).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
}
