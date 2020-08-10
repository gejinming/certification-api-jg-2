package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
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
import com.gnet.service.CcCourseService;
import com.gnet.service.CcMajorTeacherService;
import com.gnet.utils.SpringContextHolder;

/**
 * 增加教师开课课程
 * 
 * @author SY
 * 
 * @date 2016年06月29日 14:41:41
 *
 */
@Service("EM00222")
@Transactional(readOnly=false)
public class EM00222 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Long courseId = paramsLongFilter(param.get("courseId"));
		Long teacherId = paramsLongFilter(param.get("teacherId"));
		Long termId = paramsLongFilter(param.get("termId"));
		Integer resultType = paramsIntegerFilter(param.get("resultType"));
		Integer grade = paramsIntegerFilter(param.get("grade"));
		Boolean isSharer = paramsBooleanFilter(param.get("isSharer"));
		if (courseId == null) {
			return renderFAIL("0312", response, header);
		}
		if (teacherId == null) {
			return renderFAIL("0313", response, header);
		}
		if (resultType == null) {
			return renderFAIL("0315", response, header);
		}
		if (grade == null) {
			return renderFAIL("0316", response, header);
		}
		Date date = new Date();
		
		CcCourseService ccCourseService = SpringContextHolder.getBean(CcCourseService.class);
		if(!ccCourseService.validateGrade(grade, courseId)) {
			return renderFAIL("0321", response, header);
		}
		
		if(CcTeacherCourse.dao.isExisted(courseId, teacherId, termId, grade, null)){
			return renderFAIL("0322", response, header);
		}

		Map<String, Object> result = new HashMap<>();
		
		if(isSharer != null && isSharer) {
			// 找到之前的分享人，把他变成非分享人
			CcTeacherCourse sharedTeacherCourse = CcTeacherCourse.dao.findSharer(courseId, termId, grade);
			if(sharedTeacherCourse != null) {
				sharedTeacherCourse.set("modify_date", date);
				sharedTeacherCourse.set("is_sharer", null);
				sharedTeacherCourse.set("is_shared", null);
				if(!sharedTeacherCourse.update()) {
					result.put("isSuccess", Boolean.FALSE);
					return renderSUC(result, response, header);
				}
			}
		}
		
		
		CcTeacherCourse ccTeacherCourse = new CcTeacherCourse();
		
		ccTeacherCourse.set("create_date", date);
		ccTeacherCourse.set("modify_date", date);
		ccTeacherCourse.set("course_id", courseId);
		ccTeacherCourse.set("teacher_id", teacherId);
		ccTeacherCourse.set("term_id", termId);
		ccTeacherCourse.set("result_type", resultType);
		ccTeacherCourse.set("grade", grade);
		ccTeacherCourse.set("is_sharer", isSharer == null ? Boolean.FALSE : isSharer);
		ccTeacherCourse.set("is_del", Boolean.FALSE);
		Boolean isSuccess = ccTeacherCourse.save();
		if (!isSuccess) {
			result.put("isSuccess", isSuccess);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return renderSUC(result, response, header);
		}
		
		// 检测某个课程所在专业是否已经关联了当前教师，如果不存在，则增加关联。
		CcMajorTeacherService ccMajorTeacherService = SpringContextHolder.getBean(CcMajorTeacherService.class);
		isSuccess = ccMajorTeacherService.addMajorTeacher(teacherId, date, courseId);
		if (!isSuccess) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		result.put("isSuccess", isSuccess);
		result.put("id", ccTeacherCourse.getLong("id"));
		
		return renderSUC(result, response, header);
	}
}
