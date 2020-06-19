package com.gnet.certification;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdk.nashorn.internal.ir.RuntimeNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourse;
import com.gnet.model.admin.CcMajor;
import com.gnet.model.admin.CcPlanCourseZone;
import com.gnet.model.admin.CcPlanTerm;
import com.gnet.model.admin.CcPlanTermCourse;
import com.gnet.model.admin.CcPlanVersion;
import com.gnet.model.admin.CcVersion;
import com.gnet.utils.DictUtils;
import com.gnet.utils.PriceUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.servlet.http.HttpServletRequest;

/**
 * 培养计划报表显示数据获得接口
 *
 * @author wct
 * @date 2016年8月5日
 */
@Transactional(readOnly = true)
@Service("EM00656")
public class EM00656 extends BaseApi implements IApi {

    @SuppressWarnings("unchecked")
    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {

        Map<String, Object> params = request.getData();
        Long planId = paramsLongFilter(params.get("planId"));
        Integer reportType = paramsIntegerFilter(params.get("reportType"));
        Long majorId = paramsLongFilter(params.get("majorId"));
        if (params.containsKey("majorId") && majorId == null) {
            return renderFAIL("1009", response, header, "majorId的参数值非法");
        }
        Integer grade = paramsIntegerFilter(params.get("grade"));
        if (params.containsKey("grade") && grade == null) {
            return renderFAIL("1009", response, header, "grade的参数值非法");
        }

        if (majorId != null && grade != null) {
            planId = CcVersion.dao.findNewestVersion(majorId, grade);
            if (planId == null) {
                return renderFAIL("0671", response, header);
            }
        }

        // 培养计划编号为空过滤
        if (planId == null) {
            return renderFAIL("0660", response, header);
        }

        // 没有报表类型时默认设置理论教学类型
        if (reportType == null) {
            reportType = CcPlanVersion.PLAN_REPORT_TYPE_THEORY;
        }

        CcMajor ccMajor = CcMajor.dao.findByVersionId(planId);
        // 专业不存在过滤
        if (ccMajor == null) {
            return renderFAIL("0661", response, header);
        }

        // 获得学期头信息
        List<Map<String, Object>> reportTermHeader = findTermList(planId, reportType);
        // 获得培养计划主要信息
        List<Map<String, Object>> reportBodyData = findReportBodyData(planId, reportType);
        // 计算报表总统计分
        Map<String, Object> reportSumAll = Maps.newHashMap();
        if (CcPlanVersion.PLAN_REPORT_TYPE_THEORY.equals(reportType)) {
            reportSumAll = getSumAllTheory(reportBodyData);
        } else if (CcPlanVersion.PLAN_REPORT_TYPE_PRACTICE.equals(reportType)) {
            List<Map<String, Object>> newReportBodyDataList = Lists.newArrayList();
            reportSumAll = getSumAllPractice(reportBodyData, newReportBodyDataList);
            reportBodyData = newReportBodyDataList;
        }

        // 是否需要更新
        boolean needUpdate = CcPlanVersion.dao.needToUpdateById(planId);
        // 返回结果
        Map<String, Object> result = Maps.newHashMap();
        result.put("reportTermHeader", reportTermHeader);
        result.put("reportBodyData", reportBodyData);
        result.put("reportSumAll", reportSumAll);
        result.put("reportType", reportType);
        result.put("reportTypeName", DictUtils.findLabelByTypeAndKey("planType", reportType));
        result.put("majorName", ccMajor.getStr("major_name"));
        result.put("statisticsDate", ccMajor.getDate("build_date"));
        result.put("needUpdate", needUpdate);
        return renderSUC(result, response, header);
    }

    /**
     * 获得培养计划学期信息
     *
     * @param planId 培养计划编号
     * @return
     */
    private List<Map<String, Object>> findTermList(Long planId, Integer reportType) {
        List<CcPlanTerm> ccPlanTerms = CcPlanTerm.dao.findAllSort(planId, reportType);
        List<Map<String, Object>> result = Lists.newArrayList();
        for (CcPlanTerm ccPlanTerm : ccPlanTerms) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("termId", ccPlanTerm.getLong("id"));
            map.put("year", ccPlanTerm.getInt("year"));
            map.put("yearName", ccPlanTerm.getStr("year_name"));
            map.put("term", ccPlanTerm.getInt("term"));
            map.put("termName", ccPlanTerm.getStr("term_name"));
            map.put("weekNums", ccPlanTerm.getInt("week_nums"));
            result.add(map);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> findReportBodyData(Long planId, Integer reportType) {
        List<CcPlanCourseZone> ccPlanCourseZones = CcPlanCourseZone.dao.findAllByPlan(planId, reportType);
        List<Map<String, Object>> result = Lists.newArrayList();
        // 课程区域为空集合时，直接返回
        if (ccPlanCourseZones.isEmpty()) {
            return result;
        }

        // 课程专用的区域map
        Map<String, Map<String, Object>> zoneMap = Maps.newHashMap();
        /*
         * 接手后由于理解不清晰，重新对它描述。2019年12月13日16:20:23 Edit By SY
         * 字段：Map<ccPlanCourseZone.id, List<Map<ccPlanCourseZone.id , Object>>
         * 说明：Map<区域表父节点Id, List<Map<当前区域表Id , 当前区域表的信息以及下属信息>>
         */
        Map<Long, Map<String, Object>> allZoneMap = Maps.newHashMap();
        Map<Long, List<Map<String, Object>>> parentZoneMap = Maps.newHashMap();
        for (CcPlanCourseZone ccPlanCourseZone : ccPlanCourseZones) {
            Long zoneId = ccPlanCourseZone.getLong("id");
            Map<String, Object> zoneInfo = null;
            if (allZoneMap.get(zoneId) == null) {
                zoneInfo = Maps.newHashMap();
                zoneInfo.put("id", ccPlanCourseZone.getLong("id"));
                zoneInfo.put("zoneType", ccPlanCourseZone.getInt("zone_type"));
                zoneInfo.put("zoneTypeName", DictUtils.findLabelByTypeAndKey("planCoursezoneType", ccPlanCourseZone.getInt("zone_type")));
                zoneInfo.put("hasChild", Boolean.FALSE);
                zoneInfo.put("showSumInfo", ccPlanCourseZone.getBoolean("show_sum_score"));
                zoneInfo.put("showLeastInfo", ccPlanCourseZone.getBoolean("show_least_score"));
                zoneInfo.put("sumGroupId", ccPlanCourseZone.getLong("sum_group_id"));
                if (CcPlanCourseZone.TYPE_HIERARCHY.equals(ccPlanCourseZone.getInt("zone_type"))) {
                    zoneInfo.put("name", ccPlanCourseZone.getStr("hierarchy_name"));
                } else if (CcPlanCourseZone.TYPE_HIERARCHY_SECONDARY.equals(ccPlanCourseZone.getInt("zone_type"))) {
                    zoneInfo.put("name", ccPlanCourseZone.getStr("hierarchy_secondary_name"));
                } else if (CcPlanCourseZone.TYPE_PROPERTY.equals(ccPlanCourseZone.getInt("zone_type"))) {
                    zoneInfo.put("name", ccPlanCourseZone.getStr("property_name"));
                } else if (CcPlanCourseZone.TYPE_PROPERTY_SECONDARY.equals(ccPlanCourseZone.getInt("zone_type"))) {
                    zoneInfo.put("name", ccPlanCourseZone.getStr("property_secondary_name"));
                } else if (CcPlanCourseZone.TYPE_DIRECTION.equals(ccPlanCourseZone.getInt("zone_type"))) {
                    zoneInfo.put("name", ccPlanCourseZone.getStr("direction_name"));
                } else if (CcPlanCourseZone.TYPE_MODULE.equals(ccPlanCourseZone.getInt("zone_type"))) {
                    zoneInfo.put("name", ccPlanCourseZone.getStr("module_name"));
                }

                // 合计信息
                Map<String, Object> sumInfo = Maps.newHashMap();
                sumInfo.put("credit", ccPlanCourseZone.getBigDecimal("all_score"));
                sumInfo.put("allHours", ccPlanCourseZone.getBigDecimal("all_hours"));
                sumInfo.put("theoryHours", ccPlanCourseZone.getBigDecimal("all_theory_hours"));
                sumInfo.put("experimentHours", ccPlanCourseZone.getBigDecimal("all_experiment_hours"));
                sumInfo.put("practiceHours", ccPlanCourseZone.getBigDecimal("all_practice_hours"));
                sumInfo.put("independentHours", ccPlanCourseZone.getBigDecimal("all_independent_hours"));
                sumInfo.put("exercisesHours", ccPlanCourseZone.getBigDecimal("all_exercises_hours"));
                sumInfo.put("dicussHours", ccPlanCourseZone.getBigDecimal("all_dicuss_hours"));
                sumInfo.put("extracurricularHours", ccPlanCourseZone.getBigDecimal("all_extracurricular_hours"));
                sumInfo.put("operateComputerHours", ccPlanCourseZone.getBigDecimal("all_operate_computer_hours"));
                sumInfo.put("haveClassTerm", new HashMap<Long, BigDecimal>());
                zoneInfo.put("sumInfo", sumInfo);
                // 至少信息
                Map<String, Object> leastInfo = Maps.newHashMap();
                leastInfo.put("credit", ccPlanCourseZone.getBigDecimal("least_score"));
                leastInfo.put("allHours", ccPlanCourseZone.getBigDecimal("least_hours"));
                leastInfo.put("theoryHours", ccPlanCourseZone.getBigDecimal("least_theory_hours"));
                leastInfo.put("experimentHours", ccPlanCourseZone.getBigDecimal("least_experiment_hours"));
                leastInfo.put("practiceHours", ccPlanCourseZone.getBigDecimal("least_practice_hours"));
                leastInfo.put("independentHours", ccPlanCourseZone.getBigDecimal("least_independent_hours"));
                leastInfo.put("exercisesHours", ccPlanCourseZone.getBigDecimal("least_exercises_hours"));
                leastInfo.put("dicussHours", ccPlanCourseZone.getBigDecimal("least_dicuss_hours"));
                leastInfo.put("extracurricularHours", ccPlanCourseZone.getBigDecimal("least_extracurricular_hours"));
                leastInfo.put("operateComputerHours", ccPlanCourseZone.getBigDecimal("least_operate_computer_hours"));
                leastInfo.put("haveClassTerm", new HashMap<Long, BigDecimal>());
                zoneInfo.put("leastInfo", leastInfo);

                // 关联到父级层编号下
                Long parentZoneId = ccPlanCourseZone.getLong("parent_id");
                if (!CcPlanCourseZone.NOPARENTZONEID.equals(parentZoneId)) {
                    if (parentZoneMap.get(parentZoneId) == null) {
                        List<Map<String, Object>> childZoneList = Lists.newArrayList();
                        childZoneList.add(zoneInfo);
                        parentZoneMap.put(parentZoneId, childZoneList);
                    } else {
                        parentZoneMap.get(parentZoneId).add(zoneInfo);
                    }

                } else {
                    // 最高父层直接加入结果集
                    result.add(zoneInfo);
                }

                zoneMap.put(ccPlanCourseZone.getStr("zone_path"), zoneInfo);
                allZoneMap.put(zoneId, zoneInfo);
            } else {
                zoneInfo = allZoneMap.get(zoneId);
            }

            // 获得并添加学期合计与至少数据
            Long termId = ccPlanCourseZone.getLong("plan_term_id");
            if (ccPlanCourseZone.getBigDecimal("all_ave_week_hours") != null) {
                ((Map<Long, BigDecimal>)
                        (((Map<String, Object>) zoneInfo.get("sumInfo"))
                                .get("haveClassTerm")))
                        .put(termId, ccPlanCourseZone.getBigDecimal("all_ave_week_hours"));

            }

            if (ccPlanCourseZone.getBigDecimal("least_ave_week_hours") != null) {
                ((Map<Long, BigDecimal>)
                        (((Map<String, Object>) zoneInfo.get("leastInfo"))
                                .get("haveClassTerm")))
                        .put(termId, ccPlanCourseZone.getBigDecimal("least_ave_week_hours"));

            }

        }

        // 遍历关联父亲层
        for (Map.Entry<Long, List<Map<String, Object>>> entry : parentZoneMap.entrySet()) {
            Map<String, Object> parentZoneInfo = allZoneMap.get(entry.getKey());
            parentZoneInfo.put("childZone", entry.getValue());
            parentZoneInfo.put("hasChild", Boolean.TRUE);
        }

        // 获取课程信息 普通的
        List<CcCourse> ccCourses = CcCourse.dao.findAllByPlanAndType(planId, reportType);
        //获取课程组的信息
        List<CcCourse> allByPlanAndType = CcCourse.dao.findAllCourseGroup(planId, reportType);
        for (CcCourse temp :allByPlanAndType){
            ccCourses.add(temp);
        }
        //获取分级教学的信息
        List<CcCourse> allCourseTeachGroup = CcCourse.dao.findAllCourseTeachGroup(planId, reportType);
        for (CcCourse temp :allCourseTeachGroup){
            ccCourses.add(temp);
        }
        Map<Long, Map<String, Object>> courseMap = Maps.newHashMap();
        Map<String, List<Map<String, Object>>> courseGroupMap = Maps.newHashMap();
        String lastGroupId=null;
        for (CcCourse ccCourse : ccCourses) {
            Long state1 = ccCourse.getLong("state");

            Long courseId = ccCourse.getLong("id");
            String courseInt =String.valueOf(courseId);
            String  groupId = String.valueOf(ccCourse.getLong("group_id"));


            Map<String, Object> courseInfo = null;
            //Map<String, Object> stringObjectMap = courseMap.get(courseId);
            //用于教学分组，因为教学分组有课程重复，只能根据分组id和课程id拼接组合判断是否相同
            String groupCourse=groupId+courseInt;
            if (state1==3){
                System.out.println("ss");
            }
            if (courseMap.get(courseId) == null || !lastGroupId.equals(groupCourse)) {
                StringBuilder keyBuilder = new StringBuilder();
                if (CcPlanVersion.PLAN_REPORT_TYPE_THEORY.equals(reportType)) {
                    if (ccCourse.getLong("hierarchy_id") != null) {
                        keyBuilder.append(",").append(ccCourse.getLong("hierarchy_id")).append(",");
                    }
                    if (ccCourse.getLong("hierarchy_secondary_id") != null) {
                        keyBuilder.append(",").append(ccCourse.getLong("hierarchy_secondary_id")).append(",");
                    }

                    if (ccCourse.getLong("property_id") != null) {
                        keyBuilder.append(",").append(ccCourse.getLong("property_id")).append(",");
                    }
                    if (ccCourse.getLong("property_secondary_id") != null) {
                        keyBuilder.append(",").append(ccCourse.getLong("property_secondary_id")).append(",");
                    }

                    if (ccCourse.getLong("direction_id") != null) {
                        keyBuilder.append(",").append(ccCourse.getLong("direction_id")).append(",");
                    }

                } else if (CcPlanVersion.PLAN_REPORT_TYPE_PRACTICE.equals(reportType)) {
                    if (ccCourse.getLong("module_id") != null) {
                        keyBuilder.append(",").append(ccCourse.getLong("module_id")).append(",");
                    }

                }

                String key = keyBuilder.toString();

                Map<String, Object> zoneInfo = zoneMap.get(key);

                // 培养计划需要更新后才能显示完整数据
                if (zoneInfo == null) {
                    continue;
                }

                // 初始化课程列表
                List<Map<String, Object>> courseItems = null;
                if (zoneInfo.get("courseItems") == null) {
                    courseItems = Lists.newArrayList();
                    zoneInfo.put("courseItems", courseItems);
                } else {
                    courseItems = (List<Map<String, Object>>) zoneInfo.get("courseItems");
                }


                // 课程信息元素创建
                courseInfo = Maps.newHashMap();
                courseInfo.put("id", ccCourse.getLong("id"));
                courseInfo.put("name", ccCourse.getStr("name"));
                courseInfo.put("code", ccCourse.getStr("code"));
                courseInfo.put("englishName", ccCourse.getStr("english_name"));
                courseInfo.put("credit", ccCourse.getBigDecimal("credit"));
                courseInfo.put("allHours", ccCourse.getBigDecimal("all_hours"));
                courseInfo.put("theoryHours", ccCourse.getBigDecimal("theory_hours"));
                courseInfo.put("experimentHours", ccCourse.getBigDecimal("experiment_hours"));
                courseInfo.put("practiceHours", ccCourse.getBigDecimal("practice_hours"));
                courseInfo.put("independentHours", ccCourse.getBigDecimal("independent_hours"));
                courseInfo.put("exercisesHours", ccCourse.getBigDecimal("exercises_hours"));
                courseInfo.put("dicussHours", ccCourse.getBigDecimal("dicuss_hours"));
                courseInfo.put("extracurricularHours", ccCourse.getBigDecimal("extracurricular_hours"));
                courseInfo.put("operateComputerHours", ccCourse.getBigDecimal("operate_computer_hours"));
                courseInfo.put("remark", ccCourse.getStr("remark"));
                courseInfo.put("typeValue",ccCourse.getStr("type_value"));
                courseInfo.put("typeName",ccCourse.getStr("type_name"));
                courseInfo.put("isGroup", Boolean.FALSE);
                courseInfo.put("examTerm", new ArrayList<Map<String, Object>>());
                courseInfo.put("haveClassTerm", new HashMap<String, BigDecimal>());

                //区别课程组和普通课程、多选一课程
                Long state = ccCourse.getLong("state");
                if (state==2){
                    courseInfo.put("num",ccCourse.getLong("num"));
                    courseInfo.put("groupId",ccCourse.getLong("group_id"));
                }
                // 多选一组编号
                Long courseGroupId = ccCourse.getLong("group_id");
                if (courseGroupId == null) {
                    courseItems.add(courseInfo);
                } else {

                        // 课程组key值
                        String groupKey = key + "," + courseGroupId;
                        if (courseGroupMap.get(groupKey) == null) {
                            List<Map<String, Object>> courseInfoInGroup = Lists.newArrayList();
                            courseInfoInGroup.add(courseInfo);

                            Map<String, Object> courseGroupInfo = Maps.newHashMap();
                            courseGroupInfo.put("id", courseGroupId);
                            courseGroupInfo.put("remark", ccCourse.getStr("group_remark"));
                            courseGroupInfo.put("courseItems", courseInfoInGroup);
                            courseGroupInfo.put("isGroup", Boolean.TRUE);
                            //分级教学组的id
                            courseGroupInfo.put("teachGroupId",ccCourse.getLong("teach_group_id"));
                            if (state==2 ||state==3){
                                courseGroupInfo.put("isGroupMange", Boolean.TRUE);
                                courseGroupInfo.put("isGroup", Boolean.FALSE);
                            }else{
                                courseGroupInfo.put("isGroupMange", Boolean.FALSE);

                            }
                             if(state==3) {
                                 //分级教学合并的总行数
                                 courseGroupInfo.put("sumNum", ccCourse.getLong("sumnum"));

                             }
                                courseItems.add(courseGroupInfo);
                            courseGroupMap.put(groupKey, courseInfoInGroup);

                        } else {
                            ((List<Map<String, Object>>) courseGroupMap.get(groupKey)).add(courseInfo);
                        }


                }
                lastGroupId=groupId+courseInt;

                courseMap.put(courseId, courseInfo);



                // 课程数目
                zoneInfo.put("courseNum", zoneInfo.get("courseNum") == null ? 1 : (Integer) zoneInfo.get("courseNum") + 1);

            } else {
                courseInfo = courseMap.get(courseId);
            }

            // 学期信息添加
            if (CcPlanTermCourse.TYPE_CLASS.equals(ccCourse.getInt("course_term_type"))) {
                Map<Long, BigDecimal> termInfo = (Map<Long, BigDecimal>) courseInfo.get("haveClassTerm");
                termInfo.put(ccCourse.getLong("plan_term_id"), ccCourse.getBigDecimal("week_hour"));
            } else if (CcPlanTermCourse.TYPE_EXAM.equals(ccCourse.getInt("course_term_type"))) {
                List<Map<String, Object>> examTermList = (List<Map<String, Object>>) courseInfo.get("examTerm");
                Map<String, Object> termInfo = Maps.newHashMap();
                termInfo.put("termId", ccCourse.getLong("plan_term_id"));
                termInfo.put("year", ccCourse.getInt("plan_year"));
                termInfo.put("yearName", ccCourse.getStr("plan_year_name"));
                termInfo.put("term", ccCourse.getInt("plan_term"));
                termInfo.put("termName", ccCourse.getStr("plan_term_name"));
                termInfo.put("termType", ccCourse.getInt("plan_term_type"));
                termInfo.put("termTypeName", DictUtils.findLabelByTypeAndKey("termType", ccCourse.getInt("plan_term_type")));
                examTermList.add(termInfo);
            }

        }

        return result;
    }

    /**
     * 合计所有成绩(理论教学)
     *
     * @param reportBodyData 培养计划表格主体
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getSumAllTheory(List<Map<String, Object>> reportBodyData) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("haveClassTerm", new HashMap<Long, BigDecimal>());
        for (Map<String, Object> map : reportBodyData) {
            Boolean showSumInfo = (Boolean) map.get("showSumInfo");
            Map<String, Object> valueInfo = null;
            Map<Long, BigDecimal> valueTermInfo = null;
            if (showSumInfo) {
                valueInfo = (Map<String, Object>) map.get("sumInfo");
            } else {
                valueInfo = (Map<String, Object>) map.get("leastInfo");
            }

            BigDecimal value = null;
            if (valueInfo.get("credit") != null) {
                value = (BigDecimal) valueInfo.get("credit");
                result.put("credit", result.get("credit") == null ? value : PriceUtils._add((BigDecimal) result.get("credit"), value).setScale(1, RoundingMode.HALF_UP));
            }

            if (valueInfo.get("allHours") != null) {
                value = (BigDecimal) valueInfo.get("allHours");
                result.put("allHours", result.get("allHours") == null ? value : PriceUtils._add((BigDecimal) result.get("allHours"), value).setScale(1, RoundingMode.HALF_UP));
            }

            if (valueInfo.get("theoryHours") != null) {
                value = (BigDecimal) valueInfo.get("theoryHours");
                result.put("theoryHours", result.get("theoryHours") == null ? value : PriceUtils._add((BigDecimal) result.get("theoryHours"), value).setScale(1, RoundingMode.HALF_UP));
            }

            if (valueInfo.get("experimentHours") != null) {
                value = (BigDecimal) valueInfo.get("experimentHours");
                result.put("experimentHours", result.get("experimentHours") == null ? value : PriceUtils._add((BigDecimal) result.get("experimentHours"), value).setScale(1, RoundingMode.HALF_UP));
            }

            if (valueInfo.get("practiceHours") != null) {
                value = (BigDecimal) valueInfo.get("practiceHours");
                result.put("practiceHours", result.get("practiceHours") == null ? value : PriceUtils._add((BigDecimal) result.get("practiceHours"), value).setScale(1, RoundingMode.HALF_UP));
            }

            if (valueInfo.get("independentHours") != null) {
                value = (BigDecimal) valueInfo.get("independentHours");
                result.put("independentHours", result.get("independentHours") == null ? value : PriceUtils._add((BigDecimal) result.get("independentHours"), value).setScale(1, RoundingMode.HALF_UP));
            }

            if (valueInfo.get("exercisesHours") != null) {
                value = (BigDecimal) valueInfo.get("exercisesHours");
                result.put("exercisesHours", result.get("exercisesHours") == null ? value : PriceUtils._add((BigDecimal) result.get("exercisesHours"), value).setScale(1, RoundingMode.HALF_UP));
            }

            if (valueInfo.get("dicussHours") != null) {
                value = (BigDecimal) valueInfo.get("dicussHours");
                result.put("dicussHours", result.get("dicussHours") == null ? value : PriceUtils._add((BigDecimal) result.get("dicussHours"), value).setScale(1, RoundingMode.HALF_UP));
            }

            if (valueInfo.get("extracurricularHours") != null) {
                value = (BigDecimal) valueInfo.get("extracurricularHours");
                result.put("extracurricularHours", result.get("extracurricularHours") == null ? value : PriceUtils._add((BigDecimal) result.get("extracurricularHours"), value).setScale(1, RoundingMode.HALF_UP));
            }

            if (valueInfo.get("operateComputerHours") != null) {
                value = (BigDecimal) valueInfo.get("operateComputerHours");
                result.put("operateComputerHours", result.get("operateComputerHours") == null ? value : PriceUtils._add((BigDecimal) result.get("operateComputerHours"), value).setScale(1, RoundingMode.HALF_UP));
            }

            valueTermInfo = (Map<Long, BigDecimal>) valueInfo.get("haveClassTerm");
            for (Map.Entry<Long, BigDecimal> entry : valueTermInfo.entrySet()) {
                Map<Long, BigDecimal> sumTermInfo = (Map<Long, BigDecimal>) result.get("haveClassTerm");
                sumTermInfo.put(entry.getKey(), sumTermInfo.get(entry.getKey()) == null ? entry.getValue() : PriceUtils._add(sumTermInfo.get(entry.getKey()), entry.getValue()).setScale(1, RoundingMode.HALF_UP));
            }
        }

        return result;
    }

    /**
     * 合计所有成绩(实践教学)
     *
     * @param reportBodyData 培养计划表格主体
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getSumAllPractice(List<Map<String, Object>> reportBodyData, List<Map<String, Object>> newReportBodyDataList) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("haveClassTerm", new HashMap<Long, BigDecimal>());

        Long prevSumGroupId = null;
        Map<String, Object> prevSmallSumInfo = null;
        for (Map<String, Object> map : reportBodyData) {
            Boolean showSumInfo = (Boolean) map.get("showSumInfo");
            Boolean showLeastInfo = (Boolean) map.get("showLeastInfo");
            map.put("isModuleSum", Boolean.FALSE);

            Map<String, Object> valueInfo = null;
            if (showSumInfo || (!showSumInfo && !showLeastInfo)) {
                valueInfo = (Map<String, Object>) map.get("sumInfo");
            } else {
                valueInfo = (Map<String, Object>) map.get("leastInfo");
            }

            // 所属模块组小计信息
            Long sumGroupId = (Long) map.get("sumGroupId");
            boolean haveSumGroup = false;
            if (sumGroupId != null && !sumGroupId.equals(0L)) {
                haveSumGroup = true;
            }

            Map<Long, BigDecimal> smallSumTermInfo = null;
            BigDecimal value = null;
            if (haveSumGroup) {
                Map<String, Object> smallSumInfo = null;
                if (!sumGroupId.equals(prevSumGroupId)) {
                    smallSumInfo = Maps.newHashMap();
                    smallSumInfo.put("isModuleSum", Boolean.TRUE);
                    smallSumInfo.put("haveClassTerm", new HashMap<Long, BigDecimal>());
                    if (prevSmallSumInfo != null) {
                        newReportBodyDataList.add(prevSmallSumInfo);
                    }

                    prevSmallSumInfo = smallSumInfo;
                    prevSumGroupId = sumGroupId;
                } else {
                    smallSumInfo = prevSmallSumInfo;
                }


                if (valueInfo.get("credit") != null) {
                    value = (BigDecimal) valueInfo.get("credit");
                    smallSumInfo.put("credit", smallSumInfo.get("credit") == null ? value : PriceUtils._add((BigDecimal) smallSumInfo.get("credit"), value).setScale(1, RoundingMode.HALF_UP));
                }

                if (valueInfo.get("allHours") != null) {
                    value = (BigDecimal) valueInfo.get("allHours");
                    smallSumInfo.put("allHours", smallSumInfo.get("allHours") == null ? value : PriceUtils._add((BigDecimal) smallSumInfo.get("allHours"), value).setScale(1, RoundingMode.HALF_UP));
                }

                if (valueInfo.get("theoryHours") != null) {
                    value = (BigDecimal) valueInfo.get("theoryHours");
                    smallSumInfo.put("theoryHours", smallSumInfo.get("theoryHours") == null ? value : PriceUtils._add((BigDecimal) smallSumInfo.get("theoryHours"), value).setScale(1, RoundingMode.HALF_UP));
                }

                if (valueInfo.get("experimentHours") != null) {
                    value = (BigDecimal) valueInfo.get("experimentHours");
                    smallSumInfo.put("experimentHours", smallSumInfo.get("experimentHours") == null ? value : PriceUtils._add((BigDecimal) smallSumInfo.get("experimentHours"), value).setScale(1, RoundingMode.HALF_UP));
                }

                if (valueInfo.get("practiceHours") != null) {
                    value = (BigDecimal) valueInfo.get("practiceHours");
                    smallSumInfo.put("practiceHours", smallSumInfo.get("practiceHours") == null ? value : PriceUtils._add((BigDecimal) smallSumInfo.get("practiceHours"), value).setScale(1, RoundingMode.HALF_UP));
                }

                if (valueInfo.get("independentHours") != null) {
                    value = (BigDecimal) valueInfo.get("independentHours");
                    smallSumInfo.put("independentHours", smallSumInfo.get("independentHours") == null ? value : PriceUtils._add((BigDecimal) smallSumInfo.get("independentHours"), value).setScale(1, RoundingMode.HALF_UP));
                }


                if (valueInfo.get("exercisesHours") != null) {
                    value = (BigDecimal) valueInfo.get("exercisesHours");
                    smallSumInfo.put("exercisesHours", smallSumInfo.get("exercisesHours") == null ? value : PriceUtils._add((BigDecimal) smallSumInfo.get("exercisesHours"), value).setScale(1, RoundingMode.HALF_UP));
                }

                if (valueInfo.get("dicussHours") != null) {
                    value = (BigDecimal) valueInfo.get("dicussHours");
                    smallSumInfo.put("dicussHours", smallSumInfo.get("dicussHours") == null ? value : PriceUtils._add((BigDecimal) smallSumInfo.get("dicussHours"), value).setScale(1, RoundingMode.HALF_UP));
                }

                if (valueInfo.get("extracurricularHours") != null) {
                    value = (BigDecimal) valueInfo.get("extracurricularHours");
                    smallSumInfo.put("extracurricularHours", smallSumInfo.get("extracurricularHours") == null ? value : PriceUtils._add((BigDecimal) smallSumInfo.get("extracurricularHours"), value).setScale(1, RoundingMode.HALF_UP));
                }

                if (valueInfo.get("operateComputerHours") != null) {
                    value = (BigDecimal) valueInfo.get("operateComputerHours");
                    smallSumInfo.put("operateComputerHours", smallSumInfo.get("operateComputerHours") == null ? value : PriceUtils._add((BigDecimal) smallSumInfo.get("operateComputerHours"), value).setScale(1, RoundingMode.HALF_UP));
                }

                smallSumTermInfo = (Map<Long, BigDecimal>) smallSumInfo.get("haveClassTerm");
            } else {
                if (prevSumGroupId != null) {
                    newReportBodyDataList.add(prevSmallSumInfo);
                    prevSmallSumInfo = null;
                    prevSumGroupId = null;
                }
            }

            if (valueInfo.get("credit") != null) {
                value = (BigDecimal) valueInfo.get("credit");
                result.put("credit", result.get("credit") == null ? value : PriceUtils._add((BigDecimal) result.get("credit"), value).setScale(1, RoundingMode.HALF_UP));
            }

            if (valueInfo.get("allHours") != null) {
                value = (BigDecimal) valueInfo.get("allHours");
                result.put("allHours", result.get("allHours") == null ? value : PriceUtils._add((BigDecimal) result.get("allHours"), value).setScale(1, RoundingMode.HALF_UP));
            }

            if (valueInfo.get("theoryHours") != null) {
                value = (BigDecimal) valueInfo.get("theoryHours");
                result.put("theoryHours", result.get("theoryHours") == null ? value : PriceUtils._add((BigDecimal) result.get("theoryHours"), value).setScale(1, RoundingMode.HALF_UP));
            }

            if (valueInfo.get("experimentHours") != null) {
                value = (BigDecimal) valueInfo.get("experimentHours");
                result.put("experimentHours", result.get("experimentHours") == null ? value : PriceUtils._add((BigDecimal) result.get("experimentHours"), value).setScale(1, RoundingMode.HALF_UP));
            }

            if (valueInfo.get("practiceHours") != null) {
                value = (BigDecimal) valueInfo.get("practiceHours");
                result.put("practiceHours", result.get("practiceHours") == null ? value : PriceUtils._add((BigDecimal) result.get("practiceHours"), value).setScale(1, RoundingMode.HALF_UP));
            }

            if (valueInfo.get("independentHours") != null) {
                value = (BigDecimal) valueInfo.get("independentHours");
                result.put("independentHours", result.get("independentHours") == null ? value : PriceUtils._add((BigDecimal) result.get("independentHours"), value).setScale(1, RoundingMode.HALF_UP));
            }

            if (valueInfo.get("exercisesHours") != null) {
                value = (BigDecimal) valueInfo.get("exercisesHours");
                result.put("exercisesHours", result.get("exercisesHours") == null ? value : PriceUtils._add((BigDecimal) result.get("exercisesHours"), value).setScale(1, RoundingMode.HALF_UP));
            }

            if (valueInfo.get("dicussHours") != null) {
                value = (BigDecimal) valueInfo.get("dicussHours");
                result.put("dicussHours", result.get("dicussHours") == null ? value : PriceUtils._add((BigDecimal) result.get("dicussHours"), value).setScale(1, RoundingMode.HALF_UP));
            }

            if (valueInfo.get("extracurricularHours") != null) {
                value = (BigDecimal) valueInfo.get("extracurricularHours");
                result.put("extracurricularHours", result.get("extracurricularHours") == null ? value : PriceUtils._add((BigDecimal) result.get("extracurricularHours"), value).setScale(1, RoundingMode.HALF_UP));
            }

            if (valueInfo.get("operateComputerHours") != null) {
                value = (BigDecimal) valueInfo.get("operateComputerHours");
                result.put("operateComputerHours", result.get("operateComputerHours") == null ? value : PriceUtils._add((BigDecimal) result.get("operateComputerHours"), value).setScale(1, RoundingMode.HALF_UP));
            }

            newReportBodyDataList.add(map);

            Map<Long, BigDecimal> valueTermInfo = (Map<Long, BigDecimal>) valueInfo.get("haveClassTerm");
            for (Map.Entry<Long, BigDecimal> entry : valueTermInfo.entrySet()) {
                Map<Long, BigDecimal> sumTermInfo = (Map<Long, BigDecimal>) result.get("haveClassTerm");
                sumTermInfo.put(entry.getKey(), sumTermInfo.get(entry.getKey()) == null ? entry.getValue() : PriceUtils._add(sumTermInfo.get(entry.getKey()), entry.getValue()).setScale(1, RoundingMode.HALF_UP));
                // 有合计组时计算学期
                if (haveSumGroup) {
                    smallSumTermInfo.put(entry.getKey(), smallSumTermInfo.get(entry.getKey()) == null ? entry.getValue() : PriceUtils._add(smallSumTermInfo.get(entry.getKey()), entry.getValue()).setScale(1, RoundingMode.HALF_UP));
                }

            }

        }
        // 加入最后一个
        if (prevSmallSumInfo != null) {
            newReportBodyDataList.add(prevSmallSumInfo);
        }

        return result;
    }

}
