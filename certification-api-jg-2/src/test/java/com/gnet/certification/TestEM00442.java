package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

public class TestEM00442 extends CertificationAddBeforeJUnit {

	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("year", 2019);
		data.put("graduateNums", 10);
		data.put("graduateRatio", 99);
		data.put("getDegreeRatio", 99);
		data.put("firsttimeEmployedRatio", 99);
		data.put("masterAndGoabroadRatio", 199);
		data.put("nationAndInstitutionRatio", 99);
		data.put("otherEnterpriseRatio", 99);
		data.put("majorId", 2L);
		data.put("remark", "remark");
		HttpResponse httpResponse = super.api("EM00442", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		ResponseHeader header = response.getHeader();
		if(object != null) {
			Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
			System.out.println(object);
		} else {
			System.out.println("返回错误码：" + header.getErrorcode());
		}
	}
	
}
