package com.gnet.certification;

import com.gnet.FileInfo;
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
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * 文件管理-上传文件
 *
 * @author yuhailun
 * @date 2018/01/29
 * @description
 **/
@Transactional(isolation = Isolation.READ_COMMITTED)
@Service(value = "EM00952")
public class EM00952 extends BaseApi implements IApi {

    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        Map<String, Object> params = request.getData();

        Object fileObject = params.get("fileInfo");

        if (fileObject == null || !(fileObject instanceof FileInfo)) {
            return renderFAIL("2004", response, header);
        }

        FileInfo fileInfo = (FileInfo) fileObject;
        Long parentId = paramsLongFilter(params.get("parentId"));
        File file = fileInfo.getFile();

        CcFile parentFile = null;
        Boolean isFirst = false;
        Map<String, Object> returnMap = Maps.newHashMap();

        if (file == null) {
            return renderFAIL("2004", response, header);
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
        CcFileService ccFileService = SpringContextHolder.getBean(CcFileService.class);

        File uploadFile = ccFileService.uploadFile(majorId, parentFile == null ? null : parentFile.getStr("path"), file);

        if (uploadFile == null) {
            return renderFAIL("2005", response, header);
        }

        String uploadFileName = uploadFile.getName();
        String extension = FilenameUtils.getExtension(uploadFileName);

        CcFile ccFile = new CcFile();
        Date date = new Date();
        ccFile.set("create_date", date);
        ccFile.set("modify_date", date);
        ccFile.set("create_user", userId);
        ccFile.set("modify_user", userId);
        ccFile.set("major_id", majorId);
        ccFile.set("parent_id", parentId);
        ccFile.set("name", uploadFileName);
        ccFile.set("type", extension);
        ccFile.set("path", uploadFile.getAbsolutePath());
        ccFile.set("size", uploadFile.length());
        ccFile.set("is_file", true);
        ccFile.set("is_first", isFirst);
        ccFile.set("is_del", false);

        if (!ccFile.save()) {
            return renderFAIL("2005", response, header);
        }

        if (!isFirst) {
            List<String> parentPathList = ccFileService.getAllParentPath(majorId, parentFile.getStr("path"));
            if (!CcFile.dao.updateSize2(parentPathList, file.length(),parentId, true)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return renderFAIL("2005", response, header);
            }
        }

        if (!CcFileLog.dao.createLog(majorId, userId, String.format("上传文件-%s", uploadFile.getName()))) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return renderFAIL("2005", response, header);
        }

        returnMap.put("isSuccess", true);

        return renderSUC(returnMap, response, header);
    }
}
