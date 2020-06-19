package com.gnet.service;

import com.gnet.model.admin.*;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.response.ServiceResponse;
import com.gnet.utils.*;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.*;

/**
 * 课程教学大纲模块基本信息
 * 
 * @author xzl
 * 
 * @date 2017年8月1日
 */
@Component("ccCourseOutlineModuleService")
public class CcCourseOutlineModuleService {

	public ServiceResponse saveModuleLists(List<LinkedHashMap> moduleMap, Long courseOutlineId) {
		Date date = new Date();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		List<CcCourseOutlineModule> ccCourseOutlineModuleList = Lists.newArrayList();
		//模块表头信息
		List<CcCourseOutlineTableName> ccCourseOutlineTableNameList = Lists.newArrayList();
		//模块表头信息
		List<CcCourseOutlineHeader> ccCourseOutlineHeaderList = Lists.newArrayList();

		for (int i=1 ; i<= moduleMap.size(); i++) {
			Map<String, Object> map = moduleMap.get(i-1);
			String title = ConvertUtils.convert(map.get("title"), String.class);
			Boolean isExistMainContent = ConvertUtils.convert(map.get("isExistMainContent"), Boolean.class);
			Boolean isExistSecondaryContent = ConvertUtils.convert(map.get("isExistSecondaryContent"), Boolean.class);
			Boolean isExistTeachingContent = ConvertUtils.convert(map.get("isExistTeachingContent"), Boolean.class);
			Boolean isExistTable = ConvertUtils.convert(map.get("isExistTable"), Boolean.class);
			Boolean isMainContentSupport = ConvertUtils.convert(map.get("isMainContentSupport"), Boolean.class);
			Boolean isSecondaryContentSupport = ConvertUtils.convert(map.get("isSecondaryContentSupport"), Boolean.class);
			Boolean isTeachingContentSupport = ConvertUtils.convert(map.get("isTeachingContentSupport"), Boolean.class);
			String mainContent = ConvertUtils.convert(map.get("mainContent"), String.class);
			if (StringUtils.isBlank(title)) {
				return ServiceResponse.error(String.format("第%s个课程大纲模块的标题不能为空", i));
			}

			if(title.length() > 128){
				return ServiceResponse.error(String.format("第%s个课程大纲模块的标题长度不能大于128", i));
			}

			if(isExistMainContent == null){
				return  ServiceResponse.error(String.format("第%s个课程大纲模块是否存在主要内容不能为空", i));
			}
			if(isExistSecondaryContent == null){
				return  ServiceResponse.error(String.format("第%s个课程大纲模块是否存在次要内容不能为空", i));
			}
			if(isExistTeachingContent == null){
				return  ServiceResponse.error(String.format("第%s个课程大纲模块是否存在教学内容不能为空", i));
			}
			if(isExistTable == null){
				return  ServiceResponse.error(String.format("第%s个课程大纲模块是否存在表格不能为空", i));
			}
			if(isMainContentSupport == null){
				return  ServiceResponse.error(String.format("第%s个课程大纲模块主要内容是否支持课程目标不能为空", i));
			}
			if(isSecondaryContentSupport == null){
				return  ServiceResponse.error(String.format("第%s个课程大纲模块次要内容是否支持课程目标不能为空", i));
			}
			if(isTeachingContentSupport == null){
				return  ServiceResponse.error(String.format("第%s个课程大纲模块教学内容是否支持课程目标不能为空", i));
			}

			CcCourseOutlineModule ccCourseOutlineModule = new CcCourseOutlineModule();
			ccCourseOutlineModule.set("id", idGenerate.getNextValue());
			ccCourseOutlineModule.set("create_date", date);
			ccCourseOutlineModule.set("modify_date", date);
			ccCourseOutlineModule.set("indexes", i);
			ccCourseOutlineModule.set("title", title);
			ccCourseOutlineModule.set("is_exist_main_content", isExistMainContent);
			ccCourseOutlineModule.set("is_exist_secondary_content", isExistSecondaryContent);
			ccCourseOutlineModule.set("is_exist_teaching_content", isExistTeachingContent);
			ccCourseOutlineModule.set("is_exist_table", isExistTable);
			ccCourseOutlineModule.set("is_main_content_support", isMainContentSupport);
			ccCourseOutlineModule.set("is_secondary_content_support", isSecondaryContentSupport);
			ccCourseOutlineModule.set("is_teaching_content_support", isTeachingContentSupport);
			ccCourseOutlineModule.set(CcCourseOutline.COURSE_OUTLINE_ID, courseOutlineId);
			ccCourseOutlineModule.set("is_del", Boolean.FALSE);
			ccCourseOutlineModule.set("main_content",mainContent);
			ccCourseOutlineModuleList.add(ccCourseOutlineModule);

			//保存表格信息
			List<LinkedHashMap> tables = map.containsKey("tables") ? ConvertUtils.convert(map.get("tables"), List.class) : new ArrayList<LinkedHashMap>();
			//如果存在表格但是没有表格信息则返回错误
			if(isExistTable && tables.isEmpty()){
				return  ServiceResponse.error(String.format("第%s个课程大纲模块存在表格但却没有表格信息", i));
			}
			if(isExistTable && !tables.isEmpty()){
				for(int j=1; j<=tables.size(); j++){
					//表名
					Map<String, Object> tableMap = tables.get(j-1);
					String tableName = tableMap.containsKey("tableName") ? ConvertUtils.convert(tableMap.get("tableName"), String.class) : null;
					String tableTopDetail = tableMap.containsKey("tableTopDetail") ? ConvertUtils.convert(tableMap.get("tableTopDetail"), String.class) : null;
					String tableBottomDetail = tableMap.containsKey("tableBottomDetail") ? ConvertUtils.convert(tableMap.get("tableBottomDetail"), String.class) : null;
					if(StrKit.notBlank(tableName)){
						if(tableName.length() > 128){
                           return ServiceResponse.error(String.format("第%s个课程大纲模块的第%s的表格的表名长度不能大于128", i, j));
						}
						CcCourseOutlineTableName ccCourseOutlineTableName = new CcCourseOutlineTableName();
						ccCourseOutlineTableName.set("id", idGenerate.getNextValue());
						ccCourseOutlineTableName.set("create_date", date);
						ccCourseOutlineTableName.set("modify_date", date);
						ccCourseOutlineTableName.set(CcCourseOutline.COURSE_OUTLINE_ID, courseOutlineId);
						ccCourseOutlineTableName.set("indexes", i);
						ccCourseOutlineTableName.set("number", j);
						ccCourseOutlineTableName.set("table_name", tableName);
						ccCourseOutlineTableName.set("is_del", Boolean.FALSE);
						ccCourseOutlineTableName.set("table_top_detail", tableTopDetail);
						ccCourseOutlineTableName.set("table_bottom_detail", tableBottomDetail);

						ccCourseOutlineTableNameList.add(ccCourseOutlineTableName);
					}

					//保存模块表头信息
					List<LinkedHashMap> headerMap = tableMap.containsKey("headers") ? ConvertUtils.convert(tableMap.get("headers"),List.class) : new ArrayList<LinkedHashMap>();
					if(!headerMap.isEmpty()){
						for(int n=1; n<=headerMap.size(); n++){
							Map<String, Object> temp = headerMap.get(n-1);
							String headerName = ConvertUtils.convert(temp.get("name"), String.class);
							Integer type = ConvertUtils.convert(temp.get("type"), Integer.class);

							if(StrKit.isBlank(headerName)){
								return ServiceResponse.error(String.format("第%s个课程大纲模的第%s个表格的表头名称不能为空", i, j));
							}

							if(headerName.length() > 50){
								return ServiceResponse.error(String.format("第%s个课程大纲模的第%s个表格的表头名称长度不能大于50", i, j));
							}

							if(type == null){
								return ServiceResponse.error(String.format("第%s个课程大纲模的第%s个表格的表头类型不能为空", i, j));
							}

							CcCourseOutlineHeader ccCourseOutlineHeader = new CcCourseOutlineHeader();
							ccCourseOutlineHeader.set("id", idGenerate.getNextValue());
							ccCourseOutlineHeader.set("create_date", date);
							ccCourseOutlineHeader.set("modify_date", date);
							ccCourseOutlineHeader.set("indexes", i);
							ccCourseOutlineHeader.set("number", j);
							ccCourseOutlineHeader.set("name", headerName);
							ccCourseOutlineHeader.set("type", type);
							ccCourseOutlineHeader.set("column_ordinal", n);
							ccCourseOutlineHeader.set(CcCourseOutline.COURSE_OUTLINE_ID, courseOutlineId);
							ccCourseOutlineHeader.set("hours_type", temp.containsKey("hoursType") ? ConvertUtils.convert(temp.get("hoursType"), Integer.class) : null);
							ccCourseOutlineHeader.set("total_column", temp.containsKey("totalColumn") ? ConvertUtils.convert(temp.get("totalColumn"), String.class): null);
							ccCourseOutlineHeader.set("is_del", Boolean.FALSE);

							ccCourseOutlineHeaderList.add(ccCourseOutlineHeader);
						}
					}
				}
			}
		}

		if (!ccCourseOutlineModuleList.isEmpty()) {
			if(!CcCourseOutlineModule.dao.batchSave(ccCourseOutlineModuleList)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return ServiceResponse.error("保存课程大纲失败");
			}
		}

		if(!ccCourseOutlineHeaderList.isEmpty()){
			if(!CcCourseOutlineHeader.dao.batchSave(ccCourseOutlineHeaderList)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return ServiceResponse.error("保存课程大纲失败");
			}
		}

		if(!ccCourseOutlineTableNameList.isEmpty()){
			if(!CcCourseOutlineTableName.dao.batchSave(ccCourseOutlineTableNameList)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return ServiceResponse.error("保存课程大纲失败");
			}
		}
		return ServiceResponse.succ(true);
	}

	/**
	 * 返回大纲中不重复的课程目标
	 * @param str
	 * @param stringList
	 * @return
	 */
	private List<String> unRepeatList(String str, List<String> stringList ){
		if(StrKit.notBlank(str)){
            String[] strings = str.split(",");
            for(int i=0; i<strings.length; i++){
            	if(!stringList.contains(strings[i])){
            		stringList.add(strings[i]);
				}
			}
		}
		return  stringList;
	}


	/**
	 * 验证大纲总学时与课程总学时是否一致
	 * @param number1
	 *            课程总个学时
	 * @param number2
	 *            大纲各个学时
	 * @param index1
	 *            模块序号
	 * @param index2
	 *             表序号
	 * @param field
	 *           大纲列名
	 * @return
	 */
	public ServiceResponse validateAllHours(BigDecimal number1, BigDecimal number2, Integer index1, Integer index2 , String field){
		BigDecimal temp1 = number1 == null ? BigDecimal.valueOf(0) : number1;
		BigDecimal temp2 = number2 == null ? BigDecimal.valueOf(0) : number2;
		if(!PriceUtils.eqThan(temp1, temp2)){
			return ServiceResponse.error(String.format("课程的总学时(或总周数)为%s，大纲的第%s模块的第%s个表格的%s总和为%s,两者不一致请检查后再提交", number1 == null ? "空" : number1, index1, index2, field, number2 == null ? "空" : number2));
		}
		return  ServiceResponse.succ(true);
	}
	/**
	 * 验证大纲学时与课程学时是否一致
	 * @param number1
	 *            课程各个学时
	 * @param number2
	 *            大纲各个学时
	 * @param index1
	 *            模块序号
	 * @param index2
	 *             表序号
	 * @param field
	 *           具体学时字段名称
	 * @return
	 */
	public ServiceResponse validateHours(BigDecimal number1, BigDecimal number2, Integer index1, Integer index2 , String field){
          //因为课程中上级学时等字段不存在时，可能为0可能为null
		  BigDecimal temp1 = number1 == null ? BigDecimal.valueOf(0) : number1;
          BigDecimal temp2 = number2 == null ? BigDecimal.valueOf(0) : number2;

		  if(!PriceUtils.eqThan(temp1, temp2)){
          	return  ServiceResponse.error(String.format("课程的%s为%s，大纲的第%s模块的第%s个表格的%s为%s,两者不一致请检查后再提交", field, number1 == null ? "空" : temp1, index1, index2, field, number2 == null ? "空" : temp2));
		  }
		  return  ServiceResponse.succ(temp2);
	}

	//教师编写保存大纲
    public ServiceResponse saveModules(List<LinkedHashMap> moduleMap, CcCourse course, Boolean isSubmit, CcCourseOutline ccCourseOutline) {
		Boolean isNeedValidateAllHours = CcCourseOutlineType.NAME.equals(ccCourseOutline.getStr("outlineTypeName"));
		Long courseOutlineId = ccCourseOutline.getLong("id");
		Date date = new Date();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		//大纲模板
		List<CcCourseOutlineModule> ccCourseOutlineModuleList = Lists.newArrayList();
		//模块支持的课程目标
		List<CcCourseOutlineIndications> ccCourseOutlineIndicationsList = Lists.newArrayList();
		//模块教学内容
		List<CcCourseOutlineTeachingContent> ccCourseOutlineTeachingContentList = Lists.newArrayList();
		//模块次要内容
		List<CcCourseOutlineSecondaryContent> ccCourseOutlineSecondaryContentList = Lists.newArrayList();
		//模块表名信息
		List<CcCourseOutlineTableName> ccCourseOutlineTableNameList = Lists.newArrayList();
		//模块学时信息
		List<CcCourseOutlineHours> ccCourseOutlineHours = Lists.newArrayList();
		//模块表头信息
		List<CcCourseOutlineHeader> ccCourseOutlineHeaderList = Lists.newArrayList();
        //模块表格详细信息
		List<CcCourseOutlineTableDetail> CcCourseOutlineTableDetailList = Lists.newArrayList();

		//课程下已关联课程指标点的课程目标列表
		List<CcCourseTargetIndication> ccCourseTargetIndications = CcCourseTargetIndication.dao.findByCourseId(course.getLong("id"));
		List<String> indicationIds = Lists.newArrayList();
		for(CcCourseTargetIndication ccIndication : ccCourseTargetIndications){
            indicationIds.add(String.valueOf(ccIndication.getLong("id")));
		}
		List<String> indicationIdList = Lists.newArrayList();
		//判断大纲是否填写了正确的学时（或周数）
		Boolean isCorrectHours = false;

		for(int i=1 ; i<= moduleMap.size(); i++){
			Map<String, Object> map = moduleMap.get(i-1);
			//模块的基本信息
			String title = ConvertUtils.convert(map.get("title"), String.class);
			String mainContent = map.containsKey("mainContent") ? ConvertUtils.convert(map.get("mainContent"), String.class) : null;
			Boolean isExistMainContent = map.containsKey("isExistMainContent") ? ConvertUtils.convert(map.get("isExistMainContent"), Boolean.class) : null;
			Boolean isExistSecondaryContent = map.containsKey("isExistSecondaryContent") ? ConvertUtils.convert(map.get("isExistSecondaryContent"), Boolean.class) : null;
			Boolean isExistTeachingContent = map.containsKey("isExistTeachingContent") ? ConvertUtils.convert(map.get("isExistTeachingContent"), Boolean.class) : null;
			Boolean isExistTable = map.containsKey("isExistTable") ? ConvertUtils.convert(map.get("isExistTable"), Boolean.class) : null;
            String mainContentIndications = map.containsKey("mainContentIndications") ? ConvertUtils.convert(map.get("mainContentIndications"), String.class) : null;
            Boolean isMainContentSupport = ConvertUtils.convert(map.get("isMainContentSupport"), Boolean.class);
            Boolean isSecondaryContentSupport = ConvertUtils.convert(map.get("isSecondaryContentSupport"), Boolean.class);
            Boolean isTeachingContentSupport = ConvertUtils.convert(map.get("isTeachingContentSupport"), Boolean.class);

            if(isExistMainContent == null){
				return  ServiceResponse.error(String.format("第%s个课程大纲是否存在主要内容不能为空", i));
			}
			if(isExistSecondaryContent == null){
				return  ServiceResponse.error(String.format("第%s个课程大纲是否存在次要内容不能为空", i));
			}
			if(isExistTeachingContent == null){
				return  ServiceResponse.error(String.format("第%s个课程大纲是否存在教学内容不能为空", i));
			}
			if(isExistTable == null){
				return  ServiceResponse.error(String.format("第%s个课程大纲是否存在表格不能为空", i));
			}
            if(isMainContentSupport == null){
                return  ServiceResponse.error(String.format("第%s个课程大纲模块主要内容是否支持课程目标不能为空", i));
            }
            if(isSecondaryContentSupport == null){
                return  ServiceResponse.error(String.format("第%s个课程大纲模块次要内容是否支持课程目标不能为空", i));
            }
            if(isTeachingContentSupport == null){
                return  ServiceResponse.error(String.format("第%s个课程大纲模块教学内容是否支持课程目标不能为空", i));
            }

			if (StringUtils.isBlank(title)) {
				return ServiceResponse.error(String.format("第%s个课程大纲模块的标题不能为空", i));
			}

			if(title.length() > 128){
				return ServiceResponse.error(String.format("第%s个课程大纲模块的标题长度不能大于128", i));
			}

			//教学内容
			List<HashMap> teachingContentMap = map.containsKey("teachingContents") ? ConvertUtils.convert(map.get("teachingContents"),List.class) : new ArrayList<HashMap>();
			//次要内容
			List<HashMap> secondaryContentMap = map.containsKey("secondaryContents") ? ConvertUtils.convert(map.get("secondaryContents"),List.class) : new ArrayList<HashMap>();
			//保存表格信息
			List<HashMap> tables = map.containsKey("tables") ? ConvertUtils.convert(map.get("tables"), List.class) : new ArrayList<HashMap>();

			List<String> indicationArray = Lists.newArrayList();
			List<HashMap> indicationMap = map.containsKey("indications") ? ConvertUtils.convert(map.get("indications"),List.class) : new ArrayList<HashMap>();
            if(!indicationMap.isEmpty()){
            	for(Map<String, Object> temp : indicationMap){
            		Long indicationId = temp.containsKey("indicationId") ? ConvertUtils.convert(temp.get("indicationId"), Long.class) : null;
            		String remark = temp.containsKey("remark") ? ConvertUtils.convert(temp.get("remark"), String.class) : null;

            		if(indicationId == null){
						return  ServiceResponse.error(String.format("第%s个课程大纲模块需要支持的课程目标不完整", i));
					}

					indicationArray.add(String.valueOf(indicationId));

					CcCourseOutlineIndications ccCourseOutlineIndications = new CcCourseOutlineIndications();
					ccCourseOutlineIndications.set("id", idGenerate.getNextValue());
					ccCourseOutlineIndications.set("create_date", date);
					ccCourseOutlineIndications.set("modify_date", date);
					ccCourseOutlineIndications.set("course_outline_id", courseOutlineId);
					ccCourseOutlineIndications.set("indexes", i);
					ccCourseOutlineIndications.set("course_target_indication_id", indicationId);
					ccCourseOutlineIndications.set("remark", remark);
					ccCourseOutlineIndications.set("is_del", Boolean.FALSE);

					ccCourseOutlineIndicationsList.add(ccCourseOutlineIndications);
				}
				//验证是否包含课程已关联的课程目标
/*				if(!indicationArray.containsAll(indicationIds)){
					return  ServiceResponse.error(String.format("第%s个课程大纲模块需要支持的课程目标不完整", i));
				}*/
			}

			CcCourseOutlineModule ccCourseOutlineModule = new CcCourseOutlineModule();
			ccCourseOutlineModule.set("id", idGenerate.getNextValue());
			ccCourseOutlineModule.set("create_date", date);
			ccCourseOutlineModule.set("modify_date", date);
			ccCourseOutlineModule.set("indexes", i);
			ccCourseOutlineModule.set("title", title);
			ccCourseOutlineModule.set("is_exist_main_content", isExistMainContent);
			ccCourseOutlineModule.set("is_exist_secondary_content", isExistSecondaryContent);
			ccCourseOutlineModule.set("is_exist_teaching_content", isExistTeachingContent);
			ccCourseOutlineModule.set("is_exist_table", isExistTable);
			ccCourseOutlineModule.set("main_content", mainContent);
			ccCourseOutlineModule.set(CcCourseOutline.COURSE_OUTLINE_ID, courseOutlineId);
			ccCourseOutlineModule.set("indications", mainContentIndications);
			ccCourseOutlineModule.set("is_main_content_support", isMainContentSupport);
			ccCourseOutlineModule.set("is_secondary_content_support", isSecondaryContentSupport);
			ccCourseOutlineModule.set("is_teaching_content_support", isTeachingContentSupport);
			ccCourseOutlineModule.set("is_del", Boolean.FALSE);

			ccCourseOutlineModuleList.add(ccCourseOutlineModule);

			//教学内容
			if(!teachingContentMap.isEmpty()){
				for(int k=1; k<= teachingContentMap.size(); k++){

					Map<String, Object> temp = teachingContentMap.get(k-1);
					String teachingContent = temp.containsKey("teachingContent") ? ConvertUtils.convert(temp.get("teachingContent"), String.class) : null;
					String basicRequirement = temp.containsKey("basicRequirement") ? ConvertUtils.convert(temp.get("basicRequirement"), String.class) : null;
					String hours = temp.containsKey("hours") ? ConvertUtils.convert(temp.get("hours"), String.class) : null;
					String indications = temp.containsKey("indications") ? ConvertUtils.convert(temp.get("indications"), String.class) : null;
					unRepeatList(indications, indicationIdList);

					CcCourseOutlineTeachingContent ccCourseOutlineTeachingContent = new CcCourseOutlineTeachingContent();
					ccCourseOutlineTeachingContent.set("id", idGenerate.getNextValue());
					ccCourseOutlineTeachingContent.set("create_date", date);
					ccCourseOutlineTeachingContent.set("modify_date", date);
					ccCourseOutlineTeachingContent.set("indexes", i);
					ccCourseOutlineTeachingContent.set("teaching_content", teachingContent);
					ccCourseOutlineTeachingContent.set("hours", hours);
					ccCourseOutlineTeachingContent.set("basic_requirement", basicRequirement);
					ccCourseOutlineTeachingContent.set("indications", indications);
					ccCourseOutlineTeachingContent.set(CcCourseOutline.COURSE_OUTLINE_ID, courseOutlineId);
					ccCourseOutlineTeachingContent.set("is_del", Boolean.FALSE);

					ccCourseOutlineTeachingContentList.add(ccCourseOutlineTeachingContent);

				}
			}
			//次要内容
			if(!secondaryContentMap.isEmpty()){
				for(int k=1; k<= secondaryContentMap.size(); k++ ){
					Map<String, Object> temp = secondaryContentMap.get(k-1);
					String contentTitle = temp.containsKey("title") ? ConvertUtils.convert(temp.get("title"), String.class) : null;
					String content = temp.containsKey("content") ? ConvertUtils.convert(temp.get("content"), String.class) : null;
					String indications = temp.containsKey("indications") ? ConvertUtils.convert(temp.get("indications"), String.class) : null;
					unRepeatList(indications, indicationIdList);


					CcCourseOutlineSecondaryContent ccCourseOutlineSecondaryContent = new CcCourseOutlineSecondaryContent();
					ccCourseOutlineSecondaryContent.set("id", idGenerate.getNextValue());
					ccCourseOutlineSecondaryContent.set("create_date", date);
					ccCourseOutlineSecondaryContent.set("modify_date", date);
					ccCourseOutlineSecondaryContent.set("indexes", i);
					ccCourseOutlineSecondaryContent.set("title", contentTitle);
					ccCourseOutlineSecondaryContent.set("content", content);
					ccCourseOutlineSecondaryContent.set("indications", indications);
					ccCourseOutlineSecondaryContent.set(CcCourseOutline.COURSE_OUTLINE_ID, courseOutlineId);
					ccCourseOutlineSecondaryContent.set("is_del", Boolean.FALSE);

					ccCourseOutlineSecondaryContentList.add(ccCourseOutlineSecondaryContent);
				}
			}


			//保存表格信息
            if(!tables.isEmpty()){
               for(int j=1; j<=tables.size(); j++){
				    Map<String, Object> tableMap = tables.get(j-1);

                    String tableName = tableMap.containsKey("tableName") ? ConvertUtils.convert(tableMap.get("tableName"), String.class) : null;
				    //表名
				    if(StringUtils.isNotBlank(tableName)){
				   	CcCourseOutlineTableName ccCourseOutlineTableName = new CcCourseOutlineTableName();
				   	ccCourseOutlineTableName.set("id", idGenerate.getNextValue());
				   	ccCourseOutlineTableName.set("create_date", date);
				   	ccCourseOutlineTableName.set("modify_date", date);
				   	ccCourseOutlineTableName.set(CcCourseOutline.COURSE_OUTLINE_ID, courseOutlineId);
				   	ccCourseOutlineTableName.set("indexes", i);
				   	ccCourseOutlineTableName.set("number", j);
				   	ccCourseOutlineTableName.set("table_name", tableName);
				   	ccCourseOutlineTableName.set("is_del", Boolean.FALSE);

				   	ccCourseOutlineTableNameList.add(ccCourseOutlineTableName);
				   }

				   Map<Integer, BigDecimal> theoryHoursMap = new HashMap<>();
				   Map<Integer, BigDecimal> practiceHoursMap = new HashMap<>();
				   //保存模块表头信息
				   List<HashMap> headerMap = tableMap.containsKey("headers") ? ConvertUtils.convert(tableMap.get("headers"),List.class) : new ArrayList<HashMap>();
				   if(!headerMap.isEmpty()){
					   for(int n=1; n<=headerMap.size(); n++){
						   Map<String, Object> temp = headerMap.get(n-1);
						   String headerName = temp.containsKey("name") ? ConvertUtils.convert(temp.get("name"), String.class) : null;
						   Integer type = temp.containsKey("type") ? ConvertUtils.convert(temp.get("type"), Integer.class) : null;
						   Integer hoursType = temp.containsKey("hoursType") ? ConvertUtils.convert(temp.get("hoursType"), Integer.class) : null;
						   BigDecimal hours = temp.containsKey("hours") ? NumberUtils.isNumber(ConvertUtils.convert(temp.get("hours"), String.class)) ? ConvertUtils.convert(temp.get("hours"), BigDecimal.class) : null : null;

						   if(StrKit.isBlank(headerName)){
							   return ServiceResponse.error(String.format("第%s个课程大纲模的第%s个表格的第%s个表头名称不能为空", i, j, n));
						   }
						   if(type == null){
							   return ServiceResponse.error(String.format("第%s个课程大纲模的第%s个表格的第%s个表头类型不能为空", i, j, n));
						   }

						   //表头类型为文本或指标点选择时却需要校验证明前端代码有bug
						   Boolean notNeedValidate = CcCourseOutlineHeader.TYPE_TEXT.equals(type) || CcCourseOutlineHeader.TYPE_INDICATION.equals(type);
                           if(notNeedValidate && hoursType != null){
                            	return ServiceResponse.error(String.format("第%s个课程大纲模的第%s个表格的第%s个表头类型为%s,却关联了%s", i, j, n, DictUtils.findLabelByTypeAndKey("headerType", type), DictUtils.findLabelByTypeAndKey("hoursType", hoursType)));
						   }
						   //表头类型为数字或小计时需要校验
						   Boolean isNeedValidate = CcCourseOutlineHeader.TYPE_NUMBER.equals(type) || CcCourseOutlineHeader.TYPE_HOURS_SUBTOTAL.equals(type);
						   if(hoursType != null){
							   if(isNeedValidate && CcCourse.TYPE_THEORY.equals(course.getInt("type")) && (hoursType < 5 || hoursType > 7)){
								   theoryHoursMap.put(hoursType, hours);
							   }else if(isNeedValidate && CcCourse.TYPE_PRACTICE.equals(course.getInt("type")) && (hoursType > 4 && hoursType < 8)){
								   practiceHoursMap.put(hoursType, hours);
							   }
						   }

						   CcCourseOutlineHeader ccCourseOutlineHeader = new CcCourseOutlineHeader();
						   ccCourseOutlineHeader.set("id", idGenerate.getNextValue());
						   ccCourseOutlineHeader.set("create_date", date);
						   ccCourseOutlineHeader.set("modify_date", date);
						   ccCourseOutlineHeader.set("indexes", i);
						   ccCourseOutlineHeader.set("number", j);
						   ccCourseOutlineHeader.set("name", headerName);
						   ccCourseOutlineHeader.set("type", type);
						   ccCourseOutlineHeader.set("column_ordinal", n);
						   ccCourseOutlineHeader.set(CcCourseOutline.COURSE_OUTLINE_ID, courseOutlineId);
						   ccCourseOutlineHeader.set("hours_type", hoursType);
						   ccCourseOutlineHeader.set("total_column", temp.containsKey("totalColumn") ? ConvertUtils.convert(temp.get("totalColumn"), String.class) : null);
						   ccCourseOutlineHeader.set("hours", hours);
						   ccCourseOutlineHeader.set("is_del", Boolean.FALSE);

						   ccCourseOutlineHeaderList.add(ccCourseOutlineHeader);
					   }
				   }

				   //提交时验证学时的正确性
				   BigDecimal allHours = BigDecimal.valueOf(0);
				   BigDecimal courseAllHours = course.getBigDecimal("all_hours");
				   Boolean isExistTheoryHours = !theoryHoursMap.isEmpty();
				   Boolean isExistExperimentHours = !practiceHoursMap.isEmpty();

					   if(CcCourse.TYPE_THEORY.equals(course.getInt("type"))){
					   	  for(Map.Entry<Integer, BigDecimal> entry : theoryHoursMap.entrySet()){
							   String field = DictUtils.findLabelByTypeAndKey("hoursField", entry.getKey());
							   String fieldName = DictUtils.findLabelByTypeAndKey("hoursType", entry.getKey());
							   ServiceResponse serviceResponse = validateHours(course.getBigDecimal(StringUtil.camelToUnderline(field)), entry.getValue(), i, j, fieldName);
							   if(isSubmit){
								   if(!serviceResponse.isSucc()){
									   return ServiceResponse.error(serviceResponse.getContent());
								   }
							   }
							   if(entry.getValue() != null){
								   allHours = PriceUtils._add(allHours, entry.getValue());
							   }
						  }

						  if(isSubmit && isNeedValidateAllHours){
							  //验证理论课总学时是否正确
							  if(isExistTheoryHours && !PriceUtils.eqThan(allHours, courseAllHours)){
								  return ServiceResponse.error(String.format("课程的总学时为%s，大纲的第%s模块的第%s个表格的学时总和为%s,两者不一致请检查后再提交", courseAllHours, i, j, allHours));
							  }else if(isExistTheoryHours && PriceUtils.eqThan(allHours, courseAllHours)){
								  isCorrectHours = true;
							  }
						  }

					   }else{
						   if (isSubmit) {
							   if (practiceHoursMap.size() > 1) {
								   return ServiceResponse.error(String.format("实践课的总学时(或总周数)只能由天数、课内学时、周数的一种构成，大纲的第%s模块的第%s个表格至少有以上两者并存", i, j));
							   }
						   }
						   for (Map.Entry<Integer, BigDecimal> entry : practiceHoursMap.entrySet()) {
							   if(entry.getValue() != null){
								   allHours = PriceUtils._add(allHours, entry.getValue());
							   }
							   //验证实践课总学时(或总周数)是否正确
							   ServiceResponse serviceResponse = validateAllHours(courseAllHours, entry.getValue(), i, j, DictUtils.findLabelByTypeAndKey("hoursType", entry.getKey()));
							   if (isSubmit) {
								   if (!serviceResponse.isSucc()) {
									   return ServiceResponse.error(serviceResponse.getContent());
								   } else {
									   isCorrectHours = true;
								   }
							   }
						   }
					   }

				   if(isExistTheoryHours || isExistExperimentHours){
					   //表格学时
					   CcCourseOutlineHours courseOutlineHours = new CcCourseOutlineHours();
					   courseOutlineHours.set("id", idGenerate.getNextValue());
					   courseOutlineHours.set("create_date", date);
					   courseOutlineHours.set("modify_date", date);
					   courseOutlineHours.set(CcCourseOutline.COURSE_OUTLINE_ID, courseOutlineId);
					   courseOutlineHours.set("indexes", i);
					   courseOutlineHours.set("number", j);
					   courseOutlineHours.set("all_hours", allHours);
					   courseOutlineHours.set("operate_computer_hours",  theoryHoursMap.containsKey(CcCourseOutlineHeader.OPERATECOMPUTER_HOURS) ? theoryHoursMap.get(CcCourseOutlineHeader.OPERATECOMPUTER_HOURS) : course.getBigDecimal("operate_computer_hours"));
					   courseOutlineHours.set("experiment_hours", theoryHoursMap.containsKey(CcCourseOutlineHeader.EXPERIMENT_HOURS) ? theoryHoursMap.get(CcCourseOutlineHeader.EXPERIMENT_HOURS) : course.getBigDecimal("experiment_hours"));
					   courseOutlineHours.set("theory_hours",  theoryHoursMap.containsKey(CcCourseOutlineHeader.THEORY_HOURS) ? theoryHoursMap.get(CcCourseOutlineHeader.THEORY_HOURS) : course.getBigDecimal("theory_hours"));
					   courseOutlineHours.set("practice_hours", theoryHoursMap.containsKey(CcCourseOutlineHeader.PRACTICE_HOURS) ? theoryHoursMap.get(CcCourseOutlineHeader.PRACTICE_HOURS) : course.getBigDecimal("practice_hours"));
					   courseOutlineHours.set("dicuss_hours", theoryHoursMap.containsKey(CcCourseOutlineHeader.DICUSS_HOURS) ? theoryHoursMap.get(CcCourseOutlineHeader.DICUSS_HOURS) : course.getBigDecimal("dicuss_hours"));
					   courseOutlineHours.set("exercises_hours", theoryHoursMap.containsKey(CcCourseOutlineHeader.EXERCISES_HOURS) ? theoryHoursMap.get(CcCourseOutlineHeader.EXERCISES_HOURS) : course.getBigDecimal("exercises_hours"));
					   courseOutlineHours.set("is_del", false);

					   ccCourseOutlineHours.add(courseOutlineHours);

				   }

				   //保存表格详细信息
				   List<HashMap> detailMap = tableMap.containsKey("tableDetails") ? ConvertUtils.convert(tableMap.get("tableDetails"),List.class) : new ArrayList<HashMap>();
				   if(!detailMap.isEmpty()){
					   for(Map<String, Object> temp : detailMap){
						   Integer rowOrdinal = temp.containsKey("rowOrdinal") ? ConvertUtils.convert(temp.get("rowOrdinal"), Integer.class) : null;
						   Integer columnOrdinal = temp.containsKey("columnOrdinal") ? ConvertUtils.convert(temp.get("columnOrdinal"), Integer.class) : null;
						   Integer type = temp.containsKey("type") ? ConvertUtils.convert(temp.get("type"), Integer.class) : null;
						   String content =  temp.containsKey("content") ? ConvertUtils.convert(temp.get("content"), String.class) : null;
						   if(CcCourseOutlineHeader.TYPE_INDICATION.equals(type)){
							   unRepeatList(content, indicationIdList);
						   }

						   if(rowOrdinal == null){
							   return ServiceResponse.error(String.format("第%s个课程大纲模表格行不能为空", i));
						   }
						   if(columnOrdinal == null){
							   return ServiceResponse.error(String.format("第%s个课程大纲模表格列不能为空", i));
						   }
						   if(type == null){
							   return ServiceResponse.error(String.format("第%s个课程大纲模表格列类型不能为空", i));
						   }

						   CcCourseOutlineTableDetail CcCourseOutlineTableDetail = new CcCourseOutlineTableDetail();
						   CcCourseOutlineTableDetail.set("id", idGenerate.getNextValue());
						   CcCourseOutlineTableDetail.set("create_date", date);
						   CcCourseOutlineTableDetail.set("modify_date", date);
						   CcCourseOutlineTableDetail.set("indexes", i);
						   CcCourseOutlineTableDetail.set("number", j);
						   CcCourseOutlineTableDetail.set("row_ordinal", rowOrdinal);
						   CcCourseOutlineTableDetail.set("column_ordinal", columnOrdinal);
						   CcCourseOutlineTableDetail.set("type", type);
						   CcCourseOutlineTableDetail.set("content", content);
						   CcCourseOutlineTableDetail.set(CcCourseOutline.COURSE_OUTLINE_ID, courseOutlineId);
						   CcCourseOutlineTableDetail.set("is_del", Boolean.FALSE);

						   CcCourseOutlineTableDetailList.add(CcCourseOutlineTableDetail);
					   }
				   }
			   }
			}
		}

		BigDecimal courseAllHours = course.getBigDecimal("all_hours");
		if(courseAllHours != null && !PriceUtils.isZero(courseAllHours) && isSubmit && !isCorrectHours && isNeedValidateAllHours){
			return ServiceResponse.error("大纲至少需要一个模块填写全部的正确的课程学时（或周数）的表格数据");
		}

		//todo 建工的不需要验证
		//如果提交的时候课程大纲没有支持课程关联的指标点返回错误信息
/*		if(isSubmit && !indicationIds.isEmpty() && !indicationIdList.containsAll(indicationIds)){
			return ServiceResponse.error("该大纲没有全部支持该门课下已关联指标点的课程目标");
		}

		if(indicationIds.isEmpty() || (!indicationIds.isEmpty() && indicationIdList.containsAll(indicationIds))){
			ccCourseOutline.set("is_support_course_indication", true);
		}*/

		ccCourseOutline.set("is_support_course_indication", true);
		if (!ccCourseOutline.update()) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return ServiceResponse.error("编写课程大纲失败");
		}


		if(!CcCourseOutlineModule.dao.batchSave(ccCourseOutlineModuleList)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return ServiceResponse.error("编写课程大纲失败");
		}

		if(!ccCourseOutlineIndicationsList.isEmpty()){
			if(!CcCourseOutlineIndications.dao.batchSave(ccCourseOutlineIndicationsList)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return ServiceResponse.error("编写课程大纲失败");
			}
		}

		if(!ccCourseOutlineTeachingContentList.isEmpty()){
			if(!CcCourseOutlineTeachingContent.dao.batchSave(ccCourseOutlineTeachingContentList)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return ServiceResponse.error("编写课程大纲失败");
			}
		}

		if(!ccCourseOutlineSecondaryContentList.isEmpty()){
			if(!CcCourseOutlineSecondaryContent.dao.batchSave(ccCourseOutlineSecondaryContentList)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return ServiceResponse.error("编写课程大纲失败");
			}
		}

		if(!ccCourseOutlineHeaderList.isEmpty()){
			if(!CcCourseOutlineHeader.dao.batchSave(ccCourseOutlineHeaderList)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return ServiceResponse.error("编写课程大纲失败");
			}
		}

		if(!CcCourseOutlineTableDetailList.isEmpty()){
			if(!CcCourseOutlineTableDetail.dao.batchSave(CcCourseOutlineTableDetailList)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return  ServiceResponse.error("编写课程大纲失败");
			}
		}

		if(!ccCourseOutlineTableNameList.isEmpty()){
			if(!CcCourseOutlineTableName.dao.batchSave(ccCourseOutlineTableNameList)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return ServiceResponse.error("编写课程大纲失败");
			}
		}

		if(!ccCourseOutlineHours.isEmpty()){
			if(!CcCourseOutlineHours.dao.batchSave(ccCourseOutlineHours)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return ServiceResponse.error("编写课程大纲失败");
			}
		}

		return ServiceResponse.succ(true);
    }
}
