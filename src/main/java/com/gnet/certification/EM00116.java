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
import com.gnet.service.CcTeacherService;
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
 * 导入教师接口
 * 
 * @author SY
 * @Date 2016年7月12日14:11:59
 */
@Transactional(readOnly = false)
@Service("EM00116")
public class EM00116 extends BaseApi implements IApi {
	
	private static final Logger logger = Logger.getLogger(EM00116.class);
	
	@Autowired
	private CcTeacherService ccTeacherService;
	
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
		List<Record> teacherList;
		try {
			teacherList = SpringContextHolder.getBean(ExcelHelper.class).importToRecordList("teacherExcel", file);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(e.getMessage());
			}
			return renderFAIL("0088", response, header);
		}
		// 上传文件内容为空过滤
		if (teacherList.isEmpty()) {
			return renderFAIL("0166", response, header);
		}
		// 验证导入数据的唯一性和合法性
		Map<String, Object> result = Maps.newHashMap();
		if (!ccTeacherService.validateImportList(teacherList, schoolId, office.getLong("id"))) {
			// 返回结果
			List<Map<String, Object>> errorList = Lists.newArrayList();
			for (Record record : teacherList) {
				Map<String, Object> object = Maps.newHashMap();
				String sex = StrKit.isBlank(record.getStr("sex")) ? null : DictUtils.findLabelByTypeAndKey("sex", Integer.parseInt(record.getStr("sex")));
				object.put("name", record.getStr("name"));
				object.put("code", record.getStr("code"));
				object.put("sex", sex);
				object.put("isEnabled", record.getStr("is_enabled"));
				object.put("department", record.getStr("department"));
				object.put("isLeave", record.getStr("is_leave"));
				object.put("reasons", record.get("reasons"));
				object.put("isError", record.getBoolean("isError"));
				errorList.add(object);
			}
			result.put("isSuccess", Boolean.FALSE);
			result.put("failRecords", errorList);
		} else {
			// 返回结果
			CcTeacherService ccTeacherService = SpringContextHolder.getBean(CcTeacherService.class);
			boolean isSuccess = ccTeacherService.saveTeachers(teacherList, schoolId);
			result.put("isSuccess", isSuccess);
		}
		return renderSUC(result, response, header);
	}
	
}