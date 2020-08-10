package com.gnet.certification;

import java.util.HashMap;
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
import com.jfinal.kit.StrKit;

/**
 * 增加系统用户
 * 
 * @author sll
 * @editBy SY
 * 
 * @date 2016年6月3日18:30:19
 * @editDate 2016年6月25日19:14:23
 *
 */
@Service("EM00005")
@Transactional(readOnly=false)
public class EM00005 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		String loginName = paramsStringFilter(param.get("loginName"));
		String password = paramsStringFilter(param.get("password"));
		String email = paramsStringFilter(param.get("email"));
		List<Long> roleIds = paramsJSONArrayFilter(param.get("roleIds"), Long.class);
		Boolean isEnabled = paramsBooleanFilter(param.get("isEnabled"));
		String departmentId = paramsStringFilter(param.get("departmentId"));
		String name = paramsStringFilter(param.get("name"));
		Long schoolId = paramsLongFilter(param.get("schoolId"));
		
		if (StrKit.isBlank(loginName)) {
			return renderFAIL("0010", response, header);
		}
		if (StrKit.isBlank(password)) {
			return renderFAIL("0011", response, header);
		}
		if (isEnabled == null) {
			return renderFAIL("0014", response, header);
		}
		if (schoolId == null) {
			return renderFAIL("0084", response, header);
		}
		if (StrKit.isBlank(departmentId)) {
			return renderFAIL("0015", response, header);
		}
		if (User.dao.isExisted(schoolId + "-" + loginName)) {
			return renderFAIL("0016", response, header);
		}
		
		UserService userService = SpringContextHolder.getBean(UserService.class);
		Boolean isSuccess = userService.save(null, loginName, password, departmentId, roleIds, name, isEnabled, email, schoolId);
		
		Map<String, Boolean> result = new HashMap<>();
		result.put("isSuccess", isSuccess);
		
		return renderSUC(result, response, header);
	}
	
}
