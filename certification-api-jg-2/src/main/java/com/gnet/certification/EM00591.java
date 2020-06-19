package com.gnet.certification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcTeacher;
import com.gnet.utils.DictUtils;
import com.google.common.collect.Maps;

/**
 * 统计同一专业下各个学位的人数
 * 
 * @author XZL
 * @Date 2016年7月20日
 */
@Service("EM00591")
public class EM00591 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		
		Long majorId = paramsLongFilter(param.get("majorId"));
		
		if(majorId == null){
			return renderFAIL("0130", response, header);
		}
		
		Long sum = CcTeacher.dao.getTeacherNum(majorId);
		List<CcTeacher> teachers = CcTeacher.dao.getDifferentHighestDegreesTeacherNum(majorId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		//返回内容
		List<Map<String, Object>> list = new ArrayList<>();
		for(CcTeacher temp: teachers){
			Map<String, Object> teacher =  Maps.newHashMap();
		    teacher.put("highestDegrees", DictUtils.findLabelByTypeAndKey("highestDegrees", temp.getInt("highestDegrees")));
			teacher.put("number", temp.getLong("number") == null ? 0L : temp.getLong("number"));
			list.add(teacher);
		}
		
		result.put("list", list);
		result.put("sum", sum);
		return renderSUC(result, response, header);
	}
	
}
