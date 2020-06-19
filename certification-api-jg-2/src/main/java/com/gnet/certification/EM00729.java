package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseOutlineExportLog;
import com.gnet.model.admin.CcReportBuildStatus;
import com.gnet.model.admin.User;
import com.google.common.collect.Maps;
import com.jfinal.token.TokenManager;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author YHL
 * @since 2017/10/30 下午11:47
 */
@Service("EM00729")
public class EM00729 extends BaseApi implements IApi {

    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        Map<String, Object> param = request.getData();

        Long versionId = paramsLongFilter(param.get("versionId"));

        if (versionId == null) {
            return renderFAIL("0881", response, header);
        }

        String token = request.getHeader().getToken();
        User user = UserCacheKit.getUser(token);
        Long userId = user.getLong("id");
        final String jobKey = versionId.toString() + "-" + userId.toString() + "-courseOutlineExport";

        CcCourseOutlineExportLog ccCourseOutlineExportLog = CcCourseOutlineExportLog.dao.findByJobKey(jobKey);
        if (ccCourseOutlineExportLog == null) {
            return renderFAIL("0886", response, header);
        }

        Map<String, Object> result = Maps.newHashMap();
        result.put("message", ccCourseOutlineExportLog.getStr("message"));
        result.put("isSuccess", Boolean.FALSE);
        result.put("exportStatus", ccCourseOutlineExportLog.getInt("export_status"));
        if (ccCourseOutlineExportLog.STATUS_TASK_SUCCESS.equals(ccCourseOutlineExportLog.getInt("export_status"))) {
            result.put("isSuccess", Boolean.TRUE);
            result.put("originFilePath", ccCourseOutlineExportLog.getStr("origin_file_path"));
            result.put("exportFileName", ccCourseOutlineExportLog.getStr("export_file_name"));
        }

        return renderSUC(result, response, header);
    }
}
