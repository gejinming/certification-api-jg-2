package com.gnet.certification;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.FileInfo;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.Office;
import com.gnet.plugin.poi.ExcelHelper;
import com.gnet.service.CcStudentService;
import com.gnet.service.FileService;
import com.gnet.utils.DictUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Record;


/**
 * 导入文件上传接口
 * 
 * @author wct
 * @Date 2016年6月29日
 */
@Transactional(readOnly = false)
@Service("EM00207")
public class EM00207 extends BaseApi implements IApi {
	
	private static final Logger logger = Logger.getLogger(EM00207.class);
	
	@Autowired
	private CcStudentService ccStudentService;
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Long appointSchoolId = paramsLongFilter(param.get("appointSchoolId"));
		
		Object fileInfoObject = param.get("fileInfo");
		String token = request.getHeader().getToken();
		Long schoolId = UserCacheKit.getSchoolId(token);
		Office office = UserCacheKit.getDepartmentOffice(token);
		
		// 优先选择指定学校编号
		if (appointSchoolId != null) {
			schoolId = appointSchoolId;
		}
		// 学校不存在过滤
		if (schoolId == null) {
			return renderFAIL("0084", response, header);
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
		List<Record> studentList;
		try {
			studentList = SpringContextHolder.getBean(ExcelHelper.class).importToRecordList("studentExcel", file);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(e.getMessage());
			}
			return renderFAIL("0088", response, header);
		}
		// 上传文件内容为空过滤
		if (studentList.isEmpty()) {
			return renderFAIL("0336", response, header);
		}
		// 验证导入数据的唯一性和合法性
		Map<String, Object> result = Maps.newHashMap();
		List<Map<String, Object>> returnStudentList = Lists.newArrayList();
		ccStudentService.validateImportList(studentList, schoolId, office.getLong("id"));
		// 返回结果
		for (Record record : studentList) {
			Map<String, Object> object = Maps.newHashMap();
			String sexName = StrKit.isBlank(record.getStr("sex")) ? null : DictUtils.findLabelByTypeAndKey("sex", Integer.parseInt(record.getStr("sex")));
			String statusName = StrKit.isBlank(record.getStr("status")) ? null : DictUtils.findLabelByTypeAndKey("studentStatue", Integer.parseInt(record.getStr("status")));
			object.put("name", record.getStr("name"));
			object.put("studentNo", record.getStr("studentNo"));
			object.put("sex", record.getStr("sex"));
			object.put("sexName", sexName);
			object.put("idCard", record.getStr("idCard"));
			object.put("className", record.getStr("className"));
			object.put("matriculateDate", record.getStr("matriculateDate"));
			object.put("status", record.getStr("status"));
			object.put("statusName", statusName);
			object.put("reasons", record.get("reasons"));
			object.put("isError", record.getBoolean("isError"));
			returnStudentList.add(object);
		}
		
		result.put("returnStudentList", returnStudentList);
		
		return renderSUC(result, response, header);
	}
	
	
	
}