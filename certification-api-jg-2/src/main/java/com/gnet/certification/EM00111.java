package com.gnet.certification;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcTeacher;
import com.gnet.service.UserService;
import com.gnet.utils.DictUtils;
import com.google.common.collect.Maps;

/**
 * 查看教师某条信息
 * 
 * @author SY
 * @Date 2016年6月23日14:42:54
 */
@Service("EM00111")
public class EM00111 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据：id
		Long teacherId = paramsLongFilter(params.get("id"));
		// 不能为空过滤
		if (teacherId == null) {
			return renderFAIL("0160", response, header);
		}
		// 通过id获取这条记录
		CcTeacher ccTeacher = CcTeacher.dao.findAllById(teacherId);
		if(ccTeacher == null) {
			return renderFAIL("0161", response, header);
		}
		
		// 结果返回
		Map<String, Object> result = Maps.newHashMap();
		
		result.put("id", ccTeacher.get("id"));
		result.put("createDate", ccTeacher.get("create_date"));
		result.put("modifyDate", ccTeacher.get("modify_date"));
		result.put("code", ccTeacher.get("code"));
		result.put("name", ccTeacher.get("name"));
		result.put("sex", ccTeacher.get("sex"));
		result.put("birthday", ccTeacher.get("birthday"));
		result.put("nativePlace", ccTeacher.get("native_place"));
		result.put("nation", ccTeacher.get("nation"));
		result.put("politics", ccTeacher.get("politics"));
		result.put("country", ccTeacher.get("country"));
		result.put("idCard", ccTeacher.get("id_card"));
		result.put("highestEducation", ccTeacher.get("highest_education"));
		result.put("highestDegrees", ccTeacher.get("highest_degrees"));
		result.put("bachelorSchool", ccTeacher.get("bachelor_school"));
		result.put("bachelorMajor", ccTeacher.get("bachelor_major"));
		result.put("masterSchool", ccTeacher.get("master_school"));
		result.put("masterMajor", ccTeacher.get("master_major"));
		result.put("doctorateSchool", ccTeacher.get("doctorate_school"));
		result.put("doctorateMajor", ccTeacher.get("doctorate_major"));
		result.put("comeSchoolTime", ccTeacher.get("come_school_time"));
		result.put("startEducationYear", ccTeacher.get("start_education_year"));
		result.put("jobTitle", ccTeacher.get("job_title"));
		result.put("administrative", ccTeacher.get("administrative"));
		result.put("mobilePhone", ccTeacher.get("mobile_phone"));
		result.put("mobilePhoneSec", ccTeacher.get("mobile_phone_sec"));
		result.put("officePhone", ccTeacher.get("office_phone"));
		result.put("officePhoneSec", ccTeacher.get("office_phone_sec"));
		result.put("qq", ccTeacher.get("qq"));
		result.put("wechat", ccTeacher.get("wechat"));
		result.put("email", ccTeacher.get("email"));
		result.put("officeAddress", ccTeacher.get("office_address"));
		result.put("photo", ccTeacher.get("photo"));
		result.put("isLeave", ccTeacher.get("is_leave"));
		result.put("majorId", ccTeacher.get("major_id"));
		result.put("majorName", ccTeacher.get("major_name"));
		result.put("instituteId", ccTeacher.get("institute_id"));
		result.put("instituteName", ccTeacher.get("institute_name"));
		result.put("schoolId", ccTeacher.get("school_id"));
		result.put("schoolName", ccTeacher.get("schoolName"));
		// user
		result.put("departmentId", ccTeacher.get("department"));
		result.put("departmentName", ccTeacher.get("department_name"));
		result.put("loginName", UserService.handleLoginName(ccTeacher.getStr("loginName")));
		result.put("type", ccTeacher.getInt("type"));
		result.put("roles", ccTeacher.getStr("roles"));
		result.put("typeName", DictUtils.findLabelByTypeAndKey("type", ccTeacher.getInt("type")));
		return renderSUC(result, response, header);
	}
	
}
