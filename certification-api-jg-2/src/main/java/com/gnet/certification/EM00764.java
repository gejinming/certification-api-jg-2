package com.gnet.certification;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.utils.SpringContextHolder;
import com.gnet.utils.WordTemplateKit;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  考核分析法课程目标评价表生成
 * @author YHL
 * @since 2017/10/21 下午1:26
 */
@Service("EM00764")
public class EM00764 extends BaseApi implements IApi {

    private static final String TEMPLATE_FILE_NAME = "CourseEvaluate.ftl";

    private static final String PREFIX_FILE_NAME = "课程目标评价表";


    public static final Pattern NUMBER_PATTERN = Pattern.compile("[0-9]+");

    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {

        IApi apiEM00763 = SpringContextHolder.getBean("EM00763");

        Response result = apiEM00763.excute(request, response, header, method);

        Character successFlag = header.getSuccflag();
        if (Response.FAIL.equals(successFlag)) {
            return renderFAIL(header.getErrorcode(), response, header);
        }

        Object data = result.getData();

        Map<String, Object> dataMap = (Map<String, Object>) data;

        // 初始化 freemarker: 设置参数，设置模版路径， 生成文件导出路径
        boolean preProcessResult = WordTemplateKit.preProcess();
        if (!preProcessResult){
            return renderFAIL("0972", response, header);
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
                    if (content.contains("&")) {
                        content =  StringUtils.replace(content, "&", "&#38;");
                    }
                    if (content.contains("<")) {
                        content = StringUtils.replace(content, "<", "&#60;");
                    }
                    if (content.contains(">")) {
                        content = StringUtils.replace(content, ">", "&#62;");
                    }

                    return content;
                }
                return value;
            }
        };

        Map<String, Object> filtered = JSON.parseObject(JSON.toJSONString(data, valueFilter));

        List<String> educlassNames = (List<String>) filtered.get("educlassNames");
        if (educlassNames != null) {
            StringBuilder eduClassName = new StringBuilder();

            for (int i = 0; i < educlassNames.size(); i++) {
                if (i == 0) {
                    eduClassName.append(educlassNames.get(i));
                    continue;
                }
                eduClassName.append(",");
                Matcher matcher = NUMBER_PATTERN.matcher(educlassNames.get(i));
                if (matcher.find()) {
                    eduClassName.append(matcher.group());
                }
            }

            filtered.put("educlassNames", eduClassName.toString());
        }

        Map<String, Object> map = Maps.newHashMap();
        map.put("data", filtered);

        String exportFileName = WordTemplateKit.getExportFileName();

        File exportFile = WordTemplateKit.generateExportFile(exportFileName);

        if (exportFile == null) {
            return renderFAIL("0973", response, header);
        }

        boolean processResult = WordTemplateKit.process(TEMPLATE_FILE_NAME, exportFile, map);
        if (!processResult) {
            if (exportFile.exists()) {
                exportFile.delete();
            }
            return renderFAIL("0974", response, header);
        }

        String docxFileName = exportFileName.replace(WordTemplateKit.FILE_SUFFIX_XML, WordTemplateKit.FILE_SUFFIX_DOCX);

        File docxFile = WordTemplateKit.xmlFileToDocxFile(exportFile, docxFileName);

        if (exportFile.exists()) {
            exportFile.delete();
        }

        if (docxFile == null) {
            return renderFAIL("0974", response, header);
        }

        String fileName = PREFIX_FILE_NAME + "-" + dataMap.get("courseName").toString();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("originFilePath", docxFile.getAbsolutePath());
        resultMap.put("exportFileName", fileName);

        return renderSUC(resultMap, response, header);
    }
}
