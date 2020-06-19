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
import com.gnet.model.admin.CcCourseGradeComposeDetail;
import com.gnet.object.CcCourseGradeComposeDetailOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看成绩组成元素明细表列表
 * 
 * @author sll
 * 
 * @date 2016年07月06日 14:37:10
 * 
 */
@Service("EM00410")
@Transactional(readOnly=true)
public class EM00410 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		String name = paramsStringFilter(param.get("name"));
		String detail = paramsStringFilter(param.get("detail"));
		Long courseGradeComposeId = paramsLongFilter(param.get("courseGradeComposeId"));
		if(param.containsKey("courseGradeComposeId") && courseGradeComposeId == null) {
			return renderFAIL("1009", response, header, "courseGradeComposeId的参数值非法");
		}

		// 进行分页
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcCourseGradeComposeDetailOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		Map<String, Object> ccCourseGradeComposeDetailsMap = Maps.newHashMap();		
		Page<CcCourseGradeComposeDetail> ccCourseGradeComposeDetailPage = CcCourseGradeComposeDetail.dao.page(pageable, name, detail, courseGradeComposeId);
		List<CcCourseGradeComposeDetail> ccCourseGradeComposeDetailList = ccCourseGradeComposeDetailPage.getList();
		// 判断是否分页
		if(pageable.isPaging()){
			ccCourseGradeComposeDetailsMap.put("totalRow", ccCourseGradeComposeDetailPage.getTotalRow());
			ccCourseGradeComposeDetailsMap.put("totalPage", ccCourseGradeComposeDetailPage.getTotalPage());
			ccCourseGradeComposeDetailsMap.put("pageSize", ccCourseGradeComposeDetailPage.getPageSize());
			ccCourseGradeComposeDetailsMap.put("pageNumber", ccCourseGradeComposeDetailPage.getPageNumber());
		}
		
		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (CcCourseGradeComposeDetail temp : ccCourseGradeComposeDetailList) {
			Map<String, Object> ccCourseGradeComposeDetail = new HashMap<>();
			ccCourseGradeComposeDetail.put("id", temp.get("id"));
			ccCourseGradeComposeDetail.put("createDate", temp.get("create_date"));
			ccCourseGradeComposeDetail.put("modifyDate", temp.get("modify_date"));
			ccCourseGradeComposeDetail.put("name", temp.get("name"));
			ccCourseGradeComposeDetail.put("score", temp.get("score"));
			ccCourseGradeComposeDetail.put("detail", temp.get("detail"));
			ccCourseGradeComposeDetail.put("remark", temp.get("remark"));
			ccCourseGradeComposeDetail.put("courseGradecomposeId", temp.get("course_gradecompose_id"));
			list.add(ccCourseGradeComposeDetail);
		}
		
		ccCourseGradeComposeDetailsMap.put("list", list);
		
		return renderSUC(ccCourseGradeComposeDetailsMap, response, header);
	}
}
