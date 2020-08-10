package com.gnet.certification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.CcIndication;
import com.gnet.model.admin.CcIndicationCourse;
import com.gnet.model.admin.CcTeacherCourse;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcEvaluteLevel;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.DictUtils;
import com.gnet.utils.SpringContextHolder;

/**
 * 增加考评点得分层次切换层次（2级或者5级）
 * 
 * @author SY
 * 
 * @date 2017年8月9日17:46:07
 *
 */
@Service("EM00396")
@Transactional(readOnly=false)
public class EM00396 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Integer level = paramsIntegerFilter(param.get("level"));
		Long teacherCourseId = paramsLongFilter(param.get("teacherCourseId"));
		
		if (level == null) {
			return renderFAIL("0434", response, header);
		}
		if (teacherCourseId == null) {
			return renderFAIL("0435", response, header);
		}

		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findFilteredById(teacherCourseId);
		if(ccTeacherCourse == null){
            return renderFAIL("0311", response, header);
		}

		List<CcIndicationCourse> ccIndicationCourses = CcIndicationCourse.dao.findFilteredByColumn("course_id", ccTeacherCourse.getLong("course_id"));
		if(ccIndicationCourses.isEmpty()){
            return renderFAIL("0436", response, header);
		}

		BigDecimal twos[] = {CcEvaluteLevel.LEVEL_TOW_A_VALUE, CcEvaluteLevel.LEVEL_TOW_B_VALUE};
		BigDecimal fives[] = {CcEvaluteLevel.LEVEL_FIVE_A_VALUE, CcEvaluteLevel.LEVEL_FIVE_B_VALUE, CcEvaluteLevel.LEVEL_FIVE_C_VALUE, CcEvaluteLevel.LEVEL_FIVE_D_VALUE, CcEvaluteLevel.LEVEL_FIVE_E_VALUE};

		Date date = new Date();
		List<CcEvaluteLevel> ccEvaluteLevels = new ArrayList<>();
		List<CcEvaluteLevel> returnList = new ArrayList<>();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);

		for(int n=0; n<ccIndicationCourses.size(); n++){
			Long indicationId = ccIndicationCourses.get(n).getLong("indication_id");
			if(CcEvaluteLevel.LEVEL_TOW.equals(level)){
				for(int i=1; i<= CcEvaluteLevel.LEVEL_TOW; i++){
					CcEvaluteLevel ccEvaluteLevel = new CcEvaluteLevel();
					ccEvaluteLevel.set("id", idGenerate.getNextValue());
					ccEvaluteLevel.set("create_date", date);
					ccEvaluteLevel.set("modify_date", date);
					ccEvaluteLevel.set("level_name", DictUtils.findLabelByTypeAndKey("evaluteLevelTow", i));
					ccEvaluteLevel.set("score", twos[i-1]);
					ccEvaluteLevel.set("teacher_course_id", teacherCourseId);
					ccEvaluteLevel.set("indication_id", indicationId);
					ccEvaluteLevel.set("level", CcEvaluteLevel.LEVEL_TOW);
					ccEvaluteLevel.set("is_del", Boolean.FALSE);
					ccEvaluteLevels.add(ccEvaluteLevel);
					if(n < 1){
						returnList.add(ccEvaluteLevel);
					}
				}
			}else if(CcEvaluteLevel.LEVEL_FIVE.equals(level)){
				for(int i=1; i<= CcEvaluteLevel.LEVEL_FIVE; i++ ){
					CcEvaluteLevel ccEvaluteLevel = new CcEvaluteLevel();
					ccEvaluteLevel.set("id", idGenerate.getNextValue());
					ccEvaluteLevel.set("create_date", date);
					ccEvaluteLevel.set("modify_date", date);
					ccEvaluteLevel.set("level_name", DictUtils.findLabelByTypeAndKey("evaluteLevelFive", i));
					ccEvaluteLevel.set("score", fives[i-1]);
					ccEvaluteLevel.set("teacher_course_id", teacherCourseId);
					ccEvaluteLevel.set("indication_id", indicationId);
					ccEvaluteLevel.set("is_del", Boolean.FALSE);
					ccEvaluteLevel.set("level", CcEvaluteLevel.LEVEL_FIVE);
					ccEvaluteLevels.add(ccEvaluteLevel);
					if(n < 1){
						returnList.add(ccEvaluteLevel);
					}
				}
			}

		}


		// 先删除可能存在的老数据
		Map<String, Object> result = new HashMap<>();
		if(!CcEvaluteLevel.dao.deleteAllByColumn("teacher_course_id", teacherCourseId, date)){
			result.put("isSuccess", false);
		}

		// 保存新的数据
		Boolean isSuccess = CcEvaluteLevel.dao.batchSave(ccEvaluteLevels);
		result.put("isSuccess", isSuccess);
		result.put("list", returnList);
		
		return renderSUC(result, response, header);
	}
}
