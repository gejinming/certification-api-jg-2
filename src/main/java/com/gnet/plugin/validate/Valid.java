package com.gnet.plugin.validate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Valid {
	/**
	 * 规则名。如"email","phone"等，这些验证类型必须有对应的验证器，
	 * 验证器命名格式必须是{type}<code>Verifier</code>。默认验证方法为正则表达式
	 */
	String[]	rule() 			default "";
	
	/**
	 * 当type为默认或者为空（""）是，采用该正则表达式验证指定字段
	 */
	String[] 	pattern() 		default "";
	
	/**
	 * 当验证不通过时的提示信息
	 */
	String 		tipMsg() 		default "验证不通过";
	
	/**
	 * 为自定义的验证器配置的参数
	 */
	String[] 	params() 		default "";
}
