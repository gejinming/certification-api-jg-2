package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseOutline;
import com.gnet.model.admin.CcCourseOutlineType;
import com.jfinal.kit.StrKit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 删除教学大纲类型接口
 * 
 * @author xzl
 * 
 * @date 2017-08-22 20:22:15
 *
 */
@Service("EM00708")
@Transactional(readOnly=false)
public class EM00708 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> params = request.getData();
		Map<String, Object> result = new HashMap<>();

		List<Long> ids = paramsJSONArrayFilter(params.get("ids"), Long.class);
		if(ids.isEmpty()){
			result.put("isSuccess", true);
			return renderSUC(result, response, header);
		}

		Long[] idsArray = ids.toArray(new Long[ids.size()]);

		List<CcCourseOutline> ccCourseOutlines = CcCourseOutline.dao.findFilteredByColumnIn("outline_type_id", idsArray);
		if(!ccCourseOutlines.isEmpty()){
			return renderFAIL("0896", response, header);
		}

		if(!CcCourseOutlineType.dao.deleteAll(idsArray, new Date())){
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}
}
