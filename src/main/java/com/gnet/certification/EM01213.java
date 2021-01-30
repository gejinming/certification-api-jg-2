package com.gnet.certification;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.service.CcCourseGradecompBatchService;
import com.gnet.utils.SpringContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.*;

/**
 * 增加开启课程毕业设计功能
 * 
 * @author GJM
 * @Date 2020年08月26日
 */
@Service("EM01213")
public class EM01213 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		HashMap<Object, Object> result = new HashMap<>();
		//课程id
		Long courseId = paramsLongFilter(param.get("courseId"));
		Long classId = paramsLongFilter(param.get("classId"));
		//届别
		Integer periodDate = paramsIntegerFilter(param.get("periodDate"));
		//开启教师id
		Long teacherId = paramsLongFilter(param.get("teacherId"));
		//1毕业设计、2工程实习
		Long courseType = paramsLongFilter(param.get("courseType"));
		if (courseId==null ||classId == null){
			return renderFAIL("0250", response, header);
		}
		if (periodDate==null){
			return renderFAIL("0250", response, header);
		}
		if (teacherId==null){
			return renderFAIL("0250", response, header);
		}
		if (courseType==null){
			return renderFAIL("0250", response, header);
		}
		//学校id
		CcTeacher teacher = CcTeacher.dao.findAllById(teacherId);

		Object schoolId = teacher.get("schoolId");
		Long majorId = teacher.getLong("major_id");
		Date date = new Date();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		CcCoursePeriode coursePeriode = CcCoursePeriode.dao.findCoursePeriode(courseId,classId);
		CcCoursePeriode ccCoursePeriode = new CcCoursePeriode();
		ccCoursePeriode.set("period_date",periodDate);
		ccCoursePeriode.set("course_id",courseId);
		ccCoursePeriode.set("teacher_id",teacherId);
		ccCoursePeriode.set("create_date",date);
		ccCoursePeriode.set("modify_date",date);
		ccCoursePeriode.set("is_del",Boolean.FALSE);
		ccCoursePeriode.set("school_id",schoolId);
		ccCoursePeriode.set("major_id",majorId);
		ccCoursePeriode.set("class_id",classId);
		ccCoursePeriode.set("course_type",courseType);
		if (coursePeriode == null){
			ccCoursePeriode.set("id",idGenerate.getNextValue());
			boolean isSuccess = ccCoursePeriode.save();
			result.put("isSuccess", isSuccess);
		}else {
			ccCoursePeriode.set("id",coursePeriode.getLong("id"));
			boolean isSuccess = ccCoursePeriode.update();
			result.put("isSuccess", isSuccess);

		}

		return renderSUC(result, response, header);
	}


}
