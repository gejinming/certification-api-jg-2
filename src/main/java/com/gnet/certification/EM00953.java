package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcFile;
import com.gnet.service.CcFileService;
import com.gnet.utils.SpringContextHolder;
import java.io.File;
import java.util.Map;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件管理-下载文件
 *
 * @author yuhailun
 * @date 2018/01/30
 * @description
 **/
@Service(value = "EM00953")
public class EM00953 extends BaseApi implements IApi {

    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        Map<String, Object> params = request.getData();
        Long id = paramsLongFilter(params.get("id"));

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

        CcFileService ccFileService = SpringContextHolder.getBean(CcFileService.class);
        String path = ccFile.getStr("path");
        ccFile.getBoolean("is_file");
        File file = new File("");
        if (ccFile.getBoolean("is_file")){
             file=new File(ccFile.getStr("path"));
        }else {


         file= ccFileService.downLoad(ccFile.getStr("path"), majorId, ccFile.getBoolean("is_file"),ccFile.get("type"));
        }


        if (file == null) {
            return renderFAIL("2008", response, header);
        }

        return renderFILE(file, response, header);
    }
}
