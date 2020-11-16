package com.gnet.plugin.poi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.model.admin.CcStudent;

/** 
 * 导出Excle工具
* @author SY 
* @version 创建时间：2017年10月8日 上午9:57:53 
* 类说明 
*/
public class ExcelUtil {
		/** 
		* @author SY 
		* @version 创建时间：2017年10月8日 上午9:57:53 
		* 类说明 
		*/
	
	    public static String NO_DEFINE = "no_define";//未定义的字段
	    public static String DEFAULT_DATE_PATTERN="yyyy年MM月dd日";//默认日期格式
	    public static int DEFAULT_COLOUMN_WIDTH = 17;
	    
	    /**
	     * 导出Excel 97(.xls)格式 ，少量数据
	     * @param title 标题行 
	     * @param headMap 属性-列名
	     * @param jsonArray 数据集
	     * @param datePattern 日期格式，null则用默认日期格式
	     * @param colWidth 列宽 默认 至少17个字节
	     * @param out 输出流
	     */
	    public static void exportExcel(String title,Map<String, String> headMap,JSONArray jsonArray,String datePattern,int colWidth, OutputStream out) {
	        if(datePattern==null) datePattern = DEFAULT_DATE_PATTERN;
	        // 声明一个工作薄
	        HSSFWorkbook workbook = new HSSFWorkbook();
	        workbook.createInformationProperties();
	        workbook.getDocumentSummaryInformation().setCompany("*****公司");
	        SummaryInformation si = workbook.getSummaryInformation();
	        si.setAuthor("JACK");  //填加xls文件作者信息
	        si.setApplicationName("导出程序"); //填加xls文件创建程序信息
	        si.setLastAuthor("最后保存者信息"); //填加xls文件最后保存者信息
	        si.setComments("JACK is a programmer!"); //填加xls文件作者信息
	        si.setTitle("POI导出Excel"); //填加xls文件标题信息
	        si.setSubject("POI导出Excel");//填加文件主题信息
	        si.setCreateDateTime(new Date());
	         //表头样式
	        HSSFCellStyle titleStyle = workbook.createCellStyle();
	        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        HSSFFont titleFont = workbook.createFont();
	        titleFont.setFontHeightInPoints((short) 20);
	        titleFont.setBoldweight((short) 700);
	        titleStyle.setFont(titleFont);
	        // 列头样式
	        HSSFCellStyle headerStyle = workbook.createCellStyle();
	        headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	        headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	        headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
	        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        HSSFFont headerFont = workbook.createFont();
	        headerFont.setFontHeightInPoints((short) 12);
	        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        headerStyle.setFont(headerFont);
	        // 单元格样式
	        HSSFCellStyle cellStyle = workbook.createCellStyle();
	        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
	        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	        HSSFFont cellFont = workbook.createFont();
	        cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
	        cellStyle.setFont(cellFont);
	        // 生成一个(带标题)表格
	        HSSFSheet sheet = workbook.createSheet();
	        // 声明一个画图的顶级管理器
	        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
	        // 定义注释的大小和位置,详见文档
	        HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
	                0, 0, 0, (short) 4, 2, (short) 6, 5));
	        // 设置注释内容
	        comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
	        // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
	        comment.setAuthor("JACK");
	        //设置列宽
	        int minBytes = colWidth<DEFAULT_COLOUMN_WIDTH?DEFAULT_COLOUMN_WIDTH:colWidth;//至少字节数
	        int[] arrColWidth = new int[headMap.size()];
	        // 产生表格标题行,以及设置列宽
	        String[] properties = new String[headMap.size()];
	        String[] headers = new String[headMap.size()];
	        int ii = 0;
	        for (Iterator<String> iter = headMap.keySet().iterator(); iter
	                .hasNext();) {
	            String fieldName = iter.next();

	            properties[ii] = fieldName;
	            headers[ii] = fieldName;

	            int bytes = fieldName.getBytes().length;
	            arrColWidth[ii] =  bytes < minBytes ? minBytes : bytes;
	            sheet.setColumnWidth(ii,arrColWidth[ii]*256);
	            ii++;
	        }
	        // 遍历集合数据，产生数据行
	        int rowIndex = 0;
	        for (Object obj : jsonArray) {
	            if(rowIndex == 65535 || rowIndex == 0){
	                if ( rowIndex != 0 ) sheet = workbook.createSheet();//如果数据超过了，则在第二页显示

	                HSSFRow titleRow = sheet.createRow(0);//表头 rowIndex=0
	                titleRow.createCell(0).setCellValue(title);
	                titleRow.getCell(0).setCellStyle(titleStyle);
	                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headMap.size() - 1));

	                HSSFRow headerRow = sheet.createRow(1); //列头 rowIndex =1
	                for(int i=0;i<headers.length;i++)
	                {
	                    headerRow.createCell(i).setCellValue(headers[i]);
	                    headerRow.getCell(i).setCellStyle(headerStyle);

	                }
	                rowIndex = 2;//数据内容从 rowIndex=2开始
	            }
	            JSONObject jo = (JSONObject) JSONObject.toJSON(obj);
	            HSSFRow dataRow = sheet.createRow(rowIndex);
	            for (int i = 0; i < properties.length; i++)
	            {
	                HSSFCell newCell = dataRow.createCell(i);

	                Object o =  jo.get(properties[i]);
	                String cellValue = ""; 
	                if(o==null) cellValue = "";
	                else if(o instanceof Date) cellValue = new SimpleDateFormat(datePattern).format(o);
	                else cellValue = o.toString();

	                newCell.setCellValue(cellValue);
	                newCell.setCellStyle(cellStyle);
	            }
	            rowIndex++;
	        }
	        // 自动调整宽度
	        /*for (int i = 0; i < headers.length; i++) {
	            sheet.autoSizeColumn(i);
	        }*/
	        try {
	            workbook.write(out);
	            workbook.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    /**
	     * 导出Excel 97(.xls)格式 ，少量数据
	     * @param title 标题行 
	     * @param headMap 属性-列名
	     * @param createHeadMap 属性-实时生成的额外列头
	     * 				Map<第几行，Map<第几列,Map<当前成绩组成细啊有几个考评点/考评点名称数组, 对应的数值>>>
	     * @param studentList 数据集
	     * @param datePattern 日期格式，null则用默认日期格式
	     * @param colWidth 列宽 默认 至少17个字节
	     * @param out 输出流
	     */
	    public static void exportExcel(String title,Map<String, String> headMap, Map<Integer, Map<Integer, Map<String, Object>>> createHeadMap, List<CcStudent> studentList,String datePattern,int colWidth, OutputStream out) {
	    	if(datePattern==null) datePattern = DEFAULT_DATE_PATTERN;
	    	// 声明一个工作薄
	    	HSSFWorkbook workbook = new HSSFWorkbook();
	    	workbook.createInformationProperties();
	    	workbook.getDocumentSummaryInformation().setCompany("*****公司");
	    	SummaryInformation si = workbook.getSummaryInformation();
	    	si.setAuthor("SY");  //填加xls文件作者信息
	    	si.setApplicationName("导出程序"); //填加xls文件创建程序信息
	    	si.setLastAuthor("最后保存者信息"); //填加xls文件最后保存者信息
	    	si.setComments("SY is a programmer!"); //填加xls文件作者信息
	    	si.setTitle("POI导出Excel"); //填加xls文件标题信息
	    	si.setSubject("POI导出Excel");//填加文件主题信息
	    	si.setCreateDateTime(new Date());
	    	//表头样式
	    	HSSFCellStyle titleStyle = workbook.createCellStyle();
	    	titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    	HSSFFont titleFont = workbook.createFont();
	    	titleFont.setFontHeightInPoints((short) 14);
	    	titleFont.setBoldweight((short) 700);
	    	titleFont.setColor(HSSFColor.RED.index);
	    	titleStyle.setFont(titleFont);
	    	// 列头样式
	    	HSSFCellStyle headerStyle = workbook.createCellStyle();
	    	headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	    	headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    	headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    	headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    	headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
	    	headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    	HSSFFont headerFont = workbook.createFont();
	    	headerFont.setFontHeightInPoints((short) 12);
	    	headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    	headerStyle.setFont(headerFont);
	    	// 单元格样式
	    	HSSFCellStyle cellStyle = workbook.createCellStyle();
	    	cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	    	cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    	cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    	cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    	cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
	    	cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    	cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	    	HSSFFont cellFont = workbook.createFont();
	    	cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
	    	cellStyle.setFont(cellFont);
	    	// 生成一个(带标题)表格
	    	HSSFSheet sheet = workbook.createSheet();
	    	// 声明一个画图的顶级管理器
	    	HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
	    	// 定义注释的大小和位置,详见文档
//	    	HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,0, 0, 0, (short) 4, 2, (short) 6, 5));
	    	// 设置注释内容
//	    	comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
	    	// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
//	    	comment.setAuthor("JACK");
	    	//设置列宽
	    	int minBytes = colWidth<DEFAULT_COLOUMN_WIDTH?DEFAULT_COLOUMN_WIDTH:colWidth;//至少字节数
	    	int[] arrColWidth = new int[headMap.size()];
	    	
	    	// 实时生成的列头最后一个格子的位置
	    	Integer createHeadLastCellColumnNum = 0;
	    	// 头，占了几行
	    	Integer createHeadRowNum = createHeadMap.get(3) == null ? 2 : 3;
	    	if(createHeadMap != null) {
	    		Map<Integer, Map<String, Object>> rowMap = createHeadMap.get(1);
	    		if(rowMap != null) {
	    			Integer num = 0;
	    			for(Entry<Integer, Map<String, Object>> entry : rowMap.entrySet()) {
	    				Integer column = entry.getKey();
	    				if(num < column) {
	    					Map<String, Object> columnMap = entry.getValue();
	    					Integer columnNum = (Integer) columnMap.get("number");
	    					createHeadLastCellColumnNum = column + columnNum - 1;
	    				}
	    			}
	    		}
	    	}
	    	
	    	// 产生表格标题行,以及设置列宽
	    	Integer oldHeadLong = headMap.size();
	    	String[] properties = new String[createHeadLastCellColumnNum];
	    	String[] headers = new String[createHeadLastCellColumnNum];
	    	int ii = 0;
	    	for (Iterator<String> iter = headMap.keySet().iterator(); iter.hasNext();) {
	    		String fieldName = iter.next();
	    		
	    		properties[ii] = fieldName;
	    		headers[ii] = headMap.get(fieldName);
	    		
	    		int bytes = fieldName.getBytes().length;
	    		arrColWidth[ii] =  bytes < minBytes ? minBytes : bytes;
	    		sheet.setColumnWidth(ii,arrColWidth[ii]*256);
	    		ii++;
	    	}
	    	
	    	// 设置实时生成数据的长度
	    	Map<Integer, Map<String, Object>> rowMap = createHeadMap.get(createHeadRowNum);
    		if(rowMap != null) {
    			int bytes = 0;
    			for(Entry<Integer, Map<String, Object>> entry : rowMap.entrySet()) {
    				Integer column = entry.getKey();
    				if(bytes == 0) {
    					Map<String, Object> columnMap = entry.getValue();
    					String[] values = (String[]) columnMap.get("value");
    					for(String value : values) {
    						int valueLong = value.getBytes().length;
    						bytes = bytes < valueLong ? valueLong : bytes;
    					}
    				}
    				sheet.setColumnWidth(column,(bytes < minBytes ? minBytes : bytes)*256);
    			}
    		}
    		
	    	// 遍历集合数据，产生数据行
	    	int rowIndex = 0;
	    	for (CcStudent student : studentList) {
	    		if(rowIndex == 65535 || rowIndex == 0){
	    			if ( rowIndex != 0 ) 
	    				sheet = workbook.createSheet();//如果数据超过了，则在第二页显示
	    			
	    			HSSFRow titleRow = sheet.createRow(0);//表头 rowIndex=0
	    			// TODO SY 不知道这里是否要创建 INDEX = 1 和 2的行，还是用 0的即可
	    			titleRow.createCell(0).setCellValue(title);
	    			titleRow.getCell(0).setCellStyle(titleStyle);
	    			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length - 1));
	    			// 设置一个个合并单元格，区分row长度是2和3的
	    			for(int z = 0; z < 4; z++) {
	    				sheet.addMergedRegion(new CellRangeAddress(1, createHeadRowNum, z, z));
	    			}
	    			
	    			HSSFRow headerRowOne = sheet.createRow(1); //列头 rowIndex = 1
	    			HSSFRow headerRowTow = sheet.createRow(2); //列头 rowIndex = 2
	    			HSSFRow headerRowThree = sheet.createRow(3); //列头 rowIndex = 3
	    			for(int i=0;i<headers.length;i++) {
//    				for(int i=0;i<oldHeadLong;i++) {
	    				if(i < 4) {
	    					headerRowOne.createCell(i).setCellValue(headers[i]);
	    					headerRowTow.createCell(i).setCellValue(headers[i]);
	    					headerRowOne.getCell(i).setCellStyle(headerStyle);
		    				headerRowTow.getCell(i).setCellStyle(headerStyle);
	    					if(createHeadRowNum == 3) {
		    					headerRowThree.createCell(i).setCellValue(headers[i]);
		    					headerRowThree.getCell(i).setCellStyle(headerStyle);
		    				}
	    				} else {
	    					headerRowOne.createCell(i).setCellStyle(headerStyle);
		    				headerRowTow.createCell(i).setCellStyle(headerStyle);
		    				if(createHeadRowNum == 3) {
		    					headerRowThree.createCell(i).setCellStyle(headerStyle);
		    				}
	    				}
	    			}
	    			
    				// TODO SY 弄成下拉 new
	    			setPullDown(sheet, createHeadMap);
	    			rowIndex = createHeadRowNum + 1;//数据内容从 rowIndex=createHeadRowNum开始
	    		}
//	    		JSONObject jo = (JSONObject) JSONObject.toJSON(student);
	    		HSSFRow dataRow = sheet.createRow(rowIndex);
	    		for (int i = 0; i < properties.length; i++)
	    		{
	    			HSSFCell newCell = dataRow.createCell(i);
	    			
	    			String cellValue = "";
	    			if(student.get(properties[i]) != null) {
	    				cellValue = student.get(properties[i]).toString();
	    			}
	    			
	    			newCell.setCellValue(cellValue);
	    			newCell.setCellStyle(cellStyle);
	    		}
	    		rowIndex++;
	    	}
	    	// 自动调整宽度
	    	/*for (int i = 0; i < headers.length; i++) {
	            sheet.autoSizeColumn(i);
	        }*/
	    	try {
	    		workbook.write(out);
	    		workbook.close();
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	}
	    }
	    
	    /**
	     * 导出Excel 2007 OOXML (.xlsx)格式
	     * @param title 标题行
	     * @param headMap 属性-列头
	     * @param jsonArray 数据集
	     * @param datePattern 日期格式，传null值则默认 年月日
	     * @param colWidth 列宽 默认 至少17个字节
	     * @param out 输出流
	     */
	    public static void exportExcelX(String title,Map<String, String> headMap,JSONArray jsonArray,String datePattern,int colWidth, OutputStream out) {
	        if(datePattern==null) datePattern = DEFAULT_DATE_PATTERN;
	        // 声明一个工作薄
	        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);//缓存
	        workbook.setCompressTempFiles(true);
	         //表头样式
	        CellStyle titleStyle = workbook.createCellStyle();
	        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        Font titleFont = workbook.createFont();
	        titleFont.setFontHeightInPoints((short) 20);
	        titleFont.setBoldweight((short) 700);
	        titleStyle.setFont(titleFont);
	        // 列头样式
	        CellStyle headerStyle = workbook.createCellStyle();
	        headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	        headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	        headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
	        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        Font headerFont = workbook.createFont();
	        headerFont.setFontHeightInPoints((short) 12);
	        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        headerStyle.setFont(headerFont);
	        // 单元格样式
	        CellStyle cellStyle = workbook.createCellStyle();
	        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
	        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	        Font cellFont = workbook.createFont();
	        cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
	        cellStyle.setFont(cellFont);
	        // 生成一个(带标题)表格
	        SXSSFSheet sheet = workbook.createSheet();
	        //设置列宽
	        int minBytes = colWidth<DEFAULT_COLOUMN_WIDTH?DEFAULT_COLOUMN_WIDTH:colWidth;//至少字节数
	        int[] arrColWidth = new int[headMap.size()];
	        // 产生表格标题行,以及设置列宽
	        String[] properties = new String[headMap.size()];
	        String[] headers = new String[headMap.size()];
	        int ii = 0;
	        for (Iterator<String> iter = headMap.keySet().iterator(); iter
	                .hasNext();) {
	            String fieldName = iter.next();

	            properties[ii] = fieldName;
	            headers[ii] = headMap.get(fieldName);

	            int bytes = fieldName.getBytes().length;
	            arrColWidth[ii] =  bytes < minBytes ? minBytes : bytes;
	            sheet.setColumnWidth(ii,arrColWidth[ii]*256);
	            ii++;
	        }
	        // 遍历集合数据，产生数据行
	        int rowIndex = 0;
	        for (Object obj : jsonArray) {
	            if(rowIndex == 65535 || rowIndex == 0){
	                if ( rowIndex != 0 ) sheet = workbook.createSheet();//如果数据超过了，则在第二页显示

	                SXSSFRow titleRow = sheet.createRow(0);//表头 rowIndex=0
	                titleRow.createCell(0).setCellValue(title);
	                titleRow.getCell(0).setCellStyle(titleStyle);
	                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headMap.size() - 1));

	                SXSSFRow headerRow = sheet.createRow(1); //列头 rowIndex =1
	                for(int i=0;i<headers.length;i++)
	                {
	                    headerRow.createCell(i).setCellValue(headers[i]);
	                    headerRow.getCell(i).setCellStyle(headerStyle);

	                }
	                rowIndex = 2;//数据内容从 rowIndex=2开始
	            }
	            JSONObject jo = (JSONObject) JSONObject.toJSON(obj);
	            SXSSFRow dataRow = sheet.createRow(rowIndex);
	            for (int i = 0; i < properties.length; i++)
	            {
	                SXSSFCell newCell = dataRow.createCell(i);

	                Object o =  jo.get(properties[i]);
	                String cellValue = ""; 
	                if(o==null) cellValue = "";
	                else if(o instanceof Date) cellValue = new SimpleDateFormat(datePattern).format(o);
	                else if(o instanceof Float || o instanceof Double) 
	                    cellValue= new BigDecimal(o.toString()).setScale(2,BigDecimal.ROUND_HALF_UP).toString();
	                else cellValue = o.toString();

	                newCell.setCellValue(cellValue);
	                newCell.setCellStyle(cellStyle);
	            }
	            rowIndex++;
	        }
	        // 自动调整宽度
	        /*for (int i = 0; i < headers.length; i++) {
	            sheet.autoSizeColumn(i);
	        }*/
	        try {
	            workbook.write(out);
	            workbook.close();
	            workbook.dispose();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    /**
	     * 导出Excel 2007 OOXML (.xlsx)格式
	     * @param title 标题行
	     * @param headMap 属性-列头
	     * @param createHeadMap 属性-实时生成的额外列头
	     * 				Map<第几行，Map<第几列,Map<当前成绩组成细啊有几个考评点/考评点名称数组, 对应的数值>>>
	     * @param jsonArray 数据集
	     * @param datePattern 日期格式，传null值则默认 年月日
	     * @param colWidth 列宽 默认 至少17个字节
	     * @param out 输出流
	     */
	    public static void exportExcelX(String title,Map<String, String> headMap, Map<Integer, Map<Integer, Map<String, Object>>> createHeadMap,JSONArray jsonArray,String datePattern,int colWidth, OutputStream out) {
	    	if(datePattern==null) datePattern = DEFAULT_DATE_PATTERN;
	    	// 声明一个工作薄
	    	SXSSFWorkbook workbook = new SXSSFWorkbook(1000);//缓存
	    	workbook.setCompressTempFiles(true);
	    	//表头样式
	    	CellStyle titleStyle = workbook.createCellStyle();
	    	titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    	Font titleFont = workbook.createFont();
	    	titleFont.setFontHeightInPoints((short) 18);
	    	titleFont.setBoldweight((short) 700);
	    	titleStyle.setFont(titleFont);
	    	titleFont.setColor(HSSFColor.RED.index);
	    	// 列头样式
	    	CellStyle headerStyle = workbook.createCellStyle();
	    	headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	    	headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    	headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    	headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    	headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
	    	headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    	Font headerFont = workbook.createFont();
	    	headerFont.setFontHeightInPoints((short) 12);
	    	headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    	headerStyle.setFont(headerFont);
	    	// 单元格样式
	    	CellStyle cellStyle = workbook.createCellStyle();
	    	cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	    	cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    	cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    	cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    	cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
	    	cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    	cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	    	Font cellFont = workbook.createFont();
	    	cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
	    	cellStyle.setFont(cellFont);
	    	// 生成一个(带标题)表格
	    	SXSSFSheet sheet = workbook.createSheet();
	    	//设置列宽
	    	int minBytes = colWidth<DEFAULT_COLOUMN_WIDTH?DEFAULT_COLOUMN_WIDTH:colWidth;//至少字节数
	    	int[] arrColWidth = new int[headMap.size()];
	    	
	    	// 实时生成的列头最后一个格子的位置
	    	Integer createHeadLastCellColumnNum = 0;
	    	// 头，占了几行
	    	Integer createHeadRowNum = createHeadMap.get(3) == null ? 2 : 3;
	    	if(createHeadMap != null) {
	    		Map<Integer, Map<String, Object>> rowMap = createHeadMap.get(1);
	    		if(rowMap != null) {
	    			Integer num = 0;
	    			for(Entry<Integer, Map<String, Object>> entry : rowMap.entrySet()) {
	    				Integer column = entry.getKey();
	    				if(num < column) {
	    					Map<String, Object> columnMap = entry.getValue();
	    					Integer columnNum = (Integer) columnMap.get("number");
	    					createHeadLastCellColumnNum = column + columnNum - 1;
	    				}
	    			}
	    		}
	    	}
	    	
	    	// 产生表格标题行,以及设置列宽
	    	Integer oldHeadLong = headMap.size();
	    	String[] properties = new String[createHeadLastCellColumnNum];
	    	String[] headers = new String[createHeadLastCellColumnNum];
	    	int ii = 0;
	    	for (Iterator<String> iter = headMap.keySet().iterator(); iter.hasNext();) {
	    		String fieldName = iter.next();
	    		
	    		properties[ii] = fieldName;
	    		headers[ii] = headMap.get(fieldName);
	    		
	    		int bytes = fieldName.getBytes().length;
	    		arrColWidth[ii] =  bytes < minBytes ? minBytes : bytes;
	    		sheet.setColumnWidth(ii,arrColWidth[ii]*256);
	    		ii++;
	    	}
	    	// 设置实时生成数据的长度
	    	Map<Integer, Map<String, Object>> rowMap = createHeadMap.get(createHeadRowNum);
    		if(rowMap != null) {
    			int bytes = 0;
    			for(Entry<Integer, Map<String, Object>> entry : rowMap.entrySet()) {
    				Integer column = entry.getKey();
    				if(bytes == 0) {
    					Map<String, Object> columnMap = entry.getValue();
    					String[] values = (String[]) columnMap.get("value");
    					for(String value : values) {
    						int valueLong = value.getBytes().length;
    						bytes = bytes < valueLong ? valueLong : bytes;
    					}
    				}
    				sheet.setColumnWidth(column,(bytes < minBytes ? minBytes : bytes)*256);
    			}
    		}
    		
	    	// 遍历集合数据，产生数据行
	    	int rowIndex = 0;
	    	for (Object obj : jsonArray) {
	    		if(rowIndex == 65535 || rowIndex == 0){
	    			if ( rowIndex != 0 ) 
	    				sheet = workbook.createSheet();//如果数据超过了，则在第二页显示
	    			
	    			SXSSFRow titleRow = sheet.createRow(0);//表头 rowIndex=0
	    			// TODO SY 不知道这里是否要创建 INDEX = 1 和 2的行，还是用 0的即可
	    			titleRow.createCell(0).setCellValue(title);
	    			titleRow.getCell(0).setCellStyle(titleStyle);
	    			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length - 1));
	    			// 设置一个个合并单元格，区分row长度是2和3的
	    			SXSSFRow headerRowFirst = sheet.createRow(1);
	    			for(int z = 0; z < 4; z++) {
	    				sheet.addMergedRegion(new CellRangeAddress(1, createHeadRowNum, z, z));
	    				// 样式弄一下
//	    				headerRowFirst.createCell(z).setCellStyle(headerStyle);
	    			}
	    			
	    			// TODO SY 弄成下拉
	    			setPullDown(sheet, createHeadMap);
	    			
	    			SXSSFRow headerRowOne = sheet.getRow(1); //列头 rowIndex = 1
	    			SXSSFRow headerRowTow = sheet.createRow(2); //列头 rowIndex = 2
	    			SXSSFRow headerRowThree = sheet.createRow(3); //列头 rowIndex = 3
	    			for(int i=0;i<headers.length;i++) {
	    				headerRowOne.createCell(i).setCellValue(headers[i]);
	    				headerRowOne.getCell(i).setCellStyle(headerStyle);
	    				headerRowTow.createCell(i).setCellValue(headers[i]);
	    				headerRowTow.getCell(i).setCellStyle(headerStyle);
	    				if(createHeadRowNum == 3) {
	    					headerRowThree.createCell(i).setCellValue(headers[i]);
	    					headerRowThree.getCell(i).setCellStyle(headerStyle);
	    				}
	    				
	    			}
	    			rowIndex = 2;//数据内容从 rowIndex=2开始
	    		}
	    		JSONObject jo = (JSONObject) JSONObject.toJSON(obj);
	    		SXSSFRow dataRow = sheet.createRow(rowIndex);
	    		for (int i = 0; i < properties.length; i++)
	    		{
	    			SXSSFCell newCell = dataRow.createCell(i);
	    			
	    			Object o =  jo.get(properties[i]);
	    			String cellValue = ""; 
	    			if(o==null) cellValue = "";
	    			else if(o instanceof Date) cellValue = new SimpleDateFormat(datePattern).format(o);
	    			else if(o instanceof Float || o instanceof Double) 
	    				cellValue= new BigDecimal(o.toString()).setScale(2,BigDecimal.ROUND_HALF_UP).toString();
	    			else cellValue = o.toString();
	    			
	    			newCell.setCellValue(cellValue);
	    			newCell.setCellStyle(cellStyle);
	    		}
	    		rowIndex++;
	    	}
	    	// 自动调整宽度
	    	/*for (int i = 0; i < headers.length; i++) {
	            sheet.autoSizeColumn(i);
	        }*/
	    	try {
	    		workbook.write(out);
	    		workbook.close();
	    		workbook.dispose();
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	}
	    }
	    
	    /**
	     * 创建下拉的cell
	     * @param sheet
	     * 			工作簿
	     * @param createHeadMap
	     * 			下拉列表
	     * @author SY 
	     * @version 创建时间：2017年10月9日 上午10:17:50 
	     */
	    public static void setPullDown(HSSFSheet sheet, Map<Integer, Map<Integer, Map<String, Object>>> createHeadMap) {
	    	if(createHeadMap != null && sheet != null) {
	    		// 应为排序问题，所以不循环map，而是按照数字一个个获取
	    		Integer createHeadRowNum = createHeadMap.get(3) == null ? 2 : 3;
	    		for(int i = 1; i <= createHeadRowNum; i++) {
	    			Map<Integer, Map<String, Object>> rowMap = createHeadMap.get(i);
	    			Integer naturalRowIndex = i;
//	    		for(Entry<Integer, Map<Integer, Map<String, Object>>> entryRow : createHeadMap.entrySet()) {
//	    			Map<Integer, Map<String, Object>> rowMap = entryRow.getValue();
//	    			Integer naturalRowIndex = entryRow.getKey();
	    			HSSFRow row = sheet.getRow(naturalRowIndex);
	    			// 当前cell默认是第几个数据
	    			Integer thiValutIndex = 0;
	    			// 已经处理了几列
	    			Integer caculateNumber = 0;
	    			// 应为排序问题，所以不循环map，而是按照数字一个个获取
	    			for(int y = 1; caculateNumber < rowMap.size(); y++) {
	    				Integer naturalColumnIndex = y;
	    				Map<String, Object> paras = rowMap.get(y);
	    				if(paras == null) {
	    					continue;
	    				}
	    				caculateNumber++;
//	    			for(Entry<Integer, Map<String, Object>> entryColumn : rowMap.entrySet()) {
//	    				Integer naturalColumnIndex = entryColumn.getKey();
//	    				Map<String, Object> paras = entryColumn.getValue();
	    				// 下拉数值
	    				String[] pullDown = (String[]) paras.get("value");
	    				// 这个下拉，占了几列
	    				Integer columnNum = (Integer) paras.get("number");
	    				HSSFCell cell = row.getCell(naturalColumnIndex - 1);
	    				cell.setCellValue(pullDown[thiValutIndex]);    
	    				// 合并单元格
	    				if(naturalColumnIndex + columnNum - 2 != naturalColumnIndex - 1) {
	    					sheet.addMergedRegion(new CellRangeAddress(naturalRowIndex, naturalRowIndex, naturalColumnIndex - 1, naturalColumnIndex + columnNum - 2));	    					
	    				}
	    				thiValutIndex++;
	    				// 如果要切换指标点或者成绩组成了，则设置为0
	    				if(thiValutIndex == pullDown.length) {
	    					thiValutIndex = 0;
	    				}
	    		        
	    				//得到验证对象      
//	    				//从1开始下拉框处于第几列
	    				DataValidation data_validation_list = getDataValidationByFormula(pullDown,naturalRowIndex + 1,naturalColumnIndex);
	    		        //工作表添加验证数据      
	    		        sheet.addValidationData(data_validation_list); 
	    			}
	    		}
	    	}
			
		}
	    
	    /**
	     * 创建下拉的cell
	     * @param sheet
	     * 			工作簿
	     * @param createHeadMap
	     * 			下拉列表
	     * @author SY 
	     * @version 创建时间：2017年10月9日 上午10:17:50 
	     */
	    public static void setPullDown(SXSSFSheet sheet, Map<Integer, Map<Integer, Map<String, Object>>> createHeadMap) {
	    	if(createHeadMap != null && sheet != null) {
	    		for(Entry<Integer, Map<Integer, Map<String, Object>>> entryRow : createHeadMap.entrySet()) {
	    			Map<Integer, Map<String, Object>> rowMap = entryRow.getValue();
	    			Integer naturalRowIndex = entryRow.getKey();
	    			SXSSFRow row = sheet.createRow(naturalRowIndex - 1);
	    			// 当前cell默认是第几个数据
	    			Integer thiValutIndex = 0;
	    			for(Entry<Integer, Map<String, Object>> entryColumn : rowMap.entrySet()) {
	    				Integer naturalColumnIndex = entryColumn.getKey();
	    				Map<String, Object> paras = entryColumn.getValue();
	    				// 下拉数值
	    				String[] pullDown = (String[]) paras.get("value");
	    				SXSSFCell cell = row.createCell(naturalColumnIndex - 1); 
	    				cell.setCellValue(pullDown[thiValutIndex]);    
	    				thiValutIndex++;
	    				// 如果要切换指标点或者成绩组成了，则设置为0
	    				if(thiValutIndex == pullDown.length) {
	    					thiValutIndex = 0;
	    				}
	    				
	    				//得到验证对象      
//	    				//从1开始下拉框处于第几列
	    				DataValidation data_validation_list = getDataValidationByFormula(pullDown,naturalRowIndex,naturalColumnIndex);
	    				//工作表添加验证数据      
	    				sheet.addValidationData(data_validation_list); 
	    			}
	    		}
	    	}
	    	
	    }
	    
	    /**   
	     * 使用已定义的数据源方式设置一个数据验证   
	     * @param formulaString   
	     * @param naturalRowIndex   
	     * @param naturalColumnIndex   
	     * @return   
	     */    
	    public static DataValidation getDataValidationByFormula(String[] formulaString,int naturalRowIndex,int naturalColumnIndex){    
	        //加载下拉列表内容      
	        DVConstraint constraint = DVConstraint.createExplicitListConstraint(formulaString);     
	        //设置数据有效性加载在哪个单元格上。      
	        //四个参数分别是：起始行、终止行、起始列、终止列      
	        int firstRow = naturalRowIndex-1;    
	        int lastRow = naturalRowIndex-1;    
	        int firstCol = naturalColumnIndex-1;    
	        int lastCol = naturalColumnIndex-1;    
	        CellRangeAddressList regions=new CellRangeAddressList(firstRow,lastRow,firstCol,lastCol);      
	        //数据有效性对象     
	        DataValidation data_validation_list = new HSSFDataValidation(regions,constraint);    
	        return data_validation_list;      
	    }  
	    
//	    /**   
//	     * 使用已定义的数据源方式设置一个数据验证   
//	     * @param formulaString   
//	     * @param naturalRowIndex   
//	     * @param naturalColumnIndex   
//	     * @return   
//	     */    
//	    public static XSSFDataValidation getXSSFDataValidationByFormula(String[] formulaString,int naturalRowIndex,int naturalColumnIndex){    
//	    	//加载下拉列表内容      
//	    	DVConstraint constraint = DVConstraint.createExplicitListConstraint(formulaString);     
//	    	//设置数据有效性加载在哪个单元格上。      
//	    	//四个参数分别是：起始行、终止行、起始列、终止列      
//	    	int firstRow = naturalRowIndex-1;    
//	    	int lastRow = naturalRowIndex-1;    
//	    	int firstCol = naturalColumnIndex-1;    
//	    	int lastCol = naturalColumnIndex-1;    
//	    	CellRangeAddressList regions=new CellRangeAddressList(firstRow,lastRow,firstCol,lastCol);    
//	    	
//	    	//数据有效性对象     
//	        XSSFDataValidation data_validation_list = (XSSFDataValidation) dvHelper.createValidation(constraint, regions);    
//	    	return data_validation_list;      
//	    }  
	    
		//Web 导出excel
	    public static void downloadExcelFile(String title,Map<String,String> headMap,JSONArray ja,HttpServletResponse response){
	        try {
	            ByteArrayOutputStream os = new ByteArrayOutputStream();
	            ExcelUtil.exportExcelX(title,headMap,ja,null,0,os);
	            byte[] content = os.toByteArray();
	            InputStream is = new ByteArrayInputStream(content);
	            // 设置response参数，可以打开下载页面
	            response.reset();

	            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"); 
	            response.setHeader("Content-Disposition", "attachment;filename="+ new String((title + ".xlsx").getBytes(), "iso-8859-1"));
	            response.setContentLength(content.length);
	            ServletOutputStream outputStream = response.getOutputStream();
	            BufferedInputStream bis = new BufferedInputStream(is);
	            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
	            byte[] buff = new byte[8192];
	            int bytesRead;
	            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
	                bos.write(buff, 0, bytesRead);

	            }
	            bis.close();
	            bos.close();
	            outputStream.flush();
	            outputStream.close();
	        }catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public static void main(String[] args) throws IOException {
	        int count = 10;
	        JSONArray ja = new JSONArray();
//	        for(int i=0;i<100000;i++){
        	for(int i=0;i<count;i++){
	            Student s = new Student();
	            s.setName("POI"+i);
	            s.setAge(i);
	            s.setBirthday(new Date());
	            s.setHeight(i);
	            s.setWeight(i);
	            s.setSex(i/2==0?false:true);
	            ja.add(s);
	        }
	        Map<String,String> headMap = new LinkedHashMap<String,String>();
	        headMap.put("name","姓名");
	        headMap.put("age","年龄");
	        headMap.put("birthday","生日");
	        headMap.put("height","身高");
	        headMap.put("weight","体重");
	        headMap.put("sex","性别");

	        String title = "测试";
	        /*
	        OutputStream outXls = new FileOutputStream("E://a.xls");
	        System.out.println("正在导出xls....");
	        Date d = new Date();
	        ExcelUtil.exportExcel(title,headMap,ja,null,outXls);
	        System.out.println("共"+count+"条数据,执行"+(new Date().getTime()-d.getTime())+"ms");
	        outXls.close();*/
	        //
	        OutputStream outXlsx = new FileOutputStream("F://_Use_One//output//b3.xlsx");
	        System.out.println("正在导出xlsx....");
	        Date d2 = new Date();
	        ExcelUtil.exportExcelX(title,headMap,ja,null,0,outXlsx);
	        System.out.println("共"+count+"条数据,执行"+(new Date().getTime()-d2.getTime())+"ms");
	        outXlsx.close();

	    }
	}
	class Student {
	    private String name;
	    private int age;
	    private Date birthday;
	    private float height;
	    private double weight;
	    private boolean sex;

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public Integer getAge() {
	        return age;
	    }

	    public Date getBirthday() {
	        return birthday;
	    }

	    public void setBirthday(Date birthday) {
	        this.birthday = birthday;
	    }

	    public float getHeight() {
	        return height;
	    }

	    public void setHeight(float height) {
	        this.height = height;
	    }

	    public double getWeight() {
	        return weight;
	    }

	    public void setWeight(double weight) {
	        this.weight = weight;
	    }

	    public boolean isSex() {
	        return sex;
	    }

	    public void setSex(boolean sex) {
	        this.sex = sex;
	    }

	    public void setAge(Integer age) {
	        this.age = age;
	    }
	}

