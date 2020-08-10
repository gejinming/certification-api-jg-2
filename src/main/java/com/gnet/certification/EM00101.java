package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.annotation.RequirePermission;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcVersion;

/**
 * 查看版本某条信息
 * 
 * @author SY
 * @Date 2016年6月22日14:13:54
 */
@Service("EM00101")
@RequirePermission(values = { "EM00101" })
public class EM00101 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据：id
		Long versionId = paramsLongFilter(params.get("id"));
		// 不能为空过滤
		if (versionId == null) {
			return renderFAIL("0140", response, header);
		}
		// 通过id获取这条记录
		CcVersion ccVersion = CcVersion.dao.findFilteredById(versionId);
		if(ccVersion == null) {
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
		result.put("majorName", ccVersion.get("majorName"));
		result.put("no", ccVersion.get("no"));
		result.put("name", ccVersion.get("name"));
		result.put("state", ccVersion.get("state"));
		result.put("type", ccVersion.get("type"));
		result.put("description", ccVersion.get("description"));
		result.put("publishDate", ccVersion.get("publish_date"));
		result.put("remark", ccVersion.get("remark"));
		result.put("enableGrade", ccVersion.get("enable_grade"));
		result.put("applyGrade", ccVersion.get("apply_grade"));
		result.put("minorVersion", ccVersion.get("minor_version"));
		result.put("majorVersion", ccVersion.get("major_version"));
		result.put("parentId", ccVersion.get("parent_id"));
		result.put("parentName", ccVersion.get("parentName"));
		result.put("planName", ccVersion.get("planName"));
		result.put("planCourseVersionName", ccVersion.get("planCourseVersionName"));
		result.put("graduateName", ccVersion.get("graduateName"));
		result.put("graduateIndicationVersionName", ccVersion.get("graduateIndicationVersionName"));
		result.put("pass", ccVersion.get("pass"));
		result.put("majorVersionName", majorVersion.getStr("name"));
		result.put("majorVersionId", majorVersion.getLong("id"));
		return renderSUC(result, response, header);
	}
	
}
