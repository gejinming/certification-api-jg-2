package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.google.common.collect.Lists;
import jodd.http.HttpResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  专业负责人修改课程大纲
 * 
 * @author xzl
 * @date 2017年8月1日
 */
public class TestEM00698 extends CertificationAddBeforeJUnit {

	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		Map<String, Object> courseInfo1 = new HashMap<>();
		Map<String, Object> courseInfo2 = new HashMap<>();
		List<Map<String, Object>> courseInfoList = Lists.newArrayList();
		courseInfo1.put("name", "课程代码1");
		courseInfo1.put("databaseField", "code");
		courseInfo2.put("name", "课程类型1");
		courseInfo2.put("databaseField", "typeName");
		courseInfoList.add(courseInfo1);
		courseInfoList.add(courseInfo2);

		List<Map<String, Object>> moduleList = Lists.newArrayList();
		Map<String, Object> module1 = new HashMap<>();
		Map<String, Object> module2 = new HashMap<>();
		module1.put("title", "课程的性质，目的和任务1");
		module1.put("isExistMainContent", false);
		module1.put("isExistSecondaryContent", false);
		module1.put("isExistTeachingContent", false);
		module1.put("isExistTable", false);

		module2.put("title", "教学内容，基本要求及学时分配1");
		module2.put("isExistMainContent", false);
		module2.put("isExistSecondaryContent", false);
		module2.put("isExistTeachingContent", false);
		module2.put("isExistTable", false);

		List<Map<String, Object>> tables = Lists.newArrayList();
		Map<String, Object> table = new HashMap<>();
		table.put("tableName", "软件工程教学环节及基本要求1");

		//表头
		List<Map<String, Object>> headerList = Lists.newArrayList();
		Map<String, Object> headermap1 = new HashMap<>();
		Map<String, Object> headermap2 = new HashMap<>();
		Map<String, Object> headermap3 = new HashMap<>();
		Map<String, Object> headermap4 = new HashMap<>();
		Map<String, Object> headermap5 = new HashMap<>();
		Map<String, Object> headermap6 = new HashMap<>();
		headermap1.put("name", "理论学时1");
		headermap1.put("type", 2);

		headermap2.put("name", "上机学时1");
		headermap2.put("type", 2);

		headermap3.put("name", "实验学时1");
		headermap3.put("type", 2);

		headermap4.put("name", "实践学时1");
		headermap4.put("type", 2);

		headermap5.put("name", "小计1");
		headermap5.put("type", 3);

		headermap6.put("name", "支持的指标点1");
		headermap6.put("type", 4);

		headerList.add(headermap1);
		headerList.add(headermap2);
		headerList.add(headermap3);
		headerList.add(headermap4);
		headerList.add(headermap5);
		headerList.add(headermap6);
		table.put("headers", headerList);

		tables.add(table);
		module2.put("tables", tables);

		moduleList.add(module1);
		moduleList.add(module2);

		data.put("modules", moduleList);
		data.put("courseInfos", courseInfoList);
		data.put("name", "java课程大纲1");
   //     data.put("courseOutlineId", 165966);
		data.put("courseId", 273558);
		data.put("courseOutlineTypeId", 282572);
		data.put("isCreateTemplate", false);
//		data.put("templateName", "课程大纲模板");
		
		HttpResponse httpResponse = super.api("EM00698", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		ResponseHeader header = response.getHeader();
/*		Assert.assertNotNull(object);
		Assert.assertTrue(String.format("返回错误码：%s",header.getErrorcode()), object.getBoolean("isSuccess"));
		Assert.assertTrue(String.format("返回错误信息：%s", header.getErrormessage()), object.getBoolean("isSuccess"));*/
        System.out.println(header.getErrorcode());
		System.out.println(header.getErrormessage());
	}
}
