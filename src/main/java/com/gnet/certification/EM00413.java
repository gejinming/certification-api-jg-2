package com.gnet.certification;

import java.math.BigDecimal;
import java.util.*;

import com.gnet.model.admin.*;
import com.gnet.service.CcCourseGradecompBatchService;
import com.gnet.service.CcEduindicationStuScoreService;
import com.gnet.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.response.ServiceResponse;
import com.gnet.service.CcCourseGradecomposeDetailService;
import com.gnet.utils.PriceUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;

/**
 * 编辑成绩组成元素明细表
 * 
 * @author sll
 * 
 * @date 2016年07月06日 14:37:10
 *
 */
@Service("EM00413")
@Transactional(readOnly = false)
public class EM00413 extends BaseApi implements IApi {

	@Autowired
	private CcCourseGradecompBatchService ccCourseGradecompBatchService;
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {

		Map<String, Object> param = request.getData();
		Map<String, Boolean> result = new HashMap<>();

		Long id = paramsLongFilter(param.get("id"));
		String name = paramsStringFilter(param.get("name"));
		BigDecimal score = paramsBigDecimalFilter(param.get("score"));
		String detail = paramsStringFilter(param.get("detail"));
		String remark = paramsStringFilter(param.get("remark"));
		Long courseGradecomposeId = paramsLongFilter(param.get("courseGradecomposeId"));
		List<Long> indicationIds = paramsJSONArrayFilter(param.get("indicationIdArray"), Long.class);
		BigDecimal weight = paramsBigDecimalFilter(param.get("weight"));
		//	TODO 2020/07/07 gjm 增加了批次题目成绩
		Long batchId = paramsLongFilter(param.get("batchId"));
		if (id == null) {
			return renderFAIL("0450", response, header);
		}
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

		CcCourseGradecomposeStudetail courseGradecomposeStudetail  = CcCourseGradecomposeStudetail.dao.findMaxScoreStudent(id);
		ArrayList<CcCourseGradecomposeIndication> updateGradecomposeIndications = new ArrayList<>();
		if(courseGradecomposeStudetail != null && PriceUtils.greaterThan(courseGradecomposeStudetail.getBigDecimal("score"), score)){
			return renderFAIL("0458", response, header);
		}
		Date date = new Date();
		// 指标点可以更新的数据
		List<Long> addList = Lists.newArrayList();
		if (!indicationIds.isEmpty()) {
			// 判断指标点编号本次重复数据
			List<Long> repeatList = Lists.newArrayList();
			for (Long indicationId : indicationIds) {
				if (!addList.contains(indicationId)) {
					addList.add(indicationId);

					if(weight != null){
						CcCourseGradecompose courseGradecompose = CcCourseGradecompose.dao.findFilteredById(courseGradecomposeId);
						//目前课程目标总权重
						BigDecimal allWeight =  CcCourseGradecomposeIndication.dao.calculateSumWeights(courseGradecompose.getLong("teacher_course_id"), indicationId);
						//当前成绩组成、课程目标的权重

						CcCourseGradecomposeIndication gradecomposeIndication = CcCourseGradecomposeIndication.dao.findGradecomposeIndication(courseGradecomposeId, indicationId);
						BigDecimal existWeight=new BigDecimal(0);
						if (gradecomposeIndication !=null){
							 existWeight = gradecomposeIndication.getBigDecimal("weight");
							//如果要修改的权重小于等于已经存在的，直接保存即可
							if (PriceUtils.greaterThan(existWeight,weight)){
								gradecomposeIndication.set("modify_date",date);
								gradecomposeIndication.set("weight",weight);
								updateGradecomposeIndications.add(gradecomposeIndication);
							}else{
								//总的-已存在的+新的，是否大于1
								BigDecimal newWeight = allWeight.subtract(existWeight).add(weight);
								if (PriceUtils.greaterThan(newWeight, CcCourseGradecomposeIndication.MAX_WEIGHT)) {
									return renderFAIL("0493", response, header);
								}else{
									gradecomposeIndication.set("modify_date",date);
									gradecomposeIndication.set("weight",weight);
									updateGradecomposeIndications.add(gradecomposeIndication);
								}
							}
						}

					}
				} else {
					repeatList.add(indicationId);
				}
			}
			if (!repeatList.isEmpty()) {
				return renderFAIL("0464", response, header);
			}
		}
		if (updateGradecomposeIndications.size()!=0){
			if(!CcCourseGradecomposeIndication.dao.batchUpdate(updateGradecomposeIndications,"modify_date,weight")){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}
		CcCourseGradecompose courseGradecompose = CcCourseGradecompose.dao.findFilteredById(courseGradecomposeId);
		if (courseGradecompose == null) {
			return renderFAIL("0471", response, header);
		}

		// 保存成绩组成明细

		CcCourseGradeComposeDetail ccCourseGradeComposeDetail = CcCourseGradeComposeDetail.dao.findFilteredById(id);
		if (ccCourseGradeComposeDetail == null) {
			return renderFAIL("0451", response, header);
		}
		//判断题号是否已经存在
		if (CcCourseGradeComposeDetail.dao.isExisted(name,ccCourseGradeComposeDetail.getStr("name"),courseGradecomposeId,batchId)){
			return renderFAIL("2555", response, header);
		}


		ccCourseGradeComposeDetail.set("modify_date", date);
		ccCourseGradeComposeDetail.set("name", name);
		ccCourseGradeComposeDetail.set("score", score);
		ccCourseGradeComposeDetail.set("detail", detail);
		ccCourseGradeComposeDetail.set("remark", remark);
		ccCourseGradeComposeDetail.set("weight", weight);
		ccCourseGradeComposeDetail.set("course_gradecompose_id", courseGradecomposeId);

		// 需要删除的数据
		List<Long> deleteIds = Lists.newArrayList();
		// 需要保存的list
		List<CcCourseGradecomposeDetailIndication> saveList = Lists.newArrayList();
		// 需要增加的指标点数据
		List<Long> addIds = Lists.newArrayList();
		// 需要删除的指标点数据
		List<Long> deleteIndicationIds = Lists.newArrayList();
		List<CcCourseGradecomposeDetailIndication> existList = CcCourseGradecomposeDetailIndication.dao.findFilteredByColumn("course_gradecompose_detail_id", id);
		// 该成绩组成元素明细编号已关联的指标点编号
		List<Long> existIndicationIds = Lists.newArrayList();
		for (CcCourseGradecomposeDetailIndication temp : existList) {
			existIndicationIds.add(temp.getLong("indication_id"));
		}

		if (!indicationIds.isEmpty()) {
			// 如果保存的时候没有关联指标点则existList为null
			if (!existList.isEmpty()) {
				// 和数据的已经存在的进行比较，得出哪些需要保存，哪些需要删除
				// 数据库已经存在和需要更新的相同的指标点编号
				List<Long> sameIds = Lists.newArrayList();

				for (Long indicationId : indicationIds) {
					if (existIndicationIds.contains(indicationId)) {
						sameIds.add(indicationId);
					} else {
						addIds.add(indicationId);
					}
				}

				if (!sameIds.isEmpty()) {
					existIndicationIds.removeAll(sameIds);
				}
				// 判断需要删除的指标点是否为空
				if (!existIndicationIds.isEmpty()) {
					deleteIndicationIds.addAll(existIndicationIds);
				}
			} else {
				addIds.addAll(addList);
			}

			// 题目关联指标点批量保存（需要保存的编号不为空）
			if (!addIds.isEmpty()) {
				// 如果直接输入指标点成绩需要进行数据验证
				if (CcCourseGradecompose.DIRECT_INPUT_SCORE.equals(courseGradecompose.getInt("input_score_type"))) {
					CcCourseGradecomposeDetailService courseGradecomposeDetailService = SpringContextHolder
							.getBean(CcCourseGradecomposeDetailService.class);
					List<String> errorMessageList = courseGradecomposeDetailService.getErrorMessage(score,
							courseGradecomposeId, indicationIds, null, name);
					if (!errorMessageList.isEmpty()) {
						return renderFAIL("0802", response, header, errorMessageList);
					}
				}

				IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
				for (Long addId : addIds) {
					CcCourseGradecomposeDetailIndication temp = new CcCourseGradecomposeDetailIndication();
					temp.set("id", idGenerate.getNextValue());
					temp.set("create_date", date);
					temp.set("modify_date", date);
					temp.set("course_gradecompose_detail_id", id);
					temp.set("indication_id", addId);
					temp.set("is_del", Boolean.FALSE);
					saveList.add(temp);
				}
			}
		}

		if (!existIndicationIds.isEmpty()) {
			deleteIndicationIds.addAll(existIndicationIds);
			// 需要删除的对象
			List<CcCourseGradecomposeDetailIndication> deleteLists = CcCourseGradecomposeDetailIndication.dao.findDeatil(id, existIndicationIds.toArray(new Long[existIndicationIds.size()]));
			for (CcCourseGradecomposeDetailIndication temp : deleteLists) {
				deleteIds.add(temp.getLong("id"));
			}
			if (!deleteIds.isEmpty()) {
				// 批量删除成绩组成明细指标点关联记录
				if (!CcCourseGradecomposeDetailIndication.dao.deleteAll(deleteIds.toArray(new Long[deleteIds.size()]), date)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					result.put("isSuccess", false);
					return renderSUC(result, response, header);
				}
			}
		}

		if (!ccCourseGradeComposeDetail.update()) {
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		if (!CcCourseGradecomposeDetailIndication.dao.batchSave(saveList)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		CcCourseGradecomposeDetailService courseGradecomposeDetailService = SpringContextHolder.getBean(CcCourseGradecomposeDetailService.class);
		ServiceResponse serviceResponse = courseGradecomposeDetailService.getServiceResponse(courseGradecomposeId);
		if (!serviceResponse.isSucc()) {
			return renderFAIL("0804", response, header, serviceResponse.getContent());
		}

		// 新增的指标点和删除的指标点合并
		addIds.addAll(deleteIndicationIds);
		if (!addIds.isEmpty()) {
            if (!courseGradecomposeDetailService.updateStudentGrade(courseGradecomposeId)) {
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}

		Long teacherCourseId = courseGradecompose.getLong("teacher_course_id");
		List<CcEduclass> educlassList = CcEduclass.dao.findAllByCourseId(teacherCourseId);

		if (!educlassList.isEmpty()) {
			List<Long> idList = ConvertUtils.modelListToIdList(educlassList, "id");

			CcEduindicationStuScoreService ccEduindicationStuScoreService = SpringContextHolder.getBean(CcEduindicationStuScoreService.class);

			if (!ccEduindicationStuScoreService.calculate(idList.toArray(new Long[idList.size()]), courseGradecomposeId)) {
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}


			//更新批次的总分
			if (batchId != null){
				boolean updateBatchScoreState = ccCourseGradecompBatchService.updateBatchScore(batchId);
				if (!updateBatchScoreState){
					result.put("isSuccess", false);
					return renderSUC(result, response, header);
				}

			}



		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}
