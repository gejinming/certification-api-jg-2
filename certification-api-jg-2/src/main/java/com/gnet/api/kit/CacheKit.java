package com.gnet.api.kit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;
import net.oschina.j2cache.J2Cache;


/**
 * Cache工具
 * 
 * @type utils
 * @description 这是一个对缓存的操作工具类，提供了设置缓存、删除缓存与获得缓存内容的操作
 * @author xuq
 * @date Jun 4, 2015
 * @version 1.0
 */
public class CacheKit {

	/**
	 * 保存值
	 * 
	 * @description 传入并保存字符串类型的区域、键以及任何类型的值
	 * @version 1.0
	 * @param region
	 * @param key
	 * @param value
	 */
	public static void set(String region, String key, Object value) {
		CacheChannel channel = J2Cache.getChannel();
		channel.set(region, key, value);
	}

	/**
	 * 获取值
	 * 
	 * @description 通过传入的区域与键获得值，并强制转型为目标类型
	 * @version 1.0
	 * @param region
	 * @param key
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(String region, String key, Class<T> clazz) {
		CacheChannel channel = J2Cache.getChannel();
		CacheObject obj = channel.get(region, key);

		if (obj.getValue() == null) {
			return null;
		}

		if (obj.getValue().getClass().equals(clazz)) {
			return (T) obj.getValue();
		} else {
			throw new RuntimeException("can't cast " + obj.getValue().getClass() + " to " + clazz);
		}
	}

	/**
	 * 获取该区域所有的值
	 * 
	 * @description 获取某个区域里所有缓存的键和值
	 * @version 1.0
	 * @param region
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<Object, Object> getMap(String region) {
		CacheChannel channel = J2Cache.getChannel();
		Map<Object, Object> result = new HashMap<Object, Object>();

		List<Object> keys = channel.keys(region);

		if (keys == null) {
			return result;
		}
		
		for (Object obj : keys) {
			String key = String.valueOf(obj);
			CacheObject value = channel.get(region, key);

			result.put(key, value.getValue());
		}

		return result;
	}
	
	/**
	 * 获取该区域所有的值
	 * 
	 * @description 获取某个区域里所有缓存的键和值
	 * @version 1.0
	 * @param region
	 * @param start 从0开始
	 * @param end
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> get(String region) {
		CacheChannel channel = J2Cache.getChannel();
		List<Object> result = new ArrayList<Object>();

		List<Object> keys = channel.keys(region);

		if (keys == null) {
			return result;
		}
		
		for (Object obj : keys) {
			String key = String.valueOf(obj);
			CacheObject value = channel.get(region, key);

			result.add(value.getValue());
		}

		return result;
	}

	/**
	 * 删除值
	 * 
	 * @description 根据区域和键删除某个缓存
	 * @version 1.0
	 * @param region
	 * @param key
	 */
	public static void del(String region, String key) {
		CacheChannel channel = J2Cache.getChannel();
		channel.evict(region, key);
	}

}
