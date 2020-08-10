package com.gnet.certification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcCourseGradecompose;
import com.gnet.model.admin.CcCourseGradecomposeIndication;
import com.gnet.model.admin.CcGradecompose;
import com.gnet.model.admin.CcGraduate;
import com.gnet.model.admin.CcIndication;
import com.gnet.object.CcCourseGradecomposeIndicationOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.jfinal.plugin.activerecord.Page;

/**
 * 获取教师开课课程下的成绩组成与指标点关系列表
 *
 * updated: 获取教师开课课程下的成绩组成与课程目标关系列表
 * 
 * @author XZL
 * 
 * @date 2016年7月18日
 * 
 */
@Service("EM00544")
@Transactional(readOnly=true)
public class EM00544 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		//开课课程编号
		Long teacherCourseId = paramsLongFilter(param.get("teacherCourseId"));
		if(param.containsKey("teacherCourseId") && teacherCourseId == null) {
		    return renderFAIL("1009", response, header, "teacherCourseId的参数值非法");
		}
		//开课课程成绩组成编号
		Long courseGradecomposeId = paramsLongFilter(param.get("courseGradecomposeId"));
		if(param.containsKey("courseGradecomposeId") && courseGradecomposeId == null) {
		    return renderFAIL("1009", response, header, "courseGradecomposeId的参数值非法");
		}
		
		//开课课程编号和开课课程成绩组成编号不能同时为空
		if(teacherCourseId == null && courseGradecomposeId == null){
			return renderFAIL("0479", response, header);
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
		
		Page<CcCourseGradecomposeIndication> ccCourseGradecomposeIndicationPage = page(pageable, teacherCourseId, courseGradecomposeId);
		List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndicationList = ccCourseGradecomposeIndicationPage.getList();
		Map<String, Object> ccCourseGradecomposeIndicationsMap = Maps.newHashMap();
		// 判断是否分页
		if (pageable.isPaging()) {
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
			ccCourseGradecomposeIndication.put("indicationContent", temp.getStr("indicationContent"));
			ccCourseGradecomposeIndication.put("indicationSort", temp.getInt("indicationSort"));
			ccCourseGradecomposeIndication.put("weight", temp.getBigDecimal("weight"));
			ccCourseGradecomposeIndication.put("maxScore", temp.getBigDecimal("max_score"));
			ccCourseGradecomposeIndication.put("remark", temp.getStr("remark"));
			ccCourseGradecomposeIndication.put("isDel", temp.getBoolean("is_del"));
			list.add(ccCourseGradecomposeIndication);
		}
		
		ccCourseGradecomposeIndicationsMap.put("list", list);
		
		return renderSUC(ccCourseGradecomposeIndicationsMap, response, header);
	}
	
	/**
	 * 获取教师开课课程下的成绩组成与指标点关系列表(分页)
	 * @param pageable
	 * @param teacherCourseId
	 * @param courseGradecomposeId 
	 * @return
	 */
	private Page<CcCourseGradecomposeIndication> page(Pageable pageable, Long teacherCourseId, Long courseGradecomposeId) {
		List<Object> param = Lists.newArrayList();
		String selectString = "select ccgi.*, cg.name gradecomposeName, ci.content indicationContent, ci.sort indicationSort ";
		StringBuffer sql =  new StringBuffer("from " + CcCourseGradecomposeIndication.dao.tableName + " ccgi ");
		sql.append("inner join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.id = ccgi.course_gradecompose_id ");
		sql.append("inner join " + CcGradecompose.dao.tableName + " cg on cg.id = ccg.gradecompose_id ");
		sql.append("inner join " + CcIndication.dao.tableName + " ci on ci.id =  ccgi.indication_id ");
		sql.append("where ccgi.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ccg.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and cg.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ci.is_del = ? ");
		param.add(Boolean.FALSE);
		if(courseGradecomposeId != null){
        	sql.append("and ccgi.course_gradecompose_id = ? ");
        	param.add(courseGradecomposeId);
        }
		if(teacherCourseId != null){
			sql.append("and ccg.teacher_course_id = ? ");
			param.add(teacherCourseId);
		}
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			sql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcCourseGradecomposeIndication.dao.paginate(pageable, selectString, sql.toString(), param.toArray());
	}

}
