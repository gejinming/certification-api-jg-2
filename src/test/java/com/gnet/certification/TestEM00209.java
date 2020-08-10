package com.gnet.certification;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import jodd.http.HttpResponse;

public class TestEM00209 extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		Map<String, Object> data = Maps.newHashMap();
		List<Map<String, Object>> list = Lists.newArrayList();
		Map<String, Object> student = Maps.newHashMap();
		student.put("studentNo", "1122000222");
		student.put("name", "导入学生1");
		student.put("sex", "0");
		student.put("idCard", "330226199803300022");
		student.put("status", "1");
		student.put("matriculateDate", "2016-05-21");
		student.put("className", "高网计算机111班");
		list.add(student);
		data.put("students", list);
		data.put("appointSchoolId", 165869L);
		HttpResponse httpResponse = super.api("EM00209", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertNotNull(object);
		System.out.println(object);
	}
}
