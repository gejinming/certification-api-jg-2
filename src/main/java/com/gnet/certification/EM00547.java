package com.gnet.certification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.CcGradecomposeIndicationScoreRemark;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradecomposeIndication;


/**
 * 根据开课课程成绩组成元素指标点编号获取课程下成绩组成与指标点的关系编号获取关系详细信息
 *
 * updated: 根据开课课程成绩组成元素课程目标编号获取详细信息
 * 
 * @author xzl
 * 
 * @date 2016年7月19日
 *
 */
@Service("EM00547")
@Transactional(readOnly=true)
public class EM00547 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		
		if (id == null) {
			return renderFAIL("0496", response, header);
		}
		
		CcCourseGradecomposeIndication temp = CcCourseGradecomposeIndication.dao.findInfoById(id);

		List<CcGradecomposeIndicationScoreRemark> ccGradecomposeIndicationScoreRemarks = CcGradecomposeIndicationScoreRemark.dao.findByColumn("gradecompose_indication_id", id);
        List<Map<String, Object>> scoreSectionRemarks = Lists.newArrayList();
        for(CcGradecomposeIndicationScoreRemark ccGradecomposeIndicationScoreRemark : ccGradecomposeIndicationScoreRemarks){
        	Map<String, Object> map = new HashMap<>();
        	map.put("scoreSection", ccGradecomposeIndicationScoreRemark.getStr("score_section"));
        	map.put("scoreRemark", ccGradecomposeIndicationScoreRemark.getStr("score_remark"));
			scoreSectionRemarks.add(map);
		}


		Map<String, Object> map = new HashMap<>();
		map.put("id", temp.getLong("id"));
		map.put("createDate", temp.getDate("create_date"));
		map.put("modifyDate", temp.getDate("modify_date"));
		map.put("indicationId", temp.getLong("indication_id"));
		map.put("courseGradecomposeId", temp.getLong("course_gradecompose_id"));
		map.put("indicationContent", temp.getStr("indicationContent"));
		map.put("indicationSort", temp.getInt("indicationSort"));
		map.put("gradecomposeName", temp.getStr("gradecomposeName"));
		map.put("weight", temp.getBigDecimal("weight"));
		map.put("maxScore", temp.getBigDecimal("max_score"));
		map.put("remark", temp.getStr("remark"));
		map.put("scoreSectionRemarks", scoreSectionRemarks);
		return renderSUC(map, response, header);
	}

}
