package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.gnet.utils.PriceUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 教学班课程目标达成度报表
 * 
 * @author xzl
 * @Date 2017年11月28日
 */
@Service("EM00803")
public class EM00803 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
        Map<String, Object> result = new HashMap<>();

        Long eduClassId = paramsLongFilter(params.get("eduClassId"));
        if(eduClassId == null){
        	return renderFAIL("0380", response, header);
		}
		Long indicationCourseId = paramsLongFilter(params.get("indicationCourseId"));
        if(indicationCourseId == null){
        	return renderFAIL("0360", response, header);
		}

        //获取开课课程下的课程目标
        List<CcEduclass> ccEduclasses = CcEduclass.dao.findByEduclassId(eduClassId, indicationCourseId);
        if(ccEduclasses.isEmpty()){
        	result.put("courseTargets", ccEduclasses);
			return renderSUC(result, response, header);
		}

		//教学班下课程目标成绩组成学生分数
		List<CcEduindicationStuScore> ccEduindicationStuScores = CcEduindicationStuScore.dao.findByEduclassId(eduClassId);
		List<Map<String,Object>> courseTargets = new ArrayList<>();
		for(CcEduclass ccEduclass : ccEduclasses){
			Map<String, Object> map = new HashMap<>();
			Long id = ccEduclass.getLong("id");
			BigDecimal expectedValue =  ccEduclass.getBigDecimal("expected_value");
			BigDecimal achieveValue = ccEduclass.getBigDecimal("achieve_value") == null ? new BigDecimal(0) :  ccEduclass.getBigDecimal("achieve_value").divide(expectedValue, 3, RoundingMode.HALF_UP);
			map.put("id", id);
			map.put("sort", ccEduclass.getInt("sort"));
			map.put("content", ccEduclass.getStr("content"));
			map.put("expectedValue", expectedValue);
			map.put("achieveValue", PriceUtils.greaterThan(achieveValue, new BigDecimal(1)) ? new BigDecimal(1) : achieveValue);

			for(CcEduindicationStuScore ccEduindicationStuScore : ccEduindicationStuScores){
                if(id.equals(ccEduindicationStuScore.getLong("indication_id"))){
                	Map<String, Object> temp = new HashMap<>();
					temp.put("totalScore", ccEduindicationStuScore.getBigDecimal("total_score"));
					temp.put("avgScore", ccEduindicationStuScore.getBigDecimal("avg_score"));
					map.put(String.valueOf(ccEduindicationStuScore.getLong("course_gradecompose_id")), temp);
				}
			}
			courseTargets.add(map);
		}

		result.put("courseTargets", courseTargets);
		// 结果返回
		return renderSUC(result, response, header);
	}
}
