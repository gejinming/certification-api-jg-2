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
import com.gnet.model.admin.CcCourseHierarchySecondary;
import com.jfinal.kit.StrKit;

/**
 * 修改次要课程层次某条信息
 * 
 * @author SY
 * @Date 2019年12月9日13:51:21
 */
@Service("EM01123")
public class EM01123 extends BaseApi implements IApi {

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
				
		CcCourseHierarchySecondary ccCourseHierarchySecondary = CcCourseHierarchySecondary.dao.findFilteredById(hierarchyId);
		if(ccCourseHierarchySecondary == null) {
			return renderFAIL("0101", response, header);
		}
		//name不能重复
		if(CcCourseHierarchySecondary.dao.isExisted(name, ccCourseHierarchySecondary.getStr("name"), ccCourseHierarchySecondary.getLong("plan_id"))){
			return renderFAIL("0103", response, header);
		}
		
		Date date = new Date();
		// 保存这个信息
		ccCourseHierarchySecondary.set("modify_date", date);
		ccCourseHierarchySecondary.set("name", name);
		ccCourseHierarchySecondary.set("remark", remark);
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		// 返回操作是否成功
		result.put("isSuccess", ccCourseHierarchySecondary.update());
		return renderSUC(result, response, header);
	}
	
}
