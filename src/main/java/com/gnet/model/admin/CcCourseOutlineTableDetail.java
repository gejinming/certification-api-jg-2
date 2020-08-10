package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @type model
 * @description 课程教学大纲表格详细信息
 * @table cc_course_outline
 * @author xzl
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course_outline_table_detail")
public class CcCourseOutlineTableDetail extends DbModel<CcCourseOutlineTableDetail> {

    private static final long serialVersionUID = -3958125598237390759L;
    public final static CcCourseOutlineTableDetail dao = new CcCourseOutlineTableDetail();


    /**
     * 查看表每个格子详细信息
     * @param courseOutlineId
     * @return
     */
    public List<CcCourseOutlineTableDetail> findByCourseOutlineId(Long courseOutlineId) {
        List<Object> param = Lists.newArrayList();
        StringBuffer sql = new StringBuffer("select * from " + tableName + " ");
        sql.append("where course_outline_id = ? and is_del = ? ");
        sql.append("order by indexes, number, row_ordinal, column_ordinal ");
        param.add(courseOutlineId);
        param.add(DEL_NO);

        return find(sql.toString(), param.toArray());
    }
}
