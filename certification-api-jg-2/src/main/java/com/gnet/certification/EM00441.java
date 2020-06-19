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
import com.gnet.model.admin.CcGraduateEmployment;

/**
 * 查看毕业生就业情况表详情
 * 
 * @author sll
 * 
 * @date 2016年07月20日 21:54:24
 *
 */
@Service("EM00441")
@Transactional(readOnly=true)
public class EM00441 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		
		if (id == null) {
			return renderFAIL("0600", response, header);
		}
		
		CcGraduateEmployment temp = CcGraduateEmployment.dao.findFilteredById(id);
		if(temp == null) {
			return renderFAIL("0601", response, header);
		}
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("id", temp.get("id"));
		map.put("createDate", temp.get("create_date"));
		map.put("modifyDate", temp.get("modify_date"));
		map.put("year", temp.get("year"));
		map.put("graduateNums", temp.get("graduate_nums"));
		map.put("graduateRatio", temp.get("graduate_ratio"));
		map.put("getDegreeRatio", temp.get("get_degree_ratio"));
		map.put("firsttimeEmployedRatio", temp.get("firsttime_employed_ratio"));
		map.put("masterAndGoabroadRatio", temp.get("master_and_goabroad_ratio"));
		map.put("nationAndInstitutionRatio", temp.get("nation_and_institution_ratio"));
		map.put("otherEnterpriseRatio", temp.get("other_enterprise_ratio"));
		map.put("majorId", temp.get("major_id"));
		map.put("majorName", temp.get("major_name"));
		map.put("remark", temp.get("remark"));
		
		return renderSUC(map, response, header);
	}

}
