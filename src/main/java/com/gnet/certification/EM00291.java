package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcTeacher;

/**
 * 查看专业教师联系详情
 * 
 * @author sll
 * 
 * @date 2016年06月30日 19:23:23
 *
 */
@Service("EM00291")
@Transactional(readOnly=true)
public class EM00291 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		
		if (id == null) {
			return renderFAIL("0340", response, header);
		}
		
		CcTeacher temp = CcTeacher.dao.findFilteredById(id);
		if(temp == null) {
			return renderFAIL("0341", response, header);
		}
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("id", temp.get("id"));
		map.put("createDate", temp.get("create_date"));
		map.put("modifyDate", temp.get("modify_date"));
		map.put("code", temp.get("code"));
		map.put("name", temp.get("name"));
		map.put("sex", temp.get("sex"));
		map.put("birthday", temp.get("birthday"));
		map.put("nativePlace", temp.get("native_place"));
		map.put("nation", temp.get("nation"));
		map.put("politics", temp.get("politics"));
		map.put("country", temp.get("country"));
		map.put("idCard", temp.get("id_card"));
		map.put("highestEducation", temp.get("highest_education"));
		map.put("highestEegrees", temp.get("highest_degrees"));
		map.put("bachelorSchool", temp.get("bachelor_school"));
		map.put("bachelorMajor", temp.get("bachelor_major"));
		map.put("masterSchool", temp.get("master_school"));
		map.put("masterMajor", temp.get("master_major"));
		map.put("doctorateSchool", temp.get("doctorate_school"));
		map.put("doctorateMajor", temp.get("doctorate_major"));
		map.put("comeSchoolTime", temp.get("come_school_time"));
		map.put("startEducationYear", temp.get("start_education_year"));
		map.put("jobTitle", temp.get("job_title"));
		map.put("administrative", temp.get("administrative"));
		map.put("mobilePhone", temp.get("mobile_phone"));
		map.put("mobilePhoneSec", temp.get("mobile_phone_sec"));
		map.put("officePhone", temp.get("office_phone"));
		map.put("officePhoneSec", temp.get("office_phone_sec"));
		map.put("qq", temp.get("qq"));
		map.put("wechat", temp.get("wechat"));
		map.put("email", temp.get("email"));
		map.put("officeAddress", temp.get("office_address"));
		map.put("photo", temp.get("photo"));
		map.put("isLeave", temp.get("is_leave"));
		map.put("majorId", temp.get("major_id"));
		map.put("instituteId", temp.get("institute_id"));
		map.put("schoolId", temp.get("school_id"));
		
		return renderSUC(map, response, header);
	}

}
