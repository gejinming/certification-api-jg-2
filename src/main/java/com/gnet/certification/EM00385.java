package com.gnet.certification;

import java.util.Date;
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
import com.gnet.model.admin.CcEduclassStudent;
import com.gnet.service.CcEduindicationStuScoreService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;

/**
 * 教学班学生加入/取消达成度计算
 * 
 * @author SY
 * 
 * @date 2018年1月29日
 *
 */
@Service("EM00385")
@Transactional(readOnly=false)
public class EM00385 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String,Boolean> result = new HashMap<>();
		
		Date date = new Date();
		// 教学班学生编号
		Long id = paramsLongFilter(param.get("id"));
		// 是否加入达成度计算
		Boolean isCaculate = paramsBooleanFilter(param.get("isCaculate"));
		
		CcEduclassStudent ccEduclassStudent = CcEduclassStudent.dao.findFilteredById(id);
		if(ccEduclassStudent == null){
			return renderFAIL("0445", response, header);
		}
		// 默认不加入
		if(isCaculate == null) {
			isCaculate = Boolean.FALSE;
		}
		
		
		ccEduclassStudent.set("modify_date", date);
		ccEduclassStudent.set("is_caculate", isCaculate);
		if(!ccEduclassStudent.update()){
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		Long educlassId = ccEduclassStudent.getLong("class_id");
		List<Long> educlassIdList = Lists.newArrayList();
		educlassIdList.add(educlassId);

		if (!educlassIdList.isEmpty()) {

			CcEduindicationStuScoreService ccEduindicationStuScoreService = SpringContextHolder.getBean(CcEduindicationStuScoreService.class);

			if (!ccEduindicationStuScoreService.calculateExcept(educlassIdList.toArray(new Long[educlassIdList.size()]), null)) {
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}	
	
