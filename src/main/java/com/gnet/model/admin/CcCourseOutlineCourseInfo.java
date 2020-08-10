package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * @type model
 * @description 课程教学大纲与课程相关的基本信息
 * @table cc_courseoutline_courseinfo
 * @author xzl
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course_outline_course_info")
public class CcCourseOutlineCourseInfo extends DbModel<CcCourseOutlineCourseInfo> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcCourseOutlineCourseInfo dao = new CcCourseOutlineCourseInfo();

	/*public List<CcCourseOutlineCourseInfo> findCourseOutlinInfo(Long courseOutlineId){
		new StringBuilder("");
	}*/
}
