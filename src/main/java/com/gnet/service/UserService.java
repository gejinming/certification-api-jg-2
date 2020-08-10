package com.gnet.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.Constant;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.job.AsyncDeleteUserCache;
import com.gnet.job.AsyncUpdateUserCache;
import com.gnet.model.admin.Role;
import com.gnet.model.admin.User;
import com.gnet.model.admin.UserRole;
import com.gnet.plugin.quartz.QuartzKit;
import com.gnet.plugin.quartz.callback.ITaskMonitor;
import com.gnet.utils.PasswdKit;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;

/**
 * @author SY
 * @date 2016年6月25日19:09:13
 */
@Component("userService")
public class UserService {

	private static final Logger logger = Logger.getLogger(UserService.class);
	
	
	/**
	 * 用户名去掉学校编号和分隔符
	 * @param loginName
	 * @return
	 */
	public static String handleLoginName(String loginName){
		Integer index = loginName.indexOf(User.SPLIT, 1);
		loginName = loginName.substring(index + 1, loginName.length());
		return loginName;
	}
	
	/**
	 * 保存user的数据
	 * @param user 
	 * @param email 
	 * @param isEnabled 
	 * @param name 
	 * @param roleIds 
	 * @param departmentId 
	 * @param password 
	 * @param loginName 
	 * @param schoolId 
	 * @return
	 */
	public boolean save(User user, String loginName, String password, String departmentId, List<Long> roleIds, String name, Boolean isEnabled, String email, Long schoolId) {
		Date date = new Date();
		if(user == null) {
			user = new User();
		}
		user.set("create_date", date);
		user.set("modify_date", date);
		user.set("loginName", schoolId + "-" + loginName);
		// 密码加密措施
		user.set("password", PasswdKit.entryptPassword(password));
		user.set("department", departmentId);
		user.put("roleIds", roleIds);
		user.set("name", name);
		user.set("type", User.TYPE_NORMAL);
		user.set("is_enabled", isEnabled);
		user.set("login_failure_count", Constant.USER_DEFAULT_FAIL_COUNT);
		user.set("is_bind_email", Boolean.FALSE);
		user.set("is_bind_mobile", Boolean.FALSE);
		user.set("is_locked", Constant.USER_NOTENABLED);
		user.set("is_del", Boolean.FALSE);
		if (StrKit.notBlank(email)) {
			user.set("email", email);
		}
		
		if (!user.save()) {
			return false;
		}
		
		// 保存userRole
		UserRoleService userRoleService = SpringContextHolder.getBean(UserRoleService.class);
		Boolean result = userRoleService.saveByUser(user);
		
		if (!result) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 更新用户信息
	 * @param id
	 * @param user
	 * @param password
	 * @param departmentId
	 * @param roleIds
	 * @param name
	 * @param isEnabled
	 * @param email
	 * @param type 
	 */
	public boolean update(Long id, User user, String password, String departmentId, List<Long> roleIds, String name, Boolean isEnabled, String email, Integer type) {
		user.set("id", id);
		user.set("name", name);
		user.set("email", email);
		user.set("department", departmentId);
		user.put("roleIds", roleIds);
		if (StrKit.notBlank(password)) {
			user.set("password", password);
		}
		
		if (isEnabled != null) {
			user.set("is_enabled", isEnabled);
		}
		
		if (type != null) {
			user.set("type", type);
		}
		
		
		// 修改密码，并进行密码加密
		if (StrKit.isBlank(user.getStr("password"))) {
			user.set("password", User.dao.findById(user.getLong("id")).getStr("password"));
		} else {
			user.set("password", PasswdKit.entryptPassword(user.getStr("password")));
		}
		user.set("modify_date", new Date());
		if (!user.update()) {
			return false;
		}
		
		UserRoleService userRoleService = SpringContextHolder.getBean(UserRoleService.class);
		// 更新角色信息
		Boolean result = userRoleService.saveByUser(user);
		// 更新一下缓存
		updateCache(user);
		if (!result) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		} else {
			return true;
		}
		
	}

	
	/**
	 * 返回用户的角色信息
	 * @param roles
	 * @param userName
	 * @param userId
	 * @return
	 */
	public List<Map<String, String>> getUserRoles(String roles, String userName, Long userId){
		Map<String, String> rolesMap = Maps.newHashMap();
		List<Map<String, String>> roleMapList = Lists.newArrayList();
		List<Role> rolesList = Role.dao.findAll();
		if(rolesList.isEmpty()){
			return roleMapList;
		}	
		for (Role role : rolesList) {
			rolesMap.put(String.valueOf(role.getLong("id")), role.getStr("name"));
		}
		if(StrKit.notBlank(roles)){
			for(String roleIdStr : roles.split(",")){
				Map<String, String> temp = Maps.newHashMap();
				String roleName = rolesMap.get(roleIdStr);
				if(StrKit.isBlank(roleName)){
					logger.error("用户"+userName + ",编号【" + userId + "】" + "的角色" + roleName + "在角色表中已被删除或不存在");
					continue;
				}
				temp.put("id", roleIdStr);
				temp.put("name", roleName);
				roleMapList.add(temp);
			}
		}	
		return roleMapList;		
	}
	
	/**
	 * 异步更新登陆用户的缓存信息
	 * @param user
	 */
	public void updateCache(User user) {
		final Long id = user.getLong("id");
		List<String> tokens = UserCacheKit.token(id);
		//如果当前登录用户是学校管理员，那么修改教师缓存信息的时候，无法通过教师编号获取相应的token的信息
		if(tokens != null && !tokens.isEmpty()){
			for (String token : tokens) {
				updateCache(user, token);
			}
		}
	}

	private void updateCache(User user, String token) {
		final Long id = user.getLong("id");
		if(StrKit.isBlank(token)){
			logger.error("token为空无法更新登录用户的缓存信息");
			return;
		}
		try {
			Map<String, Object> jobData = new HashMap<>();
			jobData.put("token", token);
			jobData.put("user", user);
			QuartzKit.createTaskStartNow(id + "异步更新已登陆用户信息", AsyncUpdateUserCache.class, jobData, true, SimpleScheduleBuilder.simpleSchedule(), new ITaskMonitor() {

				@Override
				public void taskStart() {
					logger.info(id + "异步更新已登陆用户信息开始");
				}

				@Override
				public void taskFinish() {
					logger.info(id + "异步更新已登陆用户信息成功");
				}

				@Override
				public void taskFail() {
					logger.error(id + "异步更新已登陆用户信息失败");
				}
			});
		} catch (SchedulerException e) {
			logger.error("执行" + id + "异步更新已登陆用户信息过程中出错", e);
		}
	}

	/**
	 * 批量异步删除已登陆用户的缓存
	 * @param idsArray
	 */
	public void deleteCache(Long[] idsArray) {
		try {
			final Long[] finalIdsArray = idsArray;
			Map<String, Object> jobData = new HashMap<>();
			jobData.put("token", UserCacheKit.tokens(idsArray));
			QuartzKit.createTaskStartNow(JsonKit.toJson(idsArray) + "异步删除已登陆用户信息", AsyncDeleteUserCache.class, jobData, true, SimpleScheduleBuilder.simpleSchedule(), new ITaskMonitor() {
				
				@Override
				public void taskStart() {
					logger.info("执行" + JsonKit.toJson(finalIdsArray) + "异步删除已登陆用户信息开始");
				}
				
				@Override
				public void taskFinish() {
					logger.info("执行" + JsonKit.toJson(finalIdsArray) + "异步删除已登陆用户信息完成");
				}
				
				@Override
				public void taskFail() {
					logger.error("执行" + JsonKit.toJson(finalIdsArray) + "异步删除已登陆用户信息失败");
				}
			});
		} catch (SchedulerException e) {
			logger.error("执行" + JsonKit.toJson(idsArray) + "异步删除已登陆用户信息过程中出错", e);
		}
	}

	/**
	 * 删除用户、角色、cache
	 * @param idsArray
	 * @param date
	 */
	public boolean deleteUser(Long[] idsArray, Date date) {
		// 删除用户，删除对应的角色联系
		if(!User.dao.deleteAll(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		if(!UserRole.dao.deleteAll(idsArray, date)){
	    	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	    	return false;
	    }
		deleteCache(idsArray);
		return true;
	}
	
}
