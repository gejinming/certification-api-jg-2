package com.gnet.certification;

import java.util.Date;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.google.common.collect.Maps;

import jodd.http.HttpResponse;

/**
 * 增加学生测试用例
 * 
 * @author wct
 * @Date 2016年6月29日
 */
public class TestEM00202 extends CertificationAddBeforeJUnit {
	
	/**
	 * 增加学生测试用例
	 */
	@Test
	public void testaddExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("studentNo", 100003);
		data.put("sex", 0);
		data.put("matriculateDate", new Date());
		data.put("name", "lll");
		data.put("grade", 2011);
		data.put("classId",162261L);
		HttpResponse httpResponse = super.api("EM00202", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
	

}
