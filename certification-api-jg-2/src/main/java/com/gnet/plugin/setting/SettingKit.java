package com.gnet.plugin.setting;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;

/**
 * 配置工具类
 * 
 * @author xuq
 * @date 2015年12月16日
 * @version 1.1
 * Modify: @v1.1 返回get为T extends Serializable
 */
public class SettingKit {
	
	/**
	 * 全局初始化
	 */
	public static boolean init() {
		SettingManager settingManager = new SettingManager();
		return settingManager.init();
	}
	
	/**
	 * 全局初始化
	 */
	public static boolean refresh() {
		SettingManager settingManager = new SettingManager();
		return settingManager.refresh();
	}
	
	/**
	 * 用户初始化
	 */
	public static boolean init(Long userId) {
		SettingManager settingManager = new SettingManager(userId);
		return settingManager.init();
	}
	
	/**
	 * 用户配置刷新
	 */
	public static boolean refresh(Long userId) {
		SettingManager settingManager = new SettingManager(userId);
		return settingManager.refresh();
	}

	/**
	 * 获取全局配置
	 */
	public static <T extends Serializable> T get(String region, String key, Class<T> clazz) {
		return get(region, key, null, clazz);
	}
	
	/**
	 * 获取全局配置
	 */
	public static <T extends Serializable> T get(String region, String key, T defaultValue, Class<T> clazz) {
		SettingManager settingManager = new SettingManager();
		return settingManager.get(region, key, defaultValue, clazz);
	}
	
	/**
	 * 获取用户配置
	 */
	public static <T extends Serializable> T get(Long userId, String region, String key, Class<T> clazz) {
		return get(userId, region, key, null, clazz);
	}
	
	/**
	 * 获取用户配置
	 */
	public static <T extends Serializable> T get(Long userId, String region, String key, T defaultValue, Class<T> clazz) {
		SettingManager settingManager = new SettingManager(userId);
		T t = settingManager.get(region, key, defaultValue, clazz);
		if (t == null) {
			SettingManager globalSettingManager = new SettingManager();
			return globalSettingManager.get(region, key, defaultValue, clazz);
		} else {
			return t;
		}
	}
	
	/**
	 * 从全局配置获取字符串
	 * 
	 * @see SettingKit#get(String, String, Object, Class)
	 */
	public static String getStr(String region, String key) {
		return getStr(region, key, null);
	}
	
	/**
	 * 从全局配置获取字符串
	 * 
	 * @see SettingKit#get(String, String, Object, Class)
	 */
	public static String getStr(String region, String key, String defaultValue) {
		return get(region, key, defaultValue, String.class);
	}
	
	/**
	 * 从用户配置获取字符串
	 * 
	 * @see SettingKit#get(Long, String, String, Object, Class)
	 */
	public static String getStr(Long userId, String region, String key) {
		return get(userId, region, key, null, String.class);
	}
	
	/**
	 * 从用户配置获取字符串
	 * 
	 * @see SettingKit#get(Long, String, String, Object, Class)
	 */
	public static String getStr(Long userId, String region, String key, String defaultValue) {
		return get(userId, region, key, defaultValue, String.class);
	}
	
	/**
	 * 从全局配置获取整型
	 * 
	 * @see SettingKit#get(String, String, Object, Class)
	 */
	public static Integer getInt(String region, String key) {
		return getInt(region, key, null);
	}
	
	/**
	 * 从全局配置获取整型
	 * 
	 * @see SettingKit#get(String, String, Object, Class)
	 */
	public static Integer getInt(String region, String key, Integer defaultValue) {
		return get(region, key, defaultValue, Integer.class);
	}
	
	/**
	 * 从用户配置获取整型
	 * 
	 * @see SettingKit#get(Long, String, String, Object, Class)
	 */
	public static Integer getInt(Long userId, String region, String key) {
		return get(userId, region, key, null, Integer.class);
	}
	
	/**
	 * 从用户配置获取整型
	 * 
	 * @see SettingKit#get(Long, String, String, Object, Class)
	 */
	public static Integer getInt(Long userId, String region, String key, Integer defaultValue) {
		return get(userId, region, key, defaultValue, Integer.class);
	}
	
	/**
	 * 从全局配置获取长整型
	 * 
	 * @see SettingKit#get(String, String, Object, Class)
	 */
	public static Long getLong(String region, String key) {
		return getLong(region, key, null);
	}
	
	/**
	 * 从全局配置获取长整型
	 * 
	 * @see SettingKit#get(String, String, Object, Class)
	 */
	public static Long getLong(String region, String key, Long defaultValue) {
		return get(region, key, defaultValue, Long.class);
	}
	
	/**
	 * 从用户配置获取长整型
	 * 
	 * @see SettingKit#get(Long, String, String, Object, Class)
	 */
	public static Long getLong(Long userId, String region, String key) {
		return get(userId, region, key, null, Long.class);
	}
	
	/**
	 * 从用户配置获取长整型
	 * 
	 * @see SettingKit#get(Long, String, String, Object, Class)
	 */
	public static Long getLong(Long userId, String region, String key, Long defaultValue) {
		return get(userId, region, key, defaultValue, Long.class);
	}
	
	/**
	 * 从全局配置获取BigDecimal
	 * 
	 * @see SettingKit#get(String, String, Object, Class)
	 */
	public static BigDecimal getBigDecimal(String region, String key) {
		return getBigDecimal(region, key, null);
	}
	
	/**
	 * 从全局配置获取BigDecimal
	 * 
	 * @see SettingKit#get(String, String, Object, Class)
	 */
	public static BigDecimal getBigDecimal(String region, String key, BigDecimal defaultValue) {
		return get(region, key, defaultValue, BigDecimal.class);
	}
	
	/**
	 * 从用户配置获取BigDecimal
	 * 
	 * @see SettingKit#get(Long, String, String, Object, Class)
	 */
	public static BigDecimal getBigDecimal(Long userId, String region, String key) {
		return getBigDecimal(userId, region, key, null);
	}
	
	/**
	 * 从用户配置获取BigDecimal
	 * 
	 * @see SettingKit#get(Long, String, String, Object, Class)
	 */
	public static BigDecimal getBigDecimal(Long userId, String region, String key, BigDecimal defaultValue) {
		return get(userId, region, key, defaultValue, BigDecimal.class);
	}
	
	/**
	 * 从全局配置获取布尔类型
	 * 
	 * @see SettingKit#get(String, String, Object, Class)
	 */
	public static Boolean getBoolean(String region, String key) {
		return getBoolean(region, key, null);
	}
	
	/**
	 * 从全局配置获取布尔类型
	 * 
	 * @see SettingManager#get(String, String, Object, Class)
	 */
	public static Boolean getBoolean(String region, String key, Boolean defaultValue) {
		return get(region, key, defaultValue, Boolean.class);
	}
	
	/**
	 * 从用户配置获取布尔类型
	 * 
	 * @see SettingKit#get(Long, String, String, Object, Class)
	 */
	public static Boolean getBoolean(Long userId, String region, String key) {
		return getBoolean(userId, region, key, null);
	}
	
	/**
	 * 从用户配置获取布尔类型
	 * 
	 * @see SettingManager#get(Long, String, String, Object, Class)
	 */
	public static Boolean getBoolean(Long userId, String region, String key, Boolean defaultValue) {
		return get(userId, region, key, defaultValue, Boolean.class);
	}
	
	/**
	 * 根据区域获取全局配置
	 */
	public static Map<String, String> getRegion(String region) {
		SettingManager settingManager = new SettingManager();
		return settingManager.getRegion(region);
	}
	
	/**
	 * 根据区域获取用户配置（无法保证获取到的配置是所有配置，但能保证获取的值为最新值）
	 */
	public static Map<String, String> getRegion(Long userId, String region) {
		SettingManager settingManager = new SettingManager(userId);
		return settingManager.getRegion(region);
	}
	
	/**
	 * 获取所有全局配置
	 */
	public static Map<String, Map<String, String>> getAll() {
		SettingManager settingManager = new SettingManager();
		return settingManager.getAll();
	}
	
	/**
	 * 获取所有用户配置（覆盖全局配置）
	 */
	public static Map<String, Map<String, String>> getAll(Long userId) {
		SettingManager settingManager = new SettingManager();
		SettingManager personal = new SettingManager(userId);
		
		Map<String, Map<String, String>> configs = settingManager.getAll();
		Iterator<Map.Entry<String, Map<String, String>>> configsIter = configs.entrySet().iterator();
		while(configsIter.hasNext()) {
			Map.Entry<String, Map<String, String>> entry = configsIter.next();
			String region = entry.getKey();
			Map<String, String> regionConfigs = entry.getValue();
			
			Iterator<Map.Entry<String, String>> regionConfigsIter = regionConfigs.entrySet().iterator();
			while(regionConfigsIter.hasNext()) {
				Map.Entry<String, String> regionEntry = regionConfigsIter.next();
				
				String value = personal.get(region, regionEntry.getKey(), String.class);
				if (value != null) {
					regionConfigs.put(regionEntry.getKey(), value);
				}
			}
		}
		
		return configs;
	}
	
	/**
	 * 全局存值
	 */
	public static boolean put(String region, String key, Serializable value) {
		SettingManager settingManager = new SettingManager();
		return settingManager.put(region, key, value);
	}
	
	/**
	 * 用户存值（必须保证全局配置项存在一个相同的配置，用户配置用来覆盖全局配置，而不是存在独立于全局配置的配置）
	 */
	public static boolean put(Long userId, String region, String key, Serializable value) {
		SettingManager settingManager = new SettingManager(userId);
		SettingManager global = new SettingManager();
		if (global.get(region, key, value.getClass()) == null) {
			// 全局配置无该项配置，直接反馈失败
			return false;
		}
		return settingManager.put(region, key, value);
	}
	
	/**
	 * 删除全局配置
	 */
	public static boolean remove(String region, String key) {
		SettingManager settingManager = new SettingManager();
		return settingManager.remove(region, key);
	}
	
	/**
	 * 删除用户配置
	 */
	public static boolean remove(Long userId, String region, String key) {
		SettingManager settingManager = new SettingManager(userId);
		return settingManager.remove(region, key);
	}
	
}
