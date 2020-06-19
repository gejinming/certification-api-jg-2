package com.gnet.utils;

import java.util.Random;

import org.apache.commons.codec.binary.Base64;

import com.jfinal.log.Logger;

/**
 * 生成access_token
 * @author wct
 * @Date 2016年6月3日
 */
public class AccessTokenKit {
	
	private static final Logger logger = Logger.getLogger(AccessTokenKit.class);
	
	/**
	 * 随机数大小
	 */
	private static final Long RANDOM_SIZE = 100000L;
	
	/**
	 * Token固定值
	 */
	private static final String TOKEN_VALUE = "CertificationApi";
	
	/**
	 * 获得token
	 * @param userId
	 * @return
	 */
	public static String getToken(Long userId) {
		try {
			if (userId == null) {
				throw new IllegalArgumentException();
			}
		} catch (IllegalArgumentException e) {
			if (logger.isErrorEnabled()) {
				logger.error("userId not found", e.getCause());
			}
			return null;
		}
		
		String access_token = TOKEN_VALUE + getTimestamp() + getRandomValue() + userId;
		
		return Base64.encodeBase64String(Digests.sha1(access_token.toString().getBytes()));
	}
	
	/**
	 * 获得随机数
	 * @return
	 */
	private static String getRandomValue() {
		Integer num = new Random(RANDOM_SIZE).nextInt();
		return num.toString();
	}
	
	/**
	 * 获得时间戳
	 * @return
	 */
	private static String getTimestamp() {
		Long timestamp = System.currentTimeMillis();
		return timestamp.toString();
	}

}
