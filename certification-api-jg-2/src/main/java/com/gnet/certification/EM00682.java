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
import com.gnet.model.admin.CcMajorStudent;
import com.google.common.collect.Maps;

/**
 * 保存或更新学生专业方向
 * 
 * @author xzl
 * 
 * @date 2016年12月8日
 *
 */
@Service("EM00682")
@Transactional(readOnly=false)
public class EM00682 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		Map<String, Object> map = new HashMap<>();
		
		Long studentId = paramsLongFilter(param.get("studentId"));
		Long versionId = paramsLongFilter(param.get("versionId"));
		Long majorDirectionId = paramsLongFilter(param.get("majorDirectionId"));
		
		if (studentId == null) {
			return renderFAIL("0330", response, header);
		}
		if(versionId == null){
			return renderFAIL("0565", response, header);
		}
		if(majorDirectionId == null){
			return renderFAIL("0280", response, header);
		}
		
		Date date = new Date();
		Boolean isSuccess = Boolean.TRUE;
        Map<String, Object> paras = Maps.newHashMap();
        paras.put("student_id", studentId);
        paras.put("version_id", versionId);
		CcMajorStudent majorStudent = CcMajorStudent.dao.findFirstByColumn(paras, false);
		if(majorStudent != null && !majorDirectionId.equals(majorStudent.getLong("major_direction_id"))){
			majorStudent.set("modify_date", date);
			majorStudent.set("major_direction_id", majorDirectionId);
			isSuccess = majorStudent.update();
		}else{
			majorStudent = new CcMajorStudent();
			majorStudent.set("create_date", date);
			majorStudent.set("modify_date", date);
			majorStudent.set("student_id", studentId);
			majorStudent.set("version_id", versionId);
			majorStudent.set("major_direction_id", majorDirectionId);
			isSuccess = majorStudent.save();
		}
		map.put("isSuccess", isSuccess);
		return renderSUC(map, response, header);
	}

}
