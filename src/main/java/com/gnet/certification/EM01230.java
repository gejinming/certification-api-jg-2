package com.gnet.certification;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcEduclass;
import com.gnet.model.admin.CcGradecompose;
import com.gnet.model.admin.CcIndicationCourse;
import com.gnet.model.admin.CcMajor;
import com.gnet.pager.Pageable;
import com.gnet.plugin.configLoader.ConfigUtils;
import com.gnet.utils.DateUtil;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Page;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 课程目标达成度导出
 *
 * @author GJM
 * @Date 2020年10月19日
 */
@Transactional(readOnly = false)
@Service("EM01230")
public class EM01230 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {

		Map<String, Object> param = request.getData();
		Long educlassId = paramsLongFilter(param.get("eduClassId"));
		Long majorId = paramsLongFilter(param.get("majorId"));
		if (educlassId==null){
			return renderFAIL("0500", response, header);
		}
		if (majorId==null){
			return renderFAIL("0296", response, header);
		}
		//课程的信息
		CcEduclass courseInfo = CcEduclass.dao.findEduclassCourse(educlassId);
		if (courseInfo==null){
			return renderFAIL("0251", response, header);
		}
		//课程的基本信息
		CcMajor majorInfo = CcMajor.dao.findById(majorId);
		StringBuilder courseInfos = new StringBuilder();
		courseInfos.append("         课程名称：  "+courseInfo.getStr("courseName"));
		courseInfos.append("       课程代码："+courseInfo.getStr("code"));
		courseInfos.append("     学时：  "+courseInfo.get("all_hours"));
		courseInfos.append("      学分： "+courseInfo.get("credit")+"\n");
		courseInfos.append("专业班级： "+courseInfo.get("educlass_name"));
		courseInfos.append("        学生数："+courseInfo.get("studentNum"));
		courseInfos.append("       开课学期："+courseInfo.get("start_year")+"-"+courseInfo.get("end_year")+"-"+courseInfo.get("term")+"\n");
		courseInfos.append("任课教师：  "+courseInfo.get("teacherName"));
		courseInfos.append("        学院："+majorInfo.get("instituteName"));
		Date date = new Date();
		courseInfos.append("      评价日期："+DateUtil.getYear(date)+"年"+DateUtil.getMonth(date)+"月"+DateUtil.getDay(date)+"日");
		Long teacherCourseId = courseInfo.getLong("teacher_course_id");
		Long planId = courseInfo.getLong("plan_id");
		Long courseId = courseInfo.getLong("courseId");
		//成绩组成列表
		List<CcGradecompose> gradecomposeList = CcGradecompose.dao.findGradecomposeByTeacherCourseId(teacherCourseId);
		//目前定义了5中模板针对5种成绩组成，如果一个教学班多于5个成绩组成，多的会没有写入
		String oldRowOne="";
		String oldRowTwo="";
		String oldRowTree="";
		String oldRowFour="";
		String oldRowFree="";
		for (int i=0;i<gradecomposeList.size();i++){
			CcGradecompose ccGradecompose = gradecomposeList.get(i);
			if (i==0){
				oldRowOne=ccGradecompose.getStr("name");
			}
			if (i==1){
				oldRowTwo=ccGradecompose.getStr("name");
			}
			if (i==2){
				oldRowTree=ccGradecompose.getStr("name");
			}
			if (i==3){
				oldRowFour=ccGradecompose.getStr("name");
			}
			if (i==4){
				oldRowFree=ccGradecompose.getStr("name");
			}
		}
		String path="";
		if (gradecomposeList.size()==1){
			path=PathKit.getRootClassPath() + File.separator + "excel" + File.separator + "课程目标达成度1.xlsx";
		}
		if (gradecomposeList.size()==2){
			path=PathKit.getRootClassPath() + File.separator + "excel" + File.separator + "课程目标达成度2.xlsx";
		}
		if (gradecomposeList.size()==3){
			path=PathKit.getRootClassPath() + File.separator + "excel" + File.separator + "课程目标达成度3.xlsx";
		}
		if (gradecomposeList.size()==4){
			path=PathKit.getRootClassPath() + File.separator + "excel" + File.separator + "课程目标达成度4.xlsx";
		}
		if (gradecomposeList.size()==5){
			path=PathKit.getRootClassPath() + File.separator + "excel" + File.separator + "课程目标达成度5.xlsx";
		}


		XSSFWorkbook wb=null;
		XSSFSheet sheet=null;
		try {
			File fi=new File(path);
			InputStream in = new FileInputStream(fi);
			wb = new XSSFWorkbook(in);
			//Workbook wb = WorkbookFactory.create(new File(path));
			sheet = wb.getSheetAt(0);
			Row row = sheet.getRow(1);
			Cell cell = row.getCell(0);
			cell.setCellValue(courseInfos.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}



		//指标点列表
		Pageable pageable = new Pageable(null, null);
		Page<CcIndicationCourse> ccIndicationCoursePage = CcIndicationCourse.dao.page(pageable, null, courseId, null, null, planId, null, true, null,null);
		List<CcIndicationCourse> ccIndicationCourseList = ccIndicationCoursePage.getList();
		//创建第几行数 从第四行开始填数据
		int rowNum=3;
		// 创建样式
		Map<String, XSSFCellStyle> styles = createStyle(wb);
		for (CcIndicationCourse ci:ccIndicationCourseList){
			Long indicatorPointId = ci.getLong("indication_id");
			//数据信息
			IApi eM00552 = SpringContextHolder.getBean("EM00552");
			HashMap<Object, Object> map = new HashMap<>();
			map.put("eduClassId",educlassId);
			map.put("indicatorPointId",indicatorPointId);
			request.setData(map);
			Response eM00552Result = eM00552.excute(request, response, header, method);
			Character successFlag = header.getSuccflag();
			if (Response.FAIL.equals(successFlag)) {
				return renderFAIL(header.getErrorcode(), response, header);
			}

			Object eM00552ResultData = eM00552Result.getData();
			if (null == eM00552ResultData) {
				return renderFAIL("0878", response, header);
			}
			Map<String, Object> filtered = JSON.parseObject(JSON.toJSONString(eM00552ResultData));
			Object indicatorPointInfo = filtered.get("indicatorPointInfo");
			Map<String, Object> indicatorPointInfos = JSON.parseObject(JSON.toJSONString(indicatorPointInfo));

			Object indicatorPoints = indicatorPointInfos.get(indicatorPointId.toString());
			JSONObject indicatorPointJson = JSON.parseObject(JSON.toJSONString(indicatorPoints));
			//毕业指标点序号
			String graduteNum=indicatorPointJson.get("graduateIndexNum")+"."+indicatorPointJson.get("indicatorPointIndexNum");
			//指标点达成度
			String indicatorPointValue=indicatorPointJson.get("indicatorPointValue")+"";
			if (indicatorPointValue==null){
				indicatorPointValue="0";
			}
			//当前毕业指标点包含的课程目标
			JSONArray indicationInfoList = indicatorPointJson.getJSONArray("indicationInfo");

			for (int i=0; i<indicationInfoList.size();i++){
				JSONObject infoListJSONObject = indicationInfoList.getJSONObject(i);
				//课程目标序号
				String indicationNum="CO"+infoListJSONObject.get("indicationIndexNum");
				//课程目标达成度
				String indicationAchieveValue = infoListJSONObject.get("indicationAchieveValue")+"";
				//课程目标期望值
				String indicationExpectedValue = infoListJSONObject.get("indicationExpectedValue")+"";
				//课程目标里的成绩组成信息
				JSONArray gradecomposeListJson = infoListJSONObject.getJSONArray("gradecomposeList");
				//EXCEL是从第四行开始的写入的
				Row row = sheet.createRow(rowNum);
				//序号
				setCellValue(row,0,rowNum-2+"",wb);
				rowNum=rowNum+1;
				for (int j=0; j<gradecomposeListJson.size();j++){
					//表头行
					XSSFRow row1 = sheet.getRow(2);
					for (int a=0;a<row1.getLastCellNum();a++){
						String cellValue = row1.getCell(a).getStringCellValue();

						if (cellValue.equals("毕业要求指标点")){
							setCellValue(row,a,graduteNum,wb);
						}
						if (cellValue.equals("课程目标")){
							setCellValue(row,a,indicationNum,wb);
						}
						if (cellValue.equals("课程目标达成度期望值Ei")){
							setCellValue(row,a,indicationExpectedValue,wb);
						}
						if (cellValue.equals("课程目标达成度ALCOi")){
							setCellValue(row,a,indicationAchieveValue,wb);
						}
						if (cellValue.equals("某一指标点对应各课程目标达成度的最小值(ALCOmin)m")){
							setCellValue(row,a,indicatorPointValue,wb);
						}

					}

					JSONObject gradecomposeMap = gradecomposeListJson.getJSONObject(j);
					//成绩组成名称
					String gradecomposeName = gradecomposeMap.getString("gradecomposeName");
					//平均分
					String gradecomposeAverage = gradecomposeMap.getString("gradecomposeAverage");
					//权重
					String gradecomposeWeight = gradecomposeMap.getString("gradecomposeWeight");
					//满分也就是总分
					String maxScore = gradecomposeMap.getString("maxScore");

					//每个成绩组成对应列
					if (oldRowOne.equals(gradecomposeName)){
						//第四、五、六分别是成绩组成1的总分、平均分、权重
						setCellValue(row,3,maxScore,wb);
						setCellValue(row,4,gradecomposeAverage,wb);
						setCellValue(row,5,gradecomposeWeight,wb);
					}


					if (oldRowTwo.equals(gradecomposeName)) {
						setCellValue(row,6,maxScore,wb);
						setCellValue(row,7,gradecomposeAverage,wb);
						setCellValue(row,8,gradecomposeWeight,wb);
					}


					if (oldRowTree.equals(gradecomposeName)) {
						setCellValue(row, 9, maxScore,wb);
						setCellValue(row, 10, gradecomposeAverage,wb);
						setCellValue(row, 11, gradecomposeWeight,wb);
					}
					if (oldRowFour.equals(gradecomposeName)) {
						setCellValue(row, 12, maxScore,wb);
						setCellValue(row, 13, gradecomposeAverage,wb);
						setCellValue(row, 14, gradecomposeWeight,wb);
					}
					if (oldRowFree.equals(gradecomposeName)) {
						setCellValue(row, 15, maxScore,wb);
						setCellValue(row, 16, gradecomposeAverage,wb);
						setCellValue(row, 17, gradecomposeWeight,wb);
					}

				}
				System.out.println("ss");

			}

		}
		String fileUrl = PathKit.getWebRootPath() + ConfigUtils.getStr("excel", "createPath");
		// 判断是否存在路径，不存在就创建
		File dir = new File(fileUrl);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		//输出Excel
		String exportUrl = PathKit.getWebRootPath() + ConfigUtils.getStr("excel", "createPath") + "课程目标和毕业要求指标点达成度.xlsx";
		try {
			FileOutputStream out = new FileOutputStream(exportUrl);
			wb.write(out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(PathKit.getWebRootPath() + System.currentTimeMillis());
		return renderFILE(new File(exportUrl), response, header);
	}

	public void setCellValue(Row row,int line,String values,XSSFWorkbook workBook){
		Cell cell = row.createCell(line);
		cell.setCellStyle(createStyle(workBook).get("celltext"));
		cell.setCellValue(values);
	}

	private static Map<String, XSSFCellStyle> createStyle(XSSFWorkbook workBook) {
		//表头样式
		XSSFCellStyle cellsStyle = workBook.createCellStyle();
		cellsStyle.setAlignment(HorizontalAlignment.CENTER);
		cellsStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		Map<String, XSSFCellStyle> styles = new HashMap<>();
		styles.put("celltext",cellsStyle);
		return styles;
	}
}