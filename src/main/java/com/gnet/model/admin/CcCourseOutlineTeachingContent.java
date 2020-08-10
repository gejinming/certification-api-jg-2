package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;

import java.util.List;

/**
 * @type model
 * @description 课程教学大纲模块教学内容
 * @table cc_course_outline
 * @author xzl
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course_outline_teaching_content")
public class CcCourseOutlineTeachingContent extends DbModel<CcCourseOutlineTeachingContent> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcCourseOutlineTeachingContent dao = new CcCourseOutlineTeachingContent();


    /**
     * 课程大纲编号
     * @param courseOutlineId
     * @return
     */
    public List<CcCourseOutlineTeachingContent> findByCourseOutlineId(Long courseOutlineId) {
        StringBuffer sql = new StringBuffer("select * from " + tableName + " ");
        sql.append("where is_del = ? and course_outline_id = ? ");
        sql.append("order by indexes ");
        return find(sql.toString(), DEL_NO, courseOutlineId);
    }
}
