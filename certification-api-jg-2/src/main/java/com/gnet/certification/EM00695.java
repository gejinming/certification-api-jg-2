package com.gnet.certification;

import java.util.List;
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
import com.gnet.model.admin.CcReportCourse;
import com.gnet.model.admin.CcScoreStuIndigrade;
import com.gnet.model.admin.CcStudentEvalute;
import com.gnet.model.admin.CcVersion;
import com.gnet.plugin.quartz.QuartzKit;
import com.gnet.plugin.quartz.callback.ITaskMonitor;
import com.gnet.service.CcReportBuildStatusService;
import com.gnet.service.CcResultStatisticsService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;

/**
 * 课程历史对比度报表生成
 * 
 * @author xzl
 * @date 2017年1月21日
 */
@Transactional(readOnly = false)
@Service("EM00695")
public class EM00695 extends BaseApi implements IApi {
	
	private static final Logger logger = Logger.getLogger(EM00695.class);

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {		
		Map<String, Object> params = request.getData();
		Map<String, Object> result = Maps.newHashMap();
		String courseCode = paramsStringFilter(params.get("courseCode"));
		Long majorId = paramsLongFilter(params.get("majorId"));
		// 课程代码不能为空
		if (StrKit.isBlank(courseCode)) {
			return renderFAIL("0670", response, header);
		}
		
		if(majorId == null){
			return renderFAIL("0130", response, header);
		}
		
		List<CcVersion> versionAndGradeList1 = CcScoreStuIndigrade.dao.getVersionAndGrade(courseCode, majorId);
		List<CcVersion> versionAndGradeList2 = CcStudentEvalute.dao.getVersionAndGrade(courseCode, majorId);
		List<CcVersion> versionAndGradeList3 = CcReportCourse.dao.getByReportEduclassGradeAndCourseCode(courseCode, majorId);
		List<CcVersion> versionAndGradeList4 = CcReportCourse.dao.getByReportEduclassEvaluteAndCourseCode(courseCode, majorId);
		List<CcVersion> versionAndGradeList5 = CcReportCourse.dao.getByStudentEvaluteAndCourseCode(courseCode, majorId);
		List<CcVersion> versionAndGradeList6 = CcReportCourse.dao.getByScoreStuIndigradeAndCourseCode(courseCode, majorId);
		versionAndGradeList1.addAll(versionAndGradeList2);
		versionAndGradeList1.addAll(versionAndGradeList3);
		versionAndGradeList1.addAll(versionAndGradeList4);
		versionAndGradeList1.addAll(versionAndGradeList5);
		versionAndGradeList1.addAll(versionAndGradeList6);
		if(versionAndGradeList1.isEmpty()){
			result.put("isSuccess", true);
		}
		//过滤重复的grade和versionId
		Map<String, CcVersion> unrepeatMap = Maps.newHashMap();
		for(CcVersion ccVersion : versionAndGradeList1){
			String key = new StringBuilder(ccVersion.getLong("versionId").toString()).append(",").append(ccVersion.getInt("grade")).toString();
			if(unrepeatMap.get(key) == null){
				unrepeatMap.put(key, ccVersion);
			}
		}
		
		final CcResultStatisticsService ccResultStatisticsService = SpringContextHolder.getBean(CcResultStatisticsService.class);
		for(Map.Entry<String, CcVersion> entry : unrepeatMap.entrySet()){
			CcVersion ccVersion = entry.getValue();
			Integer grade = ccVersion.getInt("grade");
			Long versionId =  ccVersion.getLong("versionId");
			
			Map<String, Object> jobData = Maps.newHashMap();
			jobData.put("grade", grade);
			jobData.put("versionId", versionId);
			final String name = CcReportBuildStatusService.getReportBuildKeyForCourse(grade, versionId);
			
			boolean isSuccess = Boolean.TRUE;
			try {
				
				// 上一个任务还未结束, 不开启新的运算任务
				if (QuartzKit.checkJobExists(name)) {
					continue;
				}
				
				// 创建任务执行记录
				if (!ccResultStatisticsService.createReportBuildRecord(name, CcReportBuildStatus.TYPE_COURSEINDICATION, versionId)) {
					logger.error(new StringBuilder("创建").append(name).append("任务记录时发生错误").toString());
					isSuccess = Boolean.FALSE;
					result.put("isSuccess", Boolean.FALSE);
					return renderSUC(result, response, header);
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
				result.put("isSuccess", Boolean.FALSE);
				return renderSUC(result, response, header);
			}
			
			if (isSuccess) {
				ccResultStatisticsService.updateReportBuildRecord(name, CcReportBuildStatus.TYPE_COURSEINDICATION, CcReportBuildStatus.STATUS_TASK_CREATESUCCESS, null, null);
			}
			
		}
		
		result.put("gradeVersionList", versionAndGradeList1);
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}
