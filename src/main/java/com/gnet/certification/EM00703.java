package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.gnet.utils.DictUtils;
import com.gnet.utils.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 大纲详情
 *
 * @author xzl
 *
 * @date 2017年8月11日
 *
 */
@Service("EM00703")
@Transactional(readOnly=true)
public class EM00703 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {

		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		Map<String, Object> result = new HashMap<>();

		Long courseOutlineId = paramsLongFilter(param.get("courseOutlineId"));
		Long courseId = paramsLongFilter(param.get("courseId"));
		Long courseOutlineTypeId = paramsLongFilter(param.get("courseOutlineTypeId"));


		if(courseOutlineId == null){
			if(courseId == null){
				return renderFAIL("0250", response, header);
			}

			if(courseOutlineTypeId == null){
				return  renderFAIL("0892", response, header);
			}

			Map<String, Object> params = new HashMap<>();
			params.put("course_id", courseId);
			params.put("outline_type_id", courseOutlineTypeId);
			CcCourseOutline ccCourseOutline = CcCourseOutline.dao.findFirstByColumn(params, true);
			if(ccCourseOutline == null){
				return renderSUC(result, response, header);
			}else{
				courseOutlineId = ccCourseOutline.getLong("id");
			}
		}

		CcCourse ccCourse = CcCourse.dao.findByCourseOutlineId(courseOutlineId);
		if(ccCourse == null){
			return renderFAIL("0251", response, header);
		}

		//大纲基本信息
		List<Map<String, Object>> courseInfo = Lists.newArrayList();
		List<CcCourseOutlineCourseInfo> ccCourseOutlineCourseInfoList = CcCourseOutlineCourseInfo.dao.findFilteredByColumn("course_outline_id", courseOutlineId);
		for(CcCourseOutlineCourseInfo temp : ccCourseOutlineCourseInfoList){
			Map<String, Object> map = Maps.newHashMap();
			map.put("name", temp.getStr("name"));
			map.put("databaseField", temp.getStr("database_field"));
			map.put("content", temp.getStr("content"));

			courseInfo.add(map);
		}
		//TODO Gjm 2020/06/18增加毕业指标点的选择 在这里显示课程毕业指标点编号和指标点对应的map
		Map<String, Map<String, Object>> indicationPorintMap = Maps.newConcurrentMap();
		List<CcIndicatorPoint> indeicationPoints = CcIndicatorPoint.dao.findAllByCourseId(ccCourse.getLong("id"));
		for (CcIndicatorPoint ccIndicatorPoint: indeicationPoints ){
			Long indicatorPointId = ccIndicatorPoint.getLong("id");
			Map<String, Object> map = new HashMap<>();
			map.put("id",indicatorPointId);
			map.put("content", ccIndicatorPoint.getInt("graduateIndexNum")+"-"+ccIndicatorPoint.getInt("index_num")+":"+ccIndicatorPoint.getStr("content"));
			indicationPorintMap.put(String.valueOf(indicatorPointId),map);
		}
		//课程目标编号和课程目标对应的map
		Map<String, Map<String, Object>> indicationMap = Maps.newConcurrentMap();
		//查找每个模块支持的课程目标信息
		List<CcIndication> ccIndications = CcIndication.dao.findFilteredByColumn("course_id", ccCourse.getLong("id"));
        for(CcIndication ccIndication : ccIndications){
        	Long indicationId = ccIndication.getLong("id");
        	Map<String, Object> map = new HashMap<>();
        	map.put("id", indicationId);
			map.put("sort", ccIndication.getInt("sort"));
			map.put("content", ccIndication.getStr("content"));
			//其他返回的课程目标信息中没有备注信息
			indicationMap.put(String.valueOf(indicationId), map);

		}



        //大纲模块信息
		List<CcCourseOutlineModule> ccCourseOutlineModuleList = CcCourseOutlineModule.dao.findCourseOutlineModules(courseOutlineId);
		//大纲模块教学内容
		List<CcCourseOutlineTeachingContent> ccCourseOutlineTeachingContentList = CcCourseOutlineTeachingContent.dao.findByCourseOutlineId(courseOutlineId);
		//大纲模块次要内容
		List<CcCourseOutlineSecondaryContent> ccCourseOutlineSecondaryContentList = CcCourseOutlineSecondaryContent.dao.findByCourseOutlineId(courseOutlineId);
        //大纲表名称
		List<CcCourseOutlineTableName> ccCourseOutlineTableNameList = CcCourseOutlineTableName.dao.findByCourseOutlineId(courseOutlineId);
		//大纲模块表头
		List<CcCourseOutlineHeader> ccCourseOutlineHeaderList = CcCourseOutlineHeader.dao.findByCourseOutlineId(courseOutlineId);
        //大纲模块表格信息
		List<CcCourseOutlineTableDetail> ccCourseOutlineTableDetailList = CcCourseOutlineTableDetail.dao.findByCourseOutlineId(courseOutlineId);

		Map<String, String> tableNameMap = new HashMap<>();
		//表名
		if(!ccCourseOutlineTableNameList.isEmpty()){
			for(CcCourseOutlineTableName ccCourseOutlineTableName : ccCourseOutlineTableNameList){
				String key = String.format("%s-%s", ccCourseOutlineTableName.getInt("indexes"), ccCourseOutlineTableName.getInt("number"));
				tableNameMap.put(key, ccCourseOutlineTableName.getStr("table_name"));
				tableNameMap.put(key+"-tableTop",ccCourseOutlineTableName.getStr("table_top_detail"));
				tableNameMap.put(key+"-tableBottom",ccCourseOutlineTableName.getStr("table_bottom_detail"));
			}
		}

		//模块序号与标序号集合
		Set<String> set = new HashSet<>();
		Map<String, List<Map<String, Object>>> headerMap = new HashMap<>();
		//模块表头
		if(!ccCourseOutlineHeaderList.isEmpty()){
			for(CcCourseOutlineHeader outlineHeader : ccCourseOutlineHeaderList){
				Integer number = outlineHeader.getInt("number");
				String key = String.format("%s-%s", outlineHeader.getInt("indexes"), number);
				String totalColumn = outlineHeader.getStr("total_column");
				set.add(key);

				Map<String, Object> map = new HashMap<>();
				map.put("number", number);
				map.put("name", outlineHeader.getStr("name"));
				map.put("type", outlineHeader.getInt("type"));
				map.put("columnOrdinal", outlineHeader.getInt("column_ordinal"));
				map.put("hoursType", outlineHeader.getInt("hours_type"));
				map.put("hours", outlineHeader.getBigDecimal("hours"));
				map.put("totalColumn", totalColumn);
				map.put("totalColumnArray", StrKit.notBlank(totalColumn) ? totalColumn.split(",") : null);

				List<Map<String, Object>> headers = headerMap.get(key);
				if(headers == null){
					headers = new ArrayList<>();
					headerMap.put(key, headers);
				}
				headers.add(map);
			}
		}

		//模块表格
		Map<String, List<Map<String, Object>>> tableDetailMap = new HashMap<>();
		if(!ccCourseOutlineTableDetailList.isEmpty()){
			for(CcCourseOutlineTableDetail ccCourseOutlineTableDetail : ccCourseOutlineTableDetailList){
				Integer number = ccCourseOutlineTableDetail.getInt("number");
				String key = String.format("%s-%s", ccCourseOutlineTableDetail.get("indexes"), number);

				Map<String, Object> map = new HashMap<>();
				String content = ccCourseOutlineTableDetail.getStr("content");
				Integer type = ccCourseOutlineTableDetail.getInt("type");
				map.put("number", number);
				map.put("rowOrdinal", ccCourseOutlineTableDetail.getInt("row_ordinal"));
				map.put("type", type);
				map.put("columnOrdinal", ccCourseOutlineTableDetail.getInt("column_ordinal"));
				map.put("content", ccCourseOutlineTableDetail.getStr("content"));
                //如果是课程目标选择的话附带返回课程目标数组
				if(CcCourseOutlineHeader.TYPE_INDICATION.equals(type) && StringUtils.isNotBlank(content)){
					String[] indicationIdArray = content.split(",");
					List<Map<String, Object>> indicationList = Lists.newArrayList();
					for(int i =0; i<indicationIdArray.length; i++){
						if(indicationMap.get(indicationIdArray[i]) != null){
							indicationList.add(indicationMap.get(indicationIdArray[i]));
						}
					}

					map.put("indications", indicationList);
				}
				//毕业指标点
				if(CcCourseOutlineHeader.TYPE_INDICATIONPOINT.equals(type) && StringUtils.isNotBlank(content)){
					String[] indicationPointIdArray = content.split(",");
					List<Map<String, Object>> indicationPointList = Lists.newArrayList();
					for(int i =0; i<indicationPointIdArray.length; i++){
						if(indicationPorintMap.get(indicationPointIdArray[i]) != null){
							indicationPointList.add(indicationPorintMap.get(indicationPointIdArray[i]));
						}
					}

					map.put("indications", indicationPointList);
				}



				List<Map<String, Object>> tableDetails = tableDetailMap.get(key);
				if(tableDetails == null){
					tableDetails = new ArrayList<>();
					tableDetailMap.put(key, tableDetails);
				}
				tableDetails.add(map);

			}
		}

		Map<Integer, List<Map<String, Object>>> tableMap = new HashMap<>();
		for(String key : set){
			Integer index = Integer.valueOf(key.split("-")[0]);
			Map<String, Object> table = new HashMap<>();
			table.put("tableName", tableNameMap.get(key));
			table.put("tableTopDetail",tableNameMap.get(key+"-tableTop"));
			table.put("tableBottomDetail",tableNameMap.get(key+"-tableBottom"));
			table.put("headers", headerMap.get(key));
			table.put("tableDetails", tableDetailMap.get(key));

			List<Map<String, Object>> tableLists = tableMap.get(index);
			if(tableLists == null){
				tableLists = new ArrayList<>();
				tableMap.put(index, tableLists);
			}
			tableLists.add(table);
		}

		List<Map<String, Object>> moduleList = Lists.newArrayList();
        for(CcCourseOutlineModule temp : ccCourseOutlineModuleList){
        	Map<String, Object> module = new HashMap<>();
			Integer index = temp.getInt("indexes");

			module.put("indexes", index);
			module.put("title", temp.getStr("title"));
			module.put("mainContent", temp.getStr("main_content"));
			module.put("isExistMainContent", temp.getBoolean("is_exist_main_content"));
            module.put("isExistSecondaryContent", temp.getBoolean("is_exist_secondary_content"));
            module.put("isExistTeachingContent", temp.getBoolean("is_exist_teaching_content"));
            module.put("isExistTable", temp.getBoolean("is_exist_table"));
			module.put("isMainContentSupport", temp.getBoolean("is_main_content_support"));
			module.put("isSecondaryContentSupport", temp.getBoolean("is_secondary_content_support"));
			module.put("isTeachingContentSupport", temp.getBoolean("is_teaching_content_support"));

			if(StringUtils.isNotBlank(temp.getStr("indications"))){
				String[] indicationIdArray = temp.getStr("indications").split(",");
				List<Map<String, Object>> indicationList = Lists.newArrayList();
				for(int i =0; i<indicationIdArray.length; i++){
					if(indicationMap.get(indicationIdArray[i]) != null){
						indicationList.add(indicationMap.get(indicationIdArray[i]));
					}
				}
				module.put("mainContentIndications", indicationList);
			}

			//模块教学内容
			if(!ccCourseOutlineTeachingContentList.isEmpty()){
				List<Map<String, Object>> teachingContentArray = Lists.newArrayList();
				for(CcCourseOutlineTeachingContent teachingContent : ccCourseOutlineTeachingContentList){
                     if(index.equals(teachingContent.getInt("indexes"))){
                     	Map<String, Object> map = new HashMap<>();
                     	map.put("indexes", teachingContent.getInt("indexes"));
                     	map.put("teachingContent", teachingContent.getStr("teaching_content"));
                     	map.put("basicRequirement", teachingContent.getStr("basic_requirement"));
                     	map.put("hours", teachingContent.getStr("hours"));

                     	if(StringUtils.isNotBlank(teachingContent.getStr("indications"))){
							String[] indicationIdArray = teachingContent.getStr("indications").split(",");
							List<Map<String, Object>> indicationList = Lists.newArrayList();
							for(int i =0; i<indicationIdArray.length; i++){
								if(indicationMap.get(indicationIdArray[i]) != null){
									indicationList.add(indicationMap.get(indicationIdArray[i]));
								}
							 }
							map.put("indications", indicationList);
						 }

                     	teachingContentArray.add(map);
					 }
				}
				if(!teachingContentArray.isEmpty()){
					module.put("teachingContents", teachingContentArray);
				}
			}

			//模块次要内容
			if(!ccCourseOutlineSecondaryContentList.isEmpty()){
                List<Map<String, Object>> secondaryContentArray = Lists.newArrayList();
                for(CcCourseOutlineSecondaryContent secondaryContent : ccCourseOutlineSecondaryContentList){
                	if(index.equals(secondaryContent.getInt("indexes"))){
                		Map<String, Object> map = new HashMap<>();
                		map.put("indexes", secondaryContent.getInt("indexes"));
                		map.put("title", secondaryContent.getStr("title"));
						map.put("content", secondaryContent.getStr("content"));
                		map.put("secondaryContent", secondaryContent.getStr("content"));

						if(StringUtils.isNotBlank(secondaryContent.getStr("indications"))){
							String[] indicationIdArray = secondaryContent.getStr("indications").split(",");
							List<Map<String, Object>> indicationList = Lists.newArrayList();
							for(int i =0; i<indicationIdArray.length; i++){
								if(indicationMap.get(indicationIdArray[i]) != null){
									indicationList.add(indicationMap.get(indicationIdArray[i]));
								}
							}
							map.put("indications", indicationList);
						}

                		secondaryContentArray.add(map);
					}
				}
				if(!secondaryContentArray.isEmpty()){
                	module.put("secondaryContents", secondaryContentArray);
				}
			}

			if(tableMap.get(index) != null && !tableMap.get(index).isEmpty()){
                    module.put("tables", tableMap.get(index));
			}

			module.put("moduleIndex", StringUtil.int2Chinese(index));
			moduleList.add(module);

		}

		result.put("modules", moduleList);
        result.put("courseInfos", courseInfo);
        result.put("name", ccCourse.getStr("courseOutlineName"));
        result.put("outlineTemplateId", ccCourse.getLong("outline_template_id"));
        result.put("outlineTypeName", ccCourse.getStr("outlineTypeName"));
        result.put("outlineTypeId", ccCourse.getLong("outline_type_id"));
        result.put("auditComment", ccCourse.getStr("audit_comment"));
        result.put("authorName", ccCourse.getStr("authorName"));
        result.put("auditorName", ccCourse.getStr("auditorName"));
		BigDecimal theoryHours = ccCourse.getBigDecimal("theory_hours");
		result.put("theoryHours", theoryHours == null ? theoryHours : (theoryHours.compareTo(BigDecimal.ZERO) == 1 ? theoryHours : null));
		BigDecimal experimentHours = ccCourse.getBigDecimal("experiment_hours");
		result.put("experimentHours", experimentHours == null ? experimentHours : (experimentHours.compareTo(BigDecimal.ZERO) == 1 ? experimentHours : null));
		BigDecimal practiceHours = ccCourse.getBigDecimal("practice_hours");
		result.put("practiceHours", practiceHours == null ? practiceHours : (practiceHours.compareTo(BigDecimal.ZERO) == 1 ? practiceHours : null));
		BigDecimal dicussHours = ccCourse.getBigDecimal("dicuss_hours");
		result.put("dicussHours", dicussHours == null ? dicussHours : (dicussHours.compareTo(BigDecimal.ZERO) == 1 ? dicussHours : null));
		BigDecimal exercisesHours = ccCourse.getBigDecimal("exercises_hours");
		result.put("exercisesHours", exercisesHours == null ? exercisesHours : (exercisesHours.compareTo(BigDecimal.ZERO) == 1 ? exercisesHours : null));

		result.put("courseCode", ccCourse.getStr("code"));
		result.put("courseName", ccCourse.getStr("name"));
		result.put("englishName", ccCourse.getStr("english_name"));
		result.put("credit", ccCourse.getBigDecimal("credit"));
		result.put("allHours", ccCourse.getBigDecimal("all_hours"));
		result.put("courseType", DictUtils.findLabelByTypeAndKey("courseType", ccCourse.getInt("type")));
		result.put("applicationMajor", ccCourse.getStr("application_major"));
		result.put("department", ccCourse.getStr("department"));
		result.put("participator", ccCourse.getStr("participator"));
		result.put("prerequisite", ccCourse.getStr("prerequisite"));
		result.put("nextrequisite", ccCourse.getStr("nextrequisite"));
		result.put("teamLeader", ccCourse.getStr("team_leader"));
		result.put("teamMember", ccCourse.getStr("team_member"));
		result.put("auditDean", ccCourse.getStr("aduit_dean"));
		result.put("courseId", ccCourse.getLong("id"));
		return renderSUC(result, response, header);
	}



}
