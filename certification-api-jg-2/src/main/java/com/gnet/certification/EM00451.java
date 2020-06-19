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
import com.gnet.model.admin.CcTeacherFurtherEducation;
import com.gnet.utils.DictUtils;

/**
 * 查看教师进修经历表详情
 * 
 * @author sll
 * 
 * @date 2016年07月21日 21:08:48
 *
 */
@Service("EM00451")
@Transactional(readOnly=true)
public class EM00451 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		
		if (id == null) {
			return renderFAIL("0620", response, header);
		}
		
		CcTeacherFurtherEducation temp = CcTeacherFurtherEducation.dao.findFilteredById(id);
		if(temp == null) {
			return renderFAIL("0621", response, header);
		}
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("id", temp.get("id"));
		map.put("createDate", temp.get("create_date"));
		map.put("modifyDate", temp.get("modify_date"));
		map.put("teacherId", temp.get("teacher_id"));
		map.put("teacherName", temp.get("teacher_name"));
		map.put("educationTypeName", DictUtils.findLabelByTypeAndKey("educationType", temp.getInt("education_type")));
		map.put("educationType", temp.get("education_type"));
		map.put("majorId", temp.get("major_id"));
		map.put("majorName", temp.get("major_name"));
		map.put("startTime", temp.get("start_time"));
		map.put("endTime", temp.get("end_time"));
		map.put("content", temp.get("content"));
		map.put("site", temp.get("site"));
		map.put("remark", temp.get("remark"));
		
		return renderSUC(map, response, header);
	}

}
