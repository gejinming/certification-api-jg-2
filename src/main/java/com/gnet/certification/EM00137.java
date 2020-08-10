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
import com.gnet.model.admin.School;
import com.gnet.object.SchoolOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 获取学校列表接口(无需权限)
 * 
 * @author xzl
 * 
 * @date 2016年10月28日09:29:15
 * 
 */
@Service("EM00137")
@Transactional(readOnly=true)
public class EM00137 extends BaseApi implements IApi {
		
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, SchoolOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
				
		Map<String, Object> schoolsMap = Maps.newHashMap();
		// 判断是否分页
		Page<School> schoolPage = School.dao.pageList(pageable);
		List<School> schoolList = schoolPage.getList();
		// 判断是否分页
		if(pageable.isPaging()){
			schoolsMap.put("totalRow", schoolPage.getTotalRow());
			schoolsMap.put("totalPage", schoolPage.getTotalPage());
			schoolsMap.put("pageSize", schoolPage.getPageSize());
			schoolsMap.put("pageNumber", schoolPage.getPageNumber());
		}
		
		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (School temp : schoolList) {
			Map<String, Object> school = new HashMap<>();
			school.put("id", temp.getLong("id"));
			school.put("name", temp.getStr("name"));
			school.put("createDate", temp.getDate("modify_date"));
			school.put("modifyDate", temp.getDate("modify_date"));
			school.put("description", temp.getStr("description"));
			list.add(school);
		}
		
		schoolsMap.put("list", list);
		
		return renderSUC(schoolsMap, response, header);
	}
}
