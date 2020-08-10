package com.gnet.api;

import java.io.Serializable;

public class Response implements Serializable {

	private static final long serialVersionUID = -1956169921211558448L;

	private ResponseHeader header;
	private Object data;
	private String fileName;
	
	public static final Character SUCCESS = '1';		//成功
	public static final Character FAIL = '2';			//失敗
	public static final Character FILE = '3';			//文件
	public static final Character WORD_FILE = '4';		//word文件

	public ResponseHeader getHeader() {
		return header;
	}

	public void setHeader(ResponseHeader header) {
		this.header = header;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
