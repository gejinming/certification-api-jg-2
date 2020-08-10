package com.gnet.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gnet.model.admin.*;
import com.gnet.pager.Pageable;
import com.gnet.plugin.poi.RowDefinition;
import com.gnet.utils.ConvertUtils;
import com.gnet.utils.DictUtils;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.plugin.id.IdGenerate;
import com.gnet.response.ServiceResponse;
import com.gnet.utils.PriceUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 成绩组成元素明细
 * 
 * @author xzl
 * 
 * @date 2016年11月14日16:38:24
 */
@Component("ccCourseGradecomposeDetailService")
public class CcCourseGradecomposeDetailService {

	/**
	 * 增加题目的时候返回题目分数超过成绩组成指标点权重的满分值的题目
	 * @param score 
	 * @param courseGradecomposeId
	 * @param indicationIds
	 * @param courseGradecomposeDetailId
	 * @return
	 */
	public List<String> getErrorMessage(BigDecimal score, Long courseGradecomposeId, List<Long> indicationIds, Long courseGradecomposeDetailId, String detailName) {
		List<String> errorList = Lists.newArrayList();
		List<CcCourseGradeComposeDetail> courseGradeComposeDetailList = CcCourseGradeComposeDetail.dao.findByCourseGradeComposeId(courseGradecomposeId, indicationIds.toArray(new Long[indicationIds.size()]), null);
		if(!courseGradeComposeDetailList.isEmpty()){
			for(CcCourseGradeComposeDetail temp : courseGradeComposeDetailList){
				BigDecimal restScore = PriceUtils._sub(temp.getBigDecimal("max_score"), temp.getBigDecimal("allScore") == null ? new BigDecimal(0) : temp.getBigDecimal("allScore"));
				if(PriceUtils.greaterThan(score, restScore)){
					String error = detailName + "的分数为" + score + ",超过了" + temp.getStr("gradecomposeName") + "和课程目标" + temp.getStr("content")+ "的剩余分数" + restScore;
					errorList.add(error);
				}
			}
		}
		
		return errorList;
	}

	
	/**
	 * 根据开课课程成绩组成元素编号更新其支持指标点的满分值中遇到的错误信息
	 * @param courseGradecomposeId
	 * @return
	 */
	public ServiceResponse getServiceResponse(Long courseGradecomposeId) {
		Date date = new Date();
		//根据开课课程成绩组成编号的题目分数得到对应指标点的满分值
		List<CcCourseGradeComposeDetail> courseGradeComposeDetailList = CcCourseGradeComposeDetail.dao.findByCourseGradeComposeId(courseGradecomposeId, null, null);
		//开课课程成绩组成支持的指标点
		List<CcCourseGradecomposeIndication> courseGradecomposeIndicationList = CcCourseGradecomposeIndication.dao.findFilteredByColumn("course_gradecompose_id", courseGradecomposeId);
		if(courseGradeComposeDetailList.isEmpty()){
			return ServiceResponse.succ(true);
		}
		if(!courseGradeComposeDetailList.isEmpty() && courseGradecomposeIndicationList.isEmpty()){
			return ServiceResponse.error("更新分数前先维护开课课程成绩组成元素和指标点关系 ");
		}
		
		Map<Long, BigDecimal> scoreMap = Maps.newHashMap();
		for(CcCourseGradeComposeDetail temp: courseGradeComposeDetailList){
			scoreMap.put(temp.getLong("indication_id"), temp.getBigDecimal("allScore"));
		}
		//TODO 2020/07/10增加了题目批次，每个学生的课程目标成绩=ccCourseGradecomposeStudetails的成绩/包含这个课程目标批次的数量
		Pageable pageable = new Pageable(null, null);
		Page<CcCourseGradecomposeBatch> batchList = CcCourseGradecomposeBatch.dao.page(pageable, courseGradecomposeId);
		List<CcCourseGradecomposeBatch> batchLists = batchList.getList();



		for(CcCourseGradecomposeIndication temp : courseGradecomposeIndicationList){
			Long indicationId = temp.getLong("indication_id");
			BigDecimal score = scoreMap.get(temp.getLong("indication_id"));

			//1.判断是否存在批次
			if (batchLists.size() != 0){
				//2.查询包含这个课程目标的批次数量
				List<CcCourseGradecomposeDetailIndication> batchList1 = CcCourseGradecomposeDetailIndication.dao.findBatchList(indicationId, courseGradecomposeId);
				int batchNum = batchList1.size();
				if (batchList1.size() !=0){
					BigDecimal number = new BigDecimal(batchNum);
					score = PriceUtils.div(score, number, 2);
				}
			}
			temp.set("modify_date", date);
			temp.set("max_score", score);
		}
		
		if(!CcCourseGradecomposeIndication.dao.batchUpdate(courseGradecomposeIndicationList, "modify_date, max_score")){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return ServiceResponse.error("批量更新开课课程指标点满分值失败 ");
		}
		
		return ServiceResponse.succ(true);
	}


	/**
	 * 批量题目分数变更后更新学生成绩
	 *
	 * @param courseGradecomposeIndicationIds
	 * @param studentIds
	 * @param detailIds
	 * @return
	 */
	public boolean batchUpdateStudentGrade(Long[] courseGradecomposeIndicationIds, Long[] studentIds, Long[] detailIds,Long courseGradeComposeId){
		//根据学生编号和成绩组成明细编号返回对应学生成绩
		List<CcScoreStuIndigrade> historyStudentGrades = CcScoreStuIndigrade.dao.findByStudentIdsAndDetailIds(studentIds, detailIds);
		//根据学生题目分数得出学生成绩
		List<CcCourseGradecomposeStudetail> studentGrades = CcCourseGradecomposeStudetail.dao.findByStudentIdsAndDetailIds(studentIds, detailIds, courseGradecomposeIndicationIds);
		//查出的studentGrades数据需要将同一studentId和同一courseGradecomposeIndicationId的学生分数合并一下
		List<CcCourseGradecomposeStudetail> ccCourseGradecomposeStudetails = Lists.newArrayList();
		if(!studentGrades.isEmpty()){
			CcCourseGradecomposeStudetail first = studentGrades.get(0);
			BigDecimal intiScore = new BigDecimal(0);
			Map<String, CcCourseGradecomposeStudetail> map = new HashMap<>();
			String firstKey = String.format("%s-%s", first.getLong("studentId"), first.getLong("courseGradecomposeIndicationId"));
			for(int i = 1; i <= studentGrades.size(); i++){
				CcCourseGradecomposeStudetail ccCourseGradecomposeStudetail = studentGrades.get(i-1);
				String key = String.format("%s-%s", ccCourseGradecomposeStudetail.getLong("studentId"), ccCourseGradecomposeStudetail.getLong("courseGradecomposeIndicationId"));
				if(firstKey.equals(key)){
					if (ccCourseGradecomposeStudetail.getBigDecimal("score") !=null){
						intiScore = PriceUtils.add(intiScore, ccCourseGradecomposeStudetail.getBigDecimal("score"));
					}

					ccCourseGradecomposeStudetail.set("score", intiScore);
					map.put(key,ccCourseGradecomposeStudetail);
				}else {
					ccCourseGradecomposeStudetails.add(map.get(firstKey));
					firstKey = key;
					if (ccCourseGradecomposeStudetail.getBigDecimal("score") !=null){
						intiScore = PriceUtils.add(new BigDecimal(0), ccCourseGradecomposeStudetail.getBigDecimal("score"));
					}

					ccCourseGradecomposeStudetail.set("score", intiScore);
					map.put(key,ccCourseGradecomposeStudetail);
				}

				if(i == studentGrades.size()){
					ccCourseGradecomposeStudetails.add(map.get(key));
				}
			}
		}
		//TODO 2020/07/10增加了题目批次，每个学生的课程目标成绩=ccCourseGradecomposeStudetails的成绩/包含这个课程目标批次的数量
		Pageable pageable = new Pageable(null, null);
		Page<CcCourseGradecomposeBatch> batchList = CcCourseGradecomposeBatch.dao.page(pageable, courseGradeComposeId);
		List<CcCourseGradecomposeBatch> batchLists = batchList.getList();

		//1.判断是否存在批次
		if (batchLists.size() != 0){

			for (CcCourseGradecomposeStudetail temp: ccCourseGradecomposeStudetails){
				BigDecimal score = temp.getBigDecimal("score");
				Long indicationId = temp.getLong("indication_id");
				//2.查询包含这个课程目标的批次数量
				List<CcCourseGradecomposeDetailIndication> batchList1 = CcCourseGradecomposeDetailIndication.dao.findBatchList(indicationId, courseGradeComposeId);
				int batchNum = batchList1.size();

				if (batchList1.size() !=0){

					BigDecimal number = new BigDecimal(batchNum);
					//3.用处理好的课程目标总成绩/包含这个课程目标的批次数量
					BigDecimal divideScore = PriceUtils.div(score, number, 2);
					temp.set("score",divideScore);
				}


			}
		}


		return handleStudentGrade(historyStudentGrades, ccCourseGradecomposeStudetails);
	}

	/**
	 * 题目分数变更后更新学生成绩
	 *
	 * @param courseGradecomposeIndicationIds
	 * @param studentIds
	 * @param detailIds
	 * @return
	 */
	public boolean updateStudentGrade(Long[] courseGradecomposeIndicationIds, Long[] studentIds, Long[] detailIds){
		//根据学生编号和成绩组成明细编号返回对应学生成绩
		List<CcScoreStuIndigrade> historyStudentGrades = CcScoreStuIndigrade.dao.findByStudentIdAndDetailId(studentIds, detailIds);
		//根据学生题目分数得出学生成绩
		List<CcCourseGradecomposeStudetail> studentGrades = CcCourseGradecomposeStudetail.dao.findByStudentIdAndDetailId(studentIds, detailIds, courseGradecomposeIndicationIds);
		return handleStudentGrade(historyStudentGrades, studentGrades);
	}


	/**
	 * 题目指标点变换后更新学生成绩
	 * @param courseGradecomposeId
	 * @return
	 */
	public boolean updateStudentGrade(Long courseGradecomposeId) {
		//根据指标点和开课课程成绩组成元素编号返回对应学生成绩
		List<CcScoreStuIndigrade> historyStudentGrades = CcScoreStuIndigrade.dao.findByIndicationIdsAndCourseGradecomposeId(courseGradecomposeId);
		//根据学生题目分数得出学生成绩
		List<CcCourseGradecomposeStudetail> studentGrades = CcCourseGradecomposeStudetail.dao.findByIndicationIdsAndCourseGradecomposeId(courseGradecomposeId);
        return handleStudentGrade(historyStudentGrades, studentGrades);
	}
	

	/**
	 * 处理学生成绩
	 * @param historyStudentGrades
	 * @param studentGrades
	 * @return
	 */
	private boolean handleStudentGrade(List<CcScoreStuIndigrade> historyStudentGrades, List<CcCourseGradecomposeStudetail> studentGrades){
		Map<String, CcScoreStuIndigrade> historyStudentGradeMap = Maps.newHashMap();
		Map<String, CcCourseGradecomposeStudetail> newStudentGradeMap = Maps.newHashMap();
		
		for(CcScoreStuIndigrade temp : historyStudentGrades){
			String key = new StringBuilder(temp.getLong("student_id").toString()).append(",").append(temp.getLong("indication_id").toString()).toString();
			historyStudentGradeMap.put(key, temp);
		}
		
		for(CcCourseGradecomposeStudetail temp : studentGrades){
			String key = new StringBuilder(temp.getLong("studentId").toString()).append(",").append(temp.getLong("indication_id").toString()).toString();
			newStudentGradeMap.put(key, temp);
		}
		
		//新增的学生成绩
		List<CcScoreStuIndigrade> addList = Lists.newArrayList();
		//更新的学生成绩
		List<CcScoreStuIndigrade> editList = Lists.newArrayList();
		//学生指标点开课课程成绩组成相同的
		List<CcScoreStuIndigrade> sameList = Lists.newArrayList();
		
		Date date = new Date();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		for(Map.Entry<String, CcCourseGradecomposeStudetail>entry : newStudentGradeMap.entrySet()){
			CcScoreStuIndigrade historyScoreStuIndigrade = historyStudentGradeMap.get(entry.getKey());
			BigDecimal grade = entry.getValue().getBigDecimal("score");
			Long gradecomposeIndicationId = entry.getValue().getLong("courseGradecomposeIndicationId");
			Long studentId = entry.getValue().getLong("studentId");
			
			if(historyScoreStuIndigrade == null){
				CcScoreStuIndigrade newCcScoreStuIndigrade = new CcScoreStuIndigrade();
				newCcScoreStuIndigrade.set("id", idGenerate.getNextValue());
				newCcScoreStuIndigrade.set("create_date", date);
				newCcScoreStuIndigrade.set("modify_date", date);
				newCcScoreStuIndigrade.set("gradecompose_indication_id", gradecomposeIndicationId);
				newCcScoreStuIndigrade.set("student_id", studentId);
				newCcScoreStuIndigrade.set("grade", grade);
				newCcScoreStuIndigrade.set("is_del", Boolean.FALSE);
				addList.add(newCcScoreStuIndigrade);
			}else{
				if(!grade.equals(historyScoreStuIndigrade.getBigDecimal("grade"))){
					historyScoreStuIndigrade.set("modify_date", date);
					historyScoreStuIndigrade.set("grade", grade);
					BigDecimal age = new BigDecimal("0");

					if (grade==age){
						historyScoreStuIndigrade.set("is_del",Boolean.TRUE)	;
					}else {
						historyScoreStuIndigrade.set("is_del",Boolean.FALSE)	;
					}
					editList.add(historyScoreStuIndigrade);
				}
				sameList.add(historyScoreStuIndigrade);
			}
		}
		
		if(!historyStudentGrades.isEmpty()){
			if(!sameList.isEmpty()){
				historyStudentGrades.removeAll(sameList);
			}
		    if(!historyStudentGrades.isEmpty()){
		    	Long[] deleteIdArray = new Long[historyStudentGrades.size()];
		    	for(int i=0; i<historyStudentGrades.size(); i++){
		    		deleteIdArray[i] = historyStudentGrades.get(i).getLong("id");
		    	}
		    	if(!CcScoreStuIndigrade.dao.deleteAll(deleteIdArray, date)){
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
		    	}
		    }		
		}
		
		if(!addList.isEmpty() && !CcScoreStuIndigrade.dao.batchSave(addList)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		
		if(!editList.isEmpty() && !CcScoreStuIndigrade.dao.batchUpdate(editList, "modify_date, grade,is_del")){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		
		return true;	
	}

	/**
	 * 返回开课课程下成绩组成下的课程目标
	 * @param courseGradeComposeId
	 *                         开课课程下成绩组成
	 * @return
	 */
	public RowDefinition getComposeDetailDefinition(Long courseGradeComposeId) {
		//开课课程下成绩组成关联的课程目标
		List<CcIndication> ccIndications = CcIndication.dao.findCourseGradeComposeId(courseGradeComposeId);
		Integer size = ccIndications.size();

		Integer index = ccIndications.isEmpty() ? 0 : 1;

		// ----------------------------------------- 处理head -----------------------------------
		RowDefinition rowDefinition = new RowDefinition(size + 3);

		RowDefinition.ColumnDefinition no = RowDefinition.ColumnDefinition.createCommonColumn(0, DictUtils.findLabelByTypeAndKey("subjectImport", 1));
		RowDefinition.ColumnDefinition score = RowDefinition.ColumnDefinition.createCommonColumn(1, DictUtils.findLabelByTypeAndKey("subjectImport", 2));
		RowDefinition.ColumnDefinition detail = RowDefinition.ColumnDefinition.createCommonColumn(size + 2, DictUtils.findLabelByTypeAndKey("subjectImport", 4));
		RowDefinition.ColumnDefinition remark = RowDefinition.ColumnDefinition.createCommonColumn(size + 3, DictUtils.findLabelByTypeAndKey("subjectImport", 5));
		RowDefinition.ColumnDefinition de = RowDefinition.ColumnDefinition.createCommonColumn(size + 10, DictUtils.findLabelByTypeAndKey("subjectImport", 10));
		//课程目标不为空的时候才加入
		if(!ccIndications.isEmpty()){
			String[] indicationSort = new String[ccIndications.size()];
			for(int i=0; i<ccIndications.size(); i++){
				String indications=ccIndications.get(i).getInt("sort")+":"+ccIndications.get(i).getStr("content");
				indicationSort[i] = String.format("CO%s",indications );
			}
			RowDefinition.ValidateDefinition subGrade2Validate = new RowDefinition.ValidateDefinition(indicationSort);

			RowDefinition.ColumnDefinition indication = RowDefinition.ColumnDefinition
					.createGroupColumn(2, size + 1, DictUtils.findLabelByTypeAndKey("subjectImport", 3))
					.setIndexs(rowDefinition.getIndexs());

			for(int i = 1; i <= size; i++ ){
				((RowDefinition.GroupColumnDefinition) indication).addColumn(RowDefinition.ColumnDefinition.createCommonColumn(index + i, indicationSort[i-1]).setValidateDefinition(subGrade2Validate).setDataValidationDefinition(subGrade2Validate));
			}
			//rowDefinition.addColumn(subGrade2Validate)
			rowDefinition.addColumn(indication);
		}

		rowDefinition.addColumn(no);
		rowDefinition.addColumn(score);
		rowDefinition.addColumn(detail);
		rowDefinition.addColumn(remark);

		return rowDefinition;
	}


	/**
	 * 返回题目导入成绩的头结构
	 * @param courseGradeComposeId
	 * @return
	 */
	public RowDefinition getComposeDetailScoreDefinition(Long courseGradeComposeId,Long batchId) {
		List<CcCourseGradeComposeDetail> ccCourseGradeComposeDetails = CcCourseGradeComposeDetail.dao.topicList(courseGradeComposeId, batchId);
        // List<CcCourseGradeComposeDetail> ccCourseGradeComposeDetails1 = CcCourseGradeComposeDetail.dao.findFilteredByColumn("course_gradecompose_id", courseGradeComposeId);
         Integer size = ccCourseGradeComposeDetails.size();

		// ----------------------------------------- 处理head -----------------------------------
		RowDefinition rowDefinition = new RowDefinition( size + 3);
		RowDefinition.ColumnDefinition index = RowDefinition.ColumnDefinition.createCommonColumn(0, DictUtils.findLabelByTypeAndKey("subjectScoreImport", 1));
		RowDefinition.ColumnDefinition no = RowDefinition.ColumnDefinition.createCommonColumn(1, DictUtils.findLabelByTypeAndKey("subjectScoreImport", 2));
		RowDefinition.ColumnDefinition name = RowDefinition.ColumnDefinition.createCommonColumn(2, DictUtils.findLabelByTypeAndKey("subjectScoreImport", 3));
		RowDefinition.ColumnDefinition className = RowDefinition.ColumnDefinition.createCommonColumn(3, DictUtils.findLabelByTypeAndKey("subjectScoreImport", 4));

		rowDefinition.addColumn(index);
		rowDefinition.addColumn(no);
		rowDefinition.addColumn(name);
		rowDefinition.addColumn(className);

		for(int i = 1; i <= size ; i ++ ){
			CcCourseGradeComposeDetail ccCourseGradeComposeDetail = ccCourseGradeComposeDetails.get(i-1);
			rowDefinition.addColumn(RowDefinition.ColumnDefinition.createCommonColumn(i + 3, String.format("%s(%s)", ccCourseGradeComposeDetail.getStr("name"), ccCourseGradeComposeDetail.getBigDecimal("score"))));
		}

		return rowDefinition;
	}


	/**
	 * 验证导入的题目是否合理
	 * @param subjects
	 * @param ccCourseGradeComposeDetails
	 * @param courseGradeComposeId
	 * @param indicationIdMap
	 * @param ccCourseGradecomposeDetailIndications
	 * @param ccIndications
	 * @return
	 */
	public boolean validateImportSubject(List<Map<String, Object>> subjects, List<CcCourseGradeComposeDetail> ccCourseGradeComposeDetails, Long courseGradeComposeId, Map<String, Long> indicationIdMap, List<CcCourseGradecomposeDetailIndication> ccCourseGradecomposeDetailIndications, List<String> ccIndications,Long batchId) {
		Date date = new Date();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
        List<Map<String, Object>> errorList = Lists.newArrayList();

        //数据库已经存在的题目
		List<CcCourseGradeComposeDetail> ccCourseGradeComposeDetailList = CcCourseGradeComposeDetail.dao.topicList(courseGradeComposeId, batchId);
		//List<CcCourseGradeComposeDetail> ccCourseGradeComposeDetailList = CcCourseGradeComposeDetail.dao.findFilteredByColumn("course_gradecompose_id", courseGradeComposeId);
        List<String> names = Lists.newArrayList();
        for(CcCourseGradeComposeDetail ccCourseGradeComposeDetail : ccCourseGradeComposeDetailList){
			names.add(ccCourseGradeComposeDetail.getStr("name"));
		}

		//题号相同的题目对应的行数
		List<String> nos = Lists.newArrayList();
		Map<String, String> sameNoMap = new HashMap<>();

		//验证导入的数据是否存在问题，插件已自动处理课程目标是否重复问题
        for(int i = 1; i <= subjects.size(); i++){
        	Map<String, Object> subject = subjects.get(i-1);
        	subject.put("isError", false);
        	subject.put("reasons", new ArrayList<String>());

			String noDic = DictUtils.findLabelByTypeAndKey("subjectImport", 1);
			String scoreDic = DictUtils.findLabelByTypeAndKey("subjectImport", 2);
			String supportIndicationDic = DictUtils.findLabelByTypeAndKey("subjectImport", 3);
			String detailDic = DictUtils.findLabelByTypeAndKey("subjectImport", 4);
			String remarkDic = DictUtils.findLabelByTypeAndKey("subjectImport", 5);

        	String no = resolveNo(ConvertUtils.convert(subject.get(noDic), String.class));
        	String score = ConvertUtils.convert(subject.get(scoreDic), String.class);
        	String detail = ConvertUtils.convert(subject.get(detailDic), String.class);
        	String remark = ConvertUtils.convert(subject.get(remarkDic), String.class);

        	if(StrKit.isBlank(no) || StrKit.isBlank(score)){
        		errorList.add(subject);
				subject.put("isError", true);
				List<String> reasons = ConvertUtils.convert(subject.get("reasons"), List.class);
				reasons.add(String.format("excel中的第%s行的%s或%s为空", i + 3, noDic, scoreDic));
				subject.put("reasons", reasons);
			}

			//分值必须是大于0小于1000且最多保留2位小数的正数
			if(!isScoreLegal(score)){
				errorList.add(subject);
				subject.put("isError", true);
				List<String> reasons = ConvertUtils.convert(subject.get("reasons"), List.class);
				reasons.add(String.format("excel中的第%s行的%s不是大于0小于1000的正数", i + 3, scoreDic));
				subject.put("reasons", reasons);
			}

			//验证题号是否重复
			String column = sameNoMap.get(no);
			if(StrKit.isBlank(column)){
				sameNoMap.put(no, String.format("%s", i+3));
			}else{
				sameNoMap.put(no, String.format("%s,%s", column, i+3));
			}
			if(!nos.contains(no)){
				nos.add(no);
			}else{
				errorList.add(subject);
				subject.put("isError", true);
				List<String> reasons = ConvertUtils.convert(subject.get("reasons"), List.class);
				reasons.add(String.format("excel中的第%s行的题号的%s重复了", sameNoMap.get(no), noDic));
				subject.put("reasons", reasons);
			}

			//验证该题目数据库是否已经存在了
           if(names.contains(no)){
			   errorList.add(subject);
			   subject.put("isError", true);
			   List<String> reasons = ConvertUtils.convert(subject.get("reasons"), List.class);
			   reasons.add(String.format("excel中的第%s行的题号已在系统中存在", i + 3));
			   subject.put("reasons", reasons);
		   }

		    Long courseGradeComposeDetailId = idGenerate.getNextValue();
			CcCourseGradeComposeDetail ccCourseGradeComposeDetail = new CcCourseGradeComposeDetail();
			ccCourseGradeComposeDetail.set("id", courseGradeComposeDetailId);
			ccCourseGradeComposeDetail.set("create_date", date);
			ccCourseGradeComposeDetail.set("modify_date", date);
			ccCourseGradeComposeDetail.set("name", no);
			ccCourseGradeComposeDetail.set("score", score);
			ccCourseGradeComposeDetail.set("detail", detail);
			ccCourseGradeComposeDetail.set("remark", remark);
			ccCourseGradeComposeDetail.set("course_gradecompose_id", courseGradeComposeId);
			ccCourseGradeComposeDetail.set("is_del", false);
			if (batchId != null ){
				ccCourseGradeComposeDetail.set("batch_id", batchId);
			}

			ccCourseGradeComposeDetails.add(ccCourseGradeComposeDetail);

			//验证题目是否重复关联课程目标
			List<String> repeatList = Lists.newArrayList();
			Set<String> unRepeatList = new HashSet<>();

			//题目与课程目标的关联
			if(!ccIndications.isEmpty()){
				for(String indication : ccIndications){
					 //判断题目是否关联课程目标
                     String value = ConvertUtils.convert(subject.get(indication), String.class);
					Long aLong = indicationIdMap.get(indication);

					if(StrKit.notBlank(value)){
						 repeatList.add(value);
						 unRepeatList.add(value);
						 CcCourseGradecomposeDetailIndication ccCourseGradecomposeDetailIndication = new CcCourseGradecomposeDetailIndication();
						 ccCourseGradecomposeDetailIndication.set("id", idGenerate.getNextValue());
						 ccCourseGradecomposeDetailIndication.set("create_date", date);
						 ccCourseGradecomposeDetailIndication.set("modify_date", date);
						 ccCourseGradecomposeDetailIndication.set("course_gradecompose_detail_id", courseGradeComposeDetailId);
						 ccCourseGradecomposeDetailIndication.set("indication_id", indicationIdMap.get(indication));
						 ccCourseGradecomposeDetailIndication.set("is_del", false);
						 ccCourseGradecomposeDetailIndications.add(ccCourseGradecomposeDetailIndication);
					 }
				}

				if(repeatList.size() != unRepeatList.size()){
					errorList.add(subject);
					subject.put("isError", true);
					List<String> reasons = ConvertUtils.convert(subject.get("reasons"), List.class);
					reasons.add(String.format("excel中的第%s行的题目的%s重复了", i + 3, supportIndicationDic));
					subject.put("reasons", reasons);
				}
			}
		}
		return errorList.isEmpty();
	}


	//验证分数必须大于0小于1000且最多保留2位小数
	public boolean isScoreLegal(String str){
		return isScoreLegal(str, new BigDecimal(1000));
	}


	public boolean isScoreLegal(String str, BigDecimal max){
		if(!NumberUtils.isNumber(str)){
			return false;
		}
		BigDecimal score = new BigDecimal(str);
		if(PriceUtils.greaterThan(new BigDecimal(0), score) || PriceUtils.lessThan(max, score)){
			return false;
		}
		return true;
	}


	//题号输入3时，解析出来为3.0会与数据库不相同
	public String resolveNo(String no){
		if(StrKit.isBlank(no)){
			return no;
		}
		if(NumberUtils.isNumber(no) && no.endsWith(".0")){
			no = no.substring(0, no.indexOf("."));
		}
		return no;
	}

	/**
	 * 导入学生题目成绩是否合理
	 * @param subjectMap
	 *         题目key题号(分数)value编号map
	 * @param studentIds
	 *         学生编号数组
	 * @param detailIds
*              题目编号数组
	 * @param addList
*              需要增加得学生题目分数
	 * @param editList
*              需要更新的学生题目分数
	 * @param subjects
*              excel中解析的数据情况
	 * @param eduClassId
	 *         教学班编号
	 * @param courseGradeComposeId
	 *         成绩组成编号
	 * @param studentList
	 *          教学班下学生列表
	 * @param ccEduclass
	 *          教学班
	 * @param subjectScoreMap
	 *         题目key题号(分数)value分数map
	 * @return*/
	public boolean validateImportSubjectScore(Map<String, Long> subjectMap, Set<Long> studentIds, Set<Long> detailIds, List<CcCourseGradecomposeStudetail> addList, List<CcCourseGradecomposeStudetail> editList, List<Map<String, Object>> subjects, Long eduClassId, Long courseGradeComposeId, List<CcStudent> studentList, CcEduclass ccEduclass, Map<String, BigDecimal> subjectScoreMap,Long batchId) {

		Map<String, Long> studentDetailMap = new HashMap<>();
		//教学班下在某个成绩组成下已存在的学生题目
		List<CcCourseGradecomposeStudetail> ccCourseGradecomposeStudetails = CcCourseGradecomposeStudetail.dao.findStudetail(eduClassId, courseGradeComposeId,batchId);
		for(CcCourseGradecomposeStudetail ccCourseGradecomposeStudetail : ccCourseGradecomposeStudetails){
			studentDetailMap.put(String.format("%s-%s(%s)", ccCourseGradecomposeStudetail.getStr("student_no"), ccCourseGradecomposeStudetail.getStr("name"), ccCourseGradecomposeStudetail.getBigDecimal("score")), ccCourseGradecomposeStudetail.getLong("id"));
		}

		//学生key学号value编号map
		Map<String, Long> studentIdMap = new HashMap<>();
		Map<String, String> studentNameMap = new HashMap<>();
		for(CcStudent ccStudent : studentList){
			studentIdMap.put(ccStudent.getStr("student_no"), ccStudent.getLong("id"));
			studentNameMap.put(ccStudent.getStr("student_no"), ccStudent.getStr("name"));
		}

		Date date = new Date();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		List<Map<String, Object>> errorList = Lists.newArrayList();

		//学号相同的学生对应的行数
		List<String> nos = Lists.newArrayList();
		Map<String, String> sameNoMap = new HashMap<>();
		for(int i = 1; i <= subjects.size(); i++){

			Map<String, Object> subject = subjects.get(i-1);
			subject.put("isError", false);
			subject.put("reasons", new ArrayList<String>());

			String indexDic = DictUtils.findLabelByTypeAndKey("subjectScoreImport", 1);
			String noDic =  DictUtils.findLabelByTypeAndKey("subjectScoreImport", 2);
			String nameDic =  DictUtils.findLabelByTypeAndKey("subjectScoreImport", 3);
			String classNameDic =  DictUtils.findLabelByTypeAndKey("subjectScoreImport", 4);

			String index = ConvertUtils.convert(subject.get(indexDic), String.class);
			String no = ConvertUtils.convert(subject.get(noDic), String.class);
			String name = ConvertUtils.convert(subject.get(nameDic), String.class);
			String className = ConvertUtils.convert(subject.get(classNameDic), String.class);

			if(StrKit.isBlank(index) || StrKit.isBlank(no) || StrKit.isBlank(name) || StrKit.isBlank(className)){
				errorList.add(subject);
				subject.put("isError", true);
				List<String> reasons = ConvertUtils.convert(subject.get("reasons"), List.class);
				reasons.add(String.format("excel中的第%s行的%s或%s或%s或%s为空", i + 2, index, no, name, className));
				subject.put("reasons", reasons);
			}

           //验证序号是否正确
			if(!Integer.valueOf(index).equals(i)){
				errorList.add(subject);
				subject.put("isError", true);
				List<String> reasons = ConvertUtils.convert(subject.get("reasons"), List.class);
				reasons.add(String.format("第%s个学生的序号为%s,不一致请检查", i, index));
				subject.put("reasons", reasons);
			}

			//验证excel中的学生是否重复
			String column = sameNoMap.get(no);
			if(StrKit.isBlank(column)){
				sameNoMap.put(no, String.format("%s", index));
			}else{
				sameNoMap.put(no, String.format("%s,%s", column, index));
			}
			if(!nos.contains(no)){
				nos.add(no);
			}else{
				errorList.add(subject);
				subject.put("isError", true);
				List<String> reasons = ConvertUtils.convert(subject.get("reasons"), List.class);
				reasons.add(String.format("excel中序号为%s的学生重复了", sameNoMap.get(no)));
				subject.put("reasons", reasons);
			}

			//验证教学班下是否有该学生
			if(!studentIdMap.keySet().contains(no)){
				errorList.add(subject);
				subject.put("isError", true);
				List<String> reasons = ConvertUtils.convert(subject.get("reasons"), List.class);
				reasons.add("教学班下不存在该学生");
				subject.put("reasons", reasons);
			}else if(!studentNameMap.get(no).equals(name)){
				errorList.add(subject);
				subject.put("isError", true);
				List<String> reasons = ConvertUtils.convert(subject.get("reasons"), List.class);
				reasons.add(String.format("学号和姓名不匹配系统中姓名为%s", studentNameMap.get(no)));
				subject.put("reasons", reasons);
			}

            //教学班名称是否一致
			if(!ccEduclass.getStr("educlass_name").equals(className)){
				errorList.add(subject);
				subject.put("isError", true);
				List<String> reasons = ConvertUtils.convert(subject.get("reasons"), List.class);
				reasons.add(String.format("教学班名称和系统中不一致，系统中为%s", ccEduclass.getStr("educlass_name")));
				subject.put("reasons", reasons);
			}

			Long studentId = studentIdMap.get(no);
			for(String detail : subjectMap.keySet()){
				//学生在题目下的分数，如果为空就忽略
				String value = ConvertUtils.convert(subject.get(detail), String.class);
				if(StringUtils.isNotBlank(value)){
					Long detailId = subjectMap.get(detail);
					if(studentId != null){
						studentIds.add(studentId);
					}
					detailIds.add(detailId);

					//判断是否大于0小于题目满分值的正数
					BigDecimal maxScore = subjectScoreMap.get(detail);
                    if(!isScoreLegal(value, maxScore)){
						errorList.add(subject);
						subject.put("isError", true);
						List<String> reasons = ConvertUtils.convert(subject.get("reasons"), List.class);
						reasons.add(String.format("在题目【%s】下得分不是小于等于题目满分值%s的正数,请检查", detail, maxScore));
						subject.put("reasons", reasons);
					}else{
						BigDecimal score = new BigDecimal(value);
						String key = String.format("%s-%s", no, detail);
                         //判断是新增的还是更新的
						if(studentDetailMap.keySet().contains(key)){
							CcCourseGradecomposeStudetail ccCourseGradecomposeStudetail = new CcCourseGradecomposeStudetail();
							ccCourseGradecomposeStudetail.set("id", studentDetailMap.get(key));
							ccCourseGradecomposeStudetail.set("modify_date", date);
							ccCourseGradecomposeStudetail.set("score", score);
                            editList.add(ccCourseGradecomposeStudetail);
						}else{
							CcCourseGradecomposeStudetail ccCourseGradecomposeStudetail = new CcCourseGradecomposeStudetail();
							ccCourseGradecomposeStudetail.set("id", idGenerate.getNextValue());
							ccCourseGradecomposeStudetail.set("create_date", date);
							ccCourseGradecomposeStudetail.set("modify_date", date);
							ccCourseGradecomposeStudetail.set("student_id", studentId);
							ccCourseGradecomposeStudetail.set("detail_id", detailId);
							ccCourseGradecomposeStudetail.set("score", score);
							ccCourseGradecomposeStudetail.set("is_del", false);
							addList.add(ccCourseGradecomposeStudetail);
						}
					}
				}
			}
		}
		return errorList.isEmpty();
	}
}
