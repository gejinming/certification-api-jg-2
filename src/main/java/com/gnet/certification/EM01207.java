package com.gnet.certification;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcEduclass;
import com.gnet.utils.DetectHtml;
import com.gnet.utils.SpringContextHolder;
import com.gnet.utils.WordTemplateKit;
import com.google.common.collect.Maps;
import com.jfinal.log.Logger;
import org.apache.commons.lang3.StringUtils;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *  课程持续改进和评价表数据导出
 * @author GJM
 *
 */
@Service("EM01207")
public class EM01207 extends BaseApi implements IApi {



    public static final Logger logger = Logger.getLogger(EM01207.class);

    /**
     * 非中文
     */
    public static final Pattern NON_CHINESE_PATTERN = Pattern.compile("[^\\u4e00-\\u9fa5，；。“”、（）？！《》\r\n]+");

    /**
     * 中文
     */
    public static final Pattern CHINESE_PATTERN = Pattern.compile("[\\u4e00-\\u9fa5，；。“”、（）？！《》\r\n]+");

    /**
     * 换行
     */
    public static final Pattern RETURN_PATTERN = Pattern.compile("[\r\n]");

    private static final String KEY_MAIN_CONTENT = "mainContent";

    private static final String KEY_SECONDARY_CONTENT = "secondaryContent";

    private static final String KEY_BASIC_REQUIREMENTT = "basicRequirement";

    private static final String KEY_CONTENT = "content";

    public static final String PRE_HTML = "<html>\n" +
            "<head>\n" +
            "<style>\n" +
            " table {\n" +
            "    width: 100%;\n" +
            "    margin-bottom: 10px;\n" +
            "    border-collapse: collapse;\n" +
            "    display: table;\n" +
            "    border-spacing: 2px;\n" +
            "    border-color: #000;\n" +
            " }\n" +
            " tbody {\n" +
            "    display: table-row-group;\n" +
            "    vertical-align: middle;\n" +
            "    border-color: inherit;\n" +
            " }\n" +
            " tr {\n" +
            "    display: table-row;\n" +
            "    vertical-align: inherit;\n" +
            "    border-color: inherit;\n" +
            " }\n" +
            " td, th {\n" +
            "    padding: 5px 10px;\n" +
            "    border: 1px solid #000;\n" +
            " }\n" +
            " p {\n" +
            "    font-size: 14px;\n" +
            " }\n" +
            "</style>\n" +
            "</head>\n" +
            "<body>";


    public static final String BACK_HTML = "</body></html>";

    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        Map<String, Object> param = request.getData();
        Long classId = paramsLongFilter(param.get("classId"));
        //导出类型，1是改进方案 2 评价表
        Integer exporterType = paramsIntegerFilter(param.get("exporterType"));
        if (exporterType ==null || classId==null){
            return renderFAIL("0500", response, header);
        }
        //给word命名
        String name = null;
        CcEduclass educlassById = CcEduclass.dao.findEduclassById(classId);
        if (educlassById ==null){
            return renderFAIL("0501", response, header);
        }
        String courseName = educlassById.getStr("courseName");
        String educlassName = educlassById.getStr("educlass_name");
        //启用年级
        Integer enableGrade = educlassById.getInt("enable_grade");
        //引用模板
        String TEMPLATE_FILE_NAME = "";
        if (exporterType == 2){
            TEMPLATE_FILE_NAME="courseAchievementEvaluation.ftl";
            name=courseName+"-"+"课程目标和毕业要求指标点达成度评价表";
        }else{
            //启用年级2017之后用新版本持续报告
            if (enableGrade>=2017){
                TEMPLATE_FILE_NAME = "courseReport17.ftl";
            }else{
                TEMPLATE_FILE_NAME = "courseReport.ftl";
            }

            name=courseName+"("+educlassName+")"+"达成度评价与持续改进报告";
        }



        //*****
        IApi em01206 = SpringContextHolder.getBean("EM01206");
        Response em01206Result = em01206.excute(request, response, header, method);
        Character successFlag = header.getSuccflag();
        if (Response.FAIL.equals(successFlag)) {
            return renderFAIL(header.getErrorcode(), response, header);
        }

        Object em01206ResultData = em01206Result.getData();
        if (null == em01206ResultData) {
            return renderFAIL("0878", response, header);
        }


        // 初始化 freemarker: 设置参数，设置模版路径， 生成文件导出路径
        boolean preProcessResult = WordTemplateKit.preProcess();
        if (!preProcessResult){
            return renderFAIL("0883", response, header);
        }

        // 初始化 Docx4j
        final WordprocessingMLPackage wordMLPackage;
        try {
            wordMLPackage = WordprocessingMLPackage.createPackage();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
            return renderFAIL("0883", response, header);
        }

        // 内容过滤
        ValueFilter valueFilter = new ValueFilter() {
            @Override
            public Object process(Object object, String name, Object value) {
                if (value instanceof String && StringUtils.isNotBlank((String)value)) {

                    String content = (String) value;

                    if(content.contains("&nbsp;")){
                        content = StringUtils.replace(content, "&nbsp;", " ");
                    }

                    if (content.contains("<br>")) {
                        content = StringUtils.replace(content, "<br>", "<br/>");
                    }

                    if (content.contains("<hr>")) {
                        content = StringUtils.replace(content, "<hr>", "<hr/> ");
                    }
                    //检测指定参数是否是富文本
                    if (DetectHtml.isHtml(content)){
                        boolean isExisted = ("targetRequire".equals(name) || KEY_BASIC_REQUIREMENTT.equals(name) || KEY_SECONDARY_CONTENT.equals(name)) && (content.contains("\n") || content.contains("\r\n"));
                        if (isExisted) {
                            content = StringUtils.replace(content, "\r\n", "<br/>");
                            content = StringUtils.replace(content, "\n", "<br/>");
                        }

                        content = addHtml(content);
                        final XHTMLImporterImpl xhtmlImporter = new XHTMLImporterImpl(wordMLPackage);
                        MainDocumentPart mainDocumentPart = wordMLPackage.getMainDocumentPart();
                        mainDocumentPart.getContent().clear();

                        try {
                            List<Object> objectList = xhtmlImporter.convert(content, null);
                            mainDocumentPart.getContent().addAll(objectList);
                        } catch (Exception e) {
                            logger.error(e.getMessage() + ":" + content);
                            e.printStackTrace();
                            return "";
                        }

                        String xml = mainDocumentPart.getXML();
                        mainDocumentPart.getContent().clear();
                        int startIndex = xml.indexOf("w:body");
                        int endIndex = xml.indexOf("w:sectPr");

                        if (startIndex == -1 || endIndex == -1) {
                            return "";
                        }

                        return xml.substring(startIndex + 7, endIndex - 1);
                    }

                    if (content.contains("&")) {
                        content =  StringUtils.replace(content, "&", "&#38;");
                    }
                    if (content.contains("<")) {
                        content = StringUtils.replace(content, "<", "&#60;");
                    }
                    if (content.contains(">")) {
                        content = StringUtils.replace(content, ">", "&#62;");
                    }

                    if ("targetRequire".equals(name) || "achieveAnalysis".equals(name)
                            || "problemModified".equals(name) || "teachModified".equals(name)
                            || "achieveResult".equals(name)  || "courseModified".equals(name)
                            || "courseImprovement".equals(name) || "problemContent".equals(name)
                            || "courseInfo".equals(name) || "teacherMothed".equals(name) || "assessMothed".equals(name)
                            || "testAnalysis".equals(name) || "courseLearTarget".equals(name) || "personAchieveAnalyze".equals(name)) {
                        return parseText(content);
                    }

                    return content;
                }
                return value;
            }
        };

        Map<String, Object> filtered = JSON.parseObject(JSON.toJSONString(em01206ResultData,valueFilter));


        Map<String, Object> map = Maps.newHashMap();
        map.put("data", filtered);

        String exportFileName = WordTemplateKit.getExportFileName();

        File exportFile = WordTemplateKit.generateExportFile(exportFileName);
        if (exportFile == null) {
            return renderFAIL("0879", response, header);
        }

        boolean processResult = WordTemplateKit.process(TEMPLATE_FILE_NAME, exportFile, map);
        if (!processResult) {
            if (exportFile.exists()) {
                exportFile.delete();
            }
            return renderFAIL("0879", response, header);
        }

        String fileName = name == null ? "评价表" : name;
        Map<String, Object> resultMap = new HashMap<>();

        String docxFileName = exportFileName.replace(WordTemplateKit.FILE_SUFFIX_XML, WordTemplateKit.FILE_SUFFIX_DOCX);

        /*if (StringUtils.isNotBlank(specifiedPath)) {
            docxFileName = WordTemplateKit.getExportFileName(WordTemplateKit.getExportPath(), specifiedPath, fileName, WordTemplateKit.FILE_SUFFIX_DOCX);
        }*/

        File docxFile = WordTemplateKit.xmlFileToDocxFile(exportFile, docxFileName);

        if (exportFile.exists()) {
            exportFile.delete();
        }

        if (docxFile == null) {
            return renderFAIL("0879", response, header);
        }

        resultMap.put("originFilePath", docxFile.getAbsolutePath());
        resultMap.put("exportFileName", fileName);

        return renderSUC(resultMap, response, header);

    }


    /**
     * 给富文本添加样式
     * @param content
     * @return
     */
    private String addHtml(String content){
        return PRE_HTML + content + BACK_HTML;
    }

    /**
     *  是否有回车
     * @param text
     * @return
     */
    private boolean isExistReturn(String text) {
        Matcher matcher = RETURN_PATTERN.matcher(text);
        return matcher.find();
    }

    /**
     * 解析文本中单个段落的中、英文、数字
     * @param paragraph
     * @return
     */
    private List<Map<String, Object>> parseSingleParagraph(String paragraph){
        Matcher nonChineseMather = NON_CHINESE_PATTERN.matcher(paragraph);
        Matcher chineseMather = CHINESE_PATTERN.matcher(paragraph);

        //用于排序
        Map<Integer, Map<String, Object>> sortMap = new TreeMap<>();

        //句子集合
        List<Map<String, Object>> sentenceList = new ArrayList<>();
        while (nonChineseMather.find()) {
            Map<String, Object> map = new HashMap<>();
            map.put("isNonChinese", 1);
            map.put("content", nonChineseMather.group());
            sortMap.put(nonChineseMather.start(), map);
        }
        while (chineseMather.find()) {
            Map<String, Object> map = new HashMap<>();
            map.put("isNonChinese", 0);
            map.put("content", chineseMather.group());
            sortMap.put(chineseMather.start(), map);
        }
        for (Integer key : sortMap.keySet()) {
            sentenceList.add(sortMap.get(key));
        }

        return sentenceList;
    }

    /**
     *  解析文本中的中、英文、数字
     * @param text
     * @return
     */
    private Map<String, Object> parseText(String text){

        Map<String, Object> content = new HashMap<>();
        //段落集合
        List<List<Map<String, Object>>> paragraphDataList = new ArrayList<>();

        content.put("isNonChinese", 1);

        if (isExistReturn(text)) {
            Matcher returnPattern = RETURN_PATTERN.matcher(text);

            //分割成段落
            int startIndex = 0;
            int endIndex;
            List<String> paragraphList = new ArrayList<>();
            while (returnPattern.find()) {
                endIndex = returnPattern.start();
                String paragraph = text.substring(startIndex, endIndex);
                paragraphList.add(paragraph);
                startIndex = returnPattern.end();
            }
            paragraphList.add(text.substring(startIndex));

            //循环处理每一个段落
            for (String paragraph : paragraphList) {
                paragraphDataList.add(parseSingleParagraph(paragraph));
            }

        } else  {
            paragraphDataList.add(parseSingleParagraph(text));
        }

        content.put("paragraphDataList", paragraphDataList);

        return content;
    }

}
