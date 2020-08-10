package com.gnet.plugin.poi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.gnet.model.DbModel;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelModel {

	/**
	 * Excel模型名
	 */
	String name();

	/**
	 * 头所占行数
	 */
	String rowCount() default "0";

	/**
	 * 头所占列数
	 */
	String colsCount() default "0";

	/**
	 * 目标Excel的Sheet索引（从左往右从0开始）
	 */
	String targetSheetIndex() default "0";

	/**
	 * 目标对应的Class(暂时无用，预留)
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	Class<? extends DbModel> clazz() default DbModel.class;

}
