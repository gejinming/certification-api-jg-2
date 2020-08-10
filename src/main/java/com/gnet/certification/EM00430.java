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
import com.gnet.model.admin.CcEnrollment;
import com.gnet.object.CcEnrollmentOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看招生情况表列表
 * 
 * @author sll
 * 
 * @date 2016年07月19日 09:41:29
 * 
 */
@Service("EM00430")
@Transactional(readOnly=true)
public class EM00430 extends BaseApi implements IApi {
	
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
		String majorName = paramsStringFilter(param.get("majorName"));
		Long majorId = paramsLongFilter(param.get("majorId"));
		if(param.containsKey("majorId") && majorId == null) {
		    return renderFAIL("1009", response, header, "majorId的参数值非法");
		}
		
		// 进行分页
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcEnrollmentOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		Map<String, Object> ccEnrollmentsMap = Maps.newHashMap();
		Page<CcEnrollment> ccEnrollmentPage = CcEnrollment.dao.page(pageable, startYear, endYear, majorName, majorId);
		List<CcEnrollment> ccEnrollmentList = ccEnrollmentPage.getList();
		// 判断是否分页
		if(pageable.isPaging()){
			ccEnrollmentsMap.put("totalRow", ccEnrollmentPage.getTotalRow());
			ccEnrollmentsMap.put("totalPage", ccEnrollmentPage.getTotalPage());
			ccEnrollmentsMap.put("pageSize", ccEnrollmentPage.getPageSize());
			ccEnrollmentsMap.put("pageNumber", ccEnrollmentPage.getPageNumber());
		}

		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (CcEnrollment temp : ccEnrollmentList) {
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
			
			list.add(ccEnrollment);
		}
		
		ccEnrollmentsMap.put("list", list);
		
		return renderSUC(ccEnrollmentsMap, response, header);
	}
}
