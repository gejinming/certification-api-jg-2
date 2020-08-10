package com.gnet.plugin.setting.kit;

import java.math.BigDecimal;

import com.gnet.plugin.setting.exceptions.StrParseErrorException;

/**
 * 字符串类型解析转化工具
 * 
 * @author xuq
 * @date 2015年12月14日
 * @version 1.1
 * Modify: @v1.1 	修改策略，parse的时候，如果target为空的时候，直接返回null对象。并且增加clazz参数为空时，抛出异常。
 * 					增加target的trim过滤，并且增加修正解析Boolean的问题，对于无法解析"1"和"0"进行修正。
 */
public class StrParserKit {
	
	@SuppressWarnings("unchecked")
	public static <T> T parse(String target, Class<T> clazz) throws StrParseErrorException {
		if (target == null || target.trim() == "") {
			// 目标对象为空
			return null;
		}
		if (clazz == null) {
			throw new RuntimeException("目标class不得为空");
		}
		
		// trim
		target = trimStr(target);
		
		Object value;
		if (String.class.equals(clazz)) {
			value = target;
		} else if (Integer.class.equals(clazz)) {
			value = parseInt(target);
		} else if (Long.class.equals(clazz)) {
			value = parseLong(target);
		} else if (Double.class.equals(clazz)) {
			value = parseDouble(target);
		} else if (Float.class.equals(clazz)) {
			value = parseFloat(target);
		} else if (BigDecimal.class.equals(clazz)) {
			value = parseBigDecimal(target);
		} else if (Boolean.class.equals(clazz)) {
			value = parseBoolean(target);
		} else {
			throw new RuntimeException("不支持其他类型转化");
		}
		
		return (T) value;
	}
	
	private static Integer parseInt(String target) throws StrParseErrorException {
		try {
			return Integer.valueOf(target);
		} catch (NumberFormatException e) {
			throw new StrParseErrorException(e);
		}
	}
	
	private static Long parseLong(String target) throws StrParseErrorException {
		try {
			return Long.valueOf(target);
		} catch (NumberFormatException e) {
			throw new StrParseErrorException(e);
		}
	}
	
	private static Double parseDouble(String target) throws StrParseErrorException {
		try {
			return Double.valueOf(target);
		} catch (NumberFormatException e) {
			throw new StrParseErrorException(e);
		}
	}
	
	private static Float parseFloat(String target) throws StrParseErrorException {
		try {
			return Float.valueOf(target);
		} catch (NumberFormatException e) {
			throw new StrParseErrorException(e);
		}
	}
	
	private static BigDecimal parseBigDecimal(String target) throws StrParseErrorException {
		try {
			return new BigDecimal(target);
		} catch (NumberFormatException e) {
			throw new StrParseErrorException(e);
		}
	}
	
	private static Boolean parseBoolean(String target) throws StrParseErrorException {
		try {
			if ("1".equals(target)) {
				return Boolean.TRUE;
			} else if ("0".equals(target)) {
				return Boolean.FALSE;
			} else if ("TRUE".equalsIgnoreCase(target)) {
				return Boolean.TRUE;
			} else if ("FALSE".equalsIgnoreCase(target)) {
				return Boolean.FALSE;
			} else if ("YES".equalsIgnoreCase(target)) {
				return Boolean.TRUE;
			} else if ("NO".equalsIgnoreCase(target)) {
				return Boolean.FALSE;
			} else {
				return Boolean.parseBoolean(target);
			}
		} catch (NumberFormatException e) {
			throw new StrParseErrorException(e);
		}
	}
	
	private static String trimStr(String target) {
		return target.trim();
	}
	
	private StrParserKit() {}
}
