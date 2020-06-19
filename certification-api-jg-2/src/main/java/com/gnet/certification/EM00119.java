package com.gnet.certification;

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
import com.jfinal.kit.StrKit;

/**
 * 赋予教师账号角色接口
 * 
 * @author xzl
 * 
 * @date 2016年11月2日15:58:40
 *
 */
@Service("EM00119")
@Transactional(readOnly=false)
public class EM00119 extends BaseApi implements IApi{
	
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Map<String, Boolean> result = new HashMap<>();
		
		Long teacherId = paramsLongFilter(param.get("teacherId"));
		//教师需要关联的角色
		List<String> roleIds = paramsJSONArrayFilter(param.get("roleIds"), String.class);
		
		if(teacherId == null){
			return renderFAIL("0160", response, header);
		}
			
		List<String> roles = UserCacheKit.getRoles(request.getHeader().getToken());
		if(roles.isEmpty()){
			return renderFAIL("0012", response, header);
		}
		List<Role> sysRoles = Role.dao.findByColumnIn("id", roles.toArray(new String[roles.size()])); 
		if(sysRoles.isEmpty()){
			return renderFAIL("0770", response, header);
		}
		String key = "";
		for(Role role : sysRoles){
			key = role.getStr("authorization_scope") == null ? key : role.getStr("authorization_scope") + "," + key;
		}
		
		//学校管理员可以赋予教师的角色
		List<String> roleIdList = Arrays.asList(key.split(","));
		//教师本身有的角色
		UserRole userRole = UserRole.dao.findFirstByColumn("user_id", teacherId);
		String teacherRoles = userRole.getStr("roles");
        //教师独有的学校管理员不能赋予的角色
        List<String> uniqueRoles = Lists.newArrayList();
        if(StrKit.notBlank(teacherRoles)){
            List<String> teacherRoleList = Arrays.asList(teacherRoles.split(","));
            for(String role : teacherRoleList){
            	if(!roleIdList.contains(role)){
            		uniqueRoles.add(role);
            	}
            }
        }        
        String newTeacherRole = "";
        if(roleIds.isEmpty() && uniqueRoles.isEmpty()){
        	 userRole.set("roles", "");
        	 if(!userRole.update()){
     			result.put("isSuccess", userRole.update());
    			return renderSUC(result, response, header);
        	 }
         }else{
     		 //教师需要关联的角色是否属于学校管理员可以赋予的教师角色
     		 List<String> newRoleIdList = CollectionKit.intersection(roleIds, roleIdList);
     		 newRoleIdList.addAll(uniqueRoles);
        	 for(String role : newRoleIdList){
        		 newTeacherRole = role + "," + newTeacherRole;
        	 }
        	 newTeacherRole = newTeacherRole.substring(0, newTeacherRole.length() - 1);
        	 userRole.set("roles", newTeacherRole);
        	 if(!userRole.update()){
     			result.put("isSuccess", userRole.update());
    			return renderSUC(result, response, header);
        	 }
         }
		 
        result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}
}
