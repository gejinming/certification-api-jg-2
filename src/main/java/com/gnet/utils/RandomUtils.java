package com.gnet.utils;


import org.apache.commons.lang.RandomStringUtils;

/**
 * 随机生成特定规则的字符串
 * @author xzl
 * @Date
 */
public class RandomUtils {

    public static String randomString(String type){
         return randomString(type, 4);
    }

    public static String randomString(String type, Integer length){
        return String.format("%s_%s_%s", type, System.currentTimeMillis(), RandomStringUtils.randomAlphanumeric(length));
    }
}
