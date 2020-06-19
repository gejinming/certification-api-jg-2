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
import com.gnet.job.AsyncStatisticsMajorCourseResult;
import com.gnet.model.admin.CcReportBuildStatus;
import com.gnet.plugin.quartz.QuartzKit;
import com.gnet.plugin.quartz.callback.ITaskMonitor;
import com.gnet.service.CcReportBuildStatusService;
import com.gnet.service.CcResultStatisticsService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;
import com.jfinal.log.Logger;

/**
 * 课程达成度生成报表
 * 
 * @author wct
 * @date 2016年7月13日
 */
@Transactional(readOnly = false)
@Service("EM00554")
public class EM00554 extends BaseApi implements IApi {
	
	private static final Logger logger = Logger.getLogger(EM00554.class);

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		Integer grade = paramsIntegerFilter(params.get("grade"));
		final Long versionId = paramsLongFilter(params.get("versionId"));
		// 年级为空过滤
		if (grade == null) {
			return renderFAIL("0521", response, header);
		}
		// 版本编号为空过滤
		if (versionId == null) {
			return renderFAIL("0522", response, header);
		}
		
		final CcResultStatisticsService ccResultStatisticsService = SpringContextHolder.getBean(CcResultStatisticsService.class);
		
		Map<String, Object> jobData = Maps.newHashMap();
		jobData.put("grade", grade);
		jobData.put("versionId", versionId);
		final String name = CcReportBuildStatusService.getReportBuildKeyForCourse(grade, versionId);
		
		boolean isSuccess = Boolean.TRUE;
		try {
			
			// 上一个任务还未结束, 不开启新的运算任务
			if (QuartzKit.checkJobExists(name)) {
				return renderSUC(isSuccess, response, header);
			}
			
			// 创建任务执行记录
			if (!ccResultStatisticsService.createReportBuildRecord(name, CcReportBuildStatus.TYPE_COURSEINDICATION, versionId)) {
				logger.error(new StringBuilder("创建").append(name).append("任务记录时发生错误").toString());
				isSuccess = Boolean.FALSE;
			}
			
			QuartzKit.createTaskStartNow(name, AsyncStatisticsMajorCourseResult.class, jobData, Boolean.FALSE, SimpleScheduleBuilder.simpleSchedule(), new ITaskMonitor() {
				
				@Override
				public void taskStart() {
					ccResultStatisticsService.updateReportBuildRecord(name, CcReportBuildStatus.TYPE_COURSEINDICATION, CcReportBuildStatus.STATUS_TASK_START, null, null);
					logger.info(new StringBuilder("开始执行").append(name).toString());
				}
				
				@Override
				public void taskFinish() {
					logger.info(new StringBuilder(name).append("执行结束，任务删除").toString());
				}
				
				@Override
				public void taskFail() {
					ccResultStatisticsService.updateReportBuildRecord(name, CcReportBuildStatus.TYPE_COURSEINDICATION, CcReportBuildStatus.STATUS_TASK_FAIL, Boolean.TRUE, null);
					logger.info(new StringBuilder(name).append("执行结束，任务删除").toString());
				}
				
			});
		
		} catch (SchedulerException e) {
			ccResultStatisticsService.updateReportBuildRecord(name, CcReportBuildStatus.TYPE_COURSEINDICATION, CcReportBuildStatus.STATUS_TASK_CREATEFAIL, null, null);
			logger.error(new StringBuilder("创建").append(name).append("任务时发生错误").toString(), e);
			isSuccess = Boolean.FALSE;
		}
		
		if (isSuccess) {
			ccResultStatisticsService.updateReportBuildRecord(name, CcReportBuildStatus.TYPE_COURSEINDICATION, CcReportBuildStatus.STATUS_TASK_CREATESUCCESS, null, null);
		}
		
		Map<String, Object> result = Maps.newHashMap();
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}

}
