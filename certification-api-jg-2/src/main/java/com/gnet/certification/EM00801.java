package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcCourseHierarchy;
import com.gnet.model.admin.CcCourseTargetIndication;
import com.gnet.model.admin.CcIndication;
import com.gnet.model.admin.CcIndicationCourse;
import com.gnet.object.CcCourseHierarchyOrderType;
import com.gnet.object.CcIndicationOrderType;
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
 * 课程下的课程目标
 * 
 * @author xzl
 * @Date 2017年11月22日
 */
@Service("EM00801")
public class EM00801 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据：
		Integer pageNumber = paramsIntegerFilter(params.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(params.get("pageSize"));
		String orderProperty = paramsStringFilter(params.get("orderProperty"));
		String orderDirection = paramsStringFilter(params.get("orderDirection"));
        Long courseIndicationId = paramsLongFilter(params.get("courseIndicationId"));

		if(courseIndicationId == null){
        	return renderFAIL("0250", response, header);
		}
		CcIndicationCourse ccIndicationCourse = CcIndicationCourse.dao.findFilteredById(courseIndicationId);
		if(ccIndicationCourse == null){
			return renderFAIL("1103", response, header);
		}
		Long courseId = ccIndicationCourse.getLong("course_id");
		
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcIndicationOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}

		Map<String, Object> returnMap = new HashMap<String, Object>();
		Page<CcIndication> page = CcIndication.dao.page(pageable, courseId);
		List<CcIndication> ccIndicationList = page.getList();

		// 判断是否分页
		if(pageable.isPaging()){
			returnMap.put("totalRow", page.getTotalRow());
			returnMap.put("totalPage", page.getTotalPage());
			returnMap.put("pageSize", page.getPageSize());
			returnMap.put("pageNumber", page.getPageNumber());
		}

		//已关联成绩组成的课程目标
		List<CcIndication> ccIndications = CcIndication.dao.findByCourseId(courseId);
		List<Integer> sorts = new ArrayList<>();
		for(CcIndication ccIndication : ccIndications){
			sorts.add(ccIndication.getInt("sort"));
		}

		List<CcCourseTargetIndication> ccCourseTargetIndications = CcCourseTargetIndication.dao.findByIndicationCourseId(courseIndicationId);
		List<Integer> sortList = new ArrayList<>();
		for(CcCourseTargetIndication ccIndication : ccCourseTargetIndications){
			sortList.add(ccIndication.getInt("sort"));
		}

		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (CcIndication temp : ccIndicationList) {
			Map<String, Object> ccIndication = Maps.newHashMap();
			Integer sort = temp.getInt("sort");
			ccIndication.put("id", temp.getLong("id"));
			ccIndication.put("createDate", temp.getDate("create_date"));
			ccIndication.put("modifyDate", temp.getDate("modify_date"));
			ccIndication.put("sort", sort);
			ccIndication.put("content", temp.getStr("content"));
			if(sort == 1 || sorts.contains(sort)){
				ccIndication.put("canDelete", false);
			}else{
				ccIndication.put("canDelete", true);
			}
			if(sortList.contains(sort)){
				ccIndication.put("isRelate", true);
			}else{
				ccIndication.put("isRelate", false);
			}
			list.add(ccIndication);
		}
		
		returnMap.put("list", list);
		
		// 结果返回
		return renderSUC(returnMap, response, header);
	}
}
