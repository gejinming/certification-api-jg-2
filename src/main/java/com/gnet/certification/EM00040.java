package com.gnet.certification;

import java.util.Iterator;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.utils.DictUtils;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;

/**
 * 静态常量列表接口
 * 
 * @author wct
 * @Date 2016年7月4日
 */
@Service("EM00040")
public class EM00040 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		String type = paramsStringFilter(params.get("type"));
		
		// 类型为空时传回全部的静态常量
		Map<String, Object> result = Maps.newHashMap();
		if (StrKit.isBlank(type)) {
			JSONArray array = DictUtils.findDicts();
			Iterator<Object> iter = array.iterator();
			while (iter.hasNext()) {
				JSONObject object = (JSONObject) iter.next();
				result.put(object.getString("type"), object);
			}
		} else {
			JSONObject object = DictUtils.findDicByType(type);
			result.put(object.getString("type"), object);
		}
		return renderSUC(result, response, header);
		
	}

}
