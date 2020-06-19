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
 * 增加招生情况表
 * 
 * @author sll
 * 
 * @date 2016年07月19日 09:41:29
 *
 */
@Service("EM00432")
@Transactional(readOnly=false)
public class EM00432 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Integer year = paramsIntegerFilter(param.get("year"));
		Integer enrolNum = paramsIntegerFilter(param.get("enrolNum"));
		Integer provinceDivision = paramsIntegerFilter(param.get("provinceDivision"));
		BigDecimal majorDivision = paramsBigDecimalFilter(param.get("majorDivision"));
		Integer lowestLine = paramsIntegerFilter(param.get("lowestLine"));
		BigDecimal firstVoluntaryEnrollmentRatio = paramsBigDecimalFilter(param.get("firstVoluntaryEnrollmentRatio"));
		Long majorId = paramsLongFilter(param.get("majorId"));
		String remark = paramsStringFilter(param.get("remark"));
		
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
		
		//年份的唯一性验证
		if (CcEnrollment.dao.isExists(year, majorId)) {
			return renderFAIL("0589", response, header);
		}
		
		Date date = new Date();
		CcEnrollment ccEnrollment = new CcEnrollment();
		ccEnrollment.set("create_date", date);
		ccEnrollment.set("modify_date", date);
		ccEnrollment.set("year", year);
		ccEnrollment.set("enrol_num", enrolNum);
		ccEnrollment.set("province_division", provinceDivision);
		ccEnrollment.set("major_division", majorDivision);
		ccEnrollment.set("lowest_line", lowestLine);
		ccEnrollment.set("first_voluntary_enrollment_ratio", firstVoluntaryEnrollmentRatio);
		ccEnrollment.set("major_id", majorId);
		ccEnrollment.set("is_del", Boolean.FALSE);
		ccEnrollment.set("remark", remark);
		
		Map<String, Object> result = new HashMap<>();
		result.put("isSuccess", ccEnrollment.save());
		result.put("id", ccEnrollment.getLong("id"));
		
		return renderSUC(result, response, header);
	}
}
