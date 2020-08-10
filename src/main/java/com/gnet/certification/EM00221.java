package com.gnet.certification;

import java.util.HashMap;
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
import com.gnet.model.admin.CcTeacher;
import com.gnet.model.admin.CcTeacherCourse;
import com.gnet.utils.DictUtils;

/**
 * 查看教师开课课程详情
 * 
 * @author SY
 * 
 * @date 2016年06月29日 14:41:41
 *
 */
@Service("EM00221")
@Transactional(readOnly=true)
public class EM00221 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		
		if (id == null) {
			return renderFAIL("0310", response, header);
		}
		
		CcTeacherCourse temp = CcTeacherCourse.dao.findFilteredById(id);
		if(temp == null) {
			return renderFAIL("0311", response, header);
		}
		String termName;
		Map<String, Object> map = new HashMap<>();
		if (DictUtils.findLabelByTypeAndKey("term", temp.getInt("term"))==null){
			 termName = temp.get("start_year") + "~" + temp.get("end_year");
		}else{;
			 termName = temp.get("start_year") + "~" + temp.get("end_year") + DictUtils.findLabelByTypeAndKey("term", temp.getInt("term"));
		}

		/** 是否允许修改开课课程年级 **/
		Boolean isAllowEditGrade = CcCourseGradecompose.dao.isAllowEditGrade(id) && CcEvalute.dao.isAllowEditGrade(id);
		map.put("id", temp.get("id"));
		map.put("createDate", temp.get("create_date"));
		map.put("modifyDate", temp.get("modify_date"));
		map.put("majorId", temp.getLong("major_id"));
		map.put("planId", temp.getLong("plan_id"));
		map.put("courseId", temp.get("course_id"));
		map.put("courseName", temp.get("course_name"));
		map.put("courseCode", temp.get("course_code"));
		map.put("teacherId", temp.get("teacher_id"));
		map.put("teacherName", temp.get("teacher_name"));
		map.put("termId", temp.get("term_id"));
		map.put("termViewName", termName);
		map.put("startYear", temp.get("start_year"));
		map.put("endYear", temp.get("end_year"));
		map.put("termTypeName", DictUtils.findLabelByTypeAndKey("termType", temp.getInt("term_type")));
		map.put("term", temp.getInt("term"));
		map.put("termName", DictUtils.findLabelByTypeAndKey("term", temp.getInt("term")));
		map.put("resultType", temp.get("result_type"));
		map.put("resultTypeName", DictUtils.findLabelByTypeAndKey("courseResultType", temp.getInt("result_type")));
		map.put("grade", temp.get("grade"));
		map.put("isSharer", temp.get("is_sharer"));
		map.put("isAllowEditGrade", isAllowEditGrade);
		Long gradecomposeEvalutePreid = temp.getLong("gradecompose_evalute_preid");
		if(gradecomposeEvalutePreid != null) {
			CcTeacherCourse preTemp = CcTeacherCourse.dao.findFilteredById(gradecomposeEvalutePreid);
			if(preTemp != null) {
				Long teacherId = preTemp.getLong("teacher_id");
				CcTeacher teacher = CcTeacher.dao.findFilteredById(teacherId);
				map.put("preTeacherName", teacher.getStr("name"));
			}
		}
		
		return renderSUC(map, response, header);
	}

}
