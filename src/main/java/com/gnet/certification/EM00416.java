package com.gnet.certification;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradecomposeStudetail;
import com.gnet.model.admin.CcScoreStuIndigrade;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


/**
 * 
 * 由教学班下的成绩组成的试卷分数计算该成绩组成对不同指标点的分数
 * @author xzl
 * @Date 2016年8月24日
 */
@Service("EM00416")
@Transactional(readOnly=false)
public class EM00416 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String, Object> result = new HashMap<String, Object>();
		
		//开课课程成绩组成编号
		Long courseGradecomposeId = paramsLongFilter(param.get("courseGradecomposeId"));
		//教学班编号
		Long educlassId = paramsLongFilter(param.get("educlassId"));
		
		if(courseGradecomposeId == null){
			return renderFAIL("0475", response, header);
		}
		
		if(educlassId == null){
			return renderFAIL("0380", response, header);
		}
		
		//某个教学班学生在各个指点下的成绩组成的成绩(根据成绩明细表得出)
		List<CcCourseGradecomposeStudetail> studentScoreList = CcCourseGradecomposeStudetail.dao.findByEduclassIdAndCourseGradecomposeId(courseGradecomposeId, educlassId);
		if(studentScoreList.isEmpty()){
			result.put("isSuccess", true);
			return renderSUC(result, response, header);
		}
		
		//某个教学班学生在各个指点下的成绩组成的成绩(根据考核成绩分析法学生指标点成绩表得出)
		List<CcScoreStuIndigrade> scoreStuIndigradeList = CcScoreStuIndigrade.dao.findByClassId(educlassId);
		//需要保存的list
		List<CcScoreStuIndigrade> saveList = Lists.newArrayList();
		//需要更新的list
		List<CcScoreStuIndigrade> updateList = Lists.newArrayList();
		
		Date date  =  new Date();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		if(scoreStuIndigradeList.isEmpty()){
			for(CcCourseGradecomposeStudetail studentScore : studentScoreList){
				CcScoreStuIndigrade temp = new CcScoreStuIndigrade();
				temp.set("id", idGenerate.getNextValue());
				temp.set("create_date", date);
				temp.set("modify_date", date);
				temp.set("gradecompose_indication_id", studentScore.getLong("courseGradecomposeIndicationId"));
				temp.set("student_id", studentScore.getLong("studentId"));
				temp.set("grade", studentScore.getBigDecimal("score"));
				temp.set("is_del", Boolean.FALSE);
				saveList.add(temp);
			}
	    	if(!CcScoreStuIndigrade.dao.batchSave(saveList)){
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
			
		}else{
		    //把计算得出的成绩courseGradecomposeIndicationId作为key用于判断是用来更新还是保存
		    Map<Long, List<CcScoreStuIndigrade>> map = Maps.newHashMap();
			//把已经存在考核成绩分析法学生指标点成绩表的成绩也处理成一个map用于区分哪些去更新哪些去保存
		    Map<Long, List<CcScoreStuIndigrade>> existMap = Maps.newHashMap();
		    
		    for(CcCourseGradecomposeStudetail temp :  studentScoreList){
		    	Long courseGradecomposeIndicationId = temp.getLong("courseGradecomposeIndicationId");
		    	CcScoreStuIndigrade scoreStuIndigrade = new CcScoreStuIndigrade();
		    	scoreStuIndigrade.set("gradecompose_indication_id", courseGradecomposeIndicationId);
		    	scoreStuIndigrade.set("student_id",  temp.getLong("studentId"));
		    	scoreStuIndigrade.set("grade",  temp.getBigDecimal("score"));
		    	
		    	List<CcScoreStuIndigrade> tempList = map.get(courseGradecomposeIndicationId);
		    	if(tempList == null){
		    		tempList = new ArrayList<CcScoreStuIndigrade>();
		    		map.put(courseGradecomposeIndicationId, tempList);
		    	}
		    	tempList.add(scoreStuIndigrade);
		    }
		    
		    
		    for(CcScoreStuIndigrade record : scoreStuIndigradeList){
		    	Long courseGradecomposeIndicationId = record.getLong("gradecompose_indication_id");
		    	List<CcScoreStuIndigrade> temp = existMap.get(courseGradecomposeIndicationId);
		    	if(temp == null){
		    		temp = new ArrayList<CcScoreStuIndigrade>();
		    		existMap.put(courseGradecomposeIndicationId, temp);
		    	}
		    	temp.add(record);
		    } 
		    
		    for(Entry<Long, List<CcScoreStuIndigrade>> entry : map.entrySet()){
		    	Long courseGradecomposeIndicationId = entry.getKey();
		    	List<CcScoreStuIndigrade> tempList = entry.getValue();
		    	//通过key得到数据库存在的list
		    	List<CcScoreStuIndigrade> existList = existMap.get(courseGradecomposeIndicationId);
		    	
		    	if(existList == null){
		    		for(CcScoreStuIndigrade temp : tempList){
	    				temp.set("id", idGenerate.getNextValue());
	    				temp.set("create_date", date);
	    				temp.set("modify_date", date);
	    				temp.set("gradecompose_indication_id", temp.getLong("gradecompose_indication_id"));
	    				temp.set("student_id", temp.getLong("student_id"));
	    				temp.set("grade", temp.getBigDecimal("grade"));
	    				temp.set("is_del", Boolean.FALSE); 
	    				saveList.add(temp);
		    		}	    		
		    	}else{
		    		Map<Long, CcScoreStuIndigrade> existStudentMap = Maps.newHashMap();
		    		for(CcScoreStuIndigrade temp : existList){
		    			existStudentMap.put(temp.getLong("student_id"), temp);			
		    		}
		    		for(CcScoreStuIndigrade temp : tempList){
		    			CcScoreStuIndigrade existTemp = existStudentMap.get(temp.getLong("student_id"));
		    			if(existTemp != null){
		    				existTemp.set("modify_date", date);
		    				existTemp.set("grade", temp.getBigDecimal("grade"));
		    				updateList.add(existTemp);
		    			}else{
		    				temp.set("id", idGenerate.getNextValue());
		    				temp.set("create_date", date);
		    				temp.set("modify_date", date);
		    				temp.set("gradecompose_indication_id", temp.getLong("gradecompose_indication_id"));
		    				temp.set("student_id", temp.getLong("student_id"));
		    				temp.set("grade", temp.getBigDecimal("grade"));
		    				temp.set("is_del", Boolean.FALSE); 
		    				saveList.add(temp);
		    			}		
		    		}
		    	}
		    }
		    
	    	if(!saveList.isEmpty() && !CcScoreStuIndigrade.dao.batchSave(saveList)){
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		        
	    	if(!updateList.isEmpty() && !CcScoreStuIndigrade.dao.batchUpdate(updateList, "modify_date,grade")){
				if(!saveList.isEmpty()){
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
				result.put("isSuccess", false);
				return renderSUC(result, response, header);			
		    }
		    	
		}
			
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}
	
}
