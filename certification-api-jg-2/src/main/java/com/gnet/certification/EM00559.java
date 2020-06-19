
package com.gnet.certification;

import java.math.RoundingMode;
import java.util.Date;
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
import com.gnet.model.admin.CcMajor;
import com.gnet.model.admin.CcMajorDirection;
import com.gnet.model.admin.CcReportPersonalCourse;
import com.gnet.model.admin.CcReportPersonalIndication;
import com.gnet.model.admin.CcScoreStuIndigrade;
import com.gnet.model.admin.CcStudent;
import com.gnet.model.admin.CcStudentEvalute;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 个人达成度报表显示数据获取接口
 * 
 * @author wct
 * @date 2016年7月31日
 */
@Transactional(readOnly = false)
@Service("EM00559")
public class EM00559 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long versionId = paramsLongFilter(params.get("versionId"));
		Long studentId = paramsLongFilter(params.get("studentId"));
		Long graduateId = paramsLongFilter(params.get("graduateId"));
		if(params.containsKey("graduateId") && graduateId == null) {
		    return renderFAIL("1009", response, header, "graduateId的参数值非法");
		}
		Integer grade = paramsIntegerFilter(params.get("grade"));
		
		// 年级不能为空
		if (grade == null) {
			return renderFAIL("0522", response, header);
		}
		// 培养计划版本编号
		if (versionId == null) {
			return renderFAIL("0522", response, header);
		}
		// 学生编号为空过滤
		if (studentId == null) {
			return renderFAIL("0526", response, header);
		}
		CcStudent student = CcStudent.dao.findByIdWithMajorDirection(studentId, versionId);
		// 学生不存在过滤
		if (student == null) {
			return renderFAIL("0527", response, header);
		}
		CcMajor ccMajor = CcMajor.dao.findByVersionId(versionId);
		// 学制加年级获得毕业年份
		Integer graduateYear = ccMajor.getInt("education_length") + student.getInt("grade");
		// 确认专业方向
		CcMajorDirection ccMajorDirection = null;
		String majorDirectionName = null;
		Long majorDirectionId = student.getLong("major_direction_id");
		if (majorDirectionId != null) {
			ccMajorDirection = CcMajorDirection.dao.findById(majorDirectionId);
			majorDirectionName = ccMajorDirection.getStr("name");
		} else {
			ccMajorDirection = CcMajorDirection.dao.findFirstFilteredByColumn("plan_id", versionId);
			if (ccMajorDirection != null) {
				majorDirectionName = ccMajorDirection.getStr("name");
			}
			
		}
		
		// 课程列表 
		List<Map<String, Object>> courseInfos = courseInfo(versionId, majorDirectionId, graduateId);
		// 学生个人指标点编号
		List<Map<String, Object>> indicationCourseInfos = personalIndicationCourseInfo(versionId, studentId, graduateYear, majorDirectionId, graduateId);
//		// 获得最新的统计日期
//		Date statisticsDate = CcReportPersonalIndication.dao.getNewestStatisticsDate(studentId);
//		// 判断是否需要更新
//		boolean needUpdate = CcScoreStuIndigrade.dao.needToUpdateByStudentId(studentId, majorDirectionId, grade, versionId) 
//				|| CcStudentEvalute.dao.needToUpdateByStudentId(studentId, majorDirectionId, grade, versionId);
		
		// 返回结果
		Map<String, Object> result = Maps.newHashMap();
		result.put("courseInfos", courseInfos);
		result.put("indicatioCourseInfos", indicationCourseInfos);
//		result.put("statisticsDate", statisticsDate);
//		result.put("needUpdate", needUpdate);
		result.put("majorDirectionName", majorDirectionName);
		return renderSUC(result, response, header);
	}
	
	/**
	 * 获取该版本和专业方向下的课程信息
	 * 
	 * @param versionId 版本编号
	 * @param majorDirectionId 专业方向编号
	 * @return
	 */
	private List<Map<String, Object>> courseInfo(Long versionId, Long majorDirectionId, Long graduateId) {
		List<CcCourse> courses = CcCourse.dao.findByVersionAndMajorDirection(versionId, majorDirectionId, graduateId);
		List<Map<String, Object>> result = Lists.newArrayList();
		for (CcCourse course : courses) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("courseId", course.getLong("id"));
			map.put("courseName", course.getStr("name"));
			result.add(map);
		}
		
		return result;
	}
	
	/**
	 * 获取学生个人课程达成度信息
	 * 
	 * @param studentId 学生编号
	 * @param graduateYear 毕业年份
	 * @param majorDirectionId 专业方向编号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> personalIndicationCourseInfo(Long versionId, Long studentId, Integer graduateYear, Long majorDirectionId, Long graduateId) {
		List<CcReportPersonalCourse> ccReportPersonalCourses = CcReportPersonalCourse.dao.findByStudentWithIndicationDetail(versionId, studentId, majorDirectionId, graduateId);
		List<Map<String, Object>> result = Lists.newArrayList();
		// 学生个人课程达成度情况
		Long indicationId = null;
		Map<String, Object> indicationInfo = null;
		for (CcReportPersonalCourse ccReportPersonalCourse : ccReportPersonalCourses) {
			Map<Long, Object> indicationCourseInfo = null;
			if (!ccReportPersonalCourse.getLong("indication_id").equals(indicationId)) {
				indicationInfo = Maps.newHashMap();
				indicationCourseInfo = Maps.newHashMap();
				// 指标点基本信息
				indicationInfo.put("indicationFirstNum", ccReportPersonalCourse.getInt("graduate_num"));
				indicationInfo.put("indicationLastNum", ccReportPersonalCourse.getInt("indication_num"));
				indicationInfo.put("targetResult", CcReportPersonalIndication.TARGET_RESULT);
				indicationInfo.put("indicationResult", ccReportPersonalCourse.getBigDecimal("indication_result"));
				indicationInfo.put("graduateYear", graduateYear);
				indicationInfo.put("courseResultInfo", indicationCourseInfo);
				result.add(indicationInfo);
				indicationId = ccReportPersonalCourse.getLong("indication_id");
			} else {
				indicationCourseInfo = (Map<Long, Object>) indicationInfo.get("courseResultInfo");
			}
			
			Map<String, Object> courseInfoValue = Maps.newHashMapWithExpectedSize(1);
			courseInfoValue.put("result", ccReportPersonalCourse.getBigDecimal("result"));
			courseInfoValue.put("weight", ccReportPersonalCourse.getBigDecimal("indication_course_weight").setScale(1, RoundingMode.HALF_UP));
			indicationCourseInfo.put(ccReportPersonalCourse.getLong("course_id"), courseInfoValue);
		}
		
		return result;
	}

}
