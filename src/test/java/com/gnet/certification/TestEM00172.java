package com.gnet.certification;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.plugin.configLoader.ConfigUtils;

import jodd.http.HttpResponse;

public class TestEM00172 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		
		Map<String, Object> data = new HashMap<>();
		Long planId = ConfigUtils.getLong("junitid", "plan.id");
		Long hierarchyId = ConfigUtils.getLong("junitid", "hierarchy.id");
		Long propertyId = ConfigUtils.getLong("junitid", "property.id");
		Long directionId = ConfigUtils.getLong("junitid", "direction.id");
		data.put("planId", planId);
		data.put("code", "code22");
		data.put("name", "name");
		data.put("englishName", "englishName");
		data.put("hierarchyId", hierarchyId);
		data.put("propertyId", propertyId);
		data.put("directionId", directionId);
		data.put("credit", 2);
		data.put("allHours", 2);
		data.put("theoryHours", 2);
		data.put("experimentHours", 2);
		data.put("practiceHours", 2);
		data.put("weekHour", 2);
		Long[] planTermClassIds = new Long[2];
		planTermClassIds[0] = 100L;
		planTermClassIds[1] = 101L;
		data.put("planTermClassIds", planTermClassIds);
		data.put("planTermExamIds", null);
		
		data.put("applicationMajor", "applicationMajor");
		data.put("participator", "participator");
		data.put("department", "11");
		data.put("prerequisite", "prerequisite");
		data.put("nextrequisite", "nextrequisite");
		data.put("teamLeader", "teamLeader");
		data.put("professorLeader", "professorLeader");
		data.put("aduitDean", "aduitDean");
		data.put("outlineAuthorId", "111");
		data.put("remark", "remark");
		data.put("isDel", Boolean.FALSE);
		data.put("sort", 1);
		data.put("indepentHours", new BigDecimal(10));
		
		
		HttpResponse httpResponse = super.api("EM00172", token, data).send();
		this.isOk(httpResponse);
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
		System.out.println(object);
		
	}
	
}
