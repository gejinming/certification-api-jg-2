package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcEduclassAchieveReport;
import com.gnet.model.admin.CcEduclassAssessReport;
import com.gnet.model.admin.CcIndication;
import com.jfinal.kit.PathKit;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询持续改进报告和评价表的数据
 * 
 * @author GJM
 * @Date 2020年08月26日
 */
@Service("EM01211")
public class EM01211 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Long edclessId = paramsLongFilter(param.get("classId"));
		Long courseId = paramsLongFilter(param.get("courseId"));
		HashMap<Object, Object> result = new HashMap<>();

		CcEduclassAchieveReport achieveReport = CcEduclassAchieveReport.dao.findAchieveReport(edclessId);
		if (achieveReport != null ){
			result.put("id",achieveReport.getLong("id"));
			result.put("targetRequire",achieveReport.getStr("target_require"));
			result.put("achieveAnalysis",achieveReport.getStr("achieve_analysis"));
			result.put("teachDocument", achieveReport.getStr("teach_document"));
			result.put("teachModified", achieveReport.getStr("teach_modified"));
			result.put("problemModified", achieveReport.getStr("problem_modified"));
			result.put("achieveResult", achieveReport.getStr("achieve_result"));
			result.put("assessDocunment", achieveReport.getStr("assess_docunment"));
			result.put("courseModified", achieveReport.getStr("course_modified"));
			result.put("courseImprovement", achieveReport.getStr("course_improvement"));
			result.put("assessRequire", achieveReport.getStr("assess_require"));
			result.put("reportDate",achieveReport.getStr("report_date"));
			result.put("assessDate",achieveReport.getStr("assess_date"));
			result.put("endPaper",achieveReport.getStr("end_paper"));
			result.put("problemContent",achieveReport.getStr("problem_content"));
			result.put("courseInfo",achieveReport.getStr("course_info"));
			result.put("teacherMothed",achieveReport.getStr("teacher_mothed"));
			result.put("assessMothed",achieveReport.getStr("assess_mothed"));
			result.put("testAnalysis",achieveReport.getStr("test_analysis"));
			result.put("courseLearTarget",achieveReport.getStr("course_lear_target"));
			result.put("personAchieveAnalyze",achieveReport.getStr("person_achieve_analyze"));
		}

		List<CcEduclassAssessReport> assessReportList = CcEduclassAssessReport.dao.findAssessReport(edclessId, null,null);
		//课程的课程目标
		ArrayList<Object> indicationListReport = new ArrayList<>();
		List<CcIndication> ccIndicationList = CcIndication.dao.findCourseIndicationPointReport(courseId);
		for (CcIndication temp:ccIndicationList){
			HashMap<Object, Object> indication = new HashMap<>();
			Long indicationId = temp.getLong("id");
			Integer sort = temp.getInt("sort");
			String content = temp.getStr("content");
			indication.put("indicationId",indicationId);
			indication.put("content",content);
			indication.put("sort",sort);
			Long pointId = temp.getLong("pointId");
			List<CcEduclassAssessReport> assessReport = CcEduclassAssessReport.dao.findAssessReport(edclessId, indicationId,pointId);
			for (CcEduclassAssessReport temps:assessReport){
				String reachWay = temps.getStr("reach_way");
				String assessGist = temps.getStr("assess_gist");
				String assessMethod = temps.getStr("assess_method");
				String indicationAnalyze = temps.getStr("indication_analyze");
				indication.put("reachWay",reachWay);
				indication.put("assessGist",assessGist);
				indication.put("assessMethod",assessMethod);
				indication.put("indicationAnalyze",indicationAnalyze);

			}
			indicationListReport.add(indication);
		}
		result.put("indicationListReport ", indicationListReport);

		return renderSUC(result, response, header);
	}


}
