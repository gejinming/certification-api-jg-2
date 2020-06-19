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
import com.gnet.model.admin.Permission;
import com.gnet.object.PermissionOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看权限列表
 * 
 * @author sll
 * 
 * @date 2016年6月6日15:14:50
 *
 */
@Service("EM00012")
@Transactional(readOnly=true)
public class EM00012 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		String code = paramsStringFilter(param.get("code"));
		String name = paramsStringFilter(param.get("name"));
		String pname = paramsStringFilter(param.get("pname"));
		Boolean isNeedGroup = paramsBooleanFilter(param.get("isNeedGroup"));
		if(param.containsKey("isNeedGroup") && isNeedGroup == null){
			return renderFAIL("1009", response, header, "isNeedGroup参数非法只能是true还是false");
		}
		Pageable pageable = new Pageable(pageNumber, pageSize);
		isNeedGroup = isNeedGroup == null ? false : isNeedGroup;
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, PermissionOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		
		Map<String, Object> permissionsMap = Maps.newHashMap();
		Page<Permission> permissionPage = Permission.dao.page(pageable, code, name, pname);
		List<Permission> permissionList = permissionPage.getList();
		// 判断是否分页
		if(pageable.isPaging()){
			permissionsMap.put("totalRow", permissionPage.getTotalRow());
			permissionsMap.put("totalPage", permissionPage.getTotalPage());
			permissionsMap.put("pageSize", permissionPage.getPageSize());
			permissionsMap.put("pageNumber", permissionPage.getPageNumber());
		}

		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, List<Map<String, Object>>> map = Maps.newHashMap();
		
		for(Permission temp : permissionList){
			Map<String, Object> tempMap = new HashMap<>();
			tempMap.put("id", temp.getLong("id"));
			tempMap.put("createDate", temp.getDate("create_date"));
			tempMap.put("modifyDate", temp.getDate("modify_date"));
			tempMap.put("code", temp.getStr("code"));
			tempMap.put("name", temp.getStr("name"));
			tempMap.put("pname", temp.getStr("pname"));
			tempMap.put("isSystem", temp.getBoolean("isSystem"));
			tempMap.put("description", temp.getStr("description"));
			
			//判断是否需要处理成分组形式
			if(isNeedGroup){
				String pName = temp.getStr("pname");
				List<Map<String, Object>> tempList = map.get(pName);
				if(tempList == null){
					tempList = Lists.newArrayList();
					map.put(pName, tempList);
				}
				tempList.add(tempMap);
			}else{
				list.add(tempMap);
			}
		}
		
		if(isNeedGroup){
			permissionsMap.put("list", map);
		}else{
			permissionsMap.put("list", list);
		}
		
		return renderSUC(permissionsMap, response, header);
	}
	
}
