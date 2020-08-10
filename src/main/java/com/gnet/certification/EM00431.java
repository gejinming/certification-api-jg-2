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
import com.gnet.model.admin.CcEnrollment;

/**
 * 查看招生情况表详情
 * 
 * @author sll
 * 
 * @date 2016年07月19日 09:41:29
 *
 */
@Service("EM00431")
@Transactional(readOnly=true)
public class EM00431 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		
		if (id == null) {
			return renderFAIL("0580", response, header);
		}
		
		CcEnrollment temp = CcEnrollment.dao.findFilteredById(id);
		if(temp == null) {
			return renderFAIL("0581", response, header);
		}
		
		Map<String, Object> ccEnrollment = new HashMap<>();
		ccEnrollment.put("id", temp.get("id"));
		ccEnrollment.put("creteDate", temp.get("create_date"));
		ccEnrollment.put("modifyDate", temp.get("modify_date"));
		ccEnrollment.put("year", temp.get("year"));
		ccEnrollment.put("enrolNum", temp.get("enrol_num"));
		ccEnrollment.put("provinceDivision", temp.get("province_division"));
		ccEnrollment.put("majorDivision", temp.get("major_division"));
		ccEnrollment.put("lowestLine", temp.get("lowest_line"));
		ccEnrollment.put("firstVoluntaryEnrollmentRatio", temp.get("first_voluntary_enrollment_ratio"));
		ccEnrollment.put("majorId", temp.get("major_id"));
		ccEnrollment.put("majorName", temp.get("major_name"));
		ccEnrollment.put("remark", temp.get("remark"));
		
		return renderSUC(ccEnrollment, response, header);
	}

}
