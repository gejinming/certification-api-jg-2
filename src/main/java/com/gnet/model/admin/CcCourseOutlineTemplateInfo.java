package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;

import java.util.List;

/**
 * @type model
 * @description 课程大纲模板基本信息
 * @table cc_course_outline_template_info
 * @author xzl
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course_outline_template_info")
public class CcCourseOutlineTemplateInfo extends DbModel<CcCourseOutlineTemplateInfo> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcCourseOutlineTemplateInfo dao = new CcCourseOutlineTemplateInfo();

}
