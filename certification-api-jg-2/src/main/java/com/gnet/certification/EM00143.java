package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcInstitute;
import com.gnet.model.admin.Office;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;

/**
 * 编辑学院
 * 
 * @author zsf
 * 
 * @date 2016年06月26日 18:57:47
 *
 */
@Service("EM00143")
@Transactional(readOnly=false)
public class EM00143 extends BaseApi implements IApi{

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		String name = paramsStringFilter(param.get("name"));
		String description = paramsStringFilter(param.get("description"));
		
		if (id == null) {
			return renderFAIL("0210", response, header);
		}
		if (StrKit.isBlank(name)) {
			return renderFAIL("0212", response, header);
		}
		
		Date date = new Date();
		Office office = Office.dao.findFilteredById(id);
		if(office == null) {
			return renderFAIL("0211", response, header);
		}
		
		if (CcInstitute.dao.isExisted(office.getLong("parentid"), name, office.getStr("name"))) {
			return renderFAIL("0214", response, header);
		}

		office.set("modify_date", date);
		office.set("name", name);
		office.set("description", description);
		Boolean isSuccess = office.update();
		
		if(!isSuccess) {
			Map<String, Boolean> result = new HashMap<>();
			result.put("isSuccess", Boolean.FALSE);
			return renderSUC(result, response, header);
		}
		
		CcInstitute institute = CcInstitute.dao.findFilteredById(id);
		if(institute == null) {
			return renderFAIL("0211", response, header);
		}
		institute.set("modify_date", date);
		isSuccess = institute.update();
		
		if(!isSuccess) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		Map<String, Object> result = Maps.newHashMap();
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}
	
}
