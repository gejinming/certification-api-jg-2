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
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcTerm;
import com.gnet.object.CcTermOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看学期表列表
 * 
 * @author sll
 * 
 * @date 2016年07月03日 17:31:09
 * 
 */
@Service("EM00350")
@Transactional(readOnly=true)
public class EM00350 extends BaseApi implements IApi {
	
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
		Integer term = paramsIntegerFilter(param.get("term"));
		if(param.containsKey("term") && term == null) {
			return renderFAIL("1009", response, header, "term的参数值非法");
		}
		String token = request.getHeader().getToken();
		Long schoolId = UserCacheKit.getSchoolId(token);
		
		// 进行分页
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcTermOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		
		Map<String, Object> ccTermsMap = Maps.newHashMap();
		Page<CcTerm> ccTermPage = CcTerm.dao.page(pageable, startYear, endYear, term, schoolId);
		List<CcTerm> ccTermList = ccTermPage.getList();
		//判断是否分页
		if(pageable.isPaging()){
			ccTermsMap.put("totalRow", ccTermPage.getTotalRow());
			ccTermsMap.put("totalPage", ccTermPage.getTotalPage());
			ccTermsMap.put("pageSize", ccTermPage.getPageSize());
			ccTermsMap.put("pageNumber", ccTermPage.getPageNumber());
		}

		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (CcTerm temp : ccTermList) {
			Map<String, Object> ccTerm = new HashMap<>();
			ccTerm.put("id", temp.get("id"));
			ccTerm.put("createDate", temp.get("create_date"));
			ccTerm.put("modifyDate", temp.get("modify_date"));
			ccTerm.put("startYear", temp.get("start_year"));
			ccTerm.put("endYear", temp.get("end_year"));
			ccTerm.put("term", temp.get("term"));
			ccTerm.put("termType", temp.get("term_type"));
			ccTerm.put("termTypeName", DictUtils.findLabelByTypeAndKey("termType", temp.getInt("term_type")));
			ccTerm.put("schoolId", temp.get("school_id"));
			ccTerm.put("sort", temp.get("sort"));
			ccTerm.put("remark", temp.get("remark"));
			ccTerm.put("isDel", temp.get("is_del"));
			list.add(ccTerm);
		}
		
		ccTermsMap.put("list", list);
		
		return renderSUC(ccTermsMap, response, header);
	}
	
}
