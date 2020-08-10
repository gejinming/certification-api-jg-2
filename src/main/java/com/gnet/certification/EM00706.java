package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseOutlineType;
import com.jfinal.kit.StrKit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 查看教学大纲类型详情
 * 
 * @author xzl
 * 
 * @date 2017-08-22 19:44:18
 *
 */
@Service("EM00706")
@Transactional(readOnly=false)
public class EM00706 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();

		Long id = paramsLongFilter(param.get("id"));

		if(id == null){
			return renderFAIL("0892", response, header);
		}

		CcCourseOutlineType ccCourseOutlineType = CcCourseOutlineType.dao.findFilteredById(id);
		if(ccCourseOutlineType == null){
			return  renderFAIL("0893", response, header);
		}

		Map<String, Object> map = new HashMap<>();
		map.put("id", ccCourseOutlineType.getLong("id"));
		map.put("createDate", ccCourseOutlineType.getDate("create_date"));
		map.put("modifyDate", ccCourseOutlineType.getDate("modify_date"));
		map.put("name", ccCourseOutlineType.getStr("name"));

		return renderSUC(map, response, header);
	}
}
