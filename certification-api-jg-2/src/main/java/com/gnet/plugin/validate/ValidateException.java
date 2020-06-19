package com.gnet.plugin.validate;

/**
 * @author LW
 * 验证器加载失败或者无法找到声明验证器配置文件时抛出
 */
public class ValidateException extends RuntimeException {
	private static final long serialVersionUID = 1;

	public ValidateException(String msg) {
		super(msg);
	}
}