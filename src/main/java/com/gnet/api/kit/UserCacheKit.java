package com.gnet.api.kit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.Office;
import com.gnet.model.admin.User;
import com.gnet.service.OfficeService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;
import com.jfinal.log.Logger;

/**
 * 用户缓存工具
 * 
 * @author xuq
 * @date 2016年6月7日
 * @version 1.0
 */
public class UserCacheKit extends CacheKit {
	
	private static final Logger LOG = Logger.getLogger(UserCacheKit.class);

	private static final String CACHE_REGION = "userinfo";
	private static final String MAPPING_QUEUE = "mapping_queue";
	
	private UserCacheKit() {}
	
	/**
	 * 获取token
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> token(Long id) {
		Map<Long, List<String>> mapping = CacheKit.get(CACHE_REGION, MAPPING_QUEUE, HashMap.class);
		if(mapping == null){
			LOG.error("CacheKit中region为userinfo，key为mapping_queue得到的mapping为空");
			return null;
		}
		return mapping.get(id);
	}
	
	/**
	 * 获取多个tokens
	 * 
	 * @param ids
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> tokens(Long[] ids) {
		List<String> tokens = new ArrayList<>();
		Map<Long, String> mapping = CacheKit.get(CACHE_REGION, MAPPING_QUEUE, HashMap.class);
		for (Long id : ids) {
			tokens.add(mapping.get(id));
		}
		return tokens;
	}
	
	/**
	 * 系统登录缓存
	 * 
	 * @param token
	 * @param user
	 * @param permissions
	 * @param roles
	 */
	@SuppressWarnings("unchecked")
	public static void login(String token, User user, List<String> permissions, List<String> roles, List<String> apiPermissionCodes) {
		Map<String, Object> userinfo = Maps.newHashMap();
		userinfo.put("user", user);
		userinfo.put("permissions", permissions);
		userinfo.put("roles", roles);
		userinfo.put("apiPermissionCodes", apiPermissionCodes);
		
		// 加入userid与token对应队列
		Map<Long, List<String>> mapping = CacheKit.get(CACHE_REGION, MAPPING_QUEUE, HashMap.class);
		if (mapping == null) {
			mapping = new HashMap<>();
			LOG.warn("登录队列重新初始化");
		}
		
		CacheKit.set(CACHE_REGION, token, userinfo);
		// 提示：在这里不做单点登录处理，如果为单点登录，则在这里需要判断是否存在已登陆的账户，存在则剔除。
		List<String> tokens = mapping.get(user.getLong("id"));
		if (tokens == null) {
			tokens = new ArrayList<>();
		}
		tokens.add(token);
		mapping.put(user.getLong("id"), tokens);
		CacheKit.set(CACHE_REGION, MAPPING_QUEUE, mapping);
	}
	
	/**
	 * 判断是否登陆
	 * 
	 * @param token
	 * @return
	 */
	public static boolean isLogin(String token) {
		return CacheKit.get(CACHE_REGION, token, HashMap.class) != null;
	}
	
	/**
	 * 更新用户信息
	 * 
	 * @param token
	 * @param user
	 * @param permissions
	 * @param roles
	 */
	@SuppressWarnings("unchecked")
	public static void update(String token, User user, List<String> permissions, List<String> roles) {
		Map<String, Object> userinfo = CacheKit.get(CACHE_REGION, token, HashMap.class);
		if (user != null) {
			userinfo.put("user", user);
		}
		if (permissions != null && permissions.isEmpty()) {
			userinfo.put("permissions", permissions);
		}
		if (roles != null && roles.isEmpty()) {
			userinfo.put("roles", roles);
		}
		CacheKit.set(CACHE_REGION, token, userinfo);
	}
	
	/**
	 * 设置属性
	 * 
	 * @param token
	 * @param key
	 * @param value
	 */
	@SuppressWarnings("unchecked")
	public static void setAttr(String token, String key, Object value) {
		if ("user".equalsIgnoreCase(key) || "permissions".equals(key) || "roles".equals(key)) {
			return;
		}
		Map<String, Object> userinfo = CacheKit.get(CACHE_REGION, token, HashMap.class);
		userinfo.put(key, value);
		CacheKit.set(CACHE_REGION, token, userinfo);
	}
	
	/**
	 * 获得属性
	 * 
	 * @param token
	 * @param key
	 */
	@SuppressWarnings("unchecked")
	public static Object getAttr(String token, String key) {
		Map<String, Object> userinfo = CacheKit.get(CACHE_REGION, token, HashMap.class);
		if (userinfo == null) {
			return null;
		}
		return userinfo.get(key);
	}
	
	/**
	 * 删除属性
	 * 
	 * @param token
	 * @param key
	 */
	@SuppressWarnings("unchecked")
	public static void delAttr(String token, String key) {
		if ("user".equalsIgnoreCase(key) || "permissions".equals(key) || "roles".equals(key)) {
			return;
		}
		Map<String, Object> userinfo = CacheKit.get(CACHE_REGION, token, HashMap.class);
		userinfo.remove(key);
		CacheKit.set(CACHE_REGION, token, userinfo);
	}
	
	/**
	 * 退出系统
	 * 
	 * @param token
	 */
	@SuppressWarnings("unchecked")
	public static void logout(String token) {
		Map<String, Object> userinfo = CacheKit.get(CACHE_REGION, token, HashMap.class);
		if(userinfo == null){
			LOG.warn("CacheKit中region为userinfo，key为token的map对象（对象名为userinfo）为空");
			return;
		}
		Map<Long, List<String>> mapping = CacheKit.get(CACHE_REGION, MAPPING_QUEUE, HashMap.class);
		
		// 删除队列中的存在
		int index = -1;
		Long userId = ((User) userinfo.get("user")).getLong("id");
		List<String> tokens = mapping.get(userId);
		for (int i = 0; i < tokens.size(); i++) {
			String item = tokens.get(i);
			if (item.equals(token)) {
				index = i;
				break;
			}
		}
		if (index != -1) {
			tokens.remove(index);
		}
		CacheKit.set(CACHE_REGION, MAPPING_QUEUE, mapping);
		
		// 删除缓存的已登陆用户
		CacheKit.del(CACHE_REGION, token);
	}
	
	/**
	 * 获得用户
	 * 
	 * @param token
	 */
	public static User getUser(String token) {
		return (User) getAttr(token, "user");
	}
	
	/**
	 * 获得所有权限
	 * 
	 * @param token
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getPermissions(String token) {
		return (List<String>) getAttr(token, "permissions");
	}
	
	/**
	 * 获得某一用户的所有api权限编号
	 * 
	 * @param token
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getApiPermissions(String token) {
		return (List<String>) getAttr(token, "apiPermissionCodes");
	}
	
	/**
	 * 获得所有角色
	 * 
	 * @param token
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getRoles(String token) {
		return (List<String>) getAttr(token, "roles");
	}

	/**
	 * 获取学校编号
	 * @param token
	 * @return
	 */
	public static Long getSchoolId(String token) {
		Office school = getSchool(token);
		if (school == null) {
			return null;
		}
		return school.getLong("id");
	}
	
	/**
	 * 获取学校
	 * @param token
	 * @return
	 */
	public static Office getSchool(String token) {
		User user = getUser(token);
		OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
		Office school = officeService.getSchoolByUserId(user.getLong("id"));
		return school;
	}
	
	/**
	 * 获取专业编号
	 * @param token
	 * @return
	 */
	public static Long getMajorId(String token) {
		Office office = getMajor(token);
		if (office == null) {
			return null;
		}
		return office.getLong("id");
	}
	
	/**
	 * 获取专业
	 * @param token
	 * @return
	 */
	public static Office getMajor(String token) {
		User user = getUser(token);
		OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
		Office office = officeService.getMajorByUserId(user.getLong("id"));
		return office;
	}
	
	/**
	 * 获取用户直属部门
	 * 
	 * @param token
	 * @return
	 */
	public static Office getDepartmentOffice(String token) {
		User user = getUser(token);
		OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
		Office office = officeService.findByIdWithPath(Long.valueOf(user.getStr("department")));
		if (office == null) {
			LOG.error("用户编号为" + user.getLong("id") + "的直属部门不存在");
		}
		
		return office;
	}
	
}
