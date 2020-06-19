package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;

import java.util.List;

/**
 * @type model
 * @description 课程教学大纲表頭
 * @table cc_course_outline_table_name
 * @author xzl
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course_outline_table_name")
public class CcCourseOutlineTableName extends DbModel<CcCourseOutlineTableName> {

    private static final long serialVersionUID = -3958125598237390759L;
    public final static CcCourseOutlineTableName dao = new CcCourseOutlineTableName();

    /**
     * 大纲表名称信息
     * @param courseOutlineId
     * @return
     */
    public List<CcCourseOutlineTableName> findByCourseOutlineId(Long courseOutlineId) {
        StringBuffer sql = new StringBuffer("select * from " + tableName + " ");
        sql.append("where course_outline_id = ? and is_del = ? ");
        sql.append("order by indexes, number ");
        return find(sql.toString(), courseOutlineId, DEL_NO);
    }
}
