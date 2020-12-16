package com.gnet.certification;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcRankingLevel;
import org.apache.bcel.generic.ARRAYLENGTH;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.*;

/**
 * @program: certification-api-jg-2
 * @description: 考评点等级制修改
 * @author: Gjm
 * @create: 2020-12-08 15:15
 **/
@Service("EM01236")
public class EM01236 extends BaseApi implements IApi {
    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        HashMap<Object, Object> result = new HashMap<>();
        Map<String, Object> param = request.getData();
        JSONArray rankLevelArray = paramsJSONArrayFilter(param.get("rankLevel"));
        if (rankLevelArray == null ){
            return renderFAIL("2580", response, header);
        }
        Date date = new Date();
        ArrayList<CcRankingLevel> ccRankingLevels = new ArrayList<>();
        for(int i = 0; i < rankLevelArray.size(); i++) {
            CcRankingLevel ccRankingLevel = new CcRankingLevel();
            JSONObject map = (JSONObject) rankLevelArray.get(i);
            Long id = map.getLong("id");
            String levelName = map.getString("levelName");
            BigDecimal score = map.getBigDecimal("score");
            if (id == null ){
                return renderFAIL("2581", response, header);
            }
            if (levelName == null ){
                return renderFAIL("2582", response, header);
            }
            if (score == null ){
                return renderFAIL("2583", response, header);
            }
            ccRankingLevel.set("id",id);
            ccRankingLevel.set("level_name",levelName);
            ccRankingLevel.set("modify_date",date);
            ccRankingLevel.set("score",score);
            ccRankingLevels.add(ccRankingLevel);
        }
        if (ccRankingLevels.size()!=0){
            if (!CcRankingLevel.dao.batchUpdate(ccRankingLevels,"level_name,score,modify_date")){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.put("isSuccess", false);
                return renderSUC(result, response, header);
            }
        }
        result.put("isSuccess", true);

        return renderSUC(result, response, header);
    }
}
