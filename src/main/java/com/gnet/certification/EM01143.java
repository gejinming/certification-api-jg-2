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
 * 修改等级制度某条信息
 * 
 * @author SY
 * @Date 2019年12月9日13:51:21
 */
@Service("EM01143")
public class EM01143 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据
		Long id = paramsLongFilter(params.get("id"));
		String name = paramsStringFilter(params.get("name"));
		String remark = paramsStringFilter(params.get("remark"));
		// 等级制度明细列表
        JSONArray levelDetailList = paramsJSONArrayFilter(params.get("levelDetailList"));
		
		// id不能为空信息的过滤
		if (id == null) {
			return renderFAIL("0100", response, header);
		}
		
		// name不能为空信息的过滤
		if (StrKit.isBlank(name)) {
			return renderFAIL("0102", response, header);
		}
				
		CcLevel ccLevel = CcLevel.dao.findFilteredById(id);
		if(ccLevel == null) {
			return renderFAIL("0101", response, header);
		}
		
		Date date = new Date();
		// 保存这个信息
		ccLevel.set("modify_date", date);
		ccLevel.set("name", name);
		ccLevel.set("remark", remark);
		Boolean isSuccess = ccLevel.update();
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		
		/** 对明细的操作  start **/
		// 单纯新建个对象用于后面看代码明确这个id是levelId
		Long levelId = id;
		// 分成三组：新增、修改、删除
		// 新增
		List<CcLevelDetail> ccLevelDetailsaveList = new ArrayList<>();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		JSONArray newJSONArray = new JSONArray();
		for(int i = 0; i < levelDetailList.size(); i++) {
			JSONObject map = (JSONObject) levelDetailList.get(i);
			if(map.get("id") != null) {
				// 如果存在id，就不是新增
				newJSONArray.add(map);
				continue;
			}
			CcLevelDetail ccLevelDetail = new CcLevelDetail();
			ccLevelDetail.set("id", idGenerate.getNextValue());
			ccLevelDetail.set("create_date", date);
			ccLevelDetail.set("modify_date", date);
			ccLevelDetail.set("level_id", levelId);
			ccLevelDetail.set("name", map.get("name"));
			ccLevelDetail.set("value", map.get("value"));
			ccLevelDetail.set("remark", map.get("remark"));
			ccLevelDetail.set("is_del", Boolean.FALSE);
			ccLevelDetailsaveList.add(ccLevelDetail);
		}
		isSuccess = CcLevelDetail.dao.batchSave(ccLevelDetailsaveList);
		if(!isSuccess) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", isSuccess);
			return renderSUC(result, response, header);
		}
		// 修改
		// 查询之前的明细 
		List<CcLevelDetail> ccLevelOldList = CcLevelDetail.dao.findByColumn("level_id", levelId);
		List<CcLevelDetail> ccLevelDetailEditList = new ArrayList<>();
		for(int i = 0; i < newJSONArray.size(); i++) {
			JSONObject map = (JSONObject) newJSONArray.get(i);
			Long levelDetailId = map.getLong("id");
			CcLevelDetail ccLevelDetailEdit = new CcLevelDetail();
			Long thisLevelDetailId = 0L;
			for(int y = 0; y < ccLevelOldList.size(); y++) {
				ccLevelDetailEdit = ccLevelOldList.get(y);
				thisLevelDetailId = ccLevelDetailEdit.getLong("id");
				if(thisLevelDetailId.equals(levelDetailId)) {
					break;
				}
			}
			// 防止没找到，再次判定
			if(!thisLevelDetailId.equals(levelDetailId)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				result.put("isSuccess", Boolean.FALSE);
				return renderSUC(result, response, header);
			}
			ccLevelDetailEdit.set("modify_date", date);
			ccLevelDetailEdit.set("name", map.get("name"));
			ccLevelDetailEdit.set("value", map.get("value"));
			ccLevelDetailEdit.set("remark", map.get("remark"));
			// 找到了，就修改列表增加记录，同时删除old里面的，减少循环次数
			ccLevelDetailEditList.add(ccLevelDetailEdit);
			ccLevelOldList.remove(ccLevelDetailEdit);
		}
		isSuccess = CcLevelDetail.dao.batchUpdate(ccLevelDetailEditList,"modify_date,name,value,remark");
		if(!isSuccess) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", isSuccess);
			return renderSUC(result, response, header);
		}
		// 删除
		List<CcLevelDetail> ccLevelDetailDeleteList = new ArrayList<>();
		// 通过修改步骤，把被修改的删除之后，留下的都是页面没有的数据，需要删除，所以直接add
		// TODO SY 这里不知道逻辑，但是有必要增加一个明细删除的验证，比如当xx在使用的时候不允许删除
		for(CcLevelDetail temp : ccLevelOldList) {
			temp.set("modify_date", date);
			temp.set("is_del", Boolean.TRUE);
			ccLevelDetailDeleteList.add(temp);
		}
		isSuccess = CcLevelDetail.dao.batchUpdate(ccLevelDetailDeleteList,"modify_date,is_del");
		if(!isSuccess) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", isSuccess);
			return renderSUC(result, response, header);
		}
		/** 对明细的操作  end **/
		
		// 返回操作是否成功
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}
	
}
