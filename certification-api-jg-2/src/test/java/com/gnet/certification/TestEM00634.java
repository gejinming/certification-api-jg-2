package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

public class TestEM00634 extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("id", 162511);
		data.put("grade", 2016);
		data.put("year", 2017);
		data.put("studentNo", 777);
		data.put("studentName", "xzl");
		data.put("studentSex", 0);
		data.put("transferInMajorName", "软工111");
		data.put("transferOutMajorName", "计算机111");
		
		HttpResponse httpResponse = super.api("EM00634", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
	}
	
}
