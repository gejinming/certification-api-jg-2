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
import com.gnet.service.CcStudentService;
import com.gnet.utils.FileUtils;
import com.gnet.utils.StringUtil;
import com.google.common.collect.Lists;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 题目导入模板下载
 * 
 * @author xzl
 * @Date 2018年2月6日14:30:31
 */
@Transactional(readOnly = false)
@Service("EM00970")
public class EM00970 extends BaseApi implements IApi {
	
	private static final Logger logger = Logger.getLogger(EM00970.class);
	
	@Autowired
	private CcCourseGradecomposeDetailService ccCourseGradecomposeDetailService;
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Long courseGradeComposeId = paramsLongFilter(param.get("courseGradeComposeId"));

		if(courseGradeComposeId == null){
			return  renderFAIL("0475", response, header);
		}

		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findByCourseGradeComposeId(courseGradeComposeId);
		//达成度计算类型
		Integer resultType = ccTeacherCourse.getInt("result_type");
		if(ccTeacherCourse == null){
			return renderFAIL("0501", response, header);
		}

		// 判断录入成绩类型是否是由题目明细计算获得,1:指标点成绩直接输入,2、3:由题目明细计算获得
        if(!CcCourseGradecompose.SUMMARY_INPUT_SCORE.equals(ccTeacherCourse.getInt("input_score_type")) && !CcCourseGradecompose.SUMMARY_MANYINPUT_SCORE.equals(ccTeacherCourse.getInt("input_score_type"))){
			return renderFAIL("2102", response, header);
		}

		//开课课程下成绩组成关联的课程目标
		List<CcIndication> ccIndications = CcIndication.dao.findCourseGradeComposeId(courseGradeComposeId);
		Integer depth = ccIndications.isEmpty() ? 1 : 2;

		//获取文件
		String fileUrl = PathKit.getWebRootPath() + ConfigUtils.getStr("excel", "createPath");
		String exportUrl = PathKit.getWebRootPath() + ConfigUtils.getStr("excel", "createPath") + "题目导入模板.xls";

		try {
			// 判断是否存在路径，不存在就创建
			File dir = new File(fileUrl);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			// 考核
			RowDefinition rowDefinition = ccCourseGradecomposeDetailService.getComposeDetailDefinition(courseGradeComposeId,resultType);
			ExcelExporter.exportToExcel(depth, rowDefinition, new ExcelExporter.ExcelExporterDataProcessor() {
				@Override
				public List<List<String>> invoke() {
					List<List<String>> result = new ArrayList<>();
					return result;
				}
			}, exportUrl, "注意：1.题号不可重复导入。2.题目不支持课程目标可不填。",0);//3.分值必须是大于0小于1000的正数。4.分值最多只保留小数点后2位。

		} catch (Exception e) {
			e.printStackTrace();
			if (logger.isErrorEnabled()) {
				logger.error("生成题目导入模版失败", e);
			}
			return renderFAIL("400", response, header, StringUtil.judgeContainsStr(e.getMessage()) ? "请检查模板的是否正确。包括：1.题目是否存在重复。2.是否修改过结构。3.支持选择课程目标是否重复。4.题目分数最多只保留小数点后2位。解决方案：重新下载一份。" : e.getMessage());
		}

		return renderFILE(new File(exportUrl), response, header);
	}
}