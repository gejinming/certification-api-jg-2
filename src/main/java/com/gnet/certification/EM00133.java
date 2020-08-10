package com.gnet.certification;

import java.util.Date;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import com.alibaba.druid.util.StringUtils;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.Office;
import com.gnet.model.admin.School;
import com.google.common.collect.Maps;

/**
 * 编辑School
 * 
 * @author zsf
 * 
 * @date 2016年06月25日 18:39:35
 *
 */
@Service("EM00133")
@Transactional(readOnly=false)
public class EM00133 extends BaseApi implements IApi{

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		String name = paramsStringFilter(param.get("name"));
		String description = paramsStringFilter(param.get("description"));
		
		if (id == null) {
			return renderFAIL("0200", response, header);
		}
		
		if (StringUtils.isEmpty(name)) {
			return renderFAIL("0202", response, header);
		}

		Date date = new Date();
		School school = School.dao.findFilteredById(id);
		Office office = Office.dao.findFilteredById(id);
		if(school == null || office == null) {
			return renderFAIL("0201", response, header);
		}
		
		if (School.dao.isExisted(name, office.getStr("name"))) {
			return renderFAIL("0204", response, header);
		}
		

		school.set("modify_date", date);
		Boolean isSuccess = school.update();
		office.set("name", name);
		office.set("modify_date", date);
		office.set("description", description);
		isSuccess = office.update();
		
		if(!isSuccess) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		Map<String, Object> result = Maps.newHashMap();
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}
	
}
