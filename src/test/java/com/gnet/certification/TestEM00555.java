package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

/**
 * 课程达成度报表显示
 * 
 * @author wct
 * @date 2016年7月21日
 */
public class TestEM00555 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查是否给
	 */
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("grade", 2017);
		data.put("versionId", 282717);
		data.put("graduateId", 282758);
		data.put("majorDirectionId", 282721);
//		{"grade":2017,"versionId":"282717","graduateId":282758,"majorDirectionId":282721}
		HttpResponse httpResponse = super.api("EM00555", token, data).send();
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
