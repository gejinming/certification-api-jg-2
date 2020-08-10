package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


/**
 * 
 * @type model
 * @table cc_course_group_course
 * @author SY
 * @version 1.0
 * @date 2016年07月14日 11:10:53
 *
 */
@TableBind(tableName = "cc_course_group_course")
public class CcCourseGroupCourse extends DbModel<CcCourseGroupCourse> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcCourseGroupCourse dao = new CcCourseGroupCourse();

	/*public List<CcCourse> findCourse(){

	}*/
}
