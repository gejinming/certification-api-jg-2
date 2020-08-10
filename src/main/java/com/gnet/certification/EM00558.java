package com.gnet.certification;


import java.util.Map;

import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.job.AsyncStatisticsPersonalCourseResult;
import com.gnet.model.admin.CcReportBuildStatus;
import com.gnet.plugin.quartz.QuartzKit;
import com.gnet.plugin.quartz.callback.ITaskMonitor;
import com.gnet.service.CcResultStatisticsService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;
import com.jfinal.log.Logger;

/**
 * 学生个人达成度计算
 * 
 * @author wct
 * @date 2016年7月25日
 */
@Transactional(readOnly = false)
@Service("EM00558")
public class EM00558 extends BaseApi implements IApi {
	
	private static final Logger logger = Logger.getLogger(EM00558.class);

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Integer grade = paramsIntegerFilter(params.get("grade"));
		Long versionId = paramsLongFilter(params.get("versionId"));
		// 版本编号不能为空
		if (versionId == null) {
			return renderFAIL("0521", response, header);
		}
		// 年级不能为空
		if (grade == null) {
			return renderFAIL("0522", response, header);
		}
		
		final CcResultStatisticsService ccResultStatisticsService = SpringContextHolder.getBean(CcResultStatisticsService.class);
		
		final String name = grade.toString() + versionId.toString() + "异步统计个人达成度数据";
		Map<String, Object> jobData = Maps.newHashMap();
		jobData.put("grade", grade);
		jobData.put("versionId", versionId);
		jobData.put("jobName", name);
		
		boolean isSuccess = Boolean.TRUE;
		try {
			
			// 上一个任务还未结束, 不开启新的运算任务
			if (QuartzKit.checkJobExists(name)) {
				return renderSUC(isSuccess, response, header);
			}
			
			// 创建任务执行记录
			if (!ccResultStatisticsService.createReportBuildRecord(name, CcReportBuildStatus.TYPE_STUDENT, versionId)) {
				logger.error(new StringBuilder("创建").append(name).append("任务记录时发生错误").toString());
				isSuccess = Boolean.FALSE;
			}
			
			QuartzKit.createTaskStartNow(name, AsyncStatisticsPersonalCourseResult.class, jobData, Boolean.FALSE, SimpleScheduleBuilder.simpleSchedule(), new ITaskMonitor() {
				
				@Override
				public void taskStart() {
					ccResultStatisticsService.updateReportBuildRecord(name, CcReportBuildStatus.TYPE_STUDENT, CcReportBuildStatus.STATUS_TASK_START, null, null);
					logger.info(new StringBuilder("开始执行").append(name).toString());
				}
				
				@Override
				public void taskFinish() {
					logger.info(new StringBuilder(name).append("执行结束，任务删除").toString());
				}
				
				@Override
				public void taskFail() {
					ccResultStatisticsService.updateReportBuildRecord(name, CcReportBuildStatus.TYPE_STUDENT, CcReportBuildStatus.STATUS_TASK_FAIL, Boolean.TRUE, null);
					logger.info(new StringBuilder(name).append("执行结束，任务删除").toString());
				}
				
			});
		
		} catch (SchedulerException e) {
			ccResultStatisticsService.updateReportBuildRecord(name, CcReportBuildStatus.TYPE_STUDENT, CcReportBuildStatus.STATUS_TASK_CREATEFAIL, null, null);
			logger.error(new StringBuilder("创建").append(name).append("任务时发生错误").toString(), e);
			isSuccess = Boolean.FALSE;
		}
		
		if (isSuccess) {
			ccResultStatisticsService.updateReportBuildRecord(name, CcReportBuildStatus.TYPE_STUDENT, CcReportBuildStatus.STATUS_TASK_CREATESUCCESS, null, null);
		}
		
		// 返回结果
		Map<String, Object> result = Maps.newHashMap();
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}
	

}
