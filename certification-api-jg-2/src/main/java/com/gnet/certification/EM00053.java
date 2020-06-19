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
import com.gnet.model.admin.CcCourseHierarchy;
import com.jfinal.kit.StrKit;

/**
 * 修改课程层次某条信息
 * 
 * @author SY
 * @Date 2016年6月20日14:09:05
 */
@Service("EM00053")
public class EM00053 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据
		Long hierarchyId = paramsLongFilter(params.get("id"));
		String name = paramsStringFilter(params.get("name"));
		String remark = paramsStringFilter(params.get("remark"));
		
		// hierarchyId不能为空信息的过滤
		if (hierarchyId == null) {
			return renderFAIL("0100", response, header);
		}
		
		// name不能为空信息的过滤
		if (StrKit.isBlank(name)) {
			return renderFAIL("0102", response, header);
		}
				
		CcCourseHierarchy ccCourseHierarchy = CcCourseHierarchy.dao.findFilteredById(hierarchyId);
		if(ccCourseHierarchy == null) {
			return renderFAIL("0101", response, header);
		}
		//name不能重复
		if(CcCourseHierarchy.dao.isExisted(name, ccCourseHierarchy.getStr("name"), ccCourseHierarchy.getLong("plan_id"))){
			return renderFAIL("0103", response, header);
		}
		
		Date date = new Date();
		// 保存这个信息
		ccCourseHierarchy.set("modify_date", date);
		ccCourseHierarchy.set("name", name);
		ccCourseHierarchy.set("remark", remark);
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		// 返回操作是否成功
		result.put("isSuccess", ccCourseHierarchy.update());
		return renderSUC(result, response, header);
	}
	
}
