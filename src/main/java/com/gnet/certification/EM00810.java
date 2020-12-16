package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcIndication;
import com.gnet.object.CcIndicationOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *获取课程目标
 * @author: yuhailun
 * @date: 2017/11/22
 * @description:
 **/
@Service("EM00810")
public class EM00810 extends BaseApi implements IApi {

    @SuppressWarnings("unchecked")
    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        Map<String, Object> params = request.getData();
        // 获取数据：
        Integer pageNumber = paramsIntegerFilter(params.get("pageNumber"));
        Integer pageSize = paramsIntegerFilter(params.get("pageSize"));
        String orderProperty = paramsStringFilter(params.get("orderProperty"));
        String orderDirection = paramsStringFilter(params.get("orderDirection"));
        Long courseId = paramsLongFilter(params.get("courseId"));

        if(courseId == null){
            return renderFAIL("0250", response, header);
        }

        Pageable pageable = new Pageable(pageNumber, pageSize);

        // 排序处理
        try {
            ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcIndicationOrderType.class);
        } catch (NotFoundOrderPropertyException e) {
            return renderFAIL("0085", response, header);
        } catch (NotFoundOrderDirectionException e) {
            return renderFAIL("0086", response, header);
        }

        Map<String, Object> returnMap = new HashMap<String, Object>();
        //这里改动了下只要指标点关联得课程目标
        Page<CcIndication> page = CcIndication.dao.page(pageable, courseId,1);
        List<CcIndication> ccIndicationList = page.getList();

        // 判断是否分页
        if(pageable.isPaging()){
            returnMap.put("totalRow", page.getTotalRow());
            returnMap.put("totalPage", page.getTotalPage());
            returnMap.put("pageSize", page.getPageSize());
            returnMap.put("pageNumber", page.getPageNumber());
        }

        // 返回内容过滤
        List<Map<String, Object>> list = new ArrayList<>();
        for (CcIndication temp : ccIndicationList) {
            Map<String, Object> ccIndication = Maps.newHashMap();
            Integer sort = temp.getInt("sort");
            ccIndication.put("id", temp.getLong("id"));
            ccIndication.put("createDate", temp.getDate("create_date"));
            ccIndication.put("modifyDate", temp.getDate("modify_date"));
            ccIndication.put("sort", sort);
            ccIndication.put("courseId", temp.getLong("course_id"));
            ccIndication.put("courseName", temp.getStr("courseName"));
            ccIndication.put("content", temp.getStr("content"));
            ccIndication.put("expectedValue", temp.getBigDecimal("expected_value"));
            list.add(ccIndication);
        }

        returnMap.put("list", list);

        // 结果返回
        return renderSUC(returnMap, response, header);
    }
}
