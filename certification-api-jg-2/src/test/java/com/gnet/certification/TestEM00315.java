package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

/**
 * 查看指定持续改进版本里指定年级下教学班列表的接口
 * 
 * @author SY
 *
 */
public class TestEM00315 extends CertificationAddBeforeJUnit{
	
	@Test
	public void testExecute() {
		
		Long versionId = ConfigUtils.getLong("junitid", "version.id");

		Map<String, Object> data = new HashMap<>();
		data = new HashMap<>();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("versionId", versionId);
		data.put("grade", 2014);
		HttpResponse httpResponse = super.api("EM00315", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);

	}

}
