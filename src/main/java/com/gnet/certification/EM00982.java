package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.gnet.response.ServiceResponse;
import com.gnet.service.CcCourseGradecomposeDetailService;
import com.gnet.service.CcEduindicationStuScoreService;
import com.gnet.utils.ConvertUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.log.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.*;


/**
 * 题目成绩批量保存接口
 * 
 * @author xzl
 * @Date 2018年2月12日17:48:00
 */
@Transactional(readOnly = false)
@Service("EM00982")
public class EM00982 extends BaseApi implements IApi {
	
	private static final Logger logger = Logger.getLogger(EM00982.class);

	@Autowired
	private CcCourseGradecomposeDetailService ccCourseGradecomposeDetailService;
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		List<Map<String, Object>> subjects = ConvertUtils.convert(param.get("subjects"), List.class);
		Long state = paramsLongFilter(param.get("state"));
		Map<String, Object> result = Maps.newHashMap();

		// 开课课程成绩组成元素编号
		Long courseGradeComposeId = paramsLongFilter(param.get("courseGradeComposeId"));
		Long eduClassId = paramsLongFilter(param.get("eduClassId"));
		Long batchId = paramsLongFilter(param.get("batchId"));
		// 课程成绩组成为空过滤
		if (courseGradeComposeId == null) {
			return renderFAIL("0490", response, header);
		}

		// 教学班编号为空过滤
		if (eduClassId == null) {
			return renderFAIL("0500", response, header);
		}

		if(subjects.isEmpty()){
            return renderFAIL("2107", response, header);
        }

		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findByCourseGradeComposeId(courseGradeComposeId);
		if(ccTeacherCourse == null){
			return renderFAIL("0501", response, header);
		}

		CcEduclass ccEduclass = CcEduclass.dao.findFilteredById(eduClassId);
		if(ccEduclass == null){
			return renderFAIL("0381",response, header);
		}

		// 判断录入成绩类型是否是由题目明细计算获得,1:指标点成绩直接输入,2:由题目明细计算获得
		if(!CcCourseGradecompose.SUMMARY_INPUT_SCORE.equals(ccTeacherCourse.getInt("input_score_type")) && !CcCourseGradecompose.SUMMARY_MANYINPUT_SCORE.equals(ccTeacherCourse.getInt("input_score_type"))){
			return renderFAIL("2102", response, header);
		}

		List<CcStudent> studentList = CcStudent.dao.findByEduclassIdOrderByStudentNo(eduClassId);
		if(studentList.isEmpty()){
			return renderFAIL("2104", response, header);
		}

		//List<CcCourseGradeComposeDetail> ccCourseGradeComposeDetails = CcCourseGradeComposeDetail.dao.findFilteredByColumn("course_gradecompose_id", courseGradeComposeId);
		List<CcCourseGradeComposeDetail> ccCourseGradeComposeDetails = CcCourseGradeComposeDetail.dao.topicList0(courseGradeComposeId, batchId);
		if(ccCourseGradeComposeDetails.isEmpty()){
			return renderFAIL("2105", response, header);
		}

		//题目key题号(分数)value编号map
		Map<String, Long> subjectMap = new HashMap<>();
		Map<String, BigDecimal> subjectScoreMap = new HashMap<>();
		for(CcCourseGradeComposeDetail ccCourseGradeComposeDetail : ccCourseGradeComposeDetails){
			String detail = String.format("%s(%s)", ccCourseGradeComposeDetail.getStr("name"), ccCourseGradeComposeDetail.getBigDecimal("score"));
			subjectMap.put(detail, ccCourseGradeComposeDetail.getLong("id"));
			subjectScoreMap.put(detail, ccCourseGradeComposeDetail.getBigDecimal("score"));
		}

		Set<Long> studentIds = new HashSet<>();
		Set<Long> detailIds = new HashSet<>();

		//需要新增的学生题目分数
		List<CcCourseGradecomposeStudetail> addList = Lists.newArrayList();
		//需要更新的学生题目分数
		List<CcCourseGradecomposeStudetail> editList = Lists.newArrayList();

		if(!ccCourseGradecomposeDetailService.validateImportSubjectScore(subjectMap, studentIds, detailIds, addList, editList, subjects, eduClassId, courseGradeComposeId, studentList, ccEduclass, subjectScoreMap,batchId)){
			return renderFAIL("0953", response, header);
		}

		if(addList.isEmpty() && editList.isEmpty()){
			return renderFAIL("2108", response, header);
		}

		if(!addList.isEmpty() && !CcCourseGradecomposeStudetail.dao.batchSave(addList)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

        if(!editList.isEmpty() && !CcCourseGradecomposeStudetail.dao.batchUpdate(editList, "modify_date, score")){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		List<CcCourseGradecomposeIndication> courseGradecomposeIndicationList = CcCourseGradecomposeIndication.dao.findByDetailIdAndCourseGradecomposeId(courseGradeComposeId, detailIds.toArray(new Long[detailIds.size()]));
		//批量导入时可能存在重复
		Set<Long> courseGradecomposeIndicationIds = new HashSet<>();
		if(!courseGradecomposeIndicationList.isEmpty()){
			for(int i=0; i<courseGradecomposeIndicationList.size(); i++ ){
				courseGradecomposeIndicationIds.add(courseGradecomposeIndicationList.get(i).getLong("id"));
			}
		}

		CcCourseGradecomposeDetailService courseGradecomposeDetailService = SpringContextHolder.getBean(CcCourseGradecomposeDetailService.class);
		if(!courseGradecomposeDetailService.batchUpdateStudentGrade(courseGradecomposeIndicationIds.toArray(new Long[courseGradecomposeIndicationIds.size()]), studentIds.toArray(new Long[studentIds.size()]), detailIds.toArray(new Long[detailIds.size()]),courseGradeComposeId)){
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		List<Long> educlassIdList = Lists.newArrayList();
		educlassIdList.add(eduClassId);

		if (!educlassIdList.isEmpty()) {

			CcEduindicationStuScoreService ccEduindicationStuScoreService = SpringContextHolder.getBean(CcEduindicationStuScoreService.class);

			if (!ccEduindicationStuScoreService.calculate(educlassIdList.toArray(new Long[educlassIdList.size()]), courseGradeComposeId)) {
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}

		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}