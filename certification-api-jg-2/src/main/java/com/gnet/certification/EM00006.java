package com.gnet.certification;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.User;
import com.gnet.service.UserService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;

/**
 * 编辑系统用户
 * 
 * @author sll
 * 
 * @date 2016年6月3日18:30:49
 *
 */
@Service("EM00006")
@Transactional(readOnly=false)
public class EM00006 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		Long id = paramsLongFilter(param.get("id"));
		String password = paramsStringFilter(param.get("password"));
		//邮箱可以为空
		String email = paramsStringFilter(param.get("email"));
		Integer type = paramsIntegerFilter(param.get("type"));
		//角色编号可以为空
		List<Long> roleIds = paramsJSONArrayFilter(param.get("roleIds"), Long.class);
		Boolean isEnabled = paramsBooleanFilter(param.get("isEnabled"));
		String departmentId = paramsStringFilter(param.get("departmentId"));
		String name = paramsStringFilter(param.get("name"));
		
		if (id == null) {
			return renderFAIL("0017", response, header);
		}
		
		User user = new User();
		UserService userService = SpringContextHolder.getBean(UserService.class);
		Boolean isSuccess = userService.update(id, user, password, departmentId, roleIds, name, isEnabled, email, type);
		
		Map<String, Boolean> result = Maps.newHashMap();
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}

}
