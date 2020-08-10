package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcTeacher;
import com.gnet.service.CcTeacherService;
import com.gnet.service.OfficeService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;

/**
 * 编辑专业教师联系
 * 
 * @author sll
 * 
 * @date 2016年06月30日 19:23:23
 *
 */
@Service("EM00293")
@Transactional(readOnly=false)
public class EM00293 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		String name = paramsStringFilter(param.get("name"));
		Integer sex = paramsIntegerFilter(param.get("sex"));
		Date birthday = paramsDateFilter(param.get("birthday"));
		String nativePlace = paramsStringFilter(param.get("nativePlace"));
		String nation = paramsStringFilter(param.get("nation"));
		String politics = paramsStringFilter(param.get("politics"));
		String country = paramsStringFilter(param.get("country"));
		String idCard = paramsStringFilter(param.get("idCard"));
		String highestEducation = paramsStringFilter(param.get("highestEducation"));
		Integer highestDegrees = paramsIntegerFilter(param.get("highestDegrees"));
		String bachelorSchool = paramsStringFilter(param.get("bachelorSchool"));
		String bachelorMajor = paramsStringFilter(param.get("bachelorMajor"));
		String masterSchool = paramsStringFilter(param.get("masterSchool"));
		String masterMajor = paramsStringFilter(param.get("masterMajor"));
		String doctorateSchool = paramsStringFilter(param.get("doctorateSchool"));
		String doctorateMajor = paramsStringFilter(param.get("doctorateMajor"));
		Date comeSchoolTime = paramsDateFilter(param.get("comeSchoolTime"));
		Date startEducationYear = paramsDateFilter(param.get("startEducationYear"));
		Integer jobTitle = paramsIntegerFilter(param.get("jobTitle"));
		String administrative = paramsStringFilter(param.get("administrative"));
		String mobilePhone = paramsStringFilter(param.get("mobilePhone"));
		String mobilePhoneSec = paramsStringFilter(param.get("mobilePhoneSec"));
		String officePhone = paramsStringFilter(param.get("officePhone"));
		String officePhoneSec = paramsStringFilter(param.get("officePhoneSec"));
		String qq = paramsStringFilter(param.get("qq"));
		String wechat = paramsStringFilter(param.get("wechat"));
		String email = paramsStringFilter(param.get("email"));
		String officeAddress = paramsStringFilter(param.get("officeAddress"));
		Boolean isEnabled = paramsBooleanFilter(param.get("isEnabled"));
		Boolean isLeave = paramsBooleanFilter(param.get("isLeave"));
		String password = paramsStringFilter(param.get("password"));
		Long departmentId = paramsLongFilter(param.get("departmentId"));
		JSONArray roleIds = paramsJSONArrayFilter(param.get("roleIds"));
		
		Long schoolId = null;
		Long instituteId = null;
		Long majorId = null;
		
		// 教师信息为空信息的过滤
		if (sex == null) {
			return renderFAIL("0163", response, header);
		}
		if (StrKit.isBlank(name)) {
			return renderFAIL("0164", response, header);
		}
		if (highestDegrees == null) {
			return renderFAIL("0167", response, header);
		}
		if (jobTitle == null) {
			return renderFAIL("0168", response, header);
		}
		if (departmentId == null) {
			return renderFAIL("0056", response, header);
		}
		if (roleIds == null) {
			return renderFAIL("0020", response, header);
		}
		
		List<Long> roleIdsList = Lists.newArrayList();
		for (int i = 0; i < roleIds.size(); i++) {
			roleIdsList.add(roleIds.getLong(i));
		}
		
		OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
		List<Long> departmentIds = officeService.getPathByOfficeId(departmentId);
		
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
		
		CcTeacher ccTeacher = CcTeacher.dao.findFilteredById(id);
		if(ccTeacher == null) {
			return renderFAIL("0341", response, header);
		}
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
		ccTeacher.set("is_leave", isLeave);
		ccTeacher.set("major_id", majorId);
		ccTeacher.set("institute_id", instituteId);
		ccTeacher.set("school_id", schoolId);
		ccTeacher.set("birthday", birthday);
		ccTeacher.set("come_school_time", comeSchoolTime);
		ccTeacher.set("start_education_year", startEducationYear);
		
		//更新教师
		CcTeacherService ccTeacherService = SpringContextHolder.getBean(CcTeacherService.class);
		
		Map<String, Boolean> result = new HashMap<>();
		result.put("isSuccess", ccTeacherService.update(ccTeacher, id, password, String.valueOf(departmentId), roleIdsList, name, isEnabled, email));
		return renderSUC(result, response, header);
	}
	
}
