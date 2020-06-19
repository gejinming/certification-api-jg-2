package com.gnet.certification;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.UserRole;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;

/**
 * 增加某个用户与角色的关系信息
 * 
 * @author SY
 * @Date 2016年6月21日13:54:18
 */
@Service("EM00092")
public class EM00092 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 各种其他信息
		Long roleId = paramsLongFilter(params.get("roleId"));
		Long userId = paramsLongFilter(params.get("userId"));
		// roleId不能为空信息的过滤
		if (roleId == null) {
			return renderFAIL("0020", response, header);
		}
		// userId不能为空信息的过滤
		if (userId == null) {
			return renderFAIL("0017", response, header);
		}
		UserRole userRole = UserRole.dao.findFirstByColumn("user_id", userId);
		Boolean isFirst = Boolean.FALSE;
		if(userRole == null) {
			userRole = new UserRole();
			isFirst = Boolean.TRUE;
		}
		String roles = userRole.getStr("roles");
		if(StrKit.isBlank(roles)) {
			roles = roleId.toString();
		} else {
			// 判断重复
			List<String> temp = Arrays.asList(roles.split(","));
			if(!temp.contains(roleId.toString())) {
				roles = roles + "," + roleId;
			}
		}
		userRole.set("user_id", userId);
		userRole.set("roles", roles);
		// 保存这个信息
		Boolean doResult = Boolean.TRUE;
		if(isFirst) {
			doResult = userRole.save();
		} else {
			doResult = userRole.update();
		}
		// 结果返回
		Map<String, Object> result = Maps.newHashMap();
		// 返回操作是否成功
		result.put("isSuccess", doResult);
		return renderSUC(result, response, header);
	}
	
}
