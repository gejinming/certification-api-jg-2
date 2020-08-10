package com.gnet.service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.IApi;
import com.gnet.certification.EM00727;
import com.gnet.model.admin.CcCourseOutline;
import com.gnet.model.admin.CcCourseOutlineExportLog;
import com.gnet.response.ServiceResponse;
import com.gnet.utils.SpringContextHolder;
import com.gnet.utils.WordTemplateKit;
import com.jfinal.log.Logger;
import org.apache.commons.lang3.StringUtils;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;

/**
 * @author YHL
 * @since 2017/10/30 下午4:13
 */
@Component("ccCourseOutlineExportService")
public class CcCourseOutlineExportService {

    private static final Logger logger = Logger.getLogger(CcPlanStatisticsService.class);

    public boolean export(List<CcCourseOutline> ccCourseOutlineList, Request request, Response response, ResponseHeader header, String method, Map<String, Object> resultMap) {

        Map<String, Map<String, Object>> allCourseDataMap = new HashMap<>();
        List<Map<String, Object>> failedCourseOutlineNameMapList = new ArrayList<>();
        List<Map<String, Object>> successCourseOutlineNameMapList = new ArrayList<>();

        IApi em00723 = SpringContextHolder.getBean("EM00723");

        Long courseOutlineIndex = 497394615L;
        Integer bookMarkId = 0;

        //生成文档
        String majorName = "";
        for (CcCourseOutline ccCourseOutline : ccCourseOutlineList) {
            Map<String, Object> data = new HashMap<>();
            majorName = ccCourseOutline.getStr("majorName");
            data.put("courseOutlineId", ccCourseOutline.getLong("id"));
            data.put("path", majorName);
            data.put("courseOutlineIndexOne", String.valueOf(courseOutlineIndex++));
            data.put("bookMarkIdOne", String.valueOf(bookMarkId++));
            data.put("courseOutlineName", ccCourseOutline.getStr("name"));

            request.setData(data);
            Response result = em00723.excute(request, response, header, method);
            Character successFlag = result.getHeader().getSuccflag();

            if (Response.FAIL.equals(successFlag)) {
                failedCourseOutlineNameMapList.add(data);
            }
            allCourseDataMap.put(ccCourseOutline.getStr("name"), data);
        }

        String pathName = WordTemplateKit.getExportPath() + File.separator + majorName;
        File dir = new File(pathName);

        List<File> fileList = new ArrayList<>();
        File[] fileArray = dir.listFiles();

        if (fileArray == null || fileArray.length == 0) {
            resultMap.put("errorMessage", "生成的课程大纲文件为空");
            logger.error("生成的课程大纲文件为空");
            return false;
        }

        // 筛选文件
        for (File tmpFile : fileArray) {
            if (tmpFile.getName().endsWith(WordTemplateKit.FILE_SUFFIX_DOCX)) {
                fileList.add(tmpFile);
            }
        }

        List<String> contentList = new ArrayList<>();

        for (File file : fileList) {

            String courseOutlineName = file.getName().substring(0, file.getName().length() - 5);

            WordprocessingMLPackage wordprocessingMLPackage = null;

            Boolean loadResult = true;
            try {
                wordprocessingMLPackage = Docx4J.load(file);
            } catch (Docx4JException e) {
                e.printStackTrace();
                loadResult = false;
            }

            Map<String, Object> stringObjectMap = allCourseDataMap.get(courseOutlineName);

            if (stringObjectMap == null) {
                continue;
            }

            if (loadResult) {
                String xml = wordprocessingMLPackage.getMainDocumentPart().getXML();

                int startIndex = xml.indexOf("<w:body");
                int endIndex = xml.indexOf("<w:sectPr");

                //获取文档的内容和名称
                if (startIndex != -1 && endIndex != -1) {
                    contentList.add(xml.substring(startIndex + 8, endIndex));
                    successCourseOutlineNameMapList.add(stringObjectMap);
                } else {
                    failedCourseOutlineNameMapList.add(stringObjectMap);
                }
            } else {
                failedCourseOutlineNameMapList.add(stringObjectMap);
            }

            if (file.exists()) {
                file.delete();
            }
        }

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("failedCourseOutlineMapList", failedCourseOutlineNameMapList);
        dataMap.put("successCourseOutlineMapList", successCourseOutlineNameMapList);
        dataMap.put("contentList", contentList);

        // 初始化 freemarker: 设置参数，设置模版路径， 生成文件导出路径
        boolean preProcessResult = WordTemplateKit.preProcess();
        if (!preProcessResult){
            resultMap.put("errorMessage", "初始化课程大纲模版失败");
            return false;
        }

        String exportFileName = WordTemplateKit.getExportFileName();

        File exportFile = WordTemplateKit.generateExportFile(exportFileName);
        if (exportFile == null) {
            resultMap.put("errorMessage", "生成导出文件失败");
            return false;
        }

        boolean processResult = WordTemplateKit.process(EM00727.TEMPLATE_FILE_NAME, exportFile, dataMap);
        if (!processResult) {
            if (exportFile.exists()) {
                exportFile.delete();
            }
            resultMap.put("errorMessage", "生成导出文件失败");
            return false;
        }

        String docxFileName = exportFileName.replace(WordTemplateKit.FILE_SUFFIX_XML, WordTemplateKit.FILE_SUFFIX_DOCX);

        File docxFile = WordTemplateKit.xmlFileToDocxFile(exportFile, docxFileName);

        if (docxFile == null) {
            return false;
        }

        resultMap.put("originFilePath", docxFile.getAbsolutePath());
        resultMap.put("exportFileName", majorName);

        return true;
    }

    /**
     *
     * @param originFilePath
     * @return
     */
    public ServiceResponse download(String originFilePath, String exportFileName) {
        if (StringUtils.isBlank(originFilePath) || StringUtils.isBlank(exportFileName)) {
            return ServiceResponse.error("文件路径不正确或者导出文件名称不争取");
        }

        File file = new File(originFilePath);
        if (!file.exists()) {
            return ServiceResponse.error("文件路径不存在");
        }

        return ServiceResponse.succ(file);
    }


    /**
     * 新建报表生成任务记录
     *
     * @param jobKey 任务名称
     * @param versionId 版本编号
     * @return
     */
    @Transactional(readOnly = false)
    public boolean createCourseOutlineExportLog(String jobKey, Long versionId, Long userId) {
        Date date = new Date();
        CcCourseOutlineExportLog ccCourseOutlineExportLog = new CcCourseOutlineExportLog();
        ccCourseOutlineExportLog.set("create_date", date);
        ccCourseOutlineExportLog.set("modify_date", date);
        ccCourseOutlineExportLog.set("job_key", jobKey);
        ccCourseOutlineExportLog.set("message", "准备开始导出！");
        ccCourseOutlineExportLog.set("export_status", CcCourseOutlineExportLog.STATUS_TASK_UN_CREATE);
        ccCourseOutlineExportLog.set("export_user_id", userId);
        ccCourseOutlineExportLog.set("version_id", versionId);

        return ccCourseOutlineExportLog.save();
    }

    /**
     *
     * @param jobKey
     * @param exportStatus
     * @param message
     * @return
     */
    @Transactional(readOnly = false)
    public boolean updateCourseOutlineExportLog(String jobKey, Integer exportStatus, String message) {
        return updateCourseOutlineExportLog(jobKey, exportStatus, message, null, null);
    }

    /**
     *
     * @param jobKey
     * @param exportStatus
     * @param message
     * @return
     */
    @Transactional(readOnly = false)
    public boolean updateCourseOutlineExportLog(String jobKey, Integer exportStatus, String message, String originFilePath, String exportFileName) {
        Date date = new Date();
        CcCourseOutlineExportLog ccCourseOutlineExportLog = CcCourseOutlineExportLog.dao.findByJobKey(jobKey);
        if (ccCourseOutlineExportLog == null) {
            logger.error(String.format("%s导出日志未找到", jobKey));
            return false;
        }

        ccCourseOutlineExportLog.set("export_status", exportStatus);
        ccCourseOutlineExportLog.set("message", message);
        ccCourseOutlineExportLog.set("origin_file_path", originFilePath);
        ccCourseOutlineExportLog.set("export_file_name", exportFileName);
        ccCourseOutlineExportLog.set("modify_date", date);
        return ccCourseOutlineExportLog.update();
    }

}
