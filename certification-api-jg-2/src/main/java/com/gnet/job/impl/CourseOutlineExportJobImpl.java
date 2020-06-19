package com.gnet.job.impl;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.model.admin.CcCourseOutline;
import com.gnet.model.admin.CcCourseOutlineExportLog;
import com.gnet.service.CcCourseOutlineExportService;
import com.gnet.utils.SpringContextHolder;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author YHL
 * @since 2017/10/30 下午3:21
 */
public class CourseOutlineExportJobImpl implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        List<CcCourseOutline> ccCourseOutlineList = (List<CcCourseOutline>) dataMap.get("ccCourseOutlineList");
        Request request = (Request) dataMap.get("request");
        Response response = (Response) dataMap.get("response");
        ResponseHeader header = (ResponseHeader) dataMap.get("header");
        String method = (String) dataMap.get("method");
        String jobKey = (String) dataMap.get("jobKey");

        Map<String, Object> resultMap = new HashMap<>();
        CcCourseOutlineExportService courseOutlineExportService = SpringContextHolder.getBean(CcCourseOutlineExportService.class);
        if (courseOutlineExportService.export(ccCourseOutlineList, request, response, header, method, resultMap)) {
            courseOutlineExportService.updateCourseOutlineExportLog(jobKey, CcCourseOutlineExportLog.STATUS_TASK_SUCCESS, "执行课程大纲导出任务成功", (String) resultMap.get("originFilePath"), (String) resultMap.get("exportFileName"));
        } else {
            courseOutlineExportService.updateCourseOutlineExportLog(jobKey, CcCourseOutlineExportLog.STATUS_TASK_FAILED,  (String) resultMap.get("errorMessage"));
        }

    }
}
