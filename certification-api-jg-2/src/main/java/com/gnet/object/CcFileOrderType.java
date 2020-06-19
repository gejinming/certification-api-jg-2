package com.gnet.object;

/**
 * 文件排序
 *
 * @author yuhailun
 * @date 2018/01/31
 * @description
 **/
public enum CcFileOrderType implements OrderType{

    Name("name", "cf.name"),
    TYPE("type", "cf.type"),
    SIZE("size", "cf.size"),
    MODIFYUSER("modifyUser", "su2.name"),
    MODIFYDATE("modifyDate", "cf.modify_date");


    private String key;
    private String value;

    CcFileOrderType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }
}
