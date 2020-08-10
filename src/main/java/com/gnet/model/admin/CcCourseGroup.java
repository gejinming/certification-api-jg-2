package com.gnet.model.admin;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;


/**
 * 
 * @type model
 * @table cc_course_group
 * @author SY
 * @version 1.0
 * @date 2016年07月14日 11:10:53
 *
 */
@TableBind(tableName = "cc_course_group")
public class CcCourseGroup extends DbModel<CcCourseGroup> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcCourseGroup dao = new CcCourseGroup();
	
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
	public Page<CcCourseGroup> page(Pageable pageable, Long planId, String remark, Integer type) {
		StringBuilder exceptSql = new StringBuilder("from " + CcCourseGroup.dao.tableName + " ");
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append("where plan_id = ? ");
		params.add(planId);
		exceptSql.append("and is_del = ? ");
		params.add(Boolean.FALSE);
		//exceptSql.append("and type = 1 ");
		//params.add(type);
		// 删选条件
		if (!StrKit.isBlank(remark)) {
			exceptSql.append("and remark like '%" + StringEscapeUtils.escapeSql(remark) + "%' ");
		}
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcCourseGroup.dao.paginate(pageable, "select * ", exceptSql.toString(), params.toArray());
	}

	/**
	 * 查看课程多选一表列表带有课程性质的
	 *
	 * @param code
	 * @param name
	 * @param credit
	 * @param planId
	 * @param directionId
	 * @param ignoreDirection
	 * @return
	 */
	public Page<CcCourseGroup> pageGroup(Pageable pageable,  Long planId, Long directionId, Boolean ignoreDirection, String hierarchyName, String propertyName, Integer courseOutlineStatus) {
		List<Object> params = Lists.newArrayList();
		StringBuilder headSql = new StringBuilder("select distinct ccg.id,ccg.group_name,cc.type,cc.hierarchy_id,cc.property_id,cc.direction_id,module_id, ccm.module_name moduleName"
				+ ", ccp.property_name propertyName, ccps.property_name propertySecondaryName"
				+ ", cmd.name directionName, cch.name hierarchyName, cchs.name hierarchySecondaryName "
				);
		StringBuilder exceptSql = new StringBuilder("from " + CcCourseGroup.dao.tableName + " ccg ");
        exceptSql.append("left join cc_course_group_course ccgc on ccgc.group_id=ccg.id ");
		exceptSql.append("left join " + CcCourse.dao.tableName + " cc on ccgc.course_id=cc.id and ( cc.is_del = ? or cc.is_del is null ) ");
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
		exceptSql.append("where ccg.is_del = ? and ccg.type=2 ");
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

		exceptSql.append("group by cc.course_group_id, cc.id ");
		if(courseOutlineStatus != null){
			exceptSql.append(",cco.status ");
		}

		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}

		return CcCourseGroup.dao.paginate(pageable, headSql.toString(), exceptSql.toString(), params.toArray());
	}
	/*
	 * @param id
	 * @return java.util.List<com.gnet.model.admin.CcCourseGroup>
	 * @author Gejm
	 * @description: 根据中间表查询关联的多选一组
	 * @date 2020/6/4 15:15
	 */
    public List<CcCourseGroup> groupLists(long id) {

        List<Object> params = Lists.newArrayList();
        StringBuilder sb = new StringBuilder("select a.*,b.mange_group_id  from cc_course a ");
        sb.append("inner join cc_course_group_mange_group b on a.id=b.course_id ");
        sb.append("where is_del = ? ");
        params.add(Boolean.FALSE);
        if (id !=0){
            sb.append("and b.mange_group_id = ? ");
            params.add(id);
        }
        return find( sb.toString() , params.toArray());
    }

}
