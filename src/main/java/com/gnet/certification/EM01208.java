package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcMajor;
import com.gnet.model.admin.CcTeacherFurtherEducation;
import com.gnet.plugin.configLoader.ConfigUtils;
import com.gnet.utils.DateUtil;
import com.gnet.utils.DateUtils;
import com.google.common.collect.Lists;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导出教师经历
 * 
 * @author GJM
 * @Date 2020年8月21日14:09:05
 */
@Service("EM01208")
public class EM01208 extends BaseApi implements IApi {
	private static final Logger logger = Logger.getLogger(EM01208.class);
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Integer year=paramsIntegerFilter(params.get("year"));
		Long majorId=paramsLongFilter(params.get("majorId"));
		if (majorId == null){
			return renderFAIL("0130", response, header);
		}
		Date date = new Date();
		Integer endDate;
		Integer startDate;
		Integer type=null;
		//0是导出全部
		if (year==0){
			endDate=null;
			startDate=null;
		}else {
			 endDate=DateUtil.getYear(date);
			 startDate=endDate-year;
		}

		//开始--------创建excel
		logger.info("开始--------创建excel--------------");
		// 初始一个workbook
		HSSFWorkbook workbook = new HSSFWorkbook();
		//各种格式
		Map<String, HSSFCellStyle> styles = createStyle(workbook);
		//创建sheet
		HSSFSheet sheet = workbook.createSheet();
		//创建第一行
		HSSFRow r00 = sheet.createRow(0);
		//合并单元格起始行号，终止行号， 起始列号，终止列号
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
		HSSFCell cell = r00.createCell(0);
		if (startDate==null){
			cell.setCellValue("教师进修培训情况表");
		}else {
			cell.setCellValue("近"+year+"年教师进修培训情况表("+startDate+"-"+endDate+")");
		}

		cell.setCellStyle(styles.get("header"));

		//创建第一行的单元格 标题行
		HSSFRow r0 = sheet.createRow(1);
		//冻结前两行
		sheet.createFreezePane( 0, 2, 0, 2 );
		logger.info("---------------标题行--------------");
		HSSFCell c1 = r0.createCell(0);
		HSSFCell c2 = r0.createCell(1);
		HSSFCell c3 = r0.createCell(2);
		HSSFCell c4 = r0.createCell(3);
		HSSFCell c5 = r0.createCell(4);
		HSSFCell c6 = r0.createCell(5);
		HSSFCell c7 = r0.createCell(6);
		c1.setCellValue("序号");
		c1.setCellStyle(styles.get("header"));

		c2.setCellValue("姓名");
		c2.setCellStyle(styles.get("header"));
		c3.setCellValue("项目名称（出国、下企业、短期培训、学历学位）");
		c3.setCellStyle(styles.get("header"));
		c4.setCellValue("时间");
		c4.setCellStyle(styles.get("header"));
		c5.setCellValue("对方单位");
		c5.setCellStyle(styles.get("header"));
		c6.setCellValue("主要事项介绍");
		c6.setCellStyle(styles.get("header"));
		c7.setCellValue("备注");
		c7.setCellStyle(styles.get("header"));
		for (int i=0;i<7;i++){
			setCellWidth(sheet, null, i);
		}
		logger.info("-----------------开始添加数据----------------");
		//获取教师经历列表
		List<CcTeacherFurtherEducation> ccTeacherFurtherEducationList = CcTeacherFurtherEducation.dao.finaAllTeacherFurther(startDate, endDate, type, majorId);
		//每个老师需要合并的行数
		Long oldteacherId=0l;
		//是否已经设置
		String is_set="";
		List<CcTeacherFurtherEducation> teacherMargeNum = CcTeacherFurtherEducation.dao.finaAllTeacherFurtherNum(startDate, endDate, type, majorId);
		for (int i=0;i<ccTeacherFurtherEducationList.size();i++){
			CcTeacherFurtherEducation ccTeacherFurtherEducation = ccTeacherFurtherEducationList.get(i);
			Long teacherId = ccTeacherFurtherEducation.getLong("teacher_id");
			Integer margeNums=1;
			for (CcTeacherFurtherEducation temp:teacherMargeNum ){
				Long teacher_id = temp.getLong("teacher_id");
				Integer mageNum = Integer.parseInt(temp.get("mageNum").toString());
				if (teacherId.equals(teacher_id)){
					margeNums=mageNum;
					continue;
				}
			}
			String teacherName = ccTeacherFurtherEducation.getStr("teacher_name");
			Integer educationType = ccTeacherFurtherEducation.getInt("education_type");
			//起止时间
			String time = ccTeacherFurtherEducation.getStr("start_time") + "-" + ccTeacherFurtherEducation.getStr("end_time");
			//地点
			String site = ccTeacherFurtherEducation.getStr("site");
			String content = ccTeacherFurtherEducation.getStr("content");
			String remark = ccTeacherFurtherEducation.getStr("remark");
			String educationTypeName="";
			if (educationType==1){
				educationTypeName="国内进修";
			}else if (educationType==2){
				educationTypeName="国外进修";
			}
			//创建数据行
			HSSFRow row = sheet.createRow(i + 2);
			HSSFCell cell1 = row.createCell(0);
			HSSFCell cell2 = row.createCell(1);
			HSSFCell cell3 = row.createCell(2);
			HSSFCell cell4 = row.createCell(3);
			HSSFCell cell5 = row.createCell(4);
			HSSFCell cell6 = row.createCell(5);
			HSSFCell cell7 = row.createCell(6);
			//格式
			cell1.setCellStyle(styles.get("cell"));
			cell2.setCellStyle(styles.get("cell"));
			cell3.setCellStyle(styles.get("cell"));
			cell4.setCellStyle(styles.get("cell"));
			cell5.setCellStyle(styles.get("cell"));
			cell6.setCellStyle(styles.get("cell"));
			cell7.setCellStyle(styles.get("cell"));
			cell1.setCellValue(i+1);
			if (oldteacherId.equals(teacherId) && margeNums>1 && is_set.equals(teacherId+"0")){
				//合并单元格起始行号，终止行号， 起始列号，终止列号
				sheet.addMergedRegion(new CellRangeAddress(i+1, i+margeNums, 1, 1));
				//是否设置合并标志
				is_set=teacherId+"1";
			}else if (!oldteacherId.equals(teacherId)){
				cell2.setCellValue(teacherName);
				is_set=teacherId+"0";
			}

			cell3.setCellValue(educationTypeName);
			cell4.setCellValue(time);
			cell5.setCellValue(site);
			cell6.setCellValue(content);
			cell7.setCellValue(remark);

			oldteacherId=teacherId;
		}
		String exportUrl;
		if (year==0){
			exportUrl =  "所有教师进修培训记录表.xls";
		}else {
			exportUrl =  "近"+year+"年教师进修培训记录表.xls";
		}

		downFile(exportUrl,workbook);
		File file = new File(exportUrl);
		//fileList.add(new File(exportUrl));


		// 结果返回
		return renderFILE(file, response, header);
	}


	private static Map<String, HSSFCellStyle> createStyle(HSSFWorkbook workBook) {
		//表头样式
		HSSFCellStyle titleStyle = workBook.createCellStyle();
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont titleFont = workBook.createFont();
		titleFont.setFontHeightInPoints((short) 14);
		titleFont.setBoldweight((short) 700);
		titleFont.setColor(HSSFColor.RED.index);
		titleStyle.setFont(titleFont);
		// 列头样式
		HSSFCellStyle headerStyle = workBook.createCellStyle();
		headerStyle.setWrapText(true);//先设置为自动换行
		headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle.setFillBackgroundColor(HSSFColor.WHITE.index);
		HSSFFont headerFont = workBook.createFont();
		headerFont.setFontHeightInPoints((short) 10);
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerStyle.setFont(headerFont);

		//将单元格格式设为文本
		HSSFCellStyle cellsStyle = workBook.createCellStyle();
		DataFormat format = workBook.createDataFormat();
		cellsStyle.setDataFormat(format.getFormat("@"));

		// 单元格样式
		HSSFCellStyle cellStyle = workBook.createCellStyle();
		cellStyle.setWrapText(true);//先设置为自动换行
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setFillBackgroundColor(HSSFColor.WHITE.index);
		HSSFFont cellFont = workBook.createFont();
		cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		cellStyle.setFont(cellFont);



		Map<String, HSSFCellStyle> styles = new HashMap<>();
		styles.put("title", titleStyle);
		styles.put("header", headerStyle);
		styles.put("cell", cellStyle);
		styles.put("celltext",cellsStyle);
		return styles;
	}

	private static void setCellWidth(HSSFSheet sheet, Cell cell, int cellIndex) {
		float prevWidth = sheet.getColumnWidth(cellIndex);
		//  从自适应长度变成固定10个中文字
		float newWidth = 10 * 512;
//        float newWidth = cell.getStringCellValue().getBytes().length * 256;
		Float resultWidth = prevWidth < newWidth ? newWidth : prevWidth;
		sheet.setColumnWidth(cellIndex, resultWidth.intValue());
	}

	public static void downFile(String url, Workbook workbook){

		//文档输出
		try {
			FileOutputStream out = new FileOutputStream(url);
			workbook.write(out);
			out.close();
		}catch (IOException e) {
			e.printStackTrace();
		}

	}


}
