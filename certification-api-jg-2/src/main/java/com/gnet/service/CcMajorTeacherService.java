package com.gnet.service;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.gnet.model.admin.CcCourse;
import com.gnet.model.admin.CcMajorTeacher;
import com.gnet.model.admin.CcTeacher;
import com.gnet.model.admin.CcVersion;

/**
 * 专业相关教师
 * 
 * @author SY
 * 
 * @date 2016年11月24日15:09:48
 */
@Component("ccMajorTeacherService")
public class CcMajorTeacherService {

	/**
	 * 检测某个课程所在专业是否已经关联了当前教师，如果不存在，则增加关联。
	 * @param teacherId
	 * 			教师编号（如果为空，直接认为是不增加，直接返回true）
	 * @param courseId
	 * 			课程编号（如果为空，直接认为数据出问题，返回false）
	 * @param date
	 * @return 是否新增成功（已经新增，直接返回true）
	 * @author SY 
	 * @version 创建时间：2016年11月24日 下午3:12:11 
	 */
	public Boolean addMajorTeacher(Long teacherId, Date date, Long courseId) {
		if(courseId == null) {
			return false;
		}
		CcCourse ccCourse = CcCourse.dao.findById(courseId);
		if(ccCourse == null) {
			return false;
		}
		Long planId = ccCourse.getLong("plan_id");
		return addMajorTeacher(planId, teacherId, date);
	}
	
	/**
	 * 检测某个专业是否已经关联了当前教师，如果不存在，则增加关联。
	 * @param planId
	 * 			版本编号（如果为空，直接认为数据出问题，返回false）
	 * @param teacherId
	 * 			教师编号（如果为空，直接认为是不增加，直接返回true）
	 * @param date
	 * @return 是否新增成功（已经新增，直接返回true）
	 * @author SY 
	 * @version 创建时间：2016年11月24日 下午3:12:11 
	 */
	public Boolean addMajorTeacher(Long planId, Long teacherId, Date date) {
		if(date == null) {
			date = new Date();
		}
		if(planId == null) {
			return false;
		}
		if(teacherId == null) {
			return true;
		}
		Long majorId = CcVersion.dao.findById(planId).getLong("major_id");
		// 若教师不予专业相关(不属于专业下也未加入专业相关表)，则将其加入专业相关表
		if (!CcMajorTeacher.dao.isExistTeacher(teacherId, planId) && !CcTeacher.dao.isExistedTeacherUnderMajorById(teacherId, majorId)) {
			CcMajorTeacher ccMajorTeacher = new CcMajorTeacher();
			ccMajorTeacher.set("create_date", date);
			ccMajorTeacher.set("modify_date", date);
			ccMajorTeacher.set("teacher_id", teacherId);
			ccMajorTeacher.set("version_id", planId);
			ccMajorTeacher.set("is_del", Boolean.FALSE);
			if (!ccMajorTeacher.save()) {
				return false;
			}
		}
		return true;
	}
}
