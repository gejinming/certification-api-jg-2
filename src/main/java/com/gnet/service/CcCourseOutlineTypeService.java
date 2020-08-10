package com.gnet.service;

import com.gnet.model.admin.*;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 课程教学大纲类型
 * 
 * @author xzl
 * 
 * @date 2017-08-22 17:19:14
 */
@Component("ccCourseOutlineTypeService")
public class CcCourseOutlineTypeService {


	/**
	 * 创建课程教学大纲类型
	 * @param name
	 * @param schoolId
	 * @return
	 */
	public Boolean saveOutlineType(String name, Long schoolId){

        Map<String, Object> param = new HashMap<>();
        param.put("name", name);
        param.put("school_id", schoolId);
        if(CcCourseOutlineType.dao.countFiltered(param) > 0L){
        	return true;
		}
		Date date = new Date();
		CcCourseOutlineType ccCourseOutlineType = new CcCourseOutlineType();
		ccCourseOutlineType.set("create_date", date);
		ccCourseOutlineType.set("modify_date", date);
		ccCourseOutlineType.set("name", name);
		ccCourseOutlineType.set("school_id", schoolId);
		ccCourseOutlineType.set("is_del", false);

		return ccCourseOutlineType.save();
	}

}
