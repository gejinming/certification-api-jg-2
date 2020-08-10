package com.gnet.plugin.configLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jodd.typeconverter.Convert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;

/**
 * 配置加载器(只加载properties文件)
 * @author xuq
 * @date Jan 15, 2015
 */
public class ConfigLoader {

    private static final Logger logger = Logger.getLogger(ConfigLoader.class);
    public static String separator = ",";
    private static boolean isFilter = true;
    static Map<String, Properties> propsMap = Maps.newConcurrentMap();
    
    /**
     * 不可实例化
     */
    private ConfigLoader(){
        throw new RuntimeException("不可实例化");
    }
    
    /**
     * 加载配置项
     * @param directory
     *          文件名或者文件夹名
     */
    public static final void load(String directory) {
        try {
            if(directory.contains("classpath:")) {
                directory = PathKit.getRootClassPath() + File.separator + directory.replace("classpath:", "");
            }
            
            File configFiles = new File(directory);
            if(!configFiles.exists()) 
                throw new RuntimeException("配置文件或者文件夹不存在:[" + directory + "]");
            if(configFiles.isDirectory()) { // 文件夹，载入所有文件
                File []files = configFiles.listFiles();
                for(File file : files) {
                    ConfigLoader.loadFile(file);
                }
            }else{  // 文件
                File file = configFiles;
                ConfigLoader.loadFile(file);
            }
            logger.info("config properties loaded:" + directory);
        } catch (IOException e) {
            throw new RuntimeException("配置加载出错");
        }
    }
    
    /**
     * 加载配置项
     * @param directory
     *          文件名或者文件夹名
     * @param separator
     *          分隔符
     */
    public static final void load(String directory, String separator) {
        ConfigLoader.separator = separator;
        load(directory);
    }
    
    /**
     * 加载配置项
     * @param directory
     *          文件名或者文件夹名
     * @param isFilter
     *          是否启用过滤
     */
    public static final void load(String directory, boolean isFilter) {
        ConfigLoader.isFilter = isFilter;
        load(directory);
    }
    
    /**
     * 加载配置项
     * @param directory
     *          文件名或者文件夹名
     * @param separator
     *          分隔符
     * @param isFilter
     *          是否启用过滤
     */
    public static final void load(String directory, String separator, boolean isFilter) {
        ConfigLoader.separator = separator;
        ConfigLoader.isFilter = isFilter;
        load(directory);
    }
    
    /**
     * 载入单个文件的配置
     * @param file
     *          文件
     * @throws IOException
     */
    private static void loadFile(File file) throws IOException {
        String fileName = file.getName();
        if(fileName.endsWith(".properties")) {
            String config = new String();
            String prueFileName = fileName.replace(".properties", "");
            if(prueFileName.indexOf("-") > -1) {
                config = prueFileName.split("-")[1];
            }else{
                config = fileName + new Date().getTime() + (int) (Math.random() * 10000);
            }
            Properties properties = new Properties();
            properties.load(new FileInputStream(file));
            
            filterProperties(properties, separator);
            
            propsMap.put(config, properties);
        }
    }
    
    /**
     * 过滤
     * @param props
     *          属性
     * @param separator
     *          分隔符
     */
    private static Properties filterProperties(Properties properties, String separator) {
        Iterator<Map.Entry<Object, Object>> iter = properties.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry<Object, Object> item = iter.next();
            String key = Convert.toString(item.getKey());
            String value = Convert.toString(item.getValue());
            if(value.contains(separator) && ConfigLoader.isFilter) {
                List<String> obj = Lists.newArrayList();
                String []strs = value.split(separator);
                for(String str : strs) {
                    obj.add(str.trim());
                }
                properties.put(key, obj);
            }else{
                properties.put(key, value);
            }
        }
        return properties;
    }
    
    public static void main(String[] args) {
        ConfigLoader.load(PathKit.getRootClassPath() + File.separator + "config");
        System.out.println(JsonKit.toJson(ConfigUtils.getProps("ueditor").getProperty("imageActionName")));
    }
    
}
