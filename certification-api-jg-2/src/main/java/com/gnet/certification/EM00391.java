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
import com.gnet.model.admin.CcEvaluteLevel;

/**
 * 查看考评点得分层次关系表详情
 * 
 * @author sll
 * 
 * @date 2016年07月05日 18:29:45
 *
 */
@Service("EM00391")
@Transactional(readOnly=true)
public class EM00391 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		
		if (id == null) {
			return renderFAIL("0430", response, header);
		}
		
		CcEvaluteLevel temp = CcEvaluteLevel.dao.findById(id);
		if(temp == null) {
			return renderFAIL("0431", response, header);
		}
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("id", temp.get("id"));
		map.put("createDate", temp.get("create_date"));
		map.put("modifyDate", temp.get("modify_date"));
		map.put("levelName", temp.get("level_name"));
		map.put("score", temp.get("score"));
		map.put("requirement", temp.get("requirement"));
		map.put("teacherCourseId", temp.get("teacher_course_id"));
		map.put("teacherName", temp.get("teacher_name"));
		map.put("courseName", temp.get("course_name"));
		map.put("indicationId", temp.get("indication_id"));
		map.put("indicationContent", temp.get("indication_content"));
		
		return renderSUC(map, response, header);
	}
	
}
