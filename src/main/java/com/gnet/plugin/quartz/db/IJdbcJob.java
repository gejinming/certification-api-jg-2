package com.gnet.plugin.quartz.db;

import java.math.BigDecimal;
import java.util.Date;

import org.quartz.Job;

/**
 * Job数据记录
 * 
 * @author xuq
 * @date 2015年11月19日
 * @version 1.1
 */
public interface IJdbcJob extends Job {
	
	/**
	 * 记录创建
	 * 
	 * @param date
	 */
	public Long recordCreate(String jobContent, Date date);

	/**
	 * 记录开始
	 * 
	 * @param pushJobId
	 * @param date
	 */
	public boolean recordStart(Long pushJobId, Date date);
	
	/**
	 * 记录失败
	 * 
	 * @param pushJobId
	 * @param t
	 * @param date
	 */
	public boolean recordFail(Long pushJobId, Throwable t, Date date);
	
	/**
	 * 记录进度更新
	 * 
	 * @param pushJobId
	 * @param progress
	 * @param date
	 */
	public boolean updateProgress(Long pushJobId, BigDecimal progress, Date date);
	
	/**
	 * 记录完成
	 * 
	 * @param pushJobId
	 * @param date
	 */
	public boolean recordFinish(Long pushJobId, Date date);

}
