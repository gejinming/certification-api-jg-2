package com.gnet.certification;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;
import com.jfinal.kit.PathKit;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
 *  大纲导出
 *  @author YHL
 *
 */
@Service("EM00722")
public class EM00722 extends BaseApi implements IApi {

    public static final String FILE_PATH = "generate";

    public static final String FILE_NAME = "generateWord.xml";

    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {

        Map<String, Object> param = request.getData();

        boolean isParseHtml = true;
        if(isParseHtml){
            IApi EM00723Service = SpringContextHolder.getBean("EM00723");
            return EM00723Service.excute(request, response, header, method);
        }

        IApi EM00703Service = SpringContextHolder.getBean("EM00703");
        Response result = EM00703Service.excute(request, response, header, method);

        Object data = result.getData();

        if (null == data) {
            return renderFAIL("0878", response, header);
        }

        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");

        String classPath =  PathKit.getRootClassPath();;

        String templatePath = classPath + File.separator + "word";

        Template template = null;
        try {
            configuration.setDirectoryForTemplateLoading(new File(templatePath));
            configuration.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
            template = configuration.getTemplate("template.ftl", "utf-8");
        } catch (IOException e) {
            return renderFAIL("0879", response, header);
        }

        String pathName = classPath + File.separator + FILE_PATH;

        String fileName = pathName + File.separator + FILE_NAME;

        File outFilePath = new File(pathName);

        File outFile = new File(fileName);

        if(!outFilePath.exists()){
            outFilePath.mkdirs();
        }

        if (outFile.exists()) {
            outFile.delete();
        }

        // 非法字符校验
        ValueFilter valueFilter = new ValueFilter() {
            @Override
            public Object process(Object object, String name, Object value) {
                if (value instanceof String && ((String) value).contains("&")) {
                    return StringUtils.replace((String) value, "&", "");
                } else if (value instanceof String) {
                    return StringEscapeUtils.escapeXml11((String) value);
                }
                return value;
            }
        };
        Map<String, Object> filtered = JSON.parseObject(JSON.toJSONString(data, valueFilter));

        FileOutputStream fileOutputStream = null;

        Writer outWriter = null;
        Map<String, Object> map = Maps.newHashMap();
        map.put("data", filtered);
        try {
            outFile.createNewFile();
            fileOutputStream = new FileOutputStream(outFile);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "utf-8");
            outWriter = new BufferedWriter(outputStreamWriter);
            template.process(map, outWriter);
        } catch (Exception e) {
            return renderFAIL("0879", response, header);
        } finally {
            try {
                if (outWriter != null){
                    outWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (fileOutputStream != null){
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Map<String, Object> dataMap = (Map<String, Object>) data;
        response.setFileName(dataMap.get("name").toString());

        return renderWordFile(outFile, response, header);
    }


}
