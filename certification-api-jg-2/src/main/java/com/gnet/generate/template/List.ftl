package com.gnet.generate.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.Constant;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.${moduleName};
import com.gnet.object.${moduleName}OrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;
import com.gnet.utils.DictUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看${moduleCName}列表
 * 
 * @author ${author}
 * 
 * @date ${currentDate?string("yyyy年MM月dd日 HH:mm:ss")}
 * 
 */
@Service("${API_LIST}")
@Transactional(readOnly=true)
public class ${API_LIST} extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		<#list records as record>
		<#if record.isSearch>
		${record.searchProps.type} ${record.COLUMN_CAMEL_NAME} = params${record.searchProps.type}Filter(param.get("${record.COLUMN_CAMEL_NAME}"));
		</#if>
		</#list>
		
		// 进行分页
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, ${moduleName}OrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		List<${moduleName}> ${_moduleName}List = null;
		Map<String, Object> ${_moduleName}sMap = Maps.newHashMap();
		// 判断是否分页
		if (pageNumber == null && pageSize == null) {
			<#assign num = 0>
			${_moduleName}List = ${moduleName}.dao.findAll(pageable, <#list records as record><#if record.isSearch><#if num != 0>, </#if><#assign num = num + 1>${record.COLUMN_CAMEL_NAME}</#if></#list>);
		} else {
			
			Page<${moduleName}> ${_moduleName}Page = page(pageable<#list records as record><#if record.isSearch>, ${record.COLUMN_CAMEL_NAME}</#if></#list>);
			${_moduleName}List = ${_moduleName}Page.getList();
			
			${_moduleName}sMap.put("totalRow", ${_moduleName}Page.getTotalRow());
			${_moduleName}sMap.put("totalPage", ${_moduleName}Page.getTotalPage());
			${_moduleName}sMap.put("pageSize", ${_moduleName}Page.getPageSize());
			${_moduleName}sMap.put("pageNumber", ${_moduleName}Page.getPageNumber());
			
		}
		
		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (${moduleName} temp : ${_moduleName}List) {
			Map<String, Object> ${_moduleName} = new HashMap<>();
			<#list records as record>
			${_moduleName}.put("${record.COLUMN_CAMEL_NAME}", temp.get("${record.COLUMN_NAME}"));
			</#list>
			list.add(${_moduleName});
		}
		
		${_moduleName}sMap.put("list", list);
		
		return renderSUC(${_moduleName}sMap, response, header);
	}
	
	/**
	 * 查看${moduleCName}列表分页
	 * 
	 <#list records as record>
	 <#if record.isSearch>
	 * @param ${record.COLUMN_CAMEL_NAME}
	 </#if>
	 </#list>
	 * @return
	 */
	private Page<${moduleName}> page(Pageable pageable<#list records as record><#if record.isSearch>, ${record.searchProps.type} ${record.COLUMN_CAMEL_NAME}</#if></#list>) {
		StringBuilder exceptSql = new StringBuilder("from " + ${moduleName}.dao.tableName + " ");
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append("where is_del = ? ");
		params.add(Boolean.FALSE);
		
		// 删选条件
		<#list records as record>
		<#if record.isSearch>
		<#if record.searchProps.type == "String">
		if (!StrKit.isBlank(${record.COLUMN_CAMEL_NAME})) {
			exceptSql.append("and ${record.COLUMN_NAME} like '" + StringEscapeUtils.escapeSql(${record.COLUMN_CAMEL_NAME}) + "%' ");
		}
		<#else>
		if (${record.COLUMN_CAMEL_NAME} != null) {
			exceptSql.append("and ${record.COLUMN_NAME} = ? ");
			params.add(${record.COLUMN_CAMEL_NAME});
		}
		</#if>
		</#if>
		</#list>
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return ${moduleName}.dao.paginate(pageable.getPageNumber(), pageable.getPageSize(), "select * ", exceptSql.toString(), params.toArray());
	}

}
