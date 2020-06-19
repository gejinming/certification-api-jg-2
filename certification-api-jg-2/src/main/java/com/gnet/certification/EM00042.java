package com.gnet.certification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.Role;
import com.gnet.model.admin.UserRole;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;
/**
 * 教师(用户)角色列表接口
 * 
 * @author xzl
 * 
 * @date 2016年11月7日11:07:30
 *
 */
@Service("EM00042")
@Transactional(readOnly=true)
public class EM00042 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Long userId = paramsLongFilter(param.get("userId"));
		if(userId == null){
			return renderFAIL("0017", response, header);
		}
		
		UserRole userRole = UserRole.dao.findFirstByColumn("user_id", userId);
		if(userRole == null){
			return renderFAIL("0012", response, header);
		}
		
		List<String> userRoles = UserCacheKit.getRoles(request.getHeader().getToken());
		if(userRoles.isEmpty()){
			return renderFAIL("0012", response, header);
		}
		List<Role> sysRoles = Role.dao.findByColumnIn("id", userRoles.toArray(new String[userRoles.size()])); 
		if(sysRoles.isEmpty()){
			return renderFAIL("0770", response, header);
		}
		String key = "";
		for(Role role : sysRoles){
			key = role.getStr("authorization_scope") == null ? key : role.getStr("authorization_scope") + "," + key;
		}	
		//学校管理员可以赋予教师的角色
		List<String> roleIdList = Arrays.asList(key.split(","));
		List<Role> roleList = Lists.newArrayList();
		String roles = userRole.getStr("roles");
		if(StrKit.notBlank(roles)){
    		//教师需要关联的角色是否属于学校管理员可以赋予的教师角色
    		List<String> newRoleIdList = CollectionKit.intersection(Arrays.asList(roles.split(",")), roleIdList);
    		if(!newRoleIdList.isEmpty()){
    			roleList = Role.dao.findByColumnIn("id", newRoleIdList.toArray(new String[newRoleIdList.size()]));
    		}	
		}
		
		Map<String, Object> map = Maps.newHashMap();
		List<Map<String, Object>> list = new ArrayList<>();
		for(Role temp : roleList){
			Map<String, Object> tempMap = new HashMap<>();
			tempMap.put("id", temp.getLong("id"));
			tempMap.put("createDate", temp.getDate("create_date"));
			tempMap.put("modifyDate", temp.getDate("modify_date"));
			tempMap.put("description", temp.getStr("description"));
			tempMap.put("isSystem", temp.getBoolean("isSystem"));
			tempMap.put("name", temp.getStr("name"));
			tempMap.put("authorizationScope", temp.getStr("authorization_scope"));
			list.add(tempMap);
		}
		
		map.put("list", list);
		return renderSUC(map, response, header);
	}
	
}
