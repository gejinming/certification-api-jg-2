package com.gnet.plugin.validate;


public interface Verifier {
	public VerificationResult verify(Object bean, Class<?> fieldType, Object fieldValue, String fieldName, String[] params);
}
