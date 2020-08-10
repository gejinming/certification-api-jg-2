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
import com.gnet.model.admin.CcInstitute;
import com.gnet.model.admin.Office;
import com.gnet.object.CcInstituteOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看学院列表
 * 
 * @author zsf
 * 
 * @date 2016年06月26日 18:57:47
 * 
 */
@Service("EM00140")
@Transactional(readOnly=true)
public class EM00140 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		String name = paramsStringFilter(param.get("name"));
		Pageable pageable = new Pageable(pageNumber, pageSize);
		String token = request.getHeader().getToken();
		Office departmentOffice = UserCacheKit.getDepartmentOffice(token);
		if(departmentOffice == null){
			return renderFAIL("0061", response, header);
		}
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcInstituteOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		Map<String, Object> ccInstitutesMap = Maps.newHashMap();
		Page<CcInstitute> ccInstitutePage = CcInstitute.dao.page(pageable,name, departmentOffice);
		List<CcInstitute> ccInstituteList = ccInstitutePage.getList();
		if(pageable.isPaging()){
			ccInstitutesMap.put("totalRow", ccInstitutePage.getTotalRow());
			ccInstitutesMap.put("totalPage", ccInstitutePage.getTotalPage());
			ccInstitutesMap.put("pageSize", ccInstitutePage.getPageSize());
			ccInstitutesMap.put("pageNumber", ccInstitutePage.getPageNumber());
		}
		
		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (CcInstitute temp : ccInstituteList) {
			Map<String, Object> ccInstitute = new HashMap<String, Object>();
			
			ccInstitute.put("id", temp.get("id"));
			ccInstitute.put("createDate", temp.get("create_date"));
			ccInstitute.put("modifyDate", temp.get("modify_date"));
			ccInstitute.put("parentid", temp.get("parentid"));
			ccInstitute.put("name", temp.get("name"));
			ccInstitute.put("type", temp.get("type"));
			ccInstitute.put("isSystem", temp.get("is_system"));
			ccInstitute.put("description", temp.get("description"));
			list.add(ccInstitute);
		}
		
		ccInstitutesMap.put("list", list);
		
		return renderSUC(ccInstitutesMap, response, header);
	}

}
