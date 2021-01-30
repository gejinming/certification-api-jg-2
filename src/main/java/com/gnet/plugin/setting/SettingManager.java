package com.gnet.plugin.setting;

import java.io.Serializable;
import java.util.Map;

import com.gnet.plugin.setting.impl.cache.J2SettingCacheImpl;
import com.gnet.plugin.setting.impl.db.JFinalSettingDbImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.STAngle;

/**
 * 配置管理类
 * 
 * @author xuq
 * @date 2015年12月14日
 * @version 1.1
 * Fix-Bug: @v1.0-get(String region, String key, T defaultValue, Class<T> clazz): 从数据库获取发现cache中为空，put的时候直接put特定T对象，而不是String.valueOf()。
 */
class SettingManager implements ISettingManager {
	
	private ISetting dbSetting;
	private ISetting cacheSetting;
	
	/**
	 * 全局配置
	 */
	SettingManager() {
		this.dbSetting = new JFinalSettingDbImpl();
		this.cacheSetting = new J2SettingCacheImpl();
	}

	/**
	 * 用户配置
	 * 
	 * @param userId
	 */
	SettingManager(Long userId) {
		if (userId == null) {
			throw new RuntimeException("用户编号不得为空");
		}
		this.dbSetting = new JFinalSettingDbImpl(userId);
		this.cacheSetting = new J2SettingCacheImpl(userId);
	}
	
	/**
	 * 初始化配置
	 */
	@Override
	public boolean init() {
		Map<String, Map<String, String>> configs = dbSetting.getAll();
		return cacheSetting.putAll(configs);
	}
	
	/**
	 * 获取配置
	 * 
	 * @param region
	 * @param key
	 * @param clazz
	 * @return
	 */
	@Override
	public <T extends Serializable> T get(String region, String key, Class<T> clazz) {
		return get(region, key, null, clazz);
	}
	
	/**
	 * 获取配置
	 * 
	 * @param region
	 * @param key
	 * @param defaultValue
	 * @param clazz
	 * @return
	 */
	@Override
	public <T extends Serializable> T get(String region, String key, T defaultValue, Class<T> clazz) {
		T t = cacheSetting.get(region, key, clazz);
		if (t == null) {
			// 从数据库获取
			t = dbSetting.get(region, key, clazz);
			if (t == null) {
				// 不存在，则返回null
				return defaultValue;
			} else {
				// 如果存在则将数据重新缓存到缓存级别	
				cacheSetting.put(region, key, t);
			}
		}
		return t;
	}
	
	/**
	 * 获取区域/类型下的所有参数配置
	 * 
	 * @param region
	 * @return
	 */
	@Override
	public Map<String, String> getRegion(String region) {
		return cacheSetting.getRegion(region);
	}
	
	/**
	 * 获取所有的配置项
	 */
	@Override
	public Map<String, Map<String, String>> getAll() {
		return cacheSetting.getAll();
	}
	
	/**
	 * 存储配置
	 * 
	 * @param region
	 * @param key
	 * @param value
	 * @return
	 */
	@Override
	public boolean put(String region, String key, Serializable value) {
		cacheSetting.put(region, key, value);
		try {
			if (!dbSetting.put(region, key, value)) {
				// 如果数据库存储失败，则删除缓存级别上的存储
				cacheSetting.remove(region, key);
				return false;
			}
		} catch (Throwable e) {
			// 如果数据库存储失败，则删除缓存级别上的存储
			cacheSetting.remove(region, key);
			return false;
		}
		return true;
	}
	
	/**
	 * 删除
	 * 
	 * @param region
	 * @param key
	 * @return
	 */
	@Override
	public boolean remove(String region, String key) {
		if (!dbSetting.remove(region, key)) {
			return false;
		}
		if (!cacheSetting.remove(region, key)) {
			// 尝试删除，如果失败，则直接清空该区域/种类的缓存
			cacheSetting.clear(region);
		}
		return true;
	}

	@Override
	public boolean refresh() {
		if (cacheSetting.clear()) {
			Map<String, Map<String, String>> configs = dbSetting.getAll();
			return cacheSetting.putAll(configs);
		}
		return false;
	}
	
}
