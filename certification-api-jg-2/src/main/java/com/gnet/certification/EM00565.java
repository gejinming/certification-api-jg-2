package com.gnet.certification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

import com.gnet.model.admin.CcScoreStuIndigrade;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


/**
 * 批量保存更新教学班学生成绩
 * 
 * @author XZL
 * 
 * @date 2016年7月24日
 *
 */
@Service("EM00565")
@Transactional(readOnly=false)
public class EM00565 extends BaseApi implements IApi{
	

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		Map<String, Object> result = new HashMap<>();
		//学生指标点关联课程成绩组成成绩
		JSONArray scoreStuIndigradeArray = paramsJSONArrayFilter(param.get("scoreStuIndigradeArray"));
		//有哪些开课课程成绩组成元素与指标点编号
		List<Long> courseGradecomposeIndicationIds = Lists.newArrayList();
		
		if(scoreStuIndigradeArray.isEmpty()){
			return renderFAIL("0632", response, header);
		}
		//把courseGradecomposeIndicationId作为key用于判断增加的数据有没有和数据库重复的
	    Map<Long, List<CcScoreStuIndigrade>> map = Maps.newHashMap();
	    //成绩不为空的对象list
	    List<CcScoreStuIndigrade> saveList = Lists.newArrayList();
	    //需要更新的list
	    List<CcScoreStuIndigrade> updateList = Lists.newArrayList();
	    //以studentId和gradecomposeIndicationId组成一个字符串，判断需要增加的数据本身有没有重复
	    List<String> groupList = Lists.newArrayList();
	    //重复数据
	    List<String> repeatList = Lists.newArrayList();
	    
	    Date date = new Date();
	    IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
	    for(int i=0; i<scoreStuIndigradeArray.size(); i++){
	    	CcScoreStuIndigrade scoreStuIndigrade = new CcScoreStuIndigrade();
	    	JSONObject temp = (JSONObject)scoreStuIndigradeArray.get(i);
	    	if(temp.get("studentId") == null){
	    		return renderFAIL("0330", response, header);
	    	}
	    	if(temp.get("gradecomposeIndicationId") == null){
	    		return renderFAIL("0496", response, header);
	    	}
	    	if(temp.get("grade") == null){
	    		return renderFAIL("0630", response, header);
	    	}	
	    	Long studentId = Long.valueOf(String.valueOf(temp.get("studentId")));
	    	Long gradecomposeIndicationId = Long.valueOf(String.valueOf(temp.get("gradecomposeIndicationId")));
	    	BigDecimal grade = BigDecimal.valueOf(Long.valueOf(String.valueOf(temp.get("grade"))));
	    	
	    	//证明这是要更新的数据
	    	if(temp.get("scoreStuIndigradeId") != null){
	    		scoreStuIndigrade.set("id", Long.valueOf(String.valueOf(temp.get("scoreStuIndigradeId"))));
	    		scoreStuIndigrade.set("modify_date", date);
	    		scoreStuIndigrade.set("grade", grade);
	    		updateList.add(scoreStuIndigrade);
	    	}else{
	    		//需要保存的数据
	    		scoreStuIndigrade.set("id", idGenerate.getNextValue());
	    		scoreStuIndigrade.set("create_date", date);
	    		scoreStuIndigrade.set("modify_date", date);
	    		scoreStuIndigrade.set("student_id", studentId);
		    	scoreStuIndigrade.set("gradecompose_indication_id", gradecomposeIndicationId);
		    	scoreStuIndigrade.set("grade", grade);
		    	scoreStuIndigrade.set("is_del", Boolean.FALSE);
		    	saveList.add(scoreStuIndigrade);
	    	}
	    	
	    	//得到有需要增加开课课程成绩组成元素与指标点关联编号的列表
	    	if(!courseGradecomposeIndicationIds.contains(gradecomposeIndicationId)){
	    		courseGradecomposeIndicationIds.add(gradecomposeIndicationId);
	    	}
	    	//排除某个学生在某门课程某个指标点下的某个成绩组成的成绩有没有重复添加
	    	String key = String.valueOf(studentId) + CcScoreStuIndigrade.split + String.valueOf(gradecomposeIndicationId);
	    	if(!groupList.contains(key)){
	    		groupList.add(key);
	    	}else{
	    		repeatList.add(key);
	    	}
	    }
	    
	    if(!repeatList.isEmpty()){
	    	return renderFAIL("0634", response, header);
	    }
	    
	    //如果需要更新的数据不为空则更新成绩
	    if(!updateList.isEmpty()){
	    	if(!CcScoreStuIndigrade.dao.batchUpdate(updateList, "modify_date,grade")){
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
	    }
		
	    //需要增加的学生成绩不为空
	    if(!saveList.isEmpty()){
	    	for(CcScoreStuIndigrade r : saveList){
		    	Long courseGradecomposeIndicationId = r.getLong("gradecompose_indication_id");
		    	List<CcScoreStuIndigrade> temp = map.get(courseGradecomposeIndicationId);
		    	if(temp == null){
		    		temp = new ArrayList<CcScoreStuIndigrade>();
		    		map.put(courseGradecomposeIndicationId, temp);
		    	}
		    	temp.add(r);
		    }
	    	
	    	//开课课程成绩组成元素指标点关联编号
			Long[] cgciIdsArray = courseGradecomposeIndicationIds.toArray(new Long[courseGradecomposeIndicationIds.size()]);	
			List<CcScoreStuIndigrade> scoreStuIndigrades = CcScoreStuIndigrade.dao.findFilteredByColumnIn("gradecompose_indication_id", cgciIdsArray);
			//数据库中已经有学生成绩存在了
			if(!scoreStuIndigrades.isEmpty()){
				 //处理成一个map把courseGradecomposeIndicationId作为key
			    Map<Long, List<CcScoreStuIndigrade>> existMap = Maps.newHashMap();
			    for(CcScoreStuIndigrade record : scoreStuIndigrades){
			    	Long courseGradecomposeIndicationId = record.getLong("gradecompose_indication_id");
			    	List<CcScoreStuIndigrade> temp = existMap.get(courseGradecomposeIndicationId);
			    	if(temp == null){
			    		temp = new ArrayList<CcScoreStuIndigrade>();
			    		existMap.put(courseGradecomposeIndicationId, temp);
			    	}
			    	temp.add(record);
			    }
			    //与数据库重复的学生编号
			    List<Long> repeatStudentIds = Lists.newArrayList();
			    for(Entry<Long, List<CcScoreStuIndigrade>> entry : map.entrySet()){
			    	Long courseGradecomposeIndicationId = entry.getKey();
			    	List<CcScoreStuIndigrade> tempList = entry.getValue();
			    	//通过key得到数据库存在的list
			    	List<CcScoreStuIndigrade> existList = existMap.get(courseGradecomposeIndicationId);
			    	if(!existList.isEmpty()){
			    		//数据库存在的学生编号
			    		List<Long> studentIds = Lists.newArrayList();
			    		for(CcScoreStuIndigrade temp : existList){
			    			studentIds.add(temp.getLong("student_id"));
			    		}
			    		//同一开课课程成绩组成元素与指标点关联编号下，如果学生编号重复记录下来
			    		for(int i=0; i<tempList.size(); i++){
			    			Long studentId = tempList.get(i).getLong("student_id");
			    			if(studentIds.contains(studentId)){
			    				repeatStudentIds.add(studentId);
			    			}
			    		}
			    	}
			    }
			    
			    if(!repeatStudentIds.isEmpty()){
			    	return renderFAIL("0635", response, header);
			    }
			    
			}
			
	    }
	    
    	if(!CcScoreStuIndigrade.dao.batchSave(saveList)){
    		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
    	}
			    
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}
	
}
