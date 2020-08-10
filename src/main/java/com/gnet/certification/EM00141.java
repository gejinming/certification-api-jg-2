package com.gnet.certification;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcInstitute;

/**
 * 查看学院详情
 * 
 * @author zsf
 * 
 * @date 2016年06月26日 18:57:47
 *
 */
@Service("EM00141")
@Transactional(readOnly=true)
public class EM00141 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		
		if (id == null) {
			return renderFAIL("0210", response, header);
		}
		
		CcInstitute temp = CcInstitute.dao.findById(id);
		
		CcInstitute ccInstitute = new CcInstitute();
		if(temp == null) {
			return renderFAIL("0211", response, header);
		}
		
		ccInstitute.put("id", temp.get("id"));
		ccInstitute.put("createDate", temp.get("create_date"));
		ccInstitute.put("modifyDate", temp.get("modify_date"));
		ccInstitute.put("parentid", temp.get("parentid"));
		ccInstitute.put("name", temp.get("name"));
		ccInstitute.put("type", temp.get("type"));
		ccInstitute.put("isSystem", temp.get("is_system"));
		ccInstitute.put("description", temp.get("description"));
		
		return renderSUC(ccInstitute, response, header);
	}

}
