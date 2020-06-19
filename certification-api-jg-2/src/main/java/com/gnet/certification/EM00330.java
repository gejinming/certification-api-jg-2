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
import com.gnet.model.admin.CcCourseModule;
import com.gnet.object.CcCourseModuleOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;
/**
 * 查看所属模块列表信息
 * @author xzl
 * @Date 2016年7月2日
 */
@Service("EM00330")
@Transactional(readOnly=true)
public class EM00330 extends BaseApi implements IApi{

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 分页参数
		Integer pageNumber = paramsIntegerFilter(params.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(params.get("pageSize"));
		String orderProperty = paramsStringFilter(params.get("orderProperty"));
		String orderDirection = paramsStringFilter(params.get("orderDirection"));
		String moduleName = paramsStringFilter(params.get("moduleName"));
		Long planId = paramsLongFilter(params.get("planId"));
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// planId不能为空信息的过滤
		if(planId == null){
			return renderFAIL("0140", response, header);
		}
	
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcCourseModuleOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		Map<String, Object> result = new HashMap<String, Object>();	
		Page<CcCourseModule> page = CcCourseModule.dao.page(pageable, planId, moduleName);
		List<CcCourseModule> courseModuleList = page.getList();
		//判断是否分页
		if(pageable.isPaging()){
			result.put("pageNumber", page.getPageNumber());
			result.put("pageSize", page.getPageSize());
			result.put("totalRow", page.getTotalRow());
			result.put("totalPage", page.getTotalPage());
		}
	
		//返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for(CcCourseModule temp: courseModuleList){
			Map<String, Object> courseModule =  Maps.newHashMap();
			courseModule.put("id", temp.getLong("id"));
			courseModule.put("createDate", temp.getDate("create_date"));
			courseModule.put("modifyDate", temp.getDate("modify_date"));
			courseModule.put("moduleName", temp.getStr("module_name"));
			courseModule.put("planId", temp.getLong("plan_id"));
			courseModule.put("sumGroupId", temp.getLong("sum_group_id"));
			courseModule.put("remark", temp.getStr("remark"));
			list.add(courseModule);
		}
		result.put("list", list);
		
		//返回结果
		return renderSUC(result, response, header);
		
				
	}
}
