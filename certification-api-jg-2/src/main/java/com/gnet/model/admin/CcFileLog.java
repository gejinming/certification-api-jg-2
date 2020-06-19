package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import java.util.Date;

/**
 * 文件操作日志
 *
 * @author yuhailun
 * @date 2018/01/30
 * @description
 **/
@TableBind(tableName = "cc_file_log")
public class CcFileLog extends DbModel<CcFile> {

    private static final long serialVersionUID = -3958125598237390759L;
    public static final CcFileLog dao = new CcFileLog();

    /**
     * 创建日志
     *
     * @param majorId 专业编号
     * @param userId  用户编号
     * @param content 日志内容
     * @return 是否成功
     */
    public Boolean createLog(Long majorId, Long userId, String content) {
        CcFileLog ccFileLog = new CcFileLog();
        ccFileLog.set("operate_user", userId);
        ccFileLog.set("create_date", new Date());
        ccFileLog.set("operate_content", content);
        ccFileLog.set("major_id", majorId);
        return ccFileLog.save();
    }

}
