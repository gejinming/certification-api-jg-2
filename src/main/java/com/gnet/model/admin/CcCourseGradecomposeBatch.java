package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


/**
 * @type model
 * @description 多批次列表操作，包括对数据的增删改查与列表
 * @table cc_course_gradecompose_batch
 * @author GJM
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course_gradecompose_batch")
public class CcCourseGradecomposeBatch extends DbModel<CcCourseGradecomposeBatch> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcCourseGradecomposeBatch dao = new CcCourseGradecomposeBatch();

	/*
	 * @param pageable
	 * @return com.jfinal.plugin.activerecord.Page<com.gnet.model.admin.CcCourseGradecomposeBatch>
	 * @author Gejm
	 * @description: 查看成绩组成元素多批次表列表分页
	 * @date 2020/7/6 16:13
	 */
	public Page<CcCourseGradecomposeBatch> page(Pageable pageable, Long courseGradeComposeId) {
		StringBuilder exceptSql = new StringBuilder("from " + CcCourseGradecomposeBatch.dao.tableName + " ");
		List<Object> params = Lists.newArrayList();

		exceptSql.append("where is_del = ? ");
		params.add(Boolean.FALSE);

		if (courseGradeComposeId != null) {
			exceptSql.append("and course_gradecompose_id = ? ");
			params.add(courseGradeComposeId);
		}

		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}

		return CcCourseGradecomposeBatch.dao.paginate(pageable, "select * ", exceptSql.toString(), params.toArray());
	}
	/*
	 * @param id
	 * @return com.gnet.model.admin.CcCourseGradecomposeBatch
	 * @author Gejm
	 * @description: 查找批次信息
	 * @date 2020/7/7 10:36
	 */
	public CcCourseGradecomposeBatch findBatch(Long id){
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select * from " + tableName +" where is_del = 0 and id=?");
		params.add(id);
		return findFirst(sql.toString(), params.toArray());
	}




}
