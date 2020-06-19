package com.gnet.job;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.gnet.model.admin.CcEduclass;
import com.gnet.model.admin.CcReportBuildStatus;
import com.gnet.service.CcResultStatisticsService;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.log.Logger;

/**
 * 教师开课课程下的教学班达成度分析异步任务
 * 
 * @author wct
 * @date 2016年8月10日
 */
public class AsyncStatisticsTeacherCourseEduclass implements Job {
	
	private static final Logger logger = Logger.getLogger(AsyncStatisticsTeacherCourseEduclass.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		Long teacherCourseId = jobDataMap.getLong("teacherCourseId");
		Integer resultType = jobDataMap.getInt("resultType");
		String missionKey = jobDataMap.getString("missionKey");
		Long timeNow = System.currentTimeMillis();
		
		// 获得所有教学班
		List<CcEduclass> ccEduclasses = CcEduclass.dao.findFilteredByColumn("teacher_course_id", teacherCourseId);
		CcResultStatisticsService ccResultStatisticsService = SpringContextHolder.getBean(CcResultStatisticsService.class);
		
		if (!ccEduclasses.isEmpty()) {
			for (CcEduclass ccEduclass : ccEduclasses) {
				if (!ccResultStatisticsService.statisticsEduclassResult(ccEduclass.getLong("id"), resultType)) {
					ccResultStatisticsService.updateReportBuildRecord(missionKey, CcReportBuildStatus.TYPE_TEACHERCOURSE_EDUCLASS, CcReportBuildStatus.STATUS_TASK_FAIL, Boolean.TRUE, null);
					
					logger.error(new StringBuilder("达成度计算：").append(missionKey).append("发生错误终止失败").toString());
					return;
				}
			}
		}
		
		Long timesElapse = System.currentTimeMillis() - timeNow;
		ccResultStatisticsService.updateReportBuildRecord(missionKey, CcReportBuildStatus.TYPE_TEACHERCOURSE_EDUCLASS, CcReportBuildStatus.STATUS_TASK_SUCCESS, Boolean.TRUE, timesElapse);
		logger.info(new StringBuilder("完成执行").append(missionKey).toString());
	}

}
