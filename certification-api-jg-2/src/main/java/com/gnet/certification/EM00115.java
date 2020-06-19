package com.gnet.certification;

import java.util.Map;

import org.springframework.stereotype.Service;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcTeacher;
import com.gnet.model.admin.User;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;

/**
 * 检查教师工号是否唯一
 * 
 * @author SY
 * @Date 2016年06月26日 19:15:07
 */
@Service("EM00115")
public class EM00115 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		String code = paramsStringFilter(params.get("code"));
		String originValue = paramsStringFilter(params.get("originValue")); 
		
		String token = request.getHeader().getToken();
		Long schoolId = UserCacheKit.getSchoolId(token);
		
		// 教师工号不能为空过滤
		if (StrKit.isBlank(code)) {
			return renderFAIL("0165", response, header);
		}
		if (schoolId == null) {
			return renderFAIL("0084", response, header);
		}
		// 结果返回
		Map<String, Object> result = Maps.newHashMap();
		
		//教师工号是否唯一，验证用户表中是否存在与该工号相同的登录名
		if (CcTeacher.dao.isExisted(code, originValue, schoolId) || User.dao.isExisted(code + String.valueOf(schoolId), originValue, schoolId)) {
			result.put("isExisted", true);
			return renderSUC(result, response, header);
		}
		
		result.put("isExisted", false);
		return renderSUC(result, response, header);
	}

}
