package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
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
import com.gnet.model.admin.CcMajorTeacher;
import com.gnet.model.admin.CcTeacher;
import com.gnet.service.CcTeacherService;
import com.gnet.service.OfficeService;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.kit.StrKit;

/**
 * 增加专业教师联系
 * 
 * @author sll
 * 
 * @date 2016年06月30日 19:23:23
 *
 */
@Service("EM00292")
@Transactional(readOnly=false)
public class EM00292 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Map<String, Object> result = new HashMap<>();
		//版本信息
		Long versionId = paramsLongFilter(param.get("versionId"));
		//教师信息
		String code = paramsStringFilter(param.get("code"));
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
		Boolean isLeave = paramsBooleanFilter(param.get("isLeave"));
		Long departmentId = paramsLongFilter(param.get("departmentId"));
		
		Long schoolId = null;
		Long instituteId = null;
		Long majorId = null;
		
		if(versionId == null){
			return renderFAIL("0140", response, header);
		}
		
		// 教师信息为空信息的过滤
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
		if (highestDegrees == null) {
			return renderFAIL("0167", response, header);
		}
		if (jobTitle == null) {
			return renderFAIL("0168", response, header);
		}
		if (departmentId == null) {
			return renderFAIL("0056", response, header);
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
		
		CcTeacher ccTeacher = new CcTeacher();
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
		
		//保存教师信息和用户信息
		CcTeacherService  ccTeacherService = SpringContextHolder.getBean(CcTeacherService.class);
		if (!ccTeacherService.save(ccTeacher, code, departmentId, name, email, schoolId)) {
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		//保存专业教师联系
		CcMajorTeacher ccMajorTeacher = new CcMajorTeacher();
		Date date = new Date();
		ccMajorTeacher.set("teacher_id", ccTeacher.get("id"));
		ccMajorTeacher.set("version_id", versionId);
		ccMajorTeacher.set("create_date", date);
		ccMajorTeacher.set("modify_date", date);
		ccMajorTeacher.set("is_del", Boolean.FALSE);
		if (!ccMajorTeacher.save()) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			renderSUC(result, response, header);
		}
		
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}
}
