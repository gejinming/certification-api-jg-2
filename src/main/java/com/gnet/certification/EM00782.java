package com.gnet.certification;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcTeacherCourse;
import com.gnet.service.CcTeacherCourseService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;

/**
 * 重置考评点或者成绩组成
 * 
 * @author SY
 * @Date 2017年10月19日
 */
@Service("EM00782")
@Transactional(readOnly=false)
public class EM00782 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long teacherCourseId = paramsLongFilter(params.get("teacherCourseId"));

		if (teacherCourseId == null) {
			return renderFAIL("0310", response, header);
		}

		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findFilteredById(teacherCourseId);
		if(ccTeacherCourse == null) {
			return renderFAIL("0311", response, header);
		}
		ccTeacherCourse.set("modify_date", new Date());
		ccTeacherCourse.set("gradecompose_evalute_preid", null);
		Boolean isSuccess = ccTeacherCourse.update();
		if(!isSuccess) {
			return renderFAIL("0801", response, header, "更新教师开课数据来源事变。");
		}
		// 删除当前教师开课的所有指标点和成绩组成等
		CcTeacherCourseService ccTeacherCourseService = SpringContextHolder.getBean(CcTeacherCourseService.class);
		Map<String, Object> returnMap = ccTeacherCourseService.resetGradecomposeAndEvalute(teacherCourseId);
		isSuccess = (Boolean) returnMap.get("isSuccess");
		if(!isSuccess) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return renderFAIL("0801", response, header, returnMap.get("errorMessage"));
		}
		
		// 结果返回
		Map<String, Object> result = Maps.newHashMap();
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}
	
}
