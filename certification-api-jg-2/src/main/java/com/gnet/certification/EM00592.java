package com.gnet.certification;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcTeacher;
import com.gnet.utils.DateUtils;
import com.google.common.collect.Maps;


/**
 * 统计同一专业下各个年龄段的人数
 * 
 * @author XZL
 * @Date 2016年7月20日
 */
@Service("EM00592")
public class EM00592 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		
		Long majorId = paramsLongFilter(param.get("majorId"));
		
		if(majorId == null){
			return renderFAIL("0130", response, header);
		}
			
		Date date = new Date();
		//35岁的出生日期
		Date thirtyFiveBirthday = DateUtils.addYears(date, CcTeacher.THIRTYFIVE);
		//45岁的出生日期
		Date fortyFiveBirthday = DateUtils.addYears(date, CcTeacher.FORTYFIVE);
		//60岁的出生日期
		Date sixtyBirthday = DateUtils.addYears(date, CcTeacher.SIXTY);
		
		//教师总数
		Long sum = CcTeacher.dao.getTeacherNum(majorId);
		//没有出生日期的教师，定义为其他
		Long otherNum = CcTeacher.dao.getBirthdayIsNullNum(majorId);
		//35岁以下的数量
		Long underThirtyFive = CcTeacher.dao.getSectionNumber(null, thirtyFiveBirthday, majorId);
		//大于35岁小于45岁
		Long aboveThirtyFiveUnderFortyFive = CcTeacher.dao.getSectionNumber(thirtyFiveBirthday, fortyFiveBirthday, majorId);
		//大于45小于60		
		Long aboveFortyFiveUnderSixty = CcTeacher.dao.getSectionNumber(fortyFiveBirthday, sixtyBirthday, majorId);			
		//大于60
		Long aboveSixty = CcTeacher.dao.getSectionNumber(sixtyBirthday, null, majorId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		  
		//返回内容
		List<Map<String, Object>> list = new ArrayList<>();
		
		Map<String, Object> teacher1 = Maps.newHashMap(),
		                    teacher2 = Maps.newHashMap(),
		                    teacher3 = Maps.newHashMap(),
		                    teacher4 = Maps.newHashMap(),
		                    teacher5 = Maps.newHashMap();
		                    
		teacher1.put("ageName", CcTeacher.UNDERTHIRTYFIVE);
		teacher1.put("number", underThirtyFive);
		teacher2.put("ageName", CcTeacher.ABOVETHIRTYFIVEUNDERFORTYFIVE);
		teacher2.put("number", aboveThirtyFiveUnderFortyFive);
		teacher3.put("ageName", CcTeacher.ABOVEFORTYFIVEUNDERSIXTY);
		teacher3.put("number", aboveFortyFiveUnderSixty);
		teacher4.put("ageName", CcTeacher.ABOVESIXTY);
		teacher4.put("number", aboveSixty);
		teacher5.put("ageName", CcTeacher.OTHER);
		teacher5.put("number", otherNum);
		
		list.add(teacher1);
		list.add(teacher2);
		list.add(teacher3);
		list.add(teacher4);
		list.add(teacher5);
		
		result.put("list", list);
		result.put("sum", sum);
		return renderSUC(result, response, header);
	}
	
}
