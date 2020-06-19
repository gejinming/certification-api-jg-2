package com.gnet.certification;
import java.math.BigDecimal;
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
import com.gnet.model.admin.CcCourseGradecomposeIndication;
import com.gnet.model.admin.CcScoreStuIndigrade;
import com.gnet.model.admin.CcStudent;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


/**
 * 通过教学班编号和指标点编号得到课程指标下不同成绩组成的学生成绩
 * 
 * @author XZL
 * 
 * @date 2016年7月11日
 * 
 */
@Service("EM00564")
@Transactional(readOnly=true)
public class EM00564 extends BaseApi implements IApi {
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		Map<String, Object> result = new HashMap<>();
		
		//教学班编号
	    Long educlassId = paramsLongFilter(param.get("educlassId"));
		
	    //指标点编号
	    Long indicationId = paramsLongFilter(param.get("indicationId"));
	   
	   if(indicationId == null){
			return renderFAIL("0230", response, header);
		}
		
		if(educlassId == null){
			return renderFAIL("380", response, header);
		}
	    
		//开课课程成绩组成元素指标点关联list
		List<CcCourseGradecomposeIndication> courseGradecomposeIndications = CcCourseGradecomposeIndication.dao.findByEduclassIdAndIndicationId(educlassId, indicationId);
		//如果为空就直接返回
		if(courseGradecomposeIndications.isEmpty()){
			result.put("courseGradecomposeIndicationLists", courseGradecomposeIndications);
			return renderSUC(result, response, header);
		}
		
		//返回内容过滤
		List<Map<String, Object>> courseGradecomposeIndicationLists = Lists.newArrayList();
		for(CcCourseGradecomposeIndication temp : courseGradecomposeIndications){
			Map<String, Object> courseGradecomposeIndication = Maps.newHashMap();
			courseGradecomposeIndication.put("courseGradecomposeIndicationId", temp.getLong("courseGradecomposeIndicationId"));
			courseGradecomposeIndication.put("courseGradecomposeId", temp.getLong("courseGradecomposeId"));
			courseGradecomposeIndication.put("gradecomposeName", temp.getStr("gradecomposeName"));
			courseGradecomposeIndication.put("courseGradecomposeDetailIdCount", temp.getLong("courseGradecomposeDetailIdCount"));
			courseGradecomposeIndicationLists.add(courseGradecomposeIndication);
		}
		
		//某个教学班在某一个指标点下的成绩组成已经有成绩的学生
		List<CcScoreStuIndigrade> ccScoreStuIndigrades = CcScoreStuIndigrade.dao.findDetailByClassIdAndIndicationId(educlassId, indicationId);
		Map<String, Object> studentInScoreMap = Maps.newLinkedHashMap();
		//某个教学班的所有学生
		List<CcStudent> studentLits = CcStudent.dao.findByEduclassId(educlassId);
		if(studentLits.isEmpty()){
			result.put("studentInScoreMap", studentInScoreMap);
			return renderSUC(result, response, header);
		}
		
		for (CcScoreStuIndigrade ccScoreStuIndigrade : ccScoreStuIndigrades) {
			String studentNo = ccScoreStuIndigrade.getStr("student_no");
			Map<String, Object> studentInfo = null;
			if (studentInScoreMap.get(studentNo) == null) {
				studentInfo = Maps.newHashMap();
				// 学生基本信息
				studentInfo.put("id", ccScoreStuIndigrade.getLong("student_id"));
				studentInfo.put("studentNo", studentNo);
				studentInfo.put("studentName", ccScoreStuIndigrade.getStr("student_name"));
				// 学生成绩项保存
				studentInfo.put("scoreMap", new HashMap<Long, BigDecimal>());
				studentInScoreMap.put(studentNo, studentInfo);
			} else{
				studentInfo = (Map<String, Object>) studentInScoreMap.get(studentNo);
			}
			Map<Long, Object> scoreMap = (Map<Long, Object>) studentInfo.get("scoreMap");
			Map<String, Object> scoreItem = Maps.newHashMap();
			scoreItem.put("grade", ccScoreStuIndigrade.getBigDecimal("grade"));
			scoreItem.put("type", ccScoreStuIndigrade.getInt("type"));
			scoreItem.put("levelDetailId", ccScoreStuIndigrade.getLong("level_detail_id"));
			scoreItem.put("scoreStuIndigradeId", ccScoreStuIndigrade.getLong("id"));
			scoreMap.put(ccScoreStuIndigrade.getLong("gradecompose_indication_id"), scoreItem);
		}
		
		//对于没有录入成绩的学生也放入studentInScoreMap
		for(CcStudent student : studentLits){
			Map<String, Object> studentInfo = Maps.newHashMap();
			String studentNo = student.getStr("student_no");
			if(studentInScoreMap.get(studentNo) == null){
				studentInfo.put("id", student.getLong("id"));
				studentInfo.put("studentNo", studentNo);
				studentInfo.put("studentName", student.getStr("name"));
				// 学生成绩项保存
				studentInfo.put("scoreMap", new HashMap<Long, BigDecimal>());
				studentInScoreMap.put(studentNo, studentInfo);
			}
		}
		
		result.put("courseGradecomposeIndicationLists", courseGradecomposeIndicationLists);
		result.put("studentInScoreMap", studentInScoreMap);
		 
	    return renderSUC(result, response, header);
	
	}
}
