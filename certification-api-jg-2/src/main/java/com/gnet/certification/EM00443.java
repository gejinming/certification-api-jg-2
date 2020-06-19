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
import com.gnet.model.admin.CcGraduateEmployment;

/**
 * 编辑毕业生就业情况表
 * 
 * @author sll
 * 
 * @date 2016年07月20日 21:54:24
 *
 */
@Service("EM00443")
@Transactional(readOnly=false)
public class EM00443 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		Integer year = paramsIntegerFilter(param.get("year"));
		Integer graduateNums = paramsIntegerFilter(param.get("graduateNums"));
		BigDecimal graduateRatio = paramsBigDecimalFilter(param.get("graduateRatio"));
		BigDecimal getDegreeRatio = paramsBigDecimalFilter(param.get("getDegreeRatio"));
		BigDecimal firsttimeEmployedRatio = paramsBigDecimalFilter(param.get("firsttimeEmployedRatio"));
		BigDecimal masterAndGoabroadRatio = paramsBigDecimalFilter(param.get("masterAndGoabroadRatio"));
		BigDecimal nationAndInstitutionRatio = paramsBigDecimalFilter(param.get("nationAndInstitutionRatio"));
		BigDecimal otherEnterpriseRatio = paramsBigDecimalFilter(param.get("otherEnterpriseRatio"));
		Long majorId = paramsLongFilter(param.get("majorId"));
		String remark = paramsStringFilter(param.get("remark"));
		
		if (id == null) {
			return renderFAIL("0600", response, header);
		}
		if (year == null) {
			return renderFAIL("0602", response, header);
		}
		if (majorId == null) {
			return renderFAIL("0610", response, header);
		}
		
		Date date = new Date();
		CcGraduateEmployment ccGraduateEmployment = CcGraduateEmployment.dao.findFilteredById(id);
		
		if(ccGraduateEmployment == null) {
			return renderFAIL("0601", response, header);
		}
		
		//检验年份的唯一性
		if (ccGraduateEmployment.isExists(year, ccGraduateEmployment.getInt("year"), majorId)) {
			return renderFAIL("0603", response, header);
		}
		
		ccGraduateEmployment.set("modify_date", date);
		ccGraduateEmployment.set("year", year);
		ccGraduateEmployment.set("graduate_nums", graduateNums);
		ccGraduateEmployment.set("graduate_ratio", graduateRatio);
		ccGraduateEmployment.set("get_degree_ratio", getDegreeRatio);
		ccGraduateEmployment.set("firsttime_employed_ratio", firsttimeEmployedRatio);
		ccGraduateEmployment.set("master_and_goabroad_ratio", masterAndGoabroadRatio);
		ccGraduateEmployment.set("nation_and_institution_ratio", nationAndInstitutionRatio);
		ccGraduateEmployment.set("other_enterprise_ratio", otherEnterpriseRatio);
		ccGraduateEmployment.set("major_id", majorId);
		ccGraduateEmployment.set("remark", remark);
				
		Map<String, Boolean> result = new HashMap<>();
		result.put("isSuccess", ccGraduateEmployment.update());
		return renderSUC(result, response, header);
	}
	
}
