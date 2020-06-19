package com.gnet.plugin.poi;

import com.alibaba.fastjson.JSON;
import com.gnet.plugin.poi.exception.ExcelParseException;
import com.gnet.utils.Kit;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Header3ExcelParser extends ExcelParser {

    @Override
    protected RowDefinition parseHeader(Sheet datatypeSheet, Map<String, Object> headerMap) {
        Row first = datatypeSheet.getRow(1);
        Row second = datatypeSheet.getRow(2);
        Row third = datatypeSheet.getRow(3);

        RowDefinition header = new RowDefinition(first.getPhysicalNumberOfCells());

        Iterator<Cell> cellIterator = first.iterator();

        Map<String, Map<String, String>> duplicateHeaderMap = new HashMap<>();
        Map<String, Map<String, String>> duplicateSubHeaderMap = new HashMap<>();
        Map<String, Map<String, String>> duplicateSubGradeMap = new HashMap<>();

        RowDefinition.ColumnDefinition currentHeaderColumn = null;
        RowDefinition.ColumnDefinition currentSubHeaderColumn = null;
        String currentHeaderValue = null;
        String currentSubHeaderValue = null;
        int currentHeaderEndIndex = -1;
        int currentSubHeaderEndIndex = -1;
        while (cellIterator.hasNext()) {
            Cell gradeCell = cellIterator.next();
            int cellIndex = gradeCell.getColumnIndex();

            String headerCellValue = Kit.getValue(gradeCell);

            Cell subHeaderCell = second.getCell(cellIndex);
            String subHeaderCellValue = Kit.getValue(subHeaderCell);

            Cell subGradeCell = third.getCell(cellIndex);
            String subGradeCellValue = Kit.getValue(subGradeCell);

            if (currentHeaderValue != null) {
                if (gradeCell.getCellType() != Cell.CELL_TYPE_BLANK) {
                    // 属于成绩列范畴，但是列类型非blank，则为之前定义模板框架不一致，抛出异常
                    // 校验模板的成绩类型下属指标点列，是否与模板定义的改类型下属指标点列一致
                    throw new ExcelParseException(String.format("第二行第%s列应该属于%s，与模板不一致，请修改后重试", cellIndex, currentHeaderValue));
                }

                if (currentSubHeaderValue != null && subHeaderCell.getCellType() != Cell.CELL_TYPE_BLANK) {
                    throw new ExcelParseException(String.format("第三行第%s列应该属于%s，与模板不一致，请修改后重试", cellIndex, currentSubHeaderValue));
                }

                Map<String, Object> subHeaderMap = (Map<String, Object>) headerMap.get(currentHeaderValue);

                if (currentSubHeaderValue == null) {
                    if (duplicateSubHeaderMap.containsKey(subHeaderCellValue)) {
                        Map<String, String> subInfo = duplicateSubHeaderMap.get(subHeaderCellValue);
                        // 有重复
                        throw new RuntimeException(String.format("第三行第%s个成绩类型与第三行第%s个成绩类型重复", cellIndex + 1, subInfo.get("index")));
                    }

                    Map<String, String> subInfo = new HashMap<>();
                    subInfo.put("index", String.valueOf(cellIndex));
                    subInfo.put("value", subHeaderCellValue);
                    duplicateSubHeaderMap.put(subHeaderCellValue, subInfo);

                    currentSubHeaderValue = subHeaderCellValue;

                    List<String> gradeList = (List<String>) subHeaderMap.get(currentSubHeaderValue);

                    currentSubHeaderEndIndex = cellIndex + gradeList.size() - 1;
                    currentSubHeaderColumn = RowDefinition.ColumnDefinition
                            .createGroupColumn(cellIndex, currentSubHeaderEndIndex, subHeaderCellValue)
                            .setIndexs(header.getIndexs());
                    ((RowDefinition.GroupColumnDefinition) currentHeaderColumn).addColumn(currentSubHeaderColumn);
                }

                // 校验成绩子列

                validateSubGrade(subHeaderMap, cellIndex, currentSubHeaderValue, subGradeCellValue, duplicateSubGradeMap);

                // 为Group添加子列
                ((RowDefinition.GroupColumnDefinition) currentSubHeaderColumn).addColumn(RowDefinition.ColumnDefinition.createCommonColumn(cellIndex, subGradeCellValue));

                // 如果是子项的最后一个成绩子列，清理数据
                if (currentSubHeaderEndIndex == cellIndex) {
                    currentSubHeaderColumn = null;
                    currentSubHeaderValue = null;
                    currentSubHeaderEndIndex = -1;

                    duplicateSubGradeMap.clear();
                }

                // 如果是最后一个成绩子列，清理数据
                if (currentHeaderEndIndex == cellIndex) {
                    // 属于成绩列，且为最后一列则将currentGrade和currentEndIndex清空
                    currentHeaderColumn = null;
                    currentHeaderValue = null;
                    currentHeaderEndIndex = -1;

                    duplicateSubHeaderMap.clear();
                }

                continue;
            }

            // 非成绩列，普通列
            if (!headerMap.containsKey(headerCellValue)) {
                // 不包含头信息，非成绩行
                header.addColumn(RowDefinition.ColumnDefinition.createCommonColumn(cellIndex, headerCellValue));
                continue;
            }

            // 判断模板自身成绩类型列是否有重复
            if (duplicateHeaderMap.containsKey(headerCellValue)) {
                Map<String, String> info = duplicateHeaderMap.get(headerCellValue);
                // 有重复
                throw new ExcelParseException(String.format("第二行第%s个成绩类型与第二行第%s个成绩类型重复", cellIndex + 1, info.get("index")));
            }
            Map<String, String> info = new HashMap<>();
            info.put("index", String.valueOf(cellIndex));
            info.put("value", headerCellValue);
            duplicateHeaderMap.put(headerCellValue, info);

            // 判断头下是否还有子项
            Object subHeaderObj = headerMap.get(headerCellValue);
            if (!(subHeaderObj instanceof Map)) {
                // 头下没有头
                throw new ExcelParseException("模板指定错误，该解析器只能解析3层表头");
            }
            Map<String, Object> subHeaderMap = (Map<String, Object>) subHeaderObj;
            if (!subHeaderMap.containsKey(subHeaderCellValue)) {
                throw new RuntimeException(String.format("第三行第%s列定义错误，必须在%s内选择", cellIndex + 1, JSON.toJSONString(subHeaderMap.get(subHeaderCellValue))));
            }

            if (duplicateSubHeaderMap.containsKey(subHeaderCellValue)) {
                Map<String, String> subInfo = duplicateSubHeaderMap.get(subHeaderCellValue);
                // 有重复
                throw new ExcelParseException(String.format("第三行第%s个成绩类型与第三行第%s个成绩类型重复", cellIndex + 1, subInfo.get("index")));
            }
            Map<String, String> subInfo = new HashMap<>();
            subInfo.put("index", String.valueOf(cellIndex));
            subInfo.put("value", subHeaderCellValue);
            duplicateSubHeaderMap.put(subHeaderCellValue, subInfo);

            // 校验成绩子列
            validateSubGrade(subHeaderMap, cellIndex, subHeaderCellValue, subGradeCellValue, duplicateSubGradeMap);

            currentHeaderValue = headerCellValue;
            currentSubHeaderValue = subHeaderCellValue;

            // 从该列开始即为成绩列
            // 计算该项成绩列的结束位置
            currentHeaderEndIndex = cellIndex;
            Iterator<Map.Entry<String, Object>> iter = subHeaderMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                List<String> gradeList = (List<String>) entry.getValue();
                currentHeaderEndIndex += gradeList.size();
            }
            currentHeaderEndIndex += -1;

            List<String> gradeList = (List<String>) subHeaderMap.get(subHeaderCellValue);
            currentSubHeaderEndIndex = cellIndex + gradeList.size() - 1;

            currentSubHeaderColumn = RowDefinition.ColumnDefinition
                    .createGroupColumn(cellIndex, currentSubHeaderEndIndex, subHeaderCellValue)
                    .setIndexs(header.getIndexs())
                    .addColumn(RowDefinition.ColumnDefinition.createCommonColumn(cellIndex, subGradeCellValue));
            currentHeaderColumn = RowDefinition.ColumnDefinition
                    .createGroupColumn(cellIndex, currentHeaderEndIndex, headerCellValue)
                    .setIndexs(header.getIndexs())
                    .addColumn(currentSubHeaderColumn);
            header.addColumn(currentHeaderColumn);

            // 如果是子项的最后一个成绩子列，清理数据
            if (currentSubHeaderEndIndex == cellIndex) {
                currentSubHeaderColumn = null;
                currentSubHeaderValue = null;
                currentSubHeaderEndIndex = -1;

                duplicateSubGradeMap.clear();
            }

            // 如果是最后一个成绩子列，清理数据
            if (currentHeaderEndIndex == cellIndex) {
                // 属于成绩列，且为最后一列则将currentGrade和currentEndIndex清空
                currentHeaderColumn = null;
                currentHeaderValue = null;
                currentHeaderEndIndex = -1;

                duplicateSubHeaderMap.clear();
            }

        }

        // 清理数据
        duplicateHeaderMap.clear();

        return header;
    }

    private void validateSubGrade(Map<String, Object> subHeaderMap,
                                  int cellIndex,
                                  String currentGradeValue,
                                  String subGradeCellValue,
                                  Map<String, Map<String, String>> duplicateSubGradeMap) {
        // 对成绩子列（第三行）校验，判断是否属于模板定义范畴
        List<String> gradeList = (List<String>) subHeaderMap.get(currentGradeValue);
        if (!gradeList.contains(subGradeCellValue)) {
            // 成绩列不包含在模板下该成绩类型的成绩列中
            throw new ExcelParseException(String.format("第四行第%s列必须为%s中的任意一个", cellIndex + 1, JSON.toJSONString(gradeList)));
        }

        if (duplicateSubGradeMap.containsKey(subGradeCellValue)) {
            Map<String, String> info = duplicateSubGradeMap.get(subGradeCellValue);
            // 有重复
            throw new ExcelParseException(String.format("第四行第%s列与第四行第%s列重复", cellIndex + 1, info.get("index") + 1));
        }

        Map<String, String> info = new HashMap<>(3);
        info.put("index", String.valueOf(cellIndex));
        info.put("value", subGradeCellValue);
        info.put("group", currentGradeValue);
        duplicateSubGradeMap.put(subGradeCellValue, info);
    }

}
