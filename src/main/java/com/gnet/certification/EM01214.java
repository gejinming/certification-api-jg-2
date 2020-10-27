package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCoursePeriode;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.SpringContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 查询开启课程毕业设计
 * 
 * @author GJM
 * @Date 2020年08月26日
 */
@Service("EM01214")
public class EM01214 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		HashMap<Object, Object> result = new HashMap<>();
		//课程id
		Long courseId = paramsLongFilter(param.get("courseId"));
		Long classId = paramsLongFilter(param.get("classId"));
		if (courseId==null){
			return renderFAIL("0250", response, header);
		}


		CcCoursePeriode coursePeriode = CcCoursePeriode.dao.findCoursePeriode(courseId,classId);
		if (coursePeriode !=null){
			result.put("periodDate",coursePeriode.get("period_date"));
		}

		return renderSUC(result, response, header);
	}


}
