package com.gnet.outApi;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourse;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 * 课程详情
 * @author xzl
 * 
 * @date 2017年10月30日
 */
@Service("OUTER00200")
@Transactional(readOnly=true)
public class OUTER00200 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Long version = paramsLongFilter(param.get("version"));
		String code = paramsStringFilter(param.get("code"));
		
		if (version == null) {
			return renderFAIL("0140", response, header, "版本编号不能为空");
		}
		if(StrKit.isBlank(code)){
			return renderFAIL("0253", response, header, "课程代码不能为空");
		}

		Map<String, Object> result = Maps.newHashMap();

		CcCourse ccCourse = CcCourse.dao.findDetailByCode(version, code);
		if(ccCourse == null){
			return renderFAIL("0251", response, header, "获取的课程信息为空");
		}

		result.put("id", ccCourse.getLong("id"));
		result.put("code", ccCourse.getStr("code"));
		result.put("name", ccCourse.getStr("name"));
		result.put("credit", ccCourse.getBigDecimal("credit"));
		result.put("week_time", ccCourse.getBigDecimal("week_hour"));
		result.put("total_time", ccCourse.getBigDecimal("all_hours"));
		result.put("type", ccCourse.getInt("type"));
		result.put("description", ccCourse.getStr("remark"));
		result.put("major_code", ccCourse.getStr("major_code"));
		result.put("institute_code", ccCourse.getStr("institute_code"));
		result.put("school_code", ccCourse.getStr("school_code"));

		return renderSUC(result, response, header);
	}

}
