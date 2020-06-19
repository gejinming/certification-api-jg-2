package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcStudentTransfer;
import com.jfinal.kit.StrKit;

/**
 * 增加专业转入、转出学生记录接口 
 * 
 * @author xzl
 * @Date 2016年7月26日
 */
@Service("EM00630")
public class EM00630 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		
		Integer grade = paramsIntegerFilter(param.get("grade"));
		Integer year = paramsIntegerFilter(param.get("year"));
		//对某个专业进行转入或者转出
		Long majorId = paramsLongFilter(param.get("majorId"));
		String studentNo = paramsStringFilter(param.get("studentNo"));
		//类型，1：转入，2：转出
		Integer type = paramsIntegerFilter(param.get("type"));
		String studentName = paramsStringFilter(param.get("studentName"));
		Integer studentSex = paramsIntegerFilter(param.get("studentSex"));
		//转入学生原专业名称
		String transferInMajorName = paramsStringFilter(param.get("transferInMajorName"));
		//转出学生目标专业名称		
		String transferOutMajorName = paramsStringFilter(param.get("transferOutMajorName"));		
		String remark = paramsStringFilter(param.get("remark"));
		
		if(majorId == null){
			return renderFAIL("0640", response, header);
		}
		if(grade == null){
		   return renderFAIL("0641", response, header);
		}
		if(year == null){
			return renderFAIL("0642", response, header);
		}
		if(StrKit.isBlank(studentNo)){
			return renderFAIL("0332", response, header);
		}
		if(type == null){
			return renderFAIL("0643", response, header);
		}
		
		if(!type.equals(CcStudentTransfer.TYPE_IN) && !type.equals(CcStudentTransfer.TYPE_OUT)){
			return renderFAIL("0644", response, header);
		}
		
		if(grade > year){
			return renderFAIL("0645", response, header);
		}
		
		if(year - grade > CcStudentTransfer.DIFFERENCE){
			return renderFAIL("0646", response, header);
		}
		//新增转入专业的时候，原专业名称不能为空, 转出专业时，目标专业名称不能为空
		if(type.equals(CcStudentTransfer.TYPE_IN)){
			if(StrKit.isBlank(transferInMajorName)){
				renderFAIL("0652", response, header);
			}
		}else{
			if(StrKit.isBlank(transferOutMajorName)){
				return renderFAIL("0653", response, header);
			}
		}
		
		if(CcStudentTransfer.dao.isRepeat(majorId, type, studentNo)){
			return renderFAIL("0647", response, header);
		}
		
		Date date = new Date();
		CcStudentTransfer studentTransfer = new CcStudentTransfer();
		studentTransfer.set("create_date", date);
		studentTransfer.set("modify_date", date);
		studentTransfer.set("grade", grade);
		studentTransfer.set("major_id", majorId);
		studentTransfer.set("year", year);
		studentTransfer.set("student_no", studentNo);
		studentTransfer.set("student_name", studentName);
		studentTransfer.set("student_sex", studentSex);
		studentTransfer.set("type", type);
		studentTransfer.set("transfer_in_major_name", transferInMajorName);
		studentTransfer.set("transfer_out_major_name", transferOutMajorName);
		studentTransfer.set("is_del", Boolean.FALSE);
		studentTransfer.set("remark", remark);
		
		Map<String, Object> result = new HashMap<>();
		// 返回操作是否成功
		result.put("isSuccess", studentTransfer.save());
		
		return renderSUC(result, response, header);
	}
	
}
