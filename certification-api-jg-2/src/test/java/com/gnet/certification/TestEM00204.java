package com.gnet.certification;

import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.google.common.collect.Maps;

import jodd.http.HttpResponse;

/**
 * 删除学生的测试用例
 * 
 * @author wct
 * @Date 2016年6月29日
 */
public class TestEM00204 extends CertificationAddBeforeJUnit {
	
	/**
	 * 删除一个学生
	 */
	@Test
	public void testPageExecute() {
		Map<String, Object> data = Maps.newHashMap();
		data.put("ids", new Long[]{162177L});
		HttpResponse httpResponse = super.api("EM00204", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
}
