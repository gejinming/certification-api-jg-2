package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcFile;
import com.gnet.model.admin.CcFileLog;
import com.gnet.model.admin.User;
import com.gnet.service.CcFileService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;
import java.io.File;
import java.util.Date;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * 文件管理-创建文件夹
 *
 * @author yuhailun
 * @date 2018/01/29
 * @description
 **/
@Service("EM00951")
@Transactional(isolation = Isolation.READ_COMMITTED)
public class EM00951 extends BaseApi implements IApi {

    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {

        Map<String, Object> params = request.getData();
        Long parentId = paramsLongFilter(params.get("parentId"));
        String directoryName = paramsStringFilter(params.get("name"));
        CcFile parentFile = null;
        Boolean isFirst = false;
        Map<String, Object> returnMap = Maps.newHashMap();

        if (StringUtils.isBlank(directoryName)) {
            return renderFAIL("2001", response, header);
        }

        CcFileService ccFileService = SpringContextHolder.getBean(CcFileService.class);
        if (!ccFileService.isValidFileName(directoryName)) {
            return renderFAIL("20012", response, header);
        }

        if (parentId != null) {
            parentFile = CcFile.dao.findById(parentId);
            if (parentFile == null) {
                return renderFAIL("2002", response, header);
            }
        } else {
            isFirst = true;
        }

        String token = request.getHeader().getToken();
        Long majorId = UserCacheKit.getMajorId(token);

        if (majorId == null) {
            return renderFAIL("0130", response, header);
        }

        User user = UserCacheKit.getUser(token);
        Long userId = user.getLong("id");

        File directoryFile = ccFileService.createDirectory(majorId, parentFile == null ? null : parentFile.getStr("path"), directoryName);

        if (directoryFile == null) {
            return renderFAIL("2003", response, header);
        }

        CcFile ccFile = new CcFile();
        Date date = new Date();
        ccFile.set("create_date", date);
        ccFile.set("modify_date", date);
        ccFile.set("create_user", userId);
        ccFile.set("modify_user", userId);
        ccFile.set("major_id", majorId);
        ccFile.set("parent_id", parentId);
        ccFile.set("name", directoryName);
        ccFile.set("path", directoryFile.getAbsolutePath());
        ccFile.set("size", 0);
        ccFile.set("is_file", false);
        ccFile.set("is_first", isFirst);
        ccFile.set("is_del", false);

        if (!ccFile.save()) {
            return renderFAIL("2003", response, header);
        }

        if (!CcFileLog.dao.createLog(majorId, userId, String.format("创建文件夹-%s", directoryFile.getName()))) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return renderFAIL("2003", response, header);
        }

        returnMap.put("isSuccess", true);

        return renderSUC(returnMap, response, header);
    }

}