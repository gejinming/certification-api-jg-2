package com.gnet.plugin.setting.kit;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

public class TypeCheckKit {
	
	private static final Logger logger = Logger.getLogger(TypeCheckKit.class);

	public static void check(Serializable target) {
		if (!(target instanceof String || target instanceof Integer || target instanceof Long || target instanceof Float || target instanceof Double || target instanceof BigDecimal || target instanceof Boolean)) {
			logger.error("不支持该类型：" + target.getClass());
			throw new RuntimeException("不支持该类型：" + target.getClass().getName());
		}
	}
	
	private TypeCheckKit(){}
	
}
