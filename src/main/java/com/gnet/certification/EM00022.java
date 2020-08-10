package com.gnet.certification;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.Office;
import com.gnet.model.admin.OfficePath;
import com.gnet.utils.DictUtils;
import com.google.common.collect.Maps;

/**
 * 查看某一部门详情信息
 * 
 * @author wct
 * @Date 2016年6月28日
 */
@Service("EM00022")
public class EM00022 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long id = paramsLongFilter(params.get("id"));
		// 部门编号为空过滤
		if (id == null) {
			return renderFAIL("0056", response, header);
		}
		
		// 获取对应部门以及部门路径
		Office office = getOffice(id);
		if (office == null) {
			return renderFAIL("0057", response, header);
		}
		OfficePath officePath = getOfficePath(id);
		if (officePath == null) {
			return renderFAIL("0058", response, header);
		}
		// 返回结果
		String path = officePath.getStr("office_ids");
		Map<String, Object> result = Maps.newHashMap();
		result.put("id", office.getLong("id"));
		result.put("createDate", office.getDate("create_date"));
		result.put("parentId", office.getLong("parentid"));
		result.put("code", office.getStr("code"));
		result.put("name", office.getStr("name"));
		result.put("type", Integer.valueOf(office.getStr("type")));
		result.put("typeName", DictUtils.findLabelByTypeAndKey("sysOffice", Integer.valueOf(office.getStr("type"))));
		result.put("isSystem", office.getBoolean("is_system"));
		result.put("description", office.getStr("description"));
		result.put("path", path.substring(1, path.length() - 1).split(",,"));
		return renderSUC(result, response, header);
	}
	
	/**
	 * 获得部门
	 * @param id
	 * @return
	 */
	private Office getOffice(Long id) {
		return Office.dao.findById(id);
	}
	
	/**
	 * 获得部门路径
	 * @param id
	 * @return
	 */
	private OfficePath getOfficePath(Long id) {
		return OfficePath.dao.findById(id);
	}

}
