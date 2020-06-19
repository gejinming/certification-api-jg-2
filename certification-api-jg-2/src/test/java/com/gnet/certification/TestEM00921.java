package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.plugin.configLoader.ConfigUtils;
import jodd.http.HttpResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestEM00921 extends CertificationAddBeforeJUnit {

	/**
	 * 检查数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("id", 232);
		HttpResponse httpResponse = super.api("EM00921", token, data).send();
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
