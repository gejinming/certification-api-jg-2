package com.gnet.certification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.service.CcCourseService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Record;

/**
 * 理论课程批量保存
 * 
 * @author SY
 * @date 2018年1月12日
 */
@Transactional(readOnly = false)
@Service("EM00905")
public class EM00905 extends BaseApi implements IApi{
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> params = request.getData();
		
		List<HashMap> coursesMap = paramsJSONArrayFilter(params.get("courses"), HashMap.class);
		Long planId = paramsLongFilter(params.get("planId"));
		
		// 培养计划版本编号
		if (planId == null) {
			return renderFAIL("0421", response, header);
		}
		
		if (coursesMap.isEmpty()) {
			return renderFAIL("0273", response, header);
		}
		
		List<Record> courseList = Lists.newArrayList();
		for (Map<String, String> record : coursesMap) {
			Record object = new Record();
			object.set("hierarchyName", record.get("hierarchyName"));
			object.set("seconderyHierarchyName",record.get("seconderyHierarchyName"));
			object.set("propertyName", record.get("propertyName"));
			object.set("propertySecondaryName", record.get("propertySecondaryName"));
			object.set("code", record.get("code"));
			object.set("name", record.get("name"));
			object.set("englishName", record.get("englishName"));
			object.set("credit", record.get("credit"));
			object.set("allHours", record.get("allHours"));
			object.set("theoryHours", record.get("theoryHours"));
			object.set("experimentHours", record.get("experimentHours"));
			object.set("practiceHours", record.get("practiceHours"));
			object.set("operateComputerHours", record.get("operateComputerHours"));
			object.set("exercisesHours", record.get("exercisesHours"));
			object.set("dicussHours", record.get("dicussHours"));
			object.set("extracurricularHours", record.get("extracurricularHours"));
			object.set("weekHour", record.get("weekHour"));
			object.set("planTermClassNames", record.get("planTermClassNames"));
			object.set("planTermExamNames", record.get("planTermExamNames"));
			object.set("planId", planId);
			courseList.add(object);
		}
		
		Map<String, Object> result = Maps.newHashMap();
		CcCourseService ccCourseService = SpringContextHolder.getBean(CcCourseService.class);
		boolean b = ccCourseService.validateImportTheoryList(courseList, planId);
		if(!b) {
			List<Map<String, Object>> errorList = Lists.newArrayList();
			// 返回结果
			for (Record record : courseList) {
				Map<String, Object> object = Maps.newHashMap();

				object.put("hierarchyName", record.getStr("hierarchyName"));
				object.put("propertyName", record.getStr("propertyName"));
				object.put("seconderyHierarchyName",record.getStr("seconderyHierarchyName"));
				object.put("propertySecondaryName",record.getStr("propertySecondaryName"));
				object.put("code", record.getStr("code"));
				object.put("name", record.getStr("name"));
				object.put("englishName", record.getStr("englishName"));
				object.put("credit", record.getStr("credit"));
				object.put("allHours", record.getStr("allHours"));
				object.put("theoryHours", record.getStr("theoryHours"));
				object.put("experimentHours", record.getStr("experimentHours"));
				object.put("practiceHours", record.getStr("practiceHours"));
				object.put("operateComputerHours", record.getStr("operateComputerHours"));
				object.put("exercisesHours", record.getStr("exercisesHours"));
				object.put("dicussHours", record.getStr("dicussHours"));
				object.put("extracurricularHours", record.getStr("extracurricularHours"));
				object.put("weekHour", record.getStr("weekHour"));
				object.put("planTermClassNames", record.getStr("planTermClassNames"));
				object.put("planTermExamNames", record.getStr("planTermExamNames"));
				object.put("planId", planId);
				
				// 错误参数
				object.put("reasons", record.get("reasons"));
				object.put("isError", record.getBoolean("isError"));
				errorList.add(object);
			}
			result.put("isSuccess", Boolean.FALSE);
			result.put("failRecords", errorList);
		} else {
			// 返回结果
			boolean isSuccess = ccCourseService.saveTheoryCourses(courseList);
			result.put("isSuccess", isSuccess);
		}
		
		
		return renderSUC(result, response, header);
	}
	
}
