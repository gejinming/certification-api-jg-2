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
import com.gnet.model.admin.CcCourseGradecomposeIndication;
import com.gnet.object.CcCourseGradecomposeIndicationOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看开课课程成绩组成元素指标点关联表列表
 *
 * updated: 查看开课课程成绩组成元素和课程目标关联表的列表
 * 
 * @author XZL
 * 
 * @date 2016年07月07日 20:45:05
 * 
 */
@Service("EM00541")
@Transactional(readOnly=true)
public class EM00541 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		//开课课程编号
		Long teacherCourseId = paramsLongFilter(param.get("teacherCourseId"));
		//指标点编号
		Long indicationId = paramsLongFilter(param.get("indicationId"));
		
		if(teacherCourseId == null){
			return renderFAIL("0310", response, header);
		}
		
		if (indicationId == null) {
			return renderFAIL("1111", response, header);
		}
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		
		// 进行分页
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcCourseGradecomposeIndicationOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		Map<String, Object> ccCourseGradecomposeIndicationsMap = Maps.newHashMap();
		Page<CcCourseGradecomposeIndication> ccCourseGradecomposeIndicationPage = CcCourseGradecomposeIndication.dao.page(pageable, teacherCourseId, indicationId);
		List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndicationList = ccCourseGradecomposeIndicationPage.getList();
		// 判断是否分页
		if(pageable.isPaging()){
			ccCourseGradecomposeIndicationsMap.put("totalRow", ccCourseGradecomposeIndicationPage.getTotalRow());
			ccCourseGradecomposeIndicationsMap.put("totalPage", ccCourseGradecomposeIndicationPage.getTotalPage());
			ccCourseGradecomposeIndicationsMap.put("pageSize", ccCourseGradecomposeIndicationPage.getPageSize());
			ccCourseGradecomposeIndicationsMap.put("pageNumber", ccCourseGradecomposeIndicationPage.getPageNumber());
		}
		
		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (CcCourseGradecomposeIndication temp : ccCourseGradecomposeIndicationList) {
			Map<String, Object> ccCourseGradecomposeIndication = new HashMap<>();
			ccCourseGradecomposeIndication.put("id", temp.getLong("id"));
			ccCourseGradecomposeIndication.put("createDate", temp.getDate("create_date"));
			ccCourseGradecomposeIndication.put("modifyDate", temp.getDate("modify_date"));
			ccCourseGradecomposeIndication.put("gradecomposeName", temp.getStr("gradecomposeName"));
			ccCourseGradecomposeIndication.put("indicationId", temp.getLong("indication_id"));
			ccCourseGradecomposeIndication.put("courseGradecomposeId", temp.getLong("course_gradecompose_id"));
			ccCourseGradecomposeIndication.put("weight", temp.getBigDecimal("weight"));
			ccCourseGradecomposeIndication.put("maxScore", temp.getBigDecimal("max_score"));
			ccCourseGradecomposeIndication.put("remark", temp.getStr("remark"));
			list.add(ccCourseGradecomposeIndication);
		}
		
		ccCourseGradecomposeIndicationsMap.put("list", list);
		
		return renderSUC(ccCourseGradecomposeIndicationsMap, response, header);
	}
}
