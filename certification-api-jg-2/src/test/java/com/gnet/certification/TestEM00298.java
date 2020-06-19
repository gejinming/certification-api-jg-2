package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;


import jodd.http.HttpResponse;

public class TestEM00298 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {

		Map<String, Object> data = new HashMap<>();
		data = new HashMap<>();
		data.put("pageNumber", 1);
		data.put("pageSize", 100);
//		data.put("officeId", 208561);// 浙江科技学院
//		data.put("officeId", 208562);// 信息与电子工程学院
		data.put("officeId", 208563);// 计算机科学与技术
		HttpResponse httpResponse = super.api("EM00298", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		System.out.println(object);

	}
	
}
