package com.gnet.plugin.poi;

import com.alibaba.fastjson.JSON;
import com.gnet.plugin.poi.exception.ExcelParseException;
import com.gnet.utils.ConvertUtils;
import com.gnet.utils.Kit;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Header2ExcelParser extends ExcelParser {

    /**
     * 解析头信息
     *
     * @param datatypeSheet
     * @param headerMap
     * @return
     */
    @Override
    protected RowDefinition parseHeader(Sheet datatypeSheet, Map<String, Object> headerMap) {
        Row first = datatypeSheet.getRow(1);
        Row second = datatypeSheet.getRow(2);

        RowDefinition header = new RowDefinition(first.getPhysicalNumberOfCells());

        Iterator<Cell> cellIterator = first.iterator();

        Map<String, Map<String, String>> duplicateGradeMap = new HashMap<>();
        Map<String, Map<String, String>> duplicateSubGradeMap = new HashMap<>();

        RowDefinition.ColumnDefinition currentHeaderColumn = null;
        String currentGradeValue = null;
        int currentEndIndex = -1;
        while (cellIterator.hasNext()) {
            Cell gradeCell = cellIterator.next();
            int cellIndex = gradeCell.getColumnIndex();

            String gradeCellValue = Kit.getValue(gradeCell);

            Cell subGradeCell = second.getCell(gradeCell.getColumnIndex());
            String subGradeCellValue = Kit.getValue(subGradeCell);

            if (currentGradeValue != null) {
                if (gradeCell.getCellType() != Cell.CELL_TYPE_BLANK) {
                    // 属于成绩列范畴，但是列类型非blank，则为之前定义模板框架不一致，抛出异常
                    // 校验模板的成绩类型下属指标点列，是否与模板定义的改类型下属指标点列一致
                    throw new ExcelParseException(String.format("第二行第%s列应该属于%s，与模板不一致，请修改后重试", cellIndex, currentGradeValue));
                }

                // 校验成绩子列
                validateSubGrade(headerMap, cellIndex, currentGradeValue, subGradeCellValue, duplicateSubGradeMap);

                // 为Group添加子列
                ((RowDefinition.GroupColumnDefinition) currentHeaderColumn).addColumn(RowDefinition.ColumnDefinition.createCommonColumn(cellIndex, subGradeCellValue));

                // 如果是最后一个成绩子列，清理数据
                if (currentEndIndex == gradeCell.getColumnIndex()) {
                    // 属于成绩列，且为最后一列则将currentGrade和currentEndIndex清空
                    currentHeaderColumn = null;
                    currentGradeValue = null;
                    currentEndIndex = -1;
                    duplicateSubGradeMap.clear();
                }

                continue;
            }

            // 非成绩列，普通列
            if (!headerMap.containsKey(gradeCellValue)) {
                // 不包含头信息，非成绩行
                header.addColumn(RowDefinition.ColumnDefinition.createCommonColumn(cellIndex, gradeCellValue));
                continue;
            }

            // 判断模板自身成绩类型列是否有重复
            if (duplicateGradeMap.containsKey(gradeCellValue)) {
                Map<String, String> info = duplicateGradeMap.get(gradeCellValue);
                // 有重复
                throw new ExcelParseException(String.format("第二行第%s个成绩类型与第二行第%s个成绩类型重复", cellIndex + 1, info.get("index")));
            }
            Map<String, String> info = new HashMap<>();
            info.put("index", String.valueOf(cellIndex));
            info.put("value", gradeCellValue);
            duplicateGradeMap.put(gradeCellValue, info);

            // 校验成绩子列
            validateSubGrade(headerMap, cellIndex, gradeCellValue, subGradeCellValue, duplicateSubGradeMap);

            currentGradeValue = gradeCellValue;
            List<String> gradeList = (List<String>) headerMap.get(gradeCellValue);

            // 从该列开始即为成绩列
            // 计算该项成绩列的结束位置
            currentEndIndex = cellIndex + gradeList.size() - 1;

            currentHeaderColumn = RowDefinition.ColumnDefinition
                    .createGroupColumn(cellIndex, currentEndIndex, gradeCellValue)
                    .setIndexs(header.getIndexs())
                    .addColumn(RowDefinition.ColumnDefinition.createCommonColumn(cellIndex, subGradeCellValue));
            header.addColumn(currentHeaderColumn);

            // 如果是最后一个成绩子列，清理数据
            if (currentEndIndex == gradeCell.getColumnIndex()) {
                // 属于成绩列，且为最后一列则将currentGrade和currentEndIndex清空
                currentHeaderColumn = null;
                currentGradeValue = null;
                currentEndIndex = -1;
                duplicateSubGradeMap.clear();
            }
        }

        // 清理数据
        duplicateGradeMap.clear();

        return header;
    }

    private void validateSubGrade(Map<String, Object> headerMap,
                                  int cellIndex,
                                  String currentGradeValue,
                                  String subGradeCellValue,
                                  Map<String, Map<String, String>> duplicateSubGradeMap) {
        // 对成绩子列（第三行）校验，判断是否属于模板定义范畴
        List<String> gradeList = (List<String>) headerMap.get(currentGradeValue);
        if (!gradeList.contains(subGradeCellValue)) {
            // 成绩列不包含在模板下该成绩类型的成绩列中
            throw new ExcelParseException(String.format("第三行第%s列必须为%s中的任意一个", cellIndex + 1, JSON.toJSONString(gradeList)));
        }

        if (duplicateSubGradeMap.containsKey(subGradeCellValue)) {
            Map<String, String> info = duplicateSubGradeMap.get(subGradeCellValue);
            // 有重复
            throw new ExcelParseException(String.format("第三行第%s列与第三行第%s列重复", ConvertUtils.convert(info.get("index"), Integer.class) + 1 , cellIndex + 1));
        }

        Map<String, String> info = new HashMap<>(3);
        info.put("index", String.valueOf(cellIndex));
        info.put("value", subGradeCellValue);
        info.put("group", currentGradeValue);
        duplicateSubGradeMap.put(subGradeCellValue, info);
    }
}

