package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcFile;
import com.gnet.object.CcFileOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Page;
import org.springframework.stereotype.Service;

/**
 * 文件管理-查询文件列表
 *
 * @author yuhailun
 * @date 2018/01/29
 * @description
 **/
@Service("EM00950")
public class EM00950 extends BaseApi implements IApi {

    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        Map<String, Object> params = request.getData();

        Integer pageNumber = paramsIntegerFilter(params.get("pageNumber"));
        Integer pageSize = paramsIntegerFilter(params.get("pageSize"));
        String fileName = paramsStringFilter(params.get("name"));
        String fileType = paramsStringFilter(params.get("type"));
        Boolean isDel = paramsBooleanFilter(params.get("isDel"));
        Long parentId = paramsLongFilter(params.get("parentId"));
        String orderProperty = paramsStringFilter(params.get("orderProperty"));
        String orderDirection = paramsStringFilter(params.get("orderDirection"));

        if (isDel == null) {
            isDel = false;
        }

        String token = request.getHeader().getToken();
        Long majorId = UserCacheKit.getMajorId(token);
        if (majorId == null) {
            return renderFAIL("0130", response, header);
        }

        // 进行分页
        Map<String, Object> returnMap = Maps.newHashMap();
        Pageable pageable = new Pageable(pageNumber, pageSize);
        Page<CcFile> ccFilepage = CcFile.dao.page(pageable, majorId, parentId, fileName, fileType, isDel);
        List<CcFile> fileList = ccFilepage.getList();
        if (pageable.isPaging()) {
            returnMap.put("totalRow",ccFilepage.getTotalRow());
            returnMap.put("totalPage", ccFilepage.getTotalPage());
            returnMap.put("pageSize", ccFilepage.getPageSize());
            returnMap.put("pageNumber", ccFilepage.getPageNumber());
        }
        // 排序处理
        try {
            ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcFileOrderType.class);
        } catch (NotFoundOrderPropertyException e) {
            return renderFAIL("0085", response, header);
        } catch (NotFoundOrderDirectionException e) {
            return renderFAIL("0086", response, header);
        }

        //List<CcFile> fileList = CcFile.dao.find(pageable, majorId, parentId, fileName, fileType, isDel);


        List<Map<String, Object>> list = Lists.newArrayList();
        for (CcFile ccFile : fileList) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", ccFile.get("id"));
            map.put("modifyDate", ccFile.getDate("modify_date"));
            map.put("modifyUserName", ccFile.getStr("modifyUserName"));
            map.put("name", ccFile.getStr("name"));
            map.put("type", ccFile.getStr("type"));
            map.put("size", ccFile.get("size"));
            map.put("isFile", ccFile.get("is_file"));
            map.put("isFirst", ccFile.get("is_first"));
            list.add(map);
        }

        returnMap.put("list", list);

        return renderSUC(returnMap, response, header);
    }
}
