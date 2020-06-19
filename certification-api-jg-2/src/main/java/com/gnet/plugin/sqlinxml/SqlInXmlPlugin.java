package com.gnet.plugin.sqlinxml;

import java.io.File;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.IPlugin;

public class SqlInXmlPlugin implements IPlugin {

    private String targetPath;
    
    public SqlInXmlPlugin(String targetPath) {
        if (targetPath.startsWith("classpath:")) {
            this.targetPath = PathKit.getRootClassPath() + File.separator + targetPath.replace("classpath:", "");
        } else {
            this.targetPath = targetPath;
        }
    }

    @Override
    public boolean start() {
        SqlKit.init(targetPath);
        return true;
    }

    @Override
    public boolean stop() {
        SqlKit.clearSqlMap();
        return true;
    }

}
