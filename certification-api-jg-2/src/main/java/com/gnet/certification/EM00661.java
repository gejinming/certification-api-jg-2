package com.gnet.certification;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcReportCourse;
import com.gnet.model.admin.CcScoreStuIndigrade;
import com.gnet.model.admin.CcStudentEvalute;
import com.gnet.model.admin.Office;
import com.gnet.service.OfficeService;
import com.gnet.utils.PriceUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;

/**
 * 课程达成度历史对比统计接口
 * 
 * @author wct
 * @date 2016年8月9日
 */
@Transactional(readOnly = true)
@Service("EM00661")
public class EM00661 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		String courseCode = paramsStringFilter(params.get("courseCode"));
		Long majorId = paramsLongFilter(params.get("majorId"));
		// 课程代码不能为空
		if (StrKit.isBlank(courseCode)) {
			return renderFAIL("0670", response, header);
		}
		
		if(majorId == null){
			return renderFAIL("0130", response, header);
		}
		
		// 通过token获取用户的专业编号
		OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
		String token = request.getHeader().getToken();
		Office departmentOffice = UserCacheKit.getDepartmentOffice(token);
		Long[] majorIds = officeService.getMajorIdsByOffice(departmentOffice);
		// 返回内容过滤
		if(majorIds == null || majorIds.length == 0){
             return renderFAIL("0771 ", response, header);
		}	
		
		if(!Arrays.asList(majorIds).contains(majorId)){
			return renderFAIL("0771", response, header);
		}
		
		Map<String, Object> result = getCourseCode(courseCode, majorId);
		// 是否需要更新信息
//		boolean needUpdate = needToUpdate(courseCode, majorId);
		
//		result.put("needUpdate", needUpdate);
		// 返回结果
		return renderSUC(result, response, header);
	}
	
	//判断课程各个版本的达成度是否需要更新
	private boolean needToUpdate(String courseCode, Long majorId) {
		return CcScoreStuIndigrade.dao.isNeedToUpdateByCourseCode(courseCode, majorId)
				|| CcStudentEvalute.dao.isNeedToUpdateByCourseCode(courseCode, majorId)
				|| CcReportCourse.dao.isNeedToUpdateByReportEduclassGradeAndCourseCode(courseCode, majorId)
				|| CcReportCourse.dao.isNeedToUpdateByReportEduclassEvaluteAndCourseCode(courseCode, majorId)
				|| CcReportCourse.dao.isNeedToUpdateByStudentEvaluteAndCourseCode(courseCode, majorId)
				|| CcReportCourse.dao.isNeedToUpdateByScoreStuIndigradeAndCourseCode(courseCode, majorId);
	}

	private Map<String, Object> getCourseCode(String courseCode, Long majorId) {
		List<CcReportCourse> ccReportCourses = CcReportCourse.dao.findByCourseCode(courseCode, majorId);
		Map<String, Object> result = Maps.newHashMap();
		if (ccReportCourses.isEmpty()) {
			return result;
		}
		
		// 计算评价值之和
		Map<Integer, BigDecimal[]> scoreMap = Maps.newHashMap();
		for (CcReportCourse ccReportCourse : ccReportCourses) {
			Integer grade = ccReportCourse.getInt("grade");
			if (scoreMap.get(grade) == null) {
				BigDecimal[] scoreInfo = new BigDecimal[2];
				// 第一项保存评价值和
				scoreInfo[0] = ccReportCourse.getBigDecimal("result");
				// 第二项保存权重和
				scoreInfo[1] = ccReportCourse.getBigDecimal("weight");
				scoreMap.put(grade, scoreInfo);
			} else {
				if (ccReportCourse.getBigDecimal("result") != null) {
					if (scoreMap.get(grade)[0] != null) {
						PriceUtils._add(scoreMap.get(grade)[0], ccReportCourse.getBigDecimal("result"));
					} else {
						scoreMap.get(grade)[0] = ccReportCourse.getBigDecimal("result");
					}
					
				}
			
				PriceUtils._add(scoreMap.get(grade)[1], ccReportCourse.getBigDecimal("weight"));
				
			}
			
		}
		
		// 将数据加入返回结构中
		List<BigDecimal> scoreList = Lists.newArrayList();
		List<String> category = Lists.newArrayList();
		Integer startGrade = ccReportCourses.get(0).getInt("grade");
		Integer endGrade = ccReportCourses.get(ccReportCourses.size() - 1).getInt("grade");
		for (Integer i = startGrade; i <= endGrade; i++) {
			category.add(i.toString());
			if (scoreMap.get(i) == null || scoreMap.get(i)[0] == null || PriceUtils.isZero(scoreMap.get(i)[1])) {
				scoreList.add(null);
			} else {
				scoreList.add(scoreMap.get(i)[0].divide(scoreMap.get(i)[1], 2, RoundingMode.HALF_UP));
			}
		}
		
		result.put("series", scoreList);
		result.put("category", category);
		return result;
	}

}
