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
import com.gnet.model.admin.CcStudentTransfer;
import com.gnet.utils.DictUtils;
/**
 * 查看某个学生转入专业或转出专业的记录信息接口 
 * 
 * @author xzl
 * 
 * @date 2016年7月26日
 *
 */
@Service("EM00631")
@Transactional(readOnly=true)
public class EM00631 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		
		if (id == null) {
			return renderFAIL("0648", response, header);
		}
		
		CcStudentTransfer studentTransfer = CcStudentTransfer.dao.findFilteredById(id);
		if(studentTransfer == null){
			return renderFAIL("0649", response, header);
		}
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("id", studentTransfer.getLong("id"));
		map.put("createDate", studentTransfer.getDate("create_date"));
		map.put("modifyDate", studentTransfer.getDate("modify_date"));
		map.put("grade", studentTransfer.getInt("grade"));
		map.put("majorId", studentTransfer.getLong("major_id"));
		map.put("year", studentTransfer.getInt("year"));
		map.put("studentNo", studentTransfer.getStr("student_no"));
		map.put("studentName", studentTransfer.getStr("student_name"));
		map.put("studentSex", studentTransfer.getInt("student_sex"));
		map.put("studentSexName", DictUtils.findLabelByTypeAndKey("sex",  studentTransfer.getInt("student_sex")));
		map.put("type", studentTransfer.getInt("type"));
		map.put("transferInMajorName", studentTransfer.getStr("transfer_in_major_name"));
		map.put("transferOutMajorName", studentTransfer.getStr("transfer_out_major_name"));
		map.put("remark", studentTransfer.getStr("studentTransfer"));
		return renderSUC(map, response, header);
	}

}
