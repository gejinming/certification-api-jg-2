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
import com.gnet.model.admin.CcEvalute;
import com.gnet.object.CcEvaluteOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看教师课程考评点列表
 * 
 * @author sll
 * 
 * @date 2016年07月04日 15:58:01
 * 
 */
@Service("EM00360")
@Transactional(readOnly=true)
public class EM00360 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		Long indicationId = paramsLongFilter(param.get("indicationId")); 
		if(param.containsKey("indicationId") && indicationId == null) {
			return renderFAIL("1009", response, header, "indicationId的参数值非法");
		}
		Long teacherCourseId = paramsLongFilter(param.get("teacherCourseId"));
		if(param.containsKey("teacherCourseId") && teacherCourseId == null) {
			return renderFAIL("1009", response, header, "teacherCourseId的参数值非法");
		}

		// 进行分页
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcEvaluteOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		Map<String, Object> ccEvalutesMap = Maps.newHashMap();
		Page<CcEvalute> ccEvalutePage = CcEvalute.dao.page(pageable, indicationId, teacherCourseId);
		List<CcEvalute> ccEvaluteList = ccEvalutePage.getList();
		// 判断是否分页
		if (pageable.isPaging()) {
			
			ccEvalutesMap.put("totalRow", ccEvalutePage.getTotalRow());
			ccEvalutesMap.put("totalPage", ccEvalutePage.getTotalPage());
			ccEvalutesMap.put("pageSize", ccEvalutePage.getPageSize());
			ccEvalutesMap.put("pageNumber", ccEvalutePage.getPageNumber());
		}
		
		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (CcEvalute temp : ccEvaluteList) {
			Map<String, Object> ccEvalute = new HashMap<>();
			ccEvalute.put("id", temp.get("id"));
			ccEvalute.put("createDate", temp.get("create_date"));
			ccEvalute.put("modifyDate", temp.get("modify_date"));
			ccEvalute.put("indicationId", temp.get("indication_id"));
			ccEvalute.put("indicationContent", temp.get("indication_content"));
			ccEvalute.put("teacherCourseId", temp.get("teacher_course_id"));
			ccEvalute.put("indexNum", temp.get("index_num"));
			ccEvalute.put("teacherName", temp.get("teacher_name"));
			ccEvalute.put("courseName", temp.get("course_name"));
			ccEvalute.put("content", temp.get("content"));
			ccEvalute.put("evaluteTypeId", temp.get("evalute_type_id"));
			ccEvalute.put("type", temp.getInt("type"));
			ccEvalute.put("percentage", temp.get("percentage"));
			ccEvalute.put("weight", temp.get("weight"));
			ccEvalute.put("remark", temp.get("remark"));
			list.add(ccEvalute);
		}
		
		ccEvalutesMap.put("list", list);
		
		return renderSUC(ccEvalutesMap, response, header);
	}

}
