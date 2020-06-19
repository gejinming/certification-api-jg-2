package com.gnet.generate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gnet.generate.kit.FileKit;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.druid.DruidPlugin;

import jodd.util.PropertiesUtil;

/**
 * 代码生成器
 */
public class Generate {

	private static Logger logger = LoggerFactory.getLogger(Generate.class);
	
	public enum SEARCH_DATA_TYPE {
		
	}

	public static void main(String[] args) throws Exception {
		// 获取文件分隔符
		String separator = File.separator;

		// ========== ↓↓↓↓↓↓ 执行前请修改参数，谨慎执行。↓↓↓↓↓↓ ====================
		Boolean isDev = false;
		
		// 主要提供基本功能模块代码生成。
		//C:\Users\SY\git\certification-api\src\main\java\com\jfinal
		String projectPath = "F:\\workspaces\\Eclipse_All\\Eclipse_mars\\certification-api/src/main/java/com/gnet";
		String generatorPath = projectPath + separator + "generate";
		
		String author = "SY";
		
		String moduleName = "CcIndicationCourse";
		String moduleCName = "指标点课程关系表";
		
		String dbName = "certification_01";
		String idErrorCode = "0360";	// id为空的errorCode
		String targetErrorCode = errorToCode(Integer.valueOf(idErrorCode) + 1);
		String beforeErrprCode = targetErrorCode;
		String tableName = "cc_indication_course";
		String uniqueName = "null";
		String uniqueType = "String";  // 建议 Long，Integer，String
		String uniqueCName = "学院名";
		String uniqueErrorCode = ""; // 废弃
		
		
		String API_LIST = "EM00270";
		String API_EXISTED = "EM00275";
		String API_VIEW = "EM00271";
		String API_ADD = "EM00272";
		String API_EDIT = "EM00273";
		String API_DELETE = "EM00274";
		
		List<Map<String, Object>> searches = new ArrayList<>();
		
		Map<String, Object> propertyName = new HashMap<>();
		propertyName.put("name", "indicationId");				// * 查询&排序字段名,驼峰法，比如：courseId
		propertyName.put("type", "Long");						// * 查询&排序字段的数据类型String,Integer,Long
		propertyName.put("isNullable", true);					// * 查询&排序字段是否可以不传递
		propertyName.put("errorCode", "");					// 查询&排序字段如果必须传递，则定义为空的错误编码
		searches.add(propertyName);								// * 将此查询&排序加入查询&排序列表中

		Map<String, Object> propertyName2 = new HashMap<>();
		propertyName2.put("name", "courseId");				// * 查询&排序字段名
		propertyName2.put("type", "Long");						// * 查询&排序字段的数据类型String,Integer,Long
		propertyName2.put("isNullable", true);					// * 查询&排序字段是否可以不传递
		propertyName2.put("errorCode", "");					// 查询&排序字段如果必须传递，则定义为空的错误编码
		searches.add(propertyName2);								// * 将此查询&排序加入查询&排序列表中
		

		// ========== ↑↑↑↑↑↑ 执行前请修改参数，谨慎执行。↑↑↑↑↑↑ ====================
		String _modelName = camelName(moduleName);
		
		logger.info("Project Path: {}", projectPath);
		// 模板文件路径
		String tplPath = generatorPath + separator + "template";
		logger.info("Template Path: {}", tplPath);
		
		String codePath = generatorPath + separator + "code";

		// 初始化模板引擎
		TemplateKit.init();
		TemplateKit.setTmplateDirPath(tplPath);
		
		// dbinit
		Properties p = getProperties("db.properties");
		DruidPlugin druidPlugin = new DruidPlugin(p.getProperty("db.mysql.url"), p.getProperty("db.mysql.username"), p.getProperty("db.mysql.password"));
		ActiveRecordPlugin activeRecordPlugin = new ActiveRecordPlugin(druidPlugin);
		activeRecordPlugin.setContainerFactory(new CaseInsensitiveContainerFactory());
		activeRecordPlugin.setShowSql(true);
		druidPlugin.start();
		activeRecordPlugin.start();
		
		// 获得表结构
		List<Record> records = Db.find("select  TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME, IS_NULLABLE, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, COLUMN_KEY,COLUMN_DEFAULT,NUMERIC_PRECISION,COLUMN_COMMENT from information_schema.columns where table_name = ? and TABLE_SCHEMA = ?", tableName, dbName);
		Map<String, Object> errorCode = new HashMap<>();
		Integer errorNum = Integer.valueOf(beforeErrprCode);

		List<Record> list = new ArrayList<>();
		for (Record record : records) {
			String COLUMN_CAMEL_NAME = camelName(record.getStr("COLUMN_NAME"));
			record.set("COLUMN_CAMEL_NAME", COLUMN_CAMEL_NAME);
			// 判断为空的errorCode计数
			if(!(COLUMN_CAMEL_NAME.equals("createDate") || COLUMN_CAMEL_NAME.equals("isDel") || COLUMN_CAMEL_NAME.equals("modifyDate") || COLUMN_CAMEL_NAME.equals("id") || COLUMN_CAMEL_NAME.equals("remark"))) {
				errorNum ++;
				errorCode.put(COLUMN_CAMEL_NAME, errorToCode(errorNum));
			}
			for (Map<String, Object> searchItem : searches) {
				if (searchItem.get("name").equals(record.getStr("COLUMN_CAMEL_NAME"))) {
					// 有排序选项
					searchItem.put("NAME", searchItem.get("name").toString().toUpperCase());
					record.set("isSearch", true);
					record.set("searchProps", searchItem);
				}
			}
			list.add(record);
		}
		Collections.reverse(list);

		// 定义模板变量
		Map<String, Object> model = new HashMap<>();

		model.put("isDev", isDev);
		model.put("tplPath", tplPath);
		model.put("codePath", codePath);
		model.put("currentDate", new Date());
		model.put("author", author);
		
		model.put("tableName", tableName);
		
		model.put("moduleName", moduleName);
		model.put("_moduleName", _modelName);
		model.put("moduleCName", moduleCName);
		
		model.put("records", records);
		model.put("errorCode", errorCode);
		model.put("searches", searches);
		model.put("noSearch", searches.isEmpty());
		model.put("uniqueName", uniqueName);
		model.put("uniqueNameCamel", camelName(uniqueName));
		model.put("uniqueCName", uniqueCName);
		model.put("uniqueType", uniqueType);
		model.put("uniqueErrorCode", uniqueErrorCode);
		model.put("idErrorCode", idErrorCode);
		model.put("targetErrorCode", targetErrorCode);
		
		model.put("API_LIST", API_LIST);
		model.put("API_EXISTED", API_EXISTED);
		model.put("API_VIEW", API_VIEW);
		model.put("API_ADD", API_ADD);
		model.put("API_EDIT", API_EDIT);
		model.put("API_DELETE", API_DELETE);

		String filePath = "";
		File file = new File(codePath);
		if(!(file.exists() && file.isDirectory())) {
			file.mkdirs();
		}
		// 生成 List.java
		filePath = codePath + separator + API_LIST + ".java";
		TemplateKit.process("List.ftl", filePath, model);
		logger.info(API_LIST + ".java: {}", filePath);

		// 生成 Info.java
		filePath = codePath + separator + API_VIEW + ".java";
		TemplateKit.process("Info.ftl", filePath, model);
		logger.info(API_VIEW + ".java: {}", filePath);
		
		// 生成 Add.jsx
		filePath = codePath + separator + API_ADD + ".java";
		TemplateKit.process("Add.ftl", filePath, model);
		logger.info(API_ADD + ".java: {}", filePath);
		
		// 生成 Edit.jsx
		filePath = codePath + separator + API_EDIT + ".java";
		TemplateKit.process("Edit.ftl", filePath, model);
		logger.info(API_EDIT + "java: {}", filePath);
		
		// 生成 DELETE.jsx
		filePath = codePath + separator + API_DELETE + ".java";
		TemplateKit.process("Delete.ftl", filePath, model);
		logger.info(API_EDIT + "java: {}", filePath);
		
		// 生成 Existed.jsx
		filePath = codePath + separator + API_EXISTED + ".java";
		TemplateKit.process("Existed.ftl", filePath, model);
		logger.info(API_EXISTED + "java: {}", filePath);
		
		// 生成 OrderType.jsx
		filePath = codePath + separator + moduleName + "OrderType.java";
		TemplateKit.process("OrderType.ftl", filePath, model);
		logger.info(moduleName + "OrderType.java: {}", filePath);
		
		// 生成 model.jsx
		filePath = codePath + separator + moduleName + ".java";
		TemplateKit.process("Model.ftl", filePath, model);
		logger.info(moduleName + "OrderType.java: {}", filePath);

		logger.info("Generate Success.");
	}

	/**
	 * 变成errorCode
	 * @param errorNum
	 * 			当前数量
	 * @return
	 */
	private static String errorToCode(Integer errorNum) {
		String errorCode = errorNum.toString();
		for(int i = errorCode.length(); i < 4; i++) {
			errorCode = "0" + errorCode;
		}
		return errorCode;
	}

	/**
	 * 将内容写入文件
	 * 
	 * @param content
	 * @param filePath
	 */
	public static void writeFile(String content, String filePath) {
		try {
			if (FileKit.createFile(filePath)) {
				FileOutputStream writerStream = new FileOutputStream(filePath);
				BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(writerStream, "UTF-8"));
				bufferedWriter.write(content);
				bufferedWriter.close();
				writerStream.close();
			} else {
				logger.info("生成失败，文件已存在！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Properties getProperties(String fileName) {
		try {
			return PropertiesUtil.createFromFile(PathKit.getRootClassPath() + File.separator + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static String camelName(String name) {
	    StringBuilder result = new StringBuilder();
	    // 快速检查
	    if (name == null || name.isEmpty()) {
	        // 没必要转换
	        return "";
	    } else if (!name.contains("_")) {
	        // 不含下划线，仅将首字母小写
	        return name.substring(0, 1).toLowerCase() + name.substring(1);
	    }
	    // 用下划线将原始字符串分割
	    String camels[] = name.split("_");
	    for (String camel :  camels) {
	        // 跳过原始字符串中开头、结尾的下换线或双重下划线
	        if (camel.isEmpty()) {
	            continue;
	        }
	        // 处理真正的驼峰片段
	        if (result.length() == 0) {
	            // 第一个驼峰片段，全部字母都小写
	            result.append(camel.toLowerCase());
	        } else {
	            // 其他的驼峰片段，首字母大写
	            result.append(camel.substring(0, 1).toUpperCase());
	            result.append(camel.substring(1).toLowerCase());
	        }
	    }
	    return result.toString();
	}

}
