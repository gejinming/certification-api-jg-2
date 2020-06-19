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
import com.gnet.model.admin.CcGraduateVersion;
import com.gnet.plugin.poi.ExcelHelper;
import com.gnet.service.CcGraduateService;
import com.gnet.service.FileService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Record;


/**
 * 毕业要求以及课程指标点excel导入上传解析接口
 * 
 * @author SY
 * @Date 2018年1月10日
 */
@Transactional(readOnly = false)
@Service("EM00901")
public class EM00901 extends BaseApi implements IApi {
	
	private static final Logger logger = Logger.getLogger(EM00901.class);
	
//	private CcGraduateService ccGraduateService;
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Long graduateVerId = paramsLongFilter(param.get("graduateVerId"));
		
		Object fileInfoObject = param.get("fileInfo");
		
		// 毕业要求版本编号
		if (graduateVerId == null) {
			return renderFAIL("0181", response, header);
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
		List<Record> graduateList;
		try {
			graduateList = SpringContextHolder.getBean(ExcelHelper.class).importToRecordList("graduateExcel", file);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(e.getMessage());
			}
			return renderFAIL("0088", response, header);
		}
		// 上传文件内容为空过滤
		if (graduateList.isEmpty()) {
			return renderFAIL("0336", response, header);
		}
		// 验证导入数据的唯一性和合法性
		Map<String, Object> result = Maps.newHashMap();
		List<Map<String, Object>> returnGraduateList = Lists.newArrayList();
		// 1. 验证当前版本是否存在
		CcGraduateVersion ccGraduateVersion = CcGraduateVersion.dao.findFilteredById(graduateVerId);
		if(ccGraduateVersion == null) {
			return renderFAIL("0184", response, header);
		}
		CcGraduateService ccGraduateService = SpringContextHolder.getBean(CcGraduateService.class);		
		ccGraduateService.validateImportList(graduateList, graduateVerId);
		// 返回结果
		for (Record record : graduateList) {
			Map<String, Object> object = Maps.newHashMap();
			// 通过graduateIndexNum和indIndexNum来确定是否属于同一个毕业要求或者指标点
			
			// 毕业要求参数
			object.put("graduateIndexNum", record.getStr("graduateIndexNum"));
			object.put("graduateContent", record.getStr("graduateContent"));
			object.put("graduateVerId", graduateVerId);
			
			// 指标点参数
			// 指标点内容
			object.put("indIndexNum", record.getStr("indIndexNum"));
			
			// 指标点课程联系表参数
			object.put("courseName", record.getStr("courseName"));
			object.put("courseId", record.getLong("courseId"));
			object.put("weight", record.getStr("weight"));
			
			// 错误参数
			object.put("reasons", record.get("reasons"));
			object.put("isError", record.getBoolean("isError"));
			returnGraduateList.add(object);
			
			// 原先数据。weight和courseName没变化，所以按照上面指标点课程联系表参数的来即可
			object.put("graduateIndexAndContent", record.getStr("graduateIndexAndContent"));
			object.put("indName", record.getStr("indName"));
		}
		
		result.put("returnGraduateList", returnGraduateList);
		
		return renderSUC(result, response, header);
	}
	
	
	
}