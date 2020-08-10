package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcGraduate;
import com.jfinal.kit.StrKit;

/**
 * 增加毕业要求
 * 
 * @author SY
 * 
 * @date 2016年06月24日 20:55:57
 *
 */
@Service("EM00122")
@Transactional(readOnly=false)
public class EM00122 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Long graduateVerId = paramsLongFilter(param.get("graduateVerId"));
		Integer indexNum = paramsIntegerFilter(param.get("indexNum"));
		String content = paramsStringFilter(param.get("content"));
		String remark = paramsStringFilter(param.get("remark"));
		if (graduateVerId == null) {
			return renderFAIL("0181", response, header);
		}
		if (indexNum == null) {
			return renderFAIL("0182", response, header);
		}
		if (StrKit.isBlank(content)) {
			return renderFAIL("0183", response, header);
		}
		Date date = new Date();
		
		CcGraduate ccGraduate = new CcGraduate();
		
		ccGraduate.set("create_date", date);
		ccGraduate.set("modify_date", date);
		ccGraduate.set("graduate_ver_id", graduateVerId);
		ccGraduate.set("index_num", indexNum);
		ccGraduate.set("content", content);
		ccGraduate.set("remark", remark);
		ccGraduate.set("is_del", Boolean.FALSE);
		
		Map<String, Object> result = new HashMap<>();
		result.put("isSuccess", ccGraduate.save());
		
		return renderSUC(result, response, header);
	}
	
}
