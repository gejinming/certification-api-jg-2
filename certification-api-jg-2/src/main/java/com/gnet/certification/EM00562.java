package com.gnet.certification;

import java.math.BigDecimal;
import java.util.Date;
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
import com.gnet.model.admin.CcCourseGradecomposeIndication;
import com.gnet.model.admin.CcLevelDetail;
import com.gnet.model.admin.CcScoreStuIndigrade;
import com.gnet.model.admin.CcTeacherCourse;
import com.gnet.service.CcEduindicationStuScoreService;
import com.gnet.utils.PriceUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;

/**
 * 
 * 保存或更新单个学生成绩
 * @author xzl
 * @Date 2016年7月11日
 */
@Transactional(readOnly=false)
@Service("EM00562")
public class EM00562 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		
		//考核成绩分析法学生指标点成绩编号
		Long scoreStuIndigradeId = paramsLongFilter(param.get("scoreStuIndigradeId"));
		// 学生编号
		Long studentId = paramsLongFilter(param.get("studentId"));
		//开课课程组成元素和课程目标关联编号
		Long gradecomposeIndicationId = paramsLongFilter(param.get("gradecomposeIndicationId"));
		Long educlassId = paramsLongFilter(param.get("educlassId"));
		Long levelDetailId = paramsLongFilter(param.get("levelDetailId"));
		BigDecimal grade = paramsBigDecimalFilter(param.get("grade"));
	    
		Integer type = CcTeacherCourse.RESULT_TYPE_SCORE;
		if(studentId == null){
			return renderFAIL("0330", response, header);
		}
		
		if(gradecomposeIndicationId == null){
			return renderFAIL("0481", response, header);
		}
		// 如果是评分表的，则设置类型和数据
		if(levelDetailId != null) {
			CcLevelDetail ccLevelDetail = CcLevelDetail.dao.findFilteredById(levelDetailId);
			if(ccLevelDetail == null) {
				return renderFAIL("0480", response, header);
			}
			grade = ccLevelDetail.getBigDecimal("value");
			type = CcTeacherCourse.RESULT_TYPE_EVALUATE;
		}else if(grade == null){
			return renderFAIL("0480", response, header);
		}

		if (educlassId == null) {
			return renderFAIL("0380", response, header);
		}

		if(PriceUtils.greaterThan(CcScoreStuIndigrade.MIN_SCORE, grade)){
			return renderFAIL("0630", response, header);
		}
		
		CcCourseGradecomposeIndication courseGradecomposeIndication = CcCourseGradecomposeIndication.dao.findFilteredById(gradecomposeIndicationId);
		if(courseGradecomposeIndication == null){
			return renderFAIL("0498", response, header);
		}
		BigDecimal maxScore = courseGradecomposeIndication.getBigDecimal("max_score");
		if(maxScore != null && PriceUtils.greaterThan(grade, maxScore)){
			return renderFAIL("0499", response, header, "超过满分值" + maxScore + "了");
		}
		
		CcScoreStuIndigrade scoreStuIndigrade =  new CcScoreStuIndigrade();
		
		Map<String, Object> result = new HashMap<String, Object>();
		Date date = new Date();
		
		//考核成绩更新
		if(scoreStuIndigradeId != null){
			scoreStuIndigrade.set("id", scoreStuIndigradeId);
			scoreStuIndigrade.set("modify_date", date);
			scoreStuIndigrade.set("type", type);
			scoreStuIndigrade.set("level_detail_id", levelDetailId);
			scoreStuIndigrade.set("grade", grade);

			if (!scoreStuIndigrade.update()) {
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
			result.put("id", scoreStuIndigradeId);
		} else {
			//已经存在的某个课程关联指点点下的学生成绩
			if(CcScoreStuIndigrade.dao.isExist(studentId, gradecomposeIndicationId)){
				return renderFAIL("0633", response, header);
			}    
		    //考核成绩保存
		    CcScoreStuIndigrade temp = new CcScoreStuIndigrade();
			temp.set("create_date", date);
			temp.set("modify_date", date);
			temp.set("type", type);
			temp.set("level_detail_id", levelDetailId);
			temp.set("gradecompose_indication_id", gradecomposeIndicationId);
			temp.set("student_id", studentId);
			temp.set("grade", grade);
			temp.set("is_del", Boolean.FALSE);
			if(!temp.save()){
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
			result.put("id", temp.getLong("id"));
		}

		List<Long> educlassIdList = Lists.newArrayList();
		educlassIdList.add(educlassId);

		if (!educlassIdList.isEmpty()) {

			CcEduindicationStuScoreService ccEduindicationStuScoreService = SpringContextHolder.getBean(CcEduindicationStuScoreService.class);

			if (!ccEduindicationStuScoreService.calculate(educlassIdList.toArray(new Long[educlassIdList.size()]), courseGradecomposeIndication.getLong("course_gradecompose_id"))) {
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}

		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}
	
}
