package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00332 extends CertificationAddBeforeJUnit {

	/**
	 * 检查所属模块view正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Long cousreModuleId = ConfigUtils.getLong("junitid", "courseModule.id");
		Map<String, Object> data = new HashMap<>();
		data.put("id", cousreModuleId);
		HttpResponse httpResponse = super.api("EM00332", token, data).send();
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
