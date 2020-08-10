package com.gnet.certification;

import java.math.BigDecimal;
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
import com.gnet.model.admin.CcCourseGradeComposeDetail;
import com.gnet.model.admin.CcCourseGradecompose;
import com.gnet.utils.PriceUtils;
import com.google.common.collect.Maps;

/**
 * 根据开课课程成绩组成编号获取开课课程成绩组成与指标点关系（以及满分值和剩余分数）列表
 * 
 * @author xzl
 * 
 * @date 2016年11月14日
 * 
 */
@Service("EM00549")
@Transactional(readOnly=true)
public class EM00549 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Map<String, Object> ccCourseGradecomposeIndicationsMap = Maps.newHashMap();
		
		//开课课程编号
		Long courseGradecomposeId = paramsLongFilter(param.get("courseGradecomposeId"));
		if (courseGradecomposeId == null) {
			return renderFAIL("0455", response, header);
		}
		
		CcCourseGradecompose courseGradecompose = CcCourseGradecompose.dao.findFilteredById(courseGradecomposeId);
		if(courseGradecompose == null){
			return renderFAIL("0471", response, header);
		}
		
		List<CcCourseGradeComposeDetail> courseGradeComposeDetailList = CcCourseGradeComposeDetail.dao.findByCourseGradeComposeId(courseGradecomposeId, null, null);
		
		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for(CcCourseGradeComposeDetail temp : courseGradeComposeDetailList){
			Map<String, Object> tempMap = Maps.newHashMap();
			tempMap.put("indexNum", temp.getInt("indexNum"));
			tempMap.put("graduateIndexNum", temp.getInt("graduateIndexNum"));
			tempMap.put("content", temp.getStr("content"));
			tempMap.put("maxScore", temp.getBigDecimal("max_score"));
			//如果是直接输入类型的话，返回指标点剩余分数
			if(CcCourseGradecompose.DIRECT_INPUT_SCORE.equals(courseGradecompose.getInt("input_score_type"))){
				tempMap.put("restScore", PriceUtils._sub(temp.getBigDecimal("max_score"), temp.getBigDecimal("allScore") == null ? new BigDecimal(0) : temp.getBigDecimal("allScore")));
			}
			list.add(tempMap);
		}
	
		ccCourseGradecomposeIndicationsMap.put("list", list);
		
		return renderSUC(ccCourseGradecomposeIndicationsMap, response, header);
		
	}

}
