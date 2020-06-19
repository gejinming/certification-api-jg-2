package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;

import java.util.List;

/**
 * @type model
 * @description 课程教学大纲表头信息
 * @table cc_course_outline
 * @author xzl
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course_outline_header")
public class CcCourseOutlineHeader extends DbModel<CcCourseOutlineHeader> {

    private static final long serialVersionUID = -3958125598237390759L;
    public final static CcCourseOutlineHeader dao = new CcCourseOutlineHeader();

    /**
     * 表头类型：文本
     */
    public static final Integer TYPE_TEXT = 1;

    /**
     * 表头类型：数字
     */
    public static final Integer TYPE_NUMBER = 2;

    /**
     * 表头类型：学时小计
     */
    public static final Integer TYPE_HOURS_SUBTOTAL = 3;

    /**
     * 表头类型：课程目标选择
     */
    public static final Integer TYPE_INDICATION = 4;
    /**
     * 表头类型：课程毕业指标点选择
     */
    public static final Integer TYPE_INDICATIONPOINT = 5;

    /**
     * 表头关联学时类型：理论学时
     */
    public static final Integer THEORY_HOURS = 1;

    /**
     * 表头关联学时类型：上机学时
     */
    public static final Integer OPERATECOMPUTER_HOURS   = 2;


    /**
     * 表头关联学时类型：实验学时
     */
    public static final Integer EXPERIMENT_HOURS  = 3;


    /**
     * 表头关联学时类型：实践学时
     */
    public static final Integer PRACTICE_HOURS = 4;

    /**
     * 表头关联学时类型：课内学时
     */
    public static final Integer CURRICULAR_HOURS = 5;

    /**
     * 表头关联学时类型：天数
     */
    public static final Integer DAYS = 6;

    /**
     * 表头关联学时类型：周数
     */
    public static final Integer WEEKS = 7;

    /**
     * 表头关联学时类型：研讨学时
     */
    public static final Integer DICUSS_HOURS = 8;

    /**
     * 表头关联学时类型：习题学时
     */
    public static final Integer EXERCISES_HOURS = 9;




    /**
     * 大纲模块表头信息
     * @param courseOutlineId
     * @return
     */
    public List<CcCourseOutlineHeader> findByCourseOutlineId(Long courseOutlineId) {
        StringBuffer sql = new StringBuffer("select * from " + tableName + " ");
        sql.append("where is_del = ? and course_outline_id  = ? ");
        sql.append("order by indexes, number, column_ordinal ");
        return find(sql.toString(), DEL_NO, courseOutlineId);
    }
}
