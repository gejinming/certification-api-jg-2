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
import com.gnet.model.admin.CcEvalute;

/**
 * 查看教师课程考评点详情
 * 
 * @author sll
 * 
 * @date 2016年07月04日 15:58:01
 *
 */
@Service("EM00361")
@Transactional(readOnly=true)
public class EM00361 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		
		if (id == null) {
			return renderFAIL("0370", response, header);
		}
		
		CcEvalute temp = CcEvalute.dao.findById(id);
		if(temp == null) {
			return renderFAIL("0371", response, header);
		}
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("id", temp.get("id"));
		map.put("createDate", temp.get("create_date"));
		map.put("modifyDate", temp.get("modify_date"));
		map.put("indicationId", temp.get("indication_id"));
		map.put("teacherCourseId", temp.get("teacher_course_id"));
		map.put("teacherName", temp.get("teacher_name"));
		map.put("courseName", temp.get("course_name"));
		map.put("content", temp.get("content"));
		map.put("weight", temp.get("weight"));
		map.put("evaluteTypeId", temp.get("evalute_type_id"));
		map.put("percentage", temp.get("percentage"));
		map.put("remark", temp.get("remark"));
		
		return renderSUC(map, response, header);
	}

}
