package com.gnet.certification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.object.CcCourseOrderType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcCourse;
import com.gnet.object.CcMajorOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 根据专业编号返回课程列表(课程代码唯一)
 * 
 * @author xzl
 * 
 * @date 2016年8月31日
 * 
 */
@Service("EM00672")
@Transactional(readOnly=true)
public class EM00672 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		Long majorId = paramsLongFilter(param.get("majorId"));
		if (majorId == null){
			return renderFAIL("0130", response, header);
		}
		Boolean includeUnpublished = paramsBooleanFilter(param.get("includeUnpublished"));
		if(param.containsKey("includeUnpublished") && includeUnpublished == null){
			return renderFAIL("1009", response, header, "includeUnpublished参数非法只能是true还是false");
		}
        //includeUnpublished默认取值false
		includeUnpublished = includeUnpublished == null ? false : includeUnpublished;
		
		// 进行分页
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcCourseOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		Map<String, Object> map = Maps.newHashMap();
		Page<CcCourse> page = CcCourse.dao.page(pageable, majorId, includeUnpublished);
		List<CcCourse> courseList = page.getList();
		// 判断是否分页
		if(pageable.isPaging()){
			map.put("totalRow", page.getTotalRow());
			map.put("totalPage", page.getTotalPage());
			map.put("pageSize", page.getPageSize());
			map.put("pageNumber", page.getPageNumber());
			}
		
		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (CcCourse temp : courseList) {
			Map<String, Object> course = new HashMap<>();
		    course.put("id", temp.getLong("id"));
		    course.put("code", temp.getStr("code"));
		    course.put("name", temp.getStr("name"));		
			list.add(course);
		}
		
		map.put("list", list);
		
		return renderSUC(map, response, header);
	}
}
