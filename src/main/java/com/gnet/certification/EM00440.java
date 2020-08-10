package com.gnet.certification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcGraduateEmployment;
import com.gnet.object.CcGraduateEmploymentOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看毕业生就业情况表列表
 * 
 * @author sll
 * 
 * @date 2016年07月20日 21:54:24
 * 
 */
@Service("EM00440")
@Transactional(readOnly=true)
public class EM00440 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		Integer startYear = paramsIntegerFilter(param.get("startYear"));
		if(param.containsKey("startYear") && startYear == null) {
		    return renderFAIL("1009", response, header, "startYear的参数值非法");
		}
		Integer endYear = paramsIntegerFilter(param.get("endYear"));
		if(param.containsKey("endYear") && endYear == null) {
		    return renderFAIL("1009", response, header, "endYear的参数值非法");
		}
		Long majorId = paramsLongFilter(param.get("majorId"));
		if(param.containsKey("majorId") && majorId == null) {
		    return renderFAIL("1009", response, header, "majorId的参数值非法");
		}
		
		// 进行分页
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcGraduateEmploymentOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		Map<String, Object> ccGraduateEmploymentsMap = Maps.newHashMap();
		Page<CcGraduateEmployment> ccGraduateEmploymentPage = CcGraduateEmployment.dao.page(pageable, startYear, endYear, majorId);
		List<CcGraduateEmployment> ccGraduateEmploymentList = ccGraduateEmploymentPage.getList();
		// 判断是否分页
		if(pageable.isPaging()){
			ccGraduateEmploymentsMap.put("totalRow", ccGraduateEmploymentPage.getTotalRow());
			ccGraduateEmploymentsMap.put("totalPage", ccGraduateEmploymentPage.getTotalPage());
			ccGraduateEmploymentsMap.put("pageSize", ccGraduateEmploymentPage.getPageSize());
			ccGraduateEmploymentsMap.put("pageNumber", ccGraduateEmploymentPage.getPageNumber());
		}
		
		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (CcGraduateEmployment temp : ccGraduateEmploymentList) {
			Map<String, Object> ccGraduateEmployment = new HashMap<>();
			ccGraduateEmployment.put("id", temp.get("id"));
			ccGraduateEmployment.put("createDate", temp.get("create_date"));
			ccGraduateEmployment.put("modifyDate", temp.get("modify_date"));
			ccGraduateEmployment.put("year", temp.get("year"));
			ccGraduateEmployment.put("graduateNums", temp.get("graduate_nums"));
			ccGraduateEmployment.put("graduateRatio", temp.get("graduate_ratio"));
			ccGraduateEmployment.put("getDegreeRatio", temp.get("get_degree_ratio"));
			ccGraduateEmployment.put("firsttimeEmployedRatio", temp.get("firsttime_employed_ratio"));
			ccGraduateEmployment.put("masterAndGoabroadRatio", temp.get("master_and_goabroad_ratio"));
			ccGraduateEmployment.put("nationAndInstitutionRatio", temp.get("nation_and_institution_ratio"));
			ccGraduateEmployment.put("otherEnterpriseRatio", temp.get("other_enterprise_ratio"));
			ccGraduateEmployment.put("majorId", temp.get("major_id"));
			ccGraduateEmployment.put("majorName", temp.get("major_name"));
			ccGraduateEmployment.put("remark", temp.get("remark"));
			list.add(ccGraduateEmployment);
		}
		
		ccGraduateEmploymentsMap.put("list", list);
		
		return renderSUC(ccGraduateEmploymentsMap, response, header);
	}
}
