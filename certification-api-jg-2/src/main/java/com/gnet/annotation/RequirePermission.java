package com.gnet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限声明注释<br>
 * 注意：只能用于类声明
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {

	/**
	 * 需求的权限组
	 * 
	 * @return
	 */
	String[] values();
	
}
