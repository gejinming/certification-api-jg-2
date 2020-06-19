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
import com.gnet.model.admin.CcEvaluteLevel;
import com.gnet.object.CcEvaluteLevelOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看考评点得分层次关系表列表
 * 
 * @author sll
 * 
 * @date 2016年07月05日 18:29:45
 * 
 */
@Service("EM00390")
@Transactional(readOnly=true)
public class EM00390 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		String levelName = paramsStringFilter(param.get("levelName"));
		Long teacherCourseId = paramsLongFilter(param.get("teacherCourseId"));
		if(param.containsKey("teacherCourseId") && teacherCourseId == null) {
			return renderFAIL("1009", response, header, "teacherCourseId的参数值非法");
		}

		Long indicationId = paramsLongFilter(param.get("indicationId"));
		if(param.containsKey("indicationId") && indicationId == null) {
			return renderFAIL("1009", response, header, "indicationId的参数值非法");
		}

		
		// 进行分页
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcEvaluteLevelOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		Map<String, Object> ccEvaluteLevelsMap = Maps.newHashMap();
		Page<CcEvaluteLevel> ccEvaluteLevelPage = CcEvaluteLevel.dao.page(pageable, levelName, teacherCourseId, indicationId);
		List<CcEvaluteLevel> ccEvaluteLevelList = ccEvaluteLevelPage.getList();
		// 判断是否分页
		if(pageable.isPaging()){
			ccEvaluteLevelsMap.put("totalRow", ccEvaluteLevelPage.getTotalRow());
			ccEvaluteLevelsMap.put("totalPage", ccEvaluteLevelPage.getTotalPage());
			ccEvaluteLevelsMap.put("pageSize", ccEvaluteLevelPage.getPageSize());
			ccEvaluteLevelsMap.put("pageNumber", ccEvaluteLevelPage.getPageNumber());
		}
			
		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (CcEvaluteLevel temp : ccEvaluteLevelList) {
			Map<String, Object> ccEvaluteLevel = new HashMap<>();
			ccEvaluteLevel.put("id", temp.get("id"));
			ccEvaluteLevel.put("createDate", temp.get("create_date"));
			ccEvaluteLevel.put("modifyDate", temp.get("modify_date"));
			ccEvaluteLevel.put("levelName", temp.get("level_name"));
			ccEvaluteLevel.put("score", temp.get("score"));
			ccEvaluteLevel.put("requirement", temp.get("requirement"));
			ccEvaluteLevel.put("teacherCourseId", temp.get("teacher_course_id"));
			ccEvaluteLevel.put("indicationId", temp.get("indication_id"));
			ccEvaluteLevel.put("isDel", temp.get("is_del"));
			list.add(ccEvaluteLevel);
		}
		
		ccEvaluteLevelsMap.put("list", list);
		
		return renderSUC(ccEvaluteLevelsMap, response, header);
	}
}
