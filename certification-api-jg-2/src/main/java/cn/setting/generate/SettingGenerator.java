package cn.setting.generate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.gnet.model.DbModel;
import com.gnet.plugin.configLoader.ConfigLoader;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.plugin.setting.settingEnum.ISettingEnum;
import com.gnet.plugin.tablebind.AutoTableBindPlugin;
import com.gnet.plugin.tablebind.SimpleNameStyles;
import com.gnet.plugin.validate.Verifiable;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlReporter;
import com.jfinal.plugin.spring.SpringPlugin;

public class SettingGenerator {
    
    private static final Logger logger  = Logger.getLogger(SettingGenerator.class);

	@SuppressWarnings("unchecked")
	public static void init() {
		ConfigLoader.load("classpath:config");
		// 默认在 WEB-INF/application.xml
		SpringPlugin springPlugin = new SpringPlugin();
		springPlugin.start();
		
		// 主业务数据库
		AutoTableBindPlugin atbp = new AutoTableBindPlugin("mysql", (com.alibaba.druid.pool.DruidDataSource) SpringContextHolder.getBean("dataSource"), SimpleNameStyles.LOWER_UNDERLINE);
		atbp.addExcludeClasses(DbModel.class);
		atbp.addExcludeClasses(Verifiable.class);
		atbp.setShowSql(true);
		atbp.start();

		// sql记录
		SqlReporter.setLogger(true, 3);
	}
	
	public static void main(String[] args) {
		init();
		try {
			Date date = new Date();
			IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
			
			List<Record> records = new ArrayList<Record>();
			Set<String> clazzNames = ClassScaner.getClassnameFromPackage("cn.setting", false);
			Iterator<String> iter = clazzNames.iterator();
			while (iter.hasNext()) {
				String className = iter.next();
				Class<?> clazz = Class.forName(className);
				Field regionField = clazz.getDeclaredField("REGION");
				
				String region = (String) regionField.get(null);
				
				StringBuilder enumName = new StringBuilder();
				char[] simpleClazzNameChars = clazz.getSimpleName().toCharArray();
				for (int i = 0; i < simpleClazzNameChars.length; i++) {
					char ch = simpleClazzNameChars[i];
					if (i != 0 && ch >= 'A' && ch <= 'Z') {
						enumName.append("_").append(ch);
					} else {
						enumName.append(Character.toUpperCase(ch));
					}
				}
				enumName.append("_ENUM");
				
				Class<?>[] memberClazzs = clazz.getDeclaredClasses();
				for (Class<?> memberClazz : memberClazzs) {
				    if (!memberClazz.isEnum()) {
				        continue;
				    }
					if (memberClazz.getSimpleName().equals(enumName.toString())) {
						Object[] enumObjs = memberClazz.getEnumConstants();
						for (Object enumObj : enumObjs) {
						    ISettingEnum settingEnum = (ISettingEnum) enumObj;
						    
						    Record record = new Record();
                            record.set("id", idGenerate.getNextValue());
                            record.set("create_date", date);
                            record.set("modify_date", date);
                            record.set("skey", settingEnum.getValue());
                            record.set("svalue", settingEnum.getDefaultValue());
                            record.set("region", region);
                            records.add(record);
						}
					}
					
				}
			}
			System.out.println(records);
			Db.update("delete from sys_setting");
			Db.batch("insert into sys_setting(id,create_date,modify_date,skey,svalue,region) values(?,?,?,?,?,?)", "id,create_date,modify_date,skey,svalue,region", records);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
		    if (logger.isErrorEnabled()) {
		        logger.error("必须存在REGION字段");
		    }
            e.printStackTrace();
        }
	}
	
}
