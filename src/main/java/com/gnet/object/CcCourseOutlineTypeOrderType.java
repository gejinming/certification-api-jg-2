package com.gnet.object;

import com.gnet.object.OrderType;

/**
 * Created by xzl on 2017/8/22.
 */
public enum CcCourseOutlineTypeOrderType implements OrderType {

    STATUS("status", "cco.status"),
    AUDITOR_NAME("auditorName", "ctr.name"),
    AUTHOR_NAME("authorName", "ct.name"),
    OUTLINE_TYPE_NAME("outlineTypeName", "ccot.name"),
    COURSE_NAME("courseName", "cc.name"),
    CODE("code", "cc.code"),
    MAJOR_NAME("majorName", "so.name"),
    NAME("name", "cco.name");

    private String key;
    private String value;

    private CcCourseOutlineTypeOrderType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
