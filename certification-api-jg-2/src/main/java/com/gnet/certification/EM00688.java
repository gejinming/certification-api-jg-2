package com.gnet.certification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gnet.object.CcCourseGradeComposeStudetailOrderType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcCourseGradecomposeStudetail;
import com.gnet.object.CcCourseGradeComposeDetailOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 某道题目所有输入成绩的学生列表
 * 
 * @author xzl
 * 
 * @date 2016年12月26日
 * 
 */
@Service("EM00688")
@Transactional(readOnly=true)
public class EM00688 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
        Long id = paramsLongFilter(param.get("id"));
        
        if(id == null){
        	renderFAIL("0450", response, header);
        }
		
		// 进行分页
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcCourseGradeComposeStudetailOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		Map<String, Object> ccCourseGradecomposeStudetailMap = Maps.newHashMap();		
		Page<CcCourseGradecomposeStudetail> ccCourseGradecomposeStudetailPage = CcCourseGradecomposeStudetail.dao.page(pageable, id);
		List<CcCourseGradecomposeStudetail> ccCourseGradecomposeStudetailList = ccCourseGradecomposeStudetailPage.getList();
		// 判断是否分页
		if(pageable.isPaging()){
			ccCourseGradecomposeStudetailMap.put("totalRow", ccCourseGradecomposeStudetailPage.getTotalRow());
			ccCourseGradecomposeStudetailMap.put("totalPage", ccCourseGradecomposeStudetailPage.getTotalPage());
			ccCourseGradecomposeStudetailMap.put("pageSize", ccCourseGradecomposeStudetailPage.getPageSize());
			ccCourseGradecomposeStudetailMap.put("pageNumber", ccCourseGradecomposeStudetailPage.getPageNumber());
		}
		
		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
        for(CcCourseGradecomposeStudetail temp : ccCourseGradecomposeStudetailList){
        	Map<String, Object> ccCourseGradecomposeStudetail = Maps.newHashMap();
        	ccCourseGradecomposeStudetail.put("id", temp.getLong("id"));
        	ccCourseGradecomposeStudetail.put("detailId", temp.getLong("detail_id"));
        	ccCourseGradecomposeStudetail.put("studentId", temp.getLong("studet_id"));
        	ccCourseGradecomposeStudetail.put("studentName", temp.getStr("name"));
        	ccCourseGradecomposeStudetail.put("studentNo", temp.getStr("student_no"));
        	ccCourseGradecomposeStudetail.put("score", temp.getBigDecimal("score"));
        	list.add(ccCourseGradecomposeStudetail);
        }
        
		ccCourseGradecomposeStudetailMap.put("list", list);
		return renderSUC(ccCourseGradecomposeStudetailMap, response, header);
	}
}
