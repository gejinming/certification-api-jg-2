package com.gnet.certification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.User;
import com.gnet.object.UserOrderType;
import com.gnet.pager.Pageable;
import com.gnet.service.UserService;
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看系统用户列表
 * 
 * @author sll
 * 
 * @date 2016年6月3日18:26:45
 * 
 */
@Service("EM00004")
@Transactional(readOnly=true)
public class EM00004 extends BaseApi implements IApi {

    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {

        @SuppressWarnings("unchecked")
        Map<String, Object> param = request.getData();

        Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
        Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
        String orderProperty = paramsStringFilter(param.get("orderProperty"));
        String orderDirection = paramsStringFilter(param.get("orderDirection"));
        String loginName = paramsStringFilter(param.get("loginName"));
        String email = paramsStringFilter(param.get("email"));
        String name = paramsStringFilter(param.get("name"));
        String schoolId = paramsStringFilter(param.get("schoolId"));
        Pageable pageable = new Pageable(pageNumber, pageSize);

        // 排序处理
        try {
            ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, UserOrderType.class);
        } catch (NotFoundOrderPropertyException e) {
            return renderFAIL("0085", response, header);
        } catch (NotFoundOrderDirectionException e) {
            return renderFAIL("0086", response, header);
        }

        Map<String, Object> usersMap = Maps.newHashMap();
        //Page<User> userPage = User.dao.page(pageable, loginName, email, name);
        Page<User> userPage = User.dao.page(pageable, loginName, email, name,schoolId);
        List<User> userList = userPage.getList();
        // 判断是否分页
        if(pageable.isPaging()){
            usersMap.put("totalRow", userPage.getTotalRow());
            usersMap.put("totalPage", userPage.getTotalPage());
            usersMap.put("pageSize", userPage.getPageSize());
            usersMap.put("pageNumber", userPage.getPageNumber());
        }

        // 返回内容过滤
        List<User> list = new ArrayList<User>();
        UserService userService = SpringContextHolder.getBean(UserService.class);
        for (User temp : userList) {
            User user = new User();
            user.put("id", temp.getLong("id"));
            user.put("createDate", temp.getDate("create_date"));
            user.put("modifyDate", temp.getDate("modify_date"));
            user.put("departmentId", temp.getStr("department"));
            user.put("departmentName", temp.getStr("department_name"));
            user.put("loginName", UserService.handleLoginName(temp.getStr("loginName")));
            user.put("email", temp.getStr("email"));
            user.put("name", temp.getStr("name"));
            user.put("loginDate", temp.getDate("login_date"));
            user.put("type", temp.getInt("type"));
            user.put("typeName", DictUtils.findLabelByTypeAndKey("type", temp.getInt("type")));
            user.put("loginIp", temp.getStr("login_ip"));
            user.put("isEnabled", temp.getBoolean("is_enabled"));
            user.put("isLocked", temp.getBoolean("is_locked"));
            user.put("loginFailureCount", temp.getInt("login_failure_count"));
            user.put("schoolId",temp.getStr("schoolId"));
            // 获得用户的角色信息
            user.put("roles", userService.getUserRoles(temp.getStr("roles"), temp.getStr("name"), temp.getLong("id")));
            list.add(user);
        }

        usersMap.put("list", list);

        return renderSUC(usersMap, response, header);
    }
}
