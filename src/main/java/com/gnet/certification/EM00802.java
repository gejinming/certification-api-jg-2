package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseTargetIndication;
import com.gnet.model.admin.CcIndicationCourse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 某门课程下已关联课程指标点的课程目标列表
 * 
 * @author xzl
 * @Date 2017年11月23日
 */
@Service("EM00802")
public class EM00802 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
        Map<String, Object> result = new HashMap<>();

        Long courseId = paramsLongFilter(params.get("courseId"));
        if(courseId == null){
        	return renderFAIL("0250", response, header);
		}

		List<CcCourseTargetIndication> ccCourseTargetIndications = CcCourseTargetIndication.dao.findByCourseId(courseId);
		List<Map<String,Object>> list = new ArrayList<>();
		List<CcIndicationCourse> ccIndicationCourses = CcIndicationCourse.dao.findDetailByCourseId(courseId);
        for(CcIndicationCourse ccIndicationCourse : ccIndicationCourses){
			Map<String, Object> map = new HashMap<>();
			Long indicationId = ccIndicationCourse.getLong("indicationId");
			map.put("graduateIndexNum", ccIndicationCourse.getInt("graduateIndexNum"));
			map.put("graduateContent", ccIndicationCourse.getStr("graduateContent"));
			map.put("indicationIndexNum", ccIndicationCourse.getInt("index_num"));
			map.put("indicationContent", ccIndicationCourse.getStr("indicationContent"));
			map.put("indicationId",indicationId);
			List<Map<String,Object>> courseTargets = new ArrayList<>();
			for(CcCourseTargetIndication ccCourseTargetIndication : ccCourseTargetIndications){
				if(indicationId.equals(ccCourseTargetIndication.getLong("indicationId"))){
					Map<String, Object> indicationMap = new HashMap<>();
					Long courseTargetIndicationId = ccCourseTargetIndication.getLong("courseTargetIndicationId");
					indicationMap.put("courseTargetIndicationId", courseTargetIndicationId);
					indicationMap.put("sort", ccCourseTargetIndication.getInt("sort"));
					indicationMap.put("content", ccCourseTargetIndication.getStr("content"));
					courseTargets.add(indicationMap);
				}else{
					map.put("courseTargets", courseTargets);
				}
			}
			list.add(map);
		}

		result.put("list", list);
		// 结果返回
		return renderSUC(result, response, header);
	}
}
