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
import com.gnet.model.admin.CcPlanTerm;
import com.gnet.object.CcPlanTermOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看培养计划学年学期列表
 * 
 * @author sll
 * 
 * @date 2016年07月04日 08:30:41
 * 
 */
@Service("EM00340")
@Transactional(readOnly=true)
public class EM00340 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		String yearName = paramsStringFilter(param.get("yearName"));
		String termName = paramsStringFilter(param.get("termName"));
		Long planId = paramsLongFilter(param.get("planId"));
		if(param.containsKey("planId") && planId == null){
			return renderFAIL("1009", response, header, "planId的参数值非法");
		}
		
		// 进行分页
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcPlanTermOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		Map<String, Object> ccPlanTermsMap = Maps.newHashMap();
		Page<CcPlanTerm> ccPlanTermPage = CcPlanTerm.dao.page(pageable, yearName, termName, planId);
		List<CcPlanTerm> ccPlanTermList = ccPlanTermPage.getList();
		// 判断是否分页
		if(pageable.isPaging()){	
			ccPlanTermsMap.put("totalRow", ccPlanTermPage.getTotalRow());
			ccPlanTermsMap.put("totalPage", ccPlanTermPage.getTotalPage());
			ccPlanTermsMap.put("pageSize", ccPlanTermPage.getPageSize());
			ccPlanTermsMap.put("pageNumber", ccPlanTermPage.getPageNumber());		
	    }
		
		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (CcPlanTerm temp : ccPlanTermList) {
			Map<String, Object> ccPlanTerm = new HashMap<>();
			ccPlanTerm.put("id", temp.get("id"));
			ccPlanTerm.put("createDate", temp.get("create_date"));
			ccPlanTerm.put("modifyDate", temp.get("modify_date"));
			ccPlanTerm.put("yearName", temp.get("year_name"));
			ccPlanTerm.put("year", temp.get("year"));
			ccPlanTerm.put("termName", temp.get("term_name"));
			ccPlanTerm.put("term", temp.get("term"));
			ccPlanTerm.put("termType", temp.get("term_type"));
			ccPlanTerm.put("termTypeName", DictUtils.findLabelByTypeAndKey("termType", temp.getInt("term_type")));
			ccPlanTerm.put("planId", temp.get("plan_id"));
			ccPlanTerm.put("weekNums", temp.get("week_nums"));
			ccPlanTerm.put("isDel", temp.get("is_del"));
			ccPlanTerm.put("remark", temp.get("remark"));
			list.add(ccPlanTerm);
		}
		
		ccPlanTermsMap.put("list", list);
		
		return renderSUC(ccPlanTermsMap, response, header);
	}
}
