package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.ApiPermission;
import com.gnet.model.admin.CcCourseHierarchy;
import com.gnet.object.ApiPermissionOrderType;
import com.gnet.object.CcCourseHierarchyOrderType;
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
 * 查看接口列表信息
 * 
 * @author xzl
 * @Date 2018年1月9日13:59:21
 */
@Service("EM00920")
public class EM00920 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据：
		Integer pageNumber = paramsIntegerFilter(params.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(params.get("pageSize"));
		String orderProperty = paramsStringFilter(params.get("orderProperty"));
		String orderDirection = paramsStringFilter(params.get("orderDirection"));
		String code = paramsStringFilter(params.get("code"));
		String name = paramsStringFilter(params.get("name"));
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, ApiPermissionOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Page<ApiPermission> page = ApiPermission.dao.page(pageable, code, name);
		List<ApiPermission> apiPermissionList = page.getList();
		// 判断是否分页
		if(pageable.isPaging()){
			returnMap.put("totalRow", page.getTotalRow());
			returnMap.put("totalPage", page.getTotalPage());
			returnMap.put("pageSize", page.getPageSize());
			returnMap.put("pageNumber", page.getPageNumber());
		}

		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (ApiPermission temp : apiPermissionList) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", temp.getLong("id"));
			map.put("createDate", temp.getDate("create_date"));
			map.put("modifyDate", temp.getDate("modify_date"));
			map.put("code", temp.getStr("code"));
			map.put("name", temp.getStr("name"));
			map.put("description", temp.getStr("description"));
			list.add(map);
		}
		
		returnMap.put("list", list);
		
		// 结果返回
		return renderSUC(returnMap, response, header);
	}
}
