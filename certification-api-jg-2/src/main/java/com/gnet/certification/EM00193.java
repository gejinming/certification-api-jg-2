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
import com.gnet.model.admin.CcMajorDirection;
import com.jfinal.kit.StrKit;

/**
 * 编辑专业方向
 * 
 * @author sll
 * 
 * @date 2016年06月28日 17:57:45
 *
 */
@Service("EM00193")
@Transactional(readOnly=false)
public class EM00193 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		String name = paramsStringFilter(param.get("name"));
		String remark = paramsStringFilter(param.get("remark"));
		
		if (id == null) {
			return renderFAIL("0280", response, header);
		}
		if (StrKit.isBlank(name)) {
			return renderFAIL("0282", response, header);
		}
		
		
		Date date = new Date();
		CcMajorDirection ccMajorDirection = CcMajorDirection.dao.findFilteredById(id);
		if(ccMajorDirection == null) {
			return renderFAIL("0281", response, header);
		}
		//name不能重复
		if(CcMajorDirection.dao.isExisted(name, ccMajorDirection.getLong("plan_id"), ccMajorDirection.getStr("name"))){
			return renderFAIL("0284", response, header);
		}
		
		ccMajorDirection.set("modify_date", date);
		ccMajorDirection.set("name", name);
		ccMajorDirection.set("remark", remark);
		
		Map<String, Object> result = new HashMap<>();
		result.put("isSuccess", ccMajorDirection.update());
		return renderSUC(result, response, header);
	}
	
}
