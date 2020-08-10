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
import com.gnet.model.admin.CcMajorStudent;
import com.google.common.collect.Maps;

/**
 * 返回某个学生专业方向信息
 * 
 * @author xzl
 * 
 * @date 2016年12月8日
 *
 */
@Service("EM00683")
@Transactional(readOnly=true)
public class EM00683 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		Map<String, Object> map = new HashMap<>();
		
		Long studentId = paramsLongFilter(param.get("studentId"));
		Long versionId = paramsLongFilter(param.get("versionId"));
		
		if (studentId == null) {
			return renderFAIL("0330", response, header);
		}
		if(versionId == null){
			return renderFAIL("0565", response, header);
		}
		
        Map<String, Object> paras = Maps.newHashMap();
        paras.put("student_id", studentId);
        paras.put("version_id", versionId);
		CcMajorStudent majorStudent = CcMajorStudent.dao.findFirstByColumn(paras, false);
		if(majorStudent != null){
			map.put("majorDirectionId", majorStudent.getLong("major_direction_id"));
		}
		return renderSUC(map, response, header);
	}

}
