package com.gnet.certification;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.utils.PriceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradeComposeDetail;
import com.gnet.model.admin.CcCourseGradecompose;
import com.gnet.model.admin.CcCourseGradecomposeDetailIndication;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.response.ServiceResponse;
import com.gnet.service.CcCourseGradecomposeDetailService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;

/**
 * 增加成绩组成元素明细表
 * 
 * @author sll
 * 
 * @date 2016年07月06日 14:37:10
 *
 */
@Service("EM00412")
@Transactional(readOnly=false)
public class EM00412 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Map<String, Object> result = new HashMap<>();
		
		String name = paramsStringFilter(param.get("name"));
		BigDecimal score = paramsBigDecimalFilter(param.get("score"));
		String detail = paramsStringFilter(param.get("detail"));
		String remark = paramsStringFilter(param.get("remark"));
		Long courseGradecomposeId = paramsLongFilter(param.get("courseGradecomposeId"));
		List<Long> indicationIds = paramsJSONArrayFilter(param.get("indicationIdArray"), Long.class);
		
		if (StrKit.isBlank(name)) {
			return renderFAIL("0452", response, header);
		}
		if (score == null) {
			return renderFAIL("0453", response, header);
		}

		if(PriceUtils.isZero(score)){
			return renderFAIL("1130", response, header);
		}

		if (courseGradecomposeId == null) {
			return renderFAIL("0455", response, header);
		}
		
		CcCourseGradecompose courseGradecompose = CcCourseGradecompose.dao.findFilteredById(courseGradecomposeId);
		if(courseGradecompose == null){
			return renderFAIL("0471", response, header);
		}
		
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		Long courseGradeComposeDetailId = idGenerate.getNextValue();
		//成绩组成元素明细保存
		Date date = new Date();
		CcCourseGradeComposeDetail ccCourseGradeComposeDetail = new CcCourseGradeComposeDetail();
		
		ccCourseGradeComposeDetail.set("id", courseGradeComposeDetailId);
		ccCourseGradeComposeDetail.set("create_date", date);
		ccCourseGradeComposeDetail.set("modify_date", date);
		ccCourseGradeComposeDetail.set("name", name);
		ccCourseGradeComposeDetail.set("score", score);
		ccCourseGradeComposeDetail.set("detail", detail);
		ccCourseGradeComposeDetail.set("remark", remark);
		ccCourseGradeComposeDetail.set("course_gradecompose_id", courseGradecomposeId);
		ccCourseGradeComposeDetail.set("is_del", Boolean.FALSE);
		
		//题目关联指标点批量保存
		List<CcCourseGradecomposeDetailIndication> saveList = Lists.newArrayList();
		if(!indicationIds.isEmpty()){
			//指标点可以增加的数据
			List<Long> addList = Lists.newArrayList();
			//判断指标点编号本次重复数据
			List<Long> repeatList = Lists.newArrayList();
			for(Long indicationId : indicationIds){
				if(!addList.contains(indicationId)){
					addList.add(indicationId);
				}else{
					repeatList.add(indicationId);
				}
					
			}
			if(!repeatList.isEmpty()){
				return renderFAIL("0464", response, header);
			}
			
			//如果直接输入指标点成绩需要进行数据验证
			if(CcCourseGradecompose.DIRECT_INPUT_SCORE.equals(courseGradecompose.getInt("input_score_type"))){
				CcCourseGradecomposeDetailService courseGradecomposeDetailService = SpringContextHolder.getBean(CcCourseGradecomposeDetailService.class);
				List<String> errorMessageList = courseGradecomposeDetailService.getErrorMessage(score, courseGradecomposeId, indicationIds, null, name);
				if(!errorMessageList.isEmpty()){
					return renderFAIL("0802", response, header, errorMessageList);
				}
			}
				
			for(Long indicationId: indicationIds){
				CcCourseGradecomposeDetailIndication temp =  new CcCourseGradecomposeDetailIndication();
				temp.set("id", idGenerate.getNextValue());
				temp.set("create_date", date);
				temp.set("modify_date", date);
				temp.set("course_gradecompose_detail_id", courseGradeComposeDetailId);
				temp.set("indication_id", indicationId);
				temp.set("is_del", Boolean.FALSE);
				saveList.add(temp);
			}		
		}
		
		if(!ccCourseGradeComposeDetail.save()){
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		if(!saveList.isEmpty()){
			if(!CcCourseGradecomposeDetailIndication.dao.batchSave(saveList)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}
		
		CcCourseGradecomposeDetailService courseGradecomposeDetailService = SpringContextHolder.getBean(CcCourseGradecomposeDetailService.class);
		ServiceResponse serviceResponse = courseGradecomposeDetailService.getServiceResponse(courseGradecomposeId);
		if(!serviceResponse.isSucc()){
			return renderFAIL("0804", response, header, serviceResponse.getContent());
		}
				
		result.put("isSuccess", true);
		result.put("id", courseGradeComposeDetailId);
		return renderSUC(result, response, header);
	}
}
