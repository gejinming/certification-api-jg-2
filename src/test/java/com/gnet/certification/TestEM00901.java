package com.gnet.certification;

import java.io.File;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.google.common.collect.Maps;

import jodd.http.HttpResponse;

/**
 * 毕业要求excel导入上传解析接口
 * 
 * @author SY
 *
 */
public class TestEM00901 extends CertificationAddBeforeJUnit {

	public TestEM00901() {
		super("http://127.0.0.1:8080/certification-api/app/api?fileType=file");
	}
	
	/**
	 * 导入学生
	 */
	@Test
	public void testImportStudentExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("graduateVerId", 488331);
		File file = new File("C:/Users/SY/Downloads/graduateImportTemplate.xls");
		HttpResponse httpResponse = super.api("EM00901", token, data).form("upfile", file).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	
}
