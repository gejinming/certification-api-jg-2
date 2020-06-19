package com.gnet.certification;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.Office;
import com.gnet.model.admin.User;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;

/**
 * 获取当前用户所在部门的部门树的接口
 * 
 * @author sll
 *
 */
@Service("EM00025")
public class EM00025 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> params = request.getData();
		String level = paramsStringFilter(params.get("level"));
		Boolean includeSchool = paramsBooleanFilter(params.get("includeSchool"));
		includeSchool = includeSchool == null ? Boolean.FALSE : includeSchool;
		String token = request.getHeader().getToken();
		User user = UserCacheKit.getUser(token);
		
		if (level == null) {
			return renderFAIL("0052", response, header);
		}
		
		if(StrKit.isBlank(user.getStr("department"))){
			return renderFAIL("0061", response, header);
		}
		
		//根据部门层次和学校编号查找部门
		List<Office> officesList = Office.dao.findDepartmentTreeByLevel(level, user.getStr("department"), includeSchool);
		
		//返回数据过滤
		List<Map<String, Object>> offices = Lists.newArrayList();
		for (Office temp: officesList) {
			Map<String, Object> office = Maps.newHashMap();
			office.put("id", temp.getLong("id"));
			office.put("createDate", temp.getDate("create_date"));
			office.put("modifyDate", temp.getDate("modifyDate"));
			office.put("parentId", temp.getLong("parentid"));
			office.put("parentName", temp.getStr("parentName"));
			office.put("code", temp.getStr("code"));
			office.put("name", temp.getStr("name"));
			office.put("type", temp.getStr("type"));
			office.put("isSystem", temp.getBoolean("is_del"));
			office.put("description", temp.getStr("description"));
			office.put("grade", temp.getInt("grade"));
			offices.add(office);
		}
		
		Map<String, Object> result = Maps.newHashMap();
		result.put("offices", offices);
		
		return renderSUC(result, response, header);
	}

}
