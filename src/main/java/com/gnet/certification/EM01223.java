package com.gnet.certification;

import com.gnet.FileInfo;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.api.sign.Result;
import com.gnet.service.CcAimportPlanService;
import com.gnet.service.FileService;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: certification-api-jg-2
 * @description: 导入培养计划解析保存
 * @author: Gjm
 * @create: 2020-10-09 18:02
 **/
@Transactional(readOnly = false)
@Service("EM01223")
public class EM01223 extends BaseApi implements IApi {

    public static final Logger logger = Logger.getLogger(EM01223.class);

    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        Map<String, Object> param = request.getData();
        // 培养计划版本编号
        Long planId = paramsLongFilter(param.get("planId"));
        Object fileInfoObject = param.get("fileInfo");
        //课程类型
        Integer type = paramsIntegerFilter(param.get("type"));
        // 结果返回
        Map<String, Object> result = new HashMap<>();
        // 培养计划版本编号
        if (planId == null) {
            return renderFAIL("0421", response, header);
        }
        if (type == null) {
            return renderFAIL("0563", response, header);
        }

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
        String fileUrl = PathKit.getWebRootPath() + fileInfo.getTargetUrl();
        //解析数据以及处理
        CcAimportPlanService ccAimportPlanService = SpringContextHolder.getBean(CcAimportPlanService.class);
        Result manageCourseresult = ccAimportPlanService.readExcelToObj(PathKit.getWebRootPath() + fileInfo.getTargetUrl(), planId, type);
        if (manageCourseresult.getFlag()){
            result.put("isSuccess",true);
            result.put("message","培养计划导入成功请重新更新！");
        }else {
            result.put("isSuccess",false);
            result.put("message",manageCourseresult.getMessage());
        }
        return renderSUC(result, response, header);
    }
}
