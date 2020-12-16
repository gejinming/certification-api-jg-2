package com.gnet.certification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.bcel.generic.IF_ACMPEQ;
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
import com.gnet.model.admin.CcTeacherCourse;
import com.gnet.object.CcCourseGradecomposeOrderType;
import com.gnet.pager.Pageable;
import com.gnet.service.CcCourseGradecomposeIndicationService;
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;
/**
 * 通过教师开课编号得到成绩组成列表
 * @author xzl
 * @Date 2016年7月7日
 */
@Service("EM00522")
@Transactional(readOnly=true)
public class EM00522 extends BaseApi implements IApi{

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		// 分页参数
		Integer pageNumber = paramsIntegerFilter(params.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(params.get("pageSize"));
		String orderProperty = paramsStringFilter(params.get("orderProperty"));
		String orderDirection = paramsStringFilter(params.get("orderDirection"));
		Long teacherCourseId = paramsLongFilter(params.get("teacherCourseId"));
		
		if (teacherCourseId == null) {
			return renderFAIL("0310", response, header);
		}
		
		Pageable pageable = new Pageable(pageNumber, pageSize);
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcCourseGradecomposeOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
				
		Map<String, Object> result = new HashMap<String, Object>();
	    //查询并返回结果
		Page<CcCourseGradecompose> page = CcCourseGradecompose.dao.page(pageable, teacherCourseId);
		List<CcCourseGradecompose> courseGradecomposeList = page.getList();
		
		//判断是否分页
		if(pageable.isPaging()){
			result.put("pageNumber", page.getPageNumber());
			result.put("pageSize", page.getPageSize());
			result.put("totalRow", page.getTotalRow());
			result.put("totalPage", page.getTotalPage());
		}
		
		//获取课程名和教师名
		CcTeacherCourse teacherCourse = CcTeacherCourse.dao.findTeacherNameAndCourseName(teacherCourseId);
		
		// 统计当前教师课程所有的成绩组成的各自指标点之和
		CcCourseGradecomposeIndicationService ccCourseGradecomposeIndicationService = SpringContextHolder.getBean(CcCourseGradecomposeIndicationService.class);
		// <couseGradecomposeId, fullScore>
		Map<Long, BigDecimal> fullScoreMap = ccCourseGradecomposeIndicationService.caculateCourseGradecomposeScoreToMap(teacherCourseId);
		
		//返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for(CcCourseGradecompose temp: courseGradecomposeList){
			Map<String, Object> courseGradecompose =  Maps.newHashMap();
			Long couseGradecomposeId = temp.getLong("id");
			Integer inputScoreType = temp.getInt("input_score_type");
			String inputScoreTypeName="";
			if (inputScoreType==1){
				inputScoreTypeName="单批次指标点成绩直接录入";
			}else if (inputScoreType==2){
				inputScoreTypeName="单批次题目汇总得到指标点成绩";
			}else if(inputScoreType==3){
				inputScoreTypeName="多批次题目汇总得到指标点成绩";
			}else {
				inputScoreTypeName="多批次指标点成绩直接录入";
			}
			courseGradecompose.put("id", couseGradecomposeId);
			courseGradecompose.put("createDate", temp.getDate("create_date"));
			courseGradecompose.put("modifyDate", temp.getDate("modify_date"));
			courseGradecompose.put("gradecomposeId", temp.getLong("gradecompose_id"));
			courseGradecompose.put("teacherCourseId", temp.getLong("teacher_course_id"));
			courseGradecompose.put("name", temp.getStr("name"));
			courseGradecompose.put("teacherName", teacherCourse.getStr("teacherName"));
			courseGradecompose.put("courseName", teacherCourse.getStr("courseName"));
			courseGradecompose.put("sort", temp.getInt("sort"));
			BigDecimal otherScore = temp.getBigDecimal("other_score");
			courseGradecompose.put("otherScore", otherScore);
			BigDecimal fullScore = fullScoreMap.get(couseGradecomposeId);
			otherScore = otherScore == null ? new BigDecimal(0) : otherScore;
			fullScore = fullScore == null ? new BigDecimal(0) : fullScore;
			courseGradecompose.put("fullScore", otherScore.add(fullScore));
			courseGradecompose.put("percentage", temp.getInt("percentage"));
			courseGradecompose.put("inputScoreType", temp.getInt("input_score_type"));
			courseGradecompose.put("inputScoreTypeName", inputScoreTypeName);
			courseGradecompose.put("hierarchyLevel", temp.get("hierarchy_level"));
			list.add(courseGradecompose);
		}
		result.put("list", list);
		
		//返回结果
		return renderSUC(result, response, header);
		
				
	}
}
