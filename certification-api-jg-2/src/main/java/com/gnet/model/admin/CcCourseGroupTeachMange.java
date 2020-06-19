package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;


/**
 * 
 * @type model
 * @table cc_course_group_teach_mange
 * @author SY
 * @version 1.0
 * @date 2016年07月14日 11:10:53
 *
 */
@TableBind(tableName = "cc_course_group_teach_mange")
public class CcCourseGroupTeachMange extends DbModel<CcCourseGroupTeachMange> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcCourseGroupTeachMange dao = new CcCourseGroupTeachMange();

}
