package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * @type model
 * @description 课程教学大纲模块表
 * @table cc_course_outline
 * @author xzl
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course_outline_module")
public class CcCourseOutlineModule extends DbModel<CcCourseOutlineModule> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcCourseOutlineModule dao = new CcCourseOutlineModule();


    /**
     * 课程大纲模块信息
     * @param courseOutlineId
     *         大纲编号
     * @return
     */
    public List<CcCourseOutlineModule> findCourseOutlineModules(Long courseOutlineId) {
           StringBuffer sql = new StringBuffer("select indexes, title, main_content, is_exist_main_content, is_exist_secondary_content, is_exist_teaching_content, is_exist_table, " +
                   "is_main_content_support, is_secondary_content_support, is_teaching_content_support, indications from " + tableName + " ");
           sql.append("where is_del = ? and course_outline_id = ? ");
           sql.append("order by indexes ");
           return  find(sql.toString(), DEL_NO, courseOutlineId);
    }

}
