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
import com.gnet.model.admin.CcEduclass;

/**
 * 查看教学班详情
 * 
 * @author SY
 * 
 * @date 2016年07月01日 14:41:11
 *
 */
@Service("EM00311")
@Transactional(readOnly=true)
public class EM00311 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		
		if (id == null) {
			return renderFAIL("0380", response, header);
		}
		
		CcEduclass temp = CcEduclass.dao.findEduclassById(id);
		if(temp == null) {
			return renderFAIL("0381", response, header);
		}
		
		Map<String, Object> map = new HashMap<>();
		

		map.put("id", temp.getLong("id"));
		map.put("createDate", temp.getDate("create_date"));
		map.put("modifyDate", temp.getDate("modify_date"));
		map.put("educlassName", temp.getStr("educlass_name"));
		map.put("teacherCourseId", temp.getLong("teacher_course_id"));
		map.put("teacherName", temp.getStr("teacherName"));
		map.put("studentNum", temp.getLong("studentNum"));
		map.put("courseId", temp.getLong("courseId"));
		map.put("courseName", temp.getStr("courseName"));
		map.put("courseCode", temp.getStr("courseCode"));
		map.put("resultType", temp.getInt("result_type"));
		map.put("versionId", temp.getLong("versionId"));
		map.put("majorId", temp.getLong("majorId"));
		map.put("grade", temp.getInt("grade"));

		
		return renderSUC(map, response, header);
	}

}
