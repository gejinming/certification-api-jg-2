package com.gnet.certification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.*;
import com.gnet.service.CcCourseGradecompBatchService;
import com.gnet.service.CcEduindicationStuScoreService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.PriceUtils;
import com.gnet.utils.SpringContextHolder;
import com.gnet.model.admin.CcCourseGradecomposeIndication;
import com.gnet.model.admin.CcScoreStuIndigrade;
import com.gnet.model.admin.CcStudent;
/**
 * 
 * 保存或更新批量的学生考核成绩
 * 参考EM00562
 * @author SY
 * @Date 2017年10月15日
 */
@Transactional(readOnly=false)
@Service("EM00744")
public class EM00744 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		
		// 参考接口160的上传和获取
		JSONArray scoreStuIndigradeArray = paramsJSONArrayFilter(param.get("scoreStuIndigrade"));
		//1直接录入，2 导入成绩
		Integer inputType = paramsIntegerFilter(param.get("inputType"));
		Long eduClassId = paramsLongFilter(param.get("eduClassId"));
		Long courseGradeComposeId = paramsLongFilter(param.get("courseGradeComposeId"));
		Long state = paramsLongFilter(param.get("state"));
		// 教学班编号为空过滤
		if (eduClassId == null) {
			return renderFAIL("0500", response, header);
		}
		//批次id 不为空则说明是批次
		Long batchId = paramsLongFilter(param.get("batchId"));
		CcTeacherCourse courseByClassId = CcTeacherCourse.dao.findCourseByClassId(eduClassId);
		//达成度计算类型
		Integer resultType = courseByClassId.getInt("result_type");
		Map<Long, CcCourseGradecomposeIndication> CcCourseGradecomposeIndicationMap = new HashMap<>();
		Map<String, Object> result = new HashMap<String, Object>();
		Date date = new Date();
		
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		List<CcScoreStuIndigrade> scoreStuIndigradeEditList = new ArrayList<>();
		List<CcScoreStuIndigrade> scoreStuIndigradeAddList = new ArrayList<>();
		List<CcScoreStuIndigradeBatch> scoreStuIndigradeBatchAddList = new ArrayList<>();
		for(int i = 0; i < scoreStuIndigradeArray.size(); i++) {
			JSONObject map = (JSONObject) scoreStuIndigradeArray.get(i);
			
			// 获取数据
			//考核成绩分析法学生指标点成绩编号
//			Long scoreStuIndigradeId = map.getLong("scoreStuIndigradeId");
			// 学生编号
			Long studentId = map.getLong("studentId");
			//开课课程组成元素和指标点关联编号
			Long gradecomposeIndicationId = map.getLong("gradecomposeIndicationId");
			BigDecimal grade = map.getBigDecimal("grade");
		    
			if(studentId == null){
				return renderFAIL("0330", response, header);
			}
			if(gradecomposeIndicationId == null){
				return renderFAIL("0481", response, header);
			}
			if(grade == null){
				return renderFAIL("0480", response, header);
			}
			if(PriceUtils.greaterThan(CcScoreStuIndigrade.MIN_SCORE, grade)){
				return renderFAIL("0630", response, header);
			}
			
			/*CcCourseGradecomposeIndication courseGradecomposeIndication = CcCourseGradecomposeIndicationMap.get(gradecomposeIndicationId);
			if(courseGradecomposeIndication == null) {
				courseGradecomposeIndication = CcCourseGradecomposeIndication.dao.findFilteredById(gradecomposeIndicationId);
				CcCourseGradecomposeIndicationMap.put(gradecomposeIndicationId, courseGradecomposeIndication);
			}
			if(courseGradecomposeIndication == null){
				return renderFAIL("0498", response, header);
			}
			BigDecimal maxScore = courseGradecomposeIndication.getBigDecimal("max_score");
			if(maxScore != null && PriceUtils.greaterThan(grade, maxScore)){
				CcStudent student = CcStudent.dao.findById(studentId);
				return renderFAIL("0499", response, header, "学生："+student.getStr("name")+"的超过满分值" + maxScore + "了");
			}*/

			if (batchId != null){
				CcScoreStuIndigradeBatch temp = new CcScoreStuIndigradeBatch();
				temp.set("id", idGenerate.getNextValue());
				temp.set("create_date", date);
				temp.set("modify_date", date);
				temp.set("student_id", studentId);
				temp.set("grade", grade);
				temp.set("is_del", Boolean.FALSE);
				temp.set("batch_id",batchId);
				temp.set("type",resultType);
				temp.set("gradecompose_indication_id",gradecomposeIndicationId );
				/*if (inputType==1){
					temp.set("gradecompose_indication_id",gradecomposeIndicationId );
				}else {
					ArrayList<Long> courseGradecomposeIds = new ArrayList<>();
					courseGradecomposeIds.add(courseGradeComposeId);
					//批次成绩的成绩组成课程编号跟普通的不一样
					List<CcCourseGradecomposeBatchIndication> courseBatchGradecomposeIds = CcCourseGradecomposeBatchIndication.dao.findByCourseBatchGradecomposeIds(courseGradecomposeIds, batchId, gradecomposeIndicationId);
					//查出来应该只有1条数据
					if (courseBatchGradecomposeIds.size()>1 || courseBatchGradecomposeIds.size()==0){
						return renderFAIL("0481", response, header);
					}
					Long courseGradecomposeIndicationId = courseBatchGradecomposeIds.get(0).getLong("courseGradecomposeIndicationId");
					temp.set("gradecompose_indication_id",courseGradecomposeIndicationId );
				}*/
				scoreStuIndigradeBatchAddList.add(temp);

			}else{
				CcScoreStuIndigrade temp =  new CcScoreStuIndigrade();
				//考核成绩更新
	//			if(scoreStuIndigradeId != null){
	//				temp.set("id", scoreStuIndigradeId);
	//				temp.set("modify_date", date);
	//				temp.set("grade", grade);
	//				scoreStuIndigradeEditList.add(temp);
	//			}else{
					//已经存在的某个课程关联指点点下的学生成绩
	//				if(CcScoreStuIndigrade.dao.isExist(studentId, gradecomposeIndicationId)){
	//					return renderFAIL("0633", response, header);
	//				}
					//考核成绩保存
					temp.set("id", idGenerate.getNextValue());
					temp.set("create_date", date);
					temp.set("modify_date", date);
					temp.set("gradecompose_indication_id", gradecomposeIndicationId);
					temp.set("student_id", studentId);
					temp.set("grade", grade);
					temp.set("is_del", Boolean.FALSE);
					scoreStuIndigradeAddList.add(temp);
	//			}

			}

		}

		if (batchId !=null ){
			// 删除该课程下的学生成绩
			if(!CcScoreStuIndigradeBatch.dao.deleteByModel(scoreStuIndigradeBatchAddList)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return renderFAIL("0637", response, header);
			}

			Boolean isSuccess = CcScoreStuIndigradeBatch.dao.batchSave(scoreStuIndigradeBatchAddList);
			if(!isSuccess) {
				result.put("isSuccess", isSuccess);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return renderSUC(result, response, header);
			}
			//TODO GJM 2020/10/26增加财经大学的达成度算法，把每个批次都当成一个成绩组成来显示并计算达成度
			//达成度计算方式不是财经大学的算法就进行处理,财经大学的批次成绩就保存CcScoreStuIndigradeBatch这里吧
			if (!CcTeacherCourse.RESULT_TYPE_SCORE2.equals(resultType)){
				//处理当前成绩组成的批次成绩
				CcCourseGradecompBatchService  ccCourseGradecompBatchService=SpringContextHolder.getBean(CcCourseGradecompBatchService.class);
				boolean saveState = ccCourseGradecompBatchService.saveScoreIndiction(courseGradeComposeId);
				if (!saveState){
					result.put("isSuccess", Boolean.FALSE);
					return renderSUC(result, response, header);
				}
			}
		}else {
			// 删除该课程下的学生成绩
			if(!CcScoreStuIndigrade.dao.deleteByModel(scoreStuIndigradeAddList)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return renderFAIL("0637", response, header);
			}

			Boolean isSuccess = CcScoreStuIndigrade.dao.batchSave(scoreStuIndigradeAddList);
			if(!isSuccess) {
				result.put("isSuccess", isSuccess);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return renderSUC(result, response, header);
			}
		}


		/*isSuccess = CcScoreStuIndigrade.dao.batchUpdate(scoreStuIndigradeEditList, "modify_date,grade");
		if(!isSuccess) {
			result.put("isSuccess", isSuccess);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return renderSUC(result, response, header);
		}*/
		//TODO 2020/07/02 gjm 异步更新改为由按钮触发
		/*// 更新教学班的平均成绩和总成绩
		CcEduindicationStuScoreService ccEduindicationStuScoreService = SpringContextHolder.getBean(CcEduindicationStuScoreService.class);

		if (!ccEduindicationStuScoreService.calculate(Lists.newArrayList(eduClassId), Lists.<Long>newArrayList())) {
			result.put("isSuccess", Boolean.FALSE);
			return renderSUC(result, response, header);
		}
*/
		result.put("isSuccess", Boolean.TRUE);
		return renderSUC(result, response, header);
	}
	
}
