package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;

import jodd.http.HttpResponse;
import org.junit.runner.RunWith;
import org.springframework.core.SpringVersion;
public class TestEM00001 extends CertificationJUnit {
	
	@Test
	public void testExecute() {
		String springVersion = SpringVersion.getVersion();
		System.out.println(springVersion);
	}
	
}
