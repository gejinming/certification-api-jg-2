package com.gnet.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.springframework.stereotype.Component;

import com.gnet.job.AsyncStatisticsEduclassResult;
import com.gnet.job.AsyncStatisticsMajorCourseResult;
import com.gnet.job.AsyncStatisticsPersonalCourseResult;
import com.gnet.model.admin.CcReportBuildStatus;
import com.gnet.model.admin.CcTeacherCourse;
import com.gnet.model.admin.CcVersion;
import com.gnet.plugin.quartz.QuartzKit;
import com.gnet.plugin.quartz.callback.ITaskMonitor;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;
import com.jfinal.log.Logger;

/**
 * 课程目标
 * 
 * @author SY
 * 
 * @date 2017年12月4日
 */
@Component("ccIndicationService")
public class CcIndicationService {
	
	private static final Logger logger = Logger.getLogger(CcIndicationService.class);
	
	/**
	 * 重新计算当前版本
	 * @param educlassIds
	 * 			教学班列表（这些教学班可以不属于同一个版本下）
	 * @author SY 
	 * @version 创建时间：2017年12月4日 下午4:20:31 
	 */
	public Boolean statisticsAllForJGByDiffrentEduClassId(Long eduClassIds[]) {
		if(eduClassIds == null || eduClassIds.length == 0) {
			return false;
		}
		// Map<versionId, List<educlassId>> ,让属于同一个版本下的一起计算
		Map<Long, List<Long>> versionEduclassIdListMap = new HashMap<>();
		Map<Long, CcVersion> versionMap = new HashMap<>();
		for(int i = 0; i < eduClassIds.length; i++) {
			Long educlassId = eduClassIds[i];
			CcVersion ccVersion = CcVersion.dao.findByEduClassId(educlassId);
			if(ccVersion == null) {
				return false;
			}
			Long versionId = ccVersion.getLong("id");
			versionMap.put(versionId, ccVersion);
			
			List<Long> educlassIdList = versionEduclassIdListMap.get(versionId);
			if(educlassIdList == null || educlassIdList.isEmpty()) {
				educlassIdList = new ArrayList<>();
				versionEduclassIdListMap.put(versionId, educlassIdList);
			}
			educlassIdList.add(educlassId);
		}
		// 遍历创建任务
		for(Entry<Long, List<Long>> entry : versionEduclassIdListMap.entrySet()) {
			Long versionId = entry.getKey();
			List<Long> educlassIdList = entry.getValue();
			CcVersion ccVersion = versionMap.get(versionId);
			Integer grade = ccVersion.getInt("grade");
			if(!statisticsAllByEduclassIdForJG(grade, versionId, educlassIdList.toArray(new Long[educlassIdList.size()]))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 重新计算当前版本
	 * @param educlassIds
	 * 			教学班列表（这些教学班必须属于同一个年级和同一个版本下，否则计算有误，会导致，只有第一个教学班所属的年级和版本下的课程等报表重新计算）
	 * @author SY 
	 * @version 创建时间：2017年12月4日 下午4:20:31 
	 */
	public Boolean statisticsAllForJGByEduClassId(Long eduClassIds[]) {
		if(eduClassIds == null || eduClassIds.length == 0) {
			return false;
		}
		CcVersion ccVersion = CcVersion.dao.findByEduClassId(eduClassIds[0]);
		if(ccVersion == null) {
			return false;
		}
		Long versionId = ccVersion.getLong("id");
		Integer grade = ccVersion.getInt("grade");
		return statisticsAllByEduclassIdForJG(grade, versionId, eduClassIds);
	}
	
	/**
	 * 少写一个参数
	 * @param grade
	 * @param versionId
	 * @param eduClassIds
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年12月5日 下午6:41:07 
	 */
	private Boolean statisticsAllByEduclassIdForJG(Integer grade, Long versionId, Long[] eduClassIds) {
		return statisticsAllByEduclassIdForJG(grade, versionId, eduClassIds, null);
	}

	/**
	 * 重新计算当前版本
	 * @param educlassId
	 * 			教学班
	 * @author SY 
	 * @version 创建时间：2017年12月4日 下午4:20:31 
	 */
	public Boolean statisticsAllForJGByEduClassId(Long eduClassId) {
		CcVersion ccVersion = CcVersion.dao.findByEduClassId(eduClassId);
		if(ccVersion == null) {
			return false;
		}
		Long versionId = ccVersion.getLong("id");
		Integer grade = ccVersion.getInt("grade");
		return statisticsAllByEduclassIdForJG(grade, versionId, eduClassId);
	}
	
	/**
	 * @param grade
	 * @param versionId
	 * @param eduClassId
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年12月5日 下午6:39:53 
	 */
	private Boolean statisticsAllByEduclassIdForJG(Integer grade, Long versionId, Long eduClassId) {
		Long []eduClassIds = new Long[1];
		eduClassIds[0] = eduClassId;
		return statisticsAllByEduclassIdForJG(grade, versionId, eduClassIds);
	}

	/**
	 * 重新计算当前版本
	 * @param grade
	 * @param versionId
	 * @param educlassId
	 * 			教学班
	 * @param num
	 * 			第几层递归，如果为空，就是第一层
	 * @author SY 
	 * @version 创建时间：2017年12月4日 下午4:20:31 
	 */
	private Boolean statisticsAllByEduclassIdForJG(final Integer grade, final Long versionId, final Long eduClassIds[], Integer num) {
		if(grade == null || versionId == null || eduClassIds == null) {
			return false;
		}
		final Long eduClassId = eduClassIds[0];
		if(num == null) {
			num = 0;
		}
		final Integer nextNum = num + 1;
		/********************************************* 教学班达成度数据 EM00550  START ******************************************/
		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findCourseByClassId(eduClassId);
		if (ccTeacherCourse == null) {
			return false;
		}
		
		final CcResultStatisticsService ccResultStatisticsService = SpringContextHolder.getBean(CcResultStatisticsService.class);
		final String missionKey = eduClassId.toString() + "异步统计教学班达成度数据";
		Map<String, Object> jobData = Maps.newHashMap();
		jobData.put("eduClassId", eduClassId);
		jobData.put("resultType", ccTeacherCourse.getInt("result_type"));
		jobData.put("missionKey", missionKey);

		// 选择分析法
		boolean isSuccess = Boolean.TRUE;
		try {
			
			// 上一个任务还未结束, 不开启新的运算任务
			if (QuartzKit.checkJobExists(missionKey)) {
				return false;
			}
			
			// 创建任务执行记录
			if (!ccResultStatisticsService.createReportBuildRecord(missionKey, CcReportBuildStatus.TYPE_EDUCLASS, ccTeacherCourse.getLong("version_id"))) {
				logger.error(new StringBuilder("创建").append(missionKey).append("任务记录时发生错误").toString());
				isSuccess = Boolean.FALSE;
			}
			
			QuartzKit.createTaskStartNow(missionKey, AsyncStatisticsEduclassResult.class, jobData, Boolean.FALSE, SimpleScheduleBuilder.simpleSchedule(), new ITaskMonitor() {

				@Override
				public void taskStart() {
					// 递归算法，如果不是底层。则继续递归，否则计算。
					if(eduClassIds.length > 1) {
						List<Long> newEduClassIdList = new ArrayList<>();
						for(Long educlassId : eduClassIds) {
							newEduClassIdList.add(educlassId);
						}
						// 去掉本次的教学班编号
						newEduClassIdList.remove(eduClassId);
						statisticsAllByEduclassIdForJG(grade, versionId, newEduClassIdList.toArray(new Long[newEduClassIdList.size()]), nextNum);
					} else {
						ccResultStatisticsService.updateReportBuildRecord(missionKey, CcReportBuildStatus.TYPE_EDUCLASS, CcReportBuildStatus.STATUS_TASK_START, null, null);
						logger.info(new StringBuilder("开始执行").append(missionKey).toString());
					}
				}
				
				@Override
				public void taskFinish() {
					logger.info(new StringBuilder(missionKey).append("执行结束，任务删除~~~~~~~~~~~~~~nextNum=" + nextNum).toString());
					// 如果是第一层，则执行唯一的一次课程和个人的统计
					//TODO gjm 我感觉这里是多余的，造成每次单个教学班计算达成度都要计算整个年级的 原来是nextNum==1 我改了！=1不进行下一步 去掉也是可以得
					if(nextNum != 1) {
						//这个是更新整个年级、版本的课程达成度相当于EM00554重新生成的效果
						statisticsCourseForJG(grade, versionId);
						//更新年级的个人达成度相当于EM00558接口
						statisticsPersonalForJG(grade, versionId);						
					}
				}
				
				@Override
				public void taskFail() {
					ccResultStatisticsService.updateReportBuildRecord(missionKey, CcReportBuildStatus.TYPE_EDUCLASS, CcReportBuildStatus.STATUS_TASK_FAIL, Boolean.TRUE, null);
					logger.info(new StringBuilder(missionKey).append("执行结束，任务删除").toString());
				}
				
			});
		
		} catch (SchedulerException e) {
			ccResultStatisticsService.updateReportBuildRecord(missionKey, CcReportBuildStatus.TYPE_EDUCLASS, CcReportBuildStatus.STATUS_TASK_CREATEFAIL, null, null);
			logger.error(new StringBuilder("创建").append(missionKey).append("任务时发生错误").toString(), e);
			isSuccess = Boolean.FALSE;
		}
		
		if (isSuccess) {
			ccResultStatisticsService.updateReportBuildRecord(missionKey, CcReportBuildStatus.TYPE_EDUCLASS, CcReportBuildStatus.STATUS_TASK_CREATESUCCESS, null, null);
		}
		
		return true;
	}

	/**
	 * 更新个人达成度 与EM00558效果相同
	 * @param grade
	 * @param versionId
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年12月5日 下午5:25:21 
	 */
	protected Boolean statisticsPersonalForJG(Integer grade, Long versionId) {
		
		/********************************************* 个人达成度数据 EM00558  START ******************************************/
		final CcResultStatisticsService ccResultStatisticsService = SpringContextHolder.getBean(CcResultStatisticsService.class);
		
		final String oersonalName = grade.toString() + versionId.toString() + "异步统计个人达成度数据";
		Map<String, Object> jobPersonalData = Maps.newHashMap();
		jobPersonalData.put("grade", grade);
		jobPersonalData.put("versionId", versionId);
		jobPersonalData.put("jobName", oersonalName);
		
		Boolean isSuccess = Boolean.TRUE;
		try {
			
			// 上一个任务还未结束, 不开启新的运算任务
			if (QuartzKit.checkJobExists(oersonalName)) {
				return false;
			}
			
			// 创建任务执行记录
			if (!ccResultStatisticsService.createReportBuildRecord(oersonalName, CcReportBuildStatus.TYPE_STUDENT, versionId)) {
				logger.error(new StringBuilder("创建").append(oersonalName).append("任务记录时发生错误").toString());
				isSuccess = Boolean.FALSE;
			}
			
			QuartzKit.createTaskStartNow(oersonalName, AsyncStatisticsPersonalCourseResult.class, jobPersonalData, Boolean.FALSE, SimpleScheduleBuilder.simpleSchedule(), new ITaskMonitor() {
				
				@Override
				public void taskStart() {
					ccResultStatisticsService.updateReportBuildRecord(oersonalName, CcReportBuildStatus.TYPE_STUDENT, CcReportBuildStatus.STATUS_TASK_START, null, null);
					logger.info(new StringBuilder("开始执行").append(oersonalName).toString());
				}
				
				@Override
				public void taskFinish() {
					logger.info(new StringBuilder(oersonalName).append("执行结束，任务删除").toString());
				}
				
				@Override
				public void taskFail() {
					ccResultStatisticsService.updateReportBuildRecord(oersonalName, CcReportBuildStatus.TYPE_STUDENT, CcReportBuildStatus.STATUS_TASK_FAIL, Boolean.TRUE, null);
					logger.info(new StringBuilder(oersonalName).append("执行结束，任务删除").toString());
				}
				
			});
		
		} catch (SchedulerException e) {
			ccResultStatisticsService.updateReportBuildRecord(oersonalName, CcReportBuildStatus.TYPE_STUDENT, CcReportBuildStatus.STATUS_TASK_CREATEFAIL, null, null);
			logger.error(new StringBuilder("创建").append(oersonalName).append("任务时发生错误").toString(), e);
			isSuccess = Boolean.FALSE;
		}
		
		if (isSuccess) {
			ccResultStatisticsService.updateReportBuildRecord(oersonalName, CcReportBuildStatus.TYPE_STUDENT, CcReportBuildStatus.STATUS_TASK_CREATESUCCESS, null, null);
		}
		return true;
	}

	/**
	 * 更新课程达成度 与EM00554效果相同
	 * @param grade
	 * @param versionId
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年12月5日 下午5:21:09 
	 */
	protected Boolean statisticsCourseForJG(Integer grade, Long versionId) {
		/********************************************* 课程达成度数据 EM00554  START 与EM00554效果相同******************************************/
		final CcResultStatisticsService ccResultStatisticsService = SpringContextHolder.getBean(CcResultStatisticsService.class);
		Map<String, Object> jobData = Maps.newHashMap();
		jobData = Maps.newHashMap();
		jobData.put("grade", grade);
		jobData.put("versionId", versionId);
		final String name = CcReportBuildStatusService.getReportBuildKeyForCourse(grade, versionId);
		
		Boolean isSuccess = Boolean.TRUE;
		try {
			
			// 上一个任务还未结束, 不开启新的运算任务
			if (QuartzKit.checkJobExists(name)) {
				return false;
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
		
		return true;
	}
	
    
}
