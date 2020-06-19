package com.gnet.plugin.poi;

import com.gnet.plugin.poi.exception.ExcelException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * Excel到处类
 */
public class ExcelExporter {

    /**
     * 导出定义到excel文件，注意，文件必须为xls后缀（无提示）
     *
     * @param depth                      头高度（不包含提示行）
     * @param rowDefinition              头定义
     * @param excelExporterDataProcessor 数据内容处理，接口类，内部方法必须返回@{List<List<String>>}
     * @param targetFile                 输出文件
     */
    public static void exportToExcel(int depth, RowDefinition rowDefinition, ExcelExporterDataProcessor excelExporterDataProcessor, String targetFile) {
        exportToExcel(depth, rowDefinition, excelExporterDataProcessor, targetFile, "xxxxxx");
    }

    /**
     * 导出定义到excel文件，注意，文件必须为xls后缀
     *
     * @param depth                      头高度（不包含提示行）
     * @param rowDefinition              头定义
     * @param excelExporterDataProcessor 数据内容处理，接口类，内部方法必须返回@{List<List<String>>}
     * @param targetFile                 输出文件
     * @param tip                        提示
     */
    public static void exportToExcel(int depth, RowDefinition rowDefinition, ExcelExporterDataProcessor excelExporterDataProcessor, String targetFile, String tip) {
        List<List<String>> data = excelExporterDataProcessor.invoke();

        HSSFWorkbook workBook = new HSSFWorkbook();
        HSSFSheet sheet = workBook.createSheet("数据Sheet");

        List<Map<String, Object>> rowSpan = new ArrayList<>();

        // 创建样式
        Map<String, HSSFCellStyle> styles = createStyle(workBook);

        // 创建提示
        createTip(workBook, sheet, rowDefinition, tip);

        Row row = sheet.createRow(1);
        //List<RowDefinition.ColumnDefinition> columns = rowDefinition.getGroupColumnDefinition().getColumns();
        for (RowDefinition.ColumnDefinition item : rowDefinition.getGroupColumnDefinition().getColumns()) {

            createCell(rowDefinition.getIndexs().size(), item, sheet, row, rowSpan);
        }

        for (RowDefinition.ColumnDefinition columnDefinition : rowDefinition.getGroupColumnDefinition().getColumns()) {
            if (!columnDefinition.isGroup()) {
                // 合并非组的单元格
                if (depth != 1) {
                    int index = columnDefinition.getIndex();
                    sheet.addMergedRegion(new CellRangeAddress(1, depth, index, index));
                }
            }
        }

        // 填充表头样式
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row styleRow = sheet.getRow(i);
            for (int j = 0; j < rowDefinition.getIndexs().size(); j++) {
                Cell styleCell = styleRow.getCell(j);
                if (styleCell == null) {
                    styleCell = styleRow.createCell(j);
                }
                if (i == 0) {
                    styleCell.setCellStyle(styles.get("title"));
                } else {
                    styleCell.setCellStyle(styles.get("header"));
                    setCellWidth(sheet, styleCell, j);
                }
            }
        }

        // 对body添加约束
        Iterator<Map.Entry<Integer, RowDefinition.ColumnDefinition>> iter = rowDefinition.getIndexs().entrySet().iterator();
        while (iter.hasNext()) {
            final Map.Entry<Integer, RowDefinition.ColumnDefinition> entry = iter.next();
            final int columnIndex = entry.getKey();
            final RowDefinition.ColumnDefinition columnDefinition = entry.getValue();
            if (columnDefinition.getDataValidationDefinition() != null) {
                // 存在值约束
                CellRangeAddressList cellRangeAddressList = new CellRangeAddressList();
                cellRangeAddressList.addCellRangeAddress(new CellRangeAddress(depth + 1, 1048575, columnIndex, columnIndex));

                addValidation(sheet, columnDefinition.getDataValidationDefinition(), cellRangeAddressList);
            }
        }

        // 填充数据
        for (int i = 0; i < data.size(); i++) {
            int rowNum = i + depth + 1;
            // 创建第 i 行
            Row dataRow = sheet.createRow(rowNum);
            // 获取第 i 行的数据
            List<String> item = data.get(i);
            // 循环第 i 行的所有列，并填充内容
            for (int j = 0; j < rowDefinition.getIndexs().size(); j++) {
                final RowDefinition.ColumnDefinition columnDefinition = rowDefinition.getIndexs().get(j);

                Cell dataCell = dataRow.createCell(j);

                String value;
                if (item.size() <= j) {
                    // 无数据
                    value = "";
                } else {
                    value = item.get(j);
                }

                // 判断是否有body列的约束条件，有的话检查值
                RowDefinition.ValidateDefinition dataValidateDefinition = columnDefinition.getDataValidationDefinition();
                if (dataValidateDefinition != null && !dataValidateDefinition.check(value)) {
                    throw new ExcelException(String.format("The value %s is not passed by data validation %s", value, dataValidateDefinition.checksToString()));
                }

                dataCell.setCellValue(value);
                dataCell.setCellStyle(styles.get("cell"));
                setCellWidth(sheet, dataCell, j);
            }
        }

        try {
            OutputStream ops = new FileOutputStream(targetFile);
            workBook.write(ops);
            workBook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface ExcelExporterDataProcessor {
        List<List<String>> invoke();
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

        return styles;
    }

    private static void setCellWidth(HSSFSheet sheet, Cell cell, int cellIndex) {
        float prevWidth = sheet.getColumnWidth(cellIndex);
        // 2017年12月28日， 从自适应长度变成固定10个中文字
        float newWidth = 10 * 512;
//        float newWidth = cell.getStringCellValue().getBytes().length * 256;
        Float resultWidth = prevWidth < newWidth ? newWidth : prevWidth;
        sheet.setColumnWidth(cellIndex, resultWidth.intValue());
    }

    private static void createTip(HSSFWorkbook workBook, HSSFSheet sheet, RowDefinition rowDefinition, String tip) {
        if (StringUtils.isBlank(tip)) {
            return;
        }

        // 创建tip
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, rowDefinition.getIndexs().size() - 1));
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue(tip);

    }

    private static void createCell(int colsNum, RowDefinition.ColumnDefinition columnDefinition, Sheet sheet, Row row, List<Map<String, Object>> rowSpan) {
        int rowNum = row.getRowNum();

        CellRangeAddressList addressList = new CellRangeAddressList();
        if (columnDefinition.isGroup()) {
            RowDefinition.GroupColumnDefinition groupColumnDefinition = ((RowDefinition.GroupColumnDefinition) columnDefinition);

            CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, groupColumnDefinition.getIndex(), groupColumnDefinition.getEndIndex());
            //合并单元格如果只有1列不用合并
            if (groupColumnDefinition.getIndex()==groupColumnDefinition.getEndIndex()){

            }else {
                sheet.addMergedRegion(cellRangeAddress);
            }

            addressList.addCellRangeAddress(cellRangeAddress);

            List<Integer> groupCells = new ArrayList<>();
            Row nextRow = sheet.getRow(rowNum + 1);
            if (nextRow == null) {
                nextRow = sheet.createRow(rowNum + 1);
            }
            for (RowDefinition.ColumnDefinition item : groupColumnDefinition.getColumns()) {
                groupCells.add(item.getIndex());
                createCell(colsNum, item, sheet, nextRow, rowSpan);
            }
        } else {
            //addressList.addCellRangeAddress(new CellRangeAddress(rowNum, rowNum, columnDefinition.getIndex(), columnDefinition.getIndex()));
        }

        // 存在约束则校验值并添加
       /* RowDefinition.ValidateDefinition validateDefinition = columnDefinition.getValidateDefinition();

        if (validateDefinition != null) {
            addValidation(sheet, validateDefinition, addressList);

//            if (!validateDefinition.check(columnDefinition.getName())) {
//                throw new RuntimeException(String.format("The column name is not fit for column validation"));
//            }
        }*/

        Cell cell = row.createCell(columnDefinition.getIndex());
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue(String.valueOf(columnDefinition.getName()));
    }

    private static void addValidation(Sheet sheet, RowDefinition.ValidateDefinition validateDefinition, CellRangeAddressList addressList) {
        DataValidationHelper dvHelper = sheet.getDataValidationHelper();
        DataValidationConstraint dvConstraint = dvHelper.createExplicitListConstraint(validateDefinition.getChecks());

        DataValidation dataValidation = dvHelper.createValidation(dvConstraint, addressList);
        sheet.addValidationData(dataValidation);
    }

}
