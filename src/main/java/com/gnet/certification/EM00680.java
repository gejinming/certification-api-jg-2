package com.gnet.certification;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradecompose;
import com.gnet.model.admin.CcEvalute;
import com.gnet.model.admin.CcTeacherCourse;
import com.google.common.collect.Maps;


/**
 * 切换课程的计算类型
 * 
 * @author SY
 * @Date 2016年11月24日18:12:59
 */
@Transactional
@Service("EM00680")
public class EM00680 extends BaseApi implements IApi {
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 各种其他信息
		Integer resultType = paramsIntegerFilter(params.get("resultType"));
		Long teacherCourseId = paramsLongFilter(params.get("id"));
		

		if (teacherCourseId == null) {
			return renderFAIL("0310", response, header);
		}
		if (resultType == null) {
			return renderFAIL("0315", response, header);
		}

		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findFilteredById(teacherCourseId);
		if(ccTeacherCourse == null) {
			return renderFAIL("0311", response, header);
		}
		
		Integer oldResultType = ccTeacherCourse.getInt("result_type");
		if(CcTeacherCourse.RESULT_TYPE_SCORE.equals(oldResultType)) {
			// 考核分析法， 存在开课课程和开课课程关系时候，不允许修改
			// 判断是否已经不存在开课课程成绩组成元素表
			List<CcCourseGradecompose> ccCourseGradecomposes = CcCourseGradecompose.dao.findFilteredByColumn("teacher_course_id", teacherCourseId);
			if(!ccCourseGradecomposes.isEmpty()) {
				return renderFAIL("0323", response, header);
			}
		} else if(CcTeacherCourse.RESULT_TYPE_EVALUATE.equals(oldResultType)) {
			// 评分表分析法， 存在开课课程和开课课程关系的时候，不允许修改
			// 判断是否已经不存在考评点表
			List<CcEvalute> cEvalutes = CcEvalute.dao.findFilteredByColumn("teacher_course_id", teacherCourseId);
			if(!cEvalutes.isEmpty()) {
				return renderFAIL("0324", response, header);
			}
		}
		
		ccTeacherCourse.set("modify_date", new Date());
		ccTeacherCourse.set("result_type", resultType);
		Boolean updateResult = ccTeacherCourse.update();
		
		// 结果返回
		Map<String, Object> result = Maps.newHashMap();
		// 返回操作是否成功
		result.put("isSuccess", updateResult);
		return renderSUC(result, response, header);
	}
	
}
