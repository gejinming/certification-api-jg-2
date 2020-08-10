package com.gnet.certification;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcStudent;
import com.gnet.utils.DictUtils;
import com.google.common.collect.Maps;

/**
 * 查看某一学生详情信息
 * 
 * @author wct
 * @Date 2016年6月29日
 */
@Service("EM00201")
public class EM00201 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long id = paramsLongFilter(params.get("id"));
		// 学生编号为空过滤
		if (id == null) {
			return renderFAIL("0330", response, header);
		}
		CcStudent ccStudent = CcStudent.dao.findDetailById(id);
		if (ccStudent == null) {
			return renderFAIL("0331", response, header);
		}
		
		// 返回结果
		Map<String, Object> result = Maps.newHashMap();
		result.put("id", ccStudent.getLong("id"));
		result.put("createDate", ccStudent.getDate("create_date"));
		result.put("modifyDate", ccStudent.getDate("modify_date"));
		result.put("classId", ccStudent.getLong("class_id"));
		result.put("className", ccStudent.getStr("className"));
		result.put("grade", ccStudent.getInt("grade"));
		result.put("studentNo", ccStudent.getStr("student_no"));
		result.put("name", ccStudent.getStr("name"));
		result.put("sex", ccStudent.getInt("sex"));
		result.put("sexName", DictUtils.findLabelByTypeAndKey("sex", ccStudent.getInt("sex")));
		result.put("idCard", ccStudent.getStr("id_card"));
		result.put("birthday", ccStudent.getDate("birthday"));
		result.put("address", ccStudent.getStr("address"));
		result.put("domitory", ccStudent.getStr("domitory"));
		result.put("status", ccStudent.getInt("statue"));
		result.put("statusName", DictUtils.findLabelByTypeAndKey("studentStatue", ccStudent.getInt("statue")));
		result.put("politics", ccStudent.getStr("politics"));
		result.put("nativePlace", ccStudent.getStr("native_place"));
		result.put("country", ccStudent.getStr("country"));
		result.put("nation", ccStudent.getStr("nation"));
		result.put("mobilePhone", ccStudent.getStr("mobile_phone"));
		result.put("mobilePhoneSec", ccStudent.getStr("mobile_phone_sec"));
		result.put("qq", ccStudent.getStr("qq"));
		result.put("wechat", ccStudent.getStr("wechat"));
		result.put("email", ccStudent.getStr("email"));
		result.put("photo", ccStudent.getStr("photo"));
		result.put("matriculateDate", ccStudent.getDate("matriculate_date"));
		result.put("graduateDate", ccStudent.getDate("graduate_date"));
		result.put("personal", ccStudent.getStr("personal"));
		result.put("highestEducation", ccStudent.getStr("highest_education"));
		result.put("remark", ccStudent.getStr("remark"));
		return renderSUC(result, response, header);
	}
	
}
