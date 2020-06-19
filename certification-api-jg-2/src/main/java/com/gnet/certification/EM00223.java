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
import com.gnet.model.admin.CcCourseGradecompose;
import com.gnet.model.admin.CcEvalute;
import com.gnet.model.admin.CcTeacherCourse;
import com.gnet.model.admin.CcVersion;
import com.gnet.service.CcCourseService;
import com.gnet.service.CcMajorTeacherService;
import com.gnet.utils.SpringContextHolder;

/**
 * 编辑教师开课课程
 * 
 * @author SY
 * 
 * @date 2016年06月29日 14:41:41
 *
 */
@Service("EM00223")
@Transactional(readOnly=false)
public class EM00223 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		Long courseId = paramsLongFilter(param.get("courseId"));
		Long teacherId = paramsLongFilter(param.get("teacherId"));
		Long termId = paramsLongFilter(param.get("termId"));
		Integer resultType = paramsIntegerFilter(param.get("resultType"));
		Integer grade = paramsIntegerFilter(param.get("grade"));
		Long majorId = paramsLongFilter(param.get("majorId"));
		Boolean isSharer = paramsBooleanFilter(param.get("isSharer"));
		
		if (id == null) {
			return renderFAIL("0310", response, header);
		}
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
		if(majorId == null){
			return renderFAIL("0296", response, header);
		}
		
		//通过专业和年级获取版本编号
		Long planId = CcVersion.dao.findNewestVersion(majorId, grade);
		if(planId == null){
			return renderFAIL("0840", response, header);
		}
		
		CcCourseService ccCourseService = SpringContextHolder.getBean(CcCourseService.class);
		if(!ccCourseService.validateGrade(grade, courseId)) {
			return renderFAIL("0321", response, header);
		}
		
		if(CcTeacherCourse.dao.isExisted(courseId, teacherId, termId, grade, id)){
			return renderFAIL("0322", response, header);
		}
		
		Date date = new Date();
		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findFilteredById(id);
		if(ccTeacherCourse == null) {
			return renderFAIL("0311", response, header);
		}
		
		//年级发生变化的时候才进行验证
		if(!grade.equals(ccTeacherCourse.getInt("grade"))){
			if(CcTeacherCourse.RESULT_TYPE_SCORE.equals(ccTeacherCourse.getInt("result_type"))){
				if(!CcCourseGradecompose.dao.isAllowEditGrade(id)){
					return renderFAIL("0326", response, header);
				}
			}else{
				if(!CcEvalute.dao.isAllowEditGrade(id)){
					return renderFAIL("0325", response, header);
				}
			}
		}
		
		Map<String, Object> result = new HashMap<>();
		if(isSharer != null && isSharer) {
			// 找到之前的分享人，把他变成非分享人
			CcTeacherCourse sharedTeacherCourse = CcTeacherCourse.dao.findSharer(courseId, termId, grade);
			if(sharedTeacherCourse != null) {
				Long sharedId = sharedTeacherCourse.getLong("id");
				if(id != sharedId) {
					sharedTeacherCourse.set("modify_date", date);
					sharedTeacherCourse.set("is_sharer", null);
					sharedTeacherCourse.set("is_shared", null);
					if(!sharedTeacherCourse.update()) {
						result.put("isSuccess", Boolean.FALSE);
						return renderSUC(result, response, header);
					}
				}
			}
		}
		
		ccTeacherCourse.set("modify_date", date);
		ccTeacherCourse.set("course_id", courseId);
		ccTeacherCourse.set("teacher_id", teacherId);
		ccTeacherCourse.set("term_id", termId);
		ccTeacherCourse.set("result_type", resultType);
		ccTeacherCourse.set("grade", grade);
		ccTeacherCourse.set("is_sharer", isSharer == null ? Boolean.FALSE : isSharer);
		Boolean isSuccess = ccTeacherCourse.update();
		
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
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
			
		}
		
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}
	
}
