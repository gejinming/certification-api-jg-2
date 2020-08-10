package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.gnet.api.kit.UserCacheKit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcMajor;
import com.gnet.model.admin.Office;
import com.gnet.service.OfficeService;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.kit.StrKit;

/**
 * 修改专业某条信息
 * 
 * @author SY
 * @Date 2016年6月20日14:09:05
 */
@Service("EM00083")
@Transactional(readOnly=false)
public class EM00083 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据
		Long majorId = paramsLongFilter(params.get("id"));
		Long instituteId = paramsLongFilter(params.get("instituteId"));
		Integer educationLength = paramsIntegerFilter(params.get("educationLength"));
		String description = paramsStringFilter(params.get("description"));
		String name = paramsStringFilter(params.get("majorName"));
		Integer specializedFields = paramsIntegerFilter(params.get("specializedFields"));
		Integer awardDegree = paramsIntegerFilter(params.get("awardDegree"));
		Integer educationLevel = paramsIntegerFilter(params.get("educationLevel"));
		Long schoolId = UserCacheKit.getSchoolId(request.getHeader().getToken());

		// hierarchyId不能为空过滤
		if(schoolId == null){
			return renderFAIL("0084", response, header);
		}
		if (StrKit.isBlank(name)) {
			return renderFAIL("0150", response, header);
		}
		if (majorId == null) {
			return renderFAIL("0130", response, header);
		}
		// educationLength不能为空信息的过滤
		if (educationLength == null) {
			return renderFAIL("0132", response, header);
		}
		// specializedFields不能为空信息的过滤
		if (specializedFields == null) {
			return renderFAIL("0134", response, header);
		}
		// awardDegree不能为空信息的过滤
		if (awardDegree == null) {
			return renderFAIL("0135", response, header);
		}
		// educationLevel不能为空信息的过滤
		if (educationLevel == null) {
			return renderFAIL("0136", response, header);
		}
		if (instituteId == null) {
			return renderFAIL("0138", response, header);
		}
		
		Date date = new Date();
		// 保存这个信息
		CcMajor ccMajor = CcMajor.dao.findById(majorId);
		if(ccMajor == null) {
			return renderFAIL("0137", response, header);
		}
		if (CcMajor.dao.isExisted(schoolId, name, majorId)) {
			return renderFAIL("0139", response, header);
		}
		
		ccMajor.set("modify_date", date);
		ccMajor.set("education_length", educationLength);
		ccMajor.set("description", description);
		ccMajor.set("specialized_fields", specializedFields);
		ccMajor.set("award_degree", awardDegree);
		ccMajor.set("education_level", educationLevel);
		Boolean updateResult = ccMajor.update();

		// 结果返回
		Map<String, Object> result = new HashMap<>();
		if(!updateResult) {
			result.put("isSuccess", updateResult);
			return renderSUC(result, response, header);
		}
		
		// 保存这个信息
		Office office = Office.dao.findById(majorId);
		office.set("modify_date", date);
		office.set("name", name);
		office.set("parentid", instituteId);

		OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
		updateResult = officeService.update(office, instituteId, date);
		if(!updateResult) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			// 返回操作是否成功
			result.put("isSuccess", updateResult);
			return renderSUC(result, response, header);
		}
		
		// 返回操作是否成功
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}
	
}
