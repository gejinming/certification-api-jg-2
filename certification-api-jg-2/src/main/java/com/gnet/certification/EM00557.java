package com.gnet.certification;

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
import com.gnet.model.admin.CcCourse;
import com.gnet.model.admin.CcReportCourse;
import com.gnet.model.admin.CcReportMajor;
import com.gnet.model.admin.CcScoreStuIndigrade;
import com.gnet.model.admin.CcStudentEvalute;
import com.gnet.model.admin.CcVersion;
import com.gnet.utils.PriceUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 专业达成度报表显示
 * 
 * @author wct
 * @date 2016年7月25日
 */
@Transactional(readOnly = true)
@Service("EM00557")
public class EM00557 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
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
		Long majorDirectionId = paramsLongFilter(params.get("majorDirectionId"));
		if(params.containsKey("majorDirectionId") && majorDirectionId == null) {
		    return renderFAIL("1009", response, header, "majorDirectionId的参数值非法");
		}
		// 年级不能为空过滤
		if (grade == null) {
			return renderFAIL("0521", response, header);
		}
		
		// 专业编号或版本编号必须有一个不能为空过滤
		if (majorId == null && versionId == null) {
			return renderFAIL("0524", response, header);
		}
		
		// 获得该年级使用的专业认证版本
		if (versionId == null) {
			versionId = CcVersion.dao.findNewestVersion(majorId, grade);
		}
		
		// 专业编号未找到一个版本
		if (versionId == null) {
			return renderSUC(new HashMap<String, Object>(), response, header);
		}
		
		// 毕业要求信息
		List<Map<String, Object>> graduateInfos = findGraduateInfos(grade, versionId, majorDirectionId);
		// 课程统计报表最新统计日期（专业达成度数据与课程达成度的统计时间相同）
//		Date statisticsDate = null;
//		CcReportCourse ccReportCourse = CcReportCourse.dao.getNewStatisticsDateRecord(grade);
//		if (ccReportCourse != null) {
//			statisticsDate = ccReportCourse.getDate("statistics_date");
//		}
//
//		// 是否需要更新信息
//		boolean needUpdate = needToUpdate(grade, versionId);
		
		// 返回结果
		Map<String, Object> result = Maps.newHashMap();
		result.put("graduateInfos", graduateInfos);
//		result.put("statisticsDate", statisticsDate);
//		result.put("needUpdate", needUpdate);
		return renderSUC(result, response, header);
	}
	
	/**
	 * 获得毕业要求信息
	 * 
	 * @param grade 年级
	 * @param versionId 版本编号
	 * @param majorDirectionId 专业方向编号
	 * @return
	 */
	private List<Map<String, Object>> findGraduateInfos(Integer grade, Long versionId, Long majorDirectionId) {
		List<CcReportMajor> ccReportMajors = CcReportMajor.dao.findAllByGradeAndVersion(grade, versionId, majorDirectionId);
		Map<Long, CcReportMajor> ccReportMajorForResult = Maps.newLinkedHashMap();
		Map<Long, Object> ccReportMajorIsMajorDirection = Maps.newHashMap();
		List<Map<String, Object>> result = Lists.newArrayList();
		// 遍历获得毕业要求返回结果
		for (CcReportMajor ccReportMajor : ccReportMajors) {
			Long graduateId = ccReportMajor.getLong("graduate_id");
			
			CcReportMajor prevCcReportMajor = null;
			if (ccReportMajorForResult.get(graduateId) == null) {
				ccReportMajorForResult.put(graduateId, ccReportMajor);
				continue;
			} else {
				prevCcReportMajor = ccReportMajorForResult.get(graduateId);
			}
			
			// 有方向时优先考虑方向，考虑完方向或者无方向则取最小的值
			Boolean isDirection = majorDirectionId != null && majorDirectionId.equals(ccReportMajor.getLong("major_direction_id"));
			if (isDirection) {
				if (ccReportMajorIsMajorDirection.get(graduateId) == null) {
					ccReportMajorForResult.put(graduateId, ccReportMajor);
					ccReportMajorIsMajorDirection.put(graduateId, new Object());
				} else {
					if (PriceUtils.greaterThan(prevCcReportMajor.getBigDecimal("result"), ccReportMajor.getBigDecimal("result"))) {
						ccReportMajorForResult.put(graduateId, ccReportMajor);
					}
				}
				
			} else {
				// 若有再方向上的已加入，则不在方向上的就不进行校验
				if (ccReportMajorIsMajorDirection.get(graduateId) == null && PriceUtils.greaterThan(prevCcReportMajor.getBigDecimal("result"), ccReportMajor.getBigDecimal("result"))) {
					ccReportMajorForResult.put(graduateId, ccReportMajor);
				}
				
			}
			
		}
		
		for (CcReportMajor ccReportMajor : ccReportMajorForResult.values()) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("graduateTargetResult", ccReportMajor.getBigDecimal("target_value"));
			map.put("graduateNum", ccReportMajor.getInt("graduate_index_num"));
			map.put("graduateResult", ccReportMajor.getBigDecimal("result"));
			map.put("isComplete", !PriceUtils.lessThan(ccReportMajor.getBigDecimal("result"), ccReportMajor.getBigDecimal("target_value")));
			result.add(map);
		}
		
		return result;
	}
	
	/**
	 * 判断课程记录表是否需要更新(专业达成度数据与课程达成度的统计时间相同)
	 * 
	 * @param grade 年级
	 * @param versionId 培养计划版本编号
	 * @return
	 */
	private boolean needToUpdate(Integer grade, Long versionId) {
		return CcScoreStuIndigrade.dao.isNeedToUpdateByVersionAndGrade(versionId, grade) 
				|| CcStudentEvalute.dao.isNeedToUpdateByVersionAndGrade(versionId, grade)
				|| CcCourse.dao.isNeedToUpdateByVersionAndGrade(versionId, grade);
				
	}

}
