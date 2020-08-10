package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

/**
 * 某课程考评点类别增加或修改接口
 * 
 * @author SY
 * 
 * @date 2017年8月14日
 */
public class TestEM00406 extends CertificationAddBeforeJUnit {
	
	@Test
	public void testExecute() {
		
		Map<String, Object> data = new HashMap<>();
		JSONArray indexNumArray = new JSONArray();
		Map<String, Object> map1 = new HashMap<>();
		map1.put("type", 1);
		map1.put("percentage", 60);
		indexNumArray.add(map1);
		Map<String, Object> map2 = new HashMap<>();
		map2.put("type", 2);
		map2.put("percentage", 40);
		indexNumArray.add(map2);
		data.put("typePercentageArray", indexNumArray.toString());
		data.put("teacherCourseId", 251986);
		HttpResponse httpResponse = super.api("EM00406", token, data).send();
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
