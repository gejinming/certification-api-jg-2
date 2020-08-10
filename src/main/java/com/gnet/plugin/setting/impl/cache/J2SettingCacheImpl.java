package com.gnet.plugin.setting.impl.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gnet.plugin.setting.exceptions.StrParseErrorException;
import com.gnet.plugin.setting.impl.db.JFinalSettingDbImpl;
import com.gnet.plugin.setting.kit.StrParserKit;
import com.gnet.plugin.setting.kit.TypeCheckKit;
import com.jfinal.log.Logger;

import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;
import net.oschina.j2cache.J2Cache;

/**
 * J2Cache缓存存储配置实现
 * 
 * @author xuq
 * @date 2015年12月14日
 * @version 1.0
 */
public class J2SettingCacheImpl extends SettingCacheImpl {
	
	private static final Logger logger = Logger.getLogger(JFinalSettingDbImpl.class);
	
	public J2SettingCacheImpl() {
		this(true, null);
	}
	
	public J2SettingCacheImpl(Long userId) {
		this(false, userId);
	}
	
	public J2SettingCacheImpl(Boolean isGlobal, Long userId) {
		super(isGlobal, userId);
	}
	
	@Override
	public <T> T get(String region, String key, Class<T> clazz) {
		CacheChannel channel = J2Cache.getChannel();
		CacheObject obj = channel.get(REGION, getKey(region, key));
		if (obj.getValue() == null) {
			return null;
		}
		
		try {
			return StrParserKit.parse(String.valueOf(obj.getValue()), clazz);
		} catch (StrParseErrorException e) {
			if (logger.isErrorEnabled()) {
				logger.error("无法获得对象数据，类型转化错误", e);
			}
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getRegion(String region) {
		Map<String, String> configs = new HashMap<String, String>();
		CacheChannel channel = J2Cache.getChannel();
		List<Object> list = channel.keys(REGION);
		
		if (list == null) {
			return configs;
		}
		
		for (Object obj : list) {
			String key = String.valueOf(obj);
			CacheObject value = channel.get(REGION, key);
			
			String[] rawKeys = key.split(REGION_SPLIT);
			if (isGlobal()) {
				if (rawKeys.length == 2 && key.startsWith(region + REGION_SPLIT)) {
					if (value.getValue() == null) {
						configs.put(rawKeys[1], null);
					} else {
						configs.put(rawKeys[1], String.valueOf(value.getValue()));
					}
				}
			} else {
				if (rawKeys.length == 3 && key.startsWith(region + REGION_SPLIT + getUserId() + REGION_SPLIT)) {
					if (value.getValue() == null) {
						configs.put(rawKeys[2], null);
					} else {
						configs.put(rawKeys[2], String.valueOf(value.getValue()));
					}
				}
			}
		}
		return configs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Map<String, String>> getAll() {
		Map<String, Map<String, String>> configs = new HashMap<String, Map<String, String>>();
		
		CacheChannel channel = J2Cache.getChannel();
		List<Object> list = channel.keys(REGION);
		
		if (list == null) {
			return configs;
		}
		
		for (Object obj : list) {
			String key = String.valueOf(obj);
			CacheObject value = channel.get(REGION, key);
			
			String[] rawKeys = key.split(REGION_SPLIT);
			if (isGlobal()) {
				if (rawKeys.length == 2) {
					Map<String, String> regionConfigs = configs.get(rawKeys[0]);
					if (regionConfigs == null) {
						regionConfigs = new HashMap<String, String>();
						configs.put(rawKeys[0], regionConfigs);
					}
					
					if (value.getValue() == null) {
						regionConfigs.put(rawKeys[1], null);
					} else {
						regionConfigs.put(rawKeys[1], String.valueOf(value.getValue()));
					}
				}
			} else {
				if (rawKeys.length == 3) {
					Map<String, String> regionConfigs = configs.get(rawKeys[0]);
					if (regionConfigs == null) {
						regionConfigs = new HashMap<String, String>();
						configs.put(rawKeys[0], regionConfigs);
					}
					
					if (value.getValue() == null) {
						regionConfigs.put(rawKeys[2], null);
					} else {
						regionConfigs.put(rawKeys[2], String.valueOf(value.getValue()));
					}
				}
			}
		}
		return configs;
	}

	@Override
	public boolean put(String region, String key, Serializable value) {
		// check
		TypeCheckKit.check(value);
		
		CacheChannel channel = J2Cache.getChannel();
		channel.set(REGION, getKey(region, key), value);
		return true;
	}
	
	@Override
	public boolean putAll(Map<String, Map<String, String>> configs) {
		CacheChannel channel = J2Cache.getChannel();
		
		Iterator<Map.Entry<String, Map<String, String>>> iter = configs.entrySet().iterator();
		while(iter.hasNext()) {
			Map.Entry<String, Map<String, String>> entry = iter.next();
			String region = entry.getKey();
			Map<String, String> regionConfigs = entry.getValue();
			
			Iterator<Map.Entry<String, String>> regionIter = regionConfigs.entrySet().iterator();
			while(regionIter.hasNext()) {
				Map.Entry<String, String> regionItem = regionIter.next();
				
				channel.set(REGION, getKey(region, regionItem.getKey()), regionItem.getValue());
			}
			
		}
		return true;
	}

	@Override
	public boolean remove(String region, String key) {
		CacheChannel channel = J2Cache.getChannel();
		channel.evict(REGION, getKey(region, key));
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean clear(String region) {
		CacheChannel channel = J2Cache.getChannel();
		List<String> keys = channel.keys(REGION);
		
		List<String> batchKeys = new ArrayList<String>();
		for (String key : keys) {
			String[] rawKeys = key.split(REGION_SPLIT);
			if (isGlobal()) {
				if (key.startsWith(region + REGION_SPLIT) && rawKeys.length == 2) {
					batchKeys.add(key);
				}
			} else {
				if (key.startsWith(region + REGION_SPLIT + getUserId() + REGION_SPLIT) && rawKeys.length == 3) {
					batchKeys.add(key);
				}
			}
		}
		
		channel.batchEvict(REGION, batchKeys);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean clear() {
		CacheChannel channel = J2Cache.getChannel();
		List<String> keys = channel.keys(REGION);
		
		List<String> batchKeys = new ArrayList<String>();
		for (String key : keys) {
			String[] rawKeys = key.split(REGION_SPLIT);
			if (isGlobal()) {
				if (key.contains(REGION_SPLIT) && rawKeys.length == 2) {
					batchKeys.add(key);
				}
			} else {
				if (key.contains(REGION_SPLIT + getUserId() + REGION_SPLIT) && rawKeys.length == 3) {
					batchKeys.add(key);
				}
			}
		}
		
		channel.batchEvict(REGION, batchKeys);
		return true;
	}

}
