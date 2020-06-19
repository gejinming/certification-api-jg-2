package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcVersion;

/**
 * 返回某个专业的最小启用年级
 * 
 * @author xzl
 * 
 * @date 2016年11月1日
 *
 */
@Service("EM00641")
@Transactional(readOnly=true)
public class EM00641 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long majorId = paramsLongFilter(param.get("majorId"));
		
		if (majorId == null) {
			return renderFAIL("0130", response, header);
		}
		
		Map<String, Object> map = new HashMap<>();
		CcVersion version = CcVersion.dao.findMinGradeByMajorId(majorId);
		if(version != null){
			map.put("grade", version.getInt("enable_grade"));
		}
		
		return renderSUC(map, response, header);
	}

}
