package com.gnet.api.encrypt;

import com.jfinal.log.Logger;

public class EncryptCreator {
	
	private static final Logger logger = Logger.getLogger(EncryptCreator.class);
	
	private static final String APP_ID = "aA4nIdHOXJ6n13U";
	private static final String APP_SECRET = "1VYlBllH2NM6kNS3eBxgMrD3ke6tYbGG2flE1rBF6Xn";
	
	public static String encrypt(String content) throws AesException {
		try {
			EncryptCoder encryptCoder = new EncryptCoder(APP_ID, APP_SECRET);
			
			String decryptStr = encryptCoder.encrypt(encryptCoder.getRandomStr(), content);
			
			return decryptStr;
		} catch (AesException e) {
			e.printStackTrace();
			
			if (logger.isErrorEnabled()) {
				logger.error("加密错误：" + e.getCode());
			}
			
			throw new AesException(e.getCode());
		}
	}

	public static String decrypt(String encryptStr) throws AesException {
		try {
			EncryptCoder encryptCoder = new EncryptCoder(APP_ID, APP_SECRET);
			
			String decryptStr = encryptCoder.decrypt(encryptStr);
			
			return decryptStr;
		} catch (AesException e) {
			e.printStackTrace();
			
			if (logger.isErrorEnabled()) {
				logger.error("解密错误：" + e.getCode());
			}
			
			throw new AesException(e.getCode());
		}
	}
	
}
