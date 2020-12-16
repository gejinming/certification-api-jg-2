package com.gnet.model.admin;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

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
 * @description 课程表操作，包括对数据的增删改查与列表
 * @table cc_course
 * @author SY
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course")
public class CcCourse extends DbModel<CcCourse> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcCourse dao = new CcCourse();

	/**
	 * 课程类型--理论
	 */
	public final static Integer TYPE_THEORY = 1; 
	/**
	 * 课程类型--实践
	 */
	public final static Integer TYPE_PRACTICE = 2;

	/**
	 * 课程代码是否存在判断
	 * @param columnName
	 * 			数据库的字段
	 * @param planId 
	 * @param columnValue
	 * @param excludeCourseId 课程编号
	 * @return
	 */
	public boolean isExisted(String columnName, String columnValue, Long planId, Long excludeCourseId) {
		if (excludeCourseId != null) {
			return Db.queryLong("select count(1) from cc_course where plan_id = ? and " + columnName + " = ? and id != ? and is_del = ? ", planId, columnValue, excludeCourseId, Boolean.FALSE) > 0;
		} else {
			return Db.queryLong("select count(1) from cc_course where plan_id = ? and " + columnName + " = ? and is_del = ? ", planId, columnValue, Boolean.FALSE) > 0;
		}
	}
	
	/**
	 * 
	 * 是否存在此课程代码
	 * 
	 * @description 根据课程代码查询是否存在该CcCourse
	 * @sql select count(1) from cc_course where code=?
	 * @version 1.0
	 * @param columnName
	 * 			数据库的字段
	 * @param planId 
	 * @param columnValue
	 * @return
	 */
	public boolean isExisted(String columnName, Long planId, String columnValue) {
		return isExisted(columnName, columnValue, planId, null);
	}

	/**
	 * 是否已经全在一个组内了
	 * @param courseIds
	 * 			课程ids
	 * @return
	 */
	public boolean isGroup(List<Long> courseIds,Integer type) {
		StringBuilder sqls = new StringBuilder("select count(1) from " + tableName + " where is_del = ? ");
		if (type==1){
			sqls.append("and course_group_id  is not null and id in (" + CollectionKit.convert(courseIds, ",")+ ")" );
		}else{
			sqls.append("and course_group_mange_id  is not null and id in (" + CollectionKit.convert(courseIds, ",")+ ")" );
		}

		return Db.queryLong(sqls+"", Boolean.FALSE) > 0;
	}
	
	/**
	 * 编辑时候：是否已经全在一个组内了
	 * @param courseIds
	 * 			课程ids
	 * @param id
	 * 			课程组id
	 * @return
	 */
	public boolean isGroup(List<Long> courseIds, Long id) {
		/*
		 * 判断需要增加的组的课程，没有组id，或者租id就是本身
		 */
		List<Object> params = Lists.newArrayList();
		StringBuilder sb = new StringBuilder("select count(1) from " + tableName + " ");
		sb.append("where is_del = ? ");
		params.add(Boolean.FALSE);
		sb.append("and course_group_id != ? ");
		params.add(id);
		sb.append("and course_group_id  is not null ");
		sb.append("and id in (" + CollectionKit.convert(courseIds, ",") + ")");
		return Db.queryLong( sb.toString() , params.toArray()) > 0;
	}
	
	/**
	 * 查看课程表列表分页
	 * 
	 * @param code
	 * @param name
	 * @param credit
	 * @param planId 
	 * @param directionId 
	 * @param ignoreDirection 
	 * @return
	 */
	public Page<CcCourse> page(Pageable pageable, String code, String name, Integer credit, Long planId, Long directionId, Boolean ignoreDirection, String hierarchyName, String propertyName, Integer courseOutlineStatus) {
		List<Object> params = Lists.newArrayList();
		StringBuilder headSql = new StringBuilder("select cc.*, ccm.module_name moduleName"
				+ ", ccp.property_name propertyName, ccps.property_name propertySecondaryName"
				+ ", cmd.name directionName, cch.name hierarchyName, cchs.name hierarchySecondaryName"
				+ ", sum(cic.weight) allWeight,cgmg.mange_group_id,cgtm.teach_group_id ");
		StringBuilder exceptSql = new StringBuilder("from " + CcCourse.dao.tableName + " cc ");
		if(courseOutlineStatus != null){
			exceptSql.append("inner join " + CcCourseOutline.dao.tableName + " cco on cco.course_id = cc.id and cco.is_del = ? and cco.status = ? ");
			params.add(DEL_NO);
			params.add(courseOutlineStatus);
		}
		//TODO 2020/05/15 GJM增加了分级教学和课程分组
		exceptSql.append("left join " + CcCourseGroupMangeGroup.dao.tableName + " cgmg on cgmg.course_id = cc.id  ");
		exceptSql.append("left join cc_course_group_teach_mange cgtm on cgtm.group_id=cgmg.mange_group_id ");
		exceptSql.append("left join " + CcIndicationCourse.dao.tableName + " cic on cic.course_id = cc.id and ( cic.is_del = ? or cic.is_del is null ) ");
		params.add(DEL_NO);
		exceptSql.append("left join " + CcMajorDirection.dao.tableName + " cmd on cmd.id = cc.direction_id and ( cmd.is_del = ? or cmd.is_del is null ) ");
		params.add(DEL_NO);
		exceptSql.append("left join " + CcCourseHierarchy.dao.tableName + " cch on cch.id = cc.hierarchy_id  and ( cch.is_del = ? or cch.is_del is null ) ");
		params.add(DEL_NO);
		exceptSql.append("left join " + CcCourseHierarchySecondary.dao.tableName + " cchs on cchs.id = cc.hierarchy_secondary_id  and ( cchs.is_del = ? or cchs.is_del is null ) ");
		params.add(DEL_NO);
		exceptSql.append("left join " + CcCourseProperty.dao.tableName + " ccp on ccp.id = cc.property_id and ( ccp.is_del = ? or ccp.is_del is null ) ");
		params.add(DEL_NO);
		exceptSql.append("left join " + CcCoursePropertySecondary.dao.tableName + " ccps on ccps.id = cc.property_secondary_id and ( ccps.is_del = ? or ccps.is_del is null ) ");
		params.add(DEL_NO);
		exceptSql.append("left join " + CcCourseModule.dao.tableName + " ccm on ccm.id = cc.module_id and ( ccm.is_del = ? or ccm.is_del is null ) ");
		params.add(DEL_NO);
		exceptSql.append("where cc.is_del = ? ");
		params.add(Boolean.FALSE);
		
		// 删选条件
		if (StrKit.notBlank(code)) {
			exceptSql.append("and cc.code like '%" + StringEscapeUtils.escapeSql(code) + "%' ");
		}
		if (StrKit.notBlank(name)) {
			exceptSql.append("and cc.name like '%"  + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		if (StrKit.notBlank(hierarchyName)) {
			exceptSql.append("and cch.name like '" + StringEscapeUtils.escapeSql(hierarchyName) + "%' ");
		}
		if (StrKit.notBlank(propertyName)) {
			exceptSql.append("and ccp.property_name like '" + StringEscapeUtils.escapeSql(propertyName) + "%' ");
		}

		if (credit != null) {
			exceptSql.append("and cc.credit = ? ");
			params.add(credit);
		}
		if (planId != null) {
			exceptSql.append("and cc.plan_id = ? ");
			params.add(planId);
		}
		
		if(!ignoreDirection){
			if(directionId != null){
				exceptSql.append("and ( cc.direction_id = ? or cc.direction_id is null ) ");
				params.add(directionId);
			}else{
				exceptSql.append("and cc.direction_id is null ");
			}
		}
	
		exceptSql.append("group by cc.course_group_id,cc.id ");
		if(courseOutlineStatus != null){
			exceptSql.append(",cco.status ");
		}
				
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcCourse.dao.paginate(pageable, headSql.toString(), exceptSql.toString(), params.toArray());
	}
	/**
	 * 课程指标点权重查看课程表列表分页
	 *
	 * @param code
	 * @param name
	 * @param credit
	 * @param planId
	 * @param directionId
	 * @param ignoreDirection
	 * @return
	 */
	public Page<CcCourse> page2(Pageable pageable, String code, String name, Integer credit, Long planId, Long directionId, Boolean ignoreDirection, String hierarchyName, String propertyName, Integer courseOutlineStatus) {
		List<Object> params = Lists.newArrayList();
		StringBuilder headSql = new StringBuilder("select cc.*, ccm.module_name moduleName"
				+ ", ccp.property_name propertyName, ccps.property_name propertySecondaryName"
				+ ", cmd.name directionName, cch.name hierarchyName, cchs.name hierarchySecondaryName"
				+ ", sum(cic.weight) allWeight,cgmg.mange_group_id,cgtm.teach_group_id ");
		StringBuilder exceptSql = new StringBuilder("from " + CcCourse.dao.tableName + " cc ");
		if(courseOutlineStatus != null){
			exceptSql.append("inner join " + CcCourseOutline.dao.tableName + " cco on cco.course_id = cc.id and cco.is_del = ? and cco.status = ? ");
			params.add(DEL_NO);
			params.add(courseOutlineStatus);
		}
		//TODO 2020/05/15 GJM增加了分级教学和课程分组
		exceptSql.append("left join " + CcCourseGroupMangeGroup.dao.tableName + " cgmg on cgmg.course_id = cc.id  ");
		exceptSql.append("left join cc_course_group_teach_mange cgtm on cgtm.group_id=cgmg.mange_group_id ");
		exceptSql.append("left join " + CcIndicationCourse.dao.tableName + " cic on cic.course_id = cc.id and ( cic.is_del = ? or cic.is_del is null ) ");
		params.add(DEL_NO);
		exceptSql.append("left join " + CcMajorDirection.dao.tableName + " cmd on cmd.id = cc.direction_id and ( cmd.is_del = ? or cmd.is_del is null ) ");
		params.add(DEL_NO);
		exceptSql.append("left join " + CcCourseHierarchy.dao.tableName + " cch on cch.id = cc.hierarchy_id  and ( cch.is_del = ? or cch.is_del is null ) ");
		params.add(DEL_NO);
		exceptSql.append("left join " + CcCourseHierarchySecondary.dao.tableName + " cchs on cchs.id = cc.hierarchy_secondary_id  and ( cchs.is_del = ? or cchs.is_del is null ) ");
		params.add(DEL_NO);
		exceptSql.append("left join " + CcCourseProperty.dao.tableName + " ccp on ccp.id = cc.property_id and ( ccp.is_del = ? or ccp.is_del is null ) ");
		params.add(DEL_NO);
		exceptSql.append("left join " + CcCoursePropertySecondary.dao.tableName + " ccps on ccps.id = cc.property_secondary_id and ( ccps.is_del = ? or ccps.is_del is null ) ");
		params.add(DEL_NO);
		exceptSql.append("left join " + CcCourseModule.dao.tableName + " ccm on ccm.id = cc.module_id and ( ccm.is_del = ? or ccm.is_del is null ) ");
		params.add(DEL_NO);
		//TODO 剔除加入了课程分组但没有加入分级教学的课程
		exceptSql.append("where cc.is_del = ? and cc.id not in (select course_id from cc_course_group_mange_group a " +
				"left join cc_course_group_teach_mange b on a.mange_group_id=b.group_id " +
				" where b.teach_group_id is null ) ");
		params.add(Boolean.FALSE);
		if (planId != null) {
			exceptSql.append(" and cc.plan_id = ? ");
			params.add(planId);
		}
		exceptSql.append("group by cc.course_group_id,cgmg.mange_group_id,cgtm.teach_group_id,cc.id ");

		return CcCourse.dao.paginate(pageable, headSql.toString(), exceptSql.toString(), params.toArray());
	}
	
	/**
	 * 根据年级和培养计划版本编号获取课程列表
	 * 
	 * @param grade
	 * 			年级
	 * @param versionId
	 * 			版本
	 * @param majorDirectionId
	 * 			专业方向编号
	 * @param graduateId
	 * 			毕业要求编号
	 * @return
	 */
	public List<CcCourse> findAllByGradeAndVersion(Integer grade, Long versionId, Long majorDirectionId, Long graduateId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select cc.*  from " + tableName + " cc ");
		sql.append("left join cc_teacher_course ctc on ctc.course_id = cc.id ");
		sql.append("inner join cc_indication_course cic on cic.course_id = cc.id ");
		sql.append("inner join " + CcIndicatorPoint.dao.tableName + " ci on ci.id = cic.indication_id ");
		//2020/10/30之前没有限制评分表分析法
		sql.append("where cc.plan_id = ? and ctc.grade = ? and cc.is_del = ?  ");
		params.add(versionId);
		params.add(grade);
		params.add(DEL_NO);
		if (majorDirectionId != null) {
			sql.append("and (cc.direction_id = ? or cc.direction_id is null) ");
			params.add(majorDirectionId);
		}

		if (graduateId != null) {
			sql.append("and ci.graduate_id = ? ");
			params.add(graduateId);
		}

		sql.append("group by cc.id order by ci.index_num asc, cc.course_group_id asc");
		return find(sql.toString(), params.toArray());
	}


	/**
	 * 专业负责人查看某门课程的信息
	 * @param id
	 *        课程编号
	 * @return
	 */
	public CcCourse findByCourseId(Long id) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select cc.*, ccp.property_name propertyName"
				+ ", ccps.property_name propertySecondaryName"
				+ ", cch.name hierarchyName, cchs.name hierarchySecondaryName"
				+ ", ccm.module_name moduleName from " + tableName + " cc ");
		sql.append("left join " + CcCourseProperty.dao.tableName + " ccp on ccp.id = cc.property_id ");
		sql.append("left join " + CcCoursePropertySecondary.dao.tableName + " ccps on ccps.id = cc.property_secondary_id ");
		sql.append("left join " + CcCourseHierarchy.dao.tableName + " cch on cch.id = cc.hierarchy_id ");
		sql.append("left join " + CcCourseHierarchySecondary.dao.tableName + " cchs on cchs.id = cc.hierarchy_secondary_id ");
		sql.append("left join " + CcCourseModule.dao.tableName + " ccm on ccm.id = cc.module_id ");
		sql.append("where cc.id = ? ");
		param.add(id);
		sql.append("and cc.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and (ccp.is_del = ? or ccp.is_del is null) ");
		param.add(Boolean.FALSE);
		sql.append("and (cch.is_del = ? or cch.is_del is null) ");
		param.add(Boolean.FALSE);
		sql.append("and (ccm.is_del = ? or ccm.is_del is null) ");
		param.add(Boolean.FALSE);
		
		return findFirst(sql.toString(), param.toArray());
	}

	/**
	 * 某门课程在各个大纲历史版本下的信息(code相同的即为同一门课)
	 * @param pageable
	 * @param code
	 * @param majorId
	 * @return
	 */
	public Page<CcCourse> page(Pageable pageable, String code, Long majorId) {
		List<Object> param = Lists.newArrayList();
		String selectString = "select cc.id, cc.code, cc.name, cpv.name planVersionName, "
				+ "cpv.course_version_name courseVersionName, cv.name versionName, cco.id courseOutlineId, cco.content ";
		StringBuffer sql = new StringBuffer("from " + CcCourse.dao.tableName + " cc ");
		sql.append("left join " + CcPlanVersion.dao.tableName + " cpv on cpv.id = cc.plan_id ");
		sql.append("left join " + CcVersion.dao.tableName + " cv on cv.id = cc.plan_id ");
		sql.append("left join " + CcCourseOutline.dao.tableName + " cco on cco.course_id = cc.id ");
		sql.append("where cv.major_id = ? ");
		param.add(majorId);
	    sql.append("and cc.code = ? ");
	    param.add(code);
	    sql.append("and cco.status = ? ");
	    param.add(CcCourseOutline.STATUS_AUDIT_PASS);
	    sql.append("and cco.is_del = ? ");
	    param.add(Boolean.FALSE);
	    sql.append("and cc.is_del = ? ");
	    param.add(Boolean.FALSE);
	    sql.append("and cpv.is_del = ? ");
	    param.add(Boolean.FALSE);
	    sql.append("and cv.is_del = ? ");
	    param.add(Boolean.FALSE);
	    if (StrKit.notBlank(pageable.getOrderProperty())) {
			sql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}    
		return CcCourse.dao.paginate(pageable, selectString, sql.toString(), param.toArray());
		
	}
	
	/**
	 * 获得培养计划下的课程列表（根据课程层次、课程性质、专业方向、所属模块、课程编号、学期编号一次排序）
	 * 
	 * @param planId 培养计划
	 * @return
	 */
	public List<CcCourse> findByPlanWithTerm(Long planId) {
		StringBuilder sql = new StringBuilder("select * from ( ");
		sql.append("select cc.*, cptc.plan_term_id plan_term_id from " + tableName + " cc ");
		sql.append("left join cc_plan_term_course cptc on cptc.is_del = ? and cptc.type = ? and cptc.course_id = cc.id ");
		//TODO 2020/06/19 GJM 统计数据时，如果加入了课程组那么这个课程就排除不统计，在这里先剔除加入课程分组的课程
        // 因为教学分组有重复课程并且两个课程组学分及学时都相同，取其中一个课程组即可,在这里先剔除教学分组的课程
		sql.append("where cc.plan_id = ? and cc.is_del = ? " +
				"and cc.id not in(select distinct course_id from  cc_course_group_mange_group  ) ");
		sql.append(" union all ");
		//TODO 2020/06/19 GJM 在这里取分级教学其中一个课程组的课程即可
		sql.append("select cc.*, cptc.plan_term_id plan_term_id from " + tableName + " cc ");
		sql.append("left join cc_plan_term_course cptc on cptc.is_del = ? and cptc.type = ? and cptc.course_id = cc.id ");
		//TODO  Mysql不知道什么原因在子查询中groupby 失效，只有再加一层查询了
		// select group_id from (select group_id from cc_course_group_teach_mange group by teach_group_id) aa
		sql.append("where cc.plan_id = ? and cc.is_del = ? " +
				"and cc.id  in( select course_id from cc_course_group_mange_group where mange_group_id in ( select group_id from (select group_id from cc_course_group_teach_mange group by teach_group_id) aa) )  ");
		sql.append(" ) aa ");
		sql.append("order by hierarchy_id asc, property_id asc, direction_id asc, module_id asc, course_group_id asc, id asc, plan_term_id asc ");
		return find(sql.toString(), DEL_NO, CcPlanTermCourse.TYPE_CLASS, planId, DEL_NO, DEL_NO, CcPlanTermCourse.TYPE_CLASS, planId, DEL_NO);
	}

	
	/**
	 * 根据版本和专业方向获取所有课程(EM00559)
	 * 
	 * @param versionId 版本编号
	 * @param majorDirectionId 专业方向编号
	 * @return
	 */
	public List<CcCourse> findByVersionAndMajorDirection(Long versionId, Long majorDirectionId, Long graduateId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select cc.* from " + tableName + " cc ");
		sql.append("inner join cc_indication_course cic on cic.course_id = cc.id ");
		sql.append("inner join " + CcIndicatorPoint.dao.tableName + " ci on ci.id = cic.indication_id ");
		sql.append("inner join cc_graduate cg on cg.id = ci.graduate_id and cg.graduate_ver_id = ? ");
		sql.append("where cc.plan_id = ? ");
		params.add(versionId);
		params.add(versionId);
		// 专业方向编号不能为空
		if (majorDirectionId != null) {
			sql.append("and (cc.direction_id = ? or cc.direction_id is null) ");
			params.add(majorDirectionId);
		}
		
		// 毕业要求编号不能为空
		if (graduateId != null) {
			sql.append("and cg.id = ? ");
			params.add(graduateId);
		}
		
		sql.append("group by cc.id ");
		sql.append("order by cg.index_num asc, ci.index_num asc, cc.sort desc, cc.id asc");
		return find(sql.toString(), params.toArray());
	}

	/**
	 * 返回课程信息和大纲信息
	 * @param courseOutlineId
	 *            课程大纲编号
	 * @return
	 */
	public CcCourse findByCourseOutlineId(Long courseOutlineId) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select cc.*, cco.author_id, cco.auditor_id, cco.id courseOutlineId, ct.name authorName, ctr.name auditorName, cco.name courseOutlineName, cco.audit_comment, cco.outline_type_id, " +
				"cco.outline_template_id, ccot.name outlineTypeName from " + tableName + " cc ");
		sql.append("inner join " + CcCourseOutline.dao.tableName + " cco on cco.course_id = cc.id and cco.id = ? and cco.is_del = ? ");
		param.add(courseOutlineId);
		param.add(DEL_NO);
		sql.append("inner join " + CcCourseOutlineType.dao.tableName + " ccot on ccot.id = cco.outline_type_id and ccot.is_del = ? ");
		param.add(DEL_NO);
		sql.append("left join " + CcTeacher.dao.tableName + " ct on ct.id = cco.author_id ");
        sql.append("left join " + CcTeacher.dao.tableName + " ctr on ctr.id = cco.auditor_id ");
		sql.append("where cc.is_del = ? ");
		param.add(DEL_NO);
		sql.append("and (ct.is_del = ? or ct.is_del is null )");
		param.add(DEL_NO);
		sql.append("and (ctr.is_del = ? or ctr.is_del is null )");
		param.add(DEL_NO);
		return findFirst(sql.toString(), param.toArray());
	}


	/**
	 * 根据培养计划和类型获取课程列表
	 * 
	 * @param planId 培养计划编号
	 * @param reportType 报表类型（与课程类型相同）
	 * @return
	 */
	public List<CcCourse> findAllByPlanAndType(Long planId, Integer reportType) {
		StringBuilder sql = new StringBuilder("select cc.*,cct.type_value,cct.type_name, ccg.id group_id, ccg.remark group_remark,ccg.group_name," +
				" cpt.id plan_term_id, cptc.type course_term_type, cpt.year plan_year, cpt.year_name plan_year_name, cpt.term plan_term, " +
				"cpt.term_name plan_term_name, cpt.term_type plan_term_type, 1 state, 0 num,0 teach_group_id  from " + tableName + " cc ");
		sql.append("left join cc_course_group ccg on ccg.id = cc.course_group_id and ccg.is_del = ? ");
		//sql.append("left join cc_course_group_mange ccgm on ccgm.id=ccg.course_group_id and ccgm.is_del=0 ");
		sql.append("left join cc_plan_term_course cptc on cptc.course_id = cc.id and cptc.is_del = ? ");
		sql.append("left join cc_plan_term cpt on cpt.id = cptc.plan_term_id and cpt.is_del = ? ");
		//新增课程类别 2020/05/28
		sql.append("left join cc_course_type cct on cc.type_id=cct.id and cct.is_del= ? " );
		sql.append("where cc.plan_id = ? and cc.type = ? and cc.is_del = ? and cc.id not in (select course_id from cc_course_group_mange_group) ");
		sql.append("order by cc.sort desc, cc.id asc, cpt.sort desc, cpt.year asc, cpt.term asc,ccg.id asc");
		return find(sql.toString(), DEL_NO, DEL_NO, DEL_NO,DEL_NO, planId, reportType, DEL_NO);
	}
	/*
	 * @param planId
		 * @param reportType
	 * @return java.util.List<com.gnet.model.admin.CcCourse>
	 * @author Gejm
	 * @description: 不含分级教学的课程组信息
	 * @date 2020/6/8 11:26
	 */
	public List<CcCourse> findAllCourseGroup(Long planId, Integer reportType){

		StringBuilder sql = new StringBuilder(" select cc.*,cct.type_value,cct.type_name,ccgm.remark group_remark,ccgm.group_name,cpt.id plan_term_id,cptc.type course_term_type," +
				" cpt.YEAR plan_year,cpt.year_name plan_year_name,cpt.term plan_term,cpt.term_name plan_term_name,cpt.term_type plan_term_type, " +
				"ccgmg.mange_group_id group_id, 2 state,dd.num,0 teach_group_id from " + tableName + " cc ");
		sql.append("inner join cc_course_group_mange_group ccgmg on cc.id=ccgmg.course_id ");
		sql.append("left join cc_course_group_mange ccgm on ccgmg.mange_group_id=ccgm.id and ccgm.is_del=0 ");
		sql.append("left join (SELECT mange_group_id,count(course_id) as num FROM cc_course_group_mange_group group by mange_group_id ) dd on dd.mange_group_id=ccgm.id ");
		sql.append("left join cc_plan_term_course cptc on cptc.course_id = cc.id and cptc.is_del = ? ");
		sql.append("left join cc_plan_term cpt on cpt.id = cptc.plan_term_id and cpt.is_del = ? ");
		//新增课程类别 2020/05/28
		sql.append("left join cc_course_type cct on cc.type_id=cct.id and cct.is_del= ? " );
		sql.append("where cc.plan_id = ? and cc.type = ? and cc.is_del = ?  ");
		//过滤分级教学用的课程组
		sql.append(" and ccgmg.mange_group_id in ( select distinct mange_group_id from cc_course_group_mange_group a " +
				"left  join  cc_course_group_teach_mange b on a.mange_group_id=b.group_id " +
				"where  b.teach_group_id is null)");
		sql.append("order by cc.sort desc, cc.id asc, cpt.sort desc, cpt.year asc, cpt.term asc,ccgmg.mange_group_id asc");
		return find(sql.toString(), DEL_NO, DEL_NO, DEL_NO,planId, reportType, DEL_NO);
	}
	/*
	 * @param planId
		 * @param reportType
	 * @return java.util.List<com.gnet.model.admin.CcCourse>
	 * @author Gejm
	 * @description: 分级教学的课程
	 * @date 2020/6/8 11:17
	 */
	public List<CcCourse> findAllCourseTeachGroup(Long planId, Integer reportType){

		StringBuilder sql = new StringBuilder(" select cc.*,cct.type_value,cct.type_name,ccgt.remark group_remark,ccgt.group_name,cpt.id plan_term_id,cptc.type course_term_type," +
				" cpt.YEAR plan_year,cpt.year_name plan_year_name,cpt.term plan_term,cpt.term_name plan_term_name,cpt.term_type plan_term_type, " +
				"ccgtm.group_id group_id,ccgt.id teach_group_id, 3 state,dd.num,ff.sumnum from " + tableName + " cc ");
		sql.append("inner join cc_course_group_mange_group ccgmg on cc.id=ccgmg.course_id ");
		sql.append("left join cc_course_group_teach_mange ccgtm ON ccgmg.mange_group_id = ccgtm.group_id ");
		sql.append("inner join cc_course_group_teach  ccgt on ccgtm.teach_group_id=ccgt.id and ccgt.is_del=0 ");
		sql.append("left join (SELECT mange_group_id,count(course_id) as num FROM cc_course_group_mange_group group by mange_group_id ) dd on dd.mange_group_id=ccgmg.mange_group_id ");
		sql.append("left join (SELECT teach_group_id,count( course_id ) AS sumnum FROM cc_course_group_teach_mange a LEFT JOIN cc_course_group_mange_group b ON a.group_id = b.mange_group_id GROUP BY teach_group_id) ff on ff.teach_group_id=ccgt.id ");
		sql.append("left join cc_plan_term_course cptc on cptc.course_id = cc.id and cptc.is_del = ? ");
		sql.append("left join cc_plan_term cpt on cpt.id = cptc.plan_term_id and cpt.is_del = ? ");
		//新增课程类别 2020/05/28
		sql.append("left join cc_course_type cct on cc.type_id=cct.id and cct.is_del= ? " );
		sql.append("where cc.plan_id = ? and cc.type = ? and cc.is_del = ?  ");
		sql.append("order by ccgtm.group_id ASC ,cc.sort desc, cc.id asc, cpt.sort desc, cpt.year asc, cpt.term asc,ccgt.id asc");
		return find(sql.toString(), DEL_NO, DEL_NO, DEL_NO,planId, reportType, DEL_NO);
	}

	/**
	 * 根据专业编号返回课程列表(课程代码唯一)
	 * @param pageable
	 * @param majorId
	 * @param includeUnpublished 是否包含未发布版本的课程
	 * @return
	 */
	public Page<CcCourse> page(Pageable pageable, Long majorId, Boolean includeUnpublished) {
		StringBuilder exceptSql = new StringBuilder("from " + CcCourse.dao.tableName + " cc ");
		List<Object> params = Lists.newArrayList();
		exceptSql.append("left join " + CcVersion.dao.tableName + " cv on cv.id = cc.plan_id ");
		exceptSql.append("where cv.major_id = ? ");
		params.add(majorId);
		exceptSql.append("and cc.is_del =  ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cv.is_del = ? ");
		params.add(Boolean.FALSE);
		if (!includeUnpublished) {
			//不包含未发布版本的课程（即只包含已发布的版本）
			exceptSql.append("and cv.publish_date is not null ");
		}
		exceptSql.append("group by cc.code ");
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append(" order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcCourse.dao.paginate(pageable, "select cc.id, cc.code, cc.name ", exceptSql.toString(), params.toArray());
	}

	/**
	 * 根据课程编号组获得课程
	 * @param courseIds 课程编号集合
	 * @return
	 */
	public CcCourse findByCourseHaveGroup(Long[] courseIds) {
		StringBuilder sql = new StringBuilder("select cc.* from " + tableName + " cc ");
		sql.append("where cc.id in (" + CollectionKit.convert(courseIds, ",") + ") and cc.is_del =? and cc.course_group_id is not null ");
		sql.append("limit 1");
		return CcCourse.dao.findFirst(sql.toString(), DEL_NO);
	}
	
	/**
	 * 根据客户编号获得指标点编号
	 * 
	 * @param courseIds
	 * @return
	 */
	public List<CcCourse> findByCourseIdsWithIndication(Long[] courseIds) {
		StringBuilder sql = new StringBuilder("select cc.*, cic.indication_id indication_id, cic.weight weight from " + tableName + " cc ");
		sql.append("left join cc_indication_course cic on cic.course_id = cc.id ");
		sql.append("where cc.id in (" + CollectionKit.convert(courseIds, ",") + ") and cc.is_del = ? and cic.is_del = ? ");
		return CcCourse.dao.find(sql.toString(), DEL_NO, DEL_NO);
	}

	/**
	 * 判断某个版本某个年级的课程专业方向是否有改变，如果有则需要更新专业方向达成度汇总报表
	 * @param versionId
	 * @param grade
	 * @return
	 */
	public boolean isNeedToUpdateByVersionAndGrade(Long versionId, Integer grade) {
		StringBuilder sql = new StringBuilder("select count(cc.id) from " + tableName + " cc ");
		sql.append("inner join " + CcMajorDirection.dao.tableName + " cmd on cmd.plan_id = cc.plan_id and cmd.is_del = ? ");
		sql.append("inner join " + CcReportMajor.dao.tableName + " crm on crm.major_direction_id = cmd.id and crm.grade = ? and crm.is_del = ? ");
		sql.append("where cc.is_del = ? and cc.plan_id = ? and (cc.direction_change_date > crm.statistics_date ) ");
		sql.append("limit 1 ");
		return Db.queryLong(sql.toString(), DEL_NO, grade, DEL_NO, DEL_NO, versionId) > 0;
	}

	/**
	 * 查找版本下的所有课程
	 * @param version
	 * @return
	 */
	public List<CcCourse> findByPlanId(Long version) {
        List<Object> param = Lists.newArrayList();
        StringBuilder sql = new StringBuilder("select cc.id, cc.type, cc.code, cv.apply_grade, major.code major_code, institute.code institute_code," +
				" school.code school_code, ccp.property_name, cv.name versionName, cc.name courseName, cc.credit from " + tableName + " cc ");
        sql.append("inner join " + CcVersion.dao.tableName + " cv on cv.id = cc.plan_id and cv.is_del = ? and cc.plan_id = ? ");
        param.add(DEL_NO);
        param.add(version);
        sql.append("inner join " + Office.dao.tableName + " major on major.id = cv.major_id and major.is_del = ? ");
        param.add(DEL_NO);
		sql.append("inner join " + Office.dao.tableName + " institute on institute.id = major.parentid and institute.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join " + Office.dao.tableName + " school on school.id = institute.parentid and school.is_del = ? ");
		param.add(DEL_NO);
		sql.append("left join " + CcCourseProperty.dao.tableName + " ccp on ccp.id = cc.property_id and (ccp.is_del is null or ccp.is_del = ? ) ");
		param.add(DEL_NO);
		sql.append("where cc.is_del = ? ");
		param.add(DEL_NO);
		return  find(sql.toString(), param.toArray());
	}

	/**
	 * 返回课程详情
	 * @param version
	 * @param code
	 * @return
	 */
	public CcCourse findDetailByCode(Long version, String code) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select cc.*, major.code major_code, institute.code institute_code, school.code school_code from " + tableName + " cc ");
		sql.append("inner join " + CcVersion.dao.tableName + " cv on cv.id = cc.plan_id and cv.is_del = ? and cc.plan_id = ? ");
		param.add(DEL_NO);
		param.add(version);
		sql.append("inner join " + Office.dao.tableName + " major on major.id = cv.major_id and major.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join " + Office.dao.tableName + " institute on institute.id = major.parentid and institute.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join " + Office.dao.tableName + " school on school.id = institute.parentid and school.is_del = ? ");
		param.add(DEL_NO);
		sql.append("where cc.code = ? and cc.is_del = ? ");
		param.add(code);
		param.add(DEL_NO);
		return findFirst(sql.toString(), param.toArray());
	}


	public CcCourse sumCourseInfo(long courseGroupId){
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("SELECT sum(credit) as credit,sum(all_hours) as all_hours,sum(theory_hours) as theory_hours,sum(experiment_hours) as experiment_hours,sum(practice_hours) as practice_hours,sum(indepent_hours) as indepent_hours,sum(week_hour) as week_hour ,sum(exercises_hours) as exercises_hours,sum(dicuss_hours) as dicuss_hours ,sum(extracurricular_hours) as extracurricular_hours, sum(operate_computer_hours) as operate_computer_hours" +
				" from " + tableName );
		sql.append(" where course_group_id=? and is_del=0 ");
		sql.append("group by course_group_id");
		param.add(courseGroupId);
		return findFirst(sql.toString(), param.toArray());

	}
	public List<CcCourse> subCourseCode(long courseGroupId){
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder(" select code" +
				" from " + tableName );
		sql.append(" where course_group_id=? and is_del=0");
		param.add(courseGroupId);
		return find(sql.toString(), param.toArray());
	}
	/*
	 * @param courseIds
		 * @param id
	 * @return java.util.List<com.gnet.model.admin.CcCourse>
	 * @author Gejm
	 * @description: 用于课程组的数据
	 * @date 2020/6/3 18:54
	 */
	public List<CcCourse> coursesLists(long id) {

		List<Object> params = Lists.newArrayList();
		StringBuilder sb = new StringBuilder("select a.id,a.name,a.code,b.mange_group_id as course_group_mange_id from " + tableName + " a ");
		sb.append("inner join cc_course_group_mange_group b on a.id=b.course_id ");
		sb.append("where is_del = ? ");
		params.add(Boolean.FALSE);
		if (id !=0){
            sb.append("and b.mange_group_id = ? ");
            params.add(id);
        }
		return find( sb.toString() , params.toArray());
	}
	/*
	 * @param courseIds
		 * @param type
	 * @return boolean
	 * @author Gejm
	 * @description: 判断此课程是否加入课程组或多选一
	 * @date 2020/6/8 19:07
	 */
	public boolean isGroupMange(List<Long> courseIds,Integer type) {
		StringBuilder sqls = new StringBuilder("select count(1) from");
		if (type==1){
			sqls.append( tableName + " where is_del = 0 ");
			sqls.append("and course_group_id  is not null and id in (" + CollectionKit.convert(courseIds, ",")+ ")" );
		}else{
			sqls.append(" cc_course_group_mange_group where ");
			sqls.append(" course_id in (" + CollectionKit.convert(courseIds, ",")+ ")" );
		}

		return Db.queryLong(sqls+"") > 0;
	}
	/*
	 * @param groupIds
	 * @return java.util.List<com.gnet.model.admin.CcCourse>
	 * @author Gejm
	 * @description: 根据课程组的id合计课程组的学分及总学时
	 * @date 2020/6/19 11:06
	 */
	public List<CcCourse> sumGroupMangeCreditAndHours(Long[] groupIds){
		StringBuilder sql = new StringBuilder("select sum(cc.credit) credit,sum(cc.all_hours) all_hours,ccgmg.mange_group_id from " + tableName + " cc ");
		sql.append("left join cc_course_group_mange_group ccgmg on cc.id=ccgmg.course_id ");
		sql.append("where cc.is_del=0 and ccgmg.mange_group_id  in (" + CollectionKit.convert(groupIds, ",")+ ") " );
		sql.append("group by ccgmg.mange_group_id");
		return find( sql.toString());
	}
	/*
	 * @param versionId
		 * @param statuses
	 * @return java.util.List<com.gnet.model.admin.CcCourse>
	 * @author Gejm
	 * @description: 版本发布时判断是否存在课程大纲未审核通过和未分配的
	 * @date 2020/6/26 19:19
	 */
	public List<CcCourse> findCourseStateUnfinished(Long versionId, Integer[] statuses){
		StringBuilder sql = new StringBuilder(" select cc.* FROM " + CcCourse.dao.tableName + " cc");
		sql.append(" LEFT JOIN cc_course_outline cco  ON cc.id = cco.course_id AND cco.is_del = 0");
		sql.append(" where cc.plan_id = "+versionId+ " and cc.is_del=0 and (cco.STATUS NOT IN (" + CollectionKit.convert(statuses,",") +
				") or cco.STATUS is null)");

		return find( sql.toString());

	}

	public List<CcCourse> findNameCourse(String name,Long planId){
		ArrayList<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select * from " + CcCourse.dao.tableName + " where name=? and is_del=0 and plan_id=?");
		params.add(name);
		params.add(planId);
		return  find(sql.toString(),params.toArray());

	}
	/*
	 * @param courseId
	 * @return com.gnet.model.admin.CcCourse
	 * @author Gejm
	 * @description: 查找课程的信息及专业
	 * @date 2020/12/11 11:27
	 */
	public CcCourse findCourseMajor(Long courseId){
		StringBuilder sql = new StringBuilder("select cc.*,cv.major_id from cc_course cc left join cc_version cv on cc.plan_id=cv.id where cc.id=? ");
		return findFirst(sql.toString(),courseId);
	}

}
