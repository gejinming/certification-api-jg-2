package com.gnet.job;

import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.gnet.model.admin.CcReportBuildStatus;
import com.gnet.service.CcPlanStatisticsService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;

/**
 * 异步创建更新培养计划的课程区域并统计合计数据
 * 
 * @author wct
 * @date 2016年8月4日
 */
public class AsyncStatisticsPlanReport implements Job {
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataMap = context.getMergedJobDataMap();
		Long planId = dataMap.getLong("planId");
		String missionKey = dataMap.getString("missionKey");
		Long timeNow = System.currentTimeMillis();
		
		// 记录任务创建的错误日志
		Map<String, Object> msgMap = Maps.newHashMap();
		CcPlanStatisticsService ccPlanStatisticsService = SpringContextHolder.getBean(CcPlanStatisticsService.class);
		if (!ccPlanStatisticsService.statisticsPlanReport(planId, msgMap)) {
			ccPlanStatisticsService.updateReportBuildRecord(missionKey, CcReportBuildStatus.TYPE_PLANREPORT, CcReportBuildStatus.STATUS_TASK_FAIL, Boolean.TRUE, null, msgMap.get("errorMsg") == null ? null : msgMap.get("errorMsg").toString());
		} else {
			ccPlanStatisticsService.updateReportBuildRecord(missionKey, CcReportBuildStatus.TYPE_PLANREPORT, CcReportBuildStatus.STATUS_TASK_SUCCESS, Boolean.TRUE, System.currentTimeMillis() - timeNow);
		}
		
	}
}
