package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcCourseOutlineTemplate;
import com.gnet.model.admin.CcEvaluteLevel;
import com.gnet.object.CcCourseOutlineTemplateOrderType;
import com.gnet.object.CcEvaluteLevelOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查看大纲模板列表
 * 
 * @author xzl
 * 
 * @date 2016年07月05日 18:29:45
 * 
 */
@Service("EM00699")
@Transactional(readOnly=true)
public class EM00699 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		String name = paramsStringFilter(param.get("name"));
		Long outlineTypeId = paramsLongFilter(param.get("outlineTypeId"));

		if(outlineTypeId == null){
			return  renderFAIL("0892", response, header);
		}
		
		// 进行分页
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcCourseOutlineTemplateOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		Map<String, Object> templateMap = Maps.newHashMap();
		Page<CcCourseOutlineTemplate> ccCourseOutlineTemplatePage = CcCourseOutlineTemplate.dao.page(pageable, name, outlineTypeId);
		List<CcCourseOutlineTemplate> ccCourseOutlineTemplateList = ccCourseOutlineTemplatePage.getList();
		// 判断是否分页
		if(pageable.isPaging()){
			templateMap.put("totalRow", ccCourseOutlineTemplatePage.getTotalRow());
			templateMap.put("totalPage", ccCourseOutlineTemplatePage.getTotalPage());
			templateMap.put("pageSize", ccCourseOutlineTemplatePage.getPageSize());
			templateMap.put("pageNumber", ccCourseOutlineTemplatePage.getPageNumber());
		}
			
		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (CcCourseOutlineTemplate temp : ccCourseOutlineTemplateList) {

			Map<String, Object> map = new HashMap<>();
			map.put("id", temp.getLong("id"));
			map.put("createDate", temp.getDate("create_date"));
			map.put("modifyDate", temp.getDate("modify_date"));
			map.put("name", temp.getStr("name"));
			map.put("outlineTypeId", temp.getLong("outline_type_id"));
			list.add(map);
		}

		templateMap.put("list", list);
		
		return renderSUC(templateMap, response, header);
	}
}
