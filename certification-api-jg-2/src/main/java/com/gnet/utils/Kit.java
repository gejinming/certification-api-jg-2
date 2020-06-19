package com.gnet.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

/**
 * Created with IntelliJ IDEA.
 *
 * @author xuqiang
 * @date 2018/02/08
 * @description
 **/
public class Kit {

    public static String getValue(Cell cell) {
        String obj;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                obj = StringUtils.trim(cell.getRichStringCellValue().getString());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    obj = cell.getDateCellValue().toString();
                } else {
                    obj = String.valueOf(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                obj = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                obj = cell.getCellFormula();
                break;
            default:
                obj = null;
        }

        return obj;
    }

}
