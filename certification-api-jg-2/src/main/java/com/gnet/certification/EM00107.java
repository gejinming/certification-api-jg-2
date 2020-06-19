package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcVersion;
import com.gnet.service.CcVersionService;
import com.gnet.utils.SpringContextHolder;

/**
 * 获取最新版本
 * 
 * @author SY
 * @Date 2016年6月22日18:44:23
 */
@Service("EM00107")
public class EM00107 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据
		Long majorId = paramsLongFilter(params.get("majorId"));
		Boolean isPublish = paramsBooleanFilter(params.get("isPublish"));
		
		if (majorId == null) {
			return renderFAIL("0146", response, header);
		}
		
		// 保存这个信息
		CcVersionService ccVersionService = SpringContextHolder.getBean(CcVersionService.class);
		CcVersion ccVersion = ccVersionService.findLastVersion(majorId, null, isPublish, Boolean.FALSE);
		if (ccVersion == null) {
			return renderFAIL("0141", response, header);
		}
		CcVersion majorVersion = CcVersion.dao.findVersion(ccVersion.getLong("major_id"), ccVersion.getInt("major_version"), CcVersion.INITIAL_MINOR);
	    if(majorVersion == null){
	    	return renderFAIL("0841", response, header);
	    }
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		result.put("id", ccVersion.get("id"));
		result.put("createDate", ccVersion.get("create_date"));
		result.put("modifyDate", ccVersion.get("modify_date"));
		result.put("majorId", ccVersion.get("major_id"));
		result.put("no", ccVersion.get("no"));
		result.put("name", ccVersion.get("name"));
		result.put("majorName", ccVersion.getStr("majorName"));
		result.put("state", ccVersion.get("state"));
		result.put("type", ccVersion.get("type"));
		result.put("description", ccVersion.get("description"));
		result.put("publishDate", ccVersion.get("publish_date"));//enable_grade
		result.put("remark", ccVersion.get("remark"));
		result.put("minorVersion", ccVersion.get("minor_version"));
		result.put("majorVersion", ccVersion.get("major_version"));
		result.put("parentId", ccVersion.get("parent_id"));
		result.put("enableGrade", ccVersion.get("enable_grade"));
		result.put("applyGrade", ccVersion.get("apply_grade"));
		result.put("majorVersionName", majorVersion.getStr("name"));
		result.put("majorVersionId", majorVersion.getLong("id"));
		// 返回操作是否成功
		return renderSUC(result, response, header);
	}
	
}
