package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseOutlineType;
import com.gnet.service.CcCourseOutlineTypeService;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.kit.StrKit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 增加教学大纲类型接口
 * 
 * @author xzl
 * 
 * @date 2017-08-22 19:44:18
 *
 */
@Service("EM00704")
@Transactional(readOnly=false)
public class EM00704 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Map<String, Object> result = new HashMap<>();
		
		String name = paramsStringFilter(param.get("name"));
		String token = request.getHeader().getToken();
		Long schoolId = UserCacheKit.getSchoolId(token);

		if(StrKit.isBlank(name)){
			return  renderFAIL("0890", response, header);
		}

		if(schoolId == null){
			return renderFAIL("0084", response, header);
		}

		if(CcCourseOutlineType.dao.isRepeatName(name, null, schoolId)){
			return  renderFAIL("0891", response, header);
		}
		CcCourseOutlineTypeService ccCourseOutlineTypeService =SpringContextHolder.getBean(CcCourseOutlineTypeService.class);

		result.put("isSuccess", ccCourseOutlineTypeService.saveOutlineType(name, schoolId));
		return renderSUC(result, response, header);
	}
}
