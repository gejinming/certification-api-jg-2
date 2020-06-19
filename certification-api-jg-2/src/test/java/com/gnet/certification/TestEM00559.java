package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

/**
 * 个人达成度报表查看
 * 
 * @author wct
 * @date 2016年7月12日
 */
public class TestEM00559 extends CertificationAddBeforeJUnit {
	
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("versionId", 282717);
		data.put("studentId", 283680);
		data.put("graduateId", 282758);
		data.put("grade", 2013);
//		versionId":"282717","studentId":286155,"graduateId":282776,"grade":2015
//		{"data":{"versionId":"282717","studentId":300879,"graduateId":282758,"grade":2016}
//		{"versionId":"282717","studentId":283680,"graduateId":282758,"grade":2013
		HttpResponse httpResponse = super.api("EM00559", token, data).send();
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
