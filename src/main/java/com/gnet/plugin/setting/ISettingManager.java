package com.gnet.plugin.setting;

import java.io.Serializable;
import java.util.Map;

public interface ISettingManager {

	public boolean init();
	
	public <T extends Serializable> T get(String region, String key, Class<T> clazz);
	
	public <T extends Serializable> T get(String region, String key, T defaultValue, Class<T> clazz);
	
	public Map<String, String> getRegion(String region);
	
	public Map<String, Map<String, String>> getAll();
	
	public boolean put(String region, String key, Serializable value);
	
	public boolean remove(String region, String key);
	
	public boolean refresh();
	
}
