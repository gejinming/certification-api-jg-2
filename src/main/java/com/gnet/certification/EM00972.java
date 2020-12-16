package com.gnet.certification;

import com.gnet.FileInfo;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.gnet.plugin.poi.ExcelDefinition;
import com.gnet.plugin.poi.ExcelParser;
import com.gnet.plugin.poi.Header2ExcelParser;
import com.gnet.plugin.poi.RowDefinition;
import com.gnet.plugin.poi.RowDefinition.ColumnDefinition;
import com.gnet.plugin.poi.RowDefinition.GroupColumnDefinition;
import com.gnet.response.ServiceResponse;
import com.gnet.service.CcCourseGradecompBatchService;
import com.gnet.service.CcCourseGradecomposeDetailService;
import com.gnet.utils.ConvertUtils;
import com.gnet.utils.DictUtils;
import com.gnet.utils.SpringContextHolder;
import com.gnet.utils.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.log.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 题目批量保存接口
 * 
 * @author xzl
 * @Date 2018年2月8日11:11:07
 */
@Transactional(readOnly = false)
@Service("EM00972")
public class EM00972 extends BaseApi implements IApi {
	
	private static final Logger logger = Logger.getLogger(EM00972.class);

	@Autowired
	private CcCourseGradecomposeDetailService ccCourseGradecomposeDetailService;

	@Autowired
	private CcCourseGradecompBatchService ccCourseGradecompBatchService;
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		List<Map<String, Object>> subjects = ConvertUtils.convert(param.get("subjects"), List.class);

		Map<String, Object> result = Maps.newHashMap();

		// 开课课程成绩组成元素编号
		Long courseGradeComposeId = paramsLongFilter(param.get("courseGradeComposeId"));

		//批次id
		Long batchId = paramsLongFilter(param.get("batchId"));
		// 课程成绩组成为空过滤
		if (courseGradeComposeId == null) {
			return renderFAIL("0490", response, header);
		}

		if(subjects.isEmpty()){
            return renderFAIL("2100", response, header);
        }

		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findByCourseGradeComposeId(courseGradeComposeId);
		if(ccTeacherCourse == null){
			return renderFAIL("0501", response, header);
		}
		//达成度计算类型
		Integer resultType = ccTeacherCourse.getInt("result_type");
		// 判断录入成绩类型是否是由题目明细计算获得,1:指标点成绩直接输入,2:由题目明细计算获得
		if(!CcCourseGradecompose.SUMMARY_INPUT_SCORE.equals(ccTeacherCourse.getInt("input_score_type")) && !CcCourseGradecompose.SUMMARY_MANYINPUT_SCORE.equals(ccTeacherCourse.getInt("input_score_type"))){
			return renderFAIL("2102", response, header);
		}

		//需要增加的题目列表
		List<CcCourseGradeComposeDetail> ccCourseGradeComposeDetails = Lists.newArrayList();
		//课程目标序号和编号对应map
		Map<String, Long> indicationIdMap = new HashMap<>();
		List<CcCourseGradecomposeDetailIndication> ccCourseGradecomposeDetailIndications = Lists.newArrayList();

		//开课课程下成绩组成关联的课程目标
		List<CcIndication> ccIndicationList = CcIndication.dao.findCourseGradeComposeId(courseGradeComposeId);
		List<String> ccIndications = Lists.newArrayList();
		for(CcIndication ccIndication : ccIndicationList){
			String content = String.format("CO%s", ccIndication.getInt("sort")+":"+ccIndication.getStr("content"));
			ccIndications.add(content);
			indicationIdMap.put(content, ccIndication.getLong("id"));
		}

		if(!ccCourseGradecomposeDetailService.validateImportSubject(subjects, ccCourseGradeComposeDetails, courseGradeComposeId, indicationIdMap, ccCourseGradecomposeDetailIndications, ccIndications,batchId,resultType)){
			return renderFAIL("0953", response, header);
		}

		//保存题目
        if(!CcCourseGradeComposeDetail.dao.batchSave(ccCourseGradeComposeDetails)){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("isSuccess", false);
            return renderSUC(result, response, header);
        }

        if(!ccCourseGradecomposeDetailIndications.isEmpty()){
            if(!CcCourseGradecomposeDetailIndication.dao.batchSave(ccCourseGradecomposeDetailIndications)){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.put("isSuccess", false);
                return renderSUC(result, response, header);
            }
        }

        CcCourseGradecomposeDetailService courseGradecomposeDetailService = SpringContextHolder.getBean(CcCourseGradecomposeDetailService.class);
        ServiceResponse serviceResponse = courseGradecomposeDetailService.getServiceResponse(courseGradeComposeId);
        if(!serviceResponse.isSucc()){
            return renderFAIL("0804", response, header, serviceResponse.getContent());
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