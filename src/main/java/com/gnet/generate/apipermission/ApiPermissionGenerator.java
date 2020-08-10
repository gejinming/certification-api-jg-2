package com.gnet.generate.apipermission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.esotericsoftware.minlog.Log;
import com.gnet.exception.CellTypeValidationException;
import com.gnet.model.admin.ApiPermission;
import com.gnet.model.admin.Permission;
import com.gnet.model.admin.PermissionHaveApi;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.plugin.poi.ExcelColumn;
import com.gnet.plugin.poi.ExcelField;
import com.gnet.plugin.poi.ExcelHead;
import com.gnet.plugin.poi.ExcelHelper;
import com.gnet.plugin.poi.ExcelModel;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.jfinal.aop.Before;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlReporter;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.druid.DruidPlugin;

import jodd.util.PropertiesUtil;

public class ApiPermissionGenerator {
	
	private static final Logger LOG = Logger.getLogger(ApiPermissionGenerator.class);
	
	private static IdGenerate idGenerate = null;
	
	private static ExcelHelper excelHelper = new ExcelHelper();
	
	// 开关：开启
	private static final boolean ON = Boolean.TRUE;
	
	// 开关：关闭
	private static final boolean OFF = Boolean.FALSE;
	
	public static void main(String[] args) {
		
		// 更新功能名称和组名称开关
		boolean updatePermissionNameAndPnameTurn = ON;
		
		/****************
		 *     配置结束           *
		 ****************/
		
		
		init();
		List<Record> records = Lists.newArrayList();
		readExcel(records, PathKit.getRootClassPath() + "/excel/permissions.xls");
		
		// 检查接口权限是否都在表中，若不在添加
		if (!scannerClassCheckInDB(PathKit.getRootClassPath() + "/com/gnet/certification/")) {
			LOG.error("接口权限检查添加失败");
			return;
		}
		
		// 检查功能权限是否都在表中，若不在添加
		if (!checkPermissionInDB(records, updatePermissionNameAndPnameTurn)) {
			LOG.error("功能权限检查添加失败");
			return;
		}
		
		// 检查功能权限与接口权限关联是否在表中，若不在添加
		if (!checkAssocationPermissionInDB(records)) {
			LOG.error("权限关联添加失败");
		}
	}
	
	
	
	private static void init() {
		Properties p = getProperties("db.properties");
		DruidPlugin druidPlugin = new DruidPlugin(p.getProperty("db.mysql.url"), p.getProperty("db.mysql.username"), p.getProperty("db.mysql.password"));
		ActiveRecordPlugin activeRecordPlugin = new ActiveRecordPlugin(druidPlugin);
		activeRecordPlugin.setDialect(new MysqlDialect());
		activeRecordPlugin.setShowSql(true);
		
		activeRecordPlugin.addMapping("sys_permission", Permission.class);
		activeRecordPlugin.addMapping("sys_api_permission", ApiPermission.class);
		activeRecordPlugin.addMapping("sys_permission_have_api", PermissionHaveApi.class);
		
		druidPlugin.start();
		activeRecordPlugin.start();
		// sql记录
		SqlReporter.setLogger(true, 3);
		
		idGenerate = new IdGenerate(druidPlugin.getDataSource());
		
		ExcelHead head = new ExcelHead();
		ExcelModel excelModel = ApiPermissionExcel.class.getAnnotation(ExcelModel.class);
		Field[] fields = ApiPermissionExcel.class.getDeclaredFields();
		List<ExcelColumn> excelColumns = new ArrayList<ExcelColumn>();

		Map<String, Map<Object, Object>> convertMap = new ConcurrentHashMap<String, Map<Object, Object>>();
		Map<String, Map<Object, Object>> reConvertMap = new ConcurrentHashMap<String, Map<Object, Object>>();
		for (Field field : fields) {
			ExcelField excelField = field.getAnnotation(ExcelField.class);
			String index = excelField.index();
			String fieldName = excelField.fieldName();
			String title = excelField.title();
			String[] converts = excelField.convert();
			@SuppressWarnings("deprecation")
			String shareColumnCount = excelField.shareColumnCount();
			String shareColumnTarget = excelField.shareColumnTarget();
			Class<?> fieldType = field.getType();

			ExcelColumn excelColumn = new ExcelColumn(Integer.valueOf(index), fieldName, title, fieldType, Integer.valueOf(shareColumnCount));
			if (StringUtils.isNotBlank(shareColumnTarget)) {
				excelColumn.setShareColumnTarget(shareColumnTarget);
			}
			excelColumns.add(excelColumn);

			Map<Object, Object> map = new ConcurrentHashMap<Object, Object>();
			Map<Object, Object> reMap = new ConcurrentHashMap<Object, Object>();
			for (String convert : converts) {
				String[] entry = convert.split(":");
				map.put(entry[0], entry[1]);
				reMap.put(entry[1], entry[0]);
			}
			if (!map.isEmpty()) {
				convertMap.put(fieldName, map);
				reConvertMap.put(fieldName, reMap);
			}
		}
		head.setColumnCount(Integer.valueOf(excelModel.colsCount()));
		head.setRowCount(Integer.valueOf(excelModel.rowCount()));
		head.setColumns(excelColumns);
		head.setColumnsConvertMap(convertMap);
		head.setReColumnsConvertMap(reConvertMap);
		head.setTargetSheetIndex(Integer.valueOf(excelModel.targetSheetIndex()));
		ExcelHelper.mappings.put(excelModel.name(), head);
	}
	
	@Before(Tx.class)
	private static boolean scannerClassCheckInDB(String fileDir) {
		List<ApiPermission> apiPermissions = ApiPermission.dao.findAll();
		HashSet<String> codes = Sets.newHashSet();
		for (ApiPermission apiPermission : apiPermissions) {
			codes.add(apiPermission.getStr("code"));
		}
		
		Date date = new Date();
		List<ApiPermission> addList = Lists.newArrayList();
		
		File baseDir = new File(fileDir);
		if (!baseDir.exists() || !baseDir.isDirectory()) {
			return false;
		}
		File[] files = baseDir.listFiles();
		StringBuilder info = new StringBuilder();
		for (File file : files) {
			if (file.getName().indexOf("$") > -1) {
				continue;
			}
			String filename = file.getName().substring(0, file.getName().indexOf("."));
			if(!codes.contains(filename)) {
				ApiPermission apiPermission = new ApiPermission();
				apiPermission.set("code", filename);
				apiPermission.set("name", "暂无");
				apiPermission.set("is_system", Boolean.TRUE);
				apiPermission.set("create_date", date);
				apiPermission.set("modify_date", date);
				apiPermission.set("id", idGenerate.getNextValue());
				addList.add(apiPermission);
				
				info.append("\n新增接口权限" + filename + ", 编号为" + apiPermission.getLong("id"));
			}
		}
		
		Log.info(info.toString());
		return addList.isEmpty() || ApiPermission.dao.batchSave(addList);
	}
	
	
	private static void readExcel(List<Record> records, String excelFile) {
		File file = new File(excelFile);
		try {
			records.addAll(excelHelper.importToRecordList("apiPermissionExcel", file));
		} catch (FileNotFoundException e) {
			LOG.error("权限EXCEL未找到, path:" + excelFile);
		} catch (CellTypeValidationException e) {
			LOG.error("文件导入转化错误");
		} catch (Exception e) {
			LOG.error("文件导入转化错误");
		}
	}
	
	@Before(Tx.class)
	private static boolean checkPermissionInDB(List<Record> records, boolean updatePermissionNameAndPnameTurn) {
		List<Permission> permissions = Permission.dao.findAll();
		HashSet<String> codes = Sets.newHashSet();
		Map<String, Permission> codeToPermissions = Maps.newHashMap();
		for (Permission permission : permissions) {
			codes.add(permission.getStr("code"));
			codeToPermissions.put(permission.getStr("code"), permission);
		}
		
		Date date = new Date();
		List<Permission> addList = Lists.newArrayList();
		List<Permission> updateList = Lists.newArrayList();
		StringBuilder info = new StringBuilder();
		for (Record record : records) {
			String code  = record.getStr("permission").trim();
			Permission permission = new Permission();
			if (!codes.contains(code)) {
				
				permission.set("code", code);
				permission.set("name", record.getStr("name").trim());
				permission.set("pname", record.getStr("moduleName").trim());
				permission.set("is_system", Boolean.TRUE);
				permission.set("create_date", date);
				permission.set("modify_date", date);
				permission.set("id", idGenerate.getNextValue());
				addList.add(permission);
				
				info.append("\n新增功能权限" + code + ", 编号为" + permission.getLong("id")); 
			} else {
				Permission oldPermission = codeToPermissions.get(code);
				if (updatePermissionNameAndPnameTurn && (!oldPermission.getStr("pname").equals(record.getStr("moduleName").trim()) || !oldPermission.getStr("name").equals(record.getStr("name").trim()))) {
					
					permission.set("name", record.getStr("name").trim());
					permission.set("pname", record.getStr("moduleName").trim());
					permission.set("modify_date", date);
					permission.set("id", oldPermission.getLong("id"));
					updateList.add(permission);
					
					info.append("\n修改功能权限" + code + ", 编号为" + permission.getLong("id")); 
				}
			}
		}
		
		Log.info(info.toString());
		
		if (!addList.isEmpty() && !Permission.dao.batchSave(addList)) {
			return false;
		}
		
		if (!updateList.isEmpty() && !Permission.dao.batchUpdate(updateList, "name, pname, modify_date")) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		
		return true;
	}
	
	@Before(Tx.class)
	private static boolean checkAssocationPermissionInDB(List<Record> records) {
		List<ApiPermission> apiPermissions = ApiPermission.dao.findAll();
		List<Permission> permissions = Permission.dao.findAll();
		List<PermissionHaveApi> permissionHaveApis = PermissionHaveApi.dao.findAll();
		BiMap<String, Long> apipermissionCodeToId = HashBiMap.create();
		BiMap<String, Long> permissionCodeToId = HashBiMap.create();
		for (ApiPermission apiPermission : apiPermissions) {
			apipermissionCodeToId.put(apiPermission.getStr("code"), apiPermission.getLong("id"));
		}
		
		for (Permission permission : permissions) {
			permissionCodeToId.put(permission.getStr("code"), permission.getLong("id"));
		}
		
		HashSet<String> haveAssocationList = Sets.newHashSet();
		for (PermissionHaveApi permissionHaveApi : permissionHaveApis) {
			String assocationCode = permissionCodeToId.inverse().get(permissionHaveApi.getLong("permission_id"))
					+ "," + apipermissionCodeToId.inverse().get(permissionHaveApi.getLong("api_permission_id"));
			haveAssocationList.add(assocationCode);
		}
		
		Date date = new Date();
		List<PermissionHaveApi> addList = Lists.newArrayList();
		StringBuilder info = new StringBuilder();
		for (Record record : records) {
			String permissionCode = record.getStr("permission").trim();
			for (String apiPermissionCode : record.getStr("apipermissions").trim().split("、")) {
				String assocationCode = permissionCode + "," + apiPermissionCode;
				
				if (!haveAssocationList.contains(assocationCode)) {
					if  (apipermissionCodeToId.get(apiPermissionCode) == null) {
						Log.warn("数据库中不存在" + apiPermissionCode);
						return false;
					}
					
					PermissionHaveApi permissionHaveApi = new PermissionHaveApi();
					permissionHaveApi.set("api_permission_id", apipermissionCodeToId.get(apiPermissionCode));
					permissionHaveApi.set("permission_id", permissionCodeToId.get(permissionCode));
					permissionHaveApi.set("modify_date", date);
					permissionHaveApi.set("create_date", date);
					addList.add(permissionHaveApi);
					haveAssocationList.add(assocationCode);
					
					info.append("\n新增功能权限" + permissionCode + "关联" + apiPermissionCode);
				}
			}
		}
		
		Log.info(info.toString());
		return addList.isEmpty() || PermissionHaveApi.dao.batchSave(addList);
	}
	
	private static Properties getProperties(String fileName) {
		try {
			return PropertiesUtil.createFromFile(PathKit.getRootClassPath() + File.separator + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
