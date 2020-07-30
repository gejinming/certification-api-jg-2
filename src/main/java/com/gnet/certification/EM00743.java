package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcEduclass;
import com.gnet.model.admin.CcStudent;
import com.gnet.model.admin.CcTeacherCourse;
import com.gnet.plugin.configLoader.ConfigUtils;
import com.gnet.plugin.poi.ExcelExporter;
import com.gnet.plugin.poi.RowDefinition;
import com.gnet.service.CcStudentService;
import com.gnet.utils.FileUtils;
import com.google.common.collect.Lists;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 考核成绩分析法教学班学生批量成绩录入Excel模板下载
 * 
 * @author SY
 * @Date 2017年10月6日
 */
@Transactional(readOnly = false)
@Service("EM00743")
public class EM00743 extends BaseApi implements IApi {
	
	private static final Logger logger = Logger.getLogger(EM00743.class);
	
	@Autowired
	private CcStudentService ccStudentService;
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		// 教学班编号
		final Long eduClassId = paramsLongFilter(param.get("eduClassId"));
		// 课程成绩组成编号
//		Long courseGradeComposeId = paramsLongFilter(param.get("courseGradeComposeId"));

		// 课程成绩组成编号列表
		List<Long> courseGradeComposeIds = paramsJSONArrayFilter(param.get("courseGradeComposeIds"), Long.class);

		// 教学班编号为空过滤
		if (eduClassId == null) {
			return renderFAIL("0500", response, header);
		}
		// 课程成绩组成为空过滤
		if (courseGradeComposeIds == null || courseGradeComposeIds.isEmpty()) {
			return renderFAIL("0490", response, header);
		}

//		List<Long> courseGradeComposeIds = new ArrayList<>();
//		courseGradeComposeIds.add(courseGradeComposeId);
		// 获得教师开课课程
		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findCourseByClassId(eduClassId);
		if (ccTeacherCourse == null) {
			return renderFAIL("0501", response, header);
		}
		
		// 判断是否是考核成绩法，不是就直接返回。教师开课类型,1:考核成绩分析法，2：评分表分析法
		Integer resultType = ccTeacherCourse.getInt("result_type");
		if(!CcTeacherCourse.RESULT_TYPE_SCORE.equals(resultType)) {
			return renderFAIL("1009", response, header, "课程类型必须为：考核成绩分析法。");
		}

		List<File> fileList = Lists.newArrayList();

		String zipFileUrl = PathKit.getWebRootPath() + ConfigUtils.getStr("excel", "createPath") + "学生成绩导入模板-" + System.currentTimeMillis() + ".zip";

		//获取文件
		for (Long courseGradeComposeId : courseGradeComposeIds) {
			//        String title = "注意：1.名单要求从第3行开始录入（例如：姓名为林木）2.学号以文本形式填写（左上角应该有个绿色三角才对）";
//        	String exportUrl = "F://_Use_One//output//student"+new Date().getTime()+".xls";
			String fileUrl = PathKit.getWebRootPath() + ConfigUtils.getStr("excel", "createPath");
			String exportUrl = PathKit.getWebRootPath() + ConfigUtils.getStr("excel", "createPath") + "学生成绩导入模板-" + System.currentTimeMillis() + ".xls";

			try {
				// 判断是否存在路径，不存在就创建
				File dir = new File(fileUrl);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				// 而外参数是从第几列开始的
				Integer startNaturalColumnIndex = 5;
//    			// 而外参数是从第几行开始的
//    			Integer startNaturalRowIndex = 1;
				if(CcTeacherCourse.RESULT_TYPE_SCORE == resultType) {
					// 考核
					RowDefinition rowDefinition = ccStudentService.getSingleScoreDefinition(ccTeacherCourse, courseGradeComposeId, startNaturalColumnIndex);

					ExcelExporter.exportToExcel(2, rowDefinition, new ExcelExporter.ExcelExporterDataProcessor() {
						@Override
						public List<List<String>> invoke() {
							List<List<String>> result = new ArrayList<>();

							CcEduclass ccEduclass = CcEduclass.dao.findFilteredById(eduClassId);
							String educlassName = ccEduclass.getStr("educlass_name");

							List<CcStudent> studentList = CcStudent.dao.findByEduclassIdOrderByStudentNo(eduClassId);

							for(int i = 0; i < studentList.size();) {
								CcStudent temp = studentList.get(i);
								List<String> data1 = new ArrayList<>();
								i++;
								temp.put("index", i);
								data1.add(i+"");
								data1.add(temp.getStr("student_no"));
								data1.add(temp.getStr("name"));
								data1.add(educlassName);

								result.add(data1);
							}

							return result;
						}
					}, exportUrl);
					//}, "/Users/xuqiang/Work/temp/output2.xls");
				} else {
					// 评分表
					RowDefinition rowDefinition = ccStudentService.getEvaluateDefinition(ccTeacherCourse, startNaturalColumnIndex);

					ExcelExporter.exportToExcel(3, rowDefinition, new ExcelExporter.ExcelExporterDataProcessor() {
						@Override
						public List<List<String>> invoke() {
							List<List<String>> result = new ArrayList<>();

							CcEduclass ccEduclass = CcEduclass.dao.findFilteredById(eduClassId);
							String educlassName = ccEduclass.getStr("educlass_name");

							List<CcStudent> studentList = CcStudent.dao.findByEduclassIdOrderByStudentNo(eduClassId);

							for(int i = 0; i < studentList.size();) {
								CcStudent temp = studentList.get(i);
								List<String> data1 = new ArrayList<>();
								i++;
								temp.put("index", i);
								data1.add(i+"");
								data1.add(temp.getStr("student_no"));
								data1.add(temp.getStr("name"));
								data1.add(educlassName);

								result.add(data1);
							}
							return result;
						}
					}, exportUrl);
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (logger.isErrorEnabled()) {
					logger.error("生成成绩录入模版失败", e);
				}
				continue;
			}
			fileList.add(new File(exportUrl));
		}

		if (fileList.isEmpty()) {
			return renderFAIL("", response, header);
		}

		if (fileList.size() == 1) {
			return renderFILE(fileList.get(0), response, header);
		}

		//压缩文件
		File zipFile = new File(zipFileUrl);

		if (!FileUtils.zipFiles(fileList, zipFile)) {
			return renderFAIL("", response, header);
		}

		return renderFILE(zipFile, response, header);
	}
	
}