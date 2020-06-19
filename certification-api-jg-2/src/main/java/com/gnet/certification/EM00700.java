package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 获得大纲模板详情
 * 
 * @author xzl
 * 
 * @date 2017年8月1日
 *
 */
@Service("EM00700")
@Transactional(readOnly=true)
public class EM00700 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		
		if (id == null) {
			return renderFAIL("0869", response, header);
		}

		//模板模块信息
        List<CcCourseOutlineTemplateModule> ccCourseOutlineTemplateModuleList = CcCourseOutlineTemplateModule.dao.findByTemplateId(id);
        //模板模块表名称
		List<CcCourseOutlineTemplateTableName> ccCourseOutlineTemplateTableNameList = CcCourseOutlineTemplateTableName.dao.findByTemplateId(id);
		//模板模块表头
		List<CcCourseOutlineTemplateHeader> ccCourseOutlineTemplateHeaderList = CcCourseOutlineTemplateHeader.dao.findByTemplateId(id);

		Map<String, String> tableNameMap = new HashMap<>();
		//表名
		if(!ccCourseOutlineTemplateTableNameList.isEmpty()){
			for(CcCourseOutlineTemplateTableName ccCourseOutlineTemplateTableName : ccCourseOutlineTemplateTableNameList){
				String key = String.format("%s-%s", ccCourseOutlineTemplateTableName.getInt("indexes"), ccCourseOutlineTemplateTableName.getInt("number"));
				//将表头描述与表尾描述拼接，之后再截取即可
				tableNameMap.put(key, ccCourseOutlineTemplateTableName.getStr("table_name")
						+"T"+ccCourseOutlineTemplateTableName.get("table_top_detail")
						+"B"+ccCourseOutlineTemplateTableName.get("table_bottom_detail"));
			}
		}

		//模块序号与标序号集合
		Set<String> set = new HashSet<>();
		Map<String, List<Map<String, Object>>> headerMap = new HashMap<>();
		//模块表头
		if(!ccCourseOutlineTemplateHeaderList.isEmpty()){
			for(CcCourseOutlineTemplateHeader outlineHeader : ccCourseOutlineTemplateHeaderList){
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
				map.put("totalColumn", totalColumn);

				List<Map<String, Object>> headers = headerMap.get(key);
				if(headers == null){
					headers = new ArrayList<>();
					headerMap.put(key, headers);
				}
				headers.add(map);
			}
		}

		Map<Integer, List<Map<String, Object>>> tableMap = new HashMap<>();
		for(String key : set){
			Integer index = Integer.valueOf(key.split("-")[0]);
			Map<String, Object> table = new HashMap<>();
			String tableInfo = tableNameMap.get(key);
			if (tableInfo!=null){
				String tableName = tableInfo.substring(0, tableInfo.indexOf("T"));
				String topDetail = tableInfo.substring(tableInfo.indexOf("T")+1, tableInfo.indexOf("B"));
				String bottomDetail = tableInfo.substring(tableInfo.indexOf("B")+1, tableInfo.length());
				table.put("tableName", tableName);
				table.put("tableTopDetail",topDetail);
				table.put("tableBottomDetail",bottomDetail);
				table.put("headers", headerMap.get(key));
			}else{
				table.put("tableName", null);
				table.put("headers", headerMap.get(key));
			}


			List<Map<String, Object>> tableLists = tableMap.get(index);
			if(tableLists == null){
				tableLists = new ArrayList<>();
				tableMap.put(index, tableLists);
			}
			tableLists.add(table);
		}


		List<Map<String, Object>> moduleLists = Lists.newArrayList();
		for(CcCourseOutlineTemplateModule temp : ccCourseOutlineTemplateModuleList){
            Map<String, Object> map = new HashMap<>();
            Integer index = temp.getInt("indexes");

            map.put("indexes", index);
            map.put("title", temp.getStr("title"));
			map.put("isExistMainContent", temp.getBoolean("is_exist_main_content"));
            map.put("isExistSecondaryContent", temp.getBoolean("is_exist_secondary_content"));
            map.put("isExistTeachingContent", temp.getBoolean("is_exist_teaching_content"));
            map.put("isExistTable", temp.getBoolean("is_exist_table"));
			map.put("isMainContentSupport", temp.getBoolean("is_main_content_support"));
			map.put("isSecondaryContentSupport", temp.getBoolean("is_secondary_content_support"));
			map.put("isTeachingContentSupport", temp.getBoolean("is_teaching_content_support"));
            map.put("mainContent",temp.getStr("main_content"));
            map.put("secondaryCountent",temp.getStr("secondary_countent"));
            map.put("teachingContent",temp.getStr("teaching_content"));

			ArrayList secondaryConutentList = new ArrayList();
			HashMap secondaryConutentMap = new HashMap();
			secondaryConutentMap.put("indexes",index);
			secondaryConutentMap.put("content",temp.getStr("secondary_countent"));
			secondaryConutentList.add(secondaryConutentMap);
			map.put("secondaryContents",secondaryConutentList);

			ArrayList teachingContentList = new ArrayList();
			HashMap teachingContentMap = new HashMap();
			teachingContentMap.put("indexes",index);
			teachingContentMap.put("basicRequirement",temp.getStr("teaching_content"));
			teachingContentList.add(teachingContentMap);

			map.put("teachingContents",teachingContentList);
			if(tableMap.get(index) != null && !tableMap.get(index).isEmpty()){
				map.put("tables", tableMap.get(index));
			}

            moduleLists.add(map);
		}

		List<CcCourseOutlineTemplateInfo> ccCourseOutlineTemplateInfoList = CcCourseOutlineTemplateInfo.dao.findFilteredByColumn("course_outline_template_id" ,id);
        List<Map<String, Object>> infoLists = Lists.newArrayList();
        for(CcCourseOutlineTemplateInfo temp : ccCourseOutlineTemplateInfoList){
        	Map<String, Object> map = new HashMap<>();
        	map.put("name", temp.getStr("name"));
        	map.put("databaseField", temp.getStr("database_field"));

        	infoLists.add(map);

		}

		Map<String, Object> map = new HashMap<>();
		map.put("courseInfos", infoLists);
		map.put("modules", moduleLists);

		return renderSUC(map, response, header);
	}

}
