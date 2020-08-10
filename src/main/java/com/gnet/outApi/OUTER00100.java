package com.gnet.outApi;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourse;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 版本下选修课程和必修课程列表
 * 
 * @author xzl
 * 
 * @date 2017年10月27日
 */
@Service("OUTER00100")
@Transactional(readOnly=true)
public class OUTER00100 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Long version = paramsLongFilter(param.get("version"));
		
		if (version == null) {
			return renderFAIL("0140", response, header, "版本编号不能为空");
		}

		Map<String, Object> result = Maps.newHashMap();
        List<Map<String, Object>> required_courses = Lists.newArrayList();
        List<Map<String, Object>> opional_courses = Lists.newArrayList();

        List<CcCourse> ccCourses = CcCourse.dao.findByPlanId(version);
        for(CcCourse ccCourse : ccCourses){
			Map<String, Object> require = Maps.newHashMap();
			Map<String, Object> opional = Maps.newHashMap();
        	String propertyName = ccCourse.getStr("property_name");
        	String code = ccCourse.getStr("code");
        	String courseName = ccCourse.getStr("courseName");
			BigDecimal credit = ccCourse.getBigDecimal("credit");

			result.put("id", ccCourse.getLong("id"));
			result.put("name", ccCourse.getStr("versionName"));
			result.put("major_code", ccCourse.getStr("major_code"));
			result.put("institute_code", ccCourse.getStr("institute_code"));
			result.put("school_code", ccCourse.getStr("school_code"));
			result.put("year", ccCourse.getStr("apply_grade"));

			if(CcCourse.TYPE_PRACTICE.equals(ccCourse.getInt("type")) || (propertyName.contains("必修"))){
				require.put("code", code);
				require.put("name", courseName);
				require.put("credit", credit);
				required_courses.add(require);
			}else {
				opional.put("code", code);
				opional.put("name", courseName);
				opional.put("credit", credit);
				opional_courses.add(opional);
			}
		}

        result.put("required_courses", required_courses);
		result.put("opional_courses", opional_courses);

		return renderSUC(result, response, header);
	}

}
