package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradecomposeDetailIndication;

/**
 * 增加成绩组成元素明细指标点关联
 * 
 * @author sll
 *
 */
@Service("EM00512")
@Transactional(readOnly=false)
public class EM00512 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		String courseGradecomposeDetailId = paramsStringFilter(param.get("courseGradecomposeDetailId"));
		Long indicationId = paramsLongFilter(param.get("indicationId"));
		if (courseGradecomposeDetailId == null) {
			return renderFAIL("0450", response, header);
		}
		if (indicationId == null) {
			return renderFAIL("0230", response, header);
		}
		Date date = new Date();
		
		CcCourseGradecomposeDetailIndication ccCourseGradecomposeDetailIndication = new CcCourseGradecomposeDetailIndication();
		
		ccCourseGradecomposeDetailIndication.set("create_date", date);
		ccCourseGradecomposeDetailIndication.set("modify_date", date);
		ccCourseGradecomposeDetailIndication.set("course_gradecompose_detail_id", courseGradecomposeDetailId);
		ccCourseGradecomposeDetailIndication.set("indication_id", indicationId);

		Map<String, Object> result = new HashMap<>();
		result.put("isSuccess", ccCourseGradecomposeDetailIndication.save());
		result.put("id", ccCourseGradecomposeDetailIndication.getLong("id"));
		
		return renderSUC(result, response, header);
	}
	
}
