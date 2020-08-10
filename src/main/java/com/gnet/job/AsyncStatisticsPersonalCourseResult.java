package com.gnet.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.gnet.model.admin.CcReportBuildStatus;
import com.gnet.service.CcResultStatisticsService;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.log.Logger;

/**
 * 某个版本某个年级下的个人报表统计异步任务
 * 
 * @author wct
 * @date 2016年7月31日
 */
public class AsyncStatisticsPersonalCourseResult implements Job {
	
	private static final Logger logger = Logger.getLogger(AsyncStatisticsPersonalCourseResult.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataMap = context.getMergedJobDataMap();
		Integer grade = dataMap.getInt("grade");
		Long versionId = dataMap.getLong("versionId");
		String name = dataMap.getString("jobName");
		Long timeNow = System.currentTimeMillis();
		
		CcResultStatisticsService ccResultStatisticsService = SpringContextHolder.getBean(CcResultStatisticsService.class);
		// 统计个人达成度数据
		try {
			if (!ccResultStatisticsService.statisticsPersonalCourseAndIndication(versionId, grade)) {
				ccResultStatisticsService.updateReportBuildRecord(name, CcReportBuildStatus.TYPE_STUDENT, CcReportBuildStatus.STATUS_TASK_FAIL, Boolean.TRUE, null);
				
				logger.error(new StringBuilder("达成度计算：").append(name).append("发生错误终止失败").toString());
				return;
			}
			
		} catch(Exception e) {
			ccResultStatisticsService.updateReportBuildRecord(name, CcReportBuildStatus.TYPE_STUDENT, CcReportBuildStatus.STATUS_TASK_FAIL, Boolean.TRUE, null);
			logger.error(new StringBuilder("达成度计算：").append(name).append("发生错误终止失败").toString(), e);
			return;
		}
		
		
		Long timesElapse = System.currentTimeMillis() - timeNow;
		ccResultStatisticsService.updateReportBuildRecord(name, CcReportBuildStatus.TYPE_STUDENT, CcReportBuildStatus.STATUS_TASK_SUCCESS, Boolean.TRUE, timesElapse);
		logger.info(new StringBuilder("完成执行").append(name).toString());
	}

}
