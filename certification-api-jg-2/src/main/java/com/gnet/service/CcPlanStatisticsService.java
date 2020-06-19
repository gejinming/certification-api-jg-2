package com.gnet.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.model.admin.CcCourse;
import com.gnet.model.admin.CcPlanCourseZone;
import com.gnet.model.admin.CcPlanCourseZoneTerm;
import com.gnet.model.admin.CcPlanVersion;
import com.gnet.model.admin.CcReportBuildStatus;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.PriceUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.log.Logger;

/**
 * 培养计划创建统计接口
 * 
 * @author wct
 * @date 2016年8月4日
 */
@Transactional(readOnly = true)
@Component("ccPlanStatisticsService")
public class CcPlanStatisticsService {
	
	private static final Logger logger = Logger.getLogger(CcPlanStatisticsService.class);
	
	/**
	 * 统计培养计划报表
	 * 
	 * @param planId 培养计划编号
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean statisticsPlanReport(Long planId, Map<String, Object> msgMap) {
		List<CcCourse> ccCourses = CcCourse.dao.findByPlanWithTerm(planId);
		// 查询已存在的课程区域信息，组成Map
		Map<String, CcPlanCourseZone> historyZoneMap = Maps.newHashMap();
		List<CcPlanCourseZone> historyCcPlanCourseZones = CcPlanCourseZone.dao.findByColumn("plan_id", planId);
		for (CcPlanCourseZone ccPlanCourseZone : historyCcPlanCourseZones) {
			historyZoneMap.put(ccPlanCourseZone.getStr("zone_path"), ccPlanCourseZone);
		}
		
		// 查询已存在的课程区域学期信息，组成Map
		Map<String, Map<Long, CcPlanCourseZoneTerm>> historyZoneTermsMap = Maps.newHashMap();
		List<CcPlanCourseZoneTerm> historyCcPlanCourseZoneTerms = CcPlanCourseZoneTerm.dao.findAllByPlan(planId);
		for (CcPlanCourseZoneTerm ccPlanCourseZoneTerm : historyCcPlanCourseZoneTerms) {
			String path = ccPlanCourseZoneTerm.getStr("zone_path");
			if (historyZoneTermsMap.get(path) == null) {
				Map<Long, CcPlanCourseZoneTerm> termMap = Maps.newHashMap();
				termMap.put(ccPlanCourseZoneTerm.getLong("plan_term_id"), ccPlanCourseZoneTerm);
				historyZoneTermsMap.put(path, termMap);
			} else {
				historyZoneTermsMap.get(path).put(ccPlanCourseZoneTerm.getLong("plan_term_id"), ccPlanCourseZoneTerm);
			}
			
		}
		
		logger.info("培养计划创建统计：课程信息、已存在的数据库中的课程区域以及课程区域学期信息已查询分组完毕，开始准备更新创建课程区域以及课程区域学期的数据，并合计课程信息");
		
		Date date = new Date();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		Map<String, CcPlanCourseZone> zoneMap = Maps.newHashMap(); 
		Map<String, Map<Long, CcPlanCourseZoneTerm>> zoneTermMap = Maps.newHashMap();
		Map<String, List<CcPlanCourseZone>> zoneParentMap = Maps.newHashMap();
		List<String> zoneParentMapKeys = Lists.newArrayList();
		List<CcPlanCourseZone> newCcPlanCourseZones = Lists.newArrayList();
		List<CcPlanCourseZone> updateCcPlanCourseZones = Lists.newArrayList();
		List<CcPlanCourseZoneTerm> newCcPlanCourseZoneTerms = Lists.newArrayList();
		List<CcPlanCourseZoneTerm> updateCcPlanCourseZoneTerms = Lists.newArrayList();
		// 上一次的课程编号
		Long prevCourseId = null;
		// 上一次课程的区域路径
		String prevCourseKey = null;
		// 上一次的课程分组编号
		Long prevCourseGroupId = null;
		// 一个有方向课的区域一次只加一个方向的课程以及无方向的课程
		Map<String, Long> directionMap = Maps.newHashMap();
		for (CcCourse ccCourse : ccCourses) {
			Long courseGroupId = ccCourse.getLong("course_group_id");
			// 判断是否为同一课程组
			boolean isSameGroup = courseGroupId == null || (courseGroupId != null && !courseGroupId.equals(prevCourseGroupId));
			
			if (!ccCourse.getLong("id").equals(prevCourseId)) {
				// 依次拼接课程层次编号，次要课程层次编号，课程性质编号，次要课程性质编号，专业方向编号
				StringBuilder keyBuilder = new StringBuilder();
				
				if (CcCourse.TYPE_THEORY.equals(ccCourse.getInt("type"))) {
					Long hierarchyId = ccCourse.getLong("hierarchy_id");
					Long hierarchySecondaryId = ccCourse.getLong("hierarchy_secondary_id");
					Long propertyId = ccCourse.getLong("property_id");
					Long propertySecondaryId = ccCourse.getLong("property_secondary_id");
					Long directionId = ccCourse.getLong("direction_id");
					
					Long parentId = null;
					// 课程层次分区获取
					if (hierarchyId != null) {
						String key = keyBuilder.append(",").append(hierarchyId.toString()).append(",").toString();
						
						if (zoneMap.get(key) == null) {
							CcPlanCourseZone ccPlanCourseZone = null;
							// 若没有该分区则新创建分区，若存在则修改分区
							if (historyZoneMap.get(key) == null) {
								ccPlanCourseZone = new CcPlanCourseZone();
								ccPlanCourseZone.set("parent_id", CcPlanCourseZone.NOPARENTZONEID);
								ccPlanCourseZone.set("zone_id", hierarchyId);
								ccPlanCourseZone.set("plan_id", planId);
								ccPlanCourseZone.set("zone_type", CcPlanCourseZone.TYPE_HIERARCHY);
								ccPlanCourseZone.set("plan_report_type", CcPlanVersion.PLAN_REPORT_TYPE_THEORY);
								ccPlanCourseZone.set("show_sum_score", Boolean.TRUE);
								ccPlanCourseZone.set("show_least_score", Boolean.TRUE);
								ccPlanCourseZone.set("zone_path", key);
								ccPlanCourseZone.set("is_del", CcPlanCourseZone.DEL_NO);
								ccPlanCourseZone.set("create_date", date);
								ccPlanCourseZone.set("modify_date", date);
								ccPlanCourseZone.set("id", idGenerate.getNextValue());
								zoneMap.put(key, ccPlanCourseZone);
								newCcPlanCourseZones.add(ccPlanCourseZone);
							} else {
								ccPlanCourseZone = historyZoneMap.get(key);
								ccPlanCourseZone.set("all_score", new BigDecimal(0));
								ccPlanCourseZone.set("all_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_theory_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_experiment_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_practice_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_independent_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_exercises_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_dicuss_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_extracurricular_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_operate_computer_hours", new BigDecimal(0));
								ccPlanCourseZone.set("modify_date", date);
								zoneMap.put(key, ccPlanCourseZone);
								updateCcPlanCourseZones.add(ccPlanCourseZone);
								historyZoneMap.remove(key);
							}
							
							parentId = ccPlanCourseZone.getLong("id");
							zoneTermMap.put(key, new HashMap<Long, CcPlanCourseZoneTerm>());
						} else {
							parentId = zoneMap.get(key).getLong("id");
						}
						
					} else {
						String info = new StringBuilder("培养计划统计失败，理论课")
										.append(ccCourse.getStr("name"))
										.append("缺少课程层次").toString();
						
						logger.error(info);
						msgMap.put("errorMsg", info);
						return false;
					}
					
					// 次要课程层次分区获取
					if (hierarchySecondaryId != null) {
						String key = keyBuilder.append(",").append(hierarchySecondaryId.toString()).append(",").toString();
						
						if (zoneMap.get(key) == null) {
							CcPlanCourseZone ccPlanCourseZone = null;
							// 若没有该分区则新创建分区，若存在则修改分区
							if (historyZoneMap.get(key) == null) {
								ccPlanCourseZone = new CcPlanCourseZone();
								ccPlanCourseZone.set("parent_id", parentId);
								ccPlanCourseZone.set("zone_id", hierarchySecondaryId);
								ccPlanCourseZone.set("plan_id", planId);
								ccPlanCourseZone.set("zone_type", CcPlanCourseZone.TYPE_HIERARCHY_SECONDARY);
								ccPlanCourseZone.set("plan_report_type", CcPlanVersion.PLAN_REPORT_TYPE_THEORY);
								ccPlanCourseZone.set("show_sum_score", Boolean.TRUE);
								ccPlanCourseZone.set("show_least_score", Boolean.TRUE);
								ccPlanCourseZone.set("zone_path", key);
								ccPlanCourseZone.set("is_del", CcPlanCourseZone.DEL_NO);
								ccPlanCourseZone.set("create_date", date);
								ccPlanCourseZone.set("modify_date", date);
								ccPlanCourseZone.set("id", idGenerate.getNextValue());
								zoneMap.put(key, ccPlanCourseZone);
								newCcPlanCourseZones.add(ccPlanCourseZone);
							} else {
								ccPlanCourseZone = historyZoneMap.get(key);
								ccPlanCourseZone.set("all_score", new BigDecimal(0));
								ccPlanCourseZone.set("all_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_theory_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_experiment_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_practice_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_independent_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_exercises_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_dicuss_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_extracurricular_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_operate_computer_hours", new BigDecimal(0));
								ccPlanCourseZone.set("modify_date", date);
								zoneMap.put(key, ccPlanCourseZone);
								updateCcPlanCourseZones.add(ccPlanCourseZone);
								historyZoneMap.remove(key);
							}
							
							parentId = ccPlanCourseZone.getLong("id");
							zoneTermMap.put(key, new HashMap<Long, CcPlanCourseZoneTerm>());
						} else {
							parentId = zoneMap.get(key).getLong("id");
						}
					}
					
					// 课程性质分区获取
					if (propertyId != null) {
						String parentKey = keyBuilder.toString();
						String key = keyBuilder.append(",").append(propertyId.toString()).append(",").toString();
						
						if (zoneMap.get(key) == null) {
							CcPlanCourseZone ccPlanCourseZone = null;
							// 若没有该分区则新创建分区，若存在则修改分区
							if (historyZoneMap.get(key) == null) {
								ccPlanCourseZone = new CcPlanCourseZone();
								ccPlanCourseZone.set("parent_id", parentId);
								ccPlanCourseZone.set("zone_id", propertyId);
								ccPlanCourseZone.set("plan_id", planId);
								ccPlanCourseZone.set("zone_type", CcPlanCourseZone.TYPE_PROPERTY);
								ccPlanCourseZone.set("plan_report_type", CcPlanVersion.PLAN_REPORT_TYPE_THEORY);
								ccPlanCourseZone.set("show_sum_score", Boolean.TRUE);
								ccPlanCourseZone.set("show_least_score", Boolean.TRUE);
								ccPlanCourseZone.set("zone_path", key);
								ccPlanCourseZone.set("is_del", CcPlanCourseZone.DEL_NO);
								ccPlanCourseZone.set("create_date", date);
								ccPlanCourseZone.set("modify_date", date);
								ccPlanCourseZone.set("id", idGenerate.getNextValue());
								newCcPlanCourseZones.add(ccPlanCourseZone);
								zoneMap.put(key, ccPlanCourseZone);
							} else {
								ccPlanCourseZone = historyZoneMap.get(key);
								ccPlanCourseZone.set("all_score", new BigDecimal(0));
								ccPlanCourseZone.set("all_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_theory_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_experiment_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_practice_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_independent_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_exercises_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_dicuss_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_extracurricular_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_operate_computer_hours", new BigDecimal(0));
								ccPlanCourseZone.set("modify_date", date);
								zoneMap.put(key, ccPlanCourseZone);
								updateCcPlanCourseZones.add(ccPlanCourseZone);
								historyZoneMap.remove(key);
							}
							
							zoneTermMap.put(key, new HashMap<Long, CcPlanCourseZoneTerm>());
							parentId = ccPlanCourseZone.getLong("id");
							
							// 将该层加入到它的父层中
							if (zoneParentMap.get(parentKey) == null) {
								List<CcPlanCourseZone> childCcPlanCourseZones = Lists.newArrayList();
								childCcPlanCourseZones.add(ccPlanCourseZone);
								zoneParentMap.put(parentKey, childCcPlanCourseZones);
								zoneParentMapKeys.add(parentKey);
							} else {
								zoneParentMap.get(parentKey).add(ccPlanCourseZone);
							}
						} else {
							parentId = zoneMap.get(key).getLong("id");
						}
					}
					
					// 次要课程性质分区获取
					if (propertySecondaryId != null) {
						String parentKey = keyBuilder.toString();
						String key = keyBuilder.append(",").append(propertySecondaryId.toString()).append(",").toString();
						
						if (zoneMap.get(key) == null) {
							CcPlanCourseZone ccPlanCourseZone = null;
							// 若没有该分区则新创建分区，若存在则修改分区
							if (historyZoneMap.get(key) == null) {
								ccPlanCourseZone = new CcPlanCourseZone();
								ccPlanCourseZone.set("parent_id", parentId);
								ccPlanCourseZone.set("zone_id", propertySecondaryId);
								ccPlanCourseZone.set("plan_id", planId);
								ccPlanCourseZone.set("zone_type", CcPlanCourseZone.TYPE_PROPERTY_SECONDARY);
								ccPlanCourseZone.set("plan_report_type", CcPlanVersion.PLAN_REPORT_TYPE_THEORY);
								ccPlanCourseZone.set("show_sum_score", Boolean.TRUE);
								ccPlanCourseZone.set("show_least_score", Boolean.TRUE);
								ccPlanCourseZone.set("zone_path", key);
								ccPlanCourseZone.set("is_del", CcPlanCourseZone.DEL_NO);
								ccPlanCourseZone.set("create_date", date);
								ccPlanCourseZone.set("modify_date", date);
								ccPlanCourseZone.set("id", idGenerate.getNextValue());
								newCcPlanCourseZones.add(ccPlanCourseZone);
								zoneMap.put(key, ccPlanCourseZone);
							} else {
								ccPlanCourseZone = historyZoneMap.get(key);
								ccPlanCourseZone.set("all_score", new BigDecimal(0));
								ccPlanCourseZone.set("all_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_theory_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_experiment_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_practice_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_independent_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_exercises_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_dicuss_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_extracurricular_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_operate_computer_hours", new BigDecimal(0));
								ccPlanCourseZone.set("modify_date", date);
								zoneMap.put(key, ccPlanCourseZone);
								updateCcPlanCourseZones.add(ccPlanCourseZone);
								historyZoneMap.remove(key);
							}
							
							zoneTermMap.put(key, new HashMap<Long, CcPlanCourseZoneTerm>());
							parentId = ccPlanCourseZone.getLong("id");
							
							// 将该层加入到它的父层中
							if (zoneParentMap.get(parentKey) == null) {
								List<CcPlanCourseZone> childCcPlanCourseZones = Lists.newArrayList();
								childCcPlanCourseZones.add(ccPlanCourseZone);
								zoneParentMap.put(parentKey, childCcPlanCourseZones);
								zoneParentMapKeys.add(parentKey);
							} else {
								zoneParentMap.get(parentKey).add(ccPlanCourseZone);
							}
						} else {
							parentId = zoneMap.get(key).getLong("id");
						}
					}
					
					// 专业方向分区获取
					if (directionId != null) {
						String parentKey = keyBuilder.toString();
						String key = keyBuilder.append(",").append(directionId.toString()).append(",").toString();
						CcPlanCourseZone ccPlanCourseZone = null;
						
						if (zoneMap.get(key) == null) {
							// 若没有该分区则新创建分区，若存在则修改分区
							if (historyZoneMap.get(key) == null) {
								ccPlanCourseZone = new CcPlanCourseZone();
								ccPlanCourseZone.set("parent_id", parentId);
								ccPlanCourseZone.set("zone_id", directionId);
								ccPlanCourseZone.set("plan_id", planId);
								ccPlanCourseZone.set("zone_type", CcPlanCourseZone.TYPE_DIRECTION);
								ccPlanCourseZone.set("plan_report_type", CcPlanVersion.PLAN_REPORT_TYPE_THEORY);
								ccPlanCourseZone.set("show_sum_score", Boolean.TRUE);
								ccPlanCourseZone.set("show_least_score", Boolean.TRUE);
								ccPlanCourseZone.set("zone_path", key);
								ccPlanCourseZone.set("is_del", CcPlanCourseZone.DEL_NO);
								ccPlanCourseZone.set("create_date", date);
								ccPlanCourseZone.set("modify_date", date);
								ccPlanCourseZone.set("id", idGenerate.getNextValue());
								newCcPlanCourseZones.add(ccPlanCourseZone);
								zoneMap.put(key, ccPlanCourseZone);
							} else {
								ccPlanCourseZone = historyZoneMap.get(key);
								ccPlanCourseZone.set("all_score", new BigDecimal(0));
								ccPlanCourseZone.set("all_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_theory_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_experiment_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_practice_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_independent_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_exercises_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_dicuss_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_extracurricular_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_operate_computer_hours", new BigDecimal(0));
								ccPlanCourseZone.set("modify_date", date);
								zoneMap.put(key, ccPlanCourseZone);
								updateCcPlanCourseZones.add(ccPlanCourseZone);
								historyZoneMap.remove(key);
							}
							
							zoneTermMap.put(key, new HashMap<Long, CcPlanCourseZoneTerm>());
							
							// 将该层加入到它的父层中
							if (zoneParentMap.get(parentKey) == null) {
								List<CcPlanCourseZone> childCcPlanCourseZones = Lists.newArrayList();
								childCcPlanCourseZones.add(ccPlanCourseZone);
								zoneParentMap.put(parentKey, childCcPlanCourseZones);
								zoneParentMapKeys.add(parentKey);
							} else {
								zoneParentMap.get(parentKey).add(ccPlanCourseZone);
							}
							
						} else {
							ccPlanCourseZone = zoneMap.get(key);
						}
						
						// 标记区域为含方向课的区域
						if (directionMap.get(key) == null) {
							directionMap.put(key, directionId);
							// 预置为专业方向区域
							ccPlanCourseZone.put("is_direction_zone", Boolean.TRUE);
						}
					}
					
				} else if (CcCourse.TYPE_PRACTICE.equals(ccCourse.getInt("type"))) {
					Long moduleId = ccCourse.getLong("module_id");
					Long directionId = ccCourse.getLong("direction_id");
					
					// 所属模块获取
					if (moduleId != null) {
						CcPlanCourseZone ccPlanCourseZone = null;
						String key = keyBuilder.append(",").append(moduleId.toString()).append(",").toString();
						if (zoneMap.get(key) == null) {
							// 若没有该分区则新创建分区，若存在则修改分区
							if (historyZoneMap.get(key) == null) {
								ccPlanCourseZone = new CcPlanCourseZone();
								ccPlanCourseZone.set("parent_id", CcPlanCourseZone.NOPARENTZONEID);
								ccPlanCourseZone.set("zone_id", moduleId);
								ccPlanCourseZone.set("plan_id", planId);
								ccPlanCourseZone.set("zone_type", CcPlanCourseZone.TYPE_MODULE);
								ccPlanCourseZone.set("plan_report_type", CcPlanVersion.PLAN_REPORT_TYPE_PRACTICE);
								ccPlanCourseZone.set("show_sum_score", Boolean.TRUE);
								ccPlanCourseZone.set("show_least_score", Boolean.TRUE);
								ccPlanCourseZone.set("zone_path", key);
								ccPlanCourseZone.set("is_del", CcPlanCourseZone.DEL_NO);
								ccPlanCourseZone.set("create_date", date);
								ccPlanCourseZone.set("modify_date", date);
								ccPlanCourseZone.set("id", idGenerate.getNextValue());
								newCcPlanCourseZones.add(ccPlanCourseZone);
								zoneMap.put(key, ccPlanCourseZone);
							} else {
								ccPlanCourseZone = historyZoneMap.get(key);
								ccPlanCourseZone.set("all_score", new BigDecimal(0));
								ccPlanCourseZone.set("all_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_theory_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_experiment_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_practice_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_independent_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_exercises_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_dicuss_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_extracurricular_hours", new BigDecimal(0));
								ccPlanCourseZone.set("all_operate_computer_hours", new BigDecimal(0));
								ccPlanCourseZone.set("modify_date", date);
								zoneMap.put(key, ccPlanCourseZone);
								updateCcPlanCourseZones.add(ccPlanCourseZone);
								historyZoneMap.remove(key);
							}
							
							zoneTermMap.put(key, new HashMap<Long, CcPlanCourseZoneTerm>());
						} else {
							ccPlanCourseZone = zoneMap.get(key);
						}
						
						// 标记区域为含方向课的区域
						if (directionId != null && directionMap.get(key) == null) {
							directionMap.put(key, directionId);
							// 预置为专业方向区域
							ccPlanCourseZone.put("is_direction_zone", Boolean.TRUE);
						}
						
					} else {
						
						String info = new StringBuilder("培养计划统计失败，实践课")
								.append(ccCourse.getStr("name"))
								.append("缺少所属模块").toString();
						
						logger.error(info);
						msgMap.put("errorMsg", info);
						return false;
					}
					
					
					
				} else {
					String info = new StringBuilder("培养计划统计失败，课程")
							.append(ccCourse.getStr("name"))
							.append("的课程类型为").append(ccCourse.getInt("type").toString())
							.append("无法识别").toString();
					
					logger.error(info);
					msgMap.put("errorMsg", info);
					return false;
				}
				
				// 增加分区学期信息
				String key = keyBuilder.toString();
				Long directionId = ccCourse.getLong("direction_id");
				CcPlanCourseZone ccPlanCourseZone = zoneMap.get(key);
				// 无课程组或有课程组但和上一课程组不同时，并且无方向或是在同一方向上进行合计
				if (isSameGroup && (directionMap.get(key) == null || directionMap.get(key).equals(directionId))) {
					// 计算总学分
					if (ccCourse.getBigDecimal("credit") != null) {
						BigDecimal sumNum = ccPlanCourseZone.getBigDecimal("all_score") == null 
								? new BigDecimal(0) : ccPlanCourseZone.getBigDecimal("all_score");
								
						ccPlanCourseZone.set("all_score", PriceUtils._add(
								sumNum, ccCourse.getBigDecimal("credit")).setScale(1, RoundingMode.HALF_UP));
						
					}
					
					// 计算总学时
					if (ccCourse.getBigDecimal("all_hours") != null) {
						BigDecimal sumNum = ccPlanCourseZone.getBigDecimal("all_hours") == null 
								? new BigDecimal(0) : ccPlanCourseZone.getBigDecimal("all_hours");
								
						ccPlanCourseZone.set("all_hours", PriceUtils._add(
								sumNum, ccCourse.getBigDecimal("all_hours")).setScale(1, RoundingMode.HALF_UP));
					}
					
					// 计算理论合计总学时
					if (ccCourse.getBigDecimal("theory_hours") != null) {
						BigDecimal sumNum = ccPlanCourseZone.getBigDecimal("all_theory_hours") == null 
								? new BigDecimal(0) : ccPlanCourseZone.getBigDecimal("all_theory_hours");
						
						ccPlanCourseZone.set("all_theory_hours", PriceUtils._add(
								sumNum, ccCourse.getBigDecimal("theory_hours")).setScale(1, RoundingMode.HALF_UP));
					}
					
					// 计算实验合计总学时
					if (ccCourse.getBigDecimal("experiment_hours") != null) {
						BigDecimal sumNum = ccPlanCourseZone.getBigDecimal("all_experiment_hours") == null 
								? new BigDecimal(0) : ccPlanCourseZone.getBigDecimal("all_experiment_hours");
								
						ccPlanCourseZone.set("all_experiment_hours", PriceUtils._add(
								sumNum, ccCourse.getBigDecimal("experiment_hours")).setScale(1, RoundingMode.HALF_UP));
					}
					
					// 计算实践合计总学时
					if (ccCourse.getBigDecimal("practice_hours") != null) {
						BigDecimal sumNum = ccPlanCourseZone.getBigDecimal("all_practice_hours") == null 
								? new BigDecimal(0) : ccPlanCourseZone.getBigDecimal("all_practice_hours");
								
						ccPlanCourseZone.set("all_practice_hours", PriceUtils._add(
								sumNum, ccCourse.getBigDecimal("practice_hours")).setScale(1, RoundingMode.HALF_UP));
					}
					
					// 计算自主合计总学时
					if (ccCourse.getBigDecimal("independent_hours") != null) {
						BigDecimal sumNum = ccPlanCourseZone.getBigDecimal("all_independent_hours") == null 
								? new BigDecimal(0) : ccPlanCourseZone.getBigDecimal("all_independent_hours");
								
						ccPlanCourseZone.set("all_independent_hours", PriceUtils._add(
								sumNum, ccCourse.getBigDecimal("independent_hours")).setScale(1, RoundingMode.HALF_UP));
					}

					//计算习题合计总学时
					if (ccCourse.getBigDecimal("exercises_hours") != null) {
						BigDecimal sumNum = ccPlanCourseZone.getBigDecimal("all_exercises_hours") == null
								? new BigDecimal(0) : ccPlanCourseZone.getBigDecimal("all_exercises_hours");

						ccPlanCourseZone.set("all_exercises_hours", PriceUtils._add(
								sumNum, ccCourse.getBigDecimal("exercises_hours")).setScale(1, RoundingMode.HALF_UP));
					}

					//计算研讨合计总学时
					if (ccCourse.getBigDecimal("dicuss_hours") != null) {
						BigDecimal sumNum = ccPlanCourseZone.getBigDecimal("all_dicuss_hours") == null
								? new BigDecimal(0) : ccPlanCourseZone.getBigDecimal("all_dicuss_hours");

						ccPlanCourseZone.set("all_dicuss_hours", PriceUtils._add(
								sumNum, ccCourse.getBigDecimal("dicuss_hours")).setScale(1, RoundingMode.HALF_UP));
					}

					//计算课外合计总学时
					if (ccCourse.getBigDecimal("extracurricular_hours") != null) {
						BigDecimal sumNum = ccPlanCourseZone.getBigDecimal("all_extracurricular_hours") == null
								? new BigDecimal(0) : ccPlanCourseZone.getBigDecimal("all_extracurricular_hours");

						ccPlanCourseZone.set("all_extracurricular_hours", PriceUtils._add(
								sumNum, ccCourse.getBigDecimal("extracurricular_hours")).setScale(1, RoundingMode.HALF_UP));
					}

					//计算上机合计总学时
					if (ccCourse.getBigDecimal("operate_computer_hours") != null) {
						BigDecimal sumNum = ccPlanCourseZone.getBigDecimal("all_operate_computer_hours") == null
								? new BigDecimal(0) : ccPlanCourseZone.getBigDecimal("all_operate_computer_hours");

						ccPlanCourseZone.set("all_operate_computer_hours", PriceUtils._add(
								sumNum, ccCourse.getBigDecimal("operate_computer_hours")).setScale(1, RoundingMode.HALF_UP));
					}
					
				}
				
				// 当课程区域是被预置成方向区域但存在课程不属于该方向上时，将取消该课程的方向区域状态
				if (ccPlanCourseZone.getBoolean("is_direction_zone") != null && ccPlanCourseZone.getBoolean("is_direction_zone") 
						&& (directionId == null || !directionMap.get(key).equals(directionId))) { 
					ccPlanCourseZone.put("is_direction_zone", Boolean.FALSE);
				}
				
				prevCourseId = ccCourse.getLong("id");
				prevCourseKey = key;
			
			}
			
			// 计算学期学时合计
			Long planTermId = ccCourse.getLong("plan_term_id");
			// 一门课程必须有一个开课学期
			if (planTermId == null) {
				String info = new StringBuilder("培养计划统计失败，课程")
						.append(ccCourse.getStr("name"))
						.append("缺少一个开课学期").toString();
				
				logger.error(info);
				msgMap.put("errorMsg", info);
				return false;
			}
			
			Map<Long, CcPlanCourseZoneTerm> termMap = zoneTermMap.get(prevCourseKey);
			
			CcPlanCourseZoneTerm ccPlanCourseZoneTerm = null;
			CcPlanCourseZone ccPlanCourseZone = zoneMap.get(prevCourseKey);
			if (termMap.get(planTermId) == null) {
				if (historyZoneTermsMap.get(prevCourseKey) == null || historyZoneTermsMap.get(prevCourseKey).get(planTermId) == null) {
					ccPlanCourseZoneTerm = new CcPlanCourseZoneTerm();
					ccPlanCourseZoneTerm.set("plan_term_id", planTermId);
					ccPlanCourseZoneTerm.set("plan_course_zone_id", ccPlanCourseZone.getLong("id"));
					ccPlanCourseZoneTerm.set("all_ave_week_hours", ccCourse.getBigDecimal("week_hour"));
					ccPlanCourseZoneTerm.set("is_del", CcPlanCourseZoneTerm.DEL_NO);
					ccPlanCourseZoneTerm.set("create_date", date);
					ccPlanCourseZoneTerm.set("modify_date", date);
					ccPlanCourseZoneTerm.set("id", idGenerate.getNextValue());
					termMap.put(planTermId, ccPlanCourseZoneTerm);
					newCcPlanCourseZoneTerms.add(ccPlanCourseZoneTerm);
				} else {
					ccPlanCourseZoneTerm = historyZoneTermsMap.get(prevCourseKey).get(planTermId);
					ccPlanCourseZoneTerm.set("all_ave_week_hours", ccCourse.getBigDecimal("week_hour"));
					ccPlanCourseZoneTerm.set("modify_date", date);
					termMap.put(planTermId, ccPlanCourseZoneTerm);
					updateCcPlanCourseZoneTerms.add(ccPlanCourseZoneTerm);
					historyZoneTermsMap.get(prevCourseKey).remove(planTermId);
					if (historyZoneTermsMap.get(prevCourseKey).isEmpty()) {
						historyZoneTermsMap.remove(prevCourseKey);
					}
				}
				
			} else {
				ccPlanCourseZoneTerm = termMap.get(planTermId);
				Long directionId = ccCourse.getLong("direction_id");
				// 无课程组或有课程组但和上一课程组不同时，并且无方向或是在同一方向上进行合计
				if (isSameGroup && (directionMap.get(prevCourseKey) == null || directionMap.get(prevCourseKey).equals(directionId))) {
					ccPlanCourseZoneTerm.set("all_ave_week_hours", PriceUtils._add(ccPlanCourseZoneTerm.getBigDecimal("all_ave_week_hours"), ccCourse.getBigDecimal("week_hour")).setScale(1, RoundingMode.HALF_UP));
				}
				
			}
			
			if (isSameGroup) {
				// 课程组和上一课程组不同时进行替换
				if (courseGroupId != prevCourseGroupId) {
					prevCourseGroupId = courseGroupId;
				}
			}
			
		}
		
		logger.info("培养计划创建统计：课程区域树以及课程信息已统计完毕，准备计算父层课程区域的数据");
		// 统计父层区域的合计数据
		for (Integer i = zoneParentMapKeys.size() - 1; i >= 0; i --) {
			String key = zoneParentMapKeys.get(i);
			CcPlanCourseZone parentCcPlanCourseZone = zoneMap.get(key);
			Map<Long, CcPlanCourseZoneTerm> parentCcPlanCourseZoneTermMap = zoneTermMap.get(key);
			// 加方向区域，只允许加一个
			boolean addDirectionZoneTurn = true;
			for (CcPlanCourseZone ccPlanCourseZone : zoneParentMap.get(key)) {
				if (ccPlanCourseZone.getBoolean("is_direction_zone") != null && ccPlanCourseZone.getBoolean("is_direction_zone")) {
					if (addDirectionZoneTurn) {
						addDirectionZoneTurn = false;
					} else {
						continue;
					}
				}
				
				// 合计总学分
				if (ccPlanCourseZone.getBigDecimal("all_score") != null) {
					BigDecimal sumNum = parentCcPlanCourseZone.getBigDecimal("all_score") == null 
							? new BigDecimal(0) : parentCcPlanCourseZone.getBigDecimal("all_score");
							
					parentCcPlanCourseZone.set("all_score", PriceUtils._add(
							sumNum, ccPlanCourseZone.getBigDecimal("all_score")).setScale(1, RoundingMode.HALF_UP));
				}
				
				// 合计总学时
				if (ccPlanCourseZone.getBigDecimal("all_hours") != null) {
					BigDecimal sumNum = parentCcPlanCourseZone.getBigDecimal("all_hours") == null 
							? new BigDecimal(0) : parentCcPlanCourseZone.getBigDecimal("all_hours");
							
					parentCcPlanCourseZone.set("all_hours", PriceUtils._add(
							sumNum, ccPlanCourseZone.getBigDecimal("all_hours")).setScale(1, RoundingMode.HALF_UP));
				}
				
				// 合计理论总学时
				if (ccPlanCourseZone.getBigDecimal("all_theory_hours") != null) {
					BigDecimal sumNum = parentCcPlanCourseZone.getBigDecimal("all_theory_hours") == null 
							? new BigDecimal(0) : parentCcPlanCourseZone.getBigDecimal("all_theory_hours");
							
					parentCcPlanCourseZone.set("all_theory_hours", PriceUtils._add(
							sumNum, ccPlanCourseZone.getBigDecimal("all_theory_hours")).setScale(1, RoundingMode.HALF_UP));
				}
				
				// 合计实验总学时
				if (ccPlanCourseZone.getBigDecimal("all_experiment_hours") != null) {
					BigDecimal sumNum = parentCcPlanCourseZone.getBigDecimal("all_experiment_hours") == null 
							? new BigDecimal(0) : parentCcPlanCourseZone.getBigDecimal("all_experiment_hours");
							
					parentCcPlanCourseZone.set("all_experiment_hours", PriceUtils._add(
							sumNum, ccPlanCourseZone.getBigDecimal("all_experiment_hours")).setScale(1, RoundingMode.HALF_UP));
				}
				
				// 合计实践总学时
				if (ccPlanCourseZone.getBigDecimal("all_practice_hours") != null) {
					BigDecimal sumNum = parentCcPlanCourseZone.getBigDecimal("all_practice_hours") == null 
							? new BigDecimal(0) : parentCcPlanCourseZone.getBigDecimal("all_practice_hours");
							
					parentCcPlanCourseZone.set("all_practice_hours", PriceUtils._add(
							sumNum, ccPlanCourseZone.getBigDecimal("all_practice_hours")).setScale(1, RoundingMode.HALF_UP));
				}
				
				// 合计自主学习总学时
				if (ccPlanCourseZone.getBigDecimal("all_independent_hours") != null) {
					BigDecimal sumNum = parentCcPlanCourseZone.getBigDecimal("all_independent_hours") == null 
							? new BigDecimal(0) : parentCcPlanCourseZone.getBigDecimal("all_independent_hours");
							
					parentCcPlanCourseZone.set("all_independent_hours", PriceUtils._add(
							sumNum, ccPlanCourseZone.getBigDecimal("all_independent_hours")).setScale(1, RoundingMode.HALF_UP));
				}

				//合计习题总学时
				if (ccPlanCourseZone.getBigDecimal("all_exercises_hours") != null) {
					BigDecimal sumNum = parentCcPlanCourseZone.getBigDecimal("all_exercises_hours") == null
							? new BigDecimal(0) : parentCcPlanCourseZone.getBigDecimal("all_exercises_hours");

					parentCcPlanCourseZone.set("all_exercises_hours", PriceUtils._add(
							sumNum, ccPlanCourseZone.getBigDecimal("all_exercises_hours")).setScale(1, RoundingMode.HALF_UP));
				}

				//合计研讨总学时
				if (ccPlanCourseZone.getBigDecimal("all_dicuss_hours") != null) {
					BigDecimal sumNum = parentCcPlanCourseZone.getBigDecimal("all_dicuss_hours") == null
							? new BigDecimal(0) : parentCcPlanCourseZone.getBigDecimal("all_dicuss_hours");

					parentCcPlanCourseZone.set("all_dicuss_hours", PriceUtils._add(
							sumNum, ccPlanCourseZone.getBigDecimal("all_dicuss_hours")).setScale(1, RoundingMode.HALF_UP));
				}


				//合计课外总学时
				if (ccPlanCourseZone.getBigDecimal("all_extracurricular_hours") != null) {
					BigDecimal sumNum = parentCcPlanCourseZone.getBigDecimal("all_extracurricular_hours") == null
							? new BigDecimal(0) : parentCcPlanCourseZone.getBigDecimal("all_extracurricular_hours");

					parentCcPlanCourseZone.set("all_extracurricular_hours", PriceUtils._add(
							sumNum, ccPlanCourseZone.getBigDecimal("all_extracurricular_hours")).setScale(1, RoundingMode.HALF_UP));
				}

				//合计上机总学时
				if (ccPlanCourseZone.getBigDecimal("all_operate_computer_hours") != null) {
					BigDecimal sumNum = parentCcPlanCourseZone.getBigDecimal("all_operate_computer_hours") == null
							? new BigDecimal(0) : parentCcPlanCourseZone.getBigDecimal("all_operate_computer_hours");

					parentCcPlanCourseZone.set("all_operate_computer_hours", PriceUtils._add(
							sumNum, ccPlanCourseZone.getBigDecimal("all_operate_computer_hours")).setScale(1, RoundingMode.HALF_UP));
				}


				// 合计学期信息
				for (Map.Entry<Long, CcPlanCourseZoneTerm> termEntry : zoneTermMap.get(ccPlanCourseZone.getStr("zone_path")).entrySet()) {
					CcPlanCourseZoneTerm parentCcPlanCourseZoneTerm = null;
					if (parentCcPlanCourseZoneTermMap.get(termEntry.getKey()) == null) {
						if (historyZoneTermsMap.get(key) == null || historyZoneTermsMap.get(key).get(termEntry.getKey()) == null) {
							parentCcPlanCourseZoneTerm = new CcPlanCourseZoneTerm();
							parentCcPlanCourseZoneTerm.set("plan_term_id", termEntry.getKey());
							parentCcPlanCourseZoneTerm.set("plan_course_zone_id", parentCcPlanCourseZone.getLong("id"));
							parentCcPlanCourseZoneTerm.set("all_ave_week_hours", termEntry.getValue().getBigDecimal("all_ave_week_hours"));
							parentCcPlanCourseZoneTerm.set("is_del", CcPlanCourseZoneTerm.DEL_NO);
							parentCcPlanCourseZoneTerm.set("create_date", date);
							parentCcPlanCourseZoneTerm.set("modify_date", date);
							parentCcPlanCourseZoneTerm.set("id", idGenerate.getNextValue());
							parentCcPlanCourseZoneTermMap.put(termEntry.getKey(), parentCcPlanCourseZoneTerm);
							newCcPlanCourseZoneTerms.add(parentCcPlanCourseZoneTerm);
						} else {
							parentCcPlanCourseZoneTerm = historyZoneTermsMap.get(key).get(termEntry.getKey());
							parentCcPlanCourseZoneTerm.set("all_ave_week_hours", termEntry.getValue().getBigDecimal("all_ave_week_hours"));
							parentCcPlanCourseZoneTerm.set("modify_date", date);
							parentCcPlanCourseZoneTermMap.put(termEntry.getKey(), parentCcPlanCourseZoneTerm);
							updateCcPlanCourseZoneTerms.add(parentCcPlanCourseZoneTerm);
							historyZoneTermsMap.get(key).remove(termEntry.getKey());
							if (historyZoneTermsMap.get(key).isEmpty()) {
								historyZoneTermsMap.remove(key);
							}
						}
					} else {
						parentCcPlanCourseZoneTerm = parentCcPlanCourseZoneTermMap.get(termEntry.getKey());
						parentCcPlanCourseZoneTerm.set("all_ave_week_hours", PriceUtils._add(parentCcPlanCourseZoneTerm.getBigDecimal("all_ave_week_hours"), termEntry.getValue().getBigDecimal("all_ave_week_hours")).setScale(1, RoundingMode.HALF_UP));
					}
				}
				
			}
		}
		
		logger.info("培养计划创建统计：父层课程区域的数据计算完毕， 准备开始向数据库中保存更新删除数据");
		
		// 培养计划课程区域更新
		if (!updateCcPlanCourseZones.isEmpty() && !CcPlanCourseZone.dao.batchUpdate(updateCcPlanCourseZones, "all_score, all_hours, all_theory_hours, all_experiment_hours, all_practice_hours, all_independent_hours, modify_date, all_exercises_hours, all_dicuss_hours, all_extracurricular_hours, all_operate_computer_hours")) {
			logger.error(new StringBuilder("培养计划统计失败，课程分区表(").append(CcPlanCourseZone.dao.tableName)
					.append(")更新记录失败，更新记录有(")
					.append(updateCcPlanCourseZones.toString())
					.append(")").toString());
			
			return false;
		}
		
		// 培养计划课程区域保存
		if (!newCcPlanCourseZones.isEmpty() && !CcPlanCourseZone.dao.batchSave(newCcPlanCourseZones)) {
			logger.error(new StringBuilder("培养计划统计失败，课程分区表(").append(CcPlanCourseZone.dao.tableName)
					.append(")新增记录失败，新增记录有(")
					.append(newCcPlanCourseZones.toString())
					.append(")").toString());
			
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		
		// 培养计划课程区域学期更新
		if (!updateCcPlanCourseZoneTerms.isEmpty() && !CcPlanCourseZoneTerm.dao.batchUpdate(updateCcPlanCourseZoneTerms, "all_ave_week_hours, modify_date")) {
			logger.error(new StringBuilder("培养计划统计失败，课程分区学期表(").append(CcPlanCourseZoneTerm.dao.tableName)
					.append(")更新记录失败，更新记录有(")
					.append(updateCcPlanCourseZoneTerms.toString())
					.append(")").toString());
			
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		
		// 培养计划课程区域保存
		if (!newCcPlanCourseZoneTerms.isEmpty() && !CcPlanCourseZoneTerm.dao.batchSave(newCcPlanCourseZoneTerms)) {
			logger.error(new StringBuilder("培养计划统计失败，课程分区学期表(").append(CcPlanCourseZoneTerm.dao.tableName)
					.append(")新增记录失败，新增记录有(")
					.append(newCcPlanCourseZoneTerms.toString())
					.append(")").toString());
			
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		
		// 删除旧的课程区域学期
		List<Long> deleteZoneTermIds = Lists.newArrayList();
		for (Map.Entry<String, Map<Long, CcPlanCourseZoneTerm>> entry : historyZoneTermsMap.entrySet()) {
			for (Map.Entry<Long, CcPlanCourseZoneTerm> entryTerm : entry.getValue().entrySet()) {
				deleteZoneTermIds.add(entryTerm.getValue().getLong("id"));
			}
		}
		
		if (!deleteZoneTermIds.isEmpty() && !CcPlanCourseZoneTerm.dao.deleteAllHard(deleteZoneTermIds)) {
			logger.error(new StringBuilder("培养计划统计失败，课程分区学期表(").append(CcPlanCourseZoneTerm.dao.tableName)
					.append(")删除记录失败，删除记录编号有(")
					.append(deleteZoneTermIds.toString())
					.append(")").toString());
			
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		
		// 删除旧的课程区域
		List<Long> deleteZoneIds = Lists.newArrayList();
		for (Map.Entry<String, CcPlanCourseZone> entry : historyZoneMap.entrySet()) {
			deleteZoneIds.add(entry.getValue().getLong("id"));
		}
		
		if (!deleteZoneIds.isEmpty() && !CcPlanCourseZone.dao.deleteAllHard(deleteZoneIds)) {
			logger.error(new StringBuilder("培养计划统计失败，课程分区表(").append(CcPlanCourseZone.dao.tableName)
					.append(")删除记录失败，删除记录编号有(")
					.append(deleteZoneIds.toString())
					.append(")").toString());
			
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		
		// 记录培养计划统计时间
		CcPlanVersion ccPlanVersion = CcPlanVersion.dao.findFilteredById(planId);
		ccPlanVersion.set("modify_date", date);
		ccPlanVersion.set("build_date", date);
		if (!ccPlanVersion.update()) {
			logger.error(new StringBuilder("记录培养计划统计时间失败").toString());
			
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		
		logger.info("培养计划创建统计：向数据库中保存更新删除数据完成，培养计划创建统计结束");
		
		return true;
		
	}
	
	/**
	 * 新建报表生成任务记录
	 * 
	 * @param name 任务名称
	 * @param reportType 报表类型
	 * @param versionId 版本编号
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean createReportBuildRecord(String name, Integer reportType, Long versionId) {
		Date date = new Date();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		CcReportBuildStatus ccReportBuildStatus = new CcReportBuildStatus();
		ccReportBuildStatus.set("report_build_key", name);
		ccReportBuildStatus.set("report_build_status", CcReportBuildStatus.STATUS_TASK_UNCREATE);
		ccReportBuildStatus.set("report_type", reportType);
		ccReportBuildStatus.set("version_id", versionId);
		ccReportBuildStatus.set("is_build_finish", Boolean.FALSE);
		ccReportBuildStatus.set("create_date", date);
		ccReportBuildStatus.set("modify_date", date);
		ccReportBuildStatus.set("id", idGenerate.getNextValue());
		return ccReportBuildStatus.save();
	}
	
	
	/**
	 * 更新报表生成任务记录
	 * 
	 * @param name 任务名称
	 * @param buildType 版本编号
	 * @param reportStatus 报表任务状态
	 * @param isFinish 报表任务是否已经完成
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean updateReportBuildRecord(String name, Integer buildType, Integer reportStatus, Boolean isFinish, Long elapseTime) {
		return updateReportBuildRecord(name, buildType, reportStatus, isFinish, elapseTime, null);
	}
	
	/**
	 * 更新报表生成任务记录(带错误日志)
	 * 
	 * @param name
	 * @param buildType
	 * @param reportStatus
	 * @param isFinish
	 * @param elapseTime
	 * @param msg
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean updateReportBuildRecord(String name, Integer buildType, Integer reportStatus, Boolean isFinish, Long elapseTime, String msg) {
		Date date = new Date();
		CcReportBuildStatus ccReportBuildStatus = CcReportBuildStatus.dao.getBuildStatusRecord(buildType, name);
		if (ccReportBuildStatus == null) {
			logger.error(new StringBuilder("未找到").append(name).append("任务记录").toString());
			return false;
		}
		
		ccReportBuildStatus.set("report_build_status", reportStatus);
		ccReportBuildStatus.set("report_build_elapse_time", elapseTime);
		ccReportBuildStatus.set("report_msg", msg);
		ccReportBuildStatus.set("is_build_finish", isFinish == null ? Boolean.FALSE : isFinish);
		ccReportBuildStatus.set("modify_date", date);
		return ccReportBuildStatus.update();
	}
}
