package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.job.impl.CourseOutlineExportJobImpl;
import com.gnet.model.admin.CcCourseOutline;
import com.gnet.model.admin.CcCourseOutlineExportLog;
import com.gnet.model.admin.User;
import com.gnet.plugin.quartz.QuartzKit;
import com.gnet.plugin.quartz.callback.ITaskMonitor;
import com.gnet.service.CcCourseOutlineExportService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;
import com.jfinal.log.Logger;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成专业版本下的所有审核通过的，待审核的课程大纲
 * @author YHL
 * @since 2017/10/17 下午3:12
 */
@Service("EM00727")
public class EM00727 extends BaseApi implements IApi {

    public static final String TEMPLATE_FILE_NAME = "CourseOutlineAll.ftl";

    private static final Logger logger = Logger.getLogger(EM00102.class);

    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {

        Map<String, Object> params = request.getData();
        Long versionId = paramsLongFilter(params.get("versionId"));

        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> jobData = Maps.newHashMap();

        if (versionId == null) {
            return renderFAIL("0881", response, header);
        }

        //查询专业版本编号下的所有课程大纲
        List<CcCourseOutline> ccCourseOutlineList = CcCourseOutline.dao.findByVersionIdAndStatus(versionId, new Integer[]{CcCourseOutline.STATUS_AUDIT_PASS, CcCourseOutline.STATUS_PENDING_AUDIT});
        if (ccCourseOutlineList.isEmpty()) {
            return renderFAIL("0884", response, header);
        }
        jobData.put("ccCourseOutlineList", ccCourseOutlineList);
        jobData.put("request", request);
        jobData.put("response", response);
        jobData.put("header", header);
        jobData.put("method", method);

        final CcCourseOutlineExportService ccCourseOutlineExportService = SpringContextHolder.getBean(CcCourseOutlineExportService.class);

        //获取当前用户
        String token = request.getHeader().getToken();
        User user = UserCacheKit.getUser(token);
        Long userId = user.getLong("id");
        final String jobKey = versionId.toString() + "-" + userId.toString() + "-courseOutlineExport";
        jobData.put("jobKey", jobKey);

        try {

            // 上一个任务还未结束，不开启新的导出任务
            if (QuartzKit.checkJobExists(jobKey)) {
                return renderSUC(Boolean.TRUE, response, header);
            }

            // 创建课程大纲导出日志
            if (!ccCourseOutlineExportService.createCourseOutlineExportLog(jobKey, versionId, userId)) {
                if (logger.isErrorEnabled()) {
                    logger.error(String.format("创建课程大纲导出日志失败，jobKey=%s，versionId=%s，userId=%s", jobKey, versionId, userId));
                }
                return renderFAIL("0885", response, header);
            }

            QuartzKit.createTaskStartNow(jobKey, CourseOutlineExportJobImpl.class, jobData, SimpleScheduleBuilder.simpleSchedule(), new ITaskMonitor() {

                public void taskStart() {
                    ccCourseOutlineExportService.updateCourseOutlineExportLog(jobKey, CcCourseOutlineExportLog.STATUS_TASK_START,"执行课程大纲导出任务开始");
                    logger.info("执行课程大纲导出任务开始");
                }

                public void taskFinish() {

                }

                public void taskFail() {
                    ccCourseOutlineExportService.updateCourseOutlineExportLog(jobKey, CcCourseOutlineExportLog.STATUS_TASK_FAILED,"执行课程大纲导出任务失败");
                    logger.info("执行课程大纲导出任务失败");
                }

            });
        } catch (SchedulerException e) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("创建课程大纲导出任务失败，versionId = %s", versionId), e);
                ccCourseOutlineExportService.updateCourseOutlineExportLog(jobKey, CcCourseOutlineExportLog.STATUS_TASK_CREATE_FAILED,"创建课程大纲导出任务失败");
                return renderFAIL("0885", response, header);
            }
        }

        ccCourseOutlineExportService.updateCourseOutlineExportLog(jobKey, CcCourseOutlineExportLog.STATUS_TASK_CREATE_SUCCESS,"创建课程大纲导出任务成功");
        logger.info("创建课程大纲导出任务成功");

        resultMap.put("isSuccess", Boolean.TRUE);
        return renderSUC(resultMap, response, header);
    }
}
