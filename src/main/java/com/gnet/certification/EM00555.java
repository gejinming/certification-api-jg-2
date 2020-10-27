package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获得显示课程达成度报表数据
 *
 * @author xzl
 * @date 2017年11月20日
 */
@Service("EM00555")
public class EM00555 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Integer grade = paramsIntegerFilter(params.get("grade"));
		Long versionId = paramsLongFilter(params.get("versionId"));
		if(params.containsKey("versionId") && versionId == null) {
			return renderFAIL("1009", response, header, "versionId的参数值非法");
		}
		Long majorId = paramsLongFilter(params.get("majorId"));
		if(params.containsKey("majorId") && majorId == null) {
			return renderFAIL("1009", response, header, "majorId的参数值非法");
		}
		Long graduateId = paramsLongFilter(params.get("graduateId"));
		if(params.containsKey("graduateId") && graduateId == null) {
			return renderFAIL("1009", response, header, "graduateId的参数值非法");
		}
		Long majorDirectionId = paramsLongFilter(params.get("majorDirectionId"));
		if(params.containsKey("majorDirectionId") && majorDirectionId == null) {
			return renderFAIL("1009", response, header, "majorDirectionId的参数值非法");
		}
		// 是否剔除部分学生的
		Boolean isCaculate = paramsBooleanFilter(params.get("isCaculate"));
		// 默认是的
		isCaculate = isCaculate == null ? Boolean.TRUE : isCaculate;
		// 年级为空过滤
		if (grade == null) {
			return renderFAIL("0521", response, header);
		}

		// 专业编号或版本编号必须有一个不能为空过滤
		if (majorId == null && versionId == null) {
			return renderFAIL("0524", response, header);
		}

		// 获得该年级使用的专业认证版本
		CcMajor ccMajor = null;
		if (versionId == null) {
			ccMajor = CcMajor.dao.findById(majorId);
			versionId = CcVersion.dao.findNewestVersion(majorId, grade);
		} else {
			ccMajor = CcMajor.dao.findByVersionId(versionId);
		}

		// 专业编号未找到一个版本
		if (versionId == null) {
			return renderSUC(new HashMap<String, Object>(), response, header);
		}

		// 获得持续改进版本所属的专业
		if (ccMajor == null) {
			return renderFAIL("0523", response, header);
		}

		// 毕业年份
		Integer graduateYear = grade + ccMajor.getInt("education_length");
		// 课程信息列表
		List<CcCourse> courseInfos = CcCourse.dao.findAllByGradeAndVersion(grade, versionId, majorDirectionId, graduateId);
		// 指标点课程信息列表
		List<Map<String, Object>> indicatioCourseInfos = findReportIndicationDetail(grade, versionId, graduateId, majorDirectionId, graduateYear, isCaculate);
//		// 课程统计报表最新统计日期
//		Date statisticsDate = null;
//		CcReportCourse ccReportCourse = CcReportCourse.dao.getNewStatisticsDateRecord(grade);
//		if (ccReportCourse != null) {
//			statisticsDate = ccReportCourse.getDate("statistics_date");
//		}
//
//		// 是否需要更新信息
		//boolean needUpdate = needToUpdate(grade, versionId);

		// 返回结果
		Map<String, Object> result = Maps.newHashMap();
		List<Map<String, Object>> returnCourseInfos = Lists.newArrayList();
		for (CcCourse ccCourse : courseInfos) {
			Map<String, Object> courseInfo = Maps.newHashMap();
			courseInfo.put("courseId", ccCourse.getLong("id"));
			courseInfo.put("courseName", ccCourse.getStr("name"));
			courseInfo.put("courseGroupId", ccCourse.getLong("course_group_id"));
			returnCourseInfos.add(courseInfo);
		}
		result.put("courseInfos", returnCourseInfos);
		result.put("indicatioCourseInfos", indicatioCourseInfos);
		//result.put("statisticsDate", statisticsDate);
		//result.put("needUpdate", needUpdate);
		return renderSUC(result, response, header);
	}

	/**
	 * 获得指标点详情，附带每门课程的成绩与权重
	 *
	 * @param grade 年级
	 * @param versionId 培养计划版本编号
	 * @param isCaculate
	 * 			是否计算
	 * @return
	 */
	private List<Map<String, Object>> findReportIndicationDetail(Integer grade, Long versionId, Long graduateId, Long majorDirectionId, Integer graduateYear, Boolean isCaculate) {
		List<CcReportCourse> ccReportCourses = CcReportCourse.dao.findAllByGradeAndVersion(grade, versionId, graduateId, majorDirectionId);
		
		// 如果报表记录为空返回空列表
		if (ccReportCourses.isEmpty()) {
			return Lists.newArrayList();
		}
		// 遍历课程统计表数据，组成list
		List<Map<String, Object>> result = Lists.newArrayList();
		CcReportCourse prevCcIndicationCourse = ccReportCourses.get(0);
		Long prevIndicationId = prevCcIndicationCourse.getLong("indication_id");
		Map<Long, Map<String, Object>> indicationCourseMap = Maps.newHashMap();

		for (CcReportCourse ccReportCourse : ccReportCourses) {
			Long indicationId = ccReportCourse.getLong("indication_id");

			// 新的指标点统计
			if (!indicationId.equals(prevIndicationId)) {
				// 课程组的分数
				Map<String, Object> indicationInfo = Maps.newHashMap();
				indicationInfo.put("indicationFirstNum", prevCcIndicationCourse.getInt("indication_first_num"));
				indicationInfo.put("indicationLastNum", prevCcIndicationCourse.getInt("indication_last_num"));
				if(isCaculate) {
					indicationInfo.put("indicationResult", prevCcIndicationCourse.getBigDecimal("indication_except_result"));	
				} else {
					indicationInfo.put("indicationResult", prevCcIndicationCourse.getBigDecimal("indication_result"));
				}
				indicationInfo.put("graduateYear", graduateYear);
				indicationInfo.put("targetResult", new BigDecimal(1.0));
				indicationInfo.put("courseResultInfo", indicationCourseMap);
				result.add(indicationInfo);
				// 初始化新的数据
				indicationCourseMap = Maps.newHashMap();
				prevIndicationId = indicationId;
				prevCcIndicationCourse = ccReportCourse;
			}

			Map<String, Object> indicationCourseResultMap = Maps.newHashMap();
			if(isCaculate) {
				BigDecimal resultScore = ccReportCourse.getBigDecimal("except_result");
				indicationCourseResultMap.put("result", resultScore);
			} else {
				BigDecimal resultScore = ccReportCourse.getBigDecimal("result");
				indicationCourseResultMap.put("result", resultScore);
			}
			indicationCourseResultMap.put("weight", ccReportCourse.getBigDecimal("weight"));
			indicationCourseMap.put(ccReportCourse.getLong("course_id"), indicationCourseResultMap);
		}
		// 保存最后一组记录
		Map<String, Object> indicationInfo = Maps.newHashMap();
		indicationInfo.put("indicationFirstNum", prevCcIndicationCourse.getInt("indication_first_num"));
		indicationInfo.put("indicationLastNum", prevCcIndicationCourse.getInt("indication_last_num"));
		if(isCaculate) {
			indicationInfo.put("indicationResult", prevCcIndicationCourse.getBigDecimal("indication_except_result"));	
		} else {
			indicationInfo.put("indicationResult", prevCcIndicationCourse.getBigDecimal("indication_result"));
		}
		indicationInfo.put("graduateYear", graduateYear);
		indicationInfo.put("targetResult", new BigDecimal(1.0));
		indicationInfo.put("courseResultInfo", indicationCourseMap);
		result.add(indicationInfo);

		return result;
	}

	/**
	 * 判断课程记录表是否需要更新
	 *
	 * @param grade 年级
	 * @param versionId 培养计划版本编号
	 * @return
	 *//*
	private boolean needToUpdate(Integer grade, Long versionId) {
		List<CcScoreStuIndigrade> needToUpdate = CcScoreStuIndigrade.dao.findNeedToUpdate(versionId, grade);
		if (needToUpdate.size()>0){
			return true;
		}
		return false;
	}*/

}
