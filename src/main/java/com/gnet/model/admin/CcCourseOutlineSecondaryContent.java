package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;

import java.util.List;

/**
 * @type model
 * @description 课程教学大纲次要内容
 * @table cc_course_outline
 * @author xzl
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course_outline_secondary_content")
public class CcCourseOutlineSecondaryContent extends DbModel<CcCourseOutlineSecondaryContent> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcCourseOutlineSecondaryContent dao = new CcCourseOutlineSecondaryContent();


    /**
     *课程大纲次要内容
     * @param courseOutlineId
     * @return
     */
    public List<CcCourseOutlineSecondaryContent> findByCourseOutlineId(Long courseOutlineId) {
        StringBuffer sql = new StringBuffer("select * from " + tableName + " ");
        sql.append("where is_del = ? and course_outline_id = ? ");
        sql.append("order by indexes ");
        return find(sql.toString(), DEL_NO, courseOutlineId);
    }
}
