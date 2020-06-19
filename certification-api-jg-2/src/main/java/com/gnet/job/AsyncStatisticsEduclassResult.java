package com.gnet.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.gnet.model.admin.CcReportBuildStatus;
import com.gnet.service.CcEdupointAimsAchieveService;
import com.gnet.service.CcEdupointEachAimsAchieveService;
import com.gnet.service.CcResultStatisticsService;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.log.Logger;


/**
 * 教学班达成度分析异步任务
 * 
 * @author wct
 * @date 2016年8月10日
 */
public class AsyncStatisticsEduclassResult implements Job {
	
	private static final Logger logger = Logger.getLogger(AsyncStatisticsEduclassResult.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		Long eduClassId = jobDataMap.getLong("eduClassId");
		Integer resultType = jobDataMap.getInt("resultType");
		String missionKey = jobDataMap.getString("missionKey");
		Boolean isExcept = jobDataMap.containsKey("isExcept") ? jobDataMap.getBoolean("isExcept") : null;
		Long timeNow = System.currentTimeMillis();
		
		CcResultStatisticsService ccResultStatisticsService = SpringContextHolder.getBean(CcResultStatisticsService.class);
		CcEdupointAimsAchieveService ccEdupointAimsAchieveService = SpringContextHolder.getBean(CcEdupointAimsAchieveService.class);
		CcEdupointEachAimsAchieveService ccEdupointEachAimsAchieveService = SpringContextHolder.getBean(CcEdupointEachAimsAchieveService.class);
		
		// 如实isExcept是ture，就是计算剔除学生之后的数据
		if(isExcept == null) {
			// 什么都既没有，默认全部操作
			/*
			 * 1. 统计教学班数据 [TODO SY 这个好像是重复的，我准备删掉了]
			 * 2. 统计教学班下各个课程目标的达成度(考核分析法)
			 * 3. 统计教学班&&课程指标点数据下的课程目标达成度(考核分析法)
			 */
			if (
//									!ccResultStatisticsService.statisticsEduclassResult(eduClassId)
//									|| 
					!ccEdupointEachAimsAchieveService.statisticsEdupointEachAimsAchieve(eduClassId)
					|| !ccEdupointAimsAchieveService.statisticsEdupointAimsAchieve(eduClassId)
					) {
//							if (!ccResultStatisticsService.statisticsEduclassResult(eduClassId, resultType)) {
				ccResultStatisticsService.updateReportBuildRecord(missionKey, CcReportBuildStatus.TYPE_EDUCLASS, CcReportBuildStatus.STATUS_TASK_FAIL, Boolean.TRUE, null);
				
				logger.error(new StringBuilder("达成度计算：").append(missionKey).append("发生错误终止失败").toString());
				return;
			}
			/*
			 * 1. 统计教学班数据 [TODO SY 这个好像是重复的，我准备删掉了]
			 * 2. 统计教学班下各个课程目标的达成度(考核分析法)
			 * 3. 统计教学班&&课程指标点数据下的课程目标达成度(考核分析法)
			 */
			if (
//									!ccResultStatisticsService.statisticsEduclassResult(eduClassId)
	//				|| 
					!ccEdupointEachAimsAchieveService.statisticsEdupointEachAimsAchieveExcept(eduClassId)
					|| !ccEdupointAimsAchieveService.statisticsEdupointAimsAchieveExcept(eduClassId)
					) {
	//						if (!ccResultStatisticsService.statisticsEduclassResult(eduClassId, resultType)) {
				ccResultStatisticsService.updateReportBuildRecord(missionKey, CcReportBuildStatus.TYPE_EDUCLASS, CcReportBuildStatus.STATUS_TASK_FAIL, Boolean.TRUE, null);
				
				logger.error(new StringBuilder("达成度计算：").append(missionKey).append("发生错误终止失败").toString());
				return;
			}
		} else if(isExcept) {
			// 当时true，即要求计算剔除后的学生成绩时候
			/*
			 * 1. 统计教学班数据 [TODO SY 这个好像是重复的，我准备删掉了]
			 * 2. 统计教学班下各个课程目标的达成度(考核分析法)
			 * 3. 统计教学班&&课程指标点数据下的课程目标达成度(考核分析法)
			 */
			if (
//					!ccResultStatisticsService.statisticsEduclassResult(eduClassId)
	//				|| 
					!ccEdupointEachAimsAchieveService.statisticsEdupointEachAimsAchieveExcept(eduClassId)
					|| !ccEdupointAimsAchieveService.statisticsEdupointAimsAchieveExcept(eduClassId)
					) {
	//						if (!ccResultStatisticsService.statisticsEduclassResult(eduClassId, resultType)) {
				ccResultStatisticsService.updateReportBuildRecord(missionKey, CcReportBuildStatus.TYPE_EDUCLASS, CcReportBuildStatus.STATUS_TASK_FAIL, Boolean.TRUE, null);
				
				logger.error(new StringBuilder("达成度计算：").append(missionKey).append("发生错误终止失败").toString());
				return;
			}
		} else {
			// 当时false的时候
			/*
			 * 1. 统计教学班数据 [TODO SY 这个好像是重复的，我准备删掉了]
			 * 2. 统计教学班下各个课程目标的达成度(考核分析法)
			 * 3. 统计教学班&&课程指标点数据下的课程目标达成度(考核分析法)
			 */
			if (
//								!ccResultStatisticsService.statisticsEduclassResult(eduClassId)
//								|| 
					!ccEdupointEachAimsAchieveService.statisticsEdupointEachAimsAchieve(eduClassId)
					|| !ccEdupointAimsAchieveService.statisticsEdupointAimsAchieve(eduClassId)
					) {
//						if (!ccResultStatisticsService.statisticsEduclassResult(eduClassId, resultType)) {
				ccResultStatisticsService.updateReportBuildRecord(missionKey, CcReportBuildStatus.TYPE_EDUCLASS, CcReportBuildStatus.STATUS_TASK_FAIL, Boolean.TRUE, null);
				
				logger.error(new StringBuilder("达成度计算：").append(missionKey).append("发生错误终止失败").toString());
				return;
			}
		}
		Long timesElapse = System.currentTimeMillis() - timeNow;
		ccResultStatisticsService.updateReportBuildRecord(missionKey, CcReportBuildStatus.TYPE_EDUCLASS, CcReportBuildStatus.STATUS_TASK_SUCCESS, Boolean.TRUE, timesElapse);
		logger.info(new StringBuilder("完成执行").append(missionKey).toString());
		
	}

}
