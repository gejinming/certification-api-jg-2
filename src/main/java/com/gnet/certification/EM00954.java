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
 * 文件管理-重命名文件
 *
 * @author yuhailun
 * @date 2018/01/30
 * @description
 **/
@Transactional(isolation = Isolation.READ_COMMITTED)
@Service(value = "EM00954")
public class EM00954 extends BaseApi implements IApi {

    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {

        Map<String, Object> params = request.getData();
        Long id = paramsLongFilter(params.get("id"));
        String name = paramsStringFilter(params.get("name"));

        Map<String, Object> returnMap = Maps.newHashMap();

        if (id == null) {
            return renderFAIL("2006", response, header);
        }

        if (StringUtils.isBlank(name)) {
            return renderFAIL("2009", response, header);
        }

        CcFile ccFile = CcFile.dao.findById(id);
        if (ccFile == null) {
            return renderFAIL("2007", response, header);
        }

        CcFileService ccFileService = SpringContextHolder.getBean(CcFileService.class);
        if (!ccFileService.isValidFileName(name)) {
            return renderFAIL("2012", response, header);
        }

        String token = request.getHeader().getToken();
        Long majorId = UserCacheKit.getMajorId(token);
        if (majorId == null) {
            return renderFAIL("0130", response, header);
        }

        User user = UserCacheKit.getUser(token);
        Long userId = user.getLong("id");

        Date date = new Date();

        File oldFile = new File(ccFile.getStr("path"));
        if (!oldFile.exists()) {
            return renderFAIL("2010", response, header);
        }
        String parentPath = oldFile.getParent();
        String newFileName = parentPath + File.separator + name;
        File newFile = new File(newFileName);

        String newName;
        if (ccFile.getBoolean("is_file")) {
            newName = name + "." + ccFile.getStr("type");
        } else {
            newName = name;
        }

        ccFile.set("name", newName);
        ccFile.set("modify_date", date);
        ccFile.set("path", newFile.getAbsolutePath());

        if (!ccFile.update()) {
            return renderFAIL("2010", response, header);
        }

        if (!CcFileLog.dao.createLog(majorId, userId, String.format("重命名文件-%s为%s", ccFile.getStr("name"), name))) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return renderFAIL("2010", response, header);
        }

        if (!oldFile.renameTo(newFile)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return renderFAIL("2010", response, header);
        }

        returnMap.put("isSuccess", true);

        return renderSUC(returnMap, response, header);

    }
}
