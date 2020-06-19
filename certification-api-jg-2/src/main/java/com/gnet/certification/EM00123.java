package com.gnet.certification;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcGraduate;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;

/**
 * 编辑毕业要求
 * 
 * @author SY
 * 
 * @date 2016年06月24日 20:55:57
 *
 */
@Service("EM00123")
@Transactional(readOnly=false)
public class EM00123 extends BaseApi implements IApi{

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		Long graduateVerId = paramsLongFilter(param.get("graduateVerId"));
		Integer indexNum = paramsIntegerFilter(param.get("indexNum"));
		String content = paramsStringFilter(param.get("content"));
		String remark = paramsStringFilter(param.get("remark"));
		
		if (id == null) {
			return renderFAIL("0180", response, header);
		}
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
		CcGraduate ccGraduate = CcGraduate.dao.findFilteredById(id);
		if (ccGraduate == null) {
			return renderFAIL("0184", response, header);
		}
		ccGraduate.set("modify_date", date);
		ccGraduate.set("graduate_ver_id", graduateVerId);
		ccGraduate.set("index_num", indexNum);
		ccGraduate.set("content", content);
		ccGraduate.set("remark", remark);
		
		
		Map<String, Object> result = Maps.newHashMap();
		result.put("isSuccess", ccGraduate.update());
		return renderSUC(result, response, header);
	}
	
}
