package com.gnet.model.admin;

import java.util.ArrayList;
import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * @type model
 * @description 成绩组成表操作，包括对数据的增删改查与列表
 * @table cc_gradecompose
 * @author SY
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_gradecompose")
public class CcGradecompose extends DbModel<CcGradecompose> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcGradecompose dao = new CcGradecompose();
	
	/**
	 * 检查课程层次名称是否唯一，在某个版本下
	 * @param name
	 * 			新名称
	 * @param originValue
	 * 			原先名称（可以为空）
	 * @param majorId
	 * 			专业编号
	 * @return
	 */
	public boolean isExisted(String name, String originValue, Long majorId) {
		if (StrKit.notBlank(originValue)) {
			return Db.queryLong("select count(1) from " + tableName + " where name = ? and name != ? and is_del = ? and major_id = ? ",  name, originValue, Boolean.FALSE, majorId) > 0;
		} else {
			return Db.queryLong("select count(1) from " + tableName + " where name = ? and is_del = ? and major_id = ? ",  name, Boolean.FALSE, majorId) > 0;
		}
	}
	
	/**
	 * 检查课程层次名称是否唯一，在某个版本下
	 * @param name
	 * 			新名称
	 * @param majorId
	 * 			专业编号
	 * @return
	 */
	public boolean isExisted(String name, Long majorId) {
		return isExisted(name, null, majorId);
	}

	/**
	 * 查看专业列表分页
	 * @param pageable
	 * @param majorId
	 * 			学校编号
	 * @return
	 */
	public Page<CcGradecompose> page(Pageable pageable, Long majorId) {
		
		String selectString = "select cg.* ";
		StringBuilder exceptSql = new StringBuilder("from " + CcGradecompose.dao.tableName + " cg ");
		List<Object> params = Lists.newArrayList();
		// 增加条件，为非软删除的
		exceptSql.append("where cg.is_del=? ");
		params.add(Boolean.FALSE);
		// 删选条件
		if(majorId != null) {
			exceptSql.append("and cg.major_id = ? ");
			params.add(majorId);
		}
		
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcGradecompose.dao.paginate(pageable, selectString, exceptSql.toString(), params.toArray());

	}

	/**
	 * 通过教师开课课程的编号列表查找所有的成绩组成
	 * @param teacherCourseIds
	 * @return
	 */
	public List<CcGradecompose> findByTeacherCourseIds(Long[] oldTeacherCourseIds) {
		if(oldTeacherCourseIds == null || oldTeacherCourseIds.length == 0) {
			return new ArrayList<>();
		}
		List<Object> params = Lists.newArrayList();
		StringBuilder exceptSql = new StringBuilder("select cg.*, ccg.id courseGradecomposeId, ccg.teacher_course_id teacherCourseId ");
		exceptSql.append("from " + tableName + " cg ");
		exceptSql.append("left join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.gradecompose_id = cg.id and ccg.is_del = ? ");
		params.add(Boolean.FALSE);
		// 增加条件，为非软删除的
		exceptSql.append("where cg.is_del=? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and ccg.teacher_course_id in( " + CollectionKit.convert(oldTeacherCourseIds, ",") +  " ) ");
		
		return find(exceptSql.toString(), params.toArray());
	}


	/**
	 * 通过开课课程编号获取成绩组成
	 * @param teacherCourseId
	 * @return cc_gradecompose
	 * 			成绩组成
	 * @author SY 
	 * @version 创建时间：2017年8月23日 下午8:40:52 
	 */
	public List<CcGradecompose> findGradecomposeByTeacherCourseId(Long teacherCourseId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder exceptSql = new StringBuilder();
		exceptSql.append("select cg.*, ccg.percentage, ccg.id courseGradecomposeId ");
		exceptSql.append("from " + tableName + " cg ");
		exceptSql.append("left join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.gradecompose_id = cg.id and ccg.is_del = ? ");
		params.add(Boolean.FALSE);
		// 增加条件，为非软删除的
		exceptSql.append("where cg.is_del=? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and ccg.teacher_course_id = ? ");
		params.add(teacherCourseId);
		exceptSql.append("group by cg.id ");
		return find(exceptSql.toString(), params.toArray());
	}


}
