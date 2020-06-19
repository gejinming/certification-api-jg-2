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
import com.gnet.model.admin.CcMajorTeacher;

/**
 * 增加专业认证教师
 * 
 * @author sll
 * 
 * @date 2016年7月17日14:27:40
 *
 */
@Service("EM00296")
@Transactional(readOnly=false)
public class EM00296 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Map<String, Object> result = new HashMap<>();
	
		Long teacherId = paramsLongFilter(param.get("teacherId"));
		Long versionId = paramsLongFilter(param.get("versionId"));
		String remark = paramsStringFilter(param.get("remark"));
		
		if (teacherId == null) {
			return renderFAIL("0340", response, header);
		}
		if (versionId == null) {
			return renderFAIL("0140", response, header);
		}
		
		if(CcMajorTeacher.dao.isExistTeacher(teacherId, versionId)){
			return renderFAIL("0343", response, header);
		}
			
		Date date = new Date();
		CcMajorTeacher ccMajorTeacher = new CcMajorTeacher();
		ccMajorTeacher.set("create_date", date);
		ccMajorTeacher.set("modify_date", date);
		ccMajorTeacher.set("teacher_id", teacherId);
		ccMajorTeacher.set("version_id", versionId);
		ccMajorTeacher.set("remark", remark);
		ccMajorTeacher.set("is_del", Boolean.FALSE);
	    
		result.put("isSuccess", ccMajorTeacher.save());
 		return renderSUC(result, response, header);
	}

}
