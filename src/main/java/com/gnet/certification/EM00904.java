package com.gnet.certification;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.FileInfo;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcPlanVersion;
import com.gnet.plugin.poi.ExcelHelper;
import com.gnet.service.CcCourseService;
import com.gnet.service.FileService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Record;


/**
 * 理论课程excel导入上传解析接口
 * 
 * @author SY
 * @Date 2018年1月11日
 */
@Transactional(readOnly = false)
@Service("EM00904")
public class EM00904 extends BaseApi implements IApi {
	
	private static final Logger logger = Logger.getLogger(EM00904.class);
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		// 培养计划版本编号
		Long planId = paramsLongFilter(param.get("planId"));
		
		Object fileInfoObject = param.get("fileInfo");
		
		// 培养计划版本编号
		if (planId == null) {
			return renderFAIL("0421", response, header);
		}
		
		// fileInfo合法性验证
		if (fileInfoObject == null || !(fileInfoObject instanceof FileInfo)) {
			return renderFAIL("0087", response, header);
		}
		FileInfo fileInfo = (FileInfo) fileInfoObject;
		FileService fileService = SpringContextHolder.getBean(FileService.class);
		// 上传失败验证
		if (!fileService.upload(fileInfo, Boolean.FALSE)) {
			return renderFAIL("0088", response, header);
		}
		// 获得上传的文件
		File file = new File(PathKit.getWebRootPath() + fileInfo.getTargetUrl());
		List<Record> courseList;
		try {
			courseList = SpringContextHolder.getBean(ExcelHelper.class).importToRecordList("theoryCourseExcel", file);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(e.getMessage());
			}
			return renderFAIL("0088", response, header, e.getMessage());
		}
		// 上传文件内容为空过滤
		if (courseList.isEmpty()) {
			return renderFAIL("0336", response, header);
		}
		// 验证导入数据的唯一性和合法性
		Map<String, Object> result = Maps.newHashMap();
		List<Map<String, Object>> returnCourseList = Lists.newArrayList();
		// 1. 验证当前版本是否存在
		CcPlanVersion ccPlanVersion = CcPlanVersion.dao.findFilteredById(planId);
		if(ccPlanVersion == null) {
			return renderFAIL("0422", response, header);
		}
		CcCourseService ccCourseService = SpringContextHolder.getBean(CcCourseService.class);		
		ccCourseService.validateImportTheoryList(courseList, planId);
		// 返回结果
		for (Record record : courseList) {
			Map<String, Object> object = Maps.newHashMap();
			
			object.put("hierarchyName", record.getStr("hierarchyName"));
			object.put("seconderyHierarchyName",record.get("seconderyHierarchyName"));
			object.put("propertyName", record.getStr("propertyName"));
			object.put("propertySecondaryName",record.get("propertySecondaryName"));
			object.put("code", record.getStr("code"));
			object.put("name", record.getStr("name"));
			object.put("englishName", record.getStr("englishName"));
			object.put("credit", record.getStr("credit"));
			object.put("allHours", record.getStr("allHours"));
			object.put("theoryHours", record.getStr("theoryHours"));
			object.put("experimentHours", record.getStr("experimentHours"));
			object.put("practiceHours", record.getStr("practiceHours"));
			object.put("operateComputerHours", record.getStr("operateComputerHours"));
			object.put("exercisesHours", record.getStr("exercisesHours"));
			object.put("dicussHours", record.getStr("dicussHours"));
			object.put("extracurricularHours", record.getStr("extracurricularHours"));
			object.put("weekHour", record.getStr("weekHour"));
			object.put("planTermClassNames", record.getStr("planTermClassNames"));
			object.put("planTermExamNames", record.getStr("planTermExamNames"));
			object.put("planId", planId);
			
			// 错误参数
			object.put("reasons", record.get("reasons"));
			object.put("isError", record.getBoolean("isError"));
			returnCourseList.add(object);
			
		}
		
		result.put("returnCourseList", returnCourseList);
		
		return renderSUC(result, response, header);
	}
	
	

}