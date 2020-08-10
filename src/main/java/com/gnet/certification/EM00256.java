package com.gnet.certification;

import java.util.HashMap;
import java.util.List;
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
 * 检查行政班编码是否唯一
 * 
 * @author xzl
 * @Date 2016年8月1日18:51:40
 */
@Service("EM00256")
public class EM00256 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		
		String code = paramsStringFilter(param.get("code"));
		//专业编号
		Long majorId = paramsLongFilter(param.get("majorId"));
		//行政班编号,增加的时候不需要传递，编辑时需要传递
		Long classId = paramsLongFilter(param.get("classId"));
		// 结果返回
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(StrKit.isBlank(code)){
			return renderFAIL("0300", response, header);
		}
		
		if(majorId == null){
			return renderFAIL("0283", response, header);
		}
		
		OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
		List<Long> officeIdList = officeService.getPathByOfficeId(majorId);
		if(officeIdList.isEmpty()){
			return renderFAIL("0390", response, header);
		}
		Long schoolId = officeIdList.get(0);
		
		result.put("isExistedCode", CcClass.dao.isExistedCode(schoolId, code, classId));
		return renderSUC(result, response, header);
	}

}
