package com.gnet.certification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourse;
import com.google.common.collect.Maps;

/**
 * 课程加入课程组指标点权重验证接口
 * 
 * @author wct
 * @date 2016年10月26日
 */
@Transactional(readOnly = true)
@Service("EM00626")
public class EM00626 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		List<Long> courseIds = paramsJSONArrayFilter(params.get("courseIds"), Long.class);
		Long targetCourseId = paramsLongFilter(params.get("targetCourseId"));
		// 目标课程不能为空
		if (targetCourseId == null) {
			return renderFAIL("0568", response, header);
		}
		
		Map<String, Object> result = Maps.newHashMap();
		// 已通过审核的课程编号为空时，直接判断为通过
		if (courseIds.isEmpty()) {
			result.put("isSame", Boolean.TRUE);
			return renderSUC(result, response, header);
		}
		
		List<CcCourse> targetCourses = CcCourse.dao.findByCourseIdsWithIndication(new Long[]{targetCourseId});
		// 目标课程还未关联任何指标点
		if (targetCourses.isEmpty()) {
			result.put("isSame", Boolean.TRUE);
			return renderSUC(result, response, header);
		}
		
		// 记录已存在课程的指标点和权重映射关系
		List<CcCourse> ccCourses = CcCourse.dao.findByCourseIdsWithIndication(courseIds.toArray(new Long[courseIds.size()]));
		Map<Long, BigDecimal> indicationWeightMap = Maps.newHashMap();
		for (CcCourse ccCourse : ccCourses) {
			Long indicationId = ccCourse.getLong("indication_id");
			// 指标点权重
			if (indicationWeightMap.get(indicationId) == null) {
				indicationWeightMap.put(indicationId, ccCourse.getBigDecimal("weight"));
			}
		}
		
		// 验证权重是否相同，若不同返回错误信息
		for (CcCourse ccCourse : targetCourses) {
			Long indicationId = ccCourse.getLong("indication_id");
			if (indicationWeightMap.get(indicationId) != null && !ccCourse.getBigDecimal("weight").equals(indicationWeightMap.get(indicationId))) {
				return renderFAIL("0571", response, header);
			}
		}
		
		result.put("isSame", Boolean.TRUE);
		return renderSUC(result, response, header);
	}

}
