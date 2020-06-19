package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcMajor;
import com.gnet.utils.DictUtils;

/**
 * 查看专业某条信息
 * 
 * @author SY
 * @Date 2016年6月20日14:09:05
 */
@Service("EM00081")
public class EM00081 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据：id
		Long majorId = paramsLongFilter(params.get("id"));
		// hierarchyId不能为空过滤
		if (majorId == null) {
			return renderFAIL("0130", response, header);
		}
		// 通过id获取这条记录
		CcMajor ccMajor = CcMajor.dao.findById(majorId);
		if(ccMajor == null) {
			return renderFAIL("0137", response, header);
		}
		
		// 结果返回
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", ccMajor.get("id"));
		result.put("createDate", ccMajor.get("create_date"));
		result.put("modifyDate", ccMajor.get("modify_date"));
		result.put("officerId", ccMajor.get("officer_id"));
		result.put("officerName", ccMajor.get("userName"));
		result.put("instituteId", ccMajor.get("instituteId"));
		result.put("instituteName", ccMajor.get("instituteName"));
		result.put("majorName", ccMajor.get("majorName"));
		result.put("educationLength", ccMajor.get("education_length"));
		result.put("specializedFieldsName", DictUtils.findLabelByTypeAndKey("specializedFields", ccMajor.getInt("specialized_fields")));
		result.put("specializedFields", ccMajor.getInt("specialized_fields"));
		result.put("awardDegreeName", DictUtils.findLabelByTypeAndKey("awardDegree", ccMajor.getInt("award_degree")));
		result.put("awardDegree", ccMajor.getInt("award_degree"));
		result.put("educationLevelName", DictUtils.findLabelByTypeAndKey("educationLevel", ccMajor.getInt("education_level")));
		result.put("educationLevel", ccMajor.getInt("education_level"));
		result.put("description", ccMajor.get("description"));
		return renderSUC(result, response, header);
	}
	
}
