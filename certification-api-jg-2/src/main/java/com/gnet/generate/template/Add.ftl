package com.gnet.generate.template;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.gnet.Constant;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.${moduleName};
import com.gnet.model.admin.User;
import com.gnet.model.admin.UserRole;
import com.gnet.utils.CollectionKit;
import com.gnet.utils.PasswdKit;
import com.jfinal.kit.StrKit;

/**
 * 增加${moduleCName}
 * 
 * @author ${author}
 * 
 * @date ${currentDate?string("yyyy年MM月dd日 HH:mm:ss")}
 *
 */
@Service("${API_ADD}")
@Transactional(readOnly=false)
public class ${API_ADD} extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		<#list records as record>
		<#if record.COLUMN_CAMEL_NAME != "id" && record.COLUMN_CAMEL_NAME != "createDate" && record.COLUMN_CAMEL_NAME != "modifyDate" && record.COLUMN_CAMEL_NAME != "isDel" >
		<#if record.DATA_TYPE == "varchar" || record.DATA_TYPE == "text">
		String ${record.COLUMN_CAMEL_NAME} = paramsStringFilter(param.get("${record.COLUMN_CAMEL_NAME}"));
		<#elseif record.DATA_TYPE == "int">
		Integer ${record.COLUMN_CAMEL_NAME} = paramsIntegerFilter(param.get("${record.COLUMN_CAMEL_NAME}"));
		<#elseif record.DATA_TYPE == "bigint">
		Long ${record.COLUMN_CAMEL_NAME} = paramsLongFilter(param.get("${record.COLUMN_CAMEL_NAME}"));
		<#elseif record.DATA_TYPE == "decimal">
		BigDecimal ${record.COLUMN_CAMEL_NAME} = paramsBigDecimalFilter(param.get("${record.COLUMN_CAMEL_NAME}"));
		<#elseif record.DATA_TYPE == "double">
		Double ${record.COLUMN_CAMEL_NAME} = paramsDoubleFilter(param.get("${record.COLUMN_CAMEL_NAME}"));
		<#elseif record.DATA_TYPE == "bool">
		Bool ${record.COLUMN_CAMEL_NAME} = paramsBoolFilter(param.get("${record.COLUMN_CAMEL_NAME}"));
		</#if>
		</#if>
		</#list>
		<#list records as record>
		<#if record.IS_NULLABLE == "NO">
		<#if record.COLUMN_CAMEL_NAME != "id" && record.COLUMN_CAMEL_NAME != "createDate" && record.COLUMN_CAMEL_NAME != "modifyDate" && record.COLUMN_CAMEL_NAME != "isDel">
		<#if record.DATA_TYPE == "varchar" || record.DATA_TYPE == "text">
		if (StrKit.isBlank(${record.COLUMN_CAMEL_NAME})) {
			return renderFAIL("${errorCode.get(record.COLUMN_CAMEL_NAME)}", response, header);
		}
		<#else>
		if (${record.COLUMN_CAMEL_NAME} == null) {
			return renderFAIL("${errorCode.get(record.COLUMN_CAMEL_NAME)}", response, header);
		}
		</#if>
		</#if>
		</#if>
		</#list>
		Date date = new Date();
		
		${moduleName} ${_moduleName} = new ${moduleName}();
		
		<#list records as record>
		<#if record.COLUMN_CAMEL_NAME != "id">
		<#if record.COLUMN_CAMEL_NAME == "createDate" || record.COLUMN_CAMEL_NAME == "modifyDate">
		${_moduleName}.set("${record.COLUMN_NAME}", date);
		<#elseif record.COLUMN_CAMEL_NAME == "isDel">
		${_moduleName}.set("${record.COLUMN_NAME}", Boolean.FALSE);
		<#else>
		${_moduleName}.set("${record.COLUMN_NAME}", ${record.COLUMN_CAMEL_NAME});
		</#if>
		</#if>
		</#list>
		Boolean isSuccess = ${_moduleName}.save();
		Map<String, Object> result = new HashMap<>();
		result.put("isSuccess", isSuccess);
		result.put("id", ${_moduleName}.getLong("id"));
		
		return renderSUC(result, response, header);
	}
}
