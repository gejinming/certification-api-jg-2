package com.gnet.certification;

import java.math.BigDecimal;
import java.util.Date;
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
 * 编辑招生情况表
 * 
 * @author sll
 * 
 * @date 2016年07月19日 09:41:29
 *
 */
@Service("EM00433")
@Transactional(readOnly=false)
public class EM00433 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		Integer year = paramsIntegerFilter(param.get("year"));
		Integer enrolNum = paramsIntegerFilter(param.get("enrolNum"));
		Integer provinceDivision = paramsIntegerFilter(param.get("provinceDivision"));
		BigDecimal majorDivision = paramsBigDecimalFilter(param.get("majorDivision"));
		Integer lowestLine = paramsIntegerFilter(param.get("lowestLine"));
		BigDecimal firstVoluntaryEnrollmentRatio = paramsBigDecimalFilter(param.get("firstVoluntaryEnrollmentRatio"));
		Long majorId = paramsLongFilter(param.get("majorId"));
		String remark = paramsStringFilter(param.get("remark"));
		
		if (id == null) {
			return renderFAIL("0580", response, header);
		}
		if (year == null) {
			return renderFAIL("0582", response, header);
		}
		if (enrolNum == null) {
			return renderFAIL("0583", response, header);
		}
		if (provinceDivision == null) {
			return renderFAIL("0584", response, header);
		}
		if (majorDivision == null) {
			return renderFAIL("0585", response, header);
		}
		if (lowestLine == null) {
			return renderFAIL("0586", response, header);
		}
		if (firstVoluntaryEnrollmentRatio == null) {
			return renderFAIL("0587", response, header);
		}
		if (majorId == null) {
			return renderFAIL("0588", response, header);
		}
				
		Date date = new Date();
		CcEnrollment ccEnrollment = CcEnrollment.dao.findFilteredById(id);
		if(ccEnrollment == null) {
			return renderFAIL("0580", response, header);
		}
		
		//年份唯一性验证
		if (CcEnrollment.dao.isExists(year, ccEnrollment.getInt("year"), majorId)) {
			return renderFAIL("0589", response, header);
		}
		
		ccEnrollment.set("modify_date", date);
		ccEnrollment.set("year", year);
		ccEnrollment.set("enrol_num", enrolNum);
		ccEnrollment.set("province_division", provinceDivision);
		ccEnrollment.set("major_division", majorDivision);
		ccEnrollment.set("lowest_line", lowestLine);
		ccEnrollment.set("first_voluntary_enrollment_ratio", firstVoluntaryEnrollmentRatio);
		ccEnrollment.set("major_id", majorId);
		ccEnrollment.set("remark", remark);

		Map<String, Boolean> result = new HashMap<>();
		result.put("isSuccess", ccEnrollment.update());
		return renderSUC(result, response, header);
	}
	
}
