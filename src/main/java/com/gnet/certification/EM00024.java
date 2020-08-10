package com.gnet.certification;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.Office;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;

/**
 * 修改部门的接口
 * 
 * @author wct
 * @Date 2016年6月22日
 */
@Service("EM00024")
@Transactional(readOnly=false)
public class EM00024 extends BaseApi implements IApi{

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long id = paramsLongFilter(params.get("id"));
		String code = paramsStringFilter(params.get("code"));
		String name = paramsStringFilter(params.get("name"));
		String description = paramsStringFilter(params.get("description"));
		Boolean isSystem = paramsBooleanFilter(params.get("isSystem"));
		String originValue = paramsStringFilter(params.get("originValue"));
		// 部门编号不能为空过滤
		if (id == null) {
			return renderFAIL("0056", response, header);
		}
		
		Date date = new Date();
		Office office = new Office();
		office.set("modify_date", date);
		office.set("id", id);
		if (StrKit.notBlank(code)) {
			if (codeIsExisted(code, originValue)) {
				return renderFAIL("0055", response, header);
			}
			office.set("code", code);
		}
		
		if (StrKit.notBlank(name)) {
			office.set("name", name);
		}
		
		if (isSystem != null) {
			office.set("isSystem", isSystem);
		}
		
		office.set("description", description);
		
		Map<String, Object> result = Maps.newHashMap();
		result.put("isSuccess", office.update());
		return renderSUC(result, response, header);
	}
	
	/**
	 * 判断部门代码是否已经存在
	 * @param name
	 * @return
	 */
	private boolean codeIsExisted(String code, String originValue) {
		return Office.dao.isExisted(code, originValue);
	}
}
