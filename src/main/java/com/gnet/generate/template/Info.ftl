package com.gnet.certification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.${moduleName};
import com.gnet.utils.DictUtils;
import com.jfinal.kit.StrKit;

/**
 * 查看${moduleCName}详情
 * 
 * @author ${author}
 * 
 * @date ${currentDate?string("yyyy年MM月dd日 HH:mm:ss")}
 *
 */
@Service("${API_VIEW}")
@Transactional(readOnly=true)
public class ${API_VIEW} extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		
		if (id == null) {
			return renderFAIL("${idErrorCode}", response, header);
		}
		
		${moduleName} temp = ${moduleName}.dao.findFilteredById(id);
		if(temp == null) {
			return renderFAIL("${targetErrorCode}", response, header);
		}
		
		Map<String, Object> map = new HashMap<>();
		
		<#list records as record>
		map.put("${record.COLUMN_CAMEL_NAME}", temp.get("${record.COLUMN_NAME}"));
		</#list>
		
		return renderSUC(map, response, header);
	}

}
