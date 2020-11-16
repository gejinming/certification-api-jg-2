package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.gnet.plugin.configLoader.ConfigUtils;
import com.gnet.plugin.poi.ExcelExporter;
import com.gnet.plugin.poi.RowDefinition;
import com.gnet.service.CcCourseGradecomposeDetailService;
import com.gnet.utils.StringUtil;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 题目成绩下载接口
 * 
 * @author xzl
 * @Date 2018年2月13日11:18:46
 */
@Transactional(readOnly = false)
@Service("EM00983")
public class EM00983 extends BaseApi implements IApi {
	
	private static final Logger logger = Logger.getLogger(EM00983.class);
	
	@Autowired
	private CcCourseGradecomposeDetailService ccCourseGradecomposeDetailService;
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Long courseGradeComposeId = paramsLongFilter(param.get("courseGradeComposeId"));
		final Long eduClassId = paramsLongFilter(param.get("eduClassId"));
		final Long batchId = paramsLongFilter(param.get("batchId"));
		if(courseGradeComposeId == null){
			return  renderFAIL("0475", response, header);
		}
		// 教学班编号为空过滤
		if (eduClassId == null) {
			return renderFAIL("0500", response, header);
		}

		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findByCourseGradeComposeId(courseGradeComposeId);
		if(ccTeacherCourse == null){
			return renderFAIL("0501", response, header);
		}

		// 判断录入成绩类型是否是由题目明细计算获得,1:指标点成绩直接输入,2,3:由题目明细计算获得
        if(!CcCourseGradecompose.SUMMARY_INPUT_SCORE.equals(ccTeacherCourse.getInt("input_score_type")) && !CcCourseGradecompose.SUMMARY_MANYINPUT_SCORE.equals(ccTeacherCourse.getInt("input_score_type"))){
			return renderFAIL("2102", response, header);
		}

		final CcEduclass ccEduclass = CcEduclass.dao.findFilteredById(eduClassId);
        if(ccEduclass == null){
        	return renderFAIL("0381", response, header);
		}

		final Map<String, String> studentDetailMap = new HashMap<>();
		//教学班下在某个成绩组成下已存在的学生题目
		List<CcCourseGradecomposeStudetail> ccCourseGradecomposeStudetails = CcCourseGradecomposeStudetail.dao.findStudetail(eduClassId, courseGradeComposeId,batchId);
		for(CcCourseGradecomposeStudetail ccCourseGradecomposeStudetail : ccCourseGradecomposeStudetails){
			studentDetailMap.put(String.format("%s-%s", ccCourseGradecomposeStudetail.getLong("student_id"), ccCourseGradecomposeStudetail.getLong("detail_id")), String.valueOf(ccCourseGradecomposeStudetail.getBigDecimal("score")));
		}

		//final List<CcCourseGradeComposeDetail> ccCourseGradeComposeDetails = CcCourseGradeComposeDetail.dao.findFilteredByColumn("course_gradecompose_id", courseGradeComposeId);
		List<CcCourseGradeComposeDetail> ccCourseGradeComposeDetails = CcCourseGradeComposeDetail.dao.topicList0(courseGradeComposeId, batchId);
		final List<CcStudent> studentList = CcStudent.dao.findByEduclassIdOrderByStudentNo(eduClassId);

		//获取文件
		String fileUrl = PathKit.getWebRootPath() + ConfigUtils.getStr("excel", "createPath");
		String exportUrl = PathKit.getWebRootPath() + ConfigUtils.getStr("excel", "createPath") + "题目成绩.xls";

		try {
			// 判断是否存在路径，不存在就创建
			File dir = new File(fileUrl);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			// 题目成绩导入
			RowDefinition rowDefinition = ccCourseGradecomposeDetailService.getComposeDetailScoreDefinition(courseGradeComposeId,batchId);

			ExcelExporter.exportToExcel(1, rowDefinition, new ExcelExporter.ExcelExporterDataProcessor() {
				@Override
				public List<List<String>> invoke() {
					List<List<String>> result = new ArrayList<>();
                    for(int i = 1; i <= studentList.size() ; i++){
                    	CcStudent ccStudent = studentList.get(i-1);
						List<String> data = new ArrayList<>();
						data.add(String.format("%s", i));
						data.add(ccStudent.getStr("student_no"));
						data.add(ccStudent.getStr("name"));
						data.add(ccEduclass.getStr("educlass_name"));
						for(CcCourseGradeComposeDetail ccCourseGradeComposeDetail : ccCourseGradeComposeDetails){
							String score = studentDetailMap.get(String.format("%s-%s", ccStudent.getLong("id"), ccCourseGradeComposeDetail.getLong("id")));
                            if(StringUtils.isBlank(score)){
                                data.add("");
                            }else {
                                data.add(score);
                            }
						}
						result.add(data);
					}
					return result;
				}
			}, exportUrl, "注意：1.该成绩组成下必须存在题目和教学班下必须存在学生才能正确导入。2.学生得分不得超过题目满分值。3.如若不填分数将不做处理，若得0分请填0。4.得分最多只保留小数点后2位。 ",1);

		} catch (Exception e) {
			e.printStackTrace();
			if (logger.isErrorEnabled()) {
				logger.error("生成成绩导入模版失败", e);
			}

			return renderFAIL("400", response, header, StringUtil.judgeContainsStr(e.getMessage()) ? "请检查模板的是否正确。包括：1.学生是否存在重复。2.是否修改过结构。解决方案：重新下载一份。" : e.getMessage());
		}

		return renderFILE(new File(exportUrl), response, header);
	}
}