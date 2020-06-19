package com.gnet.certification;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcLevel;
import com.gnet.model.admin.CcLevelDetail;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.kit.StrKit;

/**
 * 新增等级制度某条信息
 * 
 * @author SY
 * @Date 2019年12月9日13:50:40
 */
@Service("EM01142")
public class EM01142 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 各种其他信息
		String name = paramsStringFilter(params.get("name"));
		String remark = paramsStringFilter(params.get("remark"));
        Long planId = paramsLongFilter(params.get("planId"));
        // 等级制度明细列表
        JSONArray levelDetailList = paramsJSONArrayFilter(params.get("levelDetailList"));
		
		// planId不能为空信息的过滤
		if (planId == null) {
			return renderFAIL("0140", response, header);
		}
		// name不能为空信息的过滤
		if (StrKit.isBlank(name)) {
			return renderFAIL("0102", response, header);
		}
		
		Date date = new Date();
		CcLevel ccLevel = new CcLevel();
		ccLevel.set("create_date", date);
		ccLevel.set("modify_date", date);
		ccLevel.set("plan_id", planId);
		ccLevel.set("name", name);
		ccLevel.set("remark", remark);
		ccLevel.set("is_del", Boolean.FALSE);
		
		// 保存这个信息
		Boolean saveResult = ccLevel.save();
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		
		// 保存明细
		if(levelDetailList.size()!= 0){
			Long levelId = ccLevel.getLong("id");
			List<CcLevelDetail> ccLevelDetails = new ArrayList<>();

			IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
			for(int i = 0; i < levelDetailList.size(); i++) {
				CcLevelDetail ccLevelDetail = new CcLevelDetail();
				JSONObject map = (JSONObject) levelDetailList.get(i);
				ccLevelDetail.set("id", idGenerate.getNextValue());
				ccLevelDetail.set("create_date", date);
				ccLevelDetail.set("modify_date", date);
				ccLevelDetail.set("level_id", levelId);
				ccLevelDetail.set("name", map.get("name"));
				ccLevelDetail.set("value", map.get("value"));
				ccLevelDetail.set("remark", map.get("remark"));
				ccLevelDetail.set("is_del", Boolean.FALSE);
				ccLevelDetails.add(ccLevelDetail);
			}
			
			saveResult = CcLevelDetail.dao.batchSave(ccLevelDetails);
			if(!saveResult) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				result.put("isSuccess", saveResult);
				return renderSUC(result, response, header);
			}
		}
		// 返回操作是否成功
		result.put("isSuccess", saveResult);
		return renderSUC(result, response, header);
	}
	
}
