package com.gnet.plugin.quartz.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.quartz.utils.ConnectionProvider;

import com.gnet.utils.SpringContextHolder;

/**
 * JFinal链接提供器
 * 
 * @author xuq
 * @date 2015年11月13日
 * @version 1.0
 */
public class JFinalConnectionProvider implements ConnectionProvider {
	
	private DataSource dataSource;
	
	@Override
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	@Override
	public void shutdown() throws SQLException {
		this.dataSource = null;
	}

	@Override
	public void initialize() throws SQLException {
		this.dataSource = SpringContextHolder.getBean("dataSource");
	}
	
}
