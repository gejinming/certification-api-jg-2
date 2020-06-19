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
import com.gnet.model.admin.CcCourseGradeComposeDetail;

/**
 * 根据编号查看课程下成绩组成详细信息
 * 
 * @author sll
 *
 */
@Service("EM00548")
@Transactional(readOnly=true)
public class EM00548 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		if (id == null) {
			return renderFAIL("450", response, header);
		}
		
		CcCourseGradeComposeDetail ccCourseGradeComposeDetail = CcCourseGradeComposeDetail.dao.findDetailById(id);
		if (ccCourseGradeComposeDetail == null) {
			return renderFAIL("0451", response, header);
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("id", ccCourseGradeComposeDetail.get("id"));
		map.put("courseId", ccCourseGradeComposeDetail.get("course_id"));
		map.put("courseName", ccCourseGradeComposeDetail.get("course_name"));
		map.put("gradeComposeId", ccCourseGradeComposeDetail.get("grade_compose_id"));
		map.put("gradeComposeName", ccCourseGradeComposeDetail.get("grade_compose_name"));
		map.put("teacherId", ccCourseGradeComposeDetail.get("teacher_id"));
		map.put("teacherName", ccCourseGradeComposeDetail.get("teacher_name"));
		map.put("termId", ccCourseGradeComposeDetail.get("term_id"));
		map.put("term", ccCourseGradeComposeDetail.get("term"));
		map.put("start_year", ccCourseGradeComposeDetail.get("start_year"));
		map.put("end_year", ccCourseGradeComposeDetail.get("end_year"));
		
		return renderSUC(map, response, header);
	}

}




