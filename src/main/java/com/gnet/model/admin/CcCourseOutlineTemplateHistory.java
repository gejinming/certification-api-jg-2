package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;

/**
 * @type model
 * @description 课程教学大纲模板历史表操作
 * @table cc_course_outline_template_history
 * @author xzl
 *
 */
@TableBind(tableName = "cc_course_outline_template_history")
public class CcCourseOutlineTemplateHistory extends DbModel<CcCourseOutlineTemplateHistory> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcCourseOutlineTemplateHistory dao = new CcCourseOutlineTemplateHistory();

}
