package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

/**
 * 个人达成度报表生成
 * 
 * @author wct
 * @date 2016年7月30日
 */
public class TestEM00558 extends CertificationAddBeforeJUnit  {
	
	/**
	 * 测试报表生成
	 */
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("grade", 2015);
		data.put("versionId", 472088);
//		"grade":2013,"versionId":"282717"
		HttpResponse httpResponse = super.api("EM00558", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		ResponseHeader header = response.getHeader();
		if(object != null) {
			System.out.println(object);
		} else {
			System.out.println("返回错误码：" + header.getErrorcode());
		}
	}
}
