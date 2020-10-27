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
 * 
 * @type model
 * @table cc_course_group_mange
 * @author GJM
 * @version 1.0
 * @date 2020年07月14日 11:10:53
 *
 */
@TableBind(tableName = "cc_course_group_mange")
public class CcCourseGroupMange extends DbModel<CcCourseGroupMange> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcCourseGroupMange dao = new CcCourseGroupMange();
	
	/**
	 * 类型-限选
	 */
	public static final Integer TYPE_LIMITED_SELECT = 1;
	

	/**
	 * 查看课程组表列表分页
	 * @param pageable 
	 * @param planId 
	 * @param remark
	 * @param type 
	 * @return
	 */
	public Page<CcCourseGroupMange> page(Pageable pageable, Long planId, String remark, Integer type) {
		StringBuilder exceptSql = new StringBuilder("from " + CcCourseGroupMange.dao.tableName + " ");
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append("where plan_id = ? ");
		params.add(planId);
		exceptSql.append("and is_del = ? ");
		params.add(Boolean.FALSE);

		// 删选条件
		if (!StrKit.isBlank(remark)) {
			exceptSql.append("and remark like '%" + StringEscapeUtils.escapeSql(remark) + "%' ");
		}
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcCourseGroupMange.dao.paginate(pageable, "select * ", exceptSql.toString(), params.toArray());
	}
	/**
	 * 查看课程多选一表列表带有课程性质的
	 *

	 * @return
	 */
	public Page<CcCourseGroupMange> pageGroup(Pageable pageable,  Long planId, Long directionId, Boolean ignoreDirection, String hierarchyName, String propertyName, Integer courseOutlineStatus) {
		List<Object> params = Lists.newArrayList();
		StringBuilder headSql = new StringBuilder("select distinct ccgm.id,ccgm.group_name,cc.type,cc.hierarchy_id,cc.property_id,cc.direction_id,module_id, ccm.module_name moduleName"
				+ ", ccp.property_name propertyName, ccps.property_name propertySecondaryName"
				+ ", cmd.name directionName, cch.name hierarchyName, cchs.name hierarchySecondaryName,ccgtm.teach_group_id "
		);
		StringBuilder exceptSql = new StringBuilder("from " + CcCourseGroupMange.dao.tableName + " ccgm ");
		exceptSql.append("left join cc_course_group_mange_group ccgmg on ccgmg.mange_group_id=ccgm.id ");
		exceptSql.append("left join " + CcCourseGroupTeachMange.dao.tableName + " ccgtm on ccgtm.group_id = ccgm.id  ");
		/*exceptSql.append("left join cc_course_group_course ccgc on ccg.id=ccgc.group_id ");*/
		exceptSql.append("left join " + CcCourse.dao.tableName + " cc on ccgmg.course_id = cc.id  and ( cc.is_del = ? or cc.is_del is null ) ");
		params.add(DEL_NO);
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
		exceptSql.append("where ccgm.is_del = ? ");
		params.add(Boolean.FALSE);

		// 删选条件

		if (StrKit.notBlank(hierarchyName)) {
			exceptSql.append("and cch.name like '" + StringEscapeUtils.escapeSql(hierarchyName) + "%' ");
		}
		if (StrKit.notBlank(propertyName)) {
			exceptSql.append("and ccp.property_name like '" + StringEscapeUtils.escapeSql(propertyName) + "%' ");
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

		//exceptSql.append("group by cc.course_group_id, cc.id ");
		if(courseOutlineStatus != null){
			exceptSql.append(",cco.status ");
		}

		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}

		return CcCourseGroupMange.dao.paginate(pageable, headSql.toString(), exceptSql.toString(), params.toArray());
	}
	/*
	 * @param id
	 * @return java.util.List<com.gnet.model.admin.CcCourseGroup>
	 * @author Gejm
	 * @description: 根据中间表查询关联的课程分组
	 * @date 2020/6/4 15:15
	 */
	public List<CcCourseGroupMange> groupLists(long id) {

		List<Object> params = Lists.newArrayList();
		StringBuilder sb = new StringBuilder("select a.*,b.teach_group_id  from " + tableName + " a ");
		sb.append("inner join cc_course_group_teach_mange b on a.id=b.group_id ");
		sb.append("where is_del = ? ");
		params.add(Boolean.FALSE);
		if (id !=0){
			sb.append("and b.teach_group_id = ? ");
			params.add(id);
		}
		return find( sb.toString() , params.toArray());
	}
	/*
	 * @param grouIds
	 * @return boolean
	 * @author Gejm
	 * @description: 判断已经是否被分级教学关联
	 * @date 2020/6/8 18:27
	 */
	public boolean isGroupTeach(Long grouIds) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sb = new StringBuilder("SELECT count(1) FROM " + tableName + " a ");
		sb.append(" inner join cc_course_group_teach_mange b on a.id=b.group_id where is_del = 0 and a.id=?");
		params.add(grouIds);
		return Db.queryLong( sb.toString() , params.toArray()) > 0;

	}


}
