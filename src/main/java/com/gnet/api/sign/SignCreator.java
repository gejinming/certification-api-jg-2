package com.gnet.api.sign;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.jfinal.log.Logger;

/**
 * 签名认证
 * 
 * @author xuq
 * @date 2015年8月24日15:50:02
 */
public class SignCreator {

	private static final Logger logger = Logger.getLogger(SignCreator.class);
	private static final String ENCODE_UTF = "UTF-8";

	/**
	 * RSA校验
	 * 
	 * @param urlsafeBase64Str
	 * @return
	 * @throws Exception
	 */
	private static boolean rsaVerify(byte[] urlsafeBase64Str) throws Exception {
		// 初始化密钥
		// 生成密钥对
		Map<String, Object> keyMap = RSACoder.initKey();
		// 公钥
		byte[] publicKey = RSACoder.getPublicKey(keyMap);

		// 私钥
		byte[] privateKey = RSACoder.getPrivateKey(keyMap);

		// 甲方进行数据的加密
		byte[] sign = RSACoder.sign(urlsafeBase64Str, privateKey);

		// 验证签名
		return RSACoder.verify(urlsafeBase64Str, publicKey, sign);
	}
	
	private static String rsaEncode(byte[] urlsafeBase64Str) throws Exception {
		// 初始化密钥
		// 生成密钥对
		Map<String, Object> keyMap = RSACoder.initKey();

		// 私钥
		byte[] privateKey = RSACoder.getPrivateKey(keyMap);

		// 甲方进行数据的加密
		byte[] sign = RSACoder.sign(urlsafeBase64Str, privateKey);
		
		return Base64.encodeBase64String(sign);
	}

	/**
	 * 校验
	 * 
	 * @param content
	 *            待校验内容
	 * @return 是否校验成功
	 */
	public static boolean verify(String content) {
		try {
			byte[] signStr = Base64.decodeBase64(content);
			
			return rsaVerify(signStr);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

			if (logger.isErrorEnabled()) {
				logger.error("不支持的编码：" + e.getMessage());
			}
			
			return false;
		} catch (Exception e) {
			e.printStackTrace();

			if (logger.isErrorEnabled()) {
				logger.error("异常：" + e.getMessage());
			}
			
			return false;
		}
	}
	
	/**
	 * 签名
	 * 
	 * @param content
	 * @return
	 */
	public static String rsaEncode(String content) {
		try {
			return rsaEncode(content.getBytes(ENCODE_UTF));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

			if (logger.isErrorEnabled()) {
				logger.error("不支持的编码：" + e.getMessage());
			}
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();

			if (logger.isErrorEnabled()) {
				logger.error("异常：" + e.getMessage());
			}
			
			return null;
		}
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException, Exception {
		String msg = "11111";
		
		String encode = rsaEncode(msg.getBytes(ENCODE_UTF));
		
		System.out.println(encode);
		
		boolean verify = rsaVerify(encode.getBytes(ENCODE_UTF));
		
		System.err.println(verify);
	}

}
