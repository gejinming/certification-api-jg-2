package com.gnet.certification;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcTeacher;
import com.gnet.model.admin.User;
import com.gnet.model.admin.UserRole;
import com.gnet.service.UserService;
import com.gnet.utils.DateUtil;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;

/**
 * 修改教师某条信息
 * 
 * @author SY
 * @Date 2016年6月23日21:58:18
 */
@Service("EM00113")
public class EM00113 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据
		Long teacherId = paramsLongFilter(params.get("id"));
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
		Long majorId = paramsLongFilter(params.get("majorId"));
		Long instituteId = paramsLongFilter(params.get("instituteId"));
		Date birthday = paramsDateFilter(params.get("birthday"));
		Date comeSchoolTime = paramsDateFilter(params.get("comeSchoolTime"));
		String startEducationYearString = paramsStringFilter(params.get("startEducationYear"));
		Date startEducationYear = DateUtil.stringtoDate(startEducationYearString, "yyyy-MM");
		Boolean isLeave = paramsBooleanFilter(params.get("isLeave"));
		
		String token = request.getHeader().getToken();
		Long schoolId = UserCacheKit.getSchoolId(token);
		if(schoolId == null){
			return renderFAIL("0084", response, header);
		}
		// 不能为空过滤
		if (teacherId == null) {
			return renderFAIL("0160", response, header);
		}
		// 保存这个信息
		CcTeacher ccTeacher = CcTeacher.dao.findFilteredById(teacherId);
		if(ccTeacher == null){
			return renderFAIL("0161", response, header);
		}
		
		// user 需要的信息 userRole 需要的信息
		List<Long> roleIds = Lists.newArrayList();
		UserRole userRole = UserRole.dao.findFirstByColumn("user_id", teacherId);
		if(userRole == null){
			return renderFAIL("0012", response, header);
		}
		String[] roleArray = userRole.getStr("roles").split(",");
		for(int i=0; i<roleArray.length; i++){
			roleIds.add(Long.valueOf(roleArray[i]));
		}
		
		String departmentId = paramsStringFilter(params.get("departmentId"));
		
		if (StrKit.isBlank(departmentId)) {
			return renderFAIL("0015", response, header);
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
		// 结果返回
		Map<String, Object> result = Maps.newHashMap();
		// 返回操作是否成功
		result.put("isSuccess", false);
				
		// 保存 User and UserRole
		UserService userService = SpringContextHolder.getBean(UserService.class);
		User user = new User();
		Boolean updateResult = userService.update(teacherId, user, null, departmentId, roleIds, name, null, email, null);
		if(!updateResult) {
			return renderSUC(result, response, header);
		}
		Date date = new Date();
		ccTeacher.set("modify_date", date);
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
		updateResult = ccTeacher.update();
		if(!updateResult) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			// 返回操作是否成功
			return renderSUC(result, response, header);
		}
		// 返回操作是否成功
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}
	
}
