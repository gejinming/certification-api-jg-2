package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;

/**
 * @author YHL
 * @since 2017/10/30 下午2:47
 */
@TableBind(tableName = "cc_course_outline_export_log")
public class CcCourseOutlineExportLog extends DbModel<CcCourseOutlineExportLog> {

    public final static CcCourseOutlineExportLog dao = new CcCourseOutlineExportLog();

    /**
     * 任务未创建状态，代码为0
     */
    public static final Integer STATUS_TASK_UN_CREATE = 0;

    /**
     * 任务创建失败，代码为1
     */
    public static final Integer STATUS_TASK_CREATE_FAILED = 1;

    /**
     * 任务创建成功，代码为2
     */
    public static final Integer STATUS_TASK_CREATE_SUCCESS = 2;

    /**
     * 大纲生成开始，代码为3
     */
    public static final Integer STATUS_TASK_START = 3;

    /**
     * 大纲生成失败，代码为4
     */
    public static final Integer STATUS_TASK_FAILED = 4;

    /**
     * 大纲生成成功，代码为5
     */
    public static final Integer STATUS_TASK_SUCCESS = 5;

    /**
     *
     * @param jobKey
     * @return
     */
    public CcCourseOutlineExportLog findByJobKey(String jobKey) {
        return findFirst("select * from " + tableName + " where job_key = ? order by create_date desc ", jobKey);
    }

}
