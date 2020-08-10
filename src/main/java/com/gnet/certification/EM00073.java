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
 * 修改成绩组成某条信息
 * 
 * @author SY
 * @Date 2016年6月20日14:09:05
 */
@Service("EM00073")
public class EM00073 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据
		Long gradecomposeId = paramsLongFilter(params.get("id"));
		String name = paramsStringFilter(params.get("name"));
		String remark = paramsStringFilter(params.get("remark"));
		// gradecomposeId不能为空信息的过滤
		if (gradecomposeId == null) {
			return renderFAIL("0120", response, header);
		}
		// name不能为空信息的过滤
		if (StrKit.isBlank(name)) {
			return renderFAIL("0121", response, header);
		}
		
		Date date = new Date();
		// 保存这个信息
		CcGradecompose ccGradecompose = CcGradecompose.dao.findById(gradecomposeId);
		if (ccGradecompose == null) {
			return renderFAIL("0122", response, header);
		}
		// name不能为重复信息的过滤
		if (CcGradecompose.dao.isExisted(name, ccGradecompose.getStr("name"), ccGradecompose.getLong("major_id"))) {
			return renderFAIL("0123", response, header);
		}
		ccGradecompose.set("modify_date", date);
		ccGradecompose.set("name", name);
		ccGradecompose.set("remark", remark);
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		// 返回操作是否成功
		result.put("isSuccess", ccGradecompose.update());
		return renderSUC(result, response, header);
	}
	
}
