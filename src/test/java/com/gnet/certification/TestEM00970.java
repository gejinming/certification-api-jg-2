package com.gnet.certification;

import jodd.http.HttpResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestEM00970 extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		//471405
		//478357
		Map<String, Object> data = new HashMap<>();
		data = new HashMap<>();
		data.put("courseGradeComposeId", 471405);
		HttpResponse httpResponse = super.api("EM00970", token, data).send();

	}
}
