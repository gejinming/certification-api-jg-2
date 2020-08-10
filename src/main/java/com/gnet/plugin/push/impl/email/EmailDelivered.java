package com.gnet.plugin.push.impl.email;

import java.math.BigDecimal;

import com.gnet.plugin.push.kit.DecimalKit;

/**
 * 电子邮件发送结果
 * 
 * @author xuq
 * @date 2015年11月17日
 * @version 1.0
 */
public class EmailDelivered {
	
	static final Integer NOT_DELIVERED 		= 1;		// 未发送 
	static final Integer DELIVERED_SUCCESS 	= 2;		// 发送成功
	static final Integer DELIVERED_FAIL 	= 3;		// 发送失败
	static final Integer DELIVERED_PARTILY 	= 4;		// 部分发送成功

	private Integer 	result;							// 邮件发送结果
	private Integer 	delivered;						// 发送成功数量
	private Integer 	allToDelivered;					// 总需要发送数
	
	public EmailDelivered(Integer result, Integer delivered, Integer allToDelivered) {
		this.result = result;
		this.delivered = delivered;
		this.allToDelivered = allToDelivered;
	}

	public Integer getResult() {
		return result;
	}

	public Integer getDelivered() {
		return delivered;
	}

	public Integer getAllToDelivered() {
		return allToDelivered;
	}
	
	public EmailDelivered deliveredSuccess() {
		setResult(DELIVERED_SUCCESS);
		return emailDelivered();
	}
	
	public EmailDelivered deliveredFail() {
		setResult(DELIVERED_FAIL);
		return this;
	}
	
	public EmailDelivered deliveredPartily() {
		setResult(DELIVERED_PARTILY);
		return this;
	}
	
	public void setResult(Integer result) {
		this.result = result;
	}

	/**
	 * 成功发送一封邮件
	 * 
	 * @return 发送结果
	 */
	public EmailDelivered emailDelivered() {
		this.delivered++;
		return this;
	}
	
	/**
	 * 获得发送比率
	 * 
	 * @return 获得发送比率
	 */
	public BigDecimal getPercentage() {
		return DecimalKit.div(new BigDecimal(delivered), new BigDecimal(allToDelivered));
	}

}
