package com.gnet.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;

import com.gnet.model.DbModel;
import com.gnet.plugin.poi.ExcelColumn;
import com.gnet.plugin.poi.ExcelHead;
import com.jfinal.plugin.activerecord.Record;

import jodd.datetime.JDateTime;

/**
 * excel的操作工具类
 * @type utils
 * @desciption
 * 		excel的操作工具类,可以导出excel文件
 * @author wct
 * @Date 2015年12月7日
 */
public class ExcelKit {
	
	/**
	 * 正式数据从第几行开始(必填)
	 */
	public static final Integer ROWCOUNTCODE = 1;
	/**
	 * 表格的标题
	 */
	public static final Integer TITLECODE = 2;
	
	/**
	 * 导出excel文件
	 * @description
	 * 		通过空的excel模板,表格头文件信息,数据信息,表格属性，生成excel文件
	 * @call {@linkplain ExcelKit this}
	 * 		 {@linkplain ExcelKit#setExcelTitle(Workbook, Sheet, String, Integer) setExcelTitle}
	 * @call {@linkplain ExcelKit this}
	 * 		 {@linkplain ExcelKit#buildDataList(Workbook, Sheet, ExcelHead, List) buildDataList}
	 * @param modelFile
	 * 			模板文件
	 * @param outFile
	 * 			生成文件
	 * @param excelHead
	 * 			表文件头信息
	 * @param dataList
	 * 			数据集
	 * @param excelProperties
	 * 			表格属性
	 */
	@SuppressWarnings("rawtypes")
	public static <T extends DbModel> void exportToExcelFile(File modelFile, File outFile, ExcelHead excelHead, List<T> dataList, Map<Integer,Object> excelProperties) throws Exception{
		//判断必填数据是否存在
		if (excelProperties == null || !(excelProperties.get(ROWCOUNTCODE) instanceof Integer)) {
			throw new Exception("rowcount is null or is not a valid Integer value");
		}
		if (excelHead.getColumns() == null || excelHead.getColumns().size() == 0) {
			throw new Exception("field info can not be null");
		}
		//设置整数数据开始的行数和表格列数
		excelHead.setRowCount((Integer)excelProperties.get(ROWCOUNTCODE));
		excelHead.setColumnCount(excelHead.getColumns().size());
		//开始建表
		InputStream input = null;
		FileOutputStream output = null;
		Workbook workbook = null;
		try {
			input = new FileInputStream(modelFile);
			workbook = WorkbookFactory.create(input);
			Sheet sheet = workbook.getSheetAt(0);
//			Patriarch patriarch = sheet.createDrawingPatriarch(); 可以绘制图片
			if (excelProperties.get(TITLECODE) != null) {
				setExcelTitle(workbook, sheet, excelProperties.get(TITLECODE).toString(), excelHead.getColumnCount());
				//若数据从第0行开始，则改为从第1 行开始
				if (excelHead.getRowCount() == 0) {
					excelHead.setRowCount(1);
				}
			}
			buildDataList(workbook, sheet, excelHead, dataList);
			output = new FileOutputStream(outFile);
			workbook.write(output);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} finally {
			try {
				if (input != null) {
					input.close();
				}
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 导出excel文件
	 * @description
	 * 		通过空的excel模板,表格头文件信息,数据信息,表格属性，生成excel文件
	 * @call {@linkplain ExcelKit this}
	 * 		 {@linkplain ExcelKit#setExcelTitle(Workbook, Sheet, String, Integer) setExcelTitle}
	 * @call {@linkplain ExcelKit this}
	 * 		 {@linkplain ExcelKit#buildRecordDataList(Workbook, Sheet, ExcelHead, List) buildDataList}
	 * @param modelFile
	 * 			模板文件
	 * @param outFile
	 * 			生成文件
	 * @param excelHead
	 * 			表文件头信息
	 * @param dataList
	 * 			数据集
	 * @param excelProperties
	 * 			表格属性
	 */
	@SuppressWarnings("rawtypes")
	public static <T extends DbModel> void exportRecordToExcelFile(File modelFile, File outFile, ExcelHead excelHead, List<Record> dataList, Map<Integer,Object> excelProperties) throws Exception{
		//判断必填数据是否存在
		if (excelProperties == null || !(excelProperties.get(ROWCOUNTCODE) instanceof Integer)) {
			throw new Exception("rowcount is null or is not a valid Integer value");
		}
		if (excelHead.getColumns() == null || excelHead.getColumns().size() == 0) {
			throw new Exception("field info can not be null");
		}
		//设置整数数据开始的行数和表格列数
		excelHead.setRowCount((Integer)excelProperties.get(ROWCOUNTCODE));
		excelHead.setColumnCount(excelHead.getColumns().size());
		//开始建表
		InputStream input = null;
		FileOutputStream output = null;
		Workbook workbook = null;
		try {
			input = new FileInputStream(modelFile);
			workbook = WorkbookFactory.create(input);
			Sheet sheet = workbook.getSheetAt(0);
//			Patriarch patriarch = sheet.createDrawingPatriarch(); 可以绘制图片
			if (excelProperties.get(TITLECODE) != null) {
				setExcelTitle(workbook, sheet, excelProperties.get(TITLECODE).toString(), excelHead.getColumnCount());
				//若数据从第0行开始，则改为从第1 行开始
				if (excelHead.getRowCount() == 0) {
					excelHead.setRowCount(1);
				}
			}
			buildRecordDataList(workbook, sheet, excelHead, dataList);
			output = new FileOutputStream(outFile);
			workbook.write(output);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} finally {
			try {
				if (input != null) {
					input.close();
				}
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 将数据加入表格
	 * @description
	 * 		先加入表头信息，之后取出数据，转型为对应类型加入每个单元格中
	 * @call {@linkplain ExcelKit this}
	 * 		 {@linkplain ExcelKit#convertHeadToMap(List) convertHeadToMap}
	 * @param workbook
	 * 			excel工作簿
	 * @param sheet
	 * 			excel表格
	 * @param head
	 * 			表文件头信息
	 * @param dataList
	 * 			数据集
	 */
	@SuppressWarnings("rawtypes")
	private static <T extends DbModel> void buildDataList(Workbook workbook, Sheet sheet, ExcelHead head, List<T> dataList) {
		List<ExcelColumn> excelColumns = head.getColumns();
		Map<Integer, ExcelColumn> excelHeadMap = convertHeadToMap(excelColumns);
		Map<String, Map<Object, Object>> excelHeadConvertMap = head.getColumnsConvertMap();
		
		//插入数据的行数(包括表头一行)
		int startRow = head.getRowCount();
		int seq = 1;
		//表头创建
		Row headRow = sheet.createRow(startRow++);
		for (int i=0; i<head.getColumnCount(); i++) {
			Cell cell = headRow.createCell(i);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(excelColumns.get(i).getFieldDispName());
		}
		
		for (T obj : dataList) {
			Row row = sheet.createRow(startRow++);
			for (int i = 0; i < head.getColumnCount(); i++) {
				ExcelColumn excelColumn = excelHeadMap.get(i);
				Cell cell = row.createCell(i);
				cell.setCellType(excelColumn.getType());
				String fieldName = excelColumn.getFieldName();
				if (StringUtils.isNotBlank(fieldName)) {
					Object valueObject = obj.get(fieldName);
					
					//如果存在需要转换的字段信息，则进行转换
					if(excelHeadConvertMap != null && excelHeadConvertMap.get(fieldName) != null) {
	    				valueObject = excelHeadConvertMap.get(fieldName).get(valueObject);
	    			}
					
					if (valueObject == null) {
						cell.setCellValue("");
					} else if (valueObject instanceof Integer) {
						cell.setCellValue((Integer)valueObject);
					} else if (valueObject instanceof String) {
						cell.setCellValue((String)valueObject);
					} else if (valueObject instanceof Date) {
						cell.setCellValue(new JDateTime((Date)valueObject).toString("YYYY-MM-DD"));
					} else {
						cell.setCellValue(valueObject.toString());
					}
 				} else {
 					//序号
 					cell.setCellValue(seq++);
 				}
			}
		}
	}
	
	/**
	 * 将数据加入表格
	 * @description
	 * 		先加入表头信息，之后取出数据，转型为对应类型加入每个单元格中
	 * @call {@linkplain ExcelKit this}
	 * 		 {@linkplain ExcelKit#convertHeadToMap(List) convertHeadToMap}
	 * @param workbook
	 * 			excel工作簿
	 * @param sheet
	 * 			excel表格
	 * @param head
	 * 			表文件头信息
	 * @param dataList
	 * 			数据集
	 */
	private static void buildRecordDataList(Workbook workbook, Sheet sheet, ExcelHead head, List<Record> dataList) {
		List<ExcelColumn> excelColumns = head.getColumns();
		Map<Integer,ExcelColumn> excelHeadMap = convertHeadToMap(excelColumns);
		Map<String, Map<Object, Object>> excelHeadConvertMap = head.getColumnsConvertMap();
		
		//插入数据的行数(包括表头一行)
		int startRow = head.getRowCount();
		int seq = 1;
		//表头创建
		Row headRow = sheet.createRow(startRow++);
		for (int i=0; i<head.getColumnCount(); i++) {
			Cell cell = headRow.createCell(i);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(excelColumns.get(i).getFieldDispName());
		}
		
		for (Record obj : dataList) {
			Row row = sheet.createRow(startRow++);
			for (int i = 0; i < head.getColumnCount(); i++) {
				ExcelColumn excelColumn = excelHeadMap.get(i);
				Cell cell = row.createCell(i);
				cell.setCellType(excelColumn.getType());
				String fieldName =excelColumn.getFieldName();
				if (StringUtils.isNotBlank(fieldName)) {
					Object valueObject = obj.get(fieldName);
					
					//如果存在需要转换的字段信息，则进行转换
					if(excelHeadConvertMap != null && excelHeadConvertMap.get(fieldName) != null) {
	    				valueObject = excelHeadConvertMap.get(fieldName).get(valueObject);
	    			}
					
					if (valueObject == null) {
						cell.setCellValue("");
					} else if (valueObject instanceof Integer) {
						cell.setCellValue((Integer)valueObject);
					} else if (valueObject instanceof String) {
						cell.setCellValue((String)valueObject);
					} else if (valueObject instanceof Date) {
						cell.setCellValue(new JDateTime((Date)valueObject).toString("YYYY-MM-DD"));
					} else {
						cell.setCellValue(valueObject.toString());
					}
 				} else {
 					//序号
 					cell.setCellValue(seq++);
 				}
			}
		}
	}
	
	/**
	 * 设置表格标题
	 * @description
	 * 		设置表格的标题,默认文字高度300,宽度200,水平垂直居中,标题框占全部列数
	 * @param workbook
	 * 			excel工作簿
	 * @param sheet
	 * 			excel表格
	 * @param title
	 * 			表格标题
	 * @param columnCount
	 * 			表格列数
	 */
	private static void setExcelTitle(Workbook workbook, Sheet sheet, String title, Integer columnCount) {
		DataFormat format = workbook.createDataFormat();
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		
		//设置字体大小
		font.setFontHeight((short)300);
		font.setBoldweight((short)200);
		style.setFont(font);
		//水平垂直居中
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setDataFormat(format.getFormat(title));
		
		Row titleRow = sheet.createRow(0);
		titleRow.setHeight((short)700);
		Cell cell = titleRow.createCell(0);
		cell.setCellStyle(style);
		cell.setCellValue(title);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnCount-1));
	}
	
	/**
	 * 将表头信息根据列号转化为Map
	 * @description
	 * 		将表头信息根据列号转化为Map
	 * @param excelColumns
	 * 			表头信息集
	 * @return
	 */
	private static Map<Integer,ExcelColumn> convertHeadToMap(List<ExcelColumn> excelColumns) {
		Map<Integer,ExcelColumn> result = new HashMap<Integer,ExcelColumn>();
		for (ExcelColumn excelColumn : excelColumns) {
			if (StringUtils.isEmpty(excelColumn.getFieldDispName())) {
				continue;
			} else {
				result.put(excelColumn.getIndex(), excelColumn);
			}
		}
		return result;
	}
	
}
