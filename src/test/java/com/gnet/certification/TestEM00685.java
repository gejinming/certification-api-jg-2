package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

/**
 * 通过专业编号和年级显示同一版本下其它年级的行政班
 * 
 * @author xzl
 *
 */
public class TestEM00685 extends CertificationAddBeforeJUnit{
	
	@Test
	public void testExecute() {
		

		Map<String, Object> data = new HashMap<>();
		data = new HashMap<>();
		data.put("pageNumber", 1);
		data.put("pageSize", 20);
		data.put("majorId", 282703);
		data.put("grade", 2016);
//		data.put("grade", 2013);
//		data.put("majorId", 165877);
//		data.put("grade", 2011);
//		"majorId":282703,"grade":2016 no
//		"majorId":282703,"grade":2013 ok
		HttpResponse httpResponse = super.api("EM00685", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		System.out.println(object);

	}

}
