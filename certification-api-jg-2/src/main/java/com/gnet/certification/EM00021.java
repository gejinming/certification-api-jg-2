package com.gnet.certification;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.Office;
import com.gnet.utils.DictUtils;

/**
 * 显示部门列表
 * @author wct
 * @Date 2016年6月17日
 */
@Service("EM00021")
public class EM00021 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		String code = paramsStringFilter(params.get("code"));
		String name = paramsStringFilter(params.get("name"));
		
		List<Office> offices = findAll(name, code);
		// 返回结果
		JSONArray result = new JSONArray();
		for (Office office : offices) {
			JSONObject object = new JSONObject();
			object.put("id", office.getLong("id"));
			object.put("createDate", office.getDate("create_date"));
			object.put("parentId", office.getLong("parentid"));
			object.put("code", office.getStr("code"));
			object.put("name", office.getStr("name"));
			object.put("type", Integer.valueOf(office.getStr("type")));
			object.put("typeName", DictUtils.findLabelByTypeAndKey("sysOffice",Integer.valueOf(office.getStr("type"))));
			object.put("description", office.getStr("description"));
			result.add(object);
		}
		
		return renderSUC(result, response, header);
	}
	
	/**
	 * 获得所有部门
	 * 
	 * @param name
	 * @param code
	 * @return
	 */
	private List<Office> findAll(String name, String code) {
		return Office.dao.findAll(name, code);
	}
			

}
