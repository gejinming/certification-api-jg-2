package com.gnet.api.encrypt;

@SuppressWarnings("serial")
public class AesException extends Exception {

	public final static int OK = 0;
	public final static int ValidateSignatureError = -40001;
	public final static int ParseXmlError = -40002;
	public final static int ComputeSignatureError = -40003;
	public final static int IllegalAesKey = -40004;
	public final static int ValidateAppidError = -40005;
	public final static int EncryptAESError = -40006;
	public final static int DecryptAESError = -40007;
	public final static int IllegalBuffer = -40008;

	private int code;

	private static String getMessage(int code) {
		switch (code) {
		case IllegalAesKey:
			return "SymmetricKey非法";
		case ValidateAppidError:
			return "appid校验失败";
		case EncryptAESError:
			return "aes加密失败";
		case DecryptAESError:
			return "aes解密失败";
		default:
			return null;
		}
	}

	public int getCode() {
		return code;
	}

	AesException(int code) {
		super(getMessage(code));
		this.code = code;
	}

}
