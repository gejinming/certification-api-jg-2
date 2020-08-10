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
 * 实践课程批量保存
 * 
 * @author SY
 * @date 2018年1月12日
 */
@Transactional(readOnly = false)
@Service("EM00908")
public class EM00908 extends BaseApi implements IApi{
	
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
			object.set("directionName", record.get("directionName"));
			object.set("moduleName", record.get("moduleName"));
			object.set("code", record.get("code"));
			object.set("name", record.get("name"));
			object.set("englishName", record.get("englishName"));
			object.set("credit", record.get("credit"));
			object.set("allHours", record.get("allHours"));
			object.set("weekHour", record.get("weekHour"));
			object.set("planTermClassNames", record.get("planTermClassNames"));
			object.set("planId", planId);
			courseList.add(object);
		}
		
		Map<String, Object> result = Maps.newHashMap();
		CcCourseService ccCourseService = SpringContextHolder.getBean(CcCourseService.class);		
		if(!ccCourseService.validateImportPracticeist(courseList, planId)) {
			List<Map<String, Object>> errorList = Lists.newArrayList();
			// 返回结果
			for (Record record : courseList) {
				Map<String, Object> object = Maps.newHashMap();
				
				object.put("directionName", record.getStr("directionName"));
				object.put("moduleName", record.getStr("moduleName"));
				object.put("code", record.getStr("code"));
				object.put("name", record.getStr("name"));
				object.put("englishName", record.getStr("englishName"));
				object.put("credit", record.getStr("credit"));
				object.put("allHours", record.getStr("allHours"));
				object.put("weekHour", record.getStr("weekHour"));
				object.put("planTermClassNames", record.getStr("planTermClassNames"));
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
			boolean isSuccess = ccCourseService.savePracticeCourses(courseList);
			result.put("isSuccess", isSuccess);
		}
		
		
		return renderSUC(result, response, header);
	}
	
}
