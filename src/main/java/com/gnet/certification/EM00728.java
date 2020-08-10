package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.response.ServiceResponse;
import com.gnet.service.CcCourseOutlineExportService;
import com.gnet.utils.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

/**
 * @author YHL
 * @since 2017/10/27 下午5:45
 */
@Service("EM00728")
public class EM00728 extends BaseApi implements IApi {

    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        Map<String, Object> param = request.getData();
        String originFilePath = paramsStringFilter(param.get("originFilePath"));
        String exportFileName = paramsStringFilter(param.get("exportFileName"));

        CcCourseOutlineExportService courseOutlineExportService = SpringContextHolder.getBean(CcCourseOutlineExportService.class);

        ServiceResponse serviceResponse = courseOutlineExportService.download(originFilePath, exportFileName);

        if (serviceResponse.isSucc()) {
            response.setFileName(exportFileName);
            return renderWordFile((File) serviceResponse.getContent(), response, header);
        }

        return renderFAIL("0885", response, header);
    }
}
