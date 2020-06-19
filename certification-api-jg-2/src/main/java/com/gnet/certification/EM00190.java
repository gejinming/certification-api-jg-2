package com.gnet.certification;

import java.util.ArrayList;
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
import com.gnet.model.admin.CcMajorDirection;
import com.gnet.object.CcMajorDirectionOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看专业方向列表
 * 
 * @author sll
 * 
 * @date 2016年06月28日 17:57:45
 * 
 */
@Service("EM00190")
@Transactional(readOnly=true)
public class EM00190 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		Long planId = paramsLongFilter(param.get("planId"));
		// planId不能为空信息的过滤
		if (planId == null) {
			return renderFAIL("0140", response, header);
		}
		String name = paramsStringFilter(param.get("name"));
		
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcMajorDirectionOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		Map<String, Object> ccMajorDirectionsMap = Maps.newHashMap();
		Page<CcMajorDirection> ccMajorDirectionPage = CcMajorDirection.dao.page(pageable, planId, name);
		List<CcMajorDirection> ccMajorDirectionList = ccMajorDirectionPage.getList();
		// 判断是否分页
		if(pageable.isPaging()){
			ccMajorDirectionsMap.put("totalRow", ccMajorDirectionPage.getTotalRow());
			ccMajorDirectionsMap.put("totalPage", ccMajorDirectionPage.getTotalPage());
			ccMajorDirectionsMap.put("pageSize", ccMajorDirectionPage.getPageSize());
			ccMajorDirectionsMap.put("pageNumber", ccMajorDirectionPage.getPageNumber());
		}
		
		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (CcMajorDirection temp : ccMajorDirectionList) {
			Map<String, Object> ccMajorDirection = Maps.newHashMap();
			ccMajorDirection.put("id", temp.getLong("id"));
			ccMajorDirection.put("createDate", temp.getDate("create_date"));
			ccMajorDirection.put("modifyDate", temp.getDate("modify_date"));
			ccMajorDirection.put("name", temp.get("name"));
			ccMajorDirection.put("planId", temp.getLong("plan_id"));
			ccMajorDirection.put("remark", temp.getStr("remark"));
			list.add(ccMajorDirection);
		}
		
		ccMajorDirectionsMap.put("list", list);
		
		return renderSUC(ccMajorDirectionsMap, response, header);
	}
}
