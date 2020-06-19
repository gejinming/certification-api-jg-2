package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcLevelDetail;
import com.jfinal.kit.StrKit;

/**
 * 新增等级制度明细某条信息
 * 
 * @author SY
 * @Date 2019年12月9日13:50:40
 */
@Service("EM01162")
public class EM01162 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 各种其他信息
		String name = paramsStringFilter(params.get("name"));
		Integer value = paramsIntegerFilter(params.get("value"));
		String remark = paramsStringFilter(params.get("remark"));
        Long levelId = paramsLongFilter(params.get("levelId"));
		
		// levelId不能为空信息的过滤
		if (levelId == null) {
			return renderFAIL("0140", response, header);
		}
		// value不能为空信息的过滤
		if (value == null) {
			return renderFAIL("0102", response, header);
		}
		// name不能为空信息的过滤
		if (StrKit.isBlank(name)) {
			return renderFAIL("0102", response, header);
		}
		
		Date date = new Date();
		CcLevelDetail ccLevelDetail = new CcLevelDetail();
		ccLevelDetail.set("create_date", date);
		ccLevelDetail.set("modify_date", date);
		ccLevelDetail.set("level_id", levelId);
		ccLevelDetail.set("name", name);
		ccLevelDetail.set("value", value);
		ccLevelDetail.set("remark", remark);
		ccLevelDetail.set("is_del", Boolean.FALSE);
		
		// 保存这个信息
		Boolean saveResult = ccLevelDetail.save();
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		// 返回操作是否成功
		result.put("isSuccess", saveResult);
		return renderSUC(result, response, header);
	}
	
}
