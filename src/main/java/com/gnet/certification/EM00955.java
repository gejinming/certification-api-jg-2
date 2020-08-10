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
import com.gnet.utils.FileUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * 文件管理-删除文件
 *
 * @author yuhailun
 * @date 2018/01/30
 * @description
 **/
@Transactional(isolation = Isolation.READ_COMMITTED)
@Service(value = "EM00955")
public class EM00955 extends BaseApi implements IApi {

    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        Map<String, Object> params = request.getData();
        Long id = paramsLongFilter(params.get("id"));

        Map<String, Object> returnMap = Maps.newHashMap();

        if (id == null) {
            return renderFAIL("2006", response, header);
        }

        CcFile ccFile = CcFile.dao.findById(id);
        if (ccFile == null) {
            return renderFAIL("2007", response, header);
        }

        String token = request.getHeader().getToken();
        Long majorId = UserCacheKit.getMajorId(token);
        if (majorId == null) {
            return renderFAIL("0130", response, header);
        }

        User user = UserCacheKit.getUser(token);
        Long userId = user.getLong("id");
        Date date = new Date();

        CcFileService ccFileService = SpringContextHolder.getBean(CcFileService.class);

        String path = ccFile.getStr("path");
        File file = new File(path);

        String trashPath = ccFileService.initTrashPath(majorId);

       /* if (!file.exists()) {
            return renderFAIL("2011", response, header);
        }*/

        String targetFileName;
        targetFileName = trashPath + File.separator + file.getName();


        ccFile.set("is_del", true);
        ccFile.set("modify_date", date);
        ccFile.set("trash_path", trashPath);

        if (!ccFile.update()) {
            return renderFAIL("2011", response, header);
        }

        Boolean isFirst = ccFile.getBoolean("is_first");
        Long size = ccFile.getLong("size");

        if (!isFirst) {
            List<String> parentPathList = ccFileService.getAllParentPath(majorId, path);
            int updateState = CcFile.dao.updateSize3( size, ccFile.getLong("parent_id"));
            if (updateState==0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return renderFAIL("2011", response, header);
            }
        }



        if (!CcFileLog.dao.createLog(majorId, userId, String.format("删除文件-%s", file.getName()))) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return renderFAIL("2011", response, header);
        }

        if (file.isFile()) {
            if (!FileUtils.copyFile(path, targetFileName) || !FileUtils.deleteFile(path)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return renderFAIL("2011", response, header);
            }
        } else {
            if (!FileUtils.copyDirectoryCover(path, targetFileName, true) || !FileUtils.deleteDirectory(path)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return renderFAIL("2011", response, header);
            }
        }

        returnMap.put("isSuccess", true);

        return renderSUC(returnMap, response, header);
    }
}
