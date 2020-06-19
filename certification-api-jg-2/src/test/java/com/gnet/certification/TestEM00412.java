package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

public class TestEM00412 extends CertificationAddBeforeJUnit {

	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		Long[] indicationIdArray = new Long[1];
		indicationIdArray[0] = 165849L;
		data.put("indicationIdArray", indicationIdArray);
		data.put("name", "name");
		data.put("score", 10);
		data.put("detail", "detail");
		data.put("remark", "remark");
		data.put("courseGradecomposeId", 166176L);
		HttpResponse httpResponse = super.api("EM00412", token, data).send();
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
