package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcCourseType;
import com.gnet.object.CcCourseTypeOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查看课程类型列表信息
 * 
 * @author GJM
 * @Date 2020年04月15日13:48:33
 */
@Service("EM01180")
public class EM01180 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据：
		Integer pageNumber = paramsIntegerFilter(params.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(params.get("pageSize"));
		String orderProperty = paramsStringFilter(params.get("orderProperty"));
		String orderDirection = paramsStringFilter(params.get("orderDirection"));
		/*String name = paramsStringFilter(params.get("name"));*/
		Long planId = paramsLongFilter(params.get("planId"));
		// planId不能为空信息的过滤
		if (planId == null) {
			return renderFAIL("0140", response, header);
		}
		
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {

			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcCourseTypeOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Page<CcCourseType> page = CcCourseType.dao.page(pageable, planId);
		List<CcCourseType> ccCourseTypeList = page.getList();
		// 判断是否分页
		if(pageable.isPaging()){
			returnMap.put("totalRow", page.getTotalRow());
			returnMap.put("totalPage", page.getTotalPage());
			returnMap.put("pageSize", page.getPageSize());
			returnMap.put("pageNumber", page.getPageNumber());
		}

		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (CcCourseType temp : ccCourseTypeList) {
			Map<String, Object> ccCourseType = Maps.newHashMap();
			ccCourseType.put("typeId", temp.getLong("id"));
            ccCourseType.put("typeValue", temp.getStr("type_value"));
            ccCourseType.put("typeName", temp.getStr("type_name"));
            ccCourseType.put("planId", temp.getLong("plan_id"));
			list.add(ccCourseType);
		}
		
		returnMap.put("list", list);
		
		// 结果返回
		return renderSUC(returnMap, response, header);
	}
}
