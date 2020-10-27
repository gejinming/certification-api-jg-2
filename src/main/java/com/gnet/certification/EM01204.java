package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradecomposeBatch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: certification-api-jg-2
 * @description: 编辑批次信息
 * @author: Gjm
 * @create: 2020-07-07 10:30
 **/
@Transactional(readOnly = false)
@Service("EM01204")
public class EM01204 extends BaseApi implements IApi {

    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        Map<String, Object> param = request.getData();
        Long id = paramsLongFilter(param.get("id"));
        if (id == null){
            return renderFAIL("1009", response, header);
        }
        HashMap result = new HashMap();
        CcCourseGradecomposeBatch batch = CcCourseGradecomposeBatch.dao.findBatch(id);
        result.put("name",batch.get("name"));
        result.put("remark",batch.get("remark"));
        result.put("id",id);
        return renderSUC(result, response, header);

    }
}
