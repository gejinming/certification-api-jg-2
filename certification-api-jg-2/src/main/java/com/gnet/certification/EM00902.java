package com.gnet.certification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.service.CcGraduateService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Record;

/**
 * 毕业要求以及课程指标点批量保存
 * 
 * @author SY
 * @date 2018年1月11日
 */
@Transactional(readOnly = false)
@Service("EM00902")
public class EM00902 extends BaseApi implements IApi{
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> params = request.getData();
		
		List<HashMap> graduatesMap = paramsJSONArrayFilter(params.get("graduates"), HashMap.class);
		Long graduateVerId = paramsLongFilter(params.get("graduateVerId"));
		
		// 毕业要求版本编号
		if (graduateVerId == null) {
			return renderFAIL("0181", response, header);
		}
		
		if (graduatesMap.isEmpty()) {
			return renderFAIL("0336", response, header);
		}
		
		List<Record> graduateList = Lists.newArrayList();
		for (Map<String, String> ccTeacher : graduatesMap) {
			Record record = new Record();
			record.set("graduateIndexAndContent", ccTeacher.get("graduateIndexAndContent"));
			record.set("indName", ccTeacher.get("indName"));
			record.set("courseName", ccTeacher.get("courseName"));
			record.set("weight", ccTeacher.get("weight"));
			graduateList.add(record);
		}
		
		Map<String, Object> result = Maps.newHashMap();
		CcGraduateService ccGraduateService = SpringContextHolder.getBean(CcGraduateService.class);		
		if(!ccGraduateService.validateImportList(graduateList, graduateVerId)) {
			List<Map<String, Object>> errorList = Lists.newArrayList();
			// 返回结果
			for (Record record : graduateList) {
				Map<String, Object> object = Maps.newHashMap();
				// 通过graduateIndexNum和indIndexNum来确定是否属于同一个毕业要求或者指标点
				
				// 毕业要求参数
				object.put("graduateIndexNum", record.getStr("graduateIndexNum"));
				object.put("graduateContent", record.getStr("graduateContent"));
				object.put("graduateVerId", graduateVerId);
				
				// 指标点参数
				// 指标点内容
				object.put("indIndexNum", record.getStr("indIndexNum"));
				
				// 指标点课程联系表参数
				object.put("courseName", record.getStr("courseName"));
				object.put("courseId", record.getLong("courseId"));
				object.put("weight", record.getStr("weight"));
				
				// 错误参数
				object.put("reasons", record.get("reasons"));
				object.put("isError", record.getBoolean("isError"));
				
				// 原先数据。weight和courseName没变化，所以按照上面指标点课程联系表参数的来即可
				object.put("graduateIndexAndContent", record.getStr("graduateIndexAndContent"));
				object.put("indName", record.getStr("indName"));
				
				errorList.add(object);
			}
			result.put("isSuccess", Boolean.FALSE);
			result.put("failRecords", errorList);
		} else {
			// 返回结果
			boolean isSuccess = ccGraduateService.saveGraduats(graduateList);
			result.put("isSuccess", isSuccess);
		}
		
		
		return renderSUC(result, response, header);
	}
	
}
