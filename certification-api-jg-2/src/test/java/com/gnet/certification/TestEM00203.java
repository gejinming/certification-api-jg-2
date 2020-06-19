package com.gnet.certification;

import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.google.common.collect.Maps;

import jodd.http.HttpResponse;

/**
 * 编辑学生测试用例
 * 
 * @author wct
 * @Date 2016年6月29日
 */
public class TestEM00203 extends CertificationAddBeforeJUnit {
	
	/**
	 * 分页
	 */
	@Test
	public void testPageExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("id", 162177);
		data.put("studentNo", 100001);
		data.put("sex", 0);
		data.put("matriculateDate", "2016-06-29");
		data.put("name", "xxxxx");
		HttpResponse httpResponse = super.api("EM00203", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
}
