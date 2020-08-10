package com.gnet.plugin.poi.exception;

/**
 * Excel异常母类
 *
 * @author xuqiang
 * @date 2018/02/05
 * @description
 **/
public class ExcelException extends RuntimeException {

    public ExcelException() {
        super();
    }

    public ExcelException(String msg) {
        super(msg);
    }

    public ExcelException(String msg, Throwable t) {
        super(msg, t);
    }

}
