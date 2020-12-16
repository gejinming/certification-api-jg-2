package com.gnet.certification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcEduclassStudentStudy;
import com.gnet.model.admin.CcStudentEvalute;
import com.gnet.utils.DictUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 考评点成绩获取接口
 * 
 * @author sll
 *
 */
@Service("EM00367")
@Transactional(readOnly=true)
public class EM00367 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> params = request.getData();

		Long eduClazzId = paramsLongFilter(params.get("eduClazzId"));
		Long courseGradecomposeId = paramsLongFilter(params.get("courseGradecomposeId"));
		Long batchId = paramsLongFilter(params.get("batchId"));
		if (eduClazzId == null) {
			return renderFAIL("0500", response, header);
		}
		if (courseGradecomposeId == null) {
			return renderFAIL("0475", response, header);
		}
		/*Map<String, Object> searchParams = Maps.newHashMap();
		searchParams.put("course_gradecompose_id", courseGradecomposeId);
		searchParams.put("student_id", studentId);
		if(batchId!=null){
			searchParams.put("batch_id", batchId);
		}*/
		//根据教学班为条件获取
		//List<CcEduclassStudentStudy> educlassStudentStudies = CcEduclassStudentStudy.dao.findFilteredByColumn("class_id", eduClazzId);
		List<CcEduclassStudentStudy> educlassStudentStudies = CcEduclassStudentStudy.dao.findCourseGradecomposeStud(courseGradecomposeId, eduClazzId, batchId);
		List<Map<String, Object>> educlassStudentStudyList = new ArrayList<>();
		for(CcEduclassStudentStudy temp : educlassStudentStudies) {
			Map<String, Object> tempMap = Maps.newHashMap();
			tempMap.put("remark", temp.getInt("remark"));
			tempMap.put("studentId", temp.getLong("student_id"));
			tempMap.put("isRetake", temp.getBoolean("is_retake"));
			educlassStudentStudyList.add(tempMap);
		}
		//TODO 2020.12.9改造评分表分析法
		List<CcStudentEvalute> studentEvaluteGradeList = CcStudentEvalute.dao.findEvaluteGradeByEduClazzIdAndIndicationId(courseGradecomposeId, eduClazzId,batchId);
		
		// 返回结果
		List<Map<String, Object>> list = Lists.newArrayList();
		for (CcStudentEvalute temp: studentEvaluteGradeList){
			Map<String, Object> ccStudent = Maps.newHashMap();
			//select cs.id student_id, cess.remark , cess.is_retake, cs.student_no, cs.name student_name, cs.sex, cs.id_card,
			// cs.address,crl.id levelId,crl.level_name ,crl.level,crl.score
			ccStudent.put("id", temp.getLong("student_id"));
			ccStudent.put("studentNo", temp.getStr("student_no"));
			ccStudent.put("name", temp.getStr("student_name"));
			ccStudent.put("sex", DictUtils.findLabelByTypeAndKey("sex", temp.getInt("sex"))); 
			ccStudent.put("idCard", temp.getStr("id_card"));
			ccStudent.put("address", temp.getStr("address"));
			ccStudent.put("levelId", temp.getLong("levelId"));
			ccStudent.put("levelName", temp.getStr("level_name"));
			ccStudent.put("score", temp.getBigDecimal("score"));
			list.add(ccStudent);
		} 
		
		//得到考评点成绩列表（学生列表信息、考评点成绩信息）
		Map<String, Object> result = Maps.newHashMap();
		result.put("list", list);
		result.put("educlassStudentStudyList", educlassStudentStudyList);
		return renderSUC(result, response, header);
	}

}
