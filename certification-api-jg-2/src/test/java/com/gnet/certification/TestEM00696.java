package com.gnet.certification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.google.common.collect.Lists;

import jodd.http.HttpResponse;

/**
 * 课程达成度多个年级和版本的报表生成状态查询测试用例
 * 
 * @author SY
 * @date 2017年1月22日09:34:13
 */
public class TestEM00696 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查能否正常增加成绩组成元素明细和指标点的关联
	 */
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		Map<String, Object> params = new HashMap<>();
		List<Map<String, Object>> list = Lists.newArrayList();
		params.put("grade", 2011);
		params.put("versionId", 211891);
		list.add(params);
		data.put("params", list);
		
		HttpResponse httpResponse = super.api("EM00696", token, data).send();
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
	
	/**
	 * 检查能否正常增加成绩组成元素明细和指标点的关联
	 */
	@Test
	public void testExecute2() {
		Map<String, Object> data = new HashMap<>();
		Map<String, Object> params = new HashMap<>();
		List<Map<String, Object>> list = Lists.newArrayList();
		params.put("grade", 20112);
		params.put("versionId", 2211891);
		list.add(params);
		data.put("params", list);
		
		HttpResponse httpResponse = super.api("EM00696", token, data).send();
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
	
	/**
	 * 检查能否正常增加成绩组成元素明细和指标点的关联
	 */
	@Test
	public void testExecute3() {
		Map<String, Object> data = new HashMap<>();
		Map<String, Object> params = new HashMap<>();
		Map<String, Object> params2 = new HashMap<>();
		List<Map<String, Object>> list = Lists.newArrayList();
		params.put("grade", 2015);
		params.put("versionId", 254412);
		params2.put("grade", 2011);
		params2.put("versionId", 165889);
		list.add(params);
		data.put("params", list);
		
		HttpResponse httpResponse = super.api("EM00696", token, data).send();
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
