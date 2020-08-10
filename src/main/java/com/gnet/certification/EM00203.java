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
 * 编辑学生信息
 * 
 * @author wct
 * 
 * @date 2016年06月29日 21:34:41
 *
 */
@Service("EM00203")
@Transactional(readOnly=false)
public class EM00203 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		String studentNo = paramsStringFilter(param.get("studentNo"));
		String name = paramsStringFilter(param.get("name"));
		Integer grade = paramsIntegerFilter(param.get("grade"));
		Integer sex = paramsIntegerFilter(param.get("sex"));
		String idCard = paramsStringFilter(param.get("idCard"));
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
		Long classId = paramsLongFilter(param.get("classId"));
		String personal = paramsStringFilter(param.get("personal"));
		String highestEducation = paramsStringFilter(param.get("highestEducation"));
		String remark = paramsStringFilter(param.get("remark"));
		Date birthday = paramsDateFilter(param.get("birthday"));
		Date matriculateDate = paramsDateFilter(param.get("matriculateDate"));
		Date graduateDate = paramsDateFilter(param.get("graduateDate"));
		Long schoolId = UserCacheKit.getSchoolId(request.getHeader().getToken());
		// 学生编号不能为空过滤
		if (id == null) {
			return renderFAIL("0330", response, header);
		}
		// 学生学号不能为空过滤
		if (StrKit.isBlank(studentNo)) {
			return renderFAIL("0332", response, header);
		}
		// 学生姓名不能为空过滤
		if (StrKit.isBlank(name)) {
			return renderFAIL("0333", response, header);
		}
		// 年级不能为空过滤
		if (grade == null) {
			return renderFAIL("0337", response, header);
		}
		// 学生性别不能为空过滤
		if (sex == null) {
			return renderFAIL("0334", response, header);
		}
		// 入学日期不能为空过滤
		if (matriculateDate == null) {
			return renderFAIL("0335", response, header);
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
		
		Date date = new Date();
		CcStudent ccStudent = CcStudent.dao.findFilteredById(id);
		// 学生不存在
		if(ccStudent == null) {
			return renderFAIL("0331", response, header);
		}
		if(CcStudent.dao.isExisted(studentNo, ccStudent.getStr("student_no"), schoolId)){
			return renderFAIL("0339", response, header);
		}
		ccStudent.set("modify_date", date);
		ccStudent.set("student_no", studentNo);
		ccStudent.set("name", name);
		ccStudent.set("grade", grade);
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
		ccStudent.set("class_id", classId);
		ccStudent.set("personal", personal);
		ccStudent.set("highest_education", highestEducation);
		ccStudent.set("remark", remark);
		
		Map<String, Object> result = new HashMap<>();
		result.put("isSuccess", ccStudent.update());
		return renderSUC(result, response, header);
	}
	
}
