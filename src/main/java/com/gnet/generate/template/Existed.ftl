package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.${moduleName};
import com.gnet.model.admin.User;
import com.jfinal.kit.StrKit;

/**
 * 检查${uniqueCName}是否唯一
 * 
 * @author ${author}
 * @Date ${currentDate?string("yyyy年MM月dd日 HH:mm:ss")}
 */
@Service("${API_EXISTED}")
public class ${API_EXISTED} extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		${uniqueType} ${uniqueNameCamel} = params${uniqueType}Filter(params.get("${uniqueNameCamel}"));
		${uniqueType} originValue = params${uniqueType}Filter(params.get("originValue"));
		
		// ${uniqueCName}不能为空过滤
		<#list records as record>
		<#if record.COLUMN_NAME == uniqueNameCamel>
		<#if uniqueType == "String">
		if (StrKit.isBlank(${record.COLUMN_CAMEL_NAME})) {
			return renderFAIL("${errorCode.get(record.COLUMN_CAMEL_NAME)}", response, header);
		}
		<#else>
		if (${record.COLUMN_CAMEL_NAME} == null) {
			return renderFAIL("${errorCode.get(record.COLUMN_CAMEL_NAME)}", response, header);
		}
		</#if>
		</#if>
		</#list>
		
		// 结果返回
		Map<String, Boolean> result = new HashMap<>();
		result.put("isExisted", ${moduleName}.dao.isExisted(${uniqueNameCamel}, originValue));
		return renderSUC(result, response, header);
	}

}
