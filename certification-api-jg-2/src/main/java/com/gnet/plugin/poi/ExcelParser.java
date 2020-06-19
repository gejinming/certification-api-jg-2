package com.gnet.plugin.poi;

import com.alibaba.fastjson.JSON;
import com.gnet.plugin.poi.exception.ExcelParseException;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class ExcelParser {

    public ExcelDefinition parse(InputStream excelFile, Map<String, Object> headerMap, int headerHeight) throws IOException {
        return parse(excelFile, headerMap, headerHeight, null);
    }

    public ExcelDefinition parse(InputStream excelFile, Map<String, Object> headerMap, int headerHeight, Map<String, Object> bodyMap) throws IOException {
        Workbook workbook = new HSSFWorkbook(excelFile);
        Sheet datatypeSheet = workbook.getSheetAt(0);

        RowDefinition header = parseHeader(datatypeSheet, headerMap);

        List<RowDefinition> body = parseBody(datatypeSheet, header, headerHeight, bodyMap);

        ExcelDefinition excelDefinition = new ExcelDefinition();
        excelDefinition.setHeader(header);
        excelDefinition.setBody(body);

        return excelDefinition;
    }

    /**
     * 解析头信息
     *
     * @param datatypeSheet
     * @param headerMap
     * @return
     */
    protected abstract RowDefinition parseHeader(Sheet datatypeSheet, Map<String, Object> headerMap);

    /**
     * 解析数据内容
     *
     * @param datatypeSheet
     * @param headerDefinition
     * @return
     */
    private List<RowDefinition> parseBody(Sheet datatypeSheet, RowDefinition headerDefinition, int headerHeight, Map<String, Object> bodyMap) {
        Iterator<Row> iterator = datatypeSheet.rowIterator();

        List<RowDefinition> rowDefinitions = new ArrayList<>();
        while (iterator.hasNext()) {

            Row currentRow = iterator.next();

            if (currentRow.getRowNum() < headerHeight + 1) {
                continue;
            }

            RowDefinition rowDefinition = parseBodyRow(currentRow, headerDefinition, bodyMap);
            rowDefinitions.add(rowDefinition);
        }

        return rowDefinitions;
    }

    private RowDefinition parseBodyRow(Row row, RowDefinition headerDefinition, Map<String, Object> bodyMap) {
        RowDefinition rowValueDefinition = RowDefinition.copy(headerDefinition);

        Iterator<Cell> cellIterator = row.cellIterator();

        while (cellIterator.hasNext()) {

            Cell cell = cellIterator.next();
            RowDefinition.ColumnDefinition valueDefinition = rowValueDefinition.getIndexs().get(cell.getColumnIndex());

            // 验证value值
            Object value = null;
            if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
                value = cell.getStringCellValue();
            } else if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                // 不支持自定义日期格式
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // 日期格式
                    value = cell.getDateCellValue();
                } else {
                    value = cell.getNumericCellValue();
                }
            } else if (Cell.CELL_TYPE_BOOLEAN == cell.getCellType()) {
                value = cell.getBooleanCellValue();
            } else if (Cell.CELL_TYPE_FORMULA == cell.getCellType()) {
                value = cell.getCachedFormulaResultType();
            } else if (Cell.CELL_TYPE_BLANK == cell.getCellType()) {
                value = "";
            } else {
                value = "";
            }

            Object values = bodyMap != null ? bodyMap.get(valueDefinition.getName()) : null;
            if (values != null && !(values instanceof RowDefinition.ValidateDefinition)) {
                throw new ExcelParseException("body check value should be list");
            }

            if (values != null) {
                RowDefinition.ValidateDefinition checks = (RowDefinition.ValidateDefinition) values;

                // 存在值约束则验证约束
                if (checks != null && !checks.check(String.valueOf(value))) {
                    throw new ExcelParseException(String.format("第 %s 行第 %s 列的数据 %s 不在 %s 范围内", row.getRowNum(), cell.getColumnIndex(), String.valueOf(value), JSON.toJSONString(checks.getChecks())));
                }
            }

            valueDefinition.setValue(value);
        }

        return rowValueDefinition;
    }

}

