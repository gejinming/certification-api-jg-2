package com.gnet.certification;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.*;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.service.LoginService;
import com.gnet.service.PermissionService;
import com.gnet.utils.AccessTokenKit;
import com.gnet.utils.CollectionKit;
import com.gnet.utils.PasswdKit;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;

/**
 * 用户登录
 * @author wct
 * @Date 2016年6月3日
 */
@Service("EM00001")
public class EM00001 extends BaseApi implements IApi {
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		String loginName = paramsStringFilter(params.get("loginName"));
		String password = paramsStringFilter(params.get("password"));
		String schoolId = paramsStringFilter(params.get("schoolId"));
		//区别学生登录还是教师登录 1教师登录，2学生登录
		Integer userRole = paramsIntegerFilter(params.get("userRole"));
		// 用户名不能为空过滤
		if (StrKit.isBlank(loginName)) {
			return renderFAIL("0001", response, header);
		}
		
		// 密码不能为空过滤
		if (StrKit.isBlank(password)) {
			return renderFAIL("0002", response, header);
		}
		if (userRole==null){
			return renderFAIL("2560", response, header);
		}
		User user = new User();
		//教师登录
		if (userRole==1){
			// 用户是否存在检验,管理员不需要加学校编号
			if(loginName.equals(User.ADMIN_LOGINNAME)){
				user = getUser(loginName);
			}else{
				user = getUser(schoolId + "-" +loginName);
			}

			if (user == null) {
				return renderFAIL("0003", response, header);
			}

			user.set("login_date", new Date());
			user.set("login_ip", request.getRequestIp());
			//设置登录信息
			if(!user.update()){
				return renderFAIL("0830", response, header);
			}

			// 密码检验
			if (!PasswdKit.validatePassword(password, user.getStr("password"))) {
				return renderFAIL("0004", response, header);
			}
		}else {
			//学生登录验证
			CcStudent student = getStudent(loginName, password, schoolId);
			//User user1 = User.dao.existedStudents(loginName, password, schoolId);
			if (student != null ){
				user.put("id",student.getLong("id"));
				user.put("name",student.getStr("name"));
				user.put("password",student.getStr("password"));
				user.put("loginName",student.getStr("student_no"));

			}else{
				return renderFAIL("0003", response, header);
			}

		}

		// Token生成
		String token = AccessTokenKit.getToken(user.getLong("id"));
		// 生成Token失败过滤
		if (token == null) {
			return renderFAIL("0005", response, header);
		}
		
		// 设置Cache，缓存用户权限信息
		saveInfo(user, token);
		
		//获取该用户功能权限列表
		PermissionService permissionService = SpringContextHolder.getBean(PermissionService.class);
		List<Permission> permissions = permissionService.findByUserId(user.getLong("id"));
		List<Map<String, Object>> permissionMap = Lists.newArrayList();
		for (Permission permission : permissions) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", permission.getLong("id"));
			map.put("code", permission.getStr("code"));
			map.put("name", permission.getStr("name"));
			map.put("pname", permission.getStr("pname"));
			map.put("isSystem", permission.getBoolean("is_system"));
			permissionMap.add(map);
		}
		
		// 返回结果
		JSONObject result = new JSONObject();
		result.put("id", user.getLong("id"));
		result.put("name", user.getStr("name"));
		result.put("loginName", user.getStr("loginName"));
		result.put("token", token);
		result.put("permissions", permissionMap);
		result.put("userRole",userRole);
		
		// 添加额外的登录信息
		LoginService loginService = SpringContextHolder.getBean(LoginService.class);
		Map<String, Object> extraInfo = loginService.getExtraInfo(user.getLong("id"));
		if (!extraInfo.isEmpty()) {
			result.putAll(extraInfo);
		}
		
		return renderSUC(result, response, header);
	}
	
	/**
	 * 通过用户名获取用户
	 * @param loginName
	 * @return
	 */
	private User getUser(String loginName) {
		return User.dao.findByLoginName(loginName);
	}
	/*
	 * @param loginName
		 * @param password
		 * @param schoolId
	 * @return com.gnet.model.admin.CcStudent
	 * @author Gejm
	 * @description: 学生登录验证
	 * @date 2020/8/4 16:37
	 */
	private CcStudent getStudent(String loginName,String password,String schoolId){
		return CcStudent.dao.existedStudents(loginName,password,schoolId);
	}
	/**
	 * 登录将用户角色与权限保存到缓存中
	 * @param userId
	 * @param token
	 */
	private void saveInfo(User user, String token) {
		UserRole userRole = UserRole.dao.findById(user.getLong("id"));
		List<String> roles = Lists.newArrayList();
		if (userRole != null && StrKit.notBlank(userRole.getStr("roles"))) {
			roles = Lists.newArrayList(CollectionKit.convert(userRole.getStr("roles"), ","));
		}
		
		PermissionService permissionService = SpringContextHolder.getBean(PermissionService.class);
		List<Permission> permissions = permissionService.findByUserId(user.getLong("id"));
		List<String> permissionCodes = Lists.newArrayList();
		for (Permission permission: permissions) {
			permissionCodes.add(permission.getStr("code"));
		}
		
		//获得api权限组
		Long[] permissionIds = new Long[permissions.size()];
		for (int i = 0; i < permissions.size(); i++) {
			permissionIds[i] = permissions.get(i).getLong("id");
		}
		List<String> apiPermissionCodes = Lists.newArrayList();
		if (permissionIds.length != 0) {
			List<ApiPermission> apiPermissions = ApiPermission.dao.findApiPermissionByPermissionId(permissionIds);
			for (ApiPermission apiPermission: apiPermissions){
				apiPermissionCodes.add(apiPermission.getStr("code"));
			}
		}
		
		UserCacheKit.login(token, user, permissionCodes, roles, apiPermissionCodes);
	}
	
	

}
