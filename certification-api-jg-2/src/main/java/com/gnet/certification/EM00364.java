package com.gnet.certification;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcEvalute;
import com.gnet.model.admin.CcReportEduclassEvalute;
import com.gnet.model.admin.CcStudentEvalute;

/**
 * 批量删除教师课程考评点
 * 
 * @author sll
 * 
 * @date 2016年07月04日 15:58:01
 *
 */
@Service("EM00364")
@Transactional(readOnly=false)
public class EM00364 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String,Boolean> result = new HashMap<>();
		
		Date date = new Date();
		
		List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		//判断是否存在考评点成绩
		if (CcStudentEvalute.dao.countStudentEvalute(idsArray)) {
			return renderFAIL("0378", response, header);
		}
		
//		//判断是否存在教学班达成度报表统计表(评分表分析法)
//		List<CcReportEduclassEvalute> ccReportEduclassEvalutes = CcReportEduclassEvalute.dao.findFilteredByColumnIn("evalute_id", idsArray);
//		if (!ccReportEduclassEvalutes.isEmpty()) {
//			return renderFAIL("0379", response, header);
//		}
		
		List<CcEvalute> ccEvaluteList = CcEvalute.dao.findFilteredByColumnIn("id", idsArray);
		// 找到被删掉的数据对应的课程-指标点。用于对其剩下的课程-指标点 排序 
		List<Map<String, Long>> teacherCourseIdAndIndicationIds = new ArrayList<>();
		// <teacherCourseId + "," + indicationId  ，   这个教师课程和指标点下考评点的数量>
		Map<String, Integer> isHave = new HashMap<>();
		for(CcEvalute temp : ccEvaluteList) {
			Long indicationId = temp.getLong("indication_id");
			Long teacherCourseId = temp.getLong("teacher_course_id");
			Integer num = isHave.get(teacherCourseId + "," + indicationId);
			if(num == null || num < 1) {
				isHave.put(teacherCourseId + "," + indicationId, 1);
				Map<String, Long> tempMap = new HashMap<>();
				tempMap.put("teacherCourseId", teacherCourseId);
				tempMap.put("indicationId", indicationId);
				teacherCourseIdAndIndicationIds.add(tempMap);
			} else {
				isHave.put(teacherCourseId + "," + indicationId, num + 1);
			}
		}
		
		// 删除考评点CcEvalute
		if(!CcEvalute.dao.deleteAll(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		// 重新排序的列表获取
		List<CcEvalute> ccEvaluteListSave = CcEvalute.dao.findFilteredInTeacherCourseIdAndIndicationId(teacherCourseIdAndIndicationIds);
		Integer indexNum = 1;
		Long lastIndicationId = 0L;
		Long lastTeacherCourseId = 0L;
		Integer lastType = 0;
		Date sortDate = new Date();
		for(CcEvalute temp : ccEvaluteListSave) {
			Long indicationId = temp.getLong("indication_id");
			Long teacherCourseId = temp.getLong("teacher_course_id");
			Integer type = temp.getInt("type");
			if(!lastIndicationId.equals(indicationId) || !lastTeacherCourseId.equals(teacherCourseId) || !lastType.equals(type)) {
				// 如果发现教师课程编号或者指标点编号发生变化，则直接设置为1
				indexNum = 1;
				lastIndicationId = indicationId;
				lastTeacherCourseId = teacherCourseId;
				lastType = type;
			}
			temp.set("index_num", indexNum);
			temp.set("modify_date", sortDate);
			indexNum++;
		}
		
		//批量保存序号
		if (!CcEvalute.dao.batchUpdate(ccEvaluteListSave, "modify_date,index_num")){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}	
	
