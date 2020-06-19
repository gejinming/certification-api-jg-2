package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;

/**
 * @type model
 * @description 课程大纲表格学时表
 * @table cc_course_outline_hours
 * @author xzl
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course_outline_hours")
public class CcCourseOutlineHours extends DbModel<CcCourseOutlineHours> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcCourseOutlineHours dao = new CcCourseOutlineHours();


}
