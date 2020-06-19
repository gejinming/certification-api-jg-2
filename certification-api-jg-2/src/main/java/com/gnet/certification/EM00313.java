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
import com.gnet.model.admin.CcEduclass;
import com.jfinal.kit.StrKit;

/**
 * 编辑教学班
 * 
 * @author SY
 * 
 * @date 2016年07月01日 14:41:11
 *
 */
@Service("EM00313")
@Transactional(readOnly=false)
public class EM00313 extends BaseApi implements IApi{

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		String educlassName = paramsStringFilter(param.get("educlassName"));
		Long teacherCourseId = paramsLongFilter(param.get("teacherCourseId"));
		
		if (id == null) {
			return renderFAIL("0380", response, header);
		}
		if (StrKit.isBlank(educlassName)) {
			return renderFAIL("0382", response, header);
		}
		if (teacherCourseId == null) {
			return renderFAIL("0383", response, header);
		}

		if(CcEduclass.dao.isExisted(id, teacherCourseId, educlassName)){
			return renderFAIL("0387", response, header);
		}
		
		Date date = new Date();
		CcEduclass ccEduclass = CcEduclass.dao.findEduclassById(id);
		if(ccEduclass == null) {
			return renderFAIL("0381", response, header);
		}
		ccEduclass.set("modify_date", date);
		ccEduclass.set("educlass_name", educlassName);
		ccEduclass.set("teacher_course_id", teacherCourseId);
		
		Map<String, Boolean> result = new HashMap<>();
		result.put("isSuccess", ccEduclass.update());
		return renderSUC(result, response, header);
	}
	
}
