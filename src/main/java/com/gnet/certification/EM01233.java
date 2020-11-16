package com.gnet.certification;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.FileInfo;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.service.CcCourseGradecompBatchService;
import com.gnet.service.CcCourseGradecomposeDetailService;
import com.gnet.service.CcEduindicationStuScoreService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.*;


/**
 * 教学班所有成绩组成的成绩数据保存
 *
 * @author GJM
 * @Date 2020年11月09日
 */
@Transactional(readOnly = false)
@Service("EM01233")
public class EM01233 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		HashMap<Object, Object> result = new HashMap<>();
		Map<String, Object> param = request.getData();
		JSONArray scoreStuIndigradeArray = paramsJSONArrayFilter(param.get("scoreStuIndigrade"));
		Long eduClassId = paramsLongFilter(param.get("eduClassId"));
		// 教学班编号为空过滤
		if (eduClassId == null) {
			return renderFAIL("0500", response, header);
		}
		Date date = new Date();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		//1:单批次指标点成绩直接输入添加
		List<CcScoreStuIndigrade> scoreStuIndigradeAddList = new ArrayList<>();
		//修改
		List<CcScoreStuIndigrade> scoreStuIndigradeEditList = new ArrayList<>();
		//4：多批次指标点成绩直接输入
		List<CcScoreStuIndigradeBatch> scoreStuIndigradeBatchAddList = new ArrayList<>();
		//修改
		List<CcScoreStuIndigradeBatch> scoreStuIndigradeBatchEditList = new ArrayList<>();
		//题目分数，就不用分批次了，因为题目id可以找到批次id
		List<CcCourseGradecomposeStudetail> addDetailScoreList = Lists.newArrayList();
		//修改
		List<CcCourseGradecomposeStudetail> editDetailScoreList = Lists.newArrayList();
		Set<Long> detailIds = new HashSet<>();
		Set<Long> studentIds = new HashSet<>();
		//HashMap<Object, Object> detailbatchId = new HashMap<>();
		ArrayList<Long> detailbatchId = new ArrayList<>();
		for(int i = 0; i < scoreStuIndigradeArray.size(); i++) {
			JSONObject map = (JSONObject) scoreStuIndigradeArray.get(i);
			Long studentId = map.getLong("studentId");
			studentIds.add(studentId);
			//录入成绩类型,1:单批次指标点成绩直接输入,2单批次题目明细计算获得，3：多批次由题目明细计算得，4：多批次指标点成绩直接输入
			Integer inputScoreType = map.getInteger("inputScoreType");
			//如果是多批次的录入类型的，batchIdOrGradecomposeId就是batchId批次Id,否则是开课成绩组成id
			Long batchIdOrGradecomposeId = map.getLong("batchIdOrGradecomposeId");
			//只有直接输入类型才会存在 单批次代表的是开课课程成绩组成元素与课程目标关联的id也就是cc_course_gradecompose_indication的id，
			// 多批次代表的是与cc_course_gradecompose_batch_indication的id关联
			Long courseGradecomposeIndicationId = map.getLong("courseGradecomposeIndicationId");
			//题目类型的是题目id，否则是课程目标id
			Long indicationId = map.getLong("indicationId");
			BigDecimal score = map.getBigDecimal("value");
			if (inputScoreType==1){
				CcScoreStuIndigrade scoreStuIndigrade = new CcScoreStuIndigrade();
				scoreStuIndigrade.set("id",idGenerate.getNextValue());
				scoreStuIndigrade.set("create_date",date);
				scoreStuIndigrade.set("create_date",date);
				scoreStuIndigrade.set("gradecompose_indication_id",courseGradecomposeIndicationId);
				scoreStuIndigrade.set("student_id",studentId);
				scoreStuIndigrade.set("grade",score);
				scoreStuIndigrade.set("is_del",Boolean.FALSE);
				scoreStuIndigradeAddList.add(scoreStuIndigrade);

			}else if (inputScoreType==2 || inputScoreType==3){
				detailIds.add(indicationId);
				if (inputScoreType==3){
					if (!detailbatchId.contains(batchIdOrGradecomposeId)){
						detailbatchId.add(batchIdOrGradecomposeId);
					}
				}
				CcCourseGradecomposeStudetail gradecomposeStudetail = CcCourseGradecomposeStudetail.dao.findStudentScore(indicationId, studentId);
				if (gradecomposeStudetail != null){
					gradecomposeStudetail.set("modify_date", date);
					gradecomposeStudetail.set("score", score);
					editDetailScoreList.add(gradecomposeStudetail);
				}else{
					CcCourseGradecomposeStudetail ccCourseGradecomposeStudetail = new CcCourseGradecomposeStudetail();
					ccCourseGradecomposeStudetail.set("id", idGenerate.getNextValue());
					ccCourseGradecomposeStudetail.set("create_date", date);
					ccCourseGradecomposeStudetail.set("modify_date", date);
					ccCourseGradecomposeStudetail.set("student_id", studentId);
					ccCourseGradecomposeStudetail.set("detail_id", indicationId);
					ccCourseGradecomposeStudetail.set("score", score);
					ccCourseGradecomposeStudetail.set("is_del", false);
					addDetailScoreList.add(ccCourseGradecomposeStudetail);

				}
			}else{
				CcScoreStuIndigradeBatch temp = new CcScoreStuIndigradeBatch();
				temp.set("id", idGenerate.getNextValue());
				temp.set("create_date", date);
				temp.set("modify_date", date);
				temp.set("student_id", studentId);
				temp.set("grade", score);
				temp.set("is_del", Boolean.FALSE);
				temp.set("batch_id",batchIdOrGradecomposeId);
				temp.set("type",inputScoreType);
				temp.set("gradecompose_indication_id",courseGradecomposeIndicationId );
				scoreStuIndigradeBatchAddList.add(temp);
			}

		}
		//两种直接录入的保存
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
		//多批次直接录入方式数据保存
		// 删除该课程下的学生成绩
		if(!CcScoreStuIndigradeBatch.dao.deleteByModel(scoreStuIndigradeBatchAddList)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return renderFAIL("0637", response, header);
		}

		Boolean isSuccess2 = CcScoreStuIndigradeBatch.dao.batchSave(scoreStuIndigradeBatchAddList);
		if(!isSuccess) {
			result.put("isSuccess", isSuccess2);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return renderSUC(result, response, header);
		}

		//题目录入类型成绩保存
		if(!addDetailScoreList.isEmpty() && !CcCourseGradecomposeStudetail.dao.batchSave(addDetailScoreList)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		if(!editDetailScoreList.isEmpty() && !CcCourseGradecomposeStudetail.dao.batchUpdate(editDetailScoreList, "modify_date, score")){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		//成绩处理
		ArrayList<Long> eduClassIds = new ArrayList<>();
		eduClassIds.add(eduClassId);

		CcCourseGradecompBatchService ccCourseGradecompBatchService=SpringContextHolder.getBean(CcCourseGradecompBatchService.class);
		List<CcCourseGradecompose> courseGradecomposes = CcCourseGradecompose.dao.findByEduClassIds(eduClassIds, null);
		for (CcCourseGradecompose temp : courseGradecomposes){
			Long courseGradeComposeId = temp.getLong("id");
			//题目录入类型的处理
			if (temp.getInt("input_score_type")==2 || temp.getInt("input_score_type")==3){
				CcCourseGradecomposeDetailService courseGradecomposeDetailService = SpringContextHolder.getBean(CcCourseGradecomposeDetailService.class);
				ArrayList<Long> courseGradeComposeIdlist = new ArrayList<>();
				courseGradeComposeIdlist.add(courseGradeComposeId);
				Set<Long> detailIds2 = new HashSet<>();
				List<CcCourseGradeComposeDetail> ccCourseGradeComposeDetails =null;
				if (temp.getInt("input_score_type")==2){
					ccCourseGradeComposeDetails = CcCourseGradeComposeDetail.dao.topicList0(courseGradeComposeId, null);
				}else{
					ccCourseGradeComposeDetails = CcCourseGradeComposeDetail.dao.topicList(courseGradeComposeIdlist, detailbatchId);
				}
				for (CcCourseGradeComposeDetail temps: ccCourseGradeComposeDetails){
					detailIds2.add(temps.getLong("id"));
				}

				List<CcCourseGradecomposeIndication> courseGradecomposeIndicationList = CcCourseGradecomposeIndication.dao.findByDetailIdAndCourseGradecomposeId(courseGradeComposeId, detailIds2.toArray(new Long[detailIds2.size()]));
				//批量导入时可能存在重复
				Set<Long> courseGradecomposeIndicationIds = new HashSet<>();
				if(!courseGradecomposeIndicationList.isEmpty()){
					for(int i=0; i<courseGradecomposeIndicationList.size(); i++ ){
						courseGradecomposeIndicationIds.add(courseGradecomposeIndicationList.get(i).getLong("id"));
					}
				}
				if(!courseGradecomposeDetailService.batchUpdateStudentGrade(courseGradecomposeIndicationIds.toArray(new Long[courseGradecomposeIndicationIds.size()]), studentIds.toArray(new Long[studentIds.size()]), detailIds2.toArray(new Long[detailIds2.size()]),courseGradeComposeId)){
					result.put("isSuccess", false);
					return renderSUC(result, response, header);
				}
			}
			//多批次类型处理
			if (temp.getInt("input_score_type")==4){
				boolean saveState = ccCourseGradecompBatchService.saveScoreIndiction(courseGradeComposeId);
				if (!saveState){
					result.put("isSuccess", Boolean.FALSE);
					return renderSUC(result, response, header);
				}
			}

		}
		//统计整个教学班的成绩
		if (!eduClassIds.isEmpty()) {

			CcEduindicationStuScoreService ccEduindicationStuScoreService = SpringContextHolder.getBean(CcEduindicationStuScoreService.class);

			if (!ccEduindicationStuScoreService.calculate(eduClassIds, Lists.<Long>newArrayList())) {
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}


}