package com.gnet.certification;

import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.job.AsyncStatisticsPlanReport;

import com.gnet.model.admin.CcReportBuildStatus;
import com.gnet.model.admin.CcVersion;
import com.gnet.plugin.quartz.QuartzKit;
import com.gnet.plugin.quartz.callback.ITaskMonitor;
import com.gnet.service.CcPlanStatisticsService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;

/**
 * 培养计划报表生成接口
 * 
 * @author wct
 * @date 2016年8月2日
 */
@Transactional(readOnly = false)
@Service("EM00655")
public class EM00655 extends BaseApi implements IApi {
	
	private static final Logger logger = Logger.getLogger(EM00655.class);

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long planId = paramsLongFilter(params.get("planId"));
		Long majorId = paramsLongFilter(params.get("majorId"));
		if(params.containsKey("majorId") && majorId == null){
			return renderFAIL("1009", response, header, "majorId的参数值非法");
		}
		Integer grade = paramsIntegerFilter(params.get("grade"));
		if(params.containsKey("grade") && grade == null){
			return renderFAIL("1009", response, header, "grade的参数值非法");
		}
		if(majorId != null && grade != null){
			planId = CcVersion.dao.findNewestVersion(majorId, grade);
			if (planId == null) {
				return renderFAIL("0671", response, header);
			}
		}
		// 培养计划编号不能为空
		if (planId == null) {
			return renderFAIL("0660", response, header);
		}
		
		final CcPlanStatisticsService ccPlanStatisticsService = SpringContextHolder.getBean(CcPlanStatisticsService.class);		
		
		final String missionKey = planId.toString() + "异步统计培养计划数据";
		Map<String, Object> jobData = Maps.newHashMap();
		jobData.put("planId", planId);
		jobData.put("missionKey", missionKey);
		
		boolean isSuccess = Boolean.TRUE;
		
		try {
			// 上一个任务还未结束，不开启新的运算任务
			if (QuartzKit.checkJobExists(missionKey)) {
				return renderSUC(isSuccess, response, header);
			}
			
			// 创建任务执行记录
			if (!ccPlanStatisticsService.createReportBuildRecord(missionKey, CcReportBuildStatus.TYPE_PLANREPORT, planId)) {
				logger.error(new StringBuilder("创建").append(missionKey).append("任务记录时发生错误").toString());
				isSuccess = Boolean.FALSE;
			}
			
			// 创建生成培养计划报表任务
			QuartzKit.createTaskStartNow(missionKey, AsyncStatisticsPlanReport.class, jobData, SimpleScheduleBuilder.simpleSchedule(), new ITaskMonitor() {
				
				@Override
				public void taskStart() {
					logger.info(new StringBuilder("创建").append(missionKey).append("任务记录成功").toString());
				}
				
				@Override
				public void taskFinish() {
					logger.info(new StringBuilder(missionKey).append("执行结束，任务删除").toString());
					
				}
				
				@Override
				public void taskFail() {
					ccPlanStatisticsService.updateReportBuildRecord(missionKey, CcReportBuildStatus.TYPE_PLANREPORT, CcReportBuildStatus.STATUS_TASK_FAIL, Boolean.TRUE, null);
					logger.info(new StringBuilder(missionKey).append("执行结束，任务删除").toString());
				}
			});
			
		} catch (SchedulerException e) {
			ccPlanStatisticsService.updateReportBuildRecord(missionKey, CcReportBuildStatus.TYPE_PLANREPORT, CcReportBuildStatus.STATUS_TASK_CREATEFAIL, null, null);
			logger.error(new StringBuilder("创建").append(missionKey).append("任务时发生错误").toString(), e);
			isSuccess = Boolean.FALSE;
		}
		
		if (isSuccess) {
			ccPlanStatisticsService.updateReportBuildRecord(missionKey, CcReportBuildStatus.TYPE_PLANREPORT, CcReportBuildStatus.STATUS_TASK_CREATESUCCESS, null, null);
		}
		
		// 结果返回
		Map<String, Object> result = Maps.newHashMap();
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}
	

}
