package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.gnet.api.kit.UserCacheKit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcClass;
import com.gnet.model.admin.CcStudent;
import com.jfinal.kit.StrKit;

/**
 * 增加学生信息接口
 * 
 * @author wct
 * 
 * @date 2016年06月29日 20:36:26
 *
 */
@Service("EM00202")
@Transactional(readOnly=false)
public class EM00202 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		String studentNo = paramsStringFilter(param.get("studentNo"));
		String name = paramsStringFilter(param.get("name"));
		Integer sex = paramsIntegerFilter(param.get("sex"));
		String idCard = paramsStringFilter(param.get("idCard"));
		Date birthday = paramsDateFilter(param.get("birthday"));
		String address = paramsStringFilter(param.get("address"));
		String domitory = paramsStringFilter(param.get("domitory"));
		Integer status = paramsIntegerFilter(param.get("status"));
		String politics = paramsStringFilter(param.get("politics"));
		String nativePlace = paramsStringFilter(param.get("nativePlace"));
		String country = paramsStringFilter(param.get("country"));
		String nation = paramsStringFilter(param.get("nation"));
		String mobilePhone = paramsStringFilter(param.get("mobilePhone"));
		String mobilePhoneSec = paramsStringFilter(param.get("mobilePhoneSec"));
		String qq = paramsStringFilter(param.get("qq"));
		String wechat = paramsStringFilter(param.get("wechat"));
		String email = paramsStringFilter(param.get("email"));
		String photo = paramsStringFilter(param.get("photo"));
		Date matriculateDate = paramsDateFilter(param.get("matriculateDate"));
		Date graduateDate = paramsDateFilter(param.get("graduateDate"));
		Integer grade = paramsIntegerFilter(param.get("grade"));
		Long classId = paramsLongFilter(param.get("classId"));
		String personal = paramsStringFilter(param.get("personal"));
		String highestEducation = paramsStringFilter(param.get("highestEducation"));
		String remark = paramsStringFilter(param.get("remark"));
		Long schoolId = UserCacheKit.getSchoolId(request.getHeader().getToken());
		// 学生学号不为空过滤
		if (StrKit.isBlank(studentNo)) {
			return renderFAIL("0332", response, header);
		}
		// 学生姓名不为空过滤
		if (StrKit.isBlank(name)) {
			return renderFAIL("0333", response, header);
		}
		// 学生性别不为空过滤
		if (sex == null) {
			return renderFAIL("0334", response, header);
		}
		// 学生入学日期不能为空
		if (matriculateDate == null) {
			return renderFAIL("0335", response, header);
		}
		//年级不能为空过滤
		if (grade == null) {
			return renderFAIL("0337", response, header);
		}
		if (classId == null) {
			return renderFAIL("0338", response, header);
		}
		
		CcClass ccClass = CcClass.dao.findFilteredById(classId);
		if(ccClass == null){
			return renderFAIL("0295", response, header);
		}
		if(!grade.equals(ccClass.getInt("grade"))){
			return renderFAIL("0303", response, header);
		}

		if(CcStudent.dao.isExisted(studentNo, null, schoolId)){
			return renderFAIL("0339", response, header);
		}
		
		Date date = new Date();
		
		CcStudent ccStudent = new CcStudent();
		
		ccStudent.set("create_date", date);
		ccStudent.set("modify_date", date);
		ccStudent.set("student_no", studentNo);
		ccStudent.set("name", name);
		ccStudent.set("sex", sex);
		ccStudent.set("id_card", idCard);
		ccStudent.set("birthday", birthday);
		ccStudent.set("address", address);
		ccStudent.set("domitory", domitory);
		ccStudent.set("statue", status);
		ccStudent.set("politics", politics);
		ccStudent.set("native_place", nativePlace);
		ccStudent.set("country", country);
		ccStudent.set("nation", nation);
		ccStudent.set("mobile_phone", mobilePhone);
		ccStudent.set("mobile_phone_sec", mobilePhoneSec);
		ccStudent.set("qq", qq);
		ccStudent.set("wechat", wechat);
		ccStudent.set("email", email);
		ccStudent.set("photo", photo);
		ccStudent.set("matriculate_date", matriculateDate);
		ccStudent.set("graduate_date", graduateDate);
		ccStudent.set("grade", grade);
		ccStudent.set("class_id", classId);
		ccStudent.set("personal", personal);
		ccStudent.set("highest_education", highestEducation);
		ccStudent.set("is_del", Boolean.FALSE);
		ccStudent.set("remark", remark);
		
		Map<String, Object> result = new HashMap<>();
		result.put("isSuccess", ccStudent.save());
		
		return renderSUC(result, response, header);
	}
	
}
