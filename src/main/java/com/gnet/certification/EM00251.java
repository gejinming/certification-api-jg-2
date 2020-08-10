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
import com.gnet.model.admin.CcClass;
import com.gnet.model.admin.CcStudent;

/**
 * 查看行政班详情
 * 
 * @author sll
 * 
 * @date 2016年06月29日 18:38:27
 *
 */
@Service("EM00251")
@Transactional(readOnly=true)
public class EM00251 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		
		if (id == null) {
			return renderFAIL("0294", response, header);
		}
		
		CcClass temp = CcClass.dao.findById(id);
		if(temp == null) {
			return renderFAIL("0295", response, header);
		}
			
		Map<String, Object> map = new HashMap<>();
		map.put("id", temp.get("id"));
		map.put("createDate", temp.get("create_date"));
		map.put("modifyDate", temp.get("modify_date"));
		map.put("name", temp.get("name"));
		map.put("type", temp.get("type"));
		map.put("isSystem", temp.get("is_system"));
		map.put("grade", temp.get("grade"));
		map.put("classLeader", temp.get("class_leader"));
		map.put("remark", temp.get("remark"));
		map.put("majorId", temp.get("parentid"));
		map.put("majorName", temp.get("majorName"));
		map.put("instituteId", temp.get("instituteId"));
		map.put("instituteName", temp.get("instituteName"));
		map.put("description", temp.get("description"));
		map.put("isEditableGrade", CcStudent.dao.countFiltered("class_id", id) > 0 ? false : true);
		
		return renderSUC(map, response, header);
	}

}
