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
import com.gnet.model.admin.School;
import com.gnet.service.UserService;
import com.jfinal.kit.StrKit;

/**
 * 查看学校详情
 * 
 * @author zsf
 * 
 * @date 2016年06月25日 18:39:35
 *
 */
@Service("EM00131")
@Transactional(readOnly=true)
public class EM00131 extends BaseApi implements IApi  {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		
		if (id == null) {
			return renderFAIL("0200", response, header);
		}
				
		School school = School.dao.findById(id);
		
		if(school == null) {
			return renderFAIL("0201", response, header);
		}
		
		Map<String, Object> result = new HashMap<>();
		// 判断有无学校管理员，若有显示其loginName
		String signName = school.getStr("loginName");
		if (StrKit.notBlank(signName)) {
			school.put("loginName", UserService.handleLoginName(signName));
		}
		result.put("id", school.getLong("id"));
		result.put("name", school.getStr("name"));
		result.put("loginName", school.getStr("loginName"));
		result.put("description", school.getStr("description"));
		
		return renderSUC(result, response, header);
	}

}
