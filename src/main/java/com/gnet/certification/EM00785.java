package com.gnet.certification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
 * 强制分享考评点或者成绩组成
 * 
 * @author SY
 * @Date 2017年11月2日
 */
@Service("EM00785")
@Transactional(readOnly=false)
public class EM00785 extends BaseApi implements IApi {

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
		ccTeacherCourse.set("is_shared", Boolean.TRUE);
		Boolean isSuccess = ccTeacherCourse.update();
		// 结果返回
		Map<String, Object> result = Maps.newHashMap();
		result.put("isSuccess", isSuccess);
		if(!isSuccess) {
			return renderFAIL("403", response, header, "教师开课更新失败。");
		}
		
		// 把整个教师可以分享的教师的成绩组成进行更新
		CcTeacherCourseService ccTeacherCourseService = SpringContextHolder.getBean(CcTeacherCourseService.class);
		List<CcTeacherCourse> list = ccTeacherCourseService.findSharedTeacherCourse(ccTeacherCourse);
		// 循环，按照单个的一次次处理 以后可以更新 TODO SY
		Long sharerTeacherCourseId = teacherCourseId;
		for(CcTeacherCourse temp : list) {
			Long sharedTeacherCourseId = temp.getLong("id");
			
			// 更新这些教师开课的来源
			temp.set("modify_date", new Date());
			temp.set("gradecompose_evalute_preid", sharerTeacherCourseId);
			
			// 重置这些课程之前可能存在的数据
			Map<String, Object> returnMap = ccTeacherCourseService.forceResetGradecomposeAndEvalute(sharedTeacherCourseId);
			isSuccess = (Boolean) returnMap.get("isSuccess");
			if(!isSuccess) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return renderFAIL("0801", response, header, returnMap.get("errorMessage"));
			}
			
			// 需要根据分享人的数据建立被分享人的数据
			returnMap = ccTeacherCourseService.copyComposeGradeAndEvalute(sharedTeacherCourseId, sharerTeacherCourseId);
			isSuccess = (Boolean) returnMap.get("isSuccess");
			if(!isSuccess) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return renderFAIL("0801", response, header, returnMap.get("errorMessage"));
			}
		}
		
		// 更新教师开课的来源
		isSuccess = CcTeacherCourse.dao.batchUpdate(list, "modify_date,gradecompose_evalute_preid");
		if(!isSuccess) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return renderFAIL("0801", response, header, "教师开课信息批量更新失败。");
		}
		
		return renderSUC(result, response, header);
	}
	
}
