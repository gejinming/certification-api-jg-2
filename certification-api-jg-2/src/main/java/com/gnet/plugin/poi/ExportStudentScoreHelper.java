package com.gnet.plugin.poi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.stereotype.Component;

import com.jfinal.log.Logger;

/**
 * 学生成绩模板导出
 * 
 * @author SY
 * 
 * @date 2017年10月6日
 */
@Component("exportStudentScoreHelper")
public class ExportStudentScoreHelper {

	private static final Logger logger = Logger.getLogger(ExportStudentScoreHelper.class);
	
	public static Map<String, ExcelHead> mappings = new ConcurrentHashMap<String, ExcelHead>();
	
	private HSSFWorkbook workbook = null;     
    private HSSFCellStyle titleStyle = null;     
    private HSSFCellStyle dataStyle = null;    
    
    /**   
     * 列头样式   
     * @param workbook   
     * @param sheet   
     */    
    public void setTitleCellStyles(HSSFWorkbook workbook,HSSFSheet sheet){    
        titleStyle = workbook.createCellStyle();    
    
        //设置边框    
        titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);    
        titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);    
        titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);    
        titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);    
        //设置背景色    
        titleStyle.setFillForegroundColor(HSSFColor.WHITE.index);    
        titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);    
        //设置居中    
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);    
        //设置字体    
        HSSFFont font = workbook.createFont();    
        font.setFontName("宋体");    
        font.setFontHeightInPoints((short) 11); //设置字体大小    
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示    
        titleStyle.setFont(font);//选择需要用到的字体格式    
        //设置自动换行    
        titleStyle.setWrapText(true);    
        //设置列宽 ,第一个参数代表列id(从0开始),第2个参数代表宽度值    
        sheet.setColumnWidth(0, 5000);     
        sheet.setColumnWidth(1, 5000);     
        sheet.setColumnWidth(2, 3000);     
        sheet.setColumnWidth(3, 7000);     
        sheet.setColumnWidth(4, 5000);     
        sheet.setColumnWidth(5, 5000);     
        sheet.setColumnWidth(6, 3000);     
        sheet.setColumnWidth(7, 3000);     
        sheet.setColumnWidth(8, 5000);     
        sheet.setColumnWidth(9, 5000);     
        sheet.setColumnWidth(10, 3000);     
        sheet.setColumnWidth(11, 3000);     
        sheet.setColumnWidth(12, 3000);     
        sheet.setColumnWidth(13, 3000);     
        sheet.setColumnWidth(14, 7000);     
        sheet.setColumnWidth(15, 7000);     
        sheet.setColumnWidth(16, 7000);     
        sheet.setColumnWidth(17, 7000);     
        sheet.setColumnWidth(18, 10000);     
    }    
    /**   
     * 数据样式   
     * @param workbook   
     * @param sheet   
     */    
    public void setDataCellStyles(HSSFWorkbook workbook,HSSFSheet sheet){    
        dataStyle = workbook.createCellStyle();    
    
        //设置边框    
        dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);    
        dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);    
        dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);    
        dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);    
        //设置背景色    
        dataStyle.setFillForegroundColor(HSSFColor.WHITE.index);    
        dataStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);    
        //设置居中    
        dataStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);    
        //设置字体    
        HSSFFont font = workbook.createFont();    
        font.setFontName("宋体");    
        font.setFontHeightInPoints((short) 11); //设置字体大小    
        dataStyle.setFont(font);//选择需要用到的字体格式    
        //设置自动换行    
        dataStyle.setWrapText(true);    
    }    
  
/**   
     * 创建一列数据   
     * @param currentRow   
     * @param textList   
     */    
    public void creatRow(HSSFRow currentRow,List<String> textList){    
        if(textList!=null&&textList.size()>0){    
            int i = 0;    
            for(String cellValue : textList){    
                HSSFCell userNameLableCell = currentRow.createCell(i++);    
                userNameLableCell.setCellValue(cellValue);    
            }    
        }    
    }    
    
        
    /**   
     * 创建一列应用列头   
     * @param userinfosheet1   
     * @param userName   
     */    
    public void creatAppRowHead(HSSFSheet userinfosheet1,int naturalRowIndex){    
        HSSFRow row = userinfosheet1.createRow(naturalRowIndex-1);    
    
        //0.所属分类  
        HSSFCell Cell1 = row.createCell(0);    
        Cell1.setCellValue("所属分类");    
        Cell1.setCellStyle(titleStyle);    
    
        //1.分类名称  
        HSSFCell Cell2 = row.createCell(1);    
        Cell2.setCellValue("分类名称");    
        Cell2.setCellStyle(titleStyle);    
    
        //2.资产类型    
        HSSFCell Cell3 = row.createCell(2);    
        Cell3.setCellValue("资产类型");    
        Cell3.setCellStyle(titleStyle);    
    
        //3.设备用途  
        HSSFCell Cell4 = row.createCell(3);    
        Cell4.setCellValue("设备用途");    
        Cell4.setCellStyle(titleStyle);    
    
        //4.品牌    
        HSSFCell Cell5 = row.createCell(4);    
        Cell5.setCellValue("品牌");    
        Cell5.setCellStyle(titleStyle);    
        //5.型号   
        HSSFCell Cell6 = row.createCell(5);    
        Cell6.setCellValue("型号");    
        Cell6.setCellStyle(titleStyle);    
        //6.数量   
        HSSFCell Cell7 = row.createCell(6);    
        Cell7.setCellValue("数量");    
        Cell7.setCellStyle(titleStyle);    
        //7.计量单位   台、套、个   
        HSSFCell Cell8 = row.createCell(7);    
        Cell8.setCellValue("计量单位");    
        Cell8.setCellStyle(titleStyle);    
        //8.购入原值   
        HSSFCell Cell9 = row.createCell(8);    
        Cell9.setCellValue("购入原值");    
        Cell9.setCellStyle(titleStyle);    
        //9.购入时间  
        HSSFCell Cell10 = row.createCell(9);    
        Cell10.setCellValue("购入时间");    
        Cell10.setCellStyle(titleStyle);    
        //10.现存状态  闲置、在用   
        HSSFCell Cell11 = row.createCell(10);    
        Cell11.setCellValue("现存状态");    
        Cell11.setCellStyle(titleStyle);    
        //11.仓库状态  在库、离库   
        HSSFCell Cell12 = row.createCell(11);    
        Cell12.setCellValue("仓库状态");    
        Cell12.setCellStyle(titleStyle);    
        //12.资产属性  完好、损坏   
        HSSFCell Cell13 = row.createCell(12);    
        Cell13.setCellValue("资产属性");    
        Cell13.setCellStyle(titleStyle);    
        //13.报废属性  正常、报废  
        HSSFCell Cell14 = row.createCell(13);    
        Cell14.setCellValue("报废属性");    
        Cell14.setCellStyle(titleStyle);    
        //14.保管人   
        HSSFCell Cell15 = row.createCell(14);    
        Cell15.setCellValue("保管人");    
        Cell15.setCellStyle(titleStyle);    
        //15.资金来源  
        HSSFCell Cell16 = row.createCell(15);    
        Cell16.setCellValue("资金来源");    
        Cell16.setCellStyle(titleStyle);    
        //16.供应商  
        HSSFCell Cell17 = row.createCell(16);    
        Cell17.setCellValue("供应商");    
        Cell17.setCellStyle(titleStyle);    
        //17.供应商电话  
        HSSFCell Cell18 = row.createCell(17);    
        Cell18.setCellValue("供应商电话");    
        Cell18.setCellStyle(titleStyle);    
        //18.备注说明  
        HSSFCell Cell19 = row.createCell(18);    
        Cell19.setCellValue("备注说明");    
        Cell19.setCellStyle(titleStyle);    
    }    
        
    /**   
     * 创建一列应用数据   
     * @param userinfosheet1   
     * @param userName   
     */    
    public void creatAppRow(HSSFSheet userinfosheet1,String titels,int naturalRowIndex){    
        //在第一行第一个单元格，插入下拉框    
        HSSFRow row = userinfosheet1.createRow(naturalRowIndex-1);  
        //所属分类  
        String [] list1={"办公计算设备","网络交换设备","安全防御设备","办公打印设备","复印扫描设备","监控预警设备","影音传输设备","存储备份设备","机房运维设备","办公软件","工具软件","专业软件","系统软件","其他软件"};   
        //分类名称  
        String [] list2={"台式电脑","便携式电脑","平板电脑","智能终端","服务器","其他办公计算设备","交换机","路由器","中继设备","负载均衡","其他交换设备","防火墙","防毒墙","邮件网关","入侵防御","数据库审计","其他安全防御设备",  
        "上网行为管理","VPN","针式打印机","激光式打印机","喷墨式打印机","一体机(打印、复印、传值)","票据打印机","其他办公打印设备","一体机","复印机","扫描仪","传真机","新风空调设备","UPS电源"};   
        //资产类型  
        String [] list3={"主设备","介质","配件"};  
        //计量单位  
        String [] list4={"台","套","个"};   
        //现存状态  
        String [] list5={"闲置","在用"};   
        //仓库状态  
        String [] list6={"在库","离库"};   
        //资产属性  
        String [] list7={"完好","损坏"};   
        //报废属性  
        String [] list8={"正常","报废"};   
          
          
        //0.所属分类  
        HSSFCell cell1 = row.createCell(0);    
        cell1.setCellValue("请选择");    
        cell1.setCellStyle(dataStyle);    
    
        //1.分类名称  
        HSSFCell cell2 = row.createCell(1);    
        cell2.setCellValue("请选择");    
        cell2.setCellStyle(dataStyle);    
    
        //2.资产类型     
        HSSFCell cell3 = row.createCell(2);    
        cell3.setCellValue("请选择");    
        cell3.setCellStyle(dataStyle);    
    
      //3.设备用途  
        HSSFCell cell4 = row.createCell(3);    
        cell4.setCellValue(titels);    
        cell4.setCellStyle(dataStyle);    
    
      //4.品牌  
        HSSFCell cell5 = row.createCell(4);    
        cell5.setCellValue(titels);    
        cell5.setCellStyle(dataStyle);    
        //5.型号  
        HSSFCell cell6 = row.createCell(5);    
        cell6.setCellValue(titels);    
        cell6.setCellStyle(dataStyle);    
        //6.数量  
        HSSFCell cell7 = row.createCell(6);    
        cell7.setCellValue(titels);    
        cell7.setCellStyle(dataStyle);    
        //7.计量单位  
        HSSFCell cell8 = row.createCell(7);    
        cell8.setCellValue("请选择");    
        cell8.setCellStyle(dataStyle);    
        //8.购入原值  
        HSSFCell cell9 = row.createCell(8);    
        cell9.setCellValue(titels);    
        cell9.setCellStyle(dataStyle);    
        //9.购入时间  
        HSSFCell cell10 = row.createCell(9);    
        cell10.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString());    
        cell10.setCellStyle(dataStyle);    
        //10.现存状态  
        HSSFCell cell11 = row.createCell(10);    
        cell11.setCellValue("请选择");    
        cell11.setCellStyle(dataStyle);    
        //11.仓库状态  
        HSSFCell cell12 = row.createCell(11);    
        cell12.setCellValue("请选择");    
        cell12.setCellStyle(dataStyle);    
        //12.资产属性  
        HSSFCell cell13 = row.createCell(12);    
        cell13.setCellValue("请选择");    
        cell13.setCellStyle(dataStyle);    
        //13.报废属性  
        HSSFCell cell14 = row.createCell(13);    
        cell14.setCellValue("请选择");    
        cell14.setCellStyle(dataStyle);    
        //14.保管人  
        HSSFCell cell15 = row.createCell(14);    
        cell15.setCellValue(titels);    
        cell15.setCellStyle(dataStyle);    
        //15.资金来源  
        HSSFCell cell16 = row.createCell(15);    
        cell16.setCellValue(titels);    
        cell16.setCellStyle(dataStyle);    
        //16.供应商  
        HSSFCell cell17 = row.createCell(16);    
        cell17.setCellValue(titels);    
        cell17.setCellStyle(dataStyle);    
        //17.供应商电话  
        HSSFCell cell18 = row.createCell(17);    
        cell18.setCellValue(titels);    
        cell18.setCellStyle(dataStyle);    
        //18.备注说明  
        HSSFCell cell19 = row.createCell(18);    
        cell19.setCellValue(titels);    
        cell19.setCellStyle(dataStyle);    
    
        //得到验证对象      
        DataValidation data_validation_list = this.getDataValidationByFormula(list1,naturalRowIndex,1); //从1开始下拉框处于第几列    
        //工作表添加验证数据      
        userinfosheet1.addValidationData(data_validation_list);  
          
        DataValidation data_validation_list2 = this.getDataValidationByFormula(list2,naturalRowIndex,2);    
        //工作表添加验证数据      
        userinfosheet1.addValidationData(data_validation_list2);  
          
        DataValidation data_validation_list3 = this.getDataValidationByFormula(list3,naturalRowIndex,3);    
        //工作表添加验证数据      
        userinfosheet1.addValidationData(data_validation_list3);  
          
        DataValidation data_validation_list8 = this.getDataValidationByFormula(list4,naturalRowIndex,8);    
        //工作表添加验证数据      
        userinfosheet1.addValidationData(data_validation_list8);    
          
        DataValidation data_validation_list11 = this.getDataValidationByFormula(list5,naturalRowIndex,11);    
        //工作表添加验证数据      
        userinfosheet1.addValidationData(data_validation_list11);   
          
        DataValidation data_validation_list12 = this.getDataValidationByFormula(list6,naturalRowIndex,12);    
        //工作表添加验证数据      
        userinfosheet1.addValidationData(data_validation_list12);   
          
        DataValidation data_validation_list13 = this.getDataValidationByFormula(list7,naturalRowIndex,13);    
        //工作表添加验证数据      
        userinfosheet1.addValidationData(data_validation_list13);  
          
        DataValidation data_validation_list14 = this.getDataValidationByFormula(list8,naturalRowIndex,14);    
        //工作表添加验证数据      
        userinfosheet1.addValidationData(data_validation_list14);    
         
    }    
        
    /**   
     * 使用已定义的数据源方式设置一个数据验证   
     * @param formulaString   
     * @param naturalRowIndex   
     * @param naturalColumnIndex   
     * @return   
     */    
    public DataValidation getDataValidationByFormula(String[] formulaString,int naturalRowIndex,int naturalColumnIndex){    
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
    
    /**   
     * 生成导出下拉框excel   
     * @param outPathStr 输出路径   
     */    
    public HSSFWorkbook ExportComboxExcel (HSSFWorkbook workbook) {    
        try {    
//            workbook = new HSSFWorkbook();//excel文件对象      
            HSSFSheet sheet1 = workbook.createSheet("sheet1");//工作表对象    
            //设置列头样式    
            this.setTitleCellStyles(workbook,sheet1);    
            //设置数据样式    
            this.setDataCellStyles(workbook,sheet1);    
            //创建一行列头数据    
            this.creatAppRowHead(sheet1,1);    
            //创建一行数据    
            for (int i = 2; i < 11; i++) {    
                this.creatAppRow(sheet1, "",i);    
            }    
    
            System.out.println("导出成功!");    
        } catch (Exception e) {    
            e.printStackTrace();    
        }  
        return workbook;  
    }
    
}
