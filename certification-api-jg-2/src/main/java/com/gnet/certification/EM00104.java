package com.gnet.certification;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcVersion;
import com.gnet.response.ServiceResponse;
import com.gnet.service.CcVersionService;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.kit.StrKit;

/**
 * 修改版本某条信息的基本信息
 *  
 * @author SY
 * @Date 2016年6月22日18:44:23
 */
@Service("EM00104")
public class EM00104 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据
		Long versionId = paramsLongFilter(params.get("id"));
		String description = paramsStringFilter(params.get("description"));
		String name = paramsStringFilter(params.get("name"));
		String remark = paramsStringFilter(params.get("remark"));
		Integer enableGrade = paramsIntegerFilter(params.get("enableGrade"));
		// 另外两张关联表
		String planName = paramsStringFilter(params.get("planName"));
		String planCourseVersionName = paramsStringFilter(params.get("planCourseVersionName"));
		String graduateName = paramsStringFilter(params.get("graduateName"));
		String graduateIndicationVersionName = paramsStringFilter(params.get("graduateIndicationVersionName"));
		BigDecimal pass = paramsBigDecimalFilter(params.get("pass"));
				
		if (versionId == null) {
			return renderFAIL("0140", response, header);
		}
		if (StrKit.isBlank(name)) {
			return renderFAIL("0147", response, header);
		}
		if (pass == null) {
			return renderFAIL("0158", response, header);
		}
		if (StrKit.isBlank(planName)) {
			return renderFAIL("0159", response, header);
		}
		if (StrKit.isBlank(graduateName)) {
			return renderFAIL("0157", response, header);
		}
		
		Date date = new Date();
		// 保存这个信息
		CcVersion ccVersion = CcVersion.dao.findById(versionId);
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		
		Long majorId = ccVersion.getLong("major_id");
		Integer type = ccVersion.getInt("type");
		CcVersionService ccVersionService = SpringContextHolder.getBean(CcVersionService.class);
		// 验证版本的起始年级是否符合要求
		ServiceResponse responseResult = ccVersionService.validateUpdateEnableGrade(type, enableGrade, majorId, ccVersion.getLong("parent_id"), versionId);
		if(!responseResult.isSucc()) {
			if(CcVersion.TYPE_MAJOR_VERSION.equals(type)) {
				return renderFAIL("0155", response, header, responseResult.getContent());
			}
			return renderFAIL("0156", response, header);
		}
		
		// 找到当前版本的使用年级
		String applyGrade = ccVersionService.getUpdateApplyGrade(type, enableGrade, majorId, versionId);
		Integer oldEnableGrade = ccVersion.getInt("enable_grade");
		
		ccVersion.set("modify_date", date);
		ccVersion.set("description", description);
		ccVersion.set("enable_grade", enableGrade);
		ccVersion.set("apply_grade", applyGrade);
		ccVersion.set("name", name);
		ccVersion.set("remark", remark);
		Boolean updateResult = ccVersion.update();
		
		// 更新影响到的其他applyGrade
		if(CcVersion.TYPE_MAJOR_VERSION.equals(type) && !oldEnableGrade.equals(enableGrade)) {
			CcVersion beforeVersion = ccVersionService.findBeforeVersion(majorId, ccVersion.getInt("major_version"));
			if(beforeVersion != null){
				Map<String, Object> mapParams = new HashMap<>();
				mapParams.put("major_version", beforeVersion.getInt("major_version"));
				mapParams.put("major_id", majorId);
				List<CcVersion> ccVersionList = CcVersion.dao.findFilteredByColumn(mapParams);

				Integer beforeEnableGrade = beforeVersion.getInt("enable_grade");
				String newParentApplyGrade = "";
				// 获取父亲【启用年级】和这次【启用年级】两个数据创建的【适用年级】
				for (Integer grade = beforeEnableGrade; grade < enableGrade; grade++) {
					newParentApplyGrade = newParentApplyGrade + grade + ",";
				}
				newParentApplyGrade = newParentApplyGrade.substring(0, newParentApplyGrade.length() - 1);

				for(CcVersion temp : ccVersionList) {
					temp.set("apply_grade", newParentApplyGrade);
					temp.set("modify_date", date);
				}
				if(!CcVersion.dao.batchUpdate(ccVersionList, "modify_date,apply_grade")) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					result.put("isSuccess", updateResult);
					return renderSUC(result, response, header);
				}
			}
		}
		
		// 更新两个表信息
		updateResult = ccVersionService.updateLink(ccVersion, planName, planCourseVersionName, graduateName, graduateIndicationVersionName, pass);
		if(!updateResult) {
			// 返回操作是否成功
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", updateResult);
			return renderSUC(result, response, header);
		}
		
		// 返回操作是否成功
		result.put("isSuccess", updateResult);
		return renderSUC(result, response, header);
	}
	
}
