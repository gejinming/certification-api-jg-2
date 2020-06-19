package com.gnet.certification;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcTeacher;
import com.gnet.model.admin.User;
import com.gnet.plugin.configLoader.ConfigUtils;
import com.gnet.service.OfficeService;
import com.gnet.service.UserService;
import com.gnet.utils.DateUtil;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;


/**
 * 增加教师某条信息
 * 
 * @author SY
 * @Date 2016年6月23日20:39:28
 */
@Transactional
@Service("EM00112")
public class EM00112 extends BaseApi implements IApi {
	
	private static final Logger logger  = Logger.getLogger(EM00112.class);

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 各种其他信息
		String code = paramsStringFilter(params.get("code"));
		String name = paramsStringFilter(params.get("name"));
		String nativePlace = paramsStringFilter(params.get("nativePlace"));
		String nation = paramsStringFilter(params.get("nation"));
		String politics = paramsStringFilter(params.get("politics"));
		String country = paramsStringFilter(params.get("country"));
		String idCard = paramsStringFilter(params.get("idCard"));
		String highestEducation = paramsStringFilter(params.get("highestEducation"));
		Integer highestDegrees = paramsIntegerFilter(params.get("highestDegrees"));
		String bachelorSchool = paramsStringFilter(params.get("bachelorSchool"));
		String bachelorMajor = paramsStringFilter(params.get("bachelorMajor"));
		String masterSchool = paramsStringFilter(params.get("masterSchool"));
		String masterMajor = paramsStringFilter(params.get("masterMajor"));
		String doctorateSchool = paramsStringFilter(params.get("doctorateSchool"));
		String doctorateMajor = paramsStringFilter(params.get("doctorateMajor"));
		Integer jobTitle = paramsIntegerFilter(params.get("jobTitle"));
		String administrative = paramsStringFilter(params.get("administrative"));
		String mobilePhone = paramsStringFilter(params.get("mobilePhone"));
		String mobilePhoneSec = paramsStringFilter(params.get("mobilePhoneSec"));
		String officePhone = paramsStringFilter(params.get("officePhone"));
		String officePhoneSec = paramsStringFilter(params.get("officePhoneSec"));
		String qq = paramsStringFilter(params.get("qq"));
		String wechat = paramsStringFilter(params.get("wechat"));
		String email = paramsStringFilter(params.get("email"));
		String officeAddress = paramsStringFilter(params.get("officeAddress"));
		Integer sex = paramsIntegerFilter(params.get("sex"));
		Date birthday = paramsDateFilter(params.get("birthday"));
		Date comeSchoolTime = paramsDateFilter(params.get("comeSchoolTime"));
		String startEducationYearString = paramsStringFilter(params.get("startEducationYear"));
		Date startEducationYear = DateUtil.stringtoDate(startEducationYearString, "yyyy-MM");
		Boolean isLeave = paramsBooleanFilter(params.get("isLeave"));
		
		// user 需要的信息 userRole 需要的信息
		String loginName = code;
		String password = paramsStringFilter(params.get("password"));
		Long roleId = ConfigUtils.getLong("global", "role.teacher_id");
		if(roleId == null){
			logger.error("需要在global文件中初始化教师角色 role.teacher_id 的值");
			return renderFAIL("0169", response, header);
		}
		List<Long> roleIds = Lists.newArrayList();
		roleIds.add(roleId);
		Boolean isEnabled = paramsBooleanFilter(params.get("isEnabled"));
		Long departmentId = paramsLongFilter(params.get("departmentId"));
		
		Long schoolId = null;
		Long instituteId = null;
		Long majorId = null;

		if (departmentId == null) {
			return renderFAIL("0015", response, header);
		}
		OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
		List<Long> departmentIds = officeService.getPathByOfficeId(departmentId);
		
	    if(departmentIds == null){
	    	return renderFAIL("0057", response, header);
	    }
	    
	  //部门路径表中会保存对应部门的路径树，对应级数为 学校--学院--专业
		if (departmentIds.size() == 1) {
			schoolId = departmentIds.get(0);
		} else if (departmentIds.size() == 2) {
			schoolId = departmentIds.get(0);
			instituteId = departmentIds.get(1);
		} else if (departmentIds.size() >= 3) {
			schoolId = departmentIds.get(0);
			instituteId = departmentIds.get(1);
			majorId = departmentIds.get(2);
		}
		
		if (StrKit.isBlank(password)) {
			return renderFAIL("0011", response, header);
		}
		if (isEnabled == null) {
			return renderFAIL("0014", response, header);
		}
		if (User.dao.isExisted(schoolId + "-" + loginName)) {
			return renderFAIL("0016", response, header);
		}
		
		// 为空信息的过滤
		if (schoolId == null) {
			return renderFAIL("0084", response, header);
		}
		if (isLeave == null) {
			return renderFAIL("0162", response, header);
		}
		if (sex == null) {
			return renderFAIL("0163", response, header);
		}
		if (StrKit.isBlank(name)) {
			return renderFAIL("0164", response, header);
		}
		if (StrKit.isBlank(code)) {
			return renderFAIL("0165", response, header);
		}
		if(highestDegrees == null){
			return renderFAIL("0167", response, header);
		}
		if(jobTitle == null){
			return renderFAIL("0168", response, header);
		}
		// 结果返回
		Map<String, Object> result = Maps.newHashMap();
		
		// 保存 User and UserRole
		UserService userService = SpringContextHolder.getBean(UserService.class);
		User user = new User();
		Boolean saveResult = userService.save(user, loginName, password, String.valueOf(departmentId), roleIds, name, isEnabled, email, schoolId);
		if(!saveResult) {
			// 返回操作是否成功
			result.put("isSuccess", saveResult);
			return renderSUC(result, response, header);
		}
				
		Date date = new Date();
		CcTeacher ccTeacher = new CcTeacher();
		Long userId = user.getLong("id");
		ccTeacher.set("id", userId);
		ccTeacher.set("create_date", date);
		ccTeacher.set("modify_date", date);
		ccTeacher.set("code", code);
		ccTeacher.set("name", name);
		ccTeacher.set("country", country);
		ccTeacher.set("native_place", nativePlace);
		ccTeacher.set("nation", nation);
		ccTeacher.set("politics", politics);
		ccTeacher.set("id_card", idCard);
		ccTeacher.set("highest_education", highestEducation);
		ccTeacher.set("highest_degrees", highestDegrees);
		ccTeacher.set("bachelor_school", bachelorSchool);
		ccTeacher.set("bachelor_major", bachelorMajor);
		ccTeacher.set("master_school", masterSchool);
		ccTeacher.set("master_major", masterMajor);
		ccTeacher.set("doctorate_school", doctorateSchool);
		ccTeacher.set("doctorate_major", doctorateMajor);
		ccTeacher.set("job_title", jobTitle);
		ccTeacher.set("administrative", administrative);
		ccTeacher.set("mobile_phone", mobilePhone);
		ccTeacher.set("mobile_phone_sec", mobilePhoneSec);
		ccTeacher.set("office_phone_sec", officePhoneSec);
		ccTeacher.set("office_phone", officePhone);
		ccTeacher.set("qq", qq);
		ccTeacher.set("wechat", wechat);
		ccTeacher.set("email", email);
		ccTeacher.set("office_address", officeAddress);
		ccTeacher.set("sex", sex);
		ccTeacher.set("major_id", majorId);
		ccTeacher.set("institute_id", instituteId);
		ccTeacher.set("school_id", schoolId);
		ccTeacher.set("birthday", birthday);
		ccTeacher.set("come_school_time", comeSchoolTime);
		ccTeacher.set("start_education_year", startEducationYear);
		ccTeacher.set("is_leave", isLeave);
		ccTeacher.set("is_del", Boolean.FALSE);
		
		// 保存这个信息
		saveResult = ccTeacher.save();
		if(!saveResult) {
			// 返回操作是否成功
			result.put("isSuccess", saveResult);
			return renderSUC(result, response, header);
		}
		
		
		if(!saveResult) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}	
		// 返回操作是否成功
		result.put("isSuccess", saveResult);
		return renderSUC(result, response, header);
	}
	
}
