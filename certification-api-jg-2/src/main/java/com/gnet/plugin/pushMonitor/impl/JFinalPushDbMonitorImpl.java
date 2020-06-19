package com.gnet.plugin.pushMonitor.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.gnet.plugin.pushMonitor.IPushDbMonitor;
import com.gnet.plugin.pushMonitor.obj.OrderInfo;
import com.gnet.plugin.pushMonitor.obj.PageInfo;
import com.gnet.plugin.pushMonitor.obj.PushJob;
import com.gnet.plugin.pushMonitor.obj.PushJobError;
import com.gnet.plugin.pushMonitor.obj.PushJobLog;
import com.gnet.plugin.quartz.db.IJdbcPushConstants;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * JFinal数据监控
 * 
 * @author xuq
 * @date 2015年12月30日
 * @version 1.1
 */
public class JFinalPushDbMonitorImpl implements IPushDbMonitor, IJdbcPushConstants {

	@Override
	public List<PushJob> watch(PageInfo pageInfo, Date startTime, Date endTime) {
		List<PushJob> pushJobs = new ArrayList<PushJob>();
		
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder(TABLE_PUSH + " where 1=1 ");
		if (startTime != null) {
			sql.append(COL_START_TIME + "=? ");
			params.add(startTime);
		}
		if (endTime != null) {
			sql.append(COL_END_TIME + "=? ");
			params.add(endTime);
		}
		if (pageInfo.getOrderInfos() != null && pageInfo.getOrderInfos().length > 0) {
			sql.append("order by ");
			OrderInfo[] orderInfos = pageInfo.getOrderInfos();
			for (int i = 0; i < orderInfos.length; i++) {
				OrderInfo orderInfo = orderInfos[i];
				sql.append(orderInfo.getBy() + " " + orderInfo.getOrder().name());
				if ((i + 1) < orderInfos.length) {
					sql.append(",");
				}
			}
		}
		
		String countSql = "select count(1) from " + sql.toString();
		String findSql = "select * from " + sql.toString() + "limit " + (pageInfo.getPageNumber() - 1) * pageInfo.getPageSize() + "," + pageInfo.getPageSize();
		
		Long count = Db.queryLong(countSql, params.toArray());
		List<Record> records = Db.find(findSql, params.toArray());
		
		pageInfo.setTotalPage(count.intValue());
		for (Record record : records) {
			PushJob pushJob = new PushJob();
			pushJob.setId(record.getLong(COL_ID));
			pushJob.setCreateDate(record.getDate(COL_CREATE_DATE));
			pushJob.setModifyDate(record.getDate(COL_MODIFY_DATE));
			pushJob.setJobContent(deserialization(record.getStr(COL_JOB_CONTENT)));
			pushJob.setProgress(record.getBigDecimal(COL_PROGRESS));
			pushJobs.add(pushJob);
		}
		
		return pushJobs;
	}

	@Override
	public List<PushJobLog> watchLog(Long pushJobId) {
		List<PushJobLog> pushJobLogs = new ArrayList<PushJobLog>();
		
		StringBuilder sql = new StringBuilder("select * from " + TABLE_PUSH_LOG + " where " + COL_PUSH_JOB_ID + "=? ");
		List<Record> records = Db.find(sql.toString(), pushJobId);
		for (Record record : records) {
			PushJobLog pushJobLog = new PushJobLog();
			pushJobLog.setId(record.getLong(COL_ID));
			pushJobLog.setCreateDate(record.getDate(COL_CREATE_DATE));
			pushJobLog.setModifyDate(record.getDate(COL_MODIFY_DATE));
			pushJobLog.setEventType(record.getInt(COL_EVENT_TYPE));
			pushJobLog.setEventTime(record.getDate(COL_EVENT_TIME));
			pushJobLogs.add(pushJobLog);
		}
		return pushJobLogs;
	}
	
	@Override
	public List<PushJobError> watchError(Long pushJobId) {
		List<PushJobError> pushJobErrors = new ArrayList<PushJobError>();
		
		StringBuilder sql = new StringBuilder("select * from " + TABLE_PUSH_LOG + " where " + COL_PUSH_JOB_ID + "=? ");
		List<Record> records = Db.find(sql.toString(), pushJobId);
		for (Record record : records) {
			PushJobError pushJobError = new PushJobError();
			pushJobError.setId(record.getLong(COL_ID));
			pushJobError.setCreateDate(record.getDate(COL_CREATE_DATE));
			pushJobError.setModifyDate(record.getDate(COL_MODIFY_DATE));
			pushJobError.setPushJobLogId(record.getLong(COL_PUSH_JOB_LOG_ID));
			pushJobError.setErrorMsg(record.getStr(COL_ERROR_MSG));
			pushJobErrors.add(pushJobError);
		}
		return pushJobErrors;
	}
	
	@SuppressWarnings("rawtypes")
	private List<Map> deserialization(String target) {
		if (target.startsWith("[")) {
			return JSON.parseArray(target, Map.class);
		} else {
			Map map = JSON.parseObject(target, Map.class);
			return Arrays.asList(map);
		}
	}
	
}
