package com.gnet.plugin.validate;


public class VerificationResult {
	public final String fieldName;
	public final String verifier;
	public final String tipMsg;
	public final Object fieldValue;
	public final boolean result;
	
	/**
	 * @param fieldName 验证字段名
	 * @param type	验证的规则名
	 * @param msg	验证后的提示信息
	 * @param result 验证的结果（true or false）
	 */
	public VerificationResult(String fieldName, String verifier, String tipMsg, Object fieldValue, boolean result){
		this.fieldName 	= fieldName;
		this.fieldValue	= fieldValue;
		this.verifier 	= verifier;
		this.tipMsg 	= tipMsg;
		this.result 	= result;
	}
	
	public String toString(){
		StringBuilder s = new StringBuilder();
		s.append("{").
			append("\"type\":\"").append(this.verifier).append("\"").
			append(", \"fieldName\":\"").append(this.fieldName).append("\"").
			append(", \"fieldValue\":\"").append(this.fieldValue).append("\"").
			append(", \"tipMsg\":\"").append(this.tipMsg).append("\"").
			append(", \"result\":\"").append(this.result).append("\"").
			append("}");
		
		return s.toString();
	}
}