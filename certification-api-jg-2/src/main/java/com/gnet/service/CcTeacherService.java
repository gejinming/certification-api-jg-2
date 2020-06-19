package com.gnet.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.Constant;
import com.gnet.model.admin.CcSchool;
import com.gnet.model.admin.CcTeacher;
import com.gnet.model.admin.Office;
import com.gnet.model.admin.User;
import com.gnet.model.admin.UserRole;
import com.gnet.plugin.configLoader.ConfigUtils;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.PasswdKit;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;

/**
 * 教师
 * 
 * @author SY
 * 
 * @date 2016年7月4日19:09:40
 */
@Component("ccTeacherService")
public class CcTeacherService {

	/**
	 * 批量导入教师
	 * 
	 * @param teacherList
	 * @param schoolId
	 * @return
	 */
	public boolean saveTeachers(List<Record> teacherList, Long schoolId) {
		/*
		 * 1. 保存user
		 * 2. 保存userRole
		 * 3. 保存teacher
		 */
		Date date = new Date();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		
		// 保存user
		List<User> userList = new ArrayList<>();
		for(Record record : teacherList) {
			User user = new User();
			user.set("id", idGenerate.getNextValue());
			user.set("create_date", date);
			user.set("modify_date", date);
			user.set("loginName", schoolId + "-" + record.getStr("code"));
			// 密码加密措施
			user.set("password", PasswdKit.entryptPassword(User.DEFAULT_PASSWORD));
			user.set("department", record.getLong("departmentId"));
			user.set("name", record.getStr("name"));
			user.set("type", User.TYPE_NORMAL);
			user.set("is_enabled", record.getStr("isEnabled").equals(User.ENABLE));
			user.set("login_failure_count", Constant.USER_DEFAULT_FAIL_COUNT);
			user.set("is_bind_email", Boolean.FALSE);
			user.set("is_bind_mobile", Boolean.FALSE);
			user.set("is_locked", Constant.USER_NOTENABLED);
			user.set("is_del", Boolean.FALSE);
			
			user.put("roleIds", ConfigUtils.getStr("global", "role.teacher_id"));
			user.put("code", record.getStr("code"));
			user.put("sex", Integer.valueOf(record.getStr("sex")));
			user.put("is_leave", record.getStr("isLeave").equals(User.LEAVE));
			user.put("majorId", record.getLong("majorId"));
			user.put("instituteId", record.getLong("instituteId"));
			userList.add(user);
			
		}
		if(!User.dao.batchSave(userList)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		// 保存userRole
		List<UserRole> userRoleList = new ArrayList<>();
		for(User user : userList) {
			Long userId = user.getLong("id");
			
			//角色关联保存
			UserRole userRole = new UserRole();
			userRole.set("user_id", userId);
			userRole.set("roles", user.get("roleIds"));
			userRoleList.add(userRole);
		}
		if(!UserRole.dao.batchSave(userRoleList)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		
		// 保存Teacher
		List<CcTeacher> ccTeacherList = Lists.newArrayList();
		for (User user : userList) {
			CcTeacher ccTeacher = new CcTeacher();
			ccTeacher.set("id", user.getLong("id"));
			ccTeacher.set("create_date", date);
			ccTeacher.set("modify_date", date);
			ccTeacher.set("code", user.getStr("code"));
			ccTeacher.set("name", user.getStr("name"));
			ccTeacher.set("sex", user.getInt("sex"));
			ccTeacher.set("school_id", schoolId);
			ccTeacher.set("highest_degrees", CcTeacher.HIGHESTDEGREES_TYPE);
			ccTeacher.set("job_title", CcTeacher.JOBTITLE_TYPE);
			ccTeacher.set("is_leave", user.getBoolean("is_leave"));
			ccTeacher.set("major_id", user.getLong("majorId"));
			ccTeacher.set("institute_id", user.getLong("instituteId"));
			ccTeacher.set("is_del", Boolean.FALSE);
			ccTeacherList.add(ccTeacher);
		}
		if(!CcTeacher.dao.batchSave(ccTeacherList)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		
		return true;
	}
	
	/**
	 * 增加教师（同时保存教师信息和用户信息）
	 * 
	 * @return
	 */
	public Boolean save(CcTeacher ccTeacher, String code, Long departentId, String name, String email, Long schoolId ){
		
		Date date = new Date();
		ccTeacher.set("create_date", date);
		ccTeacher.set("modify_date", date);
		ccTeacher.set("is_del", Boolean.FALSE);
		ccTeacher.set("is_leave", Boolean.FALSE);
		if(!ccTeacher.save()){
			return false;
		}
		
		//默认角色为教师
		Long roleId = ConfigUtils.getLong("global", "role.teacher_id");
		List<Long> roleIds = Lists.newArrayList();
		roleIds.add(roleId);
		
		//保存用户
		UserService userService = SpringContextHolder.getBean(UserService.class);
		User user = new User();
		user.set("id", ccTeacher.getLong("id"));
		if (!userService.save(user, code, "1234", String.valueOf(departentId), roleIds, name, Boolean.TRUE, email, schoolId)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		
		return true;
	}
	
	/**
	 * 更新教师（同时更新教师信息和用户信息）
	 * 
	 * @param ccTeacher
	 * @param id
	 * @param password
	 * @param departmentId
	 * @param name
	 * @param isEnabled
	 * @param email
	 * @return
	 */
	public Boolean update(CcTeacher ccTeacher, Long id, String password, String departmentId, List<Long> roleIds, String name, Boolean isEnabled, String email){
		
		Date date = new Date();
		ccTeacher.set("modify_date", date);
		if (!ccTeacher.update()) {
			return false;
		}
		
		//更新用户信息
		UserService userService = SpringContextHolder.getBean(UserService.class);
		User user = new User();
		if(!userService.update(id, user, password, departmentId, roleIds, name, isEnabled, email, null)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		return true;
	}
	
	/**
	 * 导入数据验证
	 * 1.验证非空字段是否数据都存在
	 * 2.验证教工号是否存在
	 * 3.验证部门是否存在
	 * 
	 * @param ccTeachers
	 * @param schoolId
	 * @param userDepartmentId 
	 * @return
	 */
	public Boolean validateImportList(List<Record> ccTeachers, Long schoolId, Long userDepartmentId) {
		// 验证数据需要数据结构准备
		List<Record> errorList = Lists.newArrayList();
		// 验证excel中学号是否重复数据结构准备
		List<String> teacherCodeInExcel = Lists.newArrayList();
		// 验证职工号是否在数据库中重复数据结构准备
		String[] teacherCodes = new String[ccTeachers.size()];
		Map<String, Record> teacherCodeToTeacherMap = Maps.newHashMap();
		// 验证部门是否存在验证数据结构准备
		Map<String, List<Record>> departmentMap = Maps.newHashMap();

		for (int i = 0; i < ccTeachers.size(); i ++) {
			Record teacher = ccTeachers.get(i);
			// 初始化isError为False
			teacher.set("isError", Boolean.FALSE);
			teacher.set("reasons", new ArrayList<String>());
			// 验证非空字段是否都存在
			if (StrKit.isBlank(teacher.getStr("code")) || StrKit.isBlank(teacher.getStr("name")) || StrKit.isBlank(teacher.getStr("sex")) || StrKit.isBlank(teacher.getStr("isEnabled")) || StrKit.isBlank(teacher.getStr("isLeave")) || StrKit.isBlank(teacher.getStr("departmentName"))) {
				errorList.add(teacher);
				teacher.set("isError", Boolean.TRUE);
				List<String> reasons = teacher.get("reasons");
				reasons.add("该教师缺少职工号、姓名、性别、部门编号、是否离职、是否可用必填字段的其中之一，请仔细检查确认。");
			}
			// 验证excel中学号是否重复
			if (StrKit.notBlank(teacher.getStr("code"))) {
				teacherCodes[i] = teacher.getStr("code");
				if (!teacherCodeInExcel.contains(teacherCodes[i])) {
					teacherCodeInExcel.add(teacherCodes[i]);
					// 当学号不能为空且excel中不存在时，将对象存入map
					teacherCodeToTeacherMap.put(teacher.getStr("code"), teacher);
				} else {
					errorList.add(teacher);
					teacher.set("isError", Boolean.TRUE);
					List<String> reasons = teacher.get("reasons");
					reasons.add("该教师职工号在excel中有重复，请仔细检查确认。");
				}
			}
			// 部门map
			String departmentName = teacher.getStr("departmentName");
			if (StrKit.notBlank(departmentName)) {
				if (departmentMap.get(departmentName) == null) {
					departmentMap.put(departmentName, new ArrayList<Record>());
				}
				departmentMap.get(departmentName).add(teacher);
			}
			
		}
		
		// 职工号是否在数据库中重复
		List<Record> repeatInDB = CcTeacher.dao.existedTeachers(teacherCodes, schoolId);
		for (Record record : repeatInDB) {
			Record teacher = teacherCodeToTeacherMap.get(record.getStr("code"));
			errorList.add(teacher);
			teacher.set("isError", Boolean.TRUE);
			List<String> reasons = teacher.get("reasons");
			reasons.add("该教师职工号在系统中已存在，请仔细检查确认。");
		}
		
		// 部门是否存在验证
		List<Office> offices = CcSchool.dao.findBySchool(schoolId);
		List<String> officeNameInDB = Lists.newArrayList();
		for (Office office : offices) {
			officeNameInDB.add(office.getStr("name"));
		}
		
		// 用户所在部门及以下的部门(用于验证当前操作人增加某个部门以下的教师时候，是否有权限)
		List<Office> userOffices = CcSchool.dao.findByDepartment(userDepartmentId);
		List<String> officeNameInUserDB = Lists.newArrayList();
		Map<String, Office> officeUserMap = new HashMap<>();
		for (Office office : userOffices) {
			officeNameInUserDB.add(office.getStr("name"));
			officeUserMap.put(office.getStr("name"), office);
		}
		
		for (String officeName : departmentMap.keySet()) {
			if (!officeNameInDB.contains(officeName)) {
				for (Record teacher : departmentMap.get(officeName)) {
					errorList.add(teacher);
					teacher.set("isError", Boolean.TRUE);
					List<String> reasons = teacher.get("reasons");
					reasons.add("该教师的部门不存在于系统中，请仔细检查确认！");
				}
			} else if(!officeNameInUserDB.contains(officeName)) {
				// 当前操作人增加某个部门以下的教师时候，如果没有权限
				for (Record teacher : departmentMap.get(officeName)) {
					errorList.add(teacher);
					teacher.set("isError", Boolean.TRUE);
					List<String> reasons = teacher.get("reasons");
					reasons.add("该教师的部门您没有权限操作！");
				}
			} else {
				// 如果存在部门，则获取id，添加到TeacherList中
				for (Record teacher : departmentMap.get(officeName)) {
					Office temp = officeUserMap.get(officeName);
					teacher.set("departmentId", temp.get("id"));
					// 区分是学院还是专业教师
					if(Office.TYPE_BRANCH.equals(temp.get("type"))) {
						teacher.set("instituteId", temp.get("id"));
					} else if(Office.TYPE_MAJOR.equals(temp.get("type"))) {
						teacher.set("majorId", temp.get("id"));
						teacher.set("instituteId", temp.get("parentid"));
					} else {
						// 既不是学院也不是专业， 暂时是什么都不做，以后可能有其他要去，到时候再操作
					}
				}
			}
		}
		
		return errorList.isEmpty();
	}
	
}
