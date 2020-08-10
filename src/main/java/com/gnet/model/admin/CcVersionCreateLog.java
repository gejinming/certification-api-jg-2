package com.gnet.model.admin;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;


/**
 *
 * @type model
 * @table cc_version_create_log
 * @author SY
 * @version 1.0
 * @date 2016年7月20日16:03:53
 *
 */
@TableBind(tableName = "cc_version_create_log")
public class CcVersionCreateLog extends DbModel<CcVersionCreateLog> {

	private static final long serialVersionUID = -3958122238237390759L;
	public static final CcVersionCreateLog dao = new CcVersionCreateLog();


	/**
	 * 创建状态--未开始
	 */
	public static final Integer TYPE_STATUS_NOT_START = 0;
	/**
	 * 创建状态--创建中
	 */
	public static final Integer TYPE_STATUS_CREATING = 1;
	/**
	 * 创建状态--成功
	 */
	public static final Integer TYPE_STATUS_SUCCESS = 2;
	/**
	 * 创建状态--失败
	 */
	public static final Integer TYPE_STATUS_FAIL = 3;
	/**
	 * 创建状态--成功_已读
	 */
	public static final Integer TYPE_STATUS_SUCCESS_READ = 4;
	/**
	 * 创建状态--失败_已读
	 */
	public static final Integer TYPE_STATUS_FAIL_READ = 5;

	/** 没有父亲节点的版本的创建中步骤默认为1 **/
	public static final Integer STEP_CREATING = 1;

	/**
	 * 步骤--初始化
	 */
	public static final Integer STEP_NOT_START = 0;
	/**
	 * 步骤--毕业要求
	 */
	public static final Integer STEP_GRADUATE = 1;
	/**
	 * 步骤--指标点
	 */
	public static final Integer STEP_INDICATOR_POINT = 2;
	/**
	 * 步骤--专业认证教师
	 */
	public static final Integer STEP_MAJOR_TEACHER = 3;
	/**
	 * 步骤--课程组
	 */
	public static final Integer STEP_COURSE_GROUP = 4;
	/**
	 * 步骤--培养计划学期
	 */
	public static final Integer STEP_PLAN_TERM = 5;
	/**
	 * 步骤--所属模块表
	 */
	public static final Integer STEP_COURSE_MODULEE = 6;
	/**
	 * 步骤--专业方向
	 */
	public static final Integer STEP_MAJOR_DIRECTION = 7;
	/**
	 * 步骤--课程性质
	 */
	public static final Integer STEP_COURSE_PROPERTY = 8;
	/**
	 * 步骤--课程层次
	 */
	public static final Integer STEP_COURSE_HIERACHY = 9;
	/**
	 * 步骤--课程
	 */
	public static final Integer STEP_COURSE = 10;

	/**
	 * 步骤--课程目标
	 */
	public static final Integer STEP_INDICATION = 11;
	/**
	 * 步骤--指标点与课程关系
	 */
	public static final Integer STEP_INDICATION_COURSE = 12;
	/**
	 * 步骤--培养计划课程学期详情表
	 */
	public static final Integer STEP_PLAN_TER_COURSE = 13;

	/**
	 * 步骤--课程目标与指标点与课程关系的关系
	 */
	public static final Integer STEP_COURSE_TARGET_INDICATION= 14;

	/**
	 * 步骤--课程教学大纲
	 */
	public static final Integer STEP_COURSE_OUTLINE = 15;
	/**
	 * 步骤--课程教学大纲操作记录
	 */
	public static final Integer STEP_COURSE_OUTLINE_HISTORY = 16;
	/**
	 * 步骤--培养计划课程分区表
	 */
	public static final Integer STEP_PLAN_COURSE_ZONE = 17;
	/**
	 * 步骤--培养计划课程分区各培养计划学期详情表
	 */
	public static final Integer STEP_PLAN_COURSE_ZONE_TERM = 18;
	/**
	 * 步骤--教师开课课程
	 */
	public static final Integer STEP_TEACHER_COURSE = 19;
	/**
	 * 步骤--教学班
	 */
	public static final Integer STEP_EDUCLASS = 20;
	/**
	 * 步骤--教学班学生
	 */
	public static final Integer STEP_EDUCLASS_STUDENT = 21;
	/**
	 * 步骤--开课课程成绩组成元素表
	 */
	public static final Integer STEP_COURSE_GRADECOMPOSE = 22;
	/**
	 * 步骤-- 成绩组成元素明细
	 */
	public static final Integer STEP_COURSE_GRADECOMPOSE_DETAIL = 23;
	/**
	 * 步骤--成绩组成元素明细学生关联表
	 */
	public static final Integer STEP_COURSE_GRADECOMPOSE_STUDETAIL = 24;
	/**
	 * 步骤--成绩组成元素明细指标点关联表
	 */
	public static final Integer STEP_COURSE_GRADECOMPOSE_DETAIL_INDICATION = 25;
	/**
	 * 步骤--开课课程成绩组成元素指标点关联表
	 */
	public static final Integer STEP_COURSE_GRADECOMPOSE_INDICATION = 26;
	/**
	 * 步骤--考核成绩分析法学生指标点成绩
	 */
	public static final Integer STEP_SCORE_STU_INDICATION = 27;
	/**
	 /**
	 * 步骤--考评点
	 */
	public static final Integer STEP_EVALUTE = 28;
	/**
	 /**
	 * 步骤--考评点得分层次
	 */
	public static final Integer STEP_EVALUTE_LEVEL = 29;
	/**
	 * 步骤--学生考评点成绩
	 */
	public static final Integer STEP_STUDENT_EVALUTE = 30;
	/**
	 * 步骤--专业学生
	 */
	public static final Integer STEP_MAJOR_STUDENT = 31;

	/**
	 * 步骤--开课课程成绩组成元素课程目标关联的分数范围备注
	 */
	public static final Integer STEP_SCORE_REMARK = 32;

	/**
	 * 步骤--版本复制的时候总共有多少步
	 */
	public static final Integer COPY_STEP_ALL_NUM = 18;

	/**
	 * 步骤--版本创建不进行版本复制总共多少步
	 */
	public static final Integer STEP_ALL_NUM = 1;

	/**
	 * 通过majorId获取当前专业下的所有正在创建/未开始/操作失败/操作成功的versionLog（但现在的道理，应该只有一个！2016年8月17日20:00:50）
	 * @param majorId
	 * @return
	 * @author SY
	 * @date 2016年12月14日16:24:39
	 */
	public List<CcVersionCreateLog> findAndReadByMajorId(Long majorId) {
		List<CcVersionCreateLog> returnList = findByMajorId(majorId);
		// 把日志中成功和失败的，变成状态成功和失败的已读状态， 不需要知道返回的结果，因为可能不存在，可能存在。
		readByMajorId(majorId);
		return returnList;
	}

	/**
	 * 通过majorId获取当前专业下的所有正在创建/未开始/操作失败/操作成功的versionLog（但现在的道理，应该只有一个！2016年8月17日20:00:50）
	 * @param majorId
	 * @return
	 */
	public List<CcVersionCreateLog> findByMajorId(Long majorId) {
		return find("select * from " + tableName + " where major_id = ? and create_status in (" + CcVersionCreateLog.TYPE_STATUS_CREATING + ", " + CcVersionCreateLog.TYPE_STATUS_NOT_START + ", " + CcVersionCreateLog.TYPE_STATUS_SUCCESS + ", " + CcVersionCreateLog.TYPE_STATUS_FAIL + ") ", majorId);
	}

	/**
	 * 通过majorId修改当前专业下的所有操作失败/操作成功的versionLog变成操作失败/操作成功的已读状态（但现在的道理，应该只有一个！2016年8月17日20:00:50）
	 * @param majorId
	 * @return 返回是否已有操作成功的数据（注意，如果不存在数据，也是返回false）
	 * @author SY
	 * @version 创建时间：2016年12月14日 下午2:21:49 
	 */
	private Boolean readByMajorId(Long majorId) {
		Date date = new Date();
		Object[][] op = {{date, TYPE_STATUS_SUCCESS_READ, majorId, TYPE_STATUS_SUCCESS }, {date, TYPE_STATUS_FAIL_READ, majorId, TYPE_STATUS_FAIL}};
		StringBuilder sql = new StringBuilder("update " + tableName + " set modify_date = ? ");
		sql.append(", create_status = ? ");
		sql.append("where major_id = ? ");
		sql.append("and create_status = ? ");

		int[] result = Db.batch(sql.toString(), op);
		if (ArrayUtils.contains(result, 0)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 修改步骤内容
	 * @param id
	 * @param date
	 * @param createMessage
	 * @param createStep
	 */
	public Boolean updateStep(Long id, Date date, String createMessage, Integer createStep) {
		return Db.update("update " + tableName + " set modify_date = ? , create_step = ? , create_message = ?, create_status = ?  where id = ? and create_step < ?", date, createStep, createMessage, CcVersionCreateLog.TYPE_STATUS_CREATING, id, createStep) > 0;
	}

	/**
	 * 复制操作的时候，出错了，修改步骤内容
	 * @param id
	 * @param date
	 * @param createMessage
	 * @param createStep
	 * @return
	 */
	public Boolean updateStepForError(Long id, Date date, String createMessage, Integer createStep) {
		return Db.update("update " + tableName + " set modify_date = ? , create_step = ? , create_message = concat( create_message , ?), create_status = ?  where id = ? ", date, createStep, createMessage, TYPE_STATUS_FAIL, id) > 0;
	}

	/**
	 * 验证当前专业下，是否有正在新增的版本
	 * @param majorId
	 * @return
	 */
	public Boolean validateSavingByMajorId(Long majorId) {
		return Db.queryLong("select count(*) from " + tableName + " where major_id = ? and create_status in (" + CcVersionCreateLog.TYPE_STATUS_CREATING + ", " + CcVersionCreateLog.TYPE_STATUS_NOT_START + ") ", majorId) > 0;
	}

	/**
	 * 版本创建完成设计日志为成功
	 * @param versionCreateLogId
	 * @param date
	 * @return
	 */
	public Boolean updateStepSuccess(Long versionCreateLogId, Date date) {
		return Db.update("update " +  tableName + " set modify_date = ? , create_status = ?, create_message = ? where id = ? ", date, CcVersionCreateLog.TYPE_STATUS_SUCCESS, "创建成功！", versionCreateLogId) > 0;
	}

}
