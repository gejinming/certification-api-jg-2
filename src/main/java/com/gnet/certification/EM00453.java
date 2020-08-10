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
import com.gnet.model.admin.CcTeacherFurtherEducation;

/**
 * 编辑教师进修经历表
 * 
 * @author sll
 * 
 * @date 2016年07月21日 21:08:48
 *
 */
@Service("EM00453")
@Transactional(readOnly=false)
public class EM00453 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		Long teacherId = paramsLongFilter(param.get("teacherId"));
		Integer educationType = paramsIntegerFilter(param.get("educationType"));
		String startTime = paramsStringFilter(param.get("startTime"));
		String endTime = paramsStringFilter(param.get("endTime"));
		
		String content = paramsStringFilter(param.get("content"));
		String site = paramsStringFilter(param.get("site"));
		String remark = paramsStringFilter(param.get("remark"));
		
		if (id == null) {
			return renderFAIL("0620", response, header);
		}
		if (teacherId == null) {
			return renderFAIL("0622", response, header);
		}
		if (educationType == null) {
			return renderFAIL("0623", response, header);
		}
		if (startTime == null) {
			return renderFAIL("0624", response, header);
		}
		
		Date date = new Date();
		CcTeacherFurtherEducation ccTeacherFurtherEducation = CcTeacherFurtherEducation.dao.findFilteredById(id);
		if(ccTeacherFurtherEducation == null) {
			return renderFAIL("0621", response, header);
		}
		ccTeacherFurtherEducation.set("modify_date", date);
		ccTeacherFurtherEducation.set("teacher_id", teacherId);
		ccTeacherFurtherEducation.set("education_type", educationType);
		ccTeacherFurtherEducation.set("start_time", startTime);
		ccTeacherFurtherEducation.set("end_time", endTime);
		ccTeacherFurtherEducation.set("content", content);
		ccTeacherFurtherEducation.set("site", site);
		ccTeacherFurtherEducation.set("remark", remark);
		
		Map<String, Boolean> result = new HashMap<>();
		result.put("isSuccess", ccTeacherFurtherEducation.update());
		return renderSUC(result, response, header);
	}
	
}
