package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcClass;
import com.gnet.service.OfficeService;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.kit.StrKit;

/**
 * 检查行政班名称是否唯一
 * 
 * @author xzl
 * @Date 2016年8月1日18:46:18
 */
@Service("EM00255")
public class EM00255 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		
		String name = paramsStringFilter(param.get("name"));
		//专业编号
		Long majorId = paramsLongFilter(param.get("majorId"));
		//行政班编号,增加的时候不需要传递，编辑时需要传递
		Long classId = paramsLongFilter(param.get("classId"));
		// 结果返回
		Map<String, Object> result = new HashMap<String, Object>();

		// 行政班名称不能为空过滤
		if (StrKit.isBlank(name)) {
			return renderFAIL("0298", response, header);
		}
		if(majorId == null){
			return renderFAIL("0283", response, header);
		}
		
		OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
		Long schoolId = officeService.getPathByOfficeId(majorId).get(0);
			
		result.put("isExistedName", CcClass.dao.isExistedName(schoolId, name, classId));
		return renderSUC(result, response, header);
	}

}
