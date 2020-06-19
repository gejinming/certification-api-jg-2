package com.gnet.certification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcCourseHierarchySecondary;
import com.gnet.object.CcCourseHierarchySecondaryOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看次要课程层次列表信息
 * 
 * @author SY
 * @Date 2019年12月9日13:48:33
 */
@Service("EM01120")
public class EM01120 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据：
		Integer pageNumber = paramsIntegerFilter(params.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(params.get("pageSize"));
		String orderProperty = paramsStringFilter(params.get("orderProperty"));
		String orderDirection = paramsStringFilter(params.get("orderDirection"));
		String name = paramsStringFilter(params.get("name"));
		Long planId = paramsLongFilter(params.get("planId"));
		// planId不能为空信息的过滤
		if (planId == null) {
			return renderFAIL("0140", response, header);
		}
		
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcCourseHierarchySecondaryOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Page<CcCourseHierarchySecondary> page = CcCourseHierarchySecondary.dao.page(pageable, planId, name);
		List<CcCourseHierarchySecondary> ccCourseHierarchySecondaryList = page.getList();
		// 判断是否分页
		if(pageable.isPaging()){
			returnMap.put("totalRow", page.getTotalRow());
			returnMap.put("totalPage", page.getTotalPage());
			returnMap.put("pageSize", page.getPageSize());
			returnMap.put("pageNumber", page.getPageNumber());
		}

		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (CcCourseHierarchySecondary temp : ccCourseHierarchySecondaryList) {
			Map<String, Object> courseHierarchySecondary = Maps.newHashMap();
			courseHierarchySecondary.put("id", temp.getLong("id"));
			courseHierarchySecondary.put("createDate", temp.getDate("create_date"));
			courseHierarchySecondary.put("modifyDate", temp.getDate("modify_date"));
			courseHierarchySecondary.put("name", temp.getStr("name"));
			courseHierarchySecondary.put("planId", temp.getLong("plan_id"));
			courseHierarchySecondary.put("remark", temp.getStr("remark"));
			list.add(courseHierarchySecondary);
		}
		
		returnMap.put("list", list);
		
		// 结果返回
		return renderSUC(returnMap, response, header);
	}
}
