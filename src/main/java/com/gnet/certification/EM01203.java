package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradeComposeDetail;
import com.gnet.model.admin.CcCourseGradecomposeBatch;
import com.gnet.model.admin.CcCourseGradecomposeStudetail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @program: certification-api-jg-2
 * @description: 修改和删除批次
 * @author: Gjm
 * @create: 2020-07-07 09:36
 **/
@Transactional(readOnly = false)
@Service("EM01203")
public class EM01203 extends BaseApi implements IApi {

    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        Map<String, Object> param = request.getData();
        Long id = paramsLongFilter(param.get("id"));
        String name = paramsStringFilter(param.get("name"));
        Map<String, Object> result = new HashMap();
        Integer delState = paramsIntegerFilter(param.get("delState"));
        if (id == null){
            return renderFAIL("1009", response, header);
        }
        Date date = new Date();
        ArrayList<CcCourseGradecomposeBatch> ccCourseGradecomposeBatches = new ArrayList<>();
        CcCourseGradecomposeBatch ccCourseGradecomposeBatch = new CcCourseGradecomposeBatch();
        boolean batchUpdate=Boolean.TRUE;
        //修改
        if (delState == 0){
            if (name == null || name.equals("")) {
                return renderFAIL("2557", response, header);
            }else {

                ccCourseGradecomposeBatch.set("id", id);
                ccCourseGradecomposeBatch.set("name", name);
                ccCourseGradecomposeBatch.set("modify_date",date);
                ccCourseGradecomposeBatches.add(ccCourseGradecomposeBatch);
                 batchUpdate = CcCourseGradecomposeBatch.dao.batchUpdate(ccCourseGradecomposeBatches, "modify_date,name");
            }
        }else {
            List<CcCourseGradeComposeDetail> ccCourseGradeComposeDetails = CcCourseGradeComposeDetail.dao.topicList(null, id);
            //判断该批次是否有题目
            if (ccCourseGradeComposeDetails.size()!=0){
                //判断是否存在有题目已经录入成绩
                List<CcCourseGradecomposeStudetail> existScore = CcCourseGradecomposeStudetail.dao.isExistScore(id);
                if (existScore.size() !=0){
                    return renderFAIL("2558", response, header);
                }else {
                    //删除题目
                   for (CcCourseGradeComposeDetail temp:ccCourseGradeComposeDetails){
                       temp.set("is_del",true);
                   }
                    boolean batchUpdate1 = CcCourseGradeComposeDetail.dao.batchUpdate(ccCourseGradeComposeDetails, "modify_date,is_del");
                    if (!batchUpdate1){
                        result.put("isSuccess", Boolean.FALSE);
                        return renderSUC(result, response, header);
                    }
                }
            }

            ccCourseGradecomposeBatch.set("id", id);
            ccCourseGradecomposeBatch.set("is_del", Boolean.TRUE);
            ccCourseGradecomposeBatch.set("modify_date",date);
            ccCourseGradecomposeBatches.add(ccCourseGradecomposeBatch);
            batchUpdate = CcCourseGradecomposeBatch.dao.batchUpdate(ccCourseGradecomposeBatches, "modify_date,is_del");
        }
        if (!batchUpdate){
            result.put("isSuccess", Boolean.FALSE);
            return renderSUC(result, response, header);
        }
        result.put("isSuccess", Boolean.TRUE);
        return renderSUC(result, response, header);
    }



}
