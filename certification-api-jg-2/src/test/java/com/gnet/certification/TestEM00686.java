package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

/**
 * 通过行政班编号和教学班编号获取不在相同课程、相同学期排课课程教学班中的指定行政班的学生
 * 
 * @author xzl
 *
 */
public class TestEM00686 extends CertificationAddBeforeJUnit{
	
	@Test
	public void testExecute() {
		

		Map<String, Object> data = new HashMap<>();
		data = new HashMap<>();
		data.put("classId", 165878);
		data.put("eduClassId", 167707);
		HttpResponse httpResponse = super.api("EM00686", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		System.out.println(object);

	}

}
