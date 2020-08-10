package com.gnet.plugin.setting.impl.db;

import com.gnet.plugin.setting.ISetting;

/**
 * 配置数据接口
 * 
 * @author xuq
 * @date 2015年12月11日
 * @version 1.0
 */
public interface ISettingDb extends ISetting {

	static final String 	TABLE_GLOBAL 		= 	"sys_setting";			// 系统配置
	static final String 	TABLE_USER 			= 	"sys_user_setting";		// 用户配置
	
	static final String		COLUMN_ID			=	"id";					// 主键编号
	static final String		COLUMN_CREATE_DATE	= 	"create_date";			// 创建日期
	static final String		COLUMN_MODIFY_DATE	= 	"modify_date";			// 修改日期
	static final String		COLUMN_SKEY 		=	"skey";					// 键
	static final String 	COLUMN_SVALUE		= 	"svalue";				// 值
	static final String 	COLUMN_REGION		=	"region";				// 区域
	static final String		COLUMN_DESCRIPTION 	= 	"description";			// 描述
	
	static final String		COLUMN_USER_ID		= 	"user_id";				// 用户配置的用户编号
	
}
