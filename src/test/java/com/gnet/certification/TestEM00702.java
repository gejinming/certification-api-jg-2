package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.google.common.collect.Lists;
import jodd.http.HttpResponse;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  教师编写课程大纲
 * 
 * @author xzl
 * @date 2017年8月4日
 */
public class TestEM00702 extends CertificationAddBeforeJUnit {
	

	@Test
	public void testExecute() {

		Map<String, Object> data = new HashMap<>();
		Map<String, Object> courseInfo1 = new HashMap<>();
		Map<String, Object> courseInfo2 = new HashMap<>();
		List<Map<String, Object>> courseInfoList = Lists.newArrayList();
		courseInfo1.put("name", "课程代码");
		courseInfo1.put("databaseField", "code");
		courseInfo1.put("content", "rjgc2");
		courseInfo2.put("name", "课程类型");
		courseInfo2.put("databaseField", "typeName");
		courseInfo1.put("content", "理论课");
		courseInfoList.add(courseInfo1);
		courseInfoList.add(courseInfo2);


		List<Map<String, Object>> moduleMap = Lists.newArrayList();
		Map<String, Object> module1 = new HashMap<>();
        Map<String, Object> module2 = new HashMap<>();

        //基本信息
		module1.put("title", "课程的性质，目的和任务");
		module1.put("mainContent", "软件工程师一门很好的学科");


		//指标点
		List<Map<String, Object>> indicationList = Lists.newArrayList();
		Map<String, Object> indicationmap = new HashMap<>();
		indicationmap.put("indicationId", 165914);

		module1.put("indications", indicationList);


		//教学内容
		List<Map<String, Object>> teachingContentList = Lists.newArrayList();
		Map<String, Object> teachingmap1 = new HashMap<>();
		teachingmap1.put("teachingContent",  "了解软件工程设计");
		teachingmap1.put("basicRequirement", "掌握软件工程的实现原理和实现方式");
		teachingmap1.put("hours", 6);
		Map<String, Object> teachingmap2 = new HashMap<>();
		teachingmap2.put("teachingContent",  "软件工程编写");
		teachingmap2.put("basicRequirement", "了解数据类型、变量、运算符和表达式");
		teachingmap2.put("hours", 6);
		teachingContentList.add(teachingmap1);
		teachingContentList.add(teachingmap2);
		module1.put("teachingContents", teachingContentList);

		//次要内容
		List<Map<String, Object>> secondaryContentList = Lists.newArrayList();
		Map<String, Object> secondarymap1 = new HashMap<>();
		secondarymap1.put("title", "软件工程的作者");
		secondarymap1.put("content", "高网xzl");
		Map<String, Object> secondarymap2 = new HashMap<>();
		secondarymap2.put("title", "软件工程的核心思想");
		secondarymap2.put("content", "软件工程从入门到放弃");
		secondaryContentList.add(secondarymap1);
		secondaryContentList.add(secondarymap2);
		module1.put("secondaryContents", secondaryContentList);


		module2.put("title", "课内外教学环节及基本要求");
		module2.put("mainContent", "见下表");

		List<Map<String, Object>> tables = Lists.newArrayList();
        Map<String, Object> table = new HashMap<>();
        table.put("tableName", "软件工程教学环节及基本要求");

        //表头
		List<Map<String, Object>> headerList = Lists.newArrayList();
		Map<String, Object> headermap1 = new HashMap<>();
		Map<String, Object> headermap2 = new HashMap<>();
		Map<String, Object> headermap3 = new HashMap<>();
		Map<String, Object> headermap4 = new HashMap<>();
		Map<String, Object> headermap5 = new HashMap<>();
		Map<String, Object> headermap6 = new HashMap<>();
		headermap1.put("name", "理论学时");
		headermap1.put("type", 2);
		headermap1.put("hoursType", 1);

		headermap2.put("name", "上机学时");
		headermap2.put("type", 2);
		headermap2.put("hoursType", 2);

		headermap3.put("name", "实验学时");
		headermap3.put("type", 2);
		headermap3.put("hoursType", 3);

		headermap4.put("name", "实践学时");
		headermap4.put("type", 2);
		headermap4.put("hoursType", 4);

		headermap5.put("name", "小计");
		headermap5.put("type", 3);
		headermap5.put("totalColumn", "1,2,3,4");

		headermap6.put("name", "支持的指标点");
		headermap6.put("type", 4);

		headerList.add(headermap1);
		headerList.add(headermap2);
		headerList.add(headermap3);
		headerList.add(headermap4);
		headerList.add(headermap5);
		headerList.add(headermap6);
		table.put("headers", headerList);

		//表格内容
		List<Map<String, Object>> tableList = Lists.newArrayList();
		Map<String, Object> formMap1 = new HashMap<>();
		Map<String, Object> formMap2 = new HashMap<>();
		Map<String, Object> formMap3 = new HashMap<>();
		Map<String, Object> formMap4 = new HashMap<>();
		Map<String, Object> formMap5 = new HashMap<>();
		Map<String, Object> formMap6 = new HashMap<>();

		formMap1.put("rowOrdinal", 1);
		formMap1.put("columnOrdinal", 1);
		formMap1.put("type", 2);
		formMap1.put("content", 1);

		formMap2.put("rowOrdinal", 1);
		formMap2.put("columnOrdinal", 2);
		formMap2.put("type", 2);
		formMap2.put("content", 1);

		formMap3.put("rowOrdinal", 1);
		formMap3.put("columnOrdinal", 3);
		formMap3.put("type", 2);
		formMap3.put("content", 1);

		formMap4.put("rowOrdinal", 1);
		formMap4.put("columnOrdinal", 4);
		formMap4.put("type", 2);
		formMap4.put("content", 2);

		formMap5.put("rowOrdinal", 1);
		formMap5.put("columnOrdinal", 5);
		formMap5.put("type", 3);
		formMap5.put("content", 5);

		formMap6.put("rowOrdinal", 1);
		formMap6.put("columnOrdinal", 6);
		formMap6.put("type", 4);
		formMap6.put("content", "165914");

		tableList.add(formMap1);
		tableList.add(formMap2);
		tableList.add(formMap3);
		tableList.add(formMap4);
		tableList.add(formMap5);
		tableList.add(formMap6);

        table.put("tableDetails", tableList);

		tables.add(table);
		module2.put("tables", tables);

		moduleMap.add(module1);
		moduleMap.add(module2);

		data.put("courseInfos", courseInfoList);
		data.put("name", "java课程大纲");
        data.put("courseOutlineId", 166228);
        data.put("isSubmit", true);
		data.put("modules", moduleMap);

		
		HttpResponse httpResponse = super.api("EM00702", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		ResponseHeader header = response.getHeader();
		Assert.assertNotNull(object);
		Assert.assertTrue(String.format("返回错误码：%s",header.getErrorcode()), object.getBoolean("isSuccess"));
		Assert.assertTrue(String.format("返回错误信息：%s", header.getErrormessage()), object.getBoolean("isSuccess"));
		System.out.println(object);
		System.out.println(String.format("返回错误码：%s",header.getErrorcode()));
		System.out.println(String.format("返回错误信息：%s", header.getErrormessage()));

	}
}
