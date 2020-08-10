package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseOutlineType;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 检查大纲类型名称是否重复
 * 
 * @author xzl
 * @Date 2017-08-22 20:37:03
 */
@Service("EM00709")
public class EM00709 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		String name = paramsStringFilter(params.get("name"));
		Long id = paramsLongFilter(params.get("id"));
		String token = request.getHeader().getToken();
		Long schoolId = UserCacheKit.getSchoolId(token);

		if(schoolId == null){
			return renderFAIL("0084", response, header);
		}

		// 用户名不能为空过滤
		if (StrKit.isBlank(name)) {
			return renderFAIL("0890", response, header);
		}
		
		// 结果返回
		Map<String, Object> result = Maps.newHashMap();
		result.put("isRepeat", CcCourseOutlineType.dao.isRepeatName(name, id, schoolId));
		return renderSUC(result, response, header);
	}
	
}
