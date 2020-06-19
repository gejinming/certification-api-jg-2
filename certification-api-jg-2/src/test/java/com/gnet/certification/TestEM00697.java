package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Table;
import jodd.http.HttpResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  专业负责人创建课程大纲以及大纲模板
 * 
 * @author xzl
 * @date 2017年8月1日
 */
public class TestEM00697 extends CertificationAddBeforeJUnit {
	

	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		Map<String, Object> courseInfo1 = new HashMap<>();
		Map<String, Object> courseInfo2 = new HashMap<>();
		List<Map<String, Object>> courseInfoList = Lists.newArrayList();
		courseInfo1.put("name", "课程代码");
		courseInfo1.put("databaseField", "code");
		courseInfo2.put("name", "课程类型");
		courseInfo2.put("databaseField", "typeName");
		courseInfoList.add(courseInfo1);
		courseInfoList.add(courseInfo2);

		List<Map<String, Object>> moduleList = Lists.newArrayList();
		Map<String, Object> module1 = new HashMap<>();
		Map<String, Object> module2 = new HashMap<>();
		module1.put("title", "课程的性质，目的和任务");
        module1.put("isExistMainContent", false);
        module1.put("isExistSecondaryContent", false);
        module1.put("isExistTeachingContent", false);
        module1.put("isExistTable", false);

        module2.put("title", "教学内容，基本要求及学时分配");
		module2.put("isExistMainContent", false);
		module2.put("isExistSecondaryContent", false);
		module2.put("isExistTeachingContent", false);
		module2.put("isExistTable", true);

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

		tables.add(table);
		module2.put("tables", tables);

		moduleList.add(module1);
		moduleList.add(module2);

		data.put("modules", moduleList);
		data.put("courseInfos", courseInfoList);
		data.put("name", "java课程大纲");
        data.put("templateName", "java课程大纲模板8");
        data.put("courseOutlineId", 166400);
        data.put("isCreateTemplate", true);
		
		HttpResponse httpResponse = super.api("EM00697", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		ResponseHeader header = response.getHeader();
        Assert.assertNotNull(object);
        Assert.assertTrue(String.format("返回错误码：%s",header.getErrorcode()), object.getBoolean("isSuccess"));
        Assert.assertTrue(String.format("返回错误信息：%s", header.getErrormessage()), object.getBoolean("isSuccess"));
        System.out.println(object);
	}
}
