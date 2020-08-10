package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00110 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {

		Map<String, Object> data = new HashMap<>();
		data = new HashMap<>();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("versionId", 1L);
		HttpResponse httpResponse = super.api("EM00110", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);

	}
	
	/**
	 * 增加order by 以及searchValue
	 */
	@Test
	public void testExecuteStatueAll() {
		Long majorId = ConfigUtils.getLong("junitid", "major.id");

		Map<String, Object> data = new HashMap<>();
		data = new HashMap<>();
		data.put("majorId", majorId);
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("orderProperty", "name");
		data.put("orderDirection", "asc");
		data.put("majorName", "浙科院");
		HttpResponse httpResponse = super.api("EM00110", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);

	}
	
}
