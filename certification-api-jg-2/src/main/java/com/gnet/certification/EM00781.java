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
 * 接收考评点或者成绩组成的分享
 * 
 * @author SY
 * @Date 2017年10月19日
 */
@Service("EM00781")
@Transactional(readOnly=false)
public class EM00781 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long teacherCourseId = paramsLongFilter(params.get("teacherCourseId"));

		if (teacherCourseId == null) {
			return renderFAIL("0310", response, header);
		}

		// 被分享人的信息
		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findFilteredById(teacherCourseId);
		if(ccTeacherCourse == null) {
			return renderFAIL("0311", response, header);
		}
		
		Long courseId = ccTeacherCourse.getLong("course_id");
		Long termId = ccTeacherCourse.getLong("term_id");
		Integer grade = ccTeacherCourse.getInt("grade");
		CcTeacherCourseService ccTeacherCourseService = SpringContextHolder.getBean(CcTeacherCourseService.class);
		// 获取分享人的信息
		CcTeacherCourse ccTeacherCoursePre = ccTeacherCourseService.findSharer(courseId, termId, grade);
		// 当前不存在分享人
		if(ccTeacherCoursePre == null) {
			return renderFAIL("0327", response, header);
		}
		Long sharerTeacherCourseId = ccTeacherCoursePre.getLong("id");
		
		// 更新接收人信息
		ccTeacherCourse.set("modify_date", new Date());
		ccTeacherCourse.set("gradecompose_evalute_preid", sharerTeacherCourseId);
		Boolean isSuccess = ccTeacherCourse.update();
		
		// 重置之前可能存在的数据
		Map<String, Object> returnMap = ccTeacherCourseService.resetGradecomposeAndEvalute(teacherCourseId);
		isSuccess = (Boolean) returnMap.get("isSuccess");
		if(!isSuccess) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return renderFAIL("0801", response, header, returnMap.get("errorMessage"));
		}
		
		// 需要根据分享人的数据建立被分享人的数据 
		returnMap = ccTeacherCourseService.copyComposeGradeAndEvalute(teacherCourseId, sharerTeacherCourseId);
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
