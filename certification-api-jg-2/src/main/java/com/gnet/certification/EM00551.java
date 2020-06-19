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
import com.gnet.job.AsyncStatisticsTeacherCourseEduclass;
import com.gnet.model.admin.CcReportBuildStatus;
import com.gnet.model.admin.CcTeacherCourse;
import com.gnet.plugin.quartz.QuartzKit;
import com.gnet.plugin.quartz.callback.ITaskMonitor;
import com.gnet.service.CcResultStatisticsService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;
import com.jfinal.log.Logger;

/**
 * 课程下的教学班达成度报表生成接口（考核或评分）
 * 
 * @author wct
 * @date 2016年7月8日
 */
@Transactional(readOnly = false)
@Service("EM00551")
public class EM00551 extends BaseApi implements IApi {
	
	private static final Logger logger = Logger.getLogger(EM00551.class);

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long teacherCourseId = paramsLongFilter(params.get("teacherCourseId"));
		// 教师课程编号为空过滤
		if (teacherCourseId == null) {
			return renderFAIL("0506", response, header);
		}
		// 获得教师开课课程
		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findFilteredByIdWithVersion(teacherCourseId);
		if (ccTeacherCourse == null) {
			return renderFAIL("0501", response, header);
		}
		
		final CcResultStatisticsService ccResultStatisticsService = SpringContextHolder.getBean(CcResultStatisticsService.class);
		final String missionKey = teacherCourseId.toString() + "异步统计教师开课课程下的教学班达成度数据";
		Map<String, Object> jobData = Maps.newHashMap();
		jobData.put("teacherCourseId", teacherCourseId);
		jobData.put("resultType", ccTeacherCourse.getInt("result_type"));
		jobData.put("missionKey", missionKey);

		// 选择分析法
		boolean isSuccess = Boolean.TRUE;
		try {
			
			// 上一个任务还未结束, 不开启新的运算任务
			if (QuartzKit.checkJobExists(missionKey)) {
				return renderSUC(isSuccess, response, header);
			}
			
			// 创建任务执行记录
			if (!ccResultStatisticsService.createReportBuildRecord(missionKey, CcReportBuildStatus.TYPE_TEACHERCOURSE_EDUCLASS, ccTeacherCourse.getLong("version_id"))) {
				logger.error(new StringBuilder("创建").append(missionKey).append("任务记录时发生错误").toString());
				isSuccess = Boolean.FALSE;
			}
			
			QuartzKit.createTaskStartNow(missionKey, AsyncStatisticsTeacherCourseEduclass.class, jobData, Boolean.FALSE, SimpleScheduleBuilder.simpleSchedule(), new ITaskMonitor() {
				
				@Override
				public void taskStart() {
					ccResultStatisticsService.updateReportBuildRecord(missionKey, CcReportBuildStatus.TYPE_TEACHERCOURSE_EDUCLASS, CcReportBuildStatus.STATUS_TASK_START, null, null);
					logger.info(new StringBuilder("开始执行").append(missionKey).toString());
				}
				
				@Override
				public void taskFinish() {
					logger.info(new StringBuilder(missionKey).append("执行结束，任务删除").toString());
				}
				
				@Override
				public void taskFail() {
					ccResultStatisticsService.updateReportBuildRecord(missionKey, CcReportBuildStatus.TYPE_TEACHERCOURSE_EDUCLASS, CcReportBuildStatus.STATUS_TASK_FAIL, Boolean.TRUE, null);
					logger.info(new StringBuilder(missionKey).append("执行结束，任务删除").toString());
				}
				
			});
		
		} catch (SchedulerException e) {
			ccResultStatisticsService.updateReportBuildRecord(missionKey, CcReportBuildStatus.TYPE_TEACHERCOURSE_EDUCLASS, CcReportBuildStatus.STATUS_TASK_CREATEFAIL, null, null);
			logger.error(new StringBuilder("创建").append(missionKey).append("任务时发生错误").toString(), e);
			isSuccess = Boolean.FALSE;
		}
		
		if (isSuccess) {
			ccResultStatisticsService.updateReportBuildRecord(missionKey, CcReportBuildStatus.TYPE_TEACHERCOURSE_EDUCLASS, CcReportBuildStatus.STATUS_TASK_CREATESUCCESS, null, null);
		}
		
		Map<String, Object> result = Maps.newHashMap();
		result.put("isSuccess", isSuccess);
		
		return renderSUC(result, response, header);
	}
}
