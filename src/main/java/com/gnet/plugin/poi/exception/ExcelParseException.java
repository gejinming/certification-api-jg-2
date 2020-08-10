package com.gnet.plugin.poi.exception;

/**
 * excel解析异常
 *
 * @author xuqiang
 * @date 2018/02/05
 * @description
 **/
public class ExcelParseException extends ExcelException {

    public ExcelParseException() {
        super();
    }

    public ExcelParseException(String msg) {
        super(msg);
    }

    public ExcelParseException(String msg, Throwable t) {
        super(msg, t);
    }

}
