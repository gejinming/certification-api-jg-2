package com.gnet.certification;

import com.gnet.FileInfo;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.service.FileService;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.kit.PathKit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * 富文本上传图片
 *
 * @author GJM
 * @Date 2020年10月15日
 */
@Transactional(readOnly = false)
@Service("EM01225")
public class EM01225 extends BaseApi implements IApi {

    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        Map<String, Object> param = request.getData();
		//上传图片
        Object fileInfoObject = param.get("fileInfo");
        // fileInfo合法性验证
        if (fileInfoObject == null || !(fileInfoObject instanceof FileInfo)) {
            return renderFAIL("0087", response, header);
        }
        FileInfo fileInfo = (FileInfo) fileInfoObject;
        FileService fileService = SpringContextHolder.getBean(FileService.class);
        // 上传失败验证
        if (!fileService.upload(fileInfo, Boolean.FALSE)) {
            return renderFAIL("0088", response, header);
        }
        String fileUrl = fileInfo.getTargetUrl();
        Map<String, Object> result = new HashMap<>();
        if (fileUrl!=null){
            result.put("isSuccess",true);
            result.put("fileUrl",fileUrl);
        }else{
            result.put("isSuccess",false);
        }
        return renderSUC(result, response, header);
	}

}