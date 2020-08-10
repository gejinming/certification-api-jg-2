package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcMajor;
import com.gnet.model.admin.User;
import com.gnet.utils.DictUtils;

/**
 * 查看专业负责人的专业--某条信息
 * 
 * @author SY
 * @Date 2016年6月29日22:44:16
 */
@Service("EM00089")
public class EM00089 extends BaseApi implements IApi {


	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
	
		String token = request.getHeader().getToken();
		//专业负责人的编号
		Long officerId = UserCacheKit.getUser(token).getLong("id");
		//专业负责人信息
		User user = UserCacheKit.getUser(token);
		
		// officerId不能为空过滤
		if (officerId == null) {
			return renderFAIL("0131", response, header);
		}
		
		// 通过专业负责人来获得专业信息
		CcMajor ccMajor = CcMajor.dao.findByOfficerId(officerId);
		
		if(ccMajor == null) {
			return renderFAIL("0137", response, header);
		}
		
		// 结果返回
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", ccMajor.getLong("id"));
		result.put("createDate", ccMajor.getDate("create_date"));
		result.put("modifyDate", ccMajor.getDate("modify_date"));
		result.put("officerId", ccMajor.getLong("officer_id"));
		result.put("officerName", user.getStr("name"));
		result.put("instituteId", ccMajor.getLong("instituteId"));
		result.put("instituteName", ccMajor.getStr("instituteName"));
		result.put("code", ccMajor.getStr("code"));
		result.put("majorName", ccMajor.getStr("majorName"));
		result.put("englishName", ccMajor.getStr("english_name"));
		result.put("educationLength", ccMajor.getInt("education_length"));
		result.put("specializedFields", DictUtils.findLabelByTypeAndKey("specializedFields", ccMajor.getInt("specialized_fields")));
		result.put("awardDegree", DictUtils.findLabelByTypeAndKey("awardDegree", ccMajor.getInt("award_degree")));
		result.put("educationLevel", DictUtils.findLabelByTypeAndKey("educationLevel", ccMajor.getInt("education_level")));
		result.put("description", ccMajor.getStr("description"));
		result.put("remark", ccMajor.getStr("remark"));
		return renderSUC(result, response, header);
	}
	
}
