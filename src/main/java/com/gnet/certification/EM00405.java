package com.gnet.certification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcEvaluteType;

/**
 * 某课程考评点类别列表接口
 * 
 * @author SY
 * 
 * @date 2017年8月14日
 * 
 */
@Service("EM00405")
@Transactional(readOnly=true)
public class EM00405 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Long teacherCourseId = paramsLongFilter(param.get("teacherCourseId"));
		if(teacherCourseId == null) {
			return renderFAIL("0250", response, header);
		}
		
		List<CcEvaluteType> ccEvaluteTypes = CcEvaluteType.dao.findFilteredByColumn("teacher_course_id", teacherCourseId);
		Map<String, Object> map = new HashMap<>();
		map.put("list", ccEvaluteTypes);
		return renderSUC(map, response, header);
	}
}
