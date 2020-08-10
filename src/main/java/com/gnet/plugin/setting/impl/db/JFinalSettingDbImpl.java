package com.gnet.plugin.setting.impl.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.gnet.plugin.id.IdGenerate;
import com.gnet.plugin.setting.exceptions.StrParseErrorException;
import com.gnet.plugin.setting.kit.StrParserKit;
import com.gnet.plugin.setting.kit.TypeCheckKit;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * JFinal配置数据库实现
 * 
 * @author xuq
 * @date 2015年12月11日
 * @version 1.0
 */
@Transactional(readOnly = true)
public class JFinalSettingDbImpl extends SettingDbImpl {
	
	private static final Logger logger = Logger.getLogger(JFinalSettingDbImpl.class);

	private IdGenerate 	idGenerate;		// ID生成器
	
	public JFinalSettingDbImpl() {
		this(true, null);
	}
	
	public JFinalSettingDbImpl(Long userId) {
		this(false, userId);
	}
	
	private JFinalSettingDbImpl(Boolean isGlobal, Long userId) {
		super(isGlobal, userId);
		
		idGenerate = SpringContextHolder.getBean(IdGenerate.class);
	}

	@Override
	public <T> T get(String region, String key, Class<T> clazz) {
		try {
			StringBuilder sql = new StringBuilder("select " + COLUMN_SVALUE + " from " + getTableName() + " where " + COLUMN_REGION + "=? and " + COLUMN_SKEY + "=? ");
			if (isGlobal()) {
				String value = Db.queryStr(sql.toString(), region, key);
				return StrParserKit.parse(value, clazz);
			} else {
				sql.append("and " + COLUMN_USER_ID + "=? ");
				String value = Db.queryStr(sql.toString(), region, key, getUserId());
				return StrParserKit.parse(value, clazz);
			}
		} catch (StrParseErrorException e) {
			if (logger.isErrorEnabled()) {
				logger.error("无法获得对象数据，类型转化错误", e);
			}
			return null;
		}
	}
	
	@Override
	public Map<String, String> getRegion(String region) {
		Map<String, String> configs = new HashMap<String, String>();
		
		StringBuilder sql = new StringBuilder("select * from " + getTableName() + " where " + COLUMN_REGION + "=? ");
		if (isGlobal()) {
			List<Record> records = Db.find(sql.toString(), region);
			for (Record record : records) {
				configs.put(record.getStr(COLUMN_SKEY), record.getStr(COLUMN_SVALUE));
			}
		} else {
			sql.append("and " + COLUMN_USER_ID + "=? ");
			List<Record> records = Db.find(sql.toString(), region, getUserId());
			for (Record record : records) {
				configs.put(record.getStr(COLUMN_SKEY), record.getStr(COLUMN_SVALUE));
			}
		}
		return configs;
	}
	
	@Override
	public Map<String, Map<String, String>> getAll() {
		Map<String, Map<String, String>> configs = new HashMap<String, Map<String,String>>();
		
		StringBuilder sql = new StringBuilder("select * from " + getTableName() + " ");
		if (isGlobal()) {
			List<Record> records = Db.find(sql.toString());
			for (Record record : records) {
				Map<String, String> regionConfigs = configs.get(record.getStr(COLUMN_REGION));
				if (regionConfigs == null) {
					regionConfigs = new HashMap<String, String>();
					configs.put(record.getStr(COLUMN_REGION), regionConfigs);
				}
				
				regionConfigs.put(record.getStr(COLUMN_SKEY), record.getStr(COLUMN_SVALUE));
			}
		} else {
			sql.append("where " + COLUMN_USER_ID + "=? ");
			List<Record> records = Db.find(sql.toString(), getUserId());
			for (Record record : records) {
				Map<String, String> regionConfigs = configs.get(record.getStr(COLUMN_REGION));
				if (regionConfigs == null) {
					regionConfigs = new HashMap<String, String>();
					configs.put(record.getStr(COLUMN_REGION), regionConfigs);
				}
				
				regionConfigs.put(record.getStr(COLUMN_SKEY), record.getStr(COLUMN_SVALUE));
			}
		}
		return configs;
	}
	
	@Transactional(readOnly = false)
	@Override
	public boolean put(String region, String key, Serializable value) {
		// check
		TypeCheckKit.check(value);
		
		// validate is existed
		StringBuilder sql = new StringBuilder("select * from " + getTableName() + " where " + COLUMN_REGION + "=? and " + COLUMN_SKEY + "=? ");
		Record record;
		if (isGlobal()) {
			record = Db.findFirst(sql.toString(), region, key);
		} else {
			sql.append("and " + COLUMN_USER_ID + "=? ");
			record = Db.findFirst(sql.toString(), region, key, getUserId());
		}
		if (record == null) {
			// there is no same region and key.
			Date date = new Date();
			
			record = new Record();
			record.set(COLUMN_ID, idGenerate.getNextValue());
			record.set(COLUMN_CREATE_DATE, date);
			record.set(COLUMN_MODIFY_DATE, date);
			record.set(COLUMN_SKEY, key);
			record.set(COLUMN_SVALUE, String.valueOf(value));
			record.set(COLUMN_REGION, region);
			
			if (!isGlobal()) {
				record.set(COLUMN_USER_ID, getUserId());
			}
			
			return Db.save(getTableName(), COLUMN_ID, record);
		} else {
			record.set(COLUMN_SVALUE, String.valueOf(value));
			
			return Db.update(getTableName(), COLUMN_ID, record);
		}
		
	}
	
	@Transactional(readOnly = false)
	@Override
	public boolean putAll(Map<String, Map<String, String>> configs) {
		Date date = new Date();
		
		List<String> keyIndex = new ArrayList<String>();
		List<Record> records = new ArrayList<Record>();
		Iterator<Map.Entry<String, Map<String, String>>> iter = configs.entrySet().iterator();
		while(iter.hasNext()) {
			Map.Entry<String, Map<String, String>> entry = iter.next();
			String region = entry.getKey();
			Map<String, String> regionConfigs = entry.getValue();
			
			Iterator<Map.Entry<String, String>> regionIter = regionConfigs.entrySet().iterator();
			while(regionIter.hasNext()) {
				Map.Entry<String, String> regionItem = regionIter.next();
				
				Record record = new Record();
				record.set(COLUMN_ID, idGenerate.getNextValue());
				record.set(COLUMN_CREATE_DATE, date);
				record.set(COLUMN_MODIFY_DATE, date);
				record.set(COLUMN_SKEY, regionItem.getKey());
				record.set(COLUMN_SVALUE, regionItem.getValue());
				record.set(COLUMN_REGION, region);
				
				if (isGlobal()) {
					record.set(COLUMN_ID, getUserId());
				}
				
				records.add(record);
				keyIndex.add(region + "-" + regionItem.getKey());
			}
		}
		
		// 排除过滤
		List<Record> updateList = new ArrayList<Record>();
		List<Record> all = Db.find("select " + COLUMN_REGION + "," + COLUMN_SKEY + " from " + getTableName());
		for (int i = 0; i < all.size(); i++) {
			Record item = all.get(i);
			String key = item.getStr(COLUMN_REGION) + "-" + item.getStr(COLUMN_SKEY);
			if (keyIndex.indexOf(key) != -1) {
				Record record = records.get(i);
				record.set(COLUMN_ID, item.getLong(COLUMN_ID));
				record.set(COLUMN_MODIFY_DATE, item.getDate(COLUMN_MODIFY_DATE));
				updateList.add(record);
			}
		}
		if (updateList.size() > 0) {
			records.removeAll(updateList);
			
			Object[][] paras = new Object[updateList.size()][];
			for (int i = 0; i < updateList.size(); i++) {
				Record update = updateList.get(i);
				paras[i] = new Object[]{update.getDate(COLUMN_MODIFY_DATE), update.getStr(COLUMN_SVALUE), update.getLong(COLUMN_ID)};
			}
			
			Db.batch("update " + getTableName() + " set " + COLUMN_MODIFY_DATE + "=?," + COLUMN_SVALUE + "=? where " + COLUMN_ID + "=? ", paras);
		}
		
		StringBuilder sql = new StringBuilder("insert into " + getTableName() + " (" + COLUMN_ID + "," + COLUMN_CREATE_DATE + "," + COLUMN_MODIFY_DATE + "," + COLUMN_SKEY + "," + COLUMN_SVALUE + "," + COLUMN_REGION + ") values(?,?,?,?,?,?)");
		Db.batch(sql.toString(), "id,create_date,modify_date,skey,svalue,region", records);
		return true;
	}
	
	@Transactional(readOnly = false)
	@Override
	public boolean remove(String region, String key) {
		StringBuilder sql = new StringBuilder("delete from " + getTableName() + " where " + COLUMN_REGION + "=? and " + COLUMN_SKEY + "=? ");
		if (isGlobal()) {
			return Db.update(sql.toString(), region, key) >= 0;
		} else {
			sql.append("and " + COLUMN_USER_ID + "=? ");
			return Db.update(sql.toString(), region, key, getUserId()) >= 0;
		}
	}

	@Transactional(readOnly = false)
	@Override
	public boolean clear(String region) {
		StringBuilder sql = new StringBuilder("delete from " + getTableName() + " where " + COLUMN_REGION + "=? ");
		if (isGlobal()) {
			return Db.update(sql.toString(), region) >= 0;
		} else {
			sql.append("and " + COLUMN_USER_ID + "=? ");
			return Db.update(sql.toString(), region, getUserId()) >= 0;
		}
	}

	@Transactional(readOnly = false)
	@Override
	public boolean clear() {
		StringBuilder sql = new StringBuilder("delete from " + getTableName() + " ");
		if (isGlobal()) {
			return Db.update(sql.toString()) >= 0;
		} else {
			sql.append("where " + COLUMN_USER_ID + "=? ");
			return Db.update(sql.toString(), getUserId()) >= 0;
		}
	}

}
