package com.gnet.plugin.push.db;

import java.math.BigDecimal;
import java.util.Date;

import com.gnet.plugin.push.kit.DecimalKit;
import com.gnet.plugin.quartz.db.IJdbcPushConstants;
import com.gnet.plugin.quartz.db.IJdbcJob;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * JFinal推送任务数据记录
 * 
 * @author xuq
 * @date 2015年11月19日
 * @version 1.1
 */
public abstract class JFinalJdbcPushImpl implements IJdbcJob, IJdbcPushConstants {

	@Override
	public Long recordCreate(String jobContent, Date date) {
		Record pushJob = new Record();
		pushJob.set(COL_CREATE_DATE, date);
		pushJob.set(COL_MODIFY_DATE, date);
		pushJob.set(COL_PROGRESS, DecimalKit.getZero());
		pushJob.set(COL_JOB_CONTENT, jobContent);
		if (Db.save(TABLE_PUSH, pushJob)) {
			return pushJob.getLong(COL_ID);
		} else {
			return null;
		}
	}
	
	@Override
	public boolean recordStart(Long pushJobId, Date date) {
		Record pushJob = new Record();
		pushJob.set(COL_ID, pushJobId);
		pushJob.set(COL_MODIFY_DATE, date);
		pushJob.set(COL_START_TIME, date);
		if (!Db.update(TABLE_PUSH, pushJob)) {
			return false;
		}
		
		Record pushJobLog = new Record();
		pushJobLog.set(COL_CREATE_DATE, date);
		pushJobLog.set(COL_MODIFY_DATE, date);
		pushJobLog.set(COL_PUSH_JOB_ID, pushJob.getLong(COL_ID));
		pushJobLog.set(COL_EVENT_TYPE, EVENT_TYPE_START);
		pushJobLog.set(COL_EVENT_TIME, date);
		return Db.save(TABLE_PUSH_LOG, pushJobLog);
	}

	@Override
	public boolean recordFail(Long pushJobId, Throwable t, Date date) {
		Record pushJob = new Record();
		pushJob.set(COL_ID, pushJobId);
		pushJob.set(COL_MODIFY_DATE, date);
		pushJob.set(COL_END_TIME, date);
		if (!Db.update(TABLE_PUSH, pushJob)) {
			return false;
		}
		
		Record pushJobLog = new Record();
		pushJobLog.set(COL_CREATE_DATE, date);
		pushJobLog.set(COL_MODIFY_DATE, date);
		pushJobLog.set(COL_PUSH_JOB_ID, pushJob.getLong(COL_ID));
		pushJobLog.set(COL_EVENT_TYPE, EVENT_TYPE_ERROR);
		pushJobLog.set(COL_EVENT_TIME, date);
		if (!Db.save(TABLE_PUSH_LOG, pushJobLog)) {
			return false;
		}
		
		Record pushJobError = new Record();
		pushJobError.set(COL_CREATE_DATE, date);
		pushJobError.set(COL_MODIFY_DATE, date);
		pushJobError.set(COL_PUSH_JOB_ID, pushJob.getLong(COL_ID));
		pushJobError.set(COL_PUSH_JOB_LOG_ID, pushJobLog.getLong(COL_ID));
		pushJobError.set(COL_ERROR_MSG, t.toString());
		return Db.save(TABLE_PUSH_ERROR, pushJobError);
	}

	@Override
	public boolean updateProgress(Long pushJobId, BigDecimal progress, Date date) {
		Record pushJob = new Record();
		pushJob.set(COL_ID, pushJobId);
		pushJob.set(COL_MODIFY_DATE, date);
		pushJob.set(COL_PROGRESS, progress.toString());
		return Db.update(TABLE_PUSH, pushJob);
	}

	@Override
	public boolean recordFinish(Long pushJobId, Date date) {
		Record pushJob = new Record();
		pushJob.set(COL_ID, pushJobId);
		pushJob.set(COL_MODIFY_DATE, date);
		pushJob.set(COL_END_TIME, date);
		if (!Db.update(TABLE_PUSH, pushJob)) {
			return false;
		}
		
		Record pushJobLog = new Record();
		pushJobLog.set(COL_CREATE_DATE, date);
		pushJobLog.set(COL_MODIFY_DATE, date);
		pushJobLog.set(COL_PUSH_JOB_ID, pushJob.getLong(COL_ID));
		pushJobLog.set(COL_EVENT_TYPE, EVENT_TYPE_FINISH);
		pushJobLog.set(COL_EVENT_TIME, date);
		return Db.save(TABLE_PUSH_LOG, pushJobLog);
	}

}
