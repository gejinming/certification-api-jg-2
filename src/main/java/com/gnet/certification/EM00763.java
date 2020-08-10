package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.gnet.utils.ConvertUtils;
import com.gnet.utils.DictUtils;
import com.gnet.utils.PriceUtils;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 考核分析法课程目标评价接口
 *
 * @author xzl
 * @date 2017年10月18日
 */
@Transactional(readOnly = false)
@Service("EM00763")
public class EM00763 extends BaseApi implements IApi{

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {

		Map<String, Object> param = request.getData();
		Map<String, Object> result = new HashMap<>();

		Long id = paramsLongFilter(param.get("id"));

		if(id == null){
			return renderFAIL("0506", response, header);
		}

		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findFilteredById(id);
		if(ccTeacherCourse == null){
			return renderFAIL("0311", response, header);
		}


		//查询同一课程同一学期的开课课程
		Map<String, Object> paras = new HashMap<>();
		paras.put("course_id", ccTeacherCourse.getLong("course_id"));
		paras.put("term_id", ccTeacherCourse.getLong("term_id"));
		List<CcTeacherCourse> ccTeacherCourses = CcTeacherCourse.dao.findFilteredByColumn(paras);
		Long teacherCourseIds[] = new Long[ccTeacherCourses.size()];

		for(int i=0; i< ccTeacherCourses.size(); i++){
			Long teacherCourseId = ccTeacherCourses.get(i).getLong("id");
			teacherCourseIds[i] = teacherCourseId;
			//需要验证课程是否关联成绩成绩组成了
			if(CcCourseGradecompose.dao.countFiltered("teacher_course_id",teacherCourseId) <=0){
				return renderFAIL("0970", response, header);
			}

			//需要验证开课课程成绩组成是否关联指标点了
			if(!CcCourseGradecompose.dao.isRelationGradecomposeAndIndication(teacherCourseId)){
				return renderFAIL("0971", response, header);
			}
		}

		//班级名称列表
		List<String> educlassNames = Lists.newArrayList();
		List<CcEduclass> ccEduclasses = CcEduclass.dao.findFilteredByColumnIn("teacher_course_id", teacherCourseIds);
		for(CcEduclass ccEduclass : ccEduclasses){
			educlassNames.add(ccEduclass.getStr("educlass_name"));
		}

		//为保证所有导出的word的顺序一致,对传入的开课课程编号重新赋值
		id = teacherCourseIds[0];
		//指标点+成绩组成为key，开课课程关联指标点编号为value
		Map<String, String> indicationAndCompseMap = new HashMap<>();
		//成绩组成为key,开课课程成绩组成为value
		Map<Long, String> gradeCompseMap = new HashMap<>();

		List<CcTeacherCourse> ccTeacherCourseList = CcTeacherCourse.dao.findByTeacherCourseId(id);
		for(CcTeacherCourse teacherCourse : ccTeacherCourseList) {
			Long courseGradecomposeId = teacherCourse.getLong("courseGradecomposeId");
			Long gradecomposeId = teacherCourse.getLong("gradecomposeId");
			Long indicationId = teacherCourse.getLong("indicationId");
			Long gradecomposeIndicationId = teacherCourse.getLong("gradecomposeIndicationId");
			indicationAndCompseMap.put(String.format("%s-%s", indicationId, gradecomposeId), String.valueOf(gradecomposeIndicationId));
			gradeCompseMap.put(gradecomposeId, String.valueOf(courseGradecomposeId));
		}


		Map<String, BigDecimal> studentTotalScoreMap = new HashMap<>();
		//学生在各个成绩组成指标点下的成绩
		Map<String, List<Map<String, Object>>> studentScoreMap = new HashMap<>();
		Map<String, Map<String, Object>> studentsMap = new HashMap<>();


		//开课课程下学生数量
		Long studentCounts = CcEduclassStudent.dao.findStudentCounts(teacherCourseIds);
		//对应开课课程指标点下学生总成绩
		List<CcEduclassStudent> ccEduclassStudentScores = CcEduclassStudent.dao.findStudentScores(id);
		Map<String, BigDecimal> averageScoreMap = new HashMap<>();
		for(CcEduclassStudent ccEduclassStudent : ccEduclassStudentScores){
			BigDecimal totalScore = ccEduclassStudent.getBigDecimal("totalScore");
			String gradecomposeIndicationId = indicationAndCompseMap.get(String.format("%s-%s", ccEduclassStudent.getLong("indicationId"), ccEduclassStudent.getLong("gradecomposeId")));
			BigDecimal value = averageScoreMap.get(gradecomposeIndicationId);
			if(value == null){
				averageScoreMap.put(gradecomposeIndicationId, totalScore);
			}else{
				totalScore = totalScore == null ? BigDecimal.valueOf(0) : totalScore;
				averageScoreMap.put(gradecomposeIndicationId, PriceUtils._add(value, totalScore));
			}
		}

		//除以学生数量得到平均数
		for(Map.Entry<String, BigDecimal> entry : averageScoreMap.entrySet()){
			averageScoreMap.put(entry.getKey(), entry.getValue() == null ? null : entry.getValue().divide(BigDecimal.valueOf(studentCounts), 3, RoundingMode.HALF_UP));
		}

		//各个成绩组成下学生的总和
		List<CcEduclassStudent> ccEduclassStudentSums = CcEduclassStudent.dao.findByTeacherCourseId(id, true);
		for(CcEduclassStudent ccEduclassStudent : ccEduclassStudentSums){
			Long gradecomposeId = ccEduclassStudent.getLong("gradecomposeId");
			String key = String.format("%s-%s", ccEduclassStudent.getStr("studentNo"), gradeCompseMap.get(gradecomposeId));
			studentTotalScoreMap.put(key, ccEduclassStudent.getBigDecimal("totalScore"));
		}


		for(int i=0; i<teacherCourseIds.length; i++){
			Long teacherCourseId = teacherCourseIds[i];

			//各个成绩组成指标点下的学生成绩
			List<CcEduclassStudent> ccEduclassStudentList = CcEduclassStudent.dao.findByTeacherCourseId(teacherCourseId, false);
			for(CcEduclassStudent ccEduclassStudent : ccEduclassStudentList){
				Long indicationId = ccEduclassStudent.getLong("indicationId");
				Long gradecomposeId = ccEduclassStudent.getLong("gradecomposeId");
				String courseGradecomposeId = gradeCompseMap.get(gradecomposeId);
				String gradecomposeIndicationId = indicationAndCompseMap.get(String.format("%s-%s", indicationId, gradecomposeId));
				String studentNo = ccEduclassStudent.getStr("studentNo");
				String studentName = ccEduclassStudent.getStr("studentName");
				String className = ccEduclassStudent.getStr("className");
				//学生成绩可能为空
				BigDecimal grade = ccEduclassStudent.getBigDecimal("grade");

				String key = String.format("%s-%s",studentNo, courseGradecomposeId);

				if(studentsMap.get(key) == null){
					Map<String, Object> studentMap = new HashMap<>();
					studentMap.put("studentNo", studentNo);
					studentMap.put("studentName", studentName);
					studentMap.put("className", className);
					studentMap.put(gradecomposeIndicationId, grade);
					studentsMap.put(key, studentMap);
				}else{
					studentsMap.get(key).put(gradecomposeIndicationId, grade);
				}
			}
		}


		//给学生成绩按成绩组成分组并且加入学生总成绩
		for(Map.Entry<String, Map<String, Object>> entry : studentsMap.entrySet()){
			String key = entry.getKey();
			Map<String, Object> ccEduclassStudent = entry.getValue();
			String courseGradecomposeId = key.split("-")[1];
			ccEduclassStudent.put("totalScore", studentTotalScoreMap.get(key));

			List<Map<String, Object>> studentScoreLists = studentScoreMap.get(courseGradecomposeId);
			if(studentScoreLists == null){
				studentScoreLists = new ArrayList<>();
				studentScoreMap.put(courseGradecomposeId, studentScoreLists);
			}
			studentScoreLists.add(ccEduclassStudent);
			Collections.sort(studentScoreLists, new Comparator<Map<String, Object>>() {
				@Override
				public int compare(Map<String, Object> o1, Map<String, Object> o2) {
					return  ConvertUtils.convert(o1.get("studentNo"), String.class).compareTo(ConvertUtils.convert(o2.get("studentNo"), String.class));
				}
			});
		}

		//查找版本课程达成度
		CcGraduateVersion ccGraduateVersion = CcGraduateVersion.dao.findByTeacherCourseId(id);
		if(ccGraduateVersion != null){
			result.put("targetValue", ccGraduateVersion.getBigDecimal("pass"));
		}

		List<Map<String, Object>> ccCourseGradecomposes = Lists.newArrayList();
		//开课课程成绩组成
		List<CcCourseGradecompose> ccCourseGradecomposeList = CcCourseGradecompose.dao.findByTeacherCourseId(id);
		for(CcCourseGradecompose ccCourseGradecompose : ccCourseGradecomposeList){
			Map<String, Object> map = new HashMap<>();
			map.put("gradecomposeName", ccCourseGradecompose.getStr("gradecomposeName"));
			map.put("percentage", ccCourseGradecompose.getInt("percentage"));
			ccCourseGradecomposes.add(map);
		}

		CcEvaluteLevel ccEvaluteLevel = CcEvaluteLevel.dao.findFirstFilteredByColumn("teacher_course_id", id);

		//判断是百分制还是五级分制还是而级分制
		if(CcTeacherCourse.RESULT_TYPE_SCORE.equals(ccTeacherCourse.getInt("result_type"))){
			result.put("percentSystem", true);
		}else {
			if(ccEvaluteLevel != null){
				if(CcEvaluteLevel.LEVEL_TOW.equals(ccEvaluteLevel.getInt("level"))){
					result.put("twoGradeSystem", true);
				}else{
					result.put("fiveGradeSystem", true);
				}
			}
		}

		//成绩组成指标点编号关联的指标点编号
		Map<String, String> gradecomposeAndIndicationMap = new HashMap<>();
		//指标点编号对应的指标点详细信息
		Map<String, Map<String, Object>> indicationMap = new HashMap<>();
		//开课课程下的成绩组成以及指标点情况
		List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndicationList = CcCourseGradecomposeIndication.dao.findDetailsByTeacherCourseId(id);
		List<Map<String, Object>> ccCourseGradecomposeIndications = Lists.newArrayList();
		Map<String, CcCourseGradecomposeIndication> indexMap = new HashMap<>();
		//开课课程成绩组成key，对应的开课课程指标点和指标点关联为value的map
		Map<String, List<Map<String, Object>>> courseGradecomposeIndicationMap = new HashMap<>();
		for(CcCourseGradecomposeIndication temp : ccCourseGradecomposeIndicationList){
			//todo 因为当前的freemarket版本无法识别map中的key为long类型的数据
			String indicationId = String.valueOf(temp.getLong("indicationId"));
			BigDecimal maxScore = temp.getBigDecimal("max_score");
			BigDecimal weight = temp.getBigDecimal("weight");
			String gradecomposeName = temp.getStr("gradecomposeName");
			String courseGradecomposeId = String.valueOf(temp.getLong("courseGradecomposeId"));
			String gradecomposeIndicationId = String.valueOf(temp.getLong("gradecompose_indication_id"));
			Integer graduateIndexNum = temp.getInt("graduateIndexNum");
			String graduateContent = temp.getStr("graduateContent");
			Integer indicationIndexNum = temp.getInt("indicationIndexNum");
			String indicationContent = temp.getStr("indicationContent");


			if(indicationMap.get(indicationId) == null){
				Map<String, Object> indicationDetailMap = new HashMap<>();
				indicationDetailMap.put("graduateIndexNum", graduateIndexNum);
				indicationDetailMap.put("graduateContent", graduateContent);
				indicationDetailMap.put("indicationIndexNum", indicationIndexNum);
				indicationDetailMap.put("indicationContent", indicationContent);

				indicationMap.put(indicationId, indicationDetailMap);
			}

			if(gradecomposeAndIndicationMap.get(gradecomposeIndicationId) == null){
				gradecomposeAndIndicationMap.put(gradecomposeIndicationId, indicationId);
			}

			Map<String, Object> tempMap = new HashMap<>();
			tempMap.put("gradecomposeIndicationId",gradecomposeIndicationId);
			tempMap.put("indicationId", indicationId);

			List<Map<String, Object>> courseGradecomposeIndications = courseGradecomposeIndicationMap.get(courseGradecomposeId);
			if(courseGradecomposeIndications == null){
				courseGradecomposeIndications = new ArrayList<>();
				courseGradecomposeIndicationMap.put(courseGradecomposeId, courseGradecomposeIndications);
			}
			courseGradecomposeIndications.add(tempMap);

			BigDecimal averageScore = averageScoreMap.get(gradecomposeIndicationId);
			if(indexMap.get(indicationId) == null){
				Map<String, Object> map = new HashMap<>();
				List<Map<String, Object>> gradecomposeIndications = Lists.newArrayList();
				Map<String, Object> gradecomposeIndicationMap = new HashMap<>();
				gradecomposeIndicationMap.put("gradecomposeName", gradecomposeName);
				gradecomposeIndicationMap.put("maxScore", maxScore);
				gradecomposeIndicationMap.put("weight", weight);
				gradecomposeIndicationMap.put("afterCalculateMaxScore", maxScore == null ? null : maxScore.multiply(weight));
				gradecomposeIndicationMap.put("averageScore", averageScore);
				if(averageScore != null){
					gradecomposeIndicationMap.put("afterCalculateAverageScore", averageScore.multiply(weight));
				}else{
					gradecomposeIndicationMap.put("afterCalculateAverageScore", null);
				}

				gradecomposeIndications.add(gradecomposeIndicationMap);

				map.put("indicationId", indicationId);
				map.put("gradecomposeIndications", gradecomposeIndications);

				ccCourseGradecomposeIndications.add(map);
				temp.put("index", ccCourseGradecomposeIndications.size()-1);
				indexMap.put(indicationId, temp);
			}else{
				Map<String, Object> map = ccCourseGradecomposeIndications.get(indexMap.get(indicationId).getInt("index"));
				List<Map<String, Object>> gradecomposeIndications = ConvertUtils.convert(map.get("gradecomposeIndications"), List.class) ;
				Map<String, Object> gradecomposeIndicationMap = new HashMap<>();
				gradecomposeIndicationMap.put("gradecomposeName", gradecomposeName);
				gradecomposeIndicationMap.put("maxScore", maxScore);
				gradecomposeIndicationMap.put("weight", weight);
				gradecomposeIndicationMap.put("afterCalculateMaxScore", maxScore.multiply(weight));
				gradecomposeIndicationMap.put("averageScore", averageScore);
				if(averageScore != null){
					gradecomposeIndicationMap.put("afterCalculateAverageScore", averageScore.multiply(weight));
				}else{
					gradecomposeIndicationMap.put("afterCalculateAverageScore", null);
				}
				gradecomposeIndications.add(gradecomposeIndicationMap);
				map.put("gradecomposeIndications", gradecomposeIndications);
			}
		}

		//开课课程下分数范围备注
		List<CcGradecomposeIndicationScoreRemark> ccGradecomposeIndicationScoreRemarks = CcGradecomposeIndicationScoreRemark.dao.findByTeacherCourseId(id);

		Map<String, List<String>> courseGradecomposeMap = new HashMap<>();
		Map<String, List<Map<String, Object>>> scoreRemarkMap = new HashMap<>();
		for(CcGradecomposeIndicationScoreRemark temp : ccGradecomposeIndicationScoreRemarks){
			String courseGradecomposeId = String.valueOf(temp.getLong("courseGradecomposeId"));
			String gradecomposeIndicationId = String.valueOf(temp.getLong("gradecompose_indication_id"));
			String scoreSection = temp.getStr("score_section");
			String scoreRemark =  temp.getStr("score_remark");

			Map<String, Object> scoreMap = new HashMap<>();
			scoreMap.put("scoreSection", scoreSection);
			scoreMap.put("scoreRemark", scoreRemark);

			List<Map<String, Object>> scoreRemarks = scoreRemarkMap.get(gradecomposeIndicationId);
			if(scoreRemarks == null){
				scoreRemarks = new ArrayList<>();
				scoreRemarkMap.put(gradecomposeIndicationId, scoreRemarks);
			}
			scoreRemarks.add(scoreMap);

			List<String> gradecomposeIndicationIds = courseGradecomposeMap.get(courseGradecomposeId);
			if(gradecomposeIndicationIds == null){
				gradecomposeIndicationIds = new ArrayList<>();
				courseGradecomposeMap.put(courseGradecomposeId, gradecomposeIndicationIds);
			}
			if(!gradecomposeIndicationIds.contains(gradecomposeIndicationId)){
				gradecomposeIndicationIds.add(gradecomposeIndicationId);
			}
		}

		//key为开课课程成绩组成，value为同一开课课程下不同指标点的分数范围和备注
		Map<String, List<Map<String, List<Map<String, Object>>>>> scoreSectionAndRemarkMap = new HashMap<>();
		for(Map.Entry<String, List<String>> entry : courseGradecomposeMap.entrySet()){
			String courseGradecomposeId = entry.getKey();
			List<String> gradecomposeIndicationIds = entry.getValue();
			List<Map<String, List<Map<String, Object>>>> gradecomposeIndicationIdLists = new ArrayList<>();
			for(String gradecomposeIndicationId : gradecomposeIndicationIds){
				Map<String, List<Map<String, Object>>> temp = new HashMap<>();
				temp.put(gradecomposeIndicationId, scoreRemarkMap.get(gradecomposeIndicationId));
				gradecomposeIndicationIdLists.add(temp);
			}
			scoreSectionAndRemarkMap.put(courseGradecomposeId, gradecomposeIndicationIdLists);
		}


		//开课课程下的成绩组成
		List<Map<String, Object>> gradecomposes = Lists.newArrayList();
		List<CcCourseGradecompose> courseGradecomposeLists = CcCourseGradecompose.dao.findByTeacherCourseId(id);
		for(CcCourseGradecompose ccCourseGradecompose : courseGradecomposeLists){
			Map<String, Object> gradecomposeMap = new HashMap<>();
			gradecomposeMap.put("gradecomposeId", String.valueOf(ccCourseGradecompose.getLong("gradecomposeId")));
			gradecomposeMap.put("gradecomposeName", ccCourseGradecompose.getStr("gradecomposeName"));
			gradecomposes.add(gradecomposeMap);
		}

		result.put("gradecomposes", gradecomposes);
		result.put("courseGradecomposeIndicationMap", courseGradecomposeIndicationMap);
		result.put("averageScoreMap", averageScoreMap);
		result.put("educlassNames", educlassNames);
		result.put("studentScoreMap", studentScoreMap);
		result.put("scoreSectionAndRemarkMap", scoreSectionAndRemarkMap);
		result.put("gradecomposeAndIndicationMap", gradecomposeAndIndicationMap);
		result.put("indicationMap", indicationMap);
		result.put("ccCourseGradecomposeIndications", ccCourseGradecomposeIndications);
		result.put("ccCourseGradecomposes", ccCourseGradecomposes);
		result.put("startYear", ccTeacherCourse.getInt("start_year"));
		result.put("endYear", ccTeacherCourse.getInt("end_year"));
		result.put("courseName", ccTeacherCourse.getStr("course_name"));
		result.put("grade", ccTeacherCourse.getInt("grade"));
		result.put("courseType", DictUtils.findLabelByTypeAndKey("courseType", ccTeacherCourse.getInt("type")));
		result.put("hierarchyName", ccTeacherCourse.getStr("hierarchyName"));
		result.put("moduleName", ccTeacherCourse.getStr("moduleName"));

		return renderSUC(result, response, header);
	}

}
