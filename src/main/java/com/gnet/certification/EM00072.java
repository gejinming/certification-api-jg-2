package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcGradecompose;
import com.jfinal.kit.StrKit;

/**
 * 新增成绩组成某条信息
 * 
 * @author SY
 * @Date 2016年6月20日14:09:05
 */
@Service("EM00072")
public class EM00072 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 各种其他信息
		String name = paramsStringFilter(params.get("name"));
		String remark = paramsStringFilter(params.get("remark"));
		Long majorId = paramsLongFilter(params.get("majorId"));
		
		// name不能为空信息的过滤
		if (StrKit.isBlank(name)) {
			return renderFAIL("0120", response, header);
		}
		if (majorId == null) {
			return renderFAIL("0130", response, header);
		}
		// name不能为重复信息的过滤
		if (CcGradecompose.dao.isExisted(name, majorId)) {
			return renderFAIL("0123", response, header);
		}
		Date date = new Date();
		CcGradecompose ccGradecompose = new CcGradecompose();
		ccGradecompose.set("create_date", date);
		ccGradecompose.set("modify_date", date);
		ccGradecompose.set("major_id", majorId);
		ccGradecompose.set("name", name);
		ccGradecompose.set("remark", remark);
		ccGradecompose.set("is_del", Boolean.FALSE);
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		// 返回操作是否成功
		result.put("isSuccess", ccGradecompose.save());
		return renderSUC(result, response, header);
	}
	
}
