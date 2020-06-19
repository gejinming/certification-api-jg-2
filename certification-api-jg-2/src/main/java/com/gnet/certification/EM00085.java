package com.gnet.certification;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.plugin.configLoader.ConfigUtils;
import com.google.common.collect.Maps;

/**
 * 获取专业负责人角色信息
 * 
 * @author SY
 * @Date 2016年6月21日13:54:18
 */
@Service("EM00085")
public class EM00085 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		// 各种其他信息
		Long roleId = ConfigUtils.getLong("global", "role.major_director_id");
		// 结果返回
		Map<String, Object> result = Maps.newHashMap();
		// 返回操作结果
		result.put("roleId", roleId);
		return renderSUC(result, response, header);
	}
	
}
