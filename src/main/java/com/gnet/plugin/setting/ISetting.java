package com.gnet.plugin.setting;

import java.io.Serializable;
import java.util.Map;

/**
 * 配置接口
 * 
 * @author xuq
 * @date 2015年12月11日
 * @version 1.0
 */
public abstract interface ISetting {
	
	/**
	 * 根据区域和key来获取值
	 * 
	 * @param region	区域（类型）
	 * @param key		key值
	 * @param clazz		获取的值类型（目前仅支持基本类型）
	 * @return 值
	 */
	<T> T get(String region, String key, Class<T> clazz);
	
	/**
	 * 获取该区域/类型配置
	 * 
	 * @return 该区域/类型的所有配置项
	 */
	Map<String, String> getRegion(String region);
	
	/**
	 * 获取所有配置
	 * 
	 * @return 所有配置项
	 */
	Map<String, Map<String, String>> getAll();
	
	/**
	 * 将key和value存储在region
	 * 
	 * @param region	区域（类型）
	 * @param key		key值
	 * @param value		value值
	 * @return 是否存储成功
	 */
	boolean put(String region, String key, Serializable value);
	
	/**
	 * 批量存储配置
	 * 
	 * @param region 区域（类型）
	 * @param configs 配置项
	 * @return 是否存储成功
	 */
	boolean putAll(Map<String, Map<String, String>> configs);
	
	/**
	 * 根据区域和key删除
	 * 
	 * @param region	区域（类型）
	 * @param key		key值
	 * @return			是否删除成功
	 */
	boolean remove(String region, String key);
	
	/**
	 * 根据区域清除
	 * 
	 * @param region 区域/类型
	 * @return 是否成功
	 */
	boolean clear(String region);
	
	/**
	 * 清除所有配置
	 * 
	 * @return 是否成功
	 */
	boolean clear();
	
}
