package com.gnet.certification;

import java.util.*;

import com.gnet.model.admin.*;
import com.gnet.response.ServiceResponse;
import com.gnet.service.CcstudentRaningLeveService;
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
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;

/**
 * 
 * 保存或更新批量的学生考评点分析法
 * 参考EM00400
 * @author SY
 * @Date 2017年10月15日
 */
@Transactional(readOnly=false)
@Service("EM00745")
public class EM00745 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		// 课程成绩组成编号列表
		List<Long> courseGradeComposeIds = paramsJSONArrayFilter(param.get("courseGradeComposeIds"), Long.class);
		Long batchId = paramsLongFilter(param.get("batchId"));
		Long eduClassId = paramsLongFilter(param.get("eduClassId"));
		// 参考接口160的上传和获取
		JSONArray ccStudentEvaluteArray = paramsJSONArrayFilter(param.get("ccStudentEvalute"));
		// 课程成绩组成为空过滤
		if (courseGradeComposeIds == null || courseGradeComposeIds.isEmpty()) {
			return renderFAIL("0490", response, header);
		}
		if (ccStudentEvaluteArray == null) {
			return renderFAIL("0480", response, header);
		}
		// 教学班编号为空过滤
		if (eduClassId == null) {
			return renderFAIL("0500", response, header);
		}
		//先把所有数据都查出来
		List<CcStudentEvalute> courseGradecomposeList1 = CcStudentEvalute.dao.findCourseGradecomposeList(courseGradeComposeIds);
		//老数据组成一个map 判断是否新增或修改 这样速度比较快
		HashMap<Object, Long> courseGradecomposeMap = new HashMap<>();
		for (CcStudentEvalute temp : courseGradecomposeList1) {
			Long id = temp.getLong("id");
			Long courseGradecomposeId0 = temp.getLong("course_gradecompose_id");
			Long batchId0 = temp.getLong("batch_id");
			Long studentId0 = temp.getLong("student_id");
			String key0="";
			if (batchId0!=null){
				key0 = courseGradecomposeId0 + studentId0 + batchId0 + "";
			}else{
				key0 = courseGradecomposeId0+studentId0+ "";
			}

			System.out.println(key0);
			courseGradecomposeMap.put(key0, id);
		}
		/*Long courseGradecompseId0 = courseGradeComposeIds.get(0);
		CcCourseGradecompose gradecompose = CcCourseGradecompose.dao.findgradomposeType(courseGradecompseId0);
		//等级制级别
		Integer hierarchyLevel = gradecompose.getInt("hierarchy_level");
		CcTeacherCourse teacherCourse = CcTeacherCourse.dao.findByCourseGradeComposeId(courseGradecompseId0);
		CcCourse ccCourse = CcCourse.dao.findCourseMajor(teacherCourse.getLong("course_id"));
		Long majorId = ccCourse.getLong("major_id");
		List<CcRankingLevel> levelList = CcRankingLevel.dao.findLevelList(majorId, hierarchyLevel);
		HashMap<String, Long> levelLists = new HashMap<>();
		for (CcRankingLevel temps: levelList){
			String levelName = temps.getStr("level_name");
			Long id = temps.getLong("id");
			levelLists.put(levelName,id);
		}*/
		Date date = new Date();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		List<CcStudentEvalute> ccStudentEvaluteEditList = new ArrayList<>();
		List<CcStudentEvalute> ccStudentEvaluteAddList = new ArrayList<>();
		for (int i = 0; i < ccStudentEvaluteArray.size(); i++) {

			JSONObject map = (JSONObject) ccStudentEvaluteArray.get(i);
			// 获取数据
			Long studentId = map.getLong("studentId");
			JSONArray courseGradecomposeList = map.getJSONArray("courseGradecomposeList");

			// 学生编号不能为空过滤
			if (studentId == null) {
				return renderFAIL("0330", response, header);
			}
			for (int j = 0; j < courseGradecomposeList.size(); j++) {
				CcStudentEvalute ccStudentEvalute = new CcStudentEvalute();
				JSONObject gradecompose = (JSONObject) courseGradecomposeList.get(j);
				Long courseGradecomposeId = gradecompose.getLong("courseGradecomposeId");
				Long batchIds = gradecompose.getLong("batchId");
				Long levelId = gradecompose.getLong("levelId");
				if (courseGradecomposeId == null) {
					return renderFAIL("0490", response, header);
				}
				// 课程考评点编号不能为空过滤
				if (levelId == null) {
					return renderFAIL("0370", response, header);
				}
				String key="";
				if (batchIds!=null){
					key = courseGradecomposeId + studentId + batchIds + "";
				}else{
					key = courseGradecomposeId + studentId+"";
				}
				ccStudentEvalute.set("modify_date", date);
				ccStudentEvalute.set("course_gradecompose_id", courseGradecomposeId);
				ccStudentEvalute.set("batch_id", batchId);
				ccStudentEvalute.set("evalute_id", levelId);
				ccStudentEvalute.set("student_id", studentId);
				ccStudentEvalute.set("is_del", Boolean.FALSE);

				if (courseGradecomposeMap.get(key) == null) {
					ccStudentEvalute.set("id", idGenerate.getNextValue());
					ccStudentEvalute.set("create_date", date);
					ccStudentEvaluteAddList.add(ccStudentEvalute);
				} else {
					ccStudentEvalute.set("id", courseGradecomposeMap.get(key));
					ccStudentEvaluteEditList.add(ccStudentEvalute);
				}

			}

		}
			
			/*Map<String, Object> searchParams = Maps.newHashMap();
			searchParams.put("evalute_id", evaluteId);
			searchParams.put("student_id", studentId);
			CcStudentEvalute ccStudentEvalute = CcStudentEvalute.dao.findFirstByColumn(searchParams, Boolean.TRUE);
			

			if (ccStudentEvalute == null) {

				ccStudentEvalute = new CcStudentEvalute();
				ccStudentEvalute.set("evalute_id", evaluteId);
				ccStudentEvalute.set("student_id", studentId);
				ccStudentEvalute.set("level_id", levelId);
				ccStudentEvalute.set("create_date", date);
				ccStudentEvalute.set("modify_date", date);
				ccStudentEvalute.set("is_del", CcStudentEvalute.DEL_NO);
				ccStudentEvalute.set("id", idGenerate.getNextValue());
				ccStudentEvaluteAddList.add(ccStudentEvalute);
			} else {
				ccStudentEvalute.set("level_id", levelId);
				ccStudentEvalute.set("modify_date", date);
				ccStudentEvaluteEditList.add(ccStudentEvalute);
			}
		}
		*/

			// 返回结果
			Map<String, Object> result = Maps.newHashMap();
			Boolean isSuccess = CcStudentEvalute.dao.batchSave(ccStudentEvaluteAddList);
			if (!isSuccess) {
				result.put("isSuccess", isSuccess);
				return renderSUC(result, response, header);
			}
			isSuccess = CcStudentEvalute.dao.batchUpdate(ccStudentEvaluteEditList, "evalute_id,modify_date");
			if (!isSuccess) {
				result.put("isSuccess", isSuccess);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return renderSUC(result, response, header);
			}
			//分散到各个课程目标中
			List<CcCourseGradecompose> gradecomposeBatch = CcCourseGradecompose.dao.findGradecomposeBatch(courseGradeComposeIds, batchId);
			for (CcCourseGradecompose temp : gradecomposeBatch){
				Long courseGradeComposeId = temp.getLong("id");
				Long batchId1 = temp.getLong("batchId");
				Integer inputScoreType = temp.getInt("input_score_type");
				CcstudentRaningLeveService cstudentRaningLeveService = SpringContextHolder.getBean(CcstudentRaningLeveService.class);
				ServiceResponse serviceResponse = cstudentRaningLeveService.mangeRaningLeveScore(courseGradeComposeId,inputScoreType,eduClassId,batchId1);
				if(!serviceResponse.isSucc()){
					return renderFAIL("0804", response, header, serviceResponse.getContent());
				}
			}

			result.put("isSuccess", isSuccess);
			return renderSUC(result, response, header);
		}

}

