package com.gnet.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.*;
import com.jfinal.kit.StrKit;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.plugin.id.IdGenerate;
import com.gnet.response.ServiceResponse;
import com.gnet.utils.CollectionKit;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

/**
 * @author SY
 * @date 2016年6月22日19:07:22
 */
@Component("ccVersionService")
public class CcVersionService {

//	private static final Logger logger = Logger.getLogger(CcVersionService.class);
	private final Logger logger = LoggerFactory.getLogger(Model.class);
	/**
	 * 找到最新的某个版本（包括发布态、编辑态和废弃态）
	 * @param majorId
	 * @param majorVersion
	 * 			大版本（可以为空，如果不为空，则查找这个大版本下面的最新小版本）
	 * @return
	 */
	public CcVersion findLastVersion(Long majorId, Integer majorVersion){
		return findLastVersion(majorId, majorVersion, Boolean.FALSE, Boolean.TRUE);
	}

	/**
	 * 返回删除是否成功，以及对应的错误信息
	 * @param version
	 * @return
	 */
	public ServiceResponse isDeletable(CcVersion version){
		CcVersion lastCcVersion = findFilteredLastVersion(version.getLong("major_id"), null);
		return isDeletable(version, lastCcVersion.getInt("major_version"));
	}


	/**
	 * 返回删除是否成功，以及对应的错误信息
	 * @param version
	 * @param CurrentMaxMajorVersion
	 * @return
	 */
	public ServiceResponse isDeletable(CcVersion version, Integer CurrentMaxMajorVersion){
		//  1.已经发布的不能删除 2.已经废弃的不能删除
		//	3.如果小版本编号不等于最新未删除小版本号不允许删除 4.如果小版本编号为0，并且大版本编号不是最大的不允许删除
		Integer state = version.getInt("state");
		if(state.equals(CcVersion.STATUE_PUBLISH)){
			return ServiceResponse.error("已经发布的版本不能删除");
		}else if(state.equals(CcVersion.STATUE_CLOSE)){
			return ServiceResponse.error("已经废弃的版本不能删除");
		}
		if(!version.getInt("minor_version").equals(version.getInt("max_minor_version"))){
			return ServiceResponse.error("只能删除大版本的最新小版本");
		}
		if(version.getInt("minor_version").equals(CcVersion.INITIAL_MINOR) && version.getInt("major_version") < CurrentMaxMajorVersion){
			return ServiceResponse.error("不能删除不是最新的大版本");
		}
		return ServiceResponse.succ(true);
	}

	/**
	 * 找到最新的某个未废弃版本（包括发布态、编辑态,但是不包括废弃态）
	 * @param majorId
	 * @param majorVersion
	 * 			大版本（可以为空，如果不为空，则查找这个大版本下面的最新小版本）
	 * @return
	 */
	public CcVersion findFilteredLastVersion(Long majorId, Integer majorVersion){
		return findLastVersion(majorId, majorVersion, Boolean.FALSE, Boolean.FALSE);
	}

	/**
	 * 找到最新的某个版本（当isPublish为true的时候只查询最近一次发布的版本，若为false则查询最新一次创建的版本）
	 * @param majorId
	 * 			专业编号
	 * @param majorVersion
	 * 			大版本（可以为空，如果不为空，则查找这个大版本下面的最新小版本）
	 * @param isPublish
	 * 			版本状态（可以为空，当null则不加限制，当isPublish为true的时候只查询最近一次发布的版本，若为false则查询最新一次创建的版本）
	 * @param isAll
	 * 			是否显示所有（是：包括删除的，否：排除删除的）
	 * @return
	 */
	public CcVersion findLastVersion(Long majorId, Integer majorVersion, Boolean isPublish, Boolean isAll) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sb = new StringBuilder("select cv.*, major.name majorName from " + CcVersion.dao.tableName + " cv ");
		sb.append("left join " + Office.dao.tableName + " major on major.id = cv.major_id ");
		sb.append("where cv.major_id = ? ");
		params.add(majorId);
		if(majorVersion != null) {
			sb.append("and cv.major_version = ? ");
			params.add(majorVersion);
		}
		if(isPublish != null) {
			if(isPublish == Boolean.TRUE) {
				sb.append("and cv.state = ? ");
				params.add(CcVersion.STATUE_PUBLISH);
			}
		}
		if(!isAll) {
			sb.append("and cv.is_del = ? ");
			params.add(Boolean.FALSE);
		}
		sb.append("and major.is_del = ? ");
		params.add(Boolean.FALSE);
		sb.append("order by cv.major_version desc, cv.minor_version desc ");
		return CcVersion.dao.findFirst(sb.toString(), params.toArray());
	}

	/**
	 * 找到最新的某些未废弃版本（包括发布态、编辑态,但是不包括废弃态）
	 * @param majorIds
	 * @param majorVersion
	 * 			大版本（可以为空，如果不为空，则查找这个大版本下面的最新小版本）
	 * @return
	 */
	public List<CcVersion> findFilteredLastVersion(Long[] majorIds, Integer majorVersion){
		return findLastVersion(majorIds, majorVersion, Boolean.FALSE, Boolean.FALSE);
	}

	/**
	 * 找到最新的某些版本（当isPublish为true的时候只查询最近一次发布的版本，若为false则查询最新一次创建的版本）
	 * @param majorIds
	 * 			专业编号s
	 * @param majorVersion
	 * 			大版本（可以为空，如果不为空，则查找这个大版本下面的最新小版本）
	 * @param isPublish
	 * 			版本状态（可以为空，当null则不加限制，当isPublish为true的时候只查询最近一次发布的版本，若为false则查询最新一次创建的版本）
	 * @param isAll
	 * 			是否显示所有（是：包括删除的，否：排除删除的）
	 * @return
	 */
	public List<CcVersion> findLastVersion(Long[] majorIds, Integer majorVersion, Boolean isPublish, Boolean isAll) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sb = new StringBuilder("select cv.*, major.name majorName from " + CcVersion.dao.tableName + " cv ");
		sb.append("left join " + Office.dao.tableName + " major on major.id = cv.major_id ");
		sb.append("where cv.major_id in ( " + CollectionKit.convert(majorIds, ",") + ")");
		if(majorVersion != null) {
			sb.append("and cv.major_version = ? ");
			params.add(majorVersion);
		}
		if(isPublish != null) {
			if(isPublish == Boolean.TRUE) {
				sb.append("and cv.state = ? ");
				params.add(CcVersion.STATUE_PUBLISH);
			}
		}
		if(!isAll) {
			sb.append("and cv.is_del = ? ");
			params.add(Boolean.FALSE);
		}
		sb.append("and major.is_del = ? ");
		params.add(Boolean.FALSE);
		sb.append("order by cv.major_version desc, cv.minor_version desc ");
		List<CcVersion> list = CcVersion.dao.find(sb.toString(), params.toArray());
		// 因为查出来的不仅仅是这个版本最新的，可能还有上一个版本的，所以每个majroId只取第一个。因为有排序，保证第一个是最新的
		List<CcVersion> returnList = new ArrayList<>();
		// majroId， ccversion
		Map<Long, CcVersion> map = new HashMap<>();
		for(CcVersion temp : list) {
			Long thisMajorId = temp.getLong("major_id");
			if(map.get(thisMajorId) == null) {
				map.put(thisMajorId, temp);
				returnList.add(temp);
			}
		}
		return returnList;
	}

	/**
	 * 修改版本状态
	 * @param versionId
	 * @param newState
	 * 			新的状态
	 * @param oldState
	 * 			老的状态
	 * @return
	 */
	public boolean changeState(Long versionId, Integer newState, Integer oldState) {
		Date date = new Date();
		return Db.update("update " + CcVersion.dao.tableName + " set state = ?, publish_date = ?, modify_date = ? where id = ? and state = ?", newState, date, date, versionId, oldState) > 0;
	}

	/**
	 * 创建新的版本
	 * @return
	 */
	@Transactional
	public Boolean create(CcVersion ccVersion, Record allMessage) {
		CcVersionCreateLogService ccVersionCreateLogService = SpringContextHolder.getBean(CcVersionCreateLogService.class);
		Boolean saveResult;
		//设置任务状态为创建中
		saveResult = ccVersionCreateLogService.changeStepJob(allMessage.getLong("newVersionId"), "创建中！", CcVersionCreateLog.STEP_CREATING);
		if(!saveResult){
			return false;
		}

		// 保存这个信息
		saveResult = ccVersion.save();
		if(!saveResult) {
			return false;
		}

		Long parentId = allMessage.getLong("parentId");
		Integer type = ccVersion.getInt("type");
		Integer enableGrade = ccVersion.getInt("enable_grade");
		Integer majorVersion = ccVersion.getInt("major_version");
		Integer beforeMajorVersion = majorVersion;
		Long majorId = allMessage.get("majorId");
		// 如果是增加大版本，找到上一个未删除所在的大版本所有版本对应的apply_grade，  第一次录入就不计算了
		if(CcVersion.TYPE_MAJOR_VERSION.equals(type)) {
			CcVersion beforeVersion = findBeforeVersion(majorId, majorVersion);
			if(beforeVersion != null){
				beforeMajorVersion = beforeVersion.getInt("major_version");
				Integer beforeEnableGrade = beforeVersion.getInt("enable_grade");
				String newParentApplyGrade = "";
				// 获取父亲【启用年级】和这次【启用年级】两个数据创建的【适用年级】
				for (Integer grade = beforeEnableGrade; grade < enableGrade; grade++) {
					newParentApplyGrade = newParentApplyGrade + grade + ",";
				}
				newParentApplyGrade = newParentApplyGrade.substring(0, newParentApplyGrade.length() - 1);

				Map<String, Object> params = new HashMap<>();
				params.put("major_version", beforeMajorVersion);
				params.put("major_id", allMessage.get("majorId"));
				List<CcVersion> ccVersionList = CcVersion.dao.findFilteredByColumn(params);
				for(CcVersion temp : ccVersionList) {
					temp.set("apply_grade", newParentApplyGrade);
					temp.set("modify_date", ccVersion.getDate("create_date"));
				}
				if(!CcVersion.dao.batchUpdate(ccVersionList, "modify_date,apply_grade")) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
			}
		}else if(CcVersion.TYPE_MINOR_VERSION.equals(type)){
			//更新同一大版本下的最新的小版本编号
			if(!CcVersion.dao.updateMaxMinorVersion(ccVersion.getInt("minor_version"), ccVersion.getInt("major_version"), ccVersion.getLong("major_id"))) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
		}

		String planName = allMessage.getStr("planName");
		String planCourseVersionName = allMessage.getStr("planCourseVersionName");
		String graduateName = allMessage.getStr("graduateName");
		String graduateIndicationVersionName = allMessage.getStr("graduateIndicationVersionName");
		BigDecimal pass = allMessage.getBigDecimal("pass");

		saveResult = saveLink(ccVersion, planName, planCourseVersionName, graduateName, graduateIndicationVersionName, pass);
		if(!saveResult) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			// 返回操作是否成功
			return false;
		}

		// 拷贝数据
		if(parentId != null) {
			saveResult = copy(allMessage, ccVersion.getLong("id"), parentId, allMessage.getBoolean("isCopyAchievement"));
			if(!saveResult){
				return false;
			}
		}
		saveResult = ccVersionCreateLogService.finishJob(allMessage.getLong("newVersionId"));
		return saveResult;
	}

	/**
	 * 创建新的版本
	 * @return
	 */
	@Transactional
	public Boolean adminCreate(CcVersion ccVersion, Record allMessage) {
		CcVersionCreateLogService ccVersionCreateLogService = SpringContextHolder.getBean(CcVersionCreateLogService.class);
		Boolean saveResult;
		//设置任务状态为创建中
		saveResult = ccVersionCreateLogService.changeStepJob(allMessage.getLong("newVersionId"), "创建中！", CcVersionCreateLog.STEP_CREATING);
		if(!saveResult){
			return false;
		}

		// 保存这个信息
		saveResult = ccVersion.save();
		if(!saveResult) {
			return false;
		}

		Long parentId = allMessage.getLong("parentId");
//		Integer type = ccVersion.getInt("type");
//		Integer enableGrade = ccVersion.getInt("enable_grade");
//		Integer majorVersion = ccVersion.getInt("major_version");
//		Integer beforeMajorVersion = majorVersion;
//		Long majorId = allMessage.get("majorId");

		String planName = allMessage.getStr("planName");
		String planCourseVersionName = allMessage.getStr("planCourseVersionName");
		String graduateName = allMessage.getStr("graduateName");
		String graduateIndicationVersionName = allMessage.getStr("graduateIndicationVersionName");
		BigDecimal pass = allMessage.getBigDecimal("pass");

		saveResult = saveLink(ccVersion, planName, planCourseVersionName, graduateName, graduateIndicationVersionName, pass);
		if(!saveResult) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			// 返回操作是否成功
			return false;
		}

		// 拷贝数据
		if(parentId != null) {
			saveResult = copy(allMessage, ccVersion.getLong("id"), parentId, allMessage.getBoolean("isCopyAchievement"));
			if(!saveResult){
				return false;
			}
		}
		saveResult = ccVersionCreateLogService.finishJob(allMessage.getLong("newVersionId"));
		return saveResult;
	}


	/**
	 * 找到这个版本的上一个可用大版本的最新小版本
	 * 注意：如果majorVersion需要大于1，否则直接返回null
	 * @param majorId
	 * 			版本编号
	 * @param majorVersion
	 * 			大版本号
	 * @return
	 * @author SY
	 * @version 创建时间：2016年11月3日 下午7:58:49 
	 */
	public CcVersion findBeforeVersion(Long majorId, Integer majorVersion) {
		if(majorVersion == null || majorVersion <= 1) {
			return null;
		}
		return CcVersion.dao.findBefore(majorId, majorVersion);
	}

	/**
	 * 复制版本对应的各种信息
	 * @param allMessage
	 * 			各种公用的信息
	 * @param newId
	 * 			新的id
	 * @param parentId
	 * 			老的id
	 * @param isCopyAchievement
	 * 			是否拷贝学生成绩
	 * @return
	 */
	public Boolean copy(Record allMessage, Long newId, Long parentId, Boolean isCopyAchievement) {
		/*
		 * 架构--否
		 * 内容--要
		 * 成绩--随机
		 * 
		 * A. 基础表复制
		 * 		// 专业认证持续改进方案版本
		 * 		A1.  毕业要求
		 * 		A2.  指标点cc_indicator_point
		 * 		A3.  专业认证教师
		 * 		A4.  课程组
		 * 		A5.  培养计划学期表
		 * 		A6.  所属模块
		 * 		A7.  专业方向
		 * 		A8.  课程性质
		 * 		A9.  课程层次
		 * 		A10. 课程
		 *      A11. 课程目标表 cc_indcation
		 * 		A12. 指标点与课程关系
		 * 		A13. 培养计划课程学期详情表
		 * 	    A14. 课程目标与指标点与课程关系的关系表 cc_course_target_indication
		 * 		A15. 课程教学大纲文本
		 * 		A16. 课程教学大纲操作记录
		 * 		A17. 培养计划课程分区表cc_plan_course_zone
		 * 		A18. 培养计划课程分区各培养计划学期详情表cc_plan_course_zone_term
		 *
		 * B. 成绩表复制
		 * 		// 达成度计算（考核成绩分析法）
		 * 		1.  教师开课课程
		 * 		2.  教学班-cc_educlass
		 * 		3.  教学班学生-cc_educlass_student
		 * 		4.  开课课程成绩组成元素表-cc_course_gradecompose
		 * 		5.  成绩组成元素明细-cc_course_gradecompose_detail
		 * 		6.  成绩组成元素明细学生关联表-cc_course_gradecompose_studetail
		 * 		7.  成绩组成元素明细课程目标关联表-cc_course_gradecompose_detail_indication
		 * 		8.  开课课程成绩组成元素课程目标关联表-cc_course_gradecompose_indication
		 * 		9. 考核成绩分析法学生课程目标成绩-cc_score_stu_indigrade
		 *
		 * 		// 达成度计算（评分表分析法）
		 * 		10. 考评点
		 * 		11. 考评点得分层次
		 * 		12. 学生考评点成绩
		 * 		13. 专业认证学生表
		 *      14. 开课课程成绩组成元素课程目标关联的分数范围备注 cc_gradecompose_indication_score_remark
		 *
		 *
		 */
		// 基础表复制
		Boolean result = copyVersionBasics(allMessage, newId, parentId);
		if(!result) {
			return result;
		}

		// 如果不拷贝成绩，直接返回
		if(!isCopyAchievement) {
			return result;
		}

		// 拷贝成绩
		result = copyVersionAchievement(allMessage, newId, parentId);
		return result;

	}

	/**
	 * 复制版本号的时候--基础数据全都复制
	 * @param allMessage
	 * 			各种公用的信息
	 * @param newId
	 * 			新的id
	 * @param parentId
	 * 			老的id
	 */
	private boolean copyVersionBasics(Record allMessage, Long newId, Long parentId) {
		/*
		 * A. 基础表复制
		 * 		// 专业认证持续改进方案版本
		 * 		A1.  毕业要求
		 * 		A2.  指标点cc_indicator_point
		 * 		A3.  专业认证教师
		 * 		A4.  课程组
		 * 		A5.  培养计划学期表
		 * 		A6.  所属模块
		 * 		A7.  专业方向
		 * 		A8.  课程性质
		 * 		A9.  课程层次
		 * 		A10. 课程
		 *      A11. 课程目标表 cc_indcation
		 * 		A12. 指标点与课程关系
		 * 		A13. 培养计划课程学期详情表
		 * 	    A14. 课程目标与指标点与课程关系的关系表 cc_course_target_indication
		 * 		A15. 课程教学大纲文本
		 * 		A16. 课程教学大纲操作记录
		 * 		A17. 培养计划课程分区表cc_plan_course_zone
		 * 		A18. 培养计划课程分区各培养计划学期详情表cc_plan_course_zone_term
		 */
		Long newVersionId = allMessage.getLong("newVersionId");
		CcVersionCreateLogService ccVersionCreateLogService = SpringContextHolder.getBean(CcVersionCreateLogService.class);

		Long versionCreateLogId = newVersionId;
		allMessage.set("versionCreateLogId", versionCreateLogId);


		// A1.  毕业要求
		Boolean result = Boolean.FALSE;
		String message = "拷贝毕业要求";
		String messageNext = "";
		Integer type = CcVersionCreateLog.STEP_GRADUATE;
		try {
			result = copyGraduate(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			messageNext = "拷贝指标点";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}


		// A2.  指标点
		message = messageNext;
		type = CcVersionCreateLog.STEP_INDICATOR_POINT;
		try {
			result = copyIndication(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新
			messageNext = "拷贝专业认证教师";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A3.  专业认证教师
		message = messageNext;
		type = CcVersionCreateLog.STEP_MAJOR_TEACHER;
		try {
			result = copyMajorTeacher(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新
			messageNext = "拷贝课程组";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A4. 课程组
		message = messageNext;
		type = CcVersionCreateLog.STEP_COURSE_GROUP;
		try {
			result = copyCourseGroup(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新
			messageNext = "拷贝培养计划学期表";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A5. 培养计划学期表
		message = messageNext;
		type = CcVersionCreateLog.STEP_PLAN_TERM;
		try {
			result = copyPlanTerm(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新
			messageNext = "拷贝所属模块";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A6. 所属模块
		message = messageNext;
		type = CcVersionCreateLog.STEP_COURSE_MODULEE;
		try {
			result = copyCourseModule(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新
			messageNext = "拷贝专业方向";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A7. 专业方向
		message = messageNext;
		type = CcVersionCreateLog.STEP_MAJOR_DIRECTION;
		try {
			result = copyMajorDirection(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新
			messageNext = "拷贝课程性质";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A8. 课程性质
		message = messageNext;
		type = CcVersionCreateLog.STEP_COURSE_PROPERTY;
		try {
			result = copyCourseProperty(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新
			messageNext = "拷贝层次";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A9. 课程层次
		message = messageNext;
		type = CcVersionCreateLog.STEP_COURSE_HIERACHY;
		try {
			result = copyCourseHierarchy(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新
			messageNext = "拷贝课程";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A10. 课程
		message = messageNext;
		type = CcVersionCreateLog.STEP_COURSE;
		try {
			result = copyCourse(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新
			messageNext = "拷贝指标点与课程关系";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		//A11. 拷贝指标点与课程关系
		message = messageNext;
		type = CcVersionCreateLog.STEP_INDICATION;
		try {
			result = copyCourseTarget(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新
			messageNext = "拷贝课程目标";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}


		// A12. 指标点与课程关系
		message = messageNext;
		type = CcVersionCreateLog.STEP_INDICATION_COURSE;
		try {
			result = copyIndicationCourse(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新
			messageNext = "拷贝培养计划课程学期详情表";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A13. 培养计划课程学期详情表
		message = messageNext;
		type = CcVersionCreateLog.STEP_PLAN_TER_COURSE;
		try {
			result = copyPlanTermCourse(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新
			messageNext = "拷贝课程教学大纲";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		//A14. 课程目标与指标点与课程关系的关系表 cc_course_target_indication
		message = messageNext;
		type = CcVersionCreateLog.STEP_COURSE_TARGET_INDICATION;
		try {
			result = copyCourseTargetIndication(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			messageNext = "拷贝开课课程成绩组成元素课程目标关联的分数范围备注";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A15. 课程教学大纲
		message = messageNext;
		type = CcVersionCreateLog.STEP_COURSE_OUTLINE;
		try {
			result = copyCourseOutline(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新
			messageNext = "建立课程教学大纲操作记录";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A16. 课程教学大纲操作记录
		message = messageNext;
		type = CcVersionCreateLog.STEP_COURSE_OUTLINE_HISTORY;
		try {
			result = buildCourseOutlineHistory(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新
			messageNext = "拷贝培养计划课程分区表";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A17. 培养计划课程分区表cc_plan_course_zone
		message = messageNext;
		type = CcVersionCreateLog.STEP_PLAN_COURSE_ZONE;
		try {
			result = copyPlanCourseZone(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新
			messageNext = "拷贝培养计划课程分区各培养计划学期详情表";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A18. 培养计划课程分区各培养计划学期详情表cc_plan_course_zone_term
		message = messageNext;
		type = CcVersionCreateLog.STEP_PLAN_COURSE_ZONE_TERM;
		try {
			result = copyPlanCourseZoneTerm(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新
			// 日志更新
			messageNext = "拷贝课程分组信息";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
			//ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		//A19.课程分组信息两张表cc_course_group_mange cc_course_group_mange_group
		message = messageNext;
		type = CcVersionCreateLog.STEP_COURSE_GROUPS;
		try {
			result = copyCourseGroupCourse(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新
			messageNext = "拷贝课程分级教学信息";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
			//ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		//A20.课程分级教学信息两张表cc_course_group_teach cc_course_group_teach_mange
		message = messageNext;
		type = CcVersionCreateLog.STEP_COURSE_TEACH_GROUPS;
		try {
			result = copyCourseTeachGroup(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}


		return true;
	}
	/**
	 * 拷贝课程分组
	 * @param allMessage
	 * @return
	 */
	private Boolean copyCourseGroupCourse(Record allMessage){
		Date date = allMessage.getDate("date");
		Long newVersionId = allMessage.getLong("newVersionId");
		Long oldVersionId = allMessage.getLong("oldVersionId");
		Map<Long, Long> oldAndNewCourseIds = allMessage.get("oldAndNewCourseIds");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);
		//上个版本的课程分组信息
		List<CcCourseGroupMange> courseGroupManges = CcCourseGroupMange.dao.findFilteredByColumnIn("plan_id", oldVersionId);
		Map<Long, Long> oldAndNewGroupCourseIds = new HashMap<>();
		List<Long> groupId=new ArrayList<>();
		for (CcCourseGroupMange temp: courseGroupManges){
			Long id = idGenetate.getNextValue();
			groupId.add(temp.get("id"));
			oldAndNewGroupCourseIds.put(temp.get("id"),id);
			temp.set("id", id);
			temp.set("modify_date", date);
			temp.set("create_date", date);
			temp.set("plan_id",newVersionId);
		}
		//课程分组与课程的关联表
		if (groupId.size()!=0 && !groupId.isEmpty()){
			List<CcCourseGroupMangeGroup> groupMangeCourse = CcCourseGroupMangeGroup.dao.findGroupMangeCourse(groupId);
			for (CcCourseGroupMangeGroup temps: groupMangeCourse){
				temps.set("mange_group_id",oldAndNewGroupCourseIds.get(temps.getLong("mange_group_id")));
				temps.set("course_id",oldAndNewCourseIds.get(temps.getLong("course_id")));
			}
			if(!CcCourseGroupMangeGroup.dao.batchSave(groupMangeCourse)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
		}
		//保存
		if(!courseGroupManges.isEmpty() &&!CcCourseGroupMange.dao.batchSave(courseGroupManges)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		allMessage.set("oldAndNewGroupCourseIds", oldAndNewGroupCourseIds);

		return  true;
	}
	/**
	 * 拷贝课程分级教学信息
	 * @param allMessage
	 * @return
	 */
	private Boolean copyCourseTeachGroup(Record allMessage){
		Date date = allMessage.getDate("date");
		Long newVersionId = allMessage.getLong("newVersionId");
		Long oldVersionId = allMessage.getLong("oldVersionId");
		Map<Long, Long> oldAndNewGroupCourseIds = allMessage.get("oldAndNewGroupCourseIds");
		List<CcCourseGroupMangeTeach> teachGroup = CcCourseGroupMangeTeach.dao.findFilteredByColumnIn("plan_id", oldVersionId);
		List<Long> groupId=new ArrayList<>();
		Map<Long, Long> oldAndNewTeachGroup = new HashMap<>();
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);
		for (CcCourseGroupMangeTeach temp : teachGroup){
			groupId.add(temp.get("id"));
			Long id = idGenetate.getNextValue();
			oldAndNewTeachGroup.put(temp.get("id"),id);
			temp.set("id",id);
			temp.set("plan_id",newVersionId);
			temp.set("modify_date", date);
			temp.set("create_date", date);
		}
		if (groupId.size()!=0 && !groupId.isEmpty()){
			List<CcCourseGroupTeachMange> groupMangeCourse = CcCourseGroupTeachMange.dao.finTeachGroups(groupId);
			for (CcCourseGroupTeachMange temps: groupMangeCourse){
				temps.set("teach_group_id",oldAndNewTeachGroup.get(temps.getLong("teach_group_id")));
				temps.set("group_id",oldAndNewGroupCourseIds.get(temps.getLong("group_id")));
			}
			if(!CcCourseGroupTeachMange.dao.batchSave(groupMangeCourse)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
		}

		if(!teachGroup.isEmpty()&&!CcCourseGroupMangeTeach.dao.batchSave(teachGroup)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		return true;
	}
	/**
	 * 拷贝培养计划课程分区各培养计划学期详情表
	 * @param allMessage
	 * @return
	 */
	private Boolean copyPlanCourseZoneTerm(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long[] oldPlanCourseZoneIds = allMessage.get("oldPlanCourseZoneIds");
		Map<Long, Long> oldAndNewPlanCourseZoneIds = allMessage.get("oldAndNewPlanCourseZoneIds");
		Map<Long, Long> oldAndNewPlanTermIds = allMessage.get("oldAndNewPlanTermIds");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);

		if(oldPlanCourseZoneIds != null){
			List<CcPlanCourseZoneTerm> ccPlanCourseZoneTerms = CcPlanCourseZoneTerm.dao.findFilteredByColumnIn("plan_course_zone_id", oldPlanCourseZoneIds);
			if(!ccPlanCourseZoneTerms.isEmpty()){
				Long[] oldPlanCourseZoneTermIds = new Long[ccPlanCourseZoneTerms.size()];
				// Map<oldId, newId>
				Map<Long, Long> oldAndNewPlanCourseZoneTermIds = new HashMap<>();
				List<CcPlanCourseZoneTerm> newPlanCourseZoneTerms = Lists.newArrayList();
				for(int i = 0; i < ccPlanCourseZoneTerms.size(); i++) {
					CcPlanCourseZoneTerm temp = ccPlanCourseZoneTerms.get(i);
					if(oldAndNewPlanTermIds.get(temp.getLong("plan_term_id")) != null && oldAndNewPlanCourseZoneIds.get(temp.getLong("plan_course_zone_id")) != null){
						oldPlanCourseZoneTermIds[i] = temp.getLong("id");
						Long id = idGenetate.getNextValue();
						oldAndNewPlanCourseZoneTermIds.put(temp.getLong("id"), id);
						temp.set("id", id);
						temp.set("modify_date", date);
						temp.set("create_date", date);
						temp.set("plan_term_id", oldAndNewPlanTermIds.get(temp.getLong("plan_term_id")));
						temp.set("plan_course_zone_id", oldAndNewPlanCourseZoneIds.get(temp.getLong("plan_course_zone_id")));
						newPlanCourseZoneTerms.add(temp);
					}
				}


				if(!CcPlanCourseZoneTerm.dao.batchSave(newPlanCourseZoneTerms)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldPlanCourseZoneTermIds", oldPlanCourseZoneTermIds);
				allMessage.set("oldAndNewPlanCourseZoneTermIds", oldAndNewPlanCourseZoneTermIds);
			}
		}
		return true;
	}

	/**
	 * 拷贝培养计划课程分区表
	 * @param allMessage
	 * @return
	 */
	private Boolean copyPlanCourseZone(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long newVersionId = allMessage.getLong("newVersionId");
		Long oldVersionId = allMessage.getLong("oldVersionId");
		Map<Long, Long> oldAndNewCourseModuleIds = allMessage.get("oldAndNewCourseModuleIds");
		Map<Long, Long> oldAndNewMajorDirectionIds = allMessage.get("oldAndNewMajorDirectionIds");
		Map<Long, Long> oldAndNewCoursePropertyIds = allMessage.get("oldAndNewCoursePropertyIds");
		Map<Long, Long> oldAndNewCoursePropertySecondaryIds = allMessage.get("oldAndNewCoursePropertySecondaryIds");
		Map<Long, Long> oldAndNewCourseHierarchyIds = allMessage.get("oldAndNewCourseHierarchyIds");
		Map<Long, Long> oldAndNewCourseHierarchySecondaryIds = allMessage.get("oldAndNewCourseHierarchySecondaryIds");
		Map<Long, Long> oldAndNewAllIds = new HashMap<>();
		if(oldAndNewCourseModuleIds != null){
			oldAndNewAllIds.putAll(oldAndNewCourseModuleIds);
		}
		if(oldAndNewMajorDirectionIds != null){
			oldAndNewAllIds.putAll(oldAndNewMajorDirectionIds);
		}
		if(oldAndNewCoursePropertyIds != null){
			oldAndNewAllIds.putAll(oldAndNewCoursePropertyIds);
		}
		if(oldAndNewCourseHierarchyIds != null){
			oldAndNewAllIds.putAll(oldAndNewCourseHierarchyIds);
		}
		if(oldAndNewCoursePropertySecondaryIds != null){
			oldAndNewAllIds.putAll(oldAndNewCoursePropertySecondaryIds);
		}
		if(oldAndNewCourseHierarchySecondaryIds != null){
			oldAndNewAllIds.putAll(oldAndNewCourseHierarchySecondaryIds);
		}
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);

		if(oldVersionId != null){
			List<CcPlanCourseZone> ccPlanCourseZones = CcPlanCourseZone.dao.findFilteredByColumnIn("plan_id", oldVersionId);
			if(!ccPlanCourseZones.isEmpty()){
				Long[] oldPlanCourseZoneIds = new Long[ccPlanCourseZones.size()];
				// Map<oldId, newId>
				Map<Long, Long> oldAndNewPlanCourseZoneIds = new HashMap<>();
				for(int i = 0; i < ccPlanCourseZones.size(); i++) {
					CcPlanCourseZone temp = ccPlanCourseZones.get(i);
					oldAndNewPlanCourseZoneIds.put(temp.getLong("id"), idGenetate.getNextValue());
				}
				for(int i = 0; i < ccPlanCourseZones.size(); i++) {
					CcPlanCourseZone temp = ccPlanCourseZones.get(i);
					oldPlanCourseZoneIds[i] = temp.getLong("id");
					// 找到真正的的id
					Integer zoneType = temp.getInt("zone_type");
					Long newZoneId = null;
					Long oldZoneId = temp.getLong("zone_id");
					if(CcPlanCourseZone.TYPE_HIERARCHY.equals(zoneType)) {
						newZoneId = oldAndNewCourseHierarchyIds.get(oldZoneId);
					} else if(CcPlanCourseZone.TYPE_PROPERTY.equals(zoneType)) {
						newZoneId = oldAndNewCoursePropertyIds.get(oldZoneId);
					} else if(CcPlanCourseZone.TYPE_HIERARCHY_SECONDARY.equals(zoneType)) {
						newZoneId = oldAndNewCourseHierarchySecondaryIds.get(oldZoneId);
					} else if(CcPlanCourseZone.TYPE_PROPERTY_SECONDARY.equals(zoneType)) {
						newZoneId = oldAndNewCoursePropertySecondaryIds.get(oldZoneId);
					} else if(CcPlanCourseZone.TYPE_DIRECTION.equals(zoneType)) {
						newZoneId = oldAndNewMajorDirectionIds.get(oldZoneId);
					} else if(CcPlanCourseZone.TYPE_MODULE.equals(zoneType)) {
						newZoneId = oldAndNewCourseModuleIds.get(oldZoneId);
					} else {
						return false;
					}
					// 拼接路径
					String oldZonePath = temp.getStr("zone_path");
					String zonePathsStr[] = oldZonePath.substring(1, oldZonePath.length() - 1).split(",,");
					String newZonePath = "";
					Long tempNewZonePath = null;
					for(String tempId : zonePathsStr) {
						tempNewZonePath = oldAndNewAllIds.get(Long.valueOf(tempId));
						// 防止oldAndNewAllIds中的数据不全，导致保存的zone_path是null #bug：5033
						if(tempNewZonePath == null) {
							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
							return false;
						}
						newZonePath = newZonePath + "," + tempNewZonePath + ",";
					}
					temp.set("id", oldAndNewPlanCourseZoneIds.get(oldPlanCourseZoneIds[i]));
					temp.set("modify_date", date);
					temp.set("create_date", date);
					temp.set("plan_id", newVersionId);
					temp.set("zone_id", newZoneId);
					if(newZoneId == null) {
						Long a1 = oldPlanCourseZoneIds[i];
						Long a2 = oldAndNewPlanCourseZoneIds.get(oldPlanCourseZoneIds[i]);
						int a3 = i;
						System.out.println("oldPlanCourseZoneIds[i]="+a1);
						System.out.println("oldAndNewPlanCourseZoneIds.get(oldPlanCourseZoneIds[i])="+a2);
						System.out.println("i="+a3);
					}
					//无上层时parent_id为0
					temp.set("parent_id", temp.getLong("parent_id") == 0 ? 0 : oldAndNewPlanCourseZoneIds.get(temp.getLong("parent_id")));

					temp.set("zone_path", newZonePath);
				}
				if(!CcPlanCourseZone.dao.batchSave(ccPlanCourseZones)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldPlanCourseZoneIds", oldPlanCourseZoneIds);
				allMessage.set("oldAndNewPlanCourseZoneIds", oldAndNewPlanCourseZoneIds);
			}
		}
		return true;
	}

	/**
	 * 建立课程教学大纲操作记录:新建记录以及copy记录
	 * @param allMessage
	 * @return
	 */
	private Boolean buildCourseOutlineHistory(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long userId = allMessage.getLong("userId");
		Long[] oldCourseIds = allMessage.get("oldCourseIds");
		Map<Long, Long> oldAndNewCourseOutlineIds = allMessage.get("oldAndNewCourseOutlineIds");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);

		if(oldCourseIds !=null && oldCourseIds.length > 0){
			List<CcCourseOutline> ccCourseOutlines = CcCourseOutline.dao.findFilteredByColumnIn("course_id", oldCourseIds);
			if(!ccCourseOutlines.isEmpty()){
				// 用于copy保存的的记录列表
				List<CcCourseOutlineHistory> ccCourseOutlineHistoryCopys = new ArrayList<>();
				// 用于新建保存的记录列表
				List<CcCourseOutlineHistory> ccCourseOutlineHistoryNews = new ArrayList<>();
				for(CcCourseOutline temp : ccCourseOutlines) {
					CcCourseOutlineHistory ccCourseOutlineHistoryCopy = new CcCourseOutlineHistory();
					ccCourseOutlineHistoryCopy.set("id", idGenetate.getNextValue());
					ccCourseOutlineHistoryCopy.set("create_date", date);
					ccCourseOutlineHistoryCopy.set("modify_date", date);
					ccCourseOutlineHistoryCopy.set("outline_id", temp.getLong("id"));
					ccCourseOutlineHistoryCopy.set("trigger_id", userId);
					ccCourseOutlineHistoryCopy.set("event", "拷贝了课程教学大纲！");
					ccCourseOutlineHistoryCopy.set("event_type", CcCourseOutlineHistory.TYPE_VERSION_COPY);
					ccCourseOutlineHistoryCopy.set("is_del", Boolean.FALSE);
					ccCourseOutlineHistoryCopys.add(ccCourseOutlineHistoryCopy);

					CcCourseOutlineHistory ccCourseOutlineHistoryNew = new CcCourseOutlineHistory();
					ccCourseOutlineHistoryNew.set("id", idGenetate.getNextValue());
					ccCourseOutlineHistoryNew.set("create_date", date);
					ccCourseOutlineHistoryNew.set("modify_date", date);
					ccCourseOutlineHistoryNew.set("outline_id",  oldAndNewCourseOutlineIds.get(temp.getLong("id")));
					ccCourseOutlineHistoryNew.set("trigger_id", userId);
					ccCourseOutlineHistoryNew.set("event", "新建了课程教学大纲！");
					ccCourseOutlineHistoryNew.set("event_type", CcCourseOutlineHistory.TYPE_NEW_BUILD);
					ccCourseOutlineHistoryNew.set("is_del", Boolean.FALSE);
					ccCourseOutlineHistoryNews.add(ccCourseOutlineHistoryNew);
				}

				Boolean result = CcCourseOutlineHistory.dao.batchSave(ccCourseOutlineHistoryCopys);
				if(!result) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}

				result = CcCourseOutlineHistory.dao.batchSave(ccCourseOutlineHistoryNews);
				if(!result) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 拷贝课程教学大纲
	 * @param allMessage
	 * @return
	 */
	private Boolean copyCourseOutline(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long[] oldCourseIds = allMessage.get("oldCourseIds");
		Map<Long, Long> oldAndNewCourseIds = allMessage.get("oldAndNewCourseIds");
		Map<Long, Long> oldAndNewCourseTargetIds = allMessage.get("oldAndNewCourseTargetIds");
		Map<Long, Long> oldAndNewCourseTargetIndicationIds = allMessage.get("oldAndNewCourseTargetIndicationIds");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);

		if(oldCourseIds != null && oldCourseIds.length > 0){
			List<CcCourseOutline> ccCourseOutlines = CcCourseOutline.dao.findFilteredByColumnIn("course_id", oldCourseIds);
			if(!ccCourseOutlines.isEmpty()){
				Long[] oldCourseOutlineIds = new Long[ccCourseOutlines.size()];
				Map<Long, Long> oldAndNewCourseOutlineIds = new HashMap<>();
				for(int i = 0; i < ccCourseOutlines.size(); i++) {
					CcCourseOutline temp = ccCourseOutlines.get(i);
					oldCourseOutlineIds[i] = temp.getLong("id");
					Long id = idGenetate.getNextValue();
					oldAndNewCourseOutlineIds.put(temp.getLong("id"), id);

					// 如果原本就是未分配状态，则不变，否则全都变成已分配未确认状态
					Integer status = CcCourseOutline.STATUS_NOT_DISTRIBUTION.equals(temp.getInt("status")) ? CcCourseOutline.STATUS_NOT_DISTRIBUTION :  CcCourseOutline.STATUS_ASSIGNED_NOT_CONFIRMED;
					temp.set("id", id);
					temp.set("modify_date", date);
					temp.set("create_date", date);
					temp.set("status", status);
					temp.set("audit_comment", null);
					//TODO 2020/09/27 数据库里可能有脏数据，这里可能为空
					if (oldAndNewCourseIds.get(temp.getLong("course_id")) != null){
						temp.set("course_id", oldAndNewCourseIds.get(temp.getLong("course_id")));
					}
				}
				if(!CcCourseOutline.dao.batchSave(ccCourseOutlines)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldCourseOutlineIds", oldCourseOutlineIds);
				allMessage.set("oldAndNewCourseOutlineIds", oldAndNewCourseOutlineIds);

				//课程大纲课基本信息拷贝
				List<CcCourseOutlineCourseInfo> courseOutlineCourseInfos = CcCourseOutlineCourseInfo.dao.findFilteredByColumnIn(CcCourseOutline.COURSE_OUTLINE_ID, oldCourseOutlineIds);
				if(!courseOutlineCourseInfos.isEmpty()){
					for(CcCourseOutlineCourseInfo ccCourseOutlineCourseInfo : courseOutlineCourseInfos){
						ccCourseOutlineCourseInfo.set("id", idGenetate.getNextValue());
						ccCourseOutlineCourseInfo.set("create_date", date);
						ccCourseOutlineCourseInfo.set("modify_date", date);
						ccCourseOutlineCourseInfo.set(CcCourseOutline.COURSE_OUTLINE_ID, oldAndNewCourseOutlineIds.get(ccCourseOutlineCourseInfo.getLong(CcCourseOutline.COURSE_OUTLINE_ID)));
					}

					if(!CcCourseOutlineCourseInfo.dao.batchSave(courseOutlineCourseInfos)){
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return false;
					}
				}

				//课程教学大纲模块支持课程目标
				List<CcCourseOutlineIndications> ccCourseOutlineIndicationsList = CcCourseOutlineIndications.dao.findFilteredByColumnIn(CcCourseOutline.COURSE_OUTLINE_ID, oldCourseOutlineIds);
				if(!ccCourseOutlineIndicationsList.isEmpty()){
					for(CcCourseOutlineIndications ccCourseOutlineIndications : ccCourseOutlineIndicationsList){
						ccCourseOutlineIndications.set("id", idGenetate.getNextValue());
						ccCourseOutlineIndications.set("create_date", date);
						ccCourseOutlineIndications.set("modify_date", date);
						ccCourseOutlineIndications.set(CcCourseOutline.COURSE_OUTLINE_ID, oldAndNewCourseOutlineIds.get(ccCourseOutlineIndications.getLong(CcCourseOutline.COURSE_OUTLINE_ID)));
						ccCourseOutlineIndications.set("course_target_indication_id", oldAndNewCourseTargetIndicationIds.get(ccCourseOutlineIndications.getLong("course_target_indication_id")));
					}

					if(!CcCourseOutlineIndications.dao.batchSave(ccCourseOutlineIndicationsList)){
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return false;
					}
				}

				//课程教学大纲模块基本信息
				List<CcCourseOutlineModule> ccCourseOutlineModules = CcCourseOutlineModule.dao.findFilteredByColumnIn(CcCourseOutline.COURSE_OUTLINE_ID, oldCourseOutlineIds);
				if(!ccCourseOutlineModules.isEmpty()){
					for(CcCourseOutlineModule ccCourseModule : ccCourseOutlineModules){
						ccCourseModule.set("id", idGenetate.getNextValue());
						ccCourseModule.set("create_date", date);
						ccCourseModule.set("modify_date", date);
						ccCourseModule.set(CcCourseOutline.COURSE_OUTLINE_ID, oldAndNewCourseOutlineIds.get(ccCourseModule.getLong(CcCourseOutline.COURSE_OUTLINE_ID)));
					}

					if(!CcCourseOutlineModule.dao.batchSave(ccCourseOutlineModules)){
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return false;
					}
				}

				//课程教学大纲次要内容
				List<CcCourseOutlineSecondaryContent> ccCourseOutlineSecondaryContents = CcCourseOutlineSecondaryContent.dao.findFilteredByColumnIn(CcCourseOutline.COURSE_OUTLINE_ID, oldCourseOutlineIds);
				if(!ccCourseOutlineSecondaryContents.isEmpty()){
					for(CcCourseOutlineSecondaryContent ccCourseOutlineSecondaryContent : ccCourseOutlineSecondaryContents){
						ccCourseOutlineSecondaryContent.set("id", idGenetate.getNextValue());
						ccCourseOutlineSecondaryContent.set("create_date", date);
						ccCourseOutlineSecondaryContent.set("modify_date", date);
						ccCourseOutlineSecondaryContent.set(CcCourseOutline.COURSE_OUTLINE_ID, oldAndNewCourseOutlineIds.get(ccCourseOutlineSecondaryContent.getLong(CcCourseOutline.COURSE_OUTLINE_ID)));
						ccCourseOutlineSecondaryContent.set("indications", returnNewIndications(ccCourseOutlineSecondaryContent.getStr("indications"), oldAndNewCourseTargetIds));
					}

					if(!CcCourseOutlineSecondaryContent.dao.batchSave(ccCourseOutlineSecondaryContents)){
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return false;
					}
				}

				//课程教学大纲模块教学内容
				List<CcCourseOutlineTeachingContent> ccCourseOutlineTeachingContents = CcCourseOutlineTeachingContent.dao.findFilteredByColumnIn(CcCourseOutline.COURSE_OUTLINE_ID, oldCourseOutlineIds);
				if(!ccCourseOutlineTeachingContents.isEmpty()){
					for(CcCourseOutlineTeachingContent ccCourseOutlineTeachingContent : ccCourseOutlineTeachingContents){
						ccCourseOutlineTeachingContent.set("id", idGenetate.getNextValue());
						ccCourseOutlineTeachingContent.set("create_date", date);
						ccCourseOutlineTeachingContent.set("modify_date", date);
						ccCourseOutlineTeachingContent.set(CcCourseOutline.COURSE_OUTLINE_ID, oldAndNewCourseOutlineIds.get(ccCourseOutlineTeachingContent.getLong(CcCourseOutline.COURSE_OUTLINE_ID)));
						ccCourseOutlineTeachingContent.set("indications", returnNewIndications(ccCourseOutlineTeachingContent.getStr("indications"), oldAndNewCourseTargetIds));
					}

					if(!CcCourseOutlineTeachingContent.dao.batchSave(ccCourseOutlineTeachingContents)){
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return false;
					}
				}

				//课程教学大纲表名
				List<CcCourseOutlineTableName> ccCourseOutlineTableNames = CcCourseOutlineTableName.dao.findFilteredByColumnIn(CcCourseOutline.COURSE_OUTLINE_ID, oldCourseOutlineIds);
				if(!ccCourseOutlineTableNames.isEmpty()){
					for(CcCourseOutlineTableName ccCourseOutlineTableName : ccCourseOutlineTableNames){
						ccCourseOutlineTableName.set("id", idGenetate.getNextValue());
						ccCourseOutlineTableName.set("create_date", date);
						ccCourseOutlineTableName.set("modify_date", date);
						ccCourseOutlineTableName.set(CcCourseOutline.COURSE_OUTLINE_ID, oldAndNewCourseOutlineIds.get(ccCourseOutlineTableName.getLong(CcCourseOutline.COURSE_OUTLINE_ID)));
					}

					if(!CcCourseOutlineTableName.dao.batchSave(ccCourseOutlineTableNames)){
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return false;
					}
				}

				//课程教学大纲表头信息
				List<CcCourseOutlineHeader> ccCourseOutlineHeaders = CcCourseOutlineHeader.dao.findFilteredByColumnIn(CcCourseOutline.COURSE_OUTLINE_ID, oldCourseOutlineIds);
				if(!ccCourseOutlineHeaders.isEmpty()){
					for(CcCourseOutlineHeader ccCourseOutlineHeader : ccCourseOutlineHeaders){
						ccCourseOutlineHeader.set("id", idGenetate.getNextValue());
						ccCourseOutlineHeader.set("create_date", date);
						ccCourseOutlineHeader.set("modify_date", date);
						ccCourseOutlineHeader.set(CcCourseOutline.COURSE_OUTLINE_ID, oldAndNewCourseOutlineIds.get(ccCourseOutlineHeader.getLong(CcCourseOutline.COURSE_OUTLINE_ID)));
					}

					if(!CcCourseOutlineHeader.dao.batchSave(ccCourseOutlineHeaders)){
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return false;
					}
				}
			}
		}
		return true;
	}


	/**
	 * 创建版本拷贝数据返回新的课程目标
	 * @param indicationStr
	 * @param oldAndNewCourseTargetIds
	 * @return
	 */
	private String returnNewIndications(String indicationStr, Map<Long, Long> oldAndNewCourseTargetIds){
		if(StrKit.isBlank(indicationStr)){
			return null;
		}

		String[] indicationIds = indicationStr.split(",");
		Long[] newIndications = new Long[indicationIds.length];
		for(int i=0; i<indicationIds.length; i++){
			newIndications[i] = oldAndNewCourseTargetIds.get(Long.valueOf(indicationIds[i]));
		}

		return StringUtils.join(newIndications, ",");
	}


	/**
	 * 拷贝课程层次
	 * @param allMessage
	 * @return
	 */
	private Boolean copyCourseHierarchy(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long oldVersionId = allMessage.get("oldVersionId");
		Long newVersionId = allMessage.get("newVersionId");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);

		if(oldVersionId != null){
			List<CcCourseHierarchy> ccCourseHierarchys = CcCourseHierarchy.dao.findFilteredByColumn("plan_id", oldVersionId);
			if(!ccCourseHierarchys.isEmpty()){
				Long[] oldCourseHierarchyIds = new Long[ccCourseHierarchys.size()];
				Map<Long, Long> oldAndNewCourseHierarchyIds = new HashMap<>();
				for(int i = 0; i < ccCourseHierarchys.size(); i++) {
					CcCourseHierarchy temp = ccCourseHierarchys.get(i);
					oldCourseHierarchyIds[i] = temp.getLong("id");
					Long id = idGenetate.getNextValue();
					oldAndNewCourseHierarchyIds.put(temp.getLong("id"), id);

					temp.set("id", id);
					temp.set("modify_date", date);
					temp.set("create_date", date);
					temp.set("plan_id", newVersionId);
				}
				if(!CcCourseHierarchy.dao.batchSave(ccCourseHierarchys)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldCourseHierarchyIds", oldCourseHierarchyIds);
				allMessage.set("oldAndNewCourseHierarchyIds", oldAndNewCourseHierarchyIds);
			}
			/*
			 *  新增次要课程性质 2019-12-9 15:34:10 Edit By Sy
			 */
			List<CcCourseHierarchySecondary> ccCourseHierarchySecondarys = CcCourseHierarchySecondary.dao.findFilteredByColumn("plan_id", oldVersionId);
			if(!ccCourseHierarchySecondarys.isEmpty()){
				Long[] oldCourseHierarchySecondaryIds = new Long[ccCourseHierarchySecondarys.size()];
				Map<Long, Long> oldAndNewCourseHierarchySecondaryIds = new HashMap<>();
				for(int i = 0; i < ccCourseHierarchySecondarys.size(); i++) {
					CcCourseHierarchySecondary temp = ccCourseHierarchySecondarys.get(i);
					oldCourseHierarchySecondaryIds[i] = temp.getLong("id");
					Long id = idGenetate.getNextValue();
					oldAndNewCourseHierarchySecondaryIds.put(temp.getLong("id"), id);
					
					temp.set("id", id);
					temp.set("modify_date", date);
					temp.set("create_date", date);
					temp.set("plan_id", newVersionId);
				}
				if(!CcCourseHierarchySecondary.dao.batchSave(ccCourseHierarchySecondarys)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldCourseHierarchySecondaryIds", oldCourseHierarchySecondaryIds);
				allMessage.set("oldAndNewCourseHierarchySecondaryIds", oldAndNewCourseHierarchySecondaryIds);
			}
		}
		return true;
	}

	/**
	 * 拷贝课程性质
	 * @param allMessage
	 * @return
	 */
	private Boolean copyCourseProperty(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long oldVersionId = allMessage.get("oldVersionId");
		Long newVersionId = allMessage.get("newVersionId");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);

		if(oldVersionId != null){
			List<CcCourseProperty> ccCoursePropertys = CcCourseProperty.dao.findFilteredByColumn("plan_id", oldVersionId);
			if(!ccCoursePropertys.isEmpty()){
				Long[] oldCoursePropertyIds = new Long[ccCoursePropertys.size()];
				Map<Long, Long> oldAndNewCoursePropertyIds = new HashMap<>();
				for(int i = 0; i < ccCoursePropertys.size(); i++) {
					CcCourseProperty temp = ccCoursePropertys.get(i);
					oldCoursePropertyIds[i] = temp.getLong("id");
					Long id = idGenetate.getNextValue();
					oldAndNewCoursePropertyIds.put(temp.getLong("id"), id);

					temp.set("id", id);
					temp.set("modify_date", date);
					temp.set("create_date", date);
					temp.set("plan_id", newVersionId);
				}
				if(!CcCourseProperty.dao.batchSave(ccCoursePropertys)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldCoursePropertyIds", oldCoursePropertyIds);
				allMessage.set("oldAndNewCoursePropertyIds", oldAndNewCoursePropertyIds);
			}
			
			/*
			 *  因为新次要增课程性质，所以也要拷贝2019年12月9日15:32:56 Edit by SY
			 *  但是由于次要的属性都来自课程性质，所以直接按照课程性质保存
			 */
			List<CcCoursePropertySecondary> ccCoursePropertySecondarys = CcCoursePropertySecondary.dao.findFilteredByColumn("plan_id", oldVersionId);
			if(!ccCoursePropertySecondarys.isEmpty()){
				Long[] oldCoursePropertySecondaryIds = new Long[ccCoursePropertySecondarys.size()];
				Map<Long, Long> oldAndNewCoursePropertySecondaryIds = new HashMap<>();
				for(int i = 0; i < ccCoursePropertySecondarys.size(); i++) {
					CcCoursePropertySecondary temp = ccCoursePropertySecondarys.get(i);
					oldCoursePropertySecondaryIds[i] = temp.getLong("id");
					Long id = idGenetate.getNextValue();
					oldAndNewCoursePropertySecondaryIds.put(temp.getLong("id"), id);

					temp.set("id", id);
					temp.set("modify_date", date);
					temp.set("create_date", date);
					temp.set("plan_id", newVersionId);
				}
				if(!CcCoursePropertySecondary.dao.batchSave(ccCoursePropertySecondarys)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldCoursePropertySecondaryIds", oldCoursePropertySecondaryIds);
				allMessage.set("oldAndNewCoursePropertySecondaryIds", oldAndNewCoursePropertySecondaryIds);
			}
		}
		return true;
	}

	/**
	 * 拷贝专业方向
	 * @param allMessage
	 * @return
	 */
	private Boolean copyMajorDirection(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long oldVersionId = allMessage.get("oldVersionId");
		Long newVersionId = allMessage.get("newVersionId");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);

		if(oldVersionId != null){
			List<CcMajorDirection> ccMajorDirections = CcMajorDirection.dao.findFilteredByColumn("plan_id", oldVersionId);
			if(!ccMajorDirections.isEmpty()){
				Long[] oldMajorDirectionIds = new Long[ccMajorDirections.size()];
				Map<Long, Long> oldAndNewMajorDirectionIds = new HashMap<>();
				for(int i = 0; i < ccMajorDirections.size(); i++) {
					CcMajorDirection temp = ccMajorDirections.get(i);
					oldMajorDirectionIds[i] = temp.getLong("id");
					Long id = idGenetate.getNextValue();
					oldAndNewMajorDirectionIds.put(temp.getLong("id"), id);

					temp.set("id", id);
					temp.set("modify_date", date);
					temp.set("create_date", date);
					temp.set("plan_id", newVersionId);
				}
				if(!CcMajorDirection.dao.batchSave(ccMajorDirections)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldMajorDirectionIds", oldMajorDirectionIds);
				allMessage.set("oldAndNewMajorDirectionIds", oldAndNewMajorDirectionIds);
			}

		}
		return true;
	}

	/**
	 * 拷贝所属模块
	 * @param allMessage
	 * @return
	 */
	private Boolean copyCourseModule(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long oldVersionId = allMessage.get("oldVersionId");
		Long newVersionId = allMessage.get("newVersionId");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);

		if(oldVersionId != null){
			List<CcCourseModule> ccCourseModules = CcCourseModule.dao.findFilteredByColumn("plan_id", oldVersionId);
			if(!ccCourseModules.isEmpty()){
				Long[] oldCourseModuleIds = new Long[ccCourseModules.size()];
				Map<Long, Long> oldAndNewCourseModuleIds = new HashMap<>();
				for(int i = 0; i < ccCourseModules.size(); i++) {
					CcCourseModule temp = ccCourseModules.get(i);
					oldCourseModuleIds[i] = temp.getLong("id");
					Long id = idGenetate.getNextValue();
					oldAndNewCourseModuleIds.put(temp.getLong("id"), id);

					temp.set("id", id);
					temp.set("modify_date", date);
					temp.set("create_date", date);
					temp.set("plan_id", newVersionId);
				}
				if(!CcCourseModule.dao.batchSave(ccCourseModules)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldCourseModuleIds", oldCourseModuleIds);
				allMessage.set("oldAndNewCourseModuleIds", oldAndNewCourseModuleIds);
			}
		}
		return true;
	}

	/**
	 * 拷贝培养计划学期表
	 * @param allMessage
	 * @return
	 */
	private Boolean copyPlanTerm(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long oldVersionId = allMessage.get("oldVersionId");
		Long newVersionId = allMessage.get("newVersionId");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);

		List<CcPlanTerm> ccPlanTerms = CcPlanTerm.dao.findFilteredByColumn("plan_id", oldVersionId);
		Long[] oldPlanTermIds = new Long[ccPlanTerms.size()];
		Map<Long, Long> oldAndNewPlanTermIds = new HashMap<>();
		for(int i = 0; i < ccPlanTerms.size(); i++) {
			CcPlanTerm temp = ccPlanTerms.get(i);
			oldPlanTermIds[i] = temp.getLong("id");
			Long id = idGenetate.getNextValue();
			oldAndNewPlanTermIds.put(temp.getLong("id"), id);

			temp.set("id", id);
			temp.set("modify_date", date);
			temp.set("create_date", date);
			temp.set("plan_id", newVersionId);
		}
		if(!CcPlanTerm.dao.batchSave(ccPlanTerms)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		allMessage.set("oldPlanTermIds", oldPlanTermIds);
		allMessage.set("oldAndNewPlanTermIds", oldAndNewPlanTermIds);
		return true;
	}

	/**
	 * 拷贝培养计划课程学期详情表
	 * @param allMessage
	 * @return
	 */
	private Boolean copyPlanTermCourse(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long[] oldPlanTermIds = allMessage.get("oldPlanTermIds");
		Map<Long, Long> oldAndNewPlanTermIds = allMessage.get("oldAndNewPlanTermIds");
		Map<Long, Long> oldAndNewCourseIds = allMessage.get("oldAndNewCourseIds");

		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);

		if(oldPlanTermIds != null && oldPlanTermIds.length > 0){
			List<CcPlanTermCourse> ccPlanTermCourses = CcPlanTermCourse.dao.findFilteredByColumnIn("plan_term_id", oldPlanTermIds);
			if(!ccPlanTermCourses.isEmpty()){
				Long[] oldPlanTermCourseIds = new Long[ccPlanTermCourses.size()];
				Map<Long, Long> oldAndNewPlanTermCourseIds = new HashMap<>();
				for(int i = 0; i < ccPlanTermCourses.size(); i++) {
					CcPlanTermCourse temp = ccPlanTermCourses.get(i);
					oldPlanTermCourseIds[i] = temp.getLong("id");
					Long id = idGenetate.getNextValue();
					oldAndNewPlanTermCourseIds.put(temp.getLong("id"), id);
					if( oldAndNewPlanTermIds.get(temp.getLong("plan_term_id")) == null ||oldAndNewPlanTermIds.get(temp.getLong("plan_term_id"))==null ){
						logger.info(i+"错误！");
					}
					temp.set("id", id);
					temp.set("modify_date", date);
					temp.set("create_date", date);
					temp.set("plan_term_id", oldAndNewPlanTermIds.get(temp.getLong("plan_term_id")));
					//TODO 2020/09/27 数据库里可能有脏数据，这里可能为空
					if (oldAndNewCourseIds.get(temp.getLong("course_id")) != null){
						temp.set("course_id", oldAndNewCourseIds.get(temp.getLong("course_id")));
					}

				}
				if(!CcPlanTermCourse.dao.batchSave(ccPlanTermCourses)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldPlanTermCourseIds", oldPlanTermCourseIds);
				allMessage.set("oldAndNewPlanTermCourseIds", oldAndNewPlanTermCourseIds);
			}
		}
		return true;
	}

	/**
	 * 拷贝课程组
	 * @param allMessage
	 * @return
	 */
	private Boolean copyCourseGroup(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long oldVersionId = allMessage.get("oldVersionId");
		Long newVersionId = allMessage.get("newVersionId");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);

		if(oldVersionId != null){
			List<CcCourseGroup> ccCourseGroups = CcCourseGroup.dao.findFilteredByColumn("plan_id", oldVersionId);
			if(!ccCourseGroups.isEmpty()){
				Long[] oldCourseGroupIds = new Long[ccCourseGroups.size()];
				Map<Long, Long> oldAndNewCourseGroupIds = new HashMap<>();
				for(int i = 0; i < ccCourseGroups.size(); i++) {
					CcCourseGroup temp = ccCourseGroups.get(i);
					oldCourseGroupIds[i] = temp.getLong("id");
					Long id = idGenetate.getNextValue();
					oldAndNewCourseGroupIds.put(temp.getLong("id"), id);

					temp.set("id", id);
					temp.set("modify_date", date);
					temp.set("create_date", date);
					temp.set("plan_id", newVersionId);
				}
				if(!CcCourseGroup.dao.batchSave(ccCourseGroups)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldCourseGroupIds", oldCourseGroupIds);
				allMessage.set("oldAndNewCourseGroupIds", oldAndNewCourseGroupIds);
			}
		}
		return true;
	}

	/**
	 * 拷贝指标点与课程关系
	 * @param allMessage
	 * @return
	 */
	private Boolean copyIndicationCourse(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long[] oldIndicationIds = allMessage.get("oldIndicationIds");
		Map<Long, Long> oldAndNewIndicationIds = allMessage.get("oldAndNewIndicationIds");
		Map<Long, Long> oldAndNewCourseIds = allMessage.get("oldAndNewCourseIds");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);

		if(oldIndicationIds != null && oldIndicationIds.length > 0){
			List<CcIndicationCourse> ccIndicationCourses = CcIndicationCourse.dao.findFilteredByColumnIn("indication_id", oldIndicationIds);
			if(!ccIndicationCourses.isEmpty()){
				Long[] oldIndicationCourseIds = new Long[ccIndicationCourses.size()];
				Map<Long, Long> oldAndNewIndicationCourseIds = new HashMap<>();
				for(int i = 0; i < ccIndicationCourses.size(); i++) {
					CcIndicationCourse temp = ccIndicationCourses.get(i);
					oldIndicationCourseIds[i] = temp.getLong("id");

					if (oldAndNewIndicationIds.get(temp.getLong("indication_id"))==null){
						logger.error("老的指标点关联新的指标点错误为空");

					}


					if(oldAndNewCourseIds.get(temp.getLong("course_id")) ==null){
						logger.error("老的课程关联新的课程错误为空");

					}

					Long id = idGenetate.getNextValue();
					oldAndNewIndicationCourseIds.put(temp.getLong("id"), id);

					temp.set("id", id);
					temp.set("modify_date", date);
					temp.set("create_date", date);

					temp.set("indication_id", oldAndNewIndicationIds.get(temp.getLong("indication_id")));
					//TODO 2020/09/27 数据库里可能有脏数据，这里可能为空
					if (oldAndNewCourseIds.get(temp.getLong("course_id")) != null){
						temp.set("course_id", oldAndNewCourseIds.get(temp.getLong("course_id")));
					}
				}
				if(!CcIndicationCourse.dao.batchSave(ccIndicationCourses)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldIndicationCourseIds", oldIndicationCourseIds);
				allMessage.set("oldAndNewIndicationCourseIds", oldAndNewIndicationCourseIds);
			}
		}
		return true;
	}

	/**
	 * 拷贝课程
	 * @param allMessage
	 * @return
	 */
	private Boolean copyCourse(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long newVersionId = allMessage.get("newVersionId");
		Long oldVersionId = allMessage.get("oldVersionId");
		Map<Long, Long> oldAndNewCourseGroupIds = allMessage.get("oldAndNewCourseGroupIds");
		Map<Long, Long> oldAndNewCourseModuleIds = allMessage.get("oldAndNewCourseModuleIds");
		Map<Long, Long> oldAndNewMajorDirectionIds = allMessage.get("oldAndNewMajorDirectionIds");
		Map<Long, Long> oldAndNewCoursePropertyIds = allMessage.get("oldAndNewCoursePropertyIds");
		Map<Long, Long> oldAndNewCoursePropertySecondaryIds = allMessage.get("oldAndNewCoursePropertySecondaryIds");
		Map<Long, Long> oldAndNewCourseHierarchyIds = allMessage.get("oldAndNewCourseHierarchyIds");
		Map<Long, Long> oldAndNewCourseHierarchySecondaryIds = allMessage.get("oldAndNewCourseHierarchySecondaryIds");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);

		if(oldVersionId != null){

			List<CcCourse> ccCourses = CcCourse.dao.findFilteredByColumn("plan_id", oldVersionId);
			if(!ccCourses.isEmpty()){
				Long[] oldCourseIds = new Long[ccCourses.size()];
				Map<Long, Long> oldAndNewCourseIds = new HashMap<>();
				for(int i = 0; i < ccCourses.size(); i++) {
					CcCourse temp = ccCourses.get(i);
					oldCourseIds[i] = temp.getLong("id");
					Long id = idGenetate.getNextValue();
					oldAndNewCourseIds.put(oldCourseIds[i], id);

					temp.set("id", id);
					temp.set("modify_date", date);
					temp.set("create_date", date);
					temp.set("plan_id", newVersionId);
					if(temp.getLong("course_group_id") != null){
						temp.set("course_group_id", oldAndNewCourseGroupIds.get(temp.getLong("course_group_id")));
					}
					if(temp.getLong("module_id") != null){
						temp.set("module_id", oldAndNewCourseModuleIds.get(temp.getLong("module_id")));
					}
					if(temp.getLong("direction_id") != null){
						temp.set("direction_id", oldAndNewMajorDirectionIds.get(temp.getLong("direction_id")));
					}
					if(temp.getLong("property_id") != null){
						temp.set("property_id", oldAndNewCoursePropertyIds.get(temp.getLong("property_id")));
					}
					if(temp.getLong("property_secondary_id") != null){
						temp.set("property_secondary_id", oldAndNewCoursePropertySecondaryIds.get(temp.getLong("property_secondary_id")));
					}
					if(temp.getLong("hierarchy_id") != null){
						temp.set("hierarchy_id", oldAndNewCourseHierarchyIds.get(temp.getLong("hierarchy_id")));
					}
					if(temp.getLong("hierarchy_secondary_id") != null){
						temp.set("hierarchy_secondary_id", oldAndNewCourseHierarchySecondaryIds.get(temp.getLong("hierarchy_secondary_id")));
					}
				}
				if(!CcCourse.dao.batchSave(ccCourses)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldCourseIds", oldCourseIds);
				allMessage.set("oldAndNewCourseIds", oldAndNewCourseIds);
			}
		}
		return true;
	}

	/**
	 * 拷贝课程目标
	 * @param allMessage
	 * @return
	 */
	private Boolean copyCourseTarget(Record allMessage){
		Date date = allMessage.getDate("date");
		Long[] oldCourseIds = allMessage.get("oldCourseIds");
		Map<Long, Long> oldAndNewCourseIds = allMessage.get("oldAndNewCourseIds");
		if(oldCourseIds != null && oldCourseIds.length > 0){
			List<CcIndication> ccIndications = CcIndication.dao.findFilteredByColumnIn("course_id", oldCourseIds);
			if(!ccIndications.isEmpty()){
				IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);
				Long[] oldCourseTargetIds = new Long[ccIndications.size()];
				Map<Long, Long> oldAndNewCourseTargetIds = new HashMap<>();
				for(int i=0; i<ccIndications.size(); i++){
					CcIndication ccIndication = ccIndications.get(i);
					Long oldId = ccIndication.getLong("id");
					Long newId = idGenetate.getNextValue();
					oldAndNewCourseTargetIds.put(oldId, newId);
					oldCourseTargetIds[i] = newId;
					ccIndication.set("id", newId);
					ccIndication.set("create_date", date);
					ccIndication.set("modify_date", date);
					//TODO 2020/09/27 数据库里可能有脏数据，这里可能为空
					if (oldAndNewCourseIds.get(ccIndication.getLong("course_id")) != null){
						ccIndication.set("course_id", oldAndNewCourseIds.get(ccIndication.getLong("course_id")));
					}
				}
				if(!CcIndication.dao.batchSave(ccIndications)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldCourseTargetIds", oldCourseTargetIds);
				allMessage.set("oldAndNewCourseTargetIds", oldAndNewCourseTargetIds);
			}
		}
		return true;
	}

	/**
	 * 拷贝专业认证教师
	 * @param allMessage
	 * @return
	 */
	private Boolean copyMajorTeacher(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long newVersionId = allMessage.get("newVersionId");
		Long oldVersionId = allMessage.get("oldVersionId");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);

		if(oldVersionId != null){
			List<CcMajorTeacher> ccMajorTeachers = CcMajorTeacher.dao.findFilteredByColumn("version_id", oldVersionId);
			if(!ccMajorTeachers.isEmpty()){
				Long[] oldMajorTeacherIds = new Long[ccMajorTeachers.size()];
				Map<Long, Long> oldAndNewMajorTeacherIds = new HashMap<>();
				for(int i = 0; i < ccMajorTeachers.size(); i++) {
					CcMajorTeacher temp = ccMajorTeachers.get(i);
					oldMajorTeacherIds[i] = temp.getLong("id");
					Long id = idGenetate.getNextValue();
					oldAndNewMajorTeacherIds.put(temp.getLong("id"), id);

					temp.set("id", id);
					temp.set("modify_date", date);
					temp.set("create_date", date);
					temp.set("version_id", newVersionId);
				}
				if(!CcMajorTeacher.dao.batchSave(ccMajorTeachers)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldMajorTeacherIds", oldMajorTeacherIds);
				allMessage.set("oldAndNewMajorTeacherIds", oldAndNewMajorTeacherIds);
			}
		}
		return true;
	}

	/**
	 * 拷贝指标点
	 * @param allMessage
	 * @return
	 */
	private Boolean copyIndication(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long[] oldGraduateIds = allMessage.get("oldGraduateIds");
		Map<Long, Long> oldAndNewGraduateIds = allMessage.get("oldAndNewGraduateIds");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);

		if(oldGraduateIds != null && oldGraduateIds.length > 0 ){
			List<CcIndicatorPoint> ccIndications = CcIndicatorPoint.dao.findFilteredByColumnIn("graduate_id", oldGraduateIds);
			if(!ccIndications.isEmpty()){
				Long[] oldIndicationIds = new Long[ccIndications.size()];
				Map<Long, Long> oldAndNewIndicationIds = new HashMap<>();
				for(int i = 0; i < ccIndications.size(); i++) {
					CcIndicatorPoint temp = ccIndications.get(i);
					oldIndicationIds[i] = temp.getLong("id");
					Long id = idGenetate.getNextValue();
					oldAndNewIndicationIds.put(temp.getLong("id"), id);

					temp.set("id", id);
					temp.set("modify_date", date);
					temp.set("create_date", date);
					temp.set("graduate_id", oldAndNewGraduateIds.get(temp.getLong("graduate_id")));
				}
				if(!CcIndicatorPoint.dao.batchSave(ccIndications)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldIndicationIds", oldIndicationIds);
				allMessage.set("oldAndNewIndicationIds", oldAndNewIndicationIds);
			}
		}
		return true;
	}

	/**
	 * 拷贝毕业要求点
	 * @param allMessage
	 * @return
	 */
	private boolean copyGraduate(Record allMessage) {
		Date date = allMessage.getDate("date");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);
		Long newVersionId = allMessage.get("newVersionId");
		Long oldVersionId = allMessage.get("oldVersionId");

		if(oldVersionId != null){
			List<CcGraduate> ccGraduates = CcGraduate.dao.findFilteredByColumn("graduate_ver_id", oldVersionId);
			if(!ccGraduates.isEmpty()){
				Long[] oldGraduateIds = new Long[ccGraduates.size()];
				// oldId,newId
				Map<Long, Long> oldAndNewGraduateIds = new HashMap<>();
				for(int i = 0; i < ccGraduates.size(); i++) {
					CcGraduate temp = ccGraduates.get(i);
					oldGraduateIds[i] = temp.getLong("id");
					Long id = idGenetate.getNextValue();
					oldAndNewGraduateIds.put(temp.getLong("id"), id);

					temp.set("id", id);
					temp.set("modify_date", date);
					temp.set("create_date", date);
					temp.set("graduate_ver_id", newVersionId);
				}
				if(!CcGraduate.dao.batchSave(ccGraduates)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldGraduateIds", oldGraduateIds);
				allMessage.set("oldAndNewGraduateIds", oldAndNewGraduateIds);
			}
		}
		return true;
	}

	/**
	 * 复制版本的时候--成绩数据复制
	 * @param parentId
	 * @param newId
	 * @param allMessage
	 * @return
	 */
	private Boolean copyVersionAchievement(Record allMessage, Long newId, Long parentId) {
		/*
		 * B. 成绩表复制
		 * 		// 达成度计算（考核成绩分析法）
		 * 		1.  教师开课课程
		 * 		2.  教学班-cc_educlass
		 * 		3.  教学班学生-cc_educlass_student
		 * 		4.  开课课程成绩组成元素表-cc_course_gradecompose
		 * 		5.  成绩组成元素明细-cc_course_gradecompose_detail
		 * 		6.  成绩组成元素明细学生关联表-cc_course_gradecompose_studetail
		 * 		7.  成绩组成元素明细课程目标关联表-cc_course_gradecompose_detail_indication
		 * 		8.  开课课程成绩组成元素课程目标关联表-cc_course_gradecompose_indication
		 * 		9. 考核成绩分析法学生课程目标成绩-cc_score_stu_indigrade
		 *
		 * 		// 达成度计算（评分表分析法）
		 * 		10. 考评点
		 * 		11. 考评点得分层次
		 * 		12. 学生考评点成绩
		 * 		13. 专业认证学生表
		 *      14. 开课课程成绩组成元素课程目标关联的分数范围备注 cc_gradecompose_indication_score_remark
		 *
		 */
		Long versionCreateLogId = allMessage.getLong("versionCreateLogId");
		CcVersionCreateLogService ccVersionCreateLogService = SpringContextHolder.getBean(CcVersionCreateLogService.class);

		// 1.  教师开课课程
		Boolean result = Boolean.FALSE;
		String message = "拷贝教师开课课程";
		String messageNext = "";
		Integer type = CcVersionCreateLog.STEP_TEACHER_COURSE;
		try {
			result = copyTeacherCourse(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			messageNext = "拷贝教学班";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// 2.   教学班-cc_educlass
		message = messageNext;
		type = CcVersionCreateLog.STEP_EDUCLASS;
		try {
			result = copyEduclass(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			messageNext = "拷贝教学班学生";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// 3.  教学班学生-cc_educlass_student
		message = messageNext;
		type = CcVersionCreateLog.STEP_EDUCLASS_STUDENT;
		try {
			result = copyEduclassStudent(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			messageNext = "拷贝教学班学生";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// 5.  开课课程成绩组成元素表-cc_course_gradecompose
		message = messageNext;
		type = CcVersionCreateLog.STEP_COURSE_GRADECOMPOSE;
		try {
			result = copyCourseGradecompose(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			messageNext = "拷贝成绩组成元素明细";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// 6.  成绩组成元素明细-cc_course_gradecompose_detail
		message = messageNext;
		type = CcVersionCreateLog.STEP_COURSE_GRADECOMPOSE_DETAIL;
		try {
			result = copyCourseGradecomposeDetail(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			messageNext = "拷贝成绩组成元素明细学生关联表";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// 7.  成绩组成元素明细学生关联表-cc_course_gradecompose_studetail
		message = messageNext;
		type = CcVersionCreateLog.STEP_COURSE_GRADECOMPOSE_STUDETAIL;
		try {
			result = copyCourseGradecomposeStudetail(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			messageNext = "拷贝成绩组成元素明细指标点关联表";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// 8.  成绩组成元素明细课程目标关联表-cc_course_gradecompose_detail_indication
		message = messageNext;
		type = CcVersionCreateLog.STEP_COURSE_GRADECOMPOSE_DETAIL_INDICATION;
		try {
			result = copyCourseGradecomposeDetailIndication(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			messageNext = "拷贝开课课程成绩组成元素指标点关联表";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// 9.  开课课程成绩组成元素课程目标关联表-cc_course_gradecompose_indication
		message = messageNext;
		type = CcVersionCreateLog.STEP_COURSE_GRADECOMPOSE_INDICATION;
		try {
			result = copyCourseGradecomposeIndication(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			messageNext = "拷贝考核成绩分析法学生指标点成绩";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// 10. 考核成绩分析法学生指标点成绩-cc_score_stu_indigrade
		message = messageNext;
		type = CcVersionCreateLog.STEP_SCORE_STU_INDICATION;
		try {
			result = copyScoreStuIndigrade(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			messageNext = "拷贝考评点";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// 11. 考评点
		message = messageNext;
		type = CcVersionCreateLog.STEP_EVALUTE;
		try {
			result = copyEvalute(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			messageNext = "拷贝考评点得分层次";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// 12. 考评点得分层次
		message = messageNext;
		type = CcVersionCreateLog.STEP_EVALUTE_LEVEL;
		try {
			result = copyEvaluteLevel(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			messageNext = "拷贝学生考评点成绩";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// 13. 学生考评点成绩
		message = messageNext;
		type = CcVersionCreateLog.STEP_STUDENT_EVALUTE;
		try {
			result = copyStudentEvalute(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			messageNext = "拷贝专业认证学生表";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// 14. 专业认证学生表
		message = messageNext;
		type = CcVersionCreateLog.STEP_MAJOR_STUDENT;
		try {
			result = copyMajorStudent(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			messageNext = "开课课程成绩组成元素课程目标关联的分数范围备注";
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message + ",正在" + messageNext, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		//15. 开课课程成绩组成元素课程目标关联的分数范围备注 cc_gradecompose_indication_score_remark
		message = messageNext;
		type = CcVersionCreateLog.STEP_SCORE_REMARK;
		try {
			result = copyScoreRemark(allMessage);
			if(!result) {
				ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			ccVersionCreateLogService.changeStepJob(versionCreateLogId, "成功" + message, type);
		} catch (Exception e) {
			ccVersionCreateLogService.changeStepJobForError(versionCreateLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}


		return true;
	}


	/**
	 * 拷贝课程目标与指标点与课程关系的关系表
	 * @param allMessage
	 * @return
	 */
	private Boolean copyCourseTargetIndication(Record allMessage){
		Date date = allMessage.getDate("date");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);
		Long[] oldCourseTargetIds = allMessage.get("oldCourseTargetIds");
		Map<Long, Long> oldAndNewCourseTargetIds = allMessage.get("oldAndNewCourseTargetIds");
		Map<Long, Long> oldAndNewIndicationCourseIds = allMessage.get("oldAndNewIndicationCourseIds");
		if(oldCourseTargetIds != null && oldCourseTargetIds.length > 0 ){
			List<CcCourseTargetIndication> ccCourseTargetIndications = CcCourseTargetIndication.dao.findByColumnIn("indication_id", oldCourseTargetIds);
			if(!ccCourseTargetIndications.isEmpty()){
				Map<Long, Long> oldAndNewCourseTargetIndicationIds = new HashMap<>();
                Long[] oldCourseTargetIndicationIds = new Long[ccCourseTargetIndications.size()];
				for(int i=0; i<ccCourseTargetIndications.size(); i++){
					CcCourseTargetIndication ccCourseTargetIndication = ccCourseTargetIndications.get(0);
					oldCourseTargetIndicationIds[i] = ccCourseTargetIndication.getLong("id");
					Long id = idGenetate.getNextValue();
					oldAndNewCourseTargetIndicationIds.put(ccCourseTargetIndication.getLong("id"), id);
					ccCourseTargetIndication.set("id",id);
					ccCourseTargetIndication.set("create_date", date);
					ccCourseTargetIndication.set("mmodify_date", date);
					ccCourseTargetIndication.set("indication_id", oldAndNewCourseTargetIds.get(ccCourseTargetIndication.getLong("indication_id")));
					ccCourseTargetIndication.set("indication_course_id", oldAndNewIndicationCourseIds.get(ccCourseTargetIndication.getLong("indication_course_id")));
				}
				allMessage.set("oldCourseTargetIndicationIds", oldCourseTargetIndicationIds);
				allMessage.set("oldAndNewCourseTargetIndicationIds", oldAndNewCourseTargetIndicationIds);
				if(!CcCourseTargetIndication.dao.batchSave(ccCourseTargetIndications)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 拷贝开课课程成绩组成元素课程目标关联的分数范围备注
	 * @param allMessage
	 * @return
	 */
	private Boolean copyScoreRemark(Record allMessage){
		Date date = allMessage.getDate("date");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);
		Long[] oldCourseGradecomposeIndicationIds = allMessage.get("oldCourseGradecomposeIndicationIds");
		Map<Long,Long> oldAndNewCourseGradecomposeIndicationIds = allMessage.get("oldAndNewCourseGradecomposeIndicationIds");
		if(oldCourseGradecomposeIndicationIds != null && oldCourseGradecomposeIndicationIds.length > 0){
			List<CcGradecomposeIndicationScoreRemark> ccGradecomposeIndicationScoreRemarks = CcGradecomposeIndicationScoreRemark.dao.findByColumnIn("gradecompose_indication_id", oldCourseGradecomposeIndicationIds);
			if(!ccGradecomposeIndicationScoreRemarks.isEmpty()){
				for(CcGradecomposeIndicationScoreRemark ccGradecomposeIndicationScoreRemark : ccGradecomposeIndicationScoreRemarks){
					ccGradecomposeIndicationScoreRemark.set("id", idGenetate.getNextValue());
					ccGradecomposeIndicationScoreRemark.set("create_date", date);
					ccGradecomposeIndicationScoreRemark.set("modify_date", date);
					ccGradecomposeIndicationScoreRemark.set("gradecompose_indication_id", oldAndNewCourseGradecomposeIndicationIds.get("gradecompose_indication_id"));
				}
				if(!CcGradecomposeIndicationScoreRemark.dao.batchSave(ccGradecomposeIndicationScoreRemarks)){
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
			}
		}
		return true;
	}


	/**
	 * 拷贝专业认证学生表
	 * @param allMessage
	 * @return
	 */
	private Boolean copyMajorStudent(Record allMessage) {
		Date date = allMessage.getDate("date");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);
		Long newVersionId = allMessage.getLong("newVersionId");
		Long oldVersionId = allMessage.getLong("oldVersionId");
		Map<Long, Long> oldAndNewMajorDirectionIds = allMessage.get("oldAndNewMajorDirectionIds");

		List<CcMajorStudent> ccMajorStudents = CcMajorStudent.dao.findByColumn("version_id", oldVersionId);
		Long[] oldMajorStudentIds = new Long[ccMajorStudents.size()];
		// oldId,newId
		Map<Long, Long> oldAndNewMajorStudentIds = new HashMap<>();
		for(int i = 0; i < ccMajorStudents.size(); i++) {
			CcMajorStudent temp = ccMajorStudents.get(i);
			oldMajorStudentIds[i] = temp.getLong("id");
			Long id = idGenetate.getNextValue();
			oldAndNewMajorStudentIds.put(temp.getLong("id"), id);

			temp.set("id", id);
			temp.set("modify_date", date);
			temp.set("create_date", date);
			temp.set("version_id", newVersionId);
			temp.set("major_direction_id", oldAndNewMajorDirectionIds.get(temp.getLong("major_direction_id")));
		}
		if(!CcMajorStudent.dao.batchSave(ccMajorStudents)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		allMessage.set("oldMajorStudentIds", oldMajorStudentIds);
		allMessage.set("oldAndNewMajorStudentIds", oldAndNewMajorStudentIds);
		return true;
	}

	/**
	 * 拷贝学生考评点成绩
	 * @param allMessage
	 * @return
	 */
	private Boolean copyStudentEvalute(Record allMessage) {
		Date date = allMessage.getDate("date");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);
		Long[] oldEvaluteIds = allMessage.get("oldEvaluteIds");
		Map<Long, Long> oldAndNewEvaluteIds = allMessage.get("oldAndNewEvaluteIds");
		Map<Long, Long> oldAndNewEvaluteLevelIds = allMessage.get("oldAndNewEvaluteLevelIds");

		if(oldEvaluteIds != null && oldEvaluteIds.length > 0 ){
			List<CcStudentEvalute> ccStudentEvalutes = CcStudentEvalute.dao.findFilteredByColumnIn("evalute_id", oldEvaluteIds);
			if(!ccStudentEvalutes.isEmpty()){
				Long[] oldStudentEvaluteIds = new Long[ccStudentEvalutes.size()];
				// oldId,newId
				Map<Long, Long> oldAndNewStudentEvaluteIds = new HashMap<>();
				for(int i = 0; i < ccStudentEvalutes.size(); i++) {
					CcStudentEvalute temp = ccStudentEvalutes.get(i);
					oldStudentEvaluteIds[i] = temp.getLong("id");
					Long id = idGenetate.getNextValue();
					oldAndNewStudentEvaluteIds.put(temp.getLong("id"), id);

					temp.set("id", id);
					temp.set("modify_date", date);
					temp.set("create_date", date);
					temp.set("evalute_id", oldAndNewEvaluteIds.get(temp.getLong("evalute_id")));
					temp.set("level_id", oldAndNewEvaluteLevelIds.get(temp.getLong("level_id")));
				}
				if(!CcStudentEvalute.dao.batchSave(ccStudentEvalutes)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldStudentEvaluteIds", oldStudentEvaluteIds);
				allMessage.set("oldAndNewStudentEvaluteIds", oldAndNewStudentEvaluteIds);
			}
		}
		return true;
	}

	/**
	 * 拷贝考评点得分层次
	 * @param allMessage
	 * @return
	 */
	private Boolean copyEvaluteLevel(Record allMessage) {
		Date date = allMessage.getDate("date");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);
		Long[] oldTeacherCourseIds = allMessage.get("oldTeacherCourseIds");
		Map<Long, Long> oldAndNewTeacherCourseIds = allMessage.get("oldAndNewTeacherCourseIds");
		Map<Long, Long> oldAndNewIndicationIds = allMessage.get("oldAndNewIndicationIds");

		if(oldTeacherCourseIds != null && oldTeacherCourseIds.length > 0 ){
			List<CcEvaluteLevel> ccEvaluteLevels = CcEvaluteLevel.dao.findFilteredByColumnIn("teacher_course_id", oldTeacherCourseIds);
			if(!ccEvaluteLevels.isEmpty()){
				Long[] oldEvaluteLevelIds = new Long[ccEvaluteLevels.size()];
				// oldId,newId
				Map<Long, Long> oldAndNewEvaluteLevelIds = new HashMap<>();
				for(int i = 0; i < ccEvaluteLevels.size(); i++) {
					CcEvaluteLevel temp = ccEvaluteLevels.get(i);
					oldEvaluteLevelIds[i] = temp.getLong("id");
					Long id = idGenetate.getNextValue();
					oldAndNewEvaluteLevelIds.put(temp.getLong("id"), id);

					temp.set("id", id);
					temp.set("modify_date", date);
					temp.set("create_date", date);
					temp.set("teacher_course_id", oldAndNewTeacherCourseIds.get(temp.getLong("teacher_course_id")));
					temp.set("indication_id", oldAndNewIndicationIds.get(temp.getLong("indication_id")));
				}
				if(!CcEvaluteLevel.dao.batchSave(ccEvaluteLevels)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldEvaluteLevelIds", oldEvaluteLevelIds);
				allMessage.set("oldAndNewEvaluteLevelIds", oldAndNewEvaluteLevelIds);
			}
		}
		return true;
	}

	/**
	 * 拷贝考评点
	 * @param allMessage
	 * @return
	 */
	private Boolean copyEvalute(Record allMessage) {
		Date date = allMessage.getDate("date");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);
		Long[] oldTeacherCourseIds = allMessage.get("oldTeacherCourseIds");
		Map<Long, Long> oldAndNewTeacherCourseIds = allMessage.get("oldAndNewTeacherCourseIds");
		Map<Long, Long> oldAndNewIndicationIds = allMessage.get("oldAndNewIndicationIds");

		if(oldTeacherCourseIds != null && oldTeacherCourseIds.length > 0 ){
			List<CcEvalute> ccEvalutes = CcEvalute.dao.findFilteredByColumnIn("teacher_course_id", oldTeacherCourseIds);
			if(!ccEvalutes.isEmpty()){
				Long[] oldEvaluteIds = new Long[ccEvalutes.size()];
				// oldId,newId
				Map<Long, Long> oldAndNewEvaluteIds = new HashMap<>();
				for(int i = 0; i < ccEvalutes.size(); i++) {
					CcEvalute temp = ccEvalutes.get(i);
					oldEvaluteIds[i] = temp.getLong("id");
					Long id = idGenetate.getNextValue();
					oldAndNewEvaluteIds.put(temp.getLong("id"), id);

					temp.set("id", id);
					temp.set("modify_date", date);
					temp.set("create_date", date);
					temp.set("teacher_course_id", oldAndNewTeacherCourseIds.get(temp.getLong("teacher_course_id")));
					temp.set("indication_id", oldAndNewIndicationIds.get(temp.getLong("indication_id")));
				}
				if(!CcEvalute.dao.batchSave(ccEvalutes)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldEvaluteIds", oldEvaluteIds);
				allMessage.set("oldAndNewEvaluteIds", oldAndNewEvaluteIds);
			}
		}
		return true;
	}

	/**
	 * 拷贝考核成绩分析法学生指标点成绩
	 * @param allMessage
	 * @return
	 */
	private Boolean copyScoreStuIndigrade(Record allMessage) {
		Date date = allMessage.getDate("date");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);
		Long[] oldCourseGradecomposeIndicationIds = allMessage.get("oldCourseGradecomposeIndicationIds");
		Map<Long, Long> oldAndNewCourseGradecomposeIndicationIds = allMessage.get("oldAndNewCourseGradecomposeIndicationIds");

		if(oldCourseGradecomposeIndicationIds != null && oldCourseGradecomposeIndicationIds.length >0 ){
			List<CcScoreStuIndigrade> ccScoreStuIndigrades = CcScoreStuIndigrade.dao.findFilteredByColumnIn("gradecompose_indication_id", oldCourseGradecomposeIndicationIds);
			if(!ccScoreStuIndigrades.isEmpty()){
				Long[] oldScoreStuIndigradeIds = new Long[ccScoreStuIndigrades.size()];
				// oldId,newId
				Map<Long, Long> oldAndNewScoreStuIndigradeIds = new HashMap<>();
				for(int i = 0; i < ccScoreStuIndigrades.size(); i++) {
					CcScoreStuIndigrade temp = ccScoreStuIndigrades.get(i);
					oldScoreStuIndigradeIds[i] = temp.getLong("id");
					Long id = idGenetate.getNextValue();
					oldAndNewScoreStuIndigradeIds.put(temp.getLong("id"), id);

					temp.set("id", id);
					temp.set("modify_date", date);
					temp.set("create_date", date);
					temp.set("gradecompose_indication_id", oldAndNewCourseGradecomposeIndicationIds.get(temp.getLong("gradecompose_indication_id")));
				}
				if(!CcScoreStuIndigrade.dao.batchSave(ccScoreStuIndigrades)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldScoreStuIndigradeIds", oldScoreStuIndigradeIds);
				allMessage.set("oldAndNewScoreStuIndigradeIds", oldAndNewScoreStuIndigradeIds);
			}
		}
		return true;
	}

	/**
	 * 拷贝开课课程成绩组成元素指标点关联表
	 * @param allMessage
	 * @return
	 */
	private Boolean copyCourseGradecomposeIndication(Record allMessage) {
		Date date = allMessage.getDate("date");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);
		Long[] oldCourseGradecomposeIds = allMessage.get("oldCourseGradecomposeIds");
		Map<Long, Long> oldAndNewCourseGradecomposeIds = allMessage.get("oldAndNewCourseGradecomposeIds");
		Map<Long, Long> oldAndNewCourseTargetIds = allMessage.get("oldAndNewCourseTargetIds");

		if(oldCourseGradecomposeIds != null && oldCourseGradecomposeIds.length > 0 ){
			List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndications = CcCourseGradecomposeIndication.dao.findFilteredByColumnIn("course_gradecompose_id", oldCourseGradecomposeIds);
			if(!ccCourseGradecomposeIndications.isEmpty()){
				Long[] oldCourseGradecomposeIndicationIds = new Long[ccCourseGradecomposeIndications.size()];
				// oldId,newId
				Map<Long, Long> oldAndNewCourseGradecomposeIndicationIds = new HashMap<>();
				for(int i = 0; i < ccCourseGradecomposeIndications.size(); i++) {
					CcCourseGradecomposeIndication temp = ccCourseGradecomposeIndications.get(i);
					oldCourseGradecomposeIndicationIds[i] = temp.getLong("id");
					Long id = idGenetate.getNextValue();
					oldAndNewCourseGradecomposeIndicationIds.put(temp.getLong("id"), id);

					temp.set("id", id);
					temp.set("modify_date", date);
					temp.set("create_date", date);
					temp.set("course_gradecompose_id", oldAndNewCourseGradecomposeIds.get(temp.getLong("course_gradecompose_id")));
					temp.set("indication_id", oldAndNewCourseTargetIds.get(temp.getLong("indication_id")));
				}
				if(!CcCourseGradecomposeIndication.dao.batchSave(ccCourseGradecomposeIndications)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldCourseGradecomposeIndicationIds", oldCourseGradecomposeIndicationIds);
				allMessage.set("oldAndNewCourseGradecomposeIndicationIds", oldAndNewCourseGradecomposeIndicationIds);
			}
		}
		return true;
	}

	/**
	 * 拷贝成绩组成元素明细指标点关联表
	 * @param allMessage
	 * @return
	 */
	private Boolean copyCourseGradecomposeDetailIndication(Record allMessage) {
		Date date = allMessage.getDate("date");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);
		Long[] oldCourseGradecomposeDetailIds = allMessage.get("oldCourseGradecomposeDetailIds");
		Map<Long, Long> oldAndNewCourseGradecomposeDetailIds = allMessage.get("oldAndNewCourseGradecomposeDetailIds");
		Map<Long, Long> oldAndNewCourseTargetIds = allMessage.get("oldAndNewCourseTargetIds");

		if(oldCourseGradecomposeDetailIds != null && oldCourseGradecomposeDetailIds.length > 0 ){
			List<CcCourseGradecomposeDetailIndication> ccCourseGradecomposeDetailIndications = CcCourseGradecomposeDetailIndication.dao.findFilteredByColumnIn("course_gradecompose_detail_id", oldCourseGradecomposeDetailIds);
			if(!ccCourseGradecomposeDetailIndications.isEmpty()){
				Long[] oldCourseGradecomposeDetailIndicationIds = new Long[ccCourseGradecomposeDetailIndications.size()];
				// oldId,newId
				Map<Long, Long> oldAndNewCourseGradecomposeDetailIndicationIds = new HashMap<>();
				for(int i = 0; i < ccCourseGradecomposeDetailIndications.size(); i++) {
					CcCourseGradecomposeDetailIndication temp = ccCourseGradecomposeDetailIndications.get(i);
					oldCourseGradecomposeDetailIndicationIds[i] = temp.getLong("id");
					Long id = idGenetate.getNextValue();
					oldAndNewCourseGradecomposeDetailIndicationIds.put(temp.getLong("id"), id);

					temp.set("id", id);
					temp.set("modify_date", date);
					temp.set("create_date", date);
					temp.set("course_gradecompose_detail_id", oldAndNewCourseGradecomposeDetailIds.get(temp.getLong("course_gradecompose_detail_id")));
					temp.set("indication_id", oldAndNewCourseTargetIds.get(temp.getLong("indication_id")));
				}
				if(!CcCourseGradecomposeDetailIndication.dao.batchSave(ccCourseGradecomposeDetailIndications)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldCourseGradecomposeDetailIndicationIds", oldCourseGradecomposeDetailIndicationIds);
				allMessage.set("oldAndNewCourseGradecomposeDetailIndicationIds", oldAndNewCourseGradecomposeDetailIndicationIds);
			}
		}
		return true;
	}

	/**
	 * 拷贝成绩组成元素明细学生关联表
	 * @param allMessage
	 * @return
	 */
	private Boolean copyCourseGradecomposeStudetail(Record allMessage) {
		Date date = allMessage.getDate("date");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);
		Long[] oldCourseGradecomposeDetailIds = allMessage.get("oldCourseGradecomposeDetailIds");
		Map<Long, Long> oldAndNewCourseGradecomposeDetailIds = allMessage.get("oldAndNewCourseGradecomposeDetailIds");

		if(oldCourseGradecomposeDetailIds !=null && oldCourseGradecomposeDetailIds.length > 0){
			List<CcCourseGradecomposeStudetail> ccCourseGradecomposeStudetails = CcCourseGradecomposeStudetail.dao.findFilteredByColumnIn("detail_id", oldCourseGradecomposeDetailIds);
			if(!ccCourseGradecomposeStudetails.isEmpty()){
				Long[] oldCourseGradecomposeStudetailIds = new Long[ccCourseGradecomposeStudetails.size()];
				// oldId,newId
				Map<Long, Long> oldAndNewCourseGradecomposeStudetailIds = new HashMap<>();
				for(int i = 0; i < ccCourseGradecomposeStudetails.size(); i++) {
					CcCourseGradecomposeStudetail temp = ccCourseGradecomposeStudetails.get(i);
					oldCourseGradecomposeStudetailIds[i] = temp.getLong("id");
					Long id = idGenetate.getNextValue();
					oldAndNewCourseGradecomposeStudetailIds.put(temp.getLong("id"), id);

					temp.set("id", id);
					temp.set("modify_date", date);
					temp.set("create_date", date);
					temp.set("detail_id", oldAndNewCourseGradecomposeDetailIds.get(temp.getLong("detail_id")));
				}
				if(!CcCourseGradecomposeStudetail.dao.batchSave(ccCourseGradecomposeStudetails)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldCourseGradecomposeStudetailIds", oldCourseGradecomposeStudetailIds);
				allMessage.set("oldAndNewCourseGradecomposeStudetailIds", oldAndNewCourseGradecomposeStudetailIds);
			}
		}
		return true;
	}

	/**
	 * 拷贝成绩组成元素明细
	 * @param allMessage
	 * @return
	 */
	private Boolean copyCourseGradecomposeDetail(Record allMessage) {
		Date date = allMessage.getDate("date");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);
		Long[] oldCourseGradecomposeIds = allMessage.get("oldCourseGradecomposeIds");
		Map<Long, Long> oldAndNewCourseGradecomposeIds = allMessage.get("oldAndNewCourseGradecomposeIds");

		if(oldCourseGradecomposeIds != null && oldCourseGradecomposeIds.length > 0 && oldAndNewCourseGradecomposeIds != null){
			List<CcCourseGradeComposeDetail> ccCourseGradecomposeDetails = CcCourseGradeComposeDetail.dao.findFilteredByColumnIn("course_gradecompose_id", oldCourseGradecomposeIds);
			Long[] oldCourseGradecomposeDetailIds = new Long[ccCourseGradecomposeDetails.size()];
			// oldId,newId
			Map<Long, Long> oldAndNewCourseGradecomposeDetailIds = new HashMap<>();
			for(int i = 0; i < ccCourseGradecomposeDetails.size(); i++) {
				CcCourseGradeComposeDetail temp = ccCourseGradecomposeDetails.get(i);
				oldCourseGradecomposeDetailIds[i] = temp.getLong("id");
				Long id = idGenetate.getNextValue();
				oldAndNewCourseGradecomposeDetailIds.put(temp.getLong("id"), id);

				temp.set("id", id);
				temp.set("modify_date", date);
				temp.set("create_date", date);
				temp.set("course_gradecompose_id", oldAndNewCourseGradecomposeIds.get(temp.getLong("course_gradecompose_id")));
			}
			if(!CcCourseGradeComposeDetail.dao.batchSave(ccCourseGradecomposeDetails)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			allMessage.set("oldCourseGradecomposeDetailIds", oldCourseGradecomposeDetailIds);
			allMessage.set("oldAndNewCourseGradecomposeDetailIds", oldAndNewCourseGradecomposeDetailIds);
		}
		return true;
	}

	/**
	 * 拷贝开课课程成绩组成元素表
	 * @param allMessage
	 * @return
	 */
	private Boolean copyCourseGradecompose(Record allMessage) {
		Date date = allMessage.getDate("date");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);
		Long[] oldTeacherCourseIds = allMessage.get("oldTeacherCourseIds");
		Map<Long, Long> oldAndNewTeacherCourseIds = allMessage.get("oldAndNewTeacherCourseIds");

		if(oldTeacherCourseIds != null && oldTeacherCourseIds.length > 0 ){
			List<CcCourseGradecompose> ccCourseGradecomposes = CcCourseGradecompose.dao.findFilteredByColumnIn("teacher_course_id", oldTeacherCourseIds);
			if(!ccCourseGradecomposes.isEmpty()){
				Long[] oldCourseGradecomposeIds = new Long[ccCourseGradecomposes.size()];
				// oldId,newId
				Map<Long, Long> oldAndNewCourseGradecomposeIds = new HashMap<>();
				for(int i = 0; i < ccCourseGradecomposes.size(); i++) {
					CcCourseGradecompose temp = ccCourseGradecomposes.get(i);
					oldCourseGradecomposeIds[i] = temp.getLong("id");
					Long id = idGenetate.getNextValue();
					oldAndNewCourseGradecomposeIds.put(temp.getLong("id"), id);

					temp.set("id", id);
					temp.set("modify_date", date);
					temp.set("create_date", date);
					temp.set("teacher_course_id", oldAndNewTeacherCourseIds.get(temp.getLong("teacher_course_id")));
				}
				if(!CcCourseGradecompose.dao.batchSave(ccCourseGradecomposes)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldCourseGradecomposeIds", oldCourseGradecomposeIds);
				allMessage.set("oldAndNewCourseGradecomposeIds", oldAndNewCourseGradecomposeIds);
			}
		}
		return true;
	}

	/**
	 * 教学班学生
	 * @param allMessage
	 * @return
	 */
	private Boolean copyEduclassStudent(Record allMessage) {
		Date date = allMessage.getDate("date");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);
		Map<Long, Long> oldAndNewEduclassIds = allMessage.get("oldAndNewEduclassIds");
		Long[] oldEduclassIds = allMessage.get("oldEduclassIds");

		if(oldEduclassIds != null && oldEduclassIds.length > 0){
			List<CcEduclassStudent> ccEduclassStudens = CcEduclassStudent.dao.findFilteredByColumnIn("class_id", oldEduclassIds);
			if(!ccEduclassStudens.isEmpty()){
				Long[] oldEduclassStudenIds = new Long[ccEduclassStudens.size()];
				// oldId,newId
				Map<Long, Long> oldAndNewEduclassStudenIds = new HashMap<>();
				for(int i = 0; i < ccEduclassStudens.size(); i++) {
					CcEduclassStudent temp = ccEduclassStudens.get(i);
					oldEduclassStudenIds[i] = temp.getLong("id");
					Long id = idGenetate.getNextValue();
					oldAndNewEduclassStudenIds.put(temp.getLong("id"), id);

					temp.set("id", id);
					temp.set("modify_date", date);
					temp.set("create_date", date);
					temp.set("class_id", oldAndNewEduclassIds.get(temp.getLong("class_id")));
				}
				if(!CcEduclassStudent.dao.batchSave(ccEduclassStudens)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldEduclassStudenIds", oldEduclassStudenIds);
				allMessage.set("oldAndNewEduclassStudenIds", oldAndNewEduclassStudenIds);
			}
		}
		return true;
	}

	/**
	 * 拷贝教学班
	 * @param allMessage
	 * @return
	 */
	private Boolean copyEduclass(Record allMessage) {
		Date date = allMessage.getDate("date");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);
		Map<Long, Long> oldAndNewTeacherCourseIds = allMessage.get("oldAndNewTeacherCourseIds");
		Long[] oldTeacherCourseIds = allMessage.get("oldTeacherCourseIds");

		if(oldTeacherCourseIds != null && oldTeacherCourseIds.length > 0 ){
			List<CcEduclass> ccEduclasss = CcEduclass.dao.findFilteredByColumnIn("teacher_course_id", oldTeacherCourseIds);
			if(!ccEduclasss.isEmpty()){
				Long[] oldEduclassIds = new Long[ccEduclasss.size()];
				// oldId,newId
				Map<Long, Long> oldAndNewEduclassIds = new HashMap<>();
				for(int i = 0; i < ccEduclasss.size(); i++) {
					CcEduclass temp = ccEduclasss.get(i);
					oldEduclassIds[i] = temp.getLong("id");
					Long id = idGenetate.getNextValue();
					oldAndNewEduclassIds.put(temp.getLong("id"), id);

					temp.set("id", id);
					temp.set("modify_date", date);
					temp.set("create_date", date);
					temp.set("teacher_course_id", oldAndNewTeacherCourseIds.get(temp.getLong("teacher_course_id")));
				}
				if(!CcEduclass.dao.batchSave(ccEduclasss)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldEduclassIds", oldEduclassIds);
				allMessage.set("oldAndNewEduclassIds", oldAndNewEduclassIds);
			}
		}
		return true;
	}

	/**
	 * 拷贝教师开课课程
	 * @param allMessage
	 * @return
	 */
	private Boolean copyTeacherCourse(Record allMessage) {
		Date date = allMessage.getDate("date");
		IdGenerate idGenetate = SpringContextHolder.getBean(IdGenerate.class);
		Map<Long, Long> oldAndNewCourseIds = allMessage.get("oldAndNewCourseIds");
		Long[] oldCourseIds = allMessage.get("oldCourseIds");

		if(oldCourseIds != null && oldCourseIds.length > 0){
			List<CcTeacherCourse> ccTeacherCourses = CcTeacherCourse.dao.findFilteredByColumnIn("course_id", oldCourseIds);
			if(!ccTeacherCourses.isEmpty()){
				Long[] oldTeacherCourseIds = new Long[ccTeacherCourses.size()];
				// oldId,newId
				Map<Long, Long> oldAndNewTeacherCourseIds = new HashMap<>();
				for(int i = 0; i < ccTeacherCourses.size(); i++) {
					CcTeacherCourse temp = ccTeacherCourses.get(i);
					oldTeacherCourseIds[i] = temp.getLong("id");
					Long id = idGenetate.getNextValue();
					oldAndNewTeacherCourseIds.put(temp.getLong("id"), id);
					temp.set("id", id);
					temp.set("modify_date", date);
					temp.set("create_date", date);
					//TODO 2020/09/27 数据库里可能有脏数据，这里可能为空
					if (oldAndNewCourseIds.get(temp.getLong("course_id")) != null){
						temp.set("course_id", oldAndNewCourseIds.get(temp.getLong("course_id")));
					}

				}
				if(!CcTeacherCourse.dao.batchSave(ccTeacherCourses)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldTeacherCourseIds", oldTeacherCourseIds);
				allMessage.set("oldAndNewTeacherCourseIds", oldAndNewTeacherCourseIds);
			}
		}
		return true;
	}

	/**
	 * 废弃版本
	 * @param allMessage
	 */
	@Transactional
	public boolean close(Record allMessage) {
		Date date = new Date();
		Boolean result;
		Long versionId = allMessage.getLong("versionId");
		CcVersion version = allMessage.get("version");

		// 保存版本信息状态
		CcVersionService ccVersionService = SpringContextHolder.getBean(CcVersionService.class);
		Integer state = version.getInt("state");
		if (CcVersion.STATUE_EDIT.equals(state)) {
			if(!ccVersionService.changeState(versionId, CcVersion.STATUE_CLOSE, CcVersion.STATUE_EDIT)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			if(!CcVersion.dao.deleteAllById(versionId, date)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
		} else {
			return false;
		}

		// 如果是大版本删除，之前的版本的【适用年级】进行修改
		Integer type = version.getInt("type");
		Long majorId = version.getLong("major_id");
		Integer majorVersion = version.getInt("major_version");
		Integer beforeMajorVersion = majorVersion;
		// 如果是增加大版本，找到上一个未删除所在的大版本所有版本对应的apply_grade，  第一次录入就不计算了
		if(CcVersion.TYPE_MAJOR_VERSION.equals(type)) {
			CcVersion beforeVersion = findBeforeVersion(majorId, majorVersion);
			if(beforeVersion != null){
				beforeMajorVersion = beforeVersion.getInt("major_version");

				Integer beforeEnableGrade = beforeVersion.getInt("enable_grade");
				String newParentApplyGrade = beforeEnableGrade + CcVersion.GRADE_CHARACTER ;

				Map<String, Object> params = new HashMap<>();
				params.put("major_version", beforeMajorVersion);
				params.put("major_id", version.getLong("major_id"));
				List<CcVersion> ccVersionList = CcVersion.dao.findFilteredByColumn(params);
				for(CcVersion temp : ccVersionList) {
					temp.set("apply_grade", newParentApplyGrade);
					temp.set("modify_date", date);
				}
				if(!CcVersion.dao.batchUpdate(ccVersionList, "modify_date,apply_grade")) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
			}
			//修改大版本下最新的未删除小版本号
		}else if(CcVersion.TYPE_MINOR_VERSION.equals(type)){
			//找到最新的未删除的小版本号
			Integer maxMinorVersion = CcVersion.dao.findFilteredMinorVersion(majorVersion, majorId);
			//更新同一大版本下的最新小版本编号
			if(!CcVersion.dao.updateMaxMinorVersion(maxMinorVersion, majorVersion, version.getLong("major_id"))) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
		}

		/* * A. 基础表删除
				* 		// 专业认证持续改进方案版本
		        * 		A1.  毕业要求
				* 		A2.  指标点cc_indicator_point
				* 		A3.  专业认证教师
				* 		A4.  课程组
				* 		A5.  培养计划学期表
				* 		A6.  所属模块
				* 		A7.  专业方向
				* 		A8.  课程性质
				* 		A9.  课程层次
				* 		A10. 课程
				*       A11. 课程目标表 cc_indcation
				* 		A12. 指标点与课程关系
				* 		A13. 培养计划课程学期详情表
				* 	    A14. 课程目标与指标点与课程关系的关系表 cc_course_target_indication
				* 		A15. 课程教学大纲文本
				* 		A16. 课程教学大纲操作记录
				* 		A17. 培养计划课程分区表cc_plan_course_zone
				* 		A18. 培养计划课程分区各培养计划学期详情表cc_plan_course_zone_term
				*
		 * B. 成绩表删除
				* 		// 达成度计算（考核成绩分析法）
		 * 		1.  教师开课课程
				* 		2.  教学班-cc_educlass
				* 		3.  教学班学生-cc_educlass_student
				* 		4.  开课课程成绩组成元素表-cc_course_gradecompose
				* 		5.  成绩组成元素明细-cc_course_gradecompose_detail
				* 		6.  成绩组成元素明细学生关联表-cc_course_gradecompose_studetail
				* 		7.  成绩组成元素明细课程目标关联表-cc_course_gradecompose_detail_indication
				* 		8.  开课课程成绩组成元素课程目标关联表-cc_course_gradecompose_indication
				* 		9.  考核成绩分析法学生课程目标成绩-cc_score_stu_indigrade
				*
		        * 		// 达成度计算（评分表分析法）
		        * 		10. 考评点
				* 		11. 考评点得分层次
				* 		12. 学生考评点成绩
				* 		13. 专业认证学生表
				*       14. 开课课程成绩组成元素课程目标关联的分数范围备注 cc_gradecompose_indication_score_remark
		 *
		 *
		 */
		CcVersionDeleteLogService ccVersionDeleteLogService = SpringContextHolder.getBean(CcVersionDeleteLogService.class);

		Long versionDeleteLogId = versionId;
		// A1.  毕业要求
		result = closeGraduate(allMessage);
		String message = "废弃毕业要求点";
		String messageNext = "";
		try {
			type = CcVersionCreateLog.STEP_GRADUATE;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新 
			messageNext = "废弃指标点";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A2.  指标点
		message = messageNext;
		try {
			result = closeIndication(allMessage);
			type = CcVersionCreateLog.STEP_INDICATION;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新 
			messageNext = "废弃专业认证教师";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A3.  专业认证教师
		message = messageNext;
		try {
			result = closeMajorTeacher(allMessage);
			type = CcVersionCreateLog.STEP_MAJOR_TEACHER;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新
			messageNext = "废弃课程组";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A4. 课程组
		message = messageNext;
		try {
			result = closeCourseGroup(allMessage);
			type = CcVersionCreateLog.STEP_COURSE_GROUP;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新 
			messageNext = "废弃培养计划学期表";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A5. 培养计划学期表
		message = messageNext;
		try {
			result = closePlanTerm(allMessage);
			type = CcVersionCreateLog.STEP_PLAN_TERM;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新 
			messageNext = "废弃所属模块";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A6. 所属模块
		message = messageNext;
		try {
			result = closeCourseModule(allMessage);
			type = CcVersionCreateLog.STEP_COURSE_MODULEE;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新 
			messageNext = "废弃专业方向";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A7. 专业方向
		message = messageNext;
		try {
			result = closeMajorDirection(allMessage);
			type = CcVersionCreateLog.STEP_MAJOR_DIRECTION;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新 
			messageNext = "废弃课程性质";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A8. 课程性质
		message = messageNext;
		try {
			result = closeCourseProperty(allMessage);
			type = CcVersionCreateLog.STEP_COURSE_PROPERTY;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新 
			messageNext = "废弃层次";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A9. 课程层次
		message = messageNext;
		try {
			result = closeCourseHierarchy(allMessage);
			type = CcVersionCreateLog.STEP_COURSE_HIERACHY;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新 
			messageNext = "废弃课程";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A10. 课程
		message = messageNext;
		try {
			result = closeCourse(allMessage);
			type = CcVersionCreateLog.STEP_COURSE;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新
			messageNext = "废弃课程目标";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A11. 课程目标
		message = messageNext;
		try {
			result = closeCourseTarget(allMessage);
			type = CcVersionCreateLog.STEP_INDICATION;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新
			messageNext = "废弃指标点与课程关系";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A12. 指标点与课程关系
		message = messageNext;
		try {
			result = closeIndicationCourse(allMessage);
			type = CcVersionCreateLog.STEP_INDICATION_COURSE;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新
			messageNext = "废弃培养计划课程学期详情表";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A13. 培养计划课程学期详情表
		message = messageNext;
		try {
			result = closePlanTermCourse(allMessage);
			type = CcVersionCreateLog.STEP_PLAN_TER_COURSE;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新
			messageNext = "废弃课程目标与指标点与课程关系的关系";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		//A14 课程目标与指标点与课程关系的关系表 cc_course_target_indication
		message = messageNext;
		try {
			result = closeCourseTargetIndication(allMessage);
			type = CcVersionCreateLog.STEP_COURSE_TARGET_INDICATION;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新
			messageNext = "课程教学大纲";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A15. 课程教学大纲
		message = messageNext;
		try {
			result = closeCourseOutline(allMessage);
			type = CcVersionCreateLog.STEP_COURSE_OUTLINE;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新 
			messageNext = "课程教学大纲操作记录";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A16. 课程教学大纲操作记录
		message = messageNext;
		try {
			result = closeCourseOutlineHistory(allMessage);
			type = CcVersionCreateLog.STEP_COURSE_OUTLINE_HISTORY;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新
			messageNext = "培养计划课程分区";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A17. 培养计划课程分区表cc_plan_course_zone
		message = messageNext;
		try {
			result = closePlanCourseZone(allMessage);
			type = CcVersionCreateLog.STEP_PLAN_COURSE_ZONE;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新 
			messageNext = "废弃培养计划课程分区各培养计划学期详情表";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// A18. 培养计划课程分区各培养计划学期详情表cc_plan_course_zone_term
		message = messageNext;
		try {
			result = closePlanCourseZoneTerm(allMessage);
			type = CcVersionCreateLog.STEP_PLAN_COURSE_ZONE_TERM;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新 
			messageNext = "废弃教师开课课程";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		
		/* 
		 *    B. 成绩表删除
				* 		// 达成度计算（考核成绩分析法）
		 * 		1.  教师开课课程
				* 		2.  教学班-cc_educlass
				* 		3.  教学班学生-cc_educlass_student
				* 		4.  开课课程成绩组成元素表-cc_course_gradecompose
				* 		5.  成绩组成元素明细-cc_course_gradecompose_detail
				* 		6.  成绩组成元素明细学生关联表-cc_course_gradecompose_studetail
				* 		7.  成绩组成元素明细课程目标关联表-cc_course_gradecompose_detail_indication
				* 		8.  开课课程成绩组成元素课程目标关联表-cc_course_gradecompose_indication
				* 		9.  考核成绩分析法学生课程目标成绩-cc_score_stu_indigrade
				*
		        * 		// 达成度计算（评分表分析法）
		        * 		10. 考评点
				* 		11. 考评点得分层次
				* 		12. 学生考评点成绩
				* 		13. 专业认证学生表
				*       14. 开课课程成绩组成元素课程目标关联的分数范围备注 cc_gradecompose_indication_score_remark
		 */

		// 1.  教师开课课程
		message = messageNext;
		try {
			result = closeTeacherCourse(allMessage);
			// 如果当前不存在教师课程需要关闭，则直接返回
			Long[] teacherCourseIds = allMessage.get("teacherCourseIds");
			type = CcVersionCreateLog.STEP_TEACHER_COURSE;
			if(teacherCourseIds == null || teacherCourseIds.length < 1) {
				// 日志更新 
				ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + "，因为不存在教师开课课程，所以整个废弃流程完成！", type, Boolean.TRUE);
				return true;
			}
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新 
			messageNext = "废弃教学班";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// 2.   教学班-cc_educlass
		message = messageNext;
		try {
			result = closeEduclass(allMessage);
			type = CcVersionCreateLog.STEP_EDUCLASS;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新 
			messageNext = "废弃教学班学生";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// 3.  教学班学生-cc_educlass_student
		message = messageNext;
		try {
			result = closeEduclassStudent(allMessage);
			type = CcVersionCreateLog.STEP_EDUCLASS_STUDENT;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新 
			messageNext = "废弃开课课程成绩组成元素";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// 5.  开课课程成绩组成元素表-cc_course_gradecompose
		message = messageNext;
		try {
			result = closeCourseGradecompose(allMessage);
			type = CcVersionCreateLog.STEP_COURSE_GRADECOMPOSE;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新 
			messageNext = "废弃成绩组成元素明细";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// 6.  成绩组成元素明细-cc_course_gradecompose_detail
		message = messageNext;
		try {
			result = closeCourseGradecomposeDetail(allMessage);
			type = CcVersionCreateLog.STEP_COURSE_GRADECOMPOSE_DETAIL;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新 
			messageNext = "废弃成绩组成元素明细学生关联表";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// 7.  成绩组成元素明细学生关联表-cc_course_gradecompose_studetail
		message = messageNext;
		try {
			result = closeCourseGradecomposeStudetail(allMessage);
			type = CcVersionCreateLog.STEP_COURSE_GRADECOMPOSE_STUDETAIL;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新 
			messageNext = "废弃成绩组成元素明细课程目标关联表";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}


		// 8.  成绩组成元素明细课程目标关联表-cc_course_gradecompose_detail_indication
		message = messageNext;
		try {
			result = closeCourseGradecomposeDetailIndication(allMessage);
			type = CcVersionCreateLog.STEP_COURSE_GRADECOMPOSE_DETAIL_INDICATION;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新 
			messageNext = "废弃开课课程成绩组成元素指标点关联表";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}


		// 9.  开课课程成绩组成元素课程目标关联表-cc_course_gradecompose_indication
		message = messageNext;
		try {
			result = closeCourseGradecomposeIndication(allMessage);
			type = CcVersionCreateLog.STEP_COURSE_GRADECOMPOSE_INDICATION;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新 
			messageNext = "废弃考核成绩分析法学生指标点成绩";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// 10. 考核成绩分析法学生指标点成绩-cc_score_stu_indigrade
		message = messageNext;
		try {
			result = closeScoreStuIndigrade(allMessage);
			type = CcVersionCreateLog.STEP_SCORE_STU_INDICATION;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新 
			messageNext = "废弃考评点";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// 11. 考评点
		message = messageNext;
		try {
			result = closeEvalute(allMessage);
			type = CcVersionCreateLog.STEP_EVALUTE;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新 
			messageNext = "废弃考评点得分层次";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// 12. 考评点得分层次
		message = messageNext;
		try {
			result = closeEvaluteLevel(allMessage);
			type = CcVersionCreateLog.STEP_EVALUTE_LEVEL;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新 
			messageNext = "废弃学生考评点成绩";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// 13. 学生考评点成绩
		message = messageNext;
		try {
			result = closeStudentEvalute(allMessage);
			type = CcVersionCreateLog.STEP_STUDENT_EVALUTE;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新 
			messageNext = "废弃专业认证学生表";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// 14. 专业认证学生表
		message = messageNext;
		try {
			result = closeMajorStudent(allMessage);
			type = CcVersionCreateLog.STEP_MAJOR_STUDENT;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			// 日志更新
			messageNext = "废弃分数备注";
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message + ",正在" + messageNext, type, Boolean.FALSE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// 14. 开课课程成绩组成元素课程目标关联的分数范围备注 cc_gradecompose_indication_score_remark
		message = messageNext;
		try {
			result = closeScoreRemark(allMessage);
			type = CcVersionCreateLog.STEP_SCORE_REMARK;
			if(!result) {
				ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			ccVersionDeleteLogService.changeStepJob(versionDeleteLogId, "成功" + message, type, Boolean.TRUE);
		} catch (Exception e) {
			ccVersionDeleteLogService.changeStepJobForError(versionDeleteLogId, message, type);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		return true;
	}

	/**
	 * 版本废弃时，同时删除培养计划课程分区各培养计划学期详情表
	 * @param allMessage
	 * @return
	 */
	private Boolean closePlanCourseZoneTerm(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long[] planTermIds = allMessage.get("planTermIds");

		if(planTermIds !=null && planTermIds.length > 0){
			List<CcPlanCourseZoneTerm> ccPlanCourseZoneTerms = CcPlanCourseZoneTerm.dao.findFilteredByColumnIn("plan_term_id", planTermIds);
			if(!ccPlanCourseZoneTerms.isEmpty()){
				Long[] planCourseZoneTermIds = new Long[ccPlanCourseZoneTerms.size()];
				for(int i = 0; i < ccPlanCourseZoneTerms.size(); i++) {
					CcPlanCourseZoneTerm temp = ccPlanCourseZoneTerms.get(i);
					planCourseZoneTermIds[i] = temp.getLong("id");
				}
				if(!CcPlanCourseZoneTerm.dao.deleteAll(planCourseZoneTermIds, date)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("planCourseZoneTermIds", planCourseZoneTermIds);
			}
		}
		return true;
	}

	/**
	 * 版本废弃时，同时删除培养计划课程分区表
	 * @param allMessage
	 * @return
	 */
	private Boolean closePlanCourseZone(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long versionId = allMessage.get("versionId");

		List<CcPlanCourseZone> ccPlanCourseZones = CcPlanCourseZone.dao.findFilteredByColumn("plan_id", versionId);
		if(!ccPlanCourseZones.isEmpty()){
			Long[] planCourseZoneIds = new Long[ccPlanCourseZones.size()];
			for(int i = 0; i < ccPlanCourseZones.size(); i++) {
				CcPlanCourseZone temp = ccPlanCourseZones.get(i);
				planCourseZoneIds[i] = temp.getLong("id");
			}
			if(!CcPlanCourseZone.dao.deleteAll(planCourseZoneIds, date)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			allMessage.set("planCourseZoneIds", planCourseZoneIds);
		}
		return true;
	}

	/**
	 * 版本废弃时，同时删除课程教学大纲操作记录:新建记录以及copy记录 
	 * @param allMessage
	 * @return
	 */
	private Boolean closeCourseOutlineHistory(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long[] courseIds = allMessage.get("courseIds");

		if(courseIds != null && courseIds.length > 0 ){
			List<CcCourseOutline> ccCourseOutlines = CcCourseOutline.dao.findFilteredByColumnIn("course_id", courseIds);
			if(!ccCourseOutlines.isEmpty()){
				Long[] outlineIds = new Long[ccCourseOutlines.size()];
				for(int i = 0; i < ccCourseOutlines.size(); i++) {
					outlineIds[i] = ccCourseOutlines.get(i).getLong("outline_id");
				}
				if(!CcCourseOutlineHistory.dao.deleteAllByColumn("outline_id", outlineIds, date)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 废弃课程目标与指标点与课程关系的关系表
	 * @param allMessage
	 * @return
	 */
	private Boolean closeCourseTargetIndication(Record allMessage){
		Date date = allMessage.getDate("date");
		Long[] oldCourseTargetIds = allMessage.get("oldCourseTargetIds");
		if(oldCourseTargetIds != null && oldCourseTargetIds.length > 0 ){
			List<CcCourseTargetIndication> ccCourseTargetIndications = CcCourseTargetIndication.dao.findByColumnIn("course_target_id", oldCourseTargetIds);
			if(!ccCourseTargetIndications.isEmpty()){
				Long[] oldCourseTargetIndicationIds = new Long[ccCourseTargetIndications.size()];
				for(int i=0; i<ccCourseTargetIndications.size(); i++){
					CcCourseTargetIndication ccCourseTargetIndication = ccCourseTargetIndications.get(0);
					oldCourseTargetIndicationIds[i] = ccCourseTargetIndication.getLong("id");
				}
				if(!CcCourseTargetIndication.dao.deleteAll(oldCourseTargetIndicationIds, date)){
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldCourseTargetIndicationIds", oldCourseTargetIndicationIds);
			}
		}
		return true;
	}


	/**
	 * 版本废弃时，同时删除课程教学大纲文本
	 * @param allMessage
	 * @return
	 */
	private Boolean closeCourseOutline(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long[] courseIds = allMessage.get("courseIds");

		if(courseIds != null && courseIds.length > 0){
			List<CcCourseOutline> ccCourseOutlines = CcCourseOutline.dao.findFilteredByColumnIn("course_id", courseIds);
			if(!ccCourseOutlines.isEmpty()){
				Long[] courseOutlineIds = new Long[ccCourseOutlines.size()];
				for(int i = 0; i < ccCourseOutlines.size(); i++) {
					CcCourseOutline temp = ccCourseOutlines.get(i);
					courseOutlineIds[i] = temp.getLong("id");
				}

				CcCourseOutlineService ccCourseOutlineService = SpringContextHolder.getBean(CcCourseOutlineService.class);
				if(!ccCourseOutlineService.deleteCourseOutline(courseOutlineIds, date)){
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}

				allMessage.set("courseOutlineIds", courseOutlineIds);
			}
		}
		return true;
	}

	/**
	 * 版本废弃时，同时删除课程层次
	 * @param allMessage
	 * @return
	 */
	private Boolean closeCourseHierarchy(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long versionId = allMessage.get("versionId");

		List<CcCourseHierarchy> ccCourseHierarchys = CcCourseHierarchy.dao.findFilteredByColumn("plan_id", versionId);
		if(!ccCourseHierarchys.isEmpty()){
			Long[] courseHierarchyIds = new Long[ccCourseHierarchys.size()];
			for(int i = 0; i < ccCourseHierarchys.size(); i++) {
				CcCourseHierarchy temp = ccCourseHierarchys.get(i);
				courseHierarchyIds[i] = temp.getLong("id");
			}
			if(!CcCourseHierarchy.dao.deleteAll(courseHierarchyIds, date)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			allMessage.set("courseHierarchyIds", courseHierarchyIds);
		}
		/*
		 * 次要课程层次 
		 */
		List<CcCourseHierarchySecondary> ccCourseHierarchySecondarys = CcCourseHierarchySecondary.dao.findFilteredByColumn("plan_id", versionId);
		if(!ccCourseHierarchySecondarys.isEmpty()){
			Long[] courseHierarchySecondaryIds = new Long[ccCourseHierarchySecondarys.size()];
			for(int i = 0; i < ccCourseHierarchySecondarys.size(); i++) {
				CcCourseHierarchySecondary temp = ccCourseHierarchySecondarys.get(i);
				courseHierarchySecondaryIds[i] = temp.getLong("id");
			}
			if(!CcCourseHierarchySecondary.dao.deleteAll(courseHierarchySecondaryIds, date)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			allMessage.set("courseHierarchySecondaryIds", courseHierarchySecondaryIds);
		}
		return true;
	}

	/**
	 * 版本废弃时，同时删除课程性质
	 * @param allMessage
	 * @return
	 */
	private Boolean closeCourseProperty(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long versionId = allMessage.get("versionId");

		List<CcCourseProperty> ccCoursePropertys = CcCourseProperty.dao.findFilteredByColumn("plan_id", versionId);
		if(!ccCoursePropertys.isEmpty()){
			Long[] coursePropertyIds = new Long[ccCoursePropertys.size()];
			for(int i = 0; i < ccCoursePropertys.size(); i++) {
				CcCourseProperty temp = ccCoursePropertys.get(i);
				coursePropertyIds[i] = temp.getLong("id");
			}
			if(!CcCourseProperty.dao.deleteAll(coursePropertyIds, date)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			allMessage.set("coursePropertyIds", coursePropertyIds);
		}
		// 删除次要课程性质 2019年12月3日22:14:20 Edit By Sy
		List<CcCoursePropertySecondary> ccCoursePropertySecondrys = CcCoursePropertySecondary.dao.findFilteredByColumn("plan_id", versionId);
		if(!ccCoursePropertySecondrys.isEmpty()){
			Long[] coursePropertySecondaryIds = new Long[ccCoursePropertySecondrys.size()];
			for(int i = 0; i < ccCoursePropertySecondrys.size(); i++) {
				CcCoursePropertySecondary temp = ccCoursePropertySecondrys.get(i);
				coursePropertySecondaryIds[i] = temp.getLong("id");
			}
			if(!CcCoursePropertySecondary.dao.deleteAll(coursePropertySecondaryIds, date)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			allMessage.set("coursePropertySecondaryIds", coursePropertySecondaryIds);
		}
		return true;
	}

	/**
	 * 版本废弃时，同时删除专业方向
	 * @param allMessage
	 * @return
	 */
	private Boolean closeMajorDirection(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long versionId = allMessage.get("versionId");

		List<CcMajorDirection> ccMajorDirections = CcMajorDirection.dao.findFilteredByColumn("plan_id", versionId);
		if(!ccMajorDirections.isEmpty()){
			Long[] majorDirectionIds = new Long[ccMajorDirections.size()];
			for(int i = 0; i < ccMajorDirections.size(); i++) {
				CcMajorDirection temp = ccMajorDirections.get(i);
				majorDirectionIds[i] = temp.getLong("id");
			}
			if(!CcMajorDirection.dao.deleteAll(majorDirectionIds, date)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			allMessage.set("majorDirectionIds", majorDirectionIds);
		}
		return true;
	}

	/**
	 * 版本废弃时，同时删除所属模块
	 * @param allMessage
	 * @return
	 */
	private Boolean closeCourseModule(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long versionId = allMessage.get("versionId");

		List<CcCourseModule> ccCourseModules = CcCourseModule.dao.findFilteredByColumn("plan_id", versionId);
		if(!ccCourseModules.isEmpty()){
			Long[] courseModuleIds = new Long[ccCourseModules.size()];
			for(int i = 0; i < ccCourseModules.size(); i++) {
				CcCourseModule temp = ccCourseModules.get(i);
				courseModuleIds[i] = temp.getLong("id");
			}
			if(!CcCourseModule.dao.deleteAll(courseModuleIds, date)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			allMessage.set("courseModuleIds", courseModuleIds);
		}
		return true;
	}

	/**
	 * 版本废弃时，同时删除培养计划学期表
	 * @param allMessage
	 * @return
	 */
	private Boolean closePlanTerm(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long versionId = allMessage.get("versionId");

		List<CcPlanTerm> ccPlanTerms = CcPlanTerm.dao.findFilteredByColumn("plan_id", versionId);
		if(!ccPlanTerms.isEmpty()){
			Long[] planTermIds = new Long[ccPlanTerms.size()];
			for(int i = 0; i < ccPlanTerms.size(); i++) {
				CcPlanTerm temp = ccPlanTerms.get(i);
				planTermIds[i] = temp.getLong("id");
			}
			if(!CcPlanTerm.dao.deleteAll(planTermIds, date)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			allMessage.set("planTermIds", planTermIds);
		}
		return true;
	}

	/**
	 * 版本废弃时，同时删除培养计划课程学期详情表
	 * @param allMessage
	 * @return
	 */
	private Boolean closePlanTermCourse(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long[] planTermIds = allMessage.get("planTermIds");

		if(planTermIds !=null && planTermIds.length > 0){
			List<CcPlanTermCourse> ccPlanTermCourses = CcPlanTermCourse.dao.findFilteredByColumnIn("plan_term_id", planTermIds);
			if(!ccPlanTermCourses.isEmpty()){
				Long[] planTermCourseIds = new Long[ccPlanTermCourses.size()];
				for(int i = 0; i < ccPlanTermCourses.size(); i++) {
					CcPlanTermCourse temp = ccPlanTermCourses.get(i);
					planTermCourseIds[i] = temp.getLong("id");
				}
				if(!CcPlanTermCourse.dao.deleteAll(planTermCourseIds, date)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("planTermCourseIds", planTermCourseIds);
			}
		}
		return true;
	}

	/**
	 * 版本废弃时，同时删除课程组
	 * @param allMessage
	 * @return
	 */
	private Boolean closeCourseGroup(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long versionId = allMessage.get("versionId");

		List<CcCourseGroup> ccCourseGroups = CcCourseGroup.dao.findFilteredByColumn("plan_id", versionId);
		if(!ccCourseGroups.isEmpty()){
			Long[] courseGroupIds = new Long[ccCourseGroups.size()];
			for(int i = 0; i < ccCourseGroups.size(); i++) {
				CcCourseGroup temp = ccCourseGroups.get(i);
				courseGroupIds[i] = temp.getLong("id");
			}
			if(!CcCourseGroup.dao.deleteAll(courseGroupIds, date)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			allMessage.set("courseGroupIds", courseGroupIds);
		}
		return true;
	}

	/**
	 * 版本废弃时，同时删除指标点与课程关系
	 * @param allMessage
	 * @return
	 */
	private Boolean closeIndicationCourse(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long[] indicationIds = allMessage.get("indicationIds");

		if(indicationIds != null && indicationIds.length > 0){
			List<CcIndicationCourse> ccIndicationCourses = CcIndicationCourse.dao.findFilteredByColumnIn("indication_id", indicationIds);
			if(!ccIndicationCourses.isEmpty()){
				Long[] indicationCourseIds = new Long[ccIndicationCourses.size()];
				for(int i = 0; i < ccIndicationCourses.size(); i++) {
					CcIndicationCourse temp = ccIndicationCourses.get(i);
					indicationCourseIds[i] = temp.getLong("id");
				}
				if(!CcIndicationCourse.dao.deleteAll(indicationCourseIds, date)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("indicationCourseIds", indicationCourseIds);
			}
		}
		return true;
	}

	/**
	 * 版本废弃时，同时废弃课程目标
	 * @param allMessage
	 * @return
	 */
	private Boolean closeCourseTarget(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long[] oldCourseIds = allMessage.get("oldCourseIds");

		if(oldCourseIds != null && oldCourseIds.length > 0){
			List<CcIndication> ccIndications = CcIndication.dao.findFilteredByColumnIn("course_id", oldCourseIds);
			if(!ccIndications.isEmpty()){
				Long[] oldCourseTargetIds = new Long[ccIndications.size()];
				for(int i = 0; i < ccIndications.size(); i++) {
					CcIndication temp = ccIndications.get(i);
					oldCourseTargetIds[i] = temp.getLong("id");
				}
				if(!CcIndication.dao.deleteAll(oldCourseTargetIds, date)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("oldCourseTargetIds", oldCourseTargetIds);
			}
		}
		return true;
	}

	/**
	 * 版本废弃时，同时删除课程
	 * @param allMessage
	 * @return
	 */
	private Boolean closeCourse(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long versionId = allMessage.get("versionId");

		List<CcCourse> ccCourses = CcCourse.dao.findFilteredByColumn("plan_id", versionId);
		if(!ccCourses.isEmpty()){
			Long[] courseIds = new Long[ccCourses.size()];
			for(int i = 0; i < ccCourses.size(); i++) {
				CcCourse temp = ccCourses.get(i);
				courseIds[i] = temp.getLong("id");
			}
			if(!CcCourse.dao.deleteAll(courseIds, date)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			allMessage.set("courseIds", courseIds);
		}
		return true;
	}

	/**
	 * 版本废弃时，同时删除专业认证教师
	 * @param allMessage
	 * @return
	 */
	private Boolean closeMajorTeacher(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long versionId = allMessage.get("versionId");

		List<CcMajorTeacher> ccMajorTeachers = CcMajorTeacher.dao.findFilteredByColumn("version_id", versionId);
		if(!ccMajorTeachers.isEmpty()){
			Long[] majorTeacherIds = new Long[ccMajorTeachers.size()];
			for(int i = 0; i < ccMajorTeachers.size(); i++) {
				CcMajorTeacher temp = ccMajorTeachers.get(i);
				majorTeacherIds[i] = temp.getLong("id");
			}
			if(!CcMajorTeacher.dao.deleteAll(majorTeacherIds, date)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			allMessage.set("majorTeacherIds", majorTeacherIds);
		}

		return true;
	}

	/**
	 * 版本废弃时，同时删除指标点
	 * @param allMessage
	 * @return
	 */
	private Boolean closeIndication(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long[] graduateIds = allMessage.get("graduateIds");

		if(graduateIds !=null && graduateIds.length > 0){
			List<CcIndicatorPoint> ccIndications = CcIndicatorPoint.dao.findFilteredByColumnIn("graduate_id", graduateIds);
			if(!ccIndications.isEmpty()){
				Long[] indicationIds = new Long[ccIndications.size()];
				for(int i = 0; i < ccIndications.size(); i++) {
					CcIndicatorPoint temp = ccIndications.get(i);
					indicationIds[i] = temp.getLong("id");
				}
				if(!CcIndication.dao.deleteAll(indicationIds, date)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("indicationIds", indicationIds);
			}
		}
		return true;
	}

	/**
	 * 版本废弃时，同时删除毕业要求
	 * @param allMessage
	 * @return
	 */
	private Boolean closeGraduate(Record allMessage) {
		Long versionId = allMessage.getLong("versionId");
		Date date = allMessage.getDate("date");
		List<CcGraduate> ccGraduates = CcGraduate.dao.findFilteredByColumn("graduate_ver_id", versionId);
		if(!ccGraduates.isEmpty()){
			Long []ids = new Long[ccGraduates.size()];
			for(int i = 0; i < ccGraduates.size(); i++) {
				ids[i] = ccGraduates.get(i).getLong("id");
			}
			if(!CcGraduate.dao.deleteAll(ids, date)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			allMessage.set("graduateIds", ids);
		}
		return true;
	}

	/**
	 * 版本废弃时，同时删除专业认证学生表
	 * @param allMessage
	 * @return
	 */
	private Boolean closeMajorStudent(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long versionId = allMessage.getLong("versionId");

		List<CcMajorStudent> ccCcMajorStudents = CcMajorStudent.dao.findByColumnIn("version_id", versionId);
		if(!ccCcMajorStudents.isEmpty()){
			Long[] majorStudentIds = new Long[ccCcMajorStudents.size()];
			for(int i = 0; i < ccCcMajorStudents.size(); i++) {
				CcMajorStudent temp = ccCcMajorStudents.get(i);
				majorStudentIds[i] = temp.getLong("id");
			}
			if(!CcStudentEvalute.dao.deleteAll(majorStudentIds, date)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			allMessage.set("majorStudentIds", majorStudentIds);
		}
		return true;
	}

	/**
	 * 废弃开课课程成绩组成元素课程目标关联的分数范围备注
	 * @param allMessage
	 * @return
	 */
	private Boolean closeScoreRemark(Record allMessage){
		Date date = allMessage.getDate("date");
		Long[] oldCourseGradecomposeIndicationIds = allMessage.get("oldCourseGradecomposeIndicationIds");
		if(oldCourseGradecomposeIndicationIds != null && oldCourseGradecomposeIndicationIds.length > 0){
			List<CcGradecomposeIndicationScoreRemark> ccGradecomposeIndicationScoreRemarks = CcGradecomposeIndicationScoreRemark.dao.findByColumnIn("gradecompose_indication_id", oldCourseGradecomposeIndicationIds);
			if(!ccGradecomposeIndicationScoreRemarks.isEmpty()){
				Long[]  ccGradecomposeIndicationScoreRemarkIds = new Long[ccGradecomposeIndicationScoreRemarks.size()];
				if(!CcGradecomposeIndicationScoreRemark.dao.deleteAll(ccGradecomposeIndicationScoreRemarkIds, date)){
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 版本废弃时，同时删除学生考评点成绩
	 * @param allMessage
	 * @return
	 */
	private Boolean closeStudentEvalute(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long[] evaluteIds = allMessage.get("evaluteIds");

		if(evaluteIds != null && evaluteIds.length > 0){
			List<CcStudentEvalute> ccStudentEvalutes = CcStudentEvalute.dao.findFilteredByColumnIn("evalute_id", evaluteIds);
			if(!ccStudentEvalutes.isEmpty()){
				Long[] studentEvaluteIds = new Long[ccStudentEvalutes.size()];
				for(int i = 0; i < ccStudentEvalutes.size(); i++) {
					CcStudentEvalute temp = ccStudentEvalutes.get(i);
					studentEvaluteIds[i] = temp.getLong("id");
				}
				if(!CcStudentEvalute.dao.deleteAll(studentEvaluteIds, date)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("studentEvaluteIds", studentEvaluteIds);
			}
		}
		return true;
	}

	/**
	 * 版本废弃时，同时删除考评点得分层次
	 * @param allMessage
	 * @return
	 */
	private Boolean closeEvaluteLevel(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long[] teacherCourseIds = allMessage.get("teacherCourseIds");

		if(teacherCourseIds != null && teacherCourseIds.length > 0){
			List<CcEvaluteLevel> ccEvaluteLevels = CcEvaluteLevel.dao.findFilteredByColumnIn("teacher_course_id", teacherCourseIds);
			if(!ccEvaluteLevels.isEmpty()){
				Long[] evaluteLevelIds = new Long[ccEvaluteLevels.size()];
				for(int i = 0; i < ccEvaluteLevels.size(); i++) {
					CcEvaluteLevel temp = ccEvaluteLevels.get(i);
					evaluteLevelIds[i] = temp.getLong("id");
				}
				if(!CcEvaluteLevel.dao.deleteAll(evaluteLevelIds, date)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("evaluteLevelIds", evaluteLevelIds);
			}
		}
		return true;
	}

	/**
	 * 版本废弃时，同时删除考评点
	 * @param allMessage
	 * @return
	 */
	private Boolean closeEvalute(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long[] teacherCourseIds = allMessage.get("teacherCourseIds");

		if(teacherCourseIds != null && teacherCourseIds.length > 0){
			List<CcEvalute> ccEvalutes = CcEvalute.dao.findFilteredByColumnIn("teacher_course_id", teacherCourseIds);
			if(!ccEvalutes.isEmpty()){
				Long[] evaluteIds = new Long[ccEvalutes.size()];
				for(int i = 0; i < ccEvalutes.size(); i++) {
					CcEvalute temp = ccEvalutes.get(i);
					evaluteIds[i] = temp.getLong("id");
				}
				if(!CcEvalute.dao.deleteAll(evaluteIds, date)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("evaluteIds", evaluteIds);
			}
		}
		return true;
	}

	/**
	 * 版本废弃时，同时删除考核成绩分析法学生指标点成绩
	 * @param allMessage
	 * @return
	 */
	private Boolean closeScoreStuIndigrade(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long[] courseGradecomposeIndicationIds = allMessage.get("courseGradecomposeIndicationIds");

		if(courseGradecomposeIndicationIds != null && courseGradecomposeIndicationIds.length > 0){
			List<CcScoreStuIndigrade> ccScoreStuIndigrades = CcScoreStuIndigrade.dao.findFilteredByColumnIn("gradecompose_indication_id", courseGradecomposeIndicationIds);
			if(!ccScoreStuIndigrades.isEmpty()){
				Long[] scoreStuIndigradeIds = new Long[ccScoreStuIndigrades.size()];
				for(int i = 0; i < ccScoreStuIndigrades.size(); i++) {
					CcScoreStuIndigrade temp = ccScoreStuIndigrades.get(i);
					scoreStuIndigradeIds[i] = temp.getLong("id");
				}
				if(!CcScoreStuIndigrade.dao.deleteAll(scoreStuIndigradeIds, date)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("scoreStuIndigradeIds", scoreStuIndigradeIds);
			}
		}
		return true;
	}

	/**
	 * 版本废弃时，同时删除开课课程成绩组成元素指标点关联表
	 * @param allMessage
	 * @return
	 */
	private Boolean closeCourseGradecomposeIndication(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long[] courseGradecomposeIds = allMessage.get("courseGradecomposeIds");

		if(courseGradecomposeIds !=null && courseGradecomposeIds.length > 0){
			List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndications = CcCourseGradecomposeIndication.dao.findFilteredByColumnIn("course_gradecompose_id", courseGradecomposeIds);
			if(!ccCourseGradecomposeIndications.isEmpty()){
				Long[] courseGradecomposeIndicationIds = new Long[ccCourseGradecomposeIndications.size()];
				for(int i = 0; i < ccCourseGradecomposeIndications.size(); i++) {
					CcCourseGradecomposeIndication temp = ccCourseGradecomposeIndications.get(i);
					courseGradecomposeIndicationIds[i] = temp.getLong("id");
				}
				if(!CcCourseGradecomposeIndication.dao.deleteAll(courseGradecomposeIndicationIds, date)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("courseGradecomposeIndicationIds", courseGradecomposeIndicationIds);
			}
		}
		return true;
	}

	/**
	 * 版本废弃时，同时删除成绩组成元素明细指标点关联表
	 * @param allMessage
	 * @return
	 */
	private Boolean closeCourseGradecomposeDetailIndication(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long[] courseGradecomposeDetailIds = allMessage.get("courseGradecomposeDetailIds");

		if(courseGradecomposeDetailIds != null &&courseGradecomposeDetailIds.length > 0){
			List<CcCourseGradecomposeDetailIndication> ccCourseGradecomposeDetailIndications = CcCourseGradecomposeDetailIndication.dao.findFilteredByColumnIn("course_gradecompose_detail_id", courseGradecomposeDetailIds);
			if(!ccCourseGradecomposeDetailIndications.isEmpty()){
				Long[] courseGradecomposeDetailIndicationIds = new Long[ccCourseGradecomposeDetailIndications.size()];
				for(int i = 0; i < ccCourseGradecomposeDetailIndications.size(); i++) {
					CcCourseGradecomposeDetailIndication temp = ccCourseGradecomposeDetailIndications.get(i);
					courseGradecomposeDetailIndicationIds[i] = temp.getLong("id");
				}
				if(!CcCourseGradecomposeDetailIndication.dao.deleteAll(courseGradecomposeDetailIndicationIds, date)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("courseGradecomposeDetailIndicationIds", courseGradecomposeDetailIndicationIds);
			}
		}
		return true;
	}

	/**
	 * 版本废弃时，同时删除成绩组成元素明细学生关联表
	 * @param allMessage
	 * @return
	 */
	private Boolean closeCourseGradecomposeStudetail(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long[] courseGradecomposeDetailIds = allMessage.get("courseGradecomposeDetailIds");

		if(courseGradecomposeDetailIds !=null && courseGradecomposeDetailIds.length > 0){
			List<CcCourseGradecomposeStudetail> ccCourseGradecomposeStudetails = CcCourseGradecomposeStudetail.dao.findFilteredByColumnIn("detail_id", courseGradecomposeDetailIds);
			if(!ccCourseGradecomposeStudetails.isEmpty()){
				Long[] courseGradecomposeStudetailIds = new Long[ccCourseGradecomposeStudetails.size()];
				for(int i = 0; i < ccCourseGradecomposeStudetails.size(); i++) {
					CcCourseGradecomposeStudetail temp = ccCourseGradecomposeStudetails.get(i);
					courseGradecomposeStudetailIds[i] = temp.getLong("id");
				}
				if(!CcCourseGradecomposeStudetail.dao.deleteAll(courseGradecomposeStudetailIds, date)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("courseGradecomposeStudetailIds", courseGradecomposeStudetailIds);
			}
		}
		return true;
	}

	/**
	 * 版本废弃时，同时删除成绩组成元素明细
	 * @param allMessage
	 * @return
	 */
	private Boolean closeCourseGradecomposeDetail(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long[] courseGradecomposeIds = allMessage.get("courseGradecomposeIds");

		List<CcCourseGradeComposeDetail> ccCourseGradecomposeDetails = CcCourseGradeComposeDetail.dao.findFilteredByColumnIn("course_gradecompose_id", courseGradecomposeIds);
		Long[] courseGradecomposeDetailIds = new Long[ccCourseGradecomposeDetails.size()];
		for(int i = 0; i < ccCourseGradecomposeDetails.size(); i++) {
			CcCourseGradeComposeDetail temp = ccCourseGradecomposeDetails.get(i);
			courseGradecomposeDetailIds[i] = temp.getLong("id");
		}
		if(ccCourseGradecomposeDetails!=null && ccCourseGradecomposeDetails.size() > 0 && !CcCourseGradeComposeDetail.dao.deleteAll(courseGradecomposeDetailIds, date)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		allMessage.set("courseGradecomposeDetailIds", courseGradecomposeDetailIds);
		return true;
	}

	/**
	 * 版本废弃时，同时删除开课课程成绩组成元素表
	 * @param allMessage
	 * @return
	 */
	private Boolean closeCourseGradecompose(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long[] teacherCourseIds = allMessage.get("teacherCourseIds");

		if(teacherCourseIds !=null && teacherCourseIds.length > 0){
			List<CcCourseGradecompose> ccCourseGradecomposes = CcCourseGradecompose.dao.findFilteredByColumnIn("teacher_course_id", teacherCourseIds);
			if(!ccCourseGradecomposes.isEmpty()){
				Long[] courseGradecomposeIds = new Long[ccCourseGradecomposes.size()];
				for(int i = 0; i < ccCourseGradecomposes.size(); i++) {
					CcCourseGradecompose temp = ccCourseGradecomposes.get(i);
					courseGradecomposeIds[i] = temp.getLong("id");
				}
				if(!CcCourseGradecompose.dao.deleteAll(courseGradecomposeIds, date)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("courseGradecomposeIds", courseGradecomposeIds);
			}
		}
		return true;
	}

	/**
	 * 版本废弃时，同时删除教学班学生
	 * @param allMessage
	 * @return
	 */
	private Boolean closeEduclassStudent(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long[] educlassIds = allMessage.get("educlassIds");

		if(educlassIds != null && educlassIds.length > 0){
			List<CcEduclassStudent> ccEduclassStudens = CcEduclassStudent.dao.findFilteredByColumnIn("class_id", educlassIds);
			if(!ccEduclassStudens.isEmpty()){
				Long[] educlassStudenIds = new Long[ccEduclassStudens.size()];
				for(int i = 0; i < ccEduclassStudens.size(); i++) {
					CcEduclassStudent temp = ccEduclassStudens.get(i);
					educlassStudenIds[i] = temp.getLong("id");
				}
				if(!CcEduclassStudent.dao.deleteAll(educlassStudenIds, date)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("educlassStudenIds", educlassStudenIds);
			}
		}
		return true;
	}

	/**
	 * 版本废弃时，同时删除教学班
	 * @param allMessage
	 * @return
	 */
	private Boolean closeEduclass(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long[] teacherCourseIds = allMessage.get("teacherCourseIds");

		if(teacherCourseIds != null && teacherCourseIds.length > 0){
			List<CcEduclass> ccEduclasss = CcEduclass.dao.findByColumnIn("teacher_course_id", teacherCourseIds);
			if(!ccEduclasss.isEmpty()){
				Long[] educlassIds = new Long[ccEduclasss.size()];
				for(int i = 0; i < ccEduclasss.size(); i++) {
					CcEduclass temp = ccEduclasss.get(i);
					educlassIds[i] = temp.getLong("id");
				}
				if(!CcEduclass.dao.deleteAll(educlassIds, date)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("educlassIds", educlassIds);
			}
		}
		return true;
	}

	/**
	 * 版本废弃时，同时删除教师开课课程
	 * @param allMessage
	 * @return
	 */
	private Boolean closeTeacherCourse(Record allMessage) {
		Date date = allMessage.getDate("date");
		Long[] courseIds = allMessage.get("courseIds");

		if(courseIds !=null && courseIds.length > 0){
			List<CcTeacherCourse> ccTeacherCourses = CcTeacherCourse.dao.findByColumnIn("course_id", courseIds);
			if(!ccTeacherCourses.isEmpty()){
				Long[] teacherCourseIds = new Long[ccTeacherCourses.size()];
				for(int i = 0; i < ccTeacherCourses.size(); i++) {
					CcTeacherCourse temp = ccTeacherCourses.get(i);
					teacherCourseIds[i] = temp.getLong("id");
				}
				if(!CcTeacherCourse.dao.deleteAll(teacherCourseIds, date)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				allMessage.set("teacherCourseIds", teacherCourseIds);
			}
		}

		return true;
	}
	/**
	 * 保存version表的关联表
	 * @param ccVersion
	 * @param planName
	 * 			培养计划版本名称
	 * @param planCourseVersionName
	 * 			课程大纲的版本名称
	 * @param graduateName
	 * 			毕业要求版本名称
	 * @param graduateIndicationVersionName
	 * 			指标点的版本名称
	 * @param pass
	 * 			毕业要求达成度合格标准
	 * @return
	 */
	public Boolean saveLink(CcVersion ccVersion, String planName, String planCourseVersionName, String graduateName, String graduateIndicationVersionName, BigDecimal pass) {

		Long versionId = ccVersion.getLong("id");
		Date date = ccVersion.getDate("create_date");
		// 保存培养计划
		CcPlanVersion ccPlanVersion = new CcPlanVersion();
		ccPlanVersion.set("id", versionId);
		ccPlanVersion.set("create_date", date);
		ccPlanVersion.set("modify_date", date);
		ccPlanVersion.set("name", planName);
		ccPlanVersion.set("course_version_name", planCourseVersionName);
		ccPlanVersion.set("is_del", Boolean.FALSE);
		Boolean result = ccPlanVersion.save();
		if(!result) {
			return result;
		}

		// 保存毕业要求版本
		CcGraduateVersion ccGraduateVersion = new CcGraduateVersion();
		ccGraduateVersion.set("id", versionId);
		ccGraduateVersion.set("create_date", date);
		ccGraduateVersion.set("modify_date", date);
		ccGraduateVersion.set("name", graduateName);
		ccGraduateVersion.set("indication_version_name", graduateIndicationVersionName);
		ccGraduateVersion.set("pass", pass);
		ccGraduateVersion.set("is_del", Boolean.FALSE);
		result = ccGraduateVersion.save();
		if(!result) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return result;
		}
		return result;
	}

	/**
	 * 跟新version表的关联表
	 * @param ccVersion
	 * @param planName
	 * 			培养计划版本名称
	 * @param planCourseVersionName
	 * 			课程大纲的版本名称
	 * @param graduateName
	 * 			毕业要求版本名称
	 * @param graduateIndicationVersionName
	 * 			指标点的版本名称
	 * @param pass
	 * 			毕业要求达成度合格标准
	 * @return
	 */
	public Boolean updateLink(CcVersion ccVersion, String planName, String planCourseVersionName, String graduateName, String graduateIndicationVersionName, BigDecimal pass) {

		Long versionId = ccVersion.getLong("id");
		Date date = ccVersion.getDate("create_date");
		// 保存培养计划
		CcPlanVersion ccPlanVersion = CcPlanVersion.dao.findFilteredById(versionId);
		CcGraduateVersion ccGraduateVersion = CcGraduateVersion.dao.findFilteredById(versionId);
		if(ccPlanVersion == null || ccGraduateVersion == null) {
			return false;
		}
		ccPlanVersion.set("modify_date", date);
		ccPlanVersion.set("name", planName);
		ccPlanVersion.set("course_version_name", planCourseVersionName);
		Boolean result = ccPlanVersion.update();
		if(!result) {
			return result;
		}

		// 保存毕业要求版本
		ccGraduateVersion.set("modify_date", date);
		ccGraduateVersion.set("name", graduateName);
		ccGraduateVersion.set("indication_version_name", graduateIndicationVersionName);
		ccGraduateVersion.set("pass", pass);
		result = ccGraduateVersion.update();
		if(!result) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return result;
		}
		return result;
	}

	/**
	 * 验证版本的起始年级的增加是否符合要求
	 * @param type
	 * 			大小版本，1大，2小
	 * @param enableGrade
	 * 			启用年级
	 * @param majorId
	 * 			专业编号
	 * @param parentId
	 * 			父亲编号（可能为空）
	 * @return
	 */
	public ServiceResponse validateSaveEnableGrade(Integer type, Integer enableGrade, Long majorId, Long parentId) {

		// 大版本 或者 最新大版本的小版本
		if(CcVersion.TYPE_MAJOR_VERSION.equals(type)) {
			CcVersion lastCcVersion = findFilteredLastVersion(majorId, null);
			Integer maxEnableGrade;
			//通过未删除的开课课程返回最大的启动年级
			CcTeacherCourse maxGradeTeacherCourse = CcTeacherCourse.dao.findMaxGradeTeacherCourse(majorId);

			if(lastCcVersion != null && maxGradeTeacherCourse != null){
				maxEnableGrade = maxGradeTeacherCourse.getInt("grade") > lastCcVersion.getInt("enable_grade") ? maxGradeTeacherCourse.getInt("grade") : lastCcVersion.getInt("enable_grade");
				if(maxEnableGrade >= enableGrade){
					return ServiceResponse.error("版本启用年级必须大于上一个版本的最大使用年级" + maxEnableGrade);
				}
			}
			//不会出现没有版本却有开课课程的情况
			if(lastCcVersion != null && maxGradeTeacherCourse == null){
				if(lastCcVersion.getInt("enable_grade") >= enableGrade){
					return ServiceResponse.error("版本启用年级必须大于上一个版本的最大使用年级" + lastCcVersion.getInt("enable_grade"));
				}
			}
			return ServiceResponse.succ(true);
		}

		// 小版本
		// 如果存在父亲，则找到对应的数据
		CcVersion parentCcVersion = CcVersion.dao.findById(parentId);
		if(parentCcVersion != null && !enableGrade.equals(parentCcVersion.getInt("enable_grade"))) {
			return ServiceResponse.error("修订版的启用年级不允许修改");
		}

		return ServiceResponse.succ(true);
	}

	/**
	 * 验证版本的起始年级是否符合要求
	 * @param type
	 * 			大小版本，1大，2小
	 * @param enableGrade
	 * 			启用年级
	 * @param majorId
	 * 			专业编号
	 * @param parentId
	 * 			父亲编号（可能为空，如果为空，则验证必须大于最新大版本的上一个大版本的起始年级）
	 * @param versionId
	 * 			版本编号 
	 * @return
	 */
	public ServiceResponse validateUpdateEnableGrade(Integer type, Integer enableGrade, Long majorId, Long parentId, Long versionId) {
		/*
		 * 遇到问题
		 * 1. 可能parentId不存在
		 * 2. 此版本可能是大版本
		 * 	2.1 如果不是最新的大版本，则不允许修改，如果数据发生变化就返回false
		 * 	2.2 需要找到父亲，然后比他大 
		 * 3. 此版本是小版本，必须和主版本一样
		 * 修改掉了lastCcVersion，因为逻辑发生变化，不再是不允许以前的版本修改了，现在是大版本都可以变，所以不能一直获取lastCcVersion了
		 */

		CcVersion ccVersion = CcVersion.dao.findById(versionId);
		Integer majorVersion = ccVersion.getInt("major_version");

		// 大版本号
		Integer oldEnableGrade = ccVersion.getInt("enable_grade");

		// 不存在parentId，而且是大版本
		if(type.equals(CcVersion.TYPE_MAJOR_VERSION)) {
			// 2.1 如果存在下一个大版本，则不允许修改
			CcVersion lastCcVersion = findFilteredLastVersion(majorId, null);
			if(lastCcVersion != null && !lastCcVersion.getLong("id").equals(versionId) && !oldEnableGrade.equals(enableGrade)) {
				return ServiceResponse.error("不是最新的大版本，则不允许修改启用年级");
			}

			// 2.2 需要找到上一个未删除所在的大版本所有版本对应的apply_grade
			CcVersion beforeVersion = findBeforeVersion(majorId, majorVersion);
			if(beforeVersion != null && beforeVersion.getInt("enable_grade") >= enableGrade) {
				return ServiceResponse.error("版本启用年级必须大于上一个版本的最大使用年级" + beforeVersion.getInt("enable_grade"));
			}
			return ServiceResponse.succ(true);
		}

		// 小版本
		// 如果存在父亲，则找到对应的数据
		CcVersion parentCcVersion = CcVersion.dao.findById(parentId);
		if(parentCcVersion != null && !enableGrade.equals(parentCcVersion.getInt("enable_grade"))) {
			return ServiceResponse.error("修订版的启用年级不允许修改");
		}

		return ServiceResponse.succ(true);
	}

	/**
	 * 获取这个版本的使用年限
	 * {逻辑：【这个版本的启用年限~下一个版本的启用年限）}
	 * 注意：这个方法基于validateSaveEnableGrade(type, enableGrade, majorId, parentId)方法返回true的基础上
	 * @param type
	 * 			大小版本，1大，2小
	 * @param enableGrade
	 * 			启用年级
	 * @param parentId
	 * 			父亲编号（可能为空）
	 * @return
	 */
	public String getSaveApplyGrade(Integer type, Integer enableGrade, Long parentId) {
		/*
		 * 说明
		 * 1. 大版本创建的时候，显示：【启用年级】级（含）以后
		 * 2. 小版本创建的时候，直接按照父亲节点的【使用年级】显示
		 */

		// 大版本 或者 最新大版本的小版本
		if(CcVersion.TYPE_MAJOR_VERSION.equals(type)) {
			return enableGrade + CcVersion.GRADE_CHARACTER;
		}

		// 小版本
		CcVersion lastCcVersion = CcVersion.dao.findById(parentId);
		if(lastCcVersion == null) {
			return null;
		}

		return lastCcVersion.getStr("apply_grade");

	}

	/**
	 * 获取这个版本的使用年限
	 * {逻辑：【这个版本的启用年限~下一个版本的启用年限）}
	 * 注意：这个方法基于validateUpdateEnableGrade(type, enableGrade, majorId, parentId)方法返回true的基础上
	 * @param type
	 * 			大小版本，1大，2小
	 * @param enableGrade
	 * 			启用年级
	 * @param majorId
	 * 			专业编号
	 * @param versionId
	 * 			版本编号
	 * @return
	 */
	public String getUpdateApplyGrade(Integer type, Integer enableGrade, Long majorId, Long versionId) {
		/*
		 * 1. 如果是大版本
		 * 	1.1 如果不是最新版，则不允许修改，所以直接返回原本的【适用年级】数据即可
		 * 	1.2 如果是最新版的修改，则直接变成enableGrade + "级（含）以后";
		 * 2. 如果是小版本
		 * 	由于现在是不允许小版本修改【启用年级】，所以返回原本的【适用年级】数据即可
		 */
		CcVersion ccVersion = CcVersion.dao.findById(versionId);
		// 大版本号
		String oldApplyGrade = ccVersion.getStr("apply_grade");

		// 不存在parentId，而且是大版本
		if(type.equals(CcVersion.TYPE_MAJOR_VERSION)) {

			// 1.1 如果不是最新版，则不允许修改，所以直接返回原本的【适用年级】数据即可
			CcVersion lastCcVersion = findFilteredLastVersion(majorId, null);
			if(!lastCcVersion.getLong("id").equals(versionId)) {
				return oldApplyGrade;
			} else {
				return enableGrade + CcVersion.GRADE_CHARACTER;
			}
		}

		// 小版本
		return oldApplyGrade;
	}

}
