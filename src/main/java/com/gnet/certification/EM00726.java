package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcCourseOutline;
import com.gnet.model.admin.CcVersion;
import com.gnet.object.CcCourseOutlineTypeOrderType;
import com.gnet.pager.Pageable;
import com.gnet.plugin.configLoader.ConfigUtils;
import com.gnet.plugin.poi.ExcelHelper;
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程大纲列表导出
 * @author xzl
 * @date 2017年9月30日
 */
@Service("EM00726")
@Transactional(readOnly=true)
public class EM00726 extends BaseApi implements IApi{

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 分页参数
		String orderProperty = paramsStringFilter(params.get("orderProperty"));
		String orderDirection = paramsStringFilter(params.get("orderDirection"));
		//课程名称
		String courseName = paramsStringFilter(params.get("courseName"));
		//大纲名称
		String name = paramsStringFilter(params.get("name"));
		Integer status = paramsIntegerFilter(params.get("status"));
		Long planId = paramsLongFilter(params.get("planId"));
		Long majorId = paramsLongFilter(params.get("majorId"));
		Integer grade = paramsIntegerFilter(params.get("grade"));
		if(params.containsKey("planId") && planId == null) {
		    return renderFAIL("1009", response, header, "planId的参数值非法");
		}

		if(params.containsKey("majorId") && majorId == null) {
			return renderFAIL("1009", response, header, "majorId的参数值非法");
		}
		if(params.containsKey("grade") && grade == null) {
			return renderFAIL("1009", response, header, "grade的参数值非法");
		}

		if(majorId != null && grade != null && planId == null){
			planId = CcVersion.dao.findNewestVersion(majorId, grade);
		}

		if(planId == null){
			return renderFAIL("0140", response, header);
		}


		Pageable pageable = new Pageable(null, null);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcCourseOutlineTypeOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		//查询并返回结果		
		Map<String, Object> result = new HashMap<>();
		Page<CcCourseOutline> page = CcCourseOutline.dao.pageByPlanId(pageable, planId, courseName, name, status);

		ExcelHelper helper = SpringContextHolder.getBean(ExcelHelper.class);
		List<CcCourseOutline> ccCourseOutlineList = page.getList();
		File modelFile = new File(String.format("%s%s%s", PathKit.getWebRootPath(), ConfigUtils.getStr("excel", "templatePath"), "courseOutlineImportTemplate.xls"));
		File outputFile = new File(String.format("%s%s%s", PathKit.getWebRootPath(), ConfigUtils.getStr("excel", "outputPath"), "大纲任务列表.xls"));

		helper.exportExcelFile("courseOutlineExcel", modelFile, outputFile, ccCourseOutlineList);

		//返回结果
		return renderFILE(outputFile, response, header);

	}

}
