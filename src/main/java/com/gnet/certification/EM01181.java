package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseType;
import com.jfinal.kit.StrKit;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: certification-api-jg-2
 * @description: 新增课程类别
 * @author: Gjm
 * @create: 2020-05-12 14:15
 **/
@Service("EM01181")
public class EM01181 extends BaseApi implements IApi {

    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        Map params = request.getData();
        String typeValue = paramsStringFilter(params.get("typeValue"));
        String typeName = paramsStringFilter(params.get("typeName"));
        Long planId = paramsLongFilter(params.get("planId"));
        // name不能为空信息的过滤
        if (StrKit.isBlank(typeValue)) {
            return renderFAIL("2506", response, header);
        }
        if (StrKit.isBlank(typeName)) {
            return renderFAIL("2507", response, header);
        }
        // name不能为重复信息的过滤
        if (CcCourseType.dao.isExisted(typeValue, planId)) {
            return renderFAIL("2504", response, header);
        }
        if (CcCourseType.dao.isExisted(typeName, planId)) {
            return renderFAIL("2503", response, header);
        }
        // planId不能为空信息的过滤
        if (planId == null) {
            return renderFAIL("0140", response, header);
        }
        HashMap<Object, Object> result = new HashMap<>();
        CcCourseType ccCourseType = new CcCourseType();
        ccCourseType.set("type_value",typeValue);
        ccCourseType.set("type_name",typeName);
        ccCourseType.set("plan_id",planId);
        ccCourseType.set("is_del", Boolean.FALSE);
        boolean isSuccess = ccCourseType.save();
        // 返回操作是否成功
        result.put("isSuccess", isSuccess);
        return renderSUC(result, response, header);

    }
}
