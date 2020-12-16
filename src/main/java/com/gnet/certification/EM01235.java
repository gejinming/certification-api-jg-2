package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcRankingLevel;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.DictUtils;
import com.gnet.utils.SpringContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.*;

/**
 * @program: certification-api-jg-2
 * @description: 考评点等级制查询
 * @author: Gjm
 * @create: 2020-12-08 15:15
 **/
@Service("EM01235")
public class EM01235 extends BaseApi implements IApi {
    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        HashMap<Object, Object> result = new HashMap<>();
        Map<String, Object> param = request.getData();
        Long majorId = paramsLongFilter(param.get("majorId"));
        Integer level = paramsIntegerFilter(param.get("level"));
        if (majorId == null){
            return renderFAIL("0130", response, header);
        }
        Date date = new Date();
        List<CcRankingLevel> rankingLevelList = CcRankingLevel.dao.findLevelList(majorId, level);
        //如果数据为空，就把默认的数据填入数据库重新查询，主要针对在此次更新之前已经创建好的专业
        if (rankingLevelList.size()==0){
            BigDecimal twos[] = {CcRankingLevel.LEVEL_TOW_A_VALUE, CcRankingLevel.LEVEL_TOW_B_VALUE};
            BigDecimal fives[] = {CcRankingLevel.LEVEL_FIVE_A_VALUE, CcRankingLevel.LEVEL_FIVE_B_VALUE, CcRankingLevel.LEVEL_FIVE_C_VALUE, CcRankingLevel.LEVEL_FIVE_D_VALUE, CcRankingLevel.LEVEL_FIVE_E_VALUE};
            IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
            //TODO 2020.12.08 GJM 新增专业后默认两个个考评等级制，五级和二级
            ArrayList<CcRankingLevel> ccRankingLevels = new ArrayList<>();
            //二级制
            for(int i = 1; i<= CcRankingLevel.LEVEL_TOW; i++){
                CcRankingLevel ccRankingLevel = new CcRankingLevel();
                ccRankingLevel.set("id", idGenerate.getNextValue());
                ccRankingLevel.set("create_date", date);
                ccRankingLevel.set("modify_date", date);
                ccRankingLevel.set("level_name", DictUtils.findLabelByTypeAndKey("evaluteLevelTowValue", i));
                ccRankingLevel.set("score", twos[i-1]);
                ccRankingLevel.set("major_id", majorId);
                ccRankingLevel.set("level", CcRankingLevel.LEVEL_TOW);
                ccRankingLevel.set("is_del", Boolean.FALSE);
                ccRankingLevels.add(ccRankingLevel);
            }
            //五级制
            for(int i=1; i<= CcRankingLevel.LEVEL_FIVE; i++ ){
                CcRankingLevel ccRankingLevel = new CcRankingLevel();
                ccRankingLevel.set("id", idGenerate.getNextValue());
                ccRankingLevel.set("create_date", date);
                ccRankingLevel.set("modify_date", date);
                ccRankingLevel.set("level_name", DictUtils.findLabelByTypeAndKey("evaluteLevelFiveValue", i));
                ccRankingLevel.set("score", fives[i-1]);
                ccRankingLevel.set("major_id", majorId);
                ccRankingLevel.set("level", CcRankingLevel.LEVEL_FIVE);
                ccRankingLevel.set("is_del", Boolean.FALSE);
                ccRankingLevels.add(ccRankingLevel);

            }
            if (!CcRankingLevel.dao.batchSave(ccRankingLevels)){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                // 返回操作是否成功
                result.put("isSuccess", false);
                return renderSUC(result, response, header);
            }
            List<CcRankingLevel> rankingLevelLists = CcRankingLevel.dao.findLevelList(majorId, level);
            result.put("data",rankingLevelLists);
        }else{
            result.put("data",rankingLevelList);
        }

        result.put("isSuccess", true);

        return renderSUC(result, response, header);
    }
}
