package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;

/**
 * 检查开课课程成绩组成元素是否增加成功
 * 
 * @author xzl
 *
 */
public class TestEM00520 extends CertificationAddBeforeJUnit{
	
	@Test
	public void testExecute() {
		
		Map<String, Object> data = new HashMap<>();
		
		JSONArray indexNumArray = new JSONArray();
		Map<String, Object> map1 = new HashMap<>();
		map1.put("gradecomposeId", 162221L);
		map1.put("percentage", 60);
		indexNumArray.add(map1);
		Map<String, Object> map2 = new HashMap<>();
		map2.put("gradecomposeId", 162223L);
		map2.put("percentage", 40);
		indexNumArray.add(map2);
		data.put("gradecomposeIdPercentageArray", indexNumArray.toString());
		
		data.put("teacherCourseId", 162341);
		HttpResponse httpResponse = super.api("EM00520", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);

	}

}
