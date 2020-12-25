package com.gnet.certification;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradecompose;

import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;


/**
 * 开课课程成绩组成元素增加
 *
 * @author xzl
 *
 * @date 2016年7月7日
 *
 */
@Service("EM00520")
@Transactional(readOnly=false)
public class EM00520 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {

		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Map<String, Object> result = new HashMap<>();

		//开课课程编号
		Long teacherCourseId = paramsLongFilter(param.get("teacherCourseId"));
		// 成绩组成以及分数
		JSONArray gradecomposeIdPercentageArray = paramsJSONArrayFilter(param.get("gradecomposeIdPercentageArray"));
		//达成度分析类型
		Integer resultType = paramsIntegerFilter(param.get("resultType"));
		Integer allPercentage = 0;
		if( resultType == null){
			return renderFAIL("0496", response, header);
		}
		//成绩组成元素编号
		List<Long> gradecomposeIds = new ArrayList<>();;
		Map<Long, Integer> newGradecomposeIdPercentageMap = new HashMap<>();
		for(int i = 0; i < gradecomposeIdPercentageArray.size(); i++) {
			JSONObject gradecomposeIdPercentageMap = (JSONObject) gradecomposeIdPercentageArray.get(i);
			Long gradecomposeId = Long.valueOf(gradecomposeIdPercentageMap.get("gradecomposeId").toString());
			if(gradecomposeId == null) {
				// 如果发生错误，直接忽略
				continue;
			}
			Integer percentage = gradecomposeIdPercentageMap.get("percentage").equals("") ? 0 : Integer.valueOf(gradecomposeIdPercentageMap.get("percentage").toString());
			allPercentage = allPercentage + percentage;
			newGradecomposeIdPercentageMap.put(gradecomposeId, percentage);
			gradecomposeIds.add(gradecomposeId);
		}
		if(gradecomposeIds.isEmpty()){
			return renderFAIL("0455", response, header);
		}

		if (teacherCourseId == null) {
			return renderFAIL("0310", response, header);
		}

		// 验证占比总共是否大于100
		if(allPercentage > 100) {
			return renderFAIL("0904", response, header);
		}
		List<CcCourseGradecompose> ccCourseGradecomposeOld = CcCourseGradecompose.dao.findFilteredByColumn("teacher_course_id", teacherCourseId);
		for(CcCourseGradecompose temp : ccCourseGradecomposeOld) {
			Integer tempPercentage = temp.getInt("percentage");
			tempPercentage = tempPercentage == null ? 0 : tempPercentage;
			allPercentage = allPercentage + tempPercentage;
			if(allPercentage > 100) {
				return renderFAIL("0904", response, header);
			}
		}

		//重复成绩组成元素编号
		List<Long> repeatGradecomposeIds = Lists.newArrayList();

		//开课课程已经存在成绩组成编号
		List<Long> existGradecomposeIds = Lists.newArrayList();

		List<CcCourseGradecompose> courseGradecomposes = CcCourseGradecompose.dao.findFilteredByColumn("teacher_course_id", teacherCourseId);

		if(!courseGradecomposes.isEmpty()){

			for(CcCourseGradecompose courseGradecompose: courseGradecomposes){
				existGradecomposeIds.add(courseGradecompose.getLong("gradecompose_id"));
			}
			//得到重复的成绩组成编号
			for(Long gradecomposeId : gradecomposeIds){
				if(existGradecomposeIds.contains(gradecomposeId)){
					repeatGradecomposeIds.add(gradecomposeId);
				}
			}

		}
		//存在重复则返回错误
		if(!repeatGradecomposeIds.isEmpty()){
			return renderFAIL("0470", response, header);
		}

		List<CcCourseGradecompose> newCourseGradecomposes  = Lists.newArrayList();

		for(Long gradecomposeId : gradecomposeIds){

			Date date = new Date();
			IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
			CcCourseGradecompose courseGradecompose = new CcCourseGradecompose();
			courseGradecompose.set("id", idGenerate.getNextValue());
			courseGradecompose.set("create_date", date);
			courseGradecompose.set("modify_date", date);
			courseGradecompose.set("gradecompose_id", gradecomposeId);
			courseGradecompose.set("teacher_course_id", teacherCourseId);
			courseGradecompose.set("sort", 0);
			courseGradecompose.set("percentage", newGradecomposeIdPercentageMap.get(gradecomposeId));
			courseGradecompose.set("input_score_type", CcCourseGradecompose.DIRECT_INPUT_SCORE);
			if(resultType == 2){
				courseGradecompose.set("hierarchy_level", 5);
			}

			courseGradecompose.set("is_del", Boolean.FALSE);
			newCourseGradecomposes.add(courseGradecompose);

		}

		if(!CcCourseGradecompose.dao.batchSave(newCourseGradecomposes)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}
}
