package com.gnet.certification;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.gnet.api.kit.UserCacheKit;
import com.gnet.model.admin.CcEvaluteLevel;
import com.gnet.model.admin.CcRankingLevel;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.DictUtils;
import com.gnet.utils.RandomUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcMajor;
import com.gnet.model.admin.Office;
import com.gnet.service.OfficeService;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.kit.StrKit;

/**
 * 新增专业某条信息
 * 
 * @author SY
 * @Date 2016年6月20日14:09:05
 */
@Service("EM00082")
@Transactional(readOnly=false)
public class EM00082 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 各种其他信息
		Integer educationLength = paramsIntegerFilter(params.get("educationLength"));
		Long instituteId = paramsLongFilter(params.get("instituteId"));
		Long officerId = paramsLongFilter(params.get("officerId"));
		String description = paramsStringFilter(params.get("description"));
		String name = paramsStringFilter(params.get("majorName"));
		Integer specializedFields = paramsIntegerFilter(params.get("specializedFields"));
		Integer awardDegree = paramsIntegerFilter(params.get("awardDegree"));
		Integer educationLevel = paramsIntegerFilter(params.get("educationLevel"));
		Long schoolId = UserCacheKit.getSchoolId(request.getHeader().getToken());

		// 为空信息的过滤
		if(schoolId == null){
			return renderFAIL("0084", response, header);
		}
		if (StrKit.isBlank(name)) {
			return renderFAIL("0150", response, header);
		}
		if (instituteId == null) {
			return renderFAIL("0138", response, header);
		}
		if (educationLength == null) {
			return renderFAIL("0132", response, header);
		}
		if (specializedFields == null) {
			return renderFAIL("0134", response, header);
		}
		if (awardDegree == null) {
			return renderFAIL("0135", response, header);
		}
		if (educationLevel == null) {
			return renderFAIL("0136", response, header);
		}
		if (CcMajor.dao.isExisted(schoolId, name, null)) {
			return renderFAIL("0139", response, header);
		}

		Date date = new Date();
		CcMajor ccMajor = new CcMajor();
		ccMajor.set("create_date", date);
		ccMajor.set("modify_date", date);
		ccMajor.set("officer_id", officerId);
		ccMajor.set("education_length", educationLength);
		ccMajor.set("specialized_fields", specializedFields);
		ccMajor.set("award_degree", awardDegree);
		ccMajor.set("education_level", educationLevel);
		ccMajor.set("description", description);
		ccMajor.set("is_del", Boolean.FALSE);
		
		// 保存这个信息
		Boolean saveResult = ccMajor.save();
		Long majorId = ccMajor.getLong("id");
				
		// 结果返回
		Map<String, Object> result = new HashMap<>();		
		if(!saveResult) {
			result.put("isSuccess", false);
			// 返回操作是否成功
			return renderSUC(result, response, header);
		}
		
		
		// 保存office的信息
		Office office = new Office();
		office.set("id", majorId);
		office.set("create_date", date);
		office.set("modify_date", date);
		office.set("parentid", instituteId);
		office.set("description", description);
		office.set("name", name);
		office.set("type", Office.TYPE_MAJOR);
		office.set("code", RandomUtils.randomString(Office.TYPE_MAJOR));
		office.set("is_system", Boolean.FALSE);
		office.set("is_del", Boolean.FALSE);
		
		OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
		saveResult = officeService.save(office, instituteId, date);
		
		if(!saveResult) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			// 返回操作是否成功
			return renderSUC(result, response, header);
		}
		BigDecimal twos[] = {CcRankingLevel.LEVEL_TOW_A_VALUE, CcRankingLevel.LEVEL_TOW_B_VALUE};
		BigDecimal fives[] = {CcRankingLevel.LEVEL_FIVE_A_VALUE, CcRankingLevel.LEVEL_FIVE_B_VALUE, CcRankingLevel.LEVEL_FIVE_C_VALUE, CcRankingLevel.LEVEL_FIVE_D_VALUE, CcRankingLevel.LEVEL_FIVE_E_VALUE};
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		//TODO 2020.12.08 GJM 新增专业后默认两个个考评等级制，五级和二级
		ArrayList<CcRankingLevel> ccRankingLevels = new ArrayList<>();
		//二级制
		for(int i = 1; i<= CcRankingLevel.LEVEL_TOW; i++){
			CcRankingLevel ccRankingLevel = new CcRankingLevel();
			ccRankingLevel.set("id", idGenerate.getNextValue());
			ccRankingLevel.set("create_date", date);
			ccRankingLevel.set("modify_date", date);
			ccRankingLevel.set("level_name", DictUtils.findLabelByTypeAndKey("evaluteLevelTowValue", i));
			ccRankingLevel.set("score", twos[i-1]);
			ccRankingLevel.set("major_id", majorId);
			ccRankingLevel.set("level", CcRankingLevel.LEVEL_TOW);
			ccRankingLevel.set("is_del", Boolean.FALSE);
			ccRankingLevels.add(ccRankingLevel);
		}
		//五级制
		for(int i=1; i<= CcRankingLevel.LEVEL_FIVE; i++ ){
			CcRankingLevel ccRankingLevel = new CcRankingLevel();
			ccRankingLevel.set("id", idGenerate.getNextValue());
			ccRankingLevel.set("create_date", date);
			ccRankingLevel.set("modify_date", date);
			ccRankingLevel.set("level_name", DictUtils.findLabelByTypeAndKey("evaluteLevelFiveValue", i));
			ccRankingLevel.set("score", fives[i-1]);
			ccRankingLevel.set("major_id", majorId);
			ccRankingLevel.set("level", CcRankingLevel.LEVEL_FIVE);
			ccRankingLevel.set("is_del", Boolean.FALSE);
			ccRankingLevels.add(ccRankingLevel);

		}
		if (!CcRankingLevel.dao.batchSave(ccRankingLevels)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			// 返回操作是否成功
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		// 返回操作是否成功
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}
	
}
