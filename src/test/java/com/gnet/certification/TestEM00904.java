package com.gnet.certification;

import java.io.File;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.google.common.collect.Maps;

import jodd.http.HttpResponse;

/**
 * 理论课程excel导入上传解析接口
 * 
 * @author SY
 *
 */
public class TestEM00904 extends CertificationAddBeforeJUnit {

	public TestEM00904() {
		super("http://127.0.0.1:8080/certification-api/app/api?fileType=file");
	}
	
	/**
	 * 理论课程excel导入上传解析接口
	 */
	@Test
	public void testImportStudentExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("planId", 300297);
		File file = new File("C:/Users/SY/Downloads/theoryCourseImportTemplate.xls");
		HttpResponse httpResponse = super.api("EM00904", token, data).form("upfile", file).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
}
