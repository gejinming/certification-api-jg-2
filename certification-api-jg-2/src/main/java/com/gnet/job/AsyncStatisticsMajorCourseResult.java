package com.gnet.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.gnet.model.admin.CcReportBuildStatus;
import com.gnet.service.CcReportBuildStatusService;
import com.gnet.service.CcResultStatisticsService;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.log.Logger;

/**
 * 统计专业下的课程达成度与专业下指标点达成度并保存到表中
 * 
 * @author wct
 * @date 2016年7月21日
 */
public class AsyncStatisticsMajorCourseResult implements Job {
	
	private static final Logger logger = Logger.getLogger(AsyncStatisticsMajorCourseResult.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataMap = context.getMergedJobDataMap();
		Integer grade = dataMap.getInt("grade");
		Long versionId = dataMap.getLong("versionId");
		String name = CcReportBuildStatusService.getReportBuildKeyForCourse(grade, versionId);
		Long timeNow = System.currentTimeMillis();
		
		CcResultStatisticsService ccResultStatisticsService = SpringContextHolder.getBean(CcResultStatisticsService.class);
		
//		// 判断是否需要更新考核分析法的教学班统计表，如果有需要则进行更新
//		// 判断是否需要更新考评点分析法的教学班统计表，如果有需要则进行更新 【废弃】
//		// 统计生成课程达成度统计表有关数据
//		try {
//			if (!ccResultStatisticsService.updateEduclassStatisticsScore(grade, versionId) 
//					|| !ccResultStatisticsService.updateEduclassStatisticsEvalute(grade, versionId)
//					|| !ccResultStatisticsService.statisticsCourseResultForJG(grade, versionId))
		// 同级生成课程达成度报表数据
		try {
			if(!ccResultStatisticsService.statisticsCourseResultForJG(grade, versionId)
				|| !ccResultStatisticsService.statisticsCourseResultForJGExcept(grade, versionId)
				)
			{
				
				ccResultStatisticsService.updateReportBuildRecord(name, CcReportBuildStatus.TYPE_COURSEINDICATION, CcReportBuildStatus.STATUS_TASK_FAIL, Boolean.TRUE, null);
				
				logger.error(new StringBuilder("达成度计算：").append(name).append("发生错误终止失败").toString());
				return;
			}
			
		} catch(Exception e) {
			ccResultStatisticsService.updateReportBuildRecord(name, CcReportBuildStatus.TYPE_COURSEINDICATION, CcReportBuildStatus.STATUS_TASK_FAIL, Boolean.TRUE, null);
			logger.error(new StringBuilder("达成度计算：").append(name).append("发生错误终止失败").toString(), e);
			return;
		}
		
		
		Long timesElapse = System.currentTimeMillis() - timeNow;
		ccResultStatisticsService.updateReportBuildRecord(name, CcReportBuildStatus.TYPE_COURSEINDICATION, CcReportBuildStatus.STATUS_TASK_SUCCESS, Boolean.TRUE, timesElapse);
		logger.info(new StringBuilder("完成执行").append(name).toString());
		
	}
	
	
}
