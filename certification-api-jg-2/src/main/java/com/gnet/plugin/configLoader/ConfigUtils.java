package com.gnet.plugin.configLoader;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import jodd.typeconverter.Convert;
import jodd.typeconverter.TypeConverterManager;

import com.google.common.collect.Lists;

/**
 * 配置使用工具
 * 
 * @author xuq
 * @date Jan 15, 2015
 */
public class ConfigUtils {

    /**
     * 不可实例化
     */
    private ConfigUtils() {
        throw new RuntimeException("不可实例化");
    }

    /**
     * 通过配置名获取该配置的所有属性
     * 
     * @param config
     *            配置名
     * @return 属性
     */
    public static Properties getProps(String config) {
        return ConfigLoader.propsMap.get(config);
    }

    /**
     * 获取值
     * 
     * @param <T>
     *            特定类
     * @param config
     *            配置名
     * @param key
     *            关键词
     * @param clazz
     *            值类型
     * @return 值
     */
    public static <T> T getValue(String config, String key, Class<T> clazz) {
        Properties props = ConfigLoader.propsMap.get(config);
        return TypeConverterManager.convertType(props.get(key), clazz);
    }

    /**
     * 获取值数组
     * 
     * @param <T>
     *            特定类
     * @param config
     *            配置名
     * @param keys
     *            关键字数组
     * @param clazz
     *            值类型
     * @return 值数组
     */
    public static <T> List<T> getValues(String config, String[] keys, Class<T> clazz) {
        List<T> result = Lists.newArrayList();
        Properties props = ConfigLoader.propsMap.get(config);
        for (String key : keys) {
            T value = TypeConverterManager.convertType(props.get(key), clazz);
            result.add(value);
        }
        return result;
    }

    public static String getStr(String config, String key) {
        return getValue(config, key, String.class);
    }

    public static Integer getInteger(String config, String key) {
        return getValue(config, key, Integer.class);
    }

    public static Long getLong(String config, String key) {
        return getValue(config, key, Long.class);
    }

    public static Date getDate(String config, String key) {
        return getValue(config, key, Date.class);
    }

    public static BigDecimal getBigDecimal(String config, String key) {
        return getValue(config, key, BigDecimal.class);
    }

    public static Double getDouble(String config, String key) {
        return getValue(config, key, Double.class);
    }

    public static Float getFloat(String config, String key) {
        return getValue(config, key, Float.class);
    }
    
    public static Boolean getBoolean(String config, String key) {
        return getValue(config, key, Boolean.class);
    }

    /**
     * 获取数组类型值
     * 
     * @param <T>
     *            特定类
     * @param config
     *            配置名
     * @param key
     *            关键词
     * @param separator
     *            分词字
     * @param clazz
     *            数组值类型
     * @return 数组
     */
    public static <T> List<T> getList(String config, String key, String separator, Class<T> clazz) {
        List<T> list = Lists.newArrayList();
        String value = Convert.toString(ConfigLoader.propsMap.get(config).get(key));
        String[] values = value.split(separator);
        for (String val : values) {
            list.add(TypeConverterManager.convertType(val, clazz));
        }
        return list;
    }
}
