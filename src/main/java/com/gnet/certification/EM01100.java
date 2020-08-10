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
import com.gnet.model.admin.CcCoursePropertySecondary;
import com.gnet.object.CcCoursePropertySecondaryOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看次要课程性质某条信息列表
 * 
 * @author SY
 * @Date 2019年12月3日16:52:01
 */
@Service("EM01100")
public class EM01100 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据：
		Integer pageNumber = paramsIntegerFilter(params.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(params.get("pageSize"));
		String orderProperty = paramsStringFilter(params.get("orderProperty"));
		String orderDirection = paramsStringFilter(params.get("orderDirection"));
		Long planId = paramsLongFilter(params.get("planId"));
		if(params.containsKey("planId") && planId == null){
			return renderFAIL("1009", response, header, "planId的参数值非法");
		}
		String propertyName = paramsStringFilter(params.get("propertyName"));
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcCoursePropertySecondaryOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		// 获取整个列表
		Map<String, Object> returnMap = new HashMap<>();
		Page<CcCoursePropertySecondary> page = CcCoursePropertySecondary.dao.page(pageable, planId, propertyName);
		List<CcCoursePropertySecondary> ccCoursePropertySecondaryList = page.getList();	
		// 判断是否分页
		if(pageable.isPaging()){
			returnMap.put("totalRow", page.getTotalRow());
			returnMap.put("totalPage", page.getTotalPage());
			returnMap.put("pageSize", page.getPageSize());
			returnMap.put("pageNumber", page.getPageNumber());
		}

		// 返回内容过滤
		List<CcCoursePropertySecondary> list = new ArrayList<>();
		for (CcCoursePropertySecondary temp : ccCoursePropertySecondaryList) {
			CcCoursePropertySecondary coursePropertySecondary = new CcCoursePropertySecondary();
			coursePropertySecondary.put("id", temp.getLong("id"));
			coursePropertySecondary.put("createDate", temp.getDate("create_date"));
			coursePropertySecondary.put("modifyDate", temp.getDate("modify_date"));
			coursePropertySecondary.put("propertyName", temp.getStr("property_name"));
			coursePropertySecondary.put("remark", temp.getStr("remark"));
			list.add(coursePropertySecondary);
		}
		
		returnMap.put("list", list);
		
		// 结果返回
		return renderSUC(returnMap, response, header);
	}
	
}
