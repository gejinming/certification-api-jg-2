package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;

import java.util.List;


/**
 * 
 * @type model
 * @table cc_course_group_mange_group
 * @author SY
 * @version 1.0
 * @date 2016年07月14日 11:10:53
 *
 */
@TableBind(tableName = "cc_course_group_mange_group")
public class CcCourseGroupMangeGroup extends DbModel<CcCourseGroupMangeGroup> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcCourseGroupMangeGroup dao = new CcCourseGroupMangeGroup();



	/*public List<CcCourse> findCourse(){

	}*/
}
