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
import com.gnet.model.admin.CcInstitute;
import com.gnet.model.admin.Office;
import com.gnet.model.admin.User;
import com.gnet.service.OfficeService;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.kit.StrKit;

/**
 * 检查学院名是否唯一
 * 
 * @author zsf
 * @Date 2016年06月26日 18:57:47
 */
@Service("EM00145")
public class EM00145 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		String name = paramsStringFilter(params.get("name"));
		String originValue = paramsStringFilter(params.get("originValue"));
		
		String token = request.getHeader().getToken();
		User user = UserCacheKit.getUser(token);
		
		if(user == null){
			return renderFAIL("0081", response, header);
		}

		// 学院名不能为空过滤
		if (StrKit.isBlank(name)) {
			return renderFAIL("0212", response, header);
		}
		
		//获取当前用户所在学校
		OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
		Office school = officeService.getSchoolByUserId(user.getLong("id"));
		if(school == null){
			return renderFAIL("0201", response, header);
		}
		
		// 结果返回
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("isExisted", CcInstitute.dao.isExisted(school.getLong("id"), name, originValue));
		return renderSUC(result, response, header);
	}

}
