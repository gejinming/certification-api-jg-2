package com.gnet.certification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.UserRole;

/**
 * 删除某个用户与角色的关联信息
 * 
 * @author SY
 * @Date 2016年6月21日13:54:18
 */
@Service("EM00093")
public class EM00093 extends BaseApi implements IApi {

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
		if(userRole == null) {
			return renderFAIL("0140", response, header);
		}
		List<String> roles = new ArrayList<>();
		List<String> tempList = Arrays.asList(userRole.getStr("roles").split(","));
		roles.addAll(tempList);
		Boolean doResult = Boolean.TRUE;
		// 如果只有一个，就直接删除，否则update
		if(roles.size() == 1) {
			doResult = UserRole.dao.deleteAllByColumn("user_id", userRole.getLong("user_id"), new Date());
		} else {
			String tempDelete = "";
			for(int i = 0; i < roles.size(); i++) {
				if(roleId.toString().equals(roles.get(i))) {
					tempDelete = roles.get(i);
					break;
				}
			}
			roles.remove(tempDelete);
			String saveRules = "";
			for(int i = 0; i < roles.size(); i++) {
				saveRules = saveRules + roles.get(i) + ",";
			}
			userRole.set("user_id", userId);
			userRole.set("roles", saveRules.substring(0, saveRules.length() - 1));
			// 保存这个信息
			doResult =  userRole.update();
		}
		// 结果返回
		JSONObject result = new JSONObject();
		// 返回操作是否成功
		result.put("isSuccess", doResult);
		return renderSUC(result, response, header);
	}
	
}
