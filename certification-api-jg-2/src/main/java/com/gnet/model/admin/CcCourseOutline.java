package com.gnet.model.admin;

import java.util.List;

import java.util.ArrayList;
import com.gnet.model.DbModel;
import com.gnet.object.CcCourseOrderType;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * @type model
 * @description 课程教学大纲文本表操作，包括对数据的增删改查与列表
 * @table cc_course_outline
 * @author SY
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course_outline")
public class CcCourseOutline extends DbModel<CcCourseOutline> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcCourseOutline dao = new CcCourseOutline();

	    /**
		 * 未分配
		 */
		public static final Integer STATUS_NOT_DISTRIBUTION = 0;
		
	   /**
		 *  已分配未确认
		 */
		public static final Integer STATUS_ASSIGNED_NOT_CONFIRMED = 1;

	   /**
		 * 未开始
		 */
		public static final Integer STATUS_NOT_START = 2;

	   /**
		 * 未提交(初始状态，或者是保存状态)
		 */
		public static final Integer STATUS_NOT_SUBMIT = 3;
		
		/**
		 * 待审核(即提交时设置的状态)
		 */
		public static final Integer STATUS_PENDING_AUDIT = 4;
		
		/**
		 * 审核通过
		 */
		public static final Integer STATUS_AUDIT_PASS = 5;
		
		/**
		 * 审核驳回
		 */
		public static final Integer STATUS_AUDIT_DISMISSED = 6;
		
		/**
		 * 大纲内容最大长度
		 */
		public static final Integer CONTENT_MAX_LEANGHT = 200000;

		/**
		 * 课程大纲编号
		 */
		public static final String COURSE_OUTLINE_ID = "course_outline_id";

		/**
		 * @param planId
		 *            版本编号
		 * @param status
		 *            状态数组(已分配未确认、未开始)
		 * @return
		 */
		public List<CcCourseOutline> findCourseOutlineByStatusAndPlanId(Long planId, Integer[] status) {
			List<Object> param = Lists.newArrayList();
			StringBuffer sql = new StringBuffer("select cco.*, cc.name courseName, cc.id courseId from " + tableName + " cco ");
			sql.append("inner join " + CcCourse.dao.tableName + " cc on cc.id = cco.course_id ");
			sql.append("where cc.plan_id = ? ");
			param.add(planId);
			sql.append("and cco.status in (" + CollectionKit.convert(status, ",") + ") ");
			sql.append("and cc.is_del = ? ");
			param.add(Boolean.FALSE);
			sql.append("and cco.is_del = ? ");
			param.add(Boolean.FALSE);
			sql.append("and cco.author_id is not null ");
			sql.append("and cco.auditor_id is not null ");
			return find(sql.toString(), param.toArray());
		}

	/**
	 * 大纲详细信息
	 * @param courseOutlineId
	 * @return
	 */
		public CcCourseOutline findById(Long courseOutlineId){
			StringBuffer sql = new StringBuffer("select cco.*, cct.name outlineTypeName, ccote.name outlineTemplateName from " + tableName +  " cco ");
			sql.append("inner join " + CcCourseOutlineType.dao.tableName + " cct on cct.id = cco.outline_type_id and cct.is_del = ? ");
			sql.append("left join " + CcCourseOutlineTemplate.dao.tableName + " ccote on cco.outline_template_id = ccote.id and (ccote.is_del = ? or ccote.is_del is null )");
			sql.append("where cco.is_del = ? and cco.id = ? ");
			return findFirst(sql.toString(), DEL_NO, DEL_NO, DEL_NO, courseOutlineId);
		}
		
		/**
		 * 根据版本编号以及排除掉的状态，查询出所有教学大纲，根据状态逆向排序
		 * @param versionId
		 * @param statuses
		 * @return
		 */
		public List<CcCourseOutline> findByVersionIdAndNotInStatus(Long versionId, Integer[] statuses) {
			List<Object> params = new ArrayList<>();
			StringBuilder sb = new StringBuilder("select cco.* from " + tableName + " cco ");
			sb.append("left join " + CcCourse.dao.tableName + " cc on cc.id = cco.course_id ");
			sb.append("where cc.plan_id = ?  and cco.is_del= ?  ");
			params.add(versionId);
			params.add(Boolean.FALSE);
			sb.append("and cco.status not in (" + CollectionKit.convert(statuses, ",") + ") ");
			sb.append("and cc." + CcCourse.IS_DEL_LABEL + " = ? ");
			params.add(Boolean.FALSE);
			sb.append("order by cco.status desc ");
			return find(sb.toString(), params.toArray());
		}

		/**
		 * 通过课程编号找到最新的课程大纲信息
		 * @param courseId
		 * @return
		 */
		public CcCourseOutline findByCourseId(Long courseId) {
			return findFirst("select * from " + tableName + " where course_id = ? and is_del = ? order by create_date desc ", courseId, Boolean.FALSE);
		}

	  /**
	   * 课程可指派的大纲列表
	   * @param courseId
	   * @return
	   */
	   public List<CcCourseOutline> findListByCourseId(Long courseId, Integer status, Long auditorId, Boolean canStartTask) {
	   	      List<Object> param = Lists.newArrayList();
              StringBuffer sql = new StringBuffer("select cco.*, ccot.name outlineTypeName, ct.name authorName, ctr.name auditorName from " + tableName + " cco ");
              sql.append("inner join " + CcCourseOutlineType.dao.tableName + " ccot on ccot.id = cco.outline_type_id and ccot.is_del = ? ");
		      sql.append("left join " + CcTeacher.dao.tableName + " ct on ct.id = cco.author_id ");
		      sql.append("left join " + CcTeacher.dao.tableName + " ctr on ctr.id = cco.auditor_id ");
              param.add(DEL_NO);
              sql.append("where cco.is_del = ? and cco.course_id = ? ");
              param.add(DEL_NO);
              param.add(courseId);
              if(status != null){
              	sql.append("and cco.status < ? ");
              	param.add(status);
			  }
			  if(auditorId != null){
              	sql.append("and cco.auditor_id = ? ");
              	param.add(auditorId);
			  }
			  if(canStartTask){
			  	sql.append("and cco.auditor_id is not null and cco.author_id is not null ");
			  }
		      sql.append("and (ct.is_del = ? or ct.is_del is null )");
		      param.add(DEL_NO);
		      sql.append("and (ctr.is_del = ? or ctr.is_del is null )");
		      param.add(DEL_NO);
              return  find(sql.toString(), param.toArray());
	    }

	/**
	 *  大纲列表
	 * @param courseOutlineIds
	 * @return
	 */
	public List<CcCourseOutline> findByCourseOutlineIds(Long[] courseOutlineIds) {
        List<Object> param = Lists.newArrayList();
        StringBuffer sql = new StringBuffer("select cco.*, cc.name courseName, ct.name authorName, ctr.name auditorName, cot.name outlineTypeName from " + tableName + " cco ");
        sql.append("inner join " + CcCourseOutlineType.dao.tableName + " cot on cot.id = cco.outline_type_id and cot.is_del = ? ");
        param.add(DEL_NO);
        sql.append("inner join " + CcCourse.dao.tableName + " cc on cc.id = cco.course_id and cc.is_del = ? ");
        param.add(DEL_NO);
	    sql.append("left join " + CcTeacher.dao.tableName + " ct on ct.id = cco.author_id ");
	    sql.append("left join " + CcTeacher.dao.tableName + " ctr on ctr.id = cco.auditor_id ");
	    sql.append("where cco.is_del = ? and cco.id in ( " + CollectionKit.convert(courseOutlineIds, ",") + " ) ");
	    param.add(DEL_NO);
	    sql.append("and (ct.is_del = ? or ct.is_del is null )");
	    param.add(DEL_NO);
	    sql.append("and (ctr.is_del = ? or ctr.is_del is null )");
	    param.add(DEL_NO);
		return  find(sql.toString(), param.toArray());
	}


	/**
	 * 当前版本的所有课程的大纲列表
	 * @param planId
	 * @param code
	 * @param name
	 * @param courseOutlineStatus
	 * @return
	 */
	public List<CcCourseOutline> listByPlanId(Long planId, String code, String name, Integer courseOutlineStatus) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select cco.name courseOutlineName, cco.status, cco.id courseOutlineId, ccot.name outlineTypeName, ct.name authorName, ");
		sql.append("ctr.name auditorName, cco.is_del isDeleteOutline, cc.id courseId from " + tableName + " cco ");
		sql.append("inner join " + CcCourse.dao.tableName + " cc on cc.id = cco.course_id ");
		sql.append("inner join " + CcVersion.dao.tableName + " cv on cv.id = cc.plan_id ");
		sql.append("left join " + CcTeacher.dao.tableName + " ct on ct.id = cco.author_id ");
		sql.append("left join " + CcTeacher.dao.tableName + " ctr on ctr.id = cco.auditor_id ");
		sql.append("left join " + CcCourseOutlineType.dao.tableName + " ccot on ccot.id = cco.outline_type_id ");
		sql.append("left join " + CcCourseProperty.dao.tableName + " ccp on ccp.id = cc.property_id ");
		sql.append("left join " + CcCourseHierarchy.dao.tableName + " cch on cch.id = cc.hierarchy_id ");
		sql.append("left join " + CcCourseModule.dao.tableName + " ccm on ccm.id = cc.module_id ");
		sql.append("where cc.is_del = ? ");
		param.add(DEL_NO);
		sql.append("and cco.is_del = ? ");
		param.add(DEL_NO);
		sql.append("and (ccot.is_del = ? or ccot.is_del is null) ");
		param.add(DEL_NO);
		sql.append("and (ccp.is_del = ? or ccp.is_del is null) ");
		param.add(DEL_NO);
		sql.append("and (cch.is_del = ? or cch.is_del is null) ");
		param.add(DEL_NO);
		sql.append("and (ccm.is_del = ? or ccm.is_del is null) ");
		param.add(DEL_NO);
		sql.append("and (ct.is_del = ? or ct.is_del is null ) ");
		param.add(DEL_NO);
		sql.append("and (ctr.is_del = ? or ctr.is_del is null ) ");
		param.add(DEL_NO);
		sql.append("and cc.plan_id = ? ");
		param.add(planId);

		// 删选条件
		if (StrKit.notBlank(code)) {
			sql.append("and cc.code like '" + StringEscapeUtils.escapeSql(code) + "%' ");
		}
		if (StrKit.notBlank(name)) {
			sql.append("and cc.name like '%" + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		if(courseOutlineStatus != null){
             sql.append("and cco.status = ? ");
             param.add(courseOutlineStatus);
		}

		return find(sql.toString(), param.toArray());
	}


	/**
	 * 教师的大纲任务（需要编写和需要审核）
	 * @param pageable
	 * @param userId
	 * @param name
	 * @param courseName
	 * @param majorName
	 * @param status
	 * @param isNeedWriteList      */
	public Page<CcCourseOutline> pageByTeacherId(Pageable pageable, Long userId, String name, String courseName, String majorName, Integer status, Boolean isNeedWriteList) {
		List<Object> param = Lists.newArrayList();
		String selectString = "select cc.*, cv.name versionName, so.name majorName, ccp.property_name propertyName, cch.name hierarchyName, ccm.module_name moduleName, " +
				"cco.auditor_id, cco.author_id, cco.status, cco.name courseOutlineName, cco.id courseOutlineId, cco.outline_type_id, cco.outline_template_id, ccot.name outlineTypeName ";
		StringBuilder sql = new StringBuilder("from " + tableName + " cco ");
		sql.append("inner join " + CcCourseOutlineType.dao.tableName + " ccot on ccot.id = cco.outline_type_id and ccot.is_del = ? ");
        param.add(DEL_NO);
		sql.append("inner join " + CcCourse.dao.tableName + " cc on cc.id = cco.course_id and cc.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join " + CcVersion.dao.tableName + " cv on cv.id = cc.plan_id and cv.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join " + Office.dao.tableName + " so on cv.major_id = so.id and so.is_del = ? ");
		param.add(DEL_NO);
		sql.append("left join " + CcCourseProperty.dao.tableName + " ccp on ccp.id = cc.property_id ");
		sql.append("left join " + CcCourseHierarchy.dao.tableName + " cch on cch.id = cc.hierarchy_id ");
		sql.append("left join " + CcCourseModule.dao.tableName + " ccm on ccm.id = cc.module_id ");
		sql.append("where cco.is_del = ? ");
		param.add(Boolean.FALSE);
		if(isNeedWriteList == null){
			if(status != null){
				sql.append("and cco.status = ? ");
				param.add(status);
			}else{
				sql.append("and cco.status > ? ");
				param.add(CcCourseOutline.STATUS_NOT_START);
			}
			sql.append("and (cco.author_id = ? or cco.auditor_id = ? ) ");
			param.add(userId);
			param.add(userId);
		}else if(isNeedWriteList){
			sql.append("and cco.author_id = ? ");
			param.add(userId);
			if(status != null){
				sql.append("and cco.status = ? ");
				param.add(status);
			}else{
				sql.append("and cco.status > ? ");
				param.add(CcCourseOutline.STATUS_NOT_START);
			}
		}else{
			if(status != null){
				sql.append("and cco.status = ? ");
				param.add(status);
			}else{
				sql.append("and cco.status > ? ");
				param.add(CcCourseOutline.STATUS_NOT_START);
			}
			sql.append("and cco.auditor_id = ? ");
			param.add(userId);
		}
		sql.append("and (ccp.is_del = ? or ccp.is_del is null) ");
		param.add(Boolean.FALSE);
		sql.append("and (cch.is_del = ? or cch.is_del is null) ");
		param.add(Boolean.FALSE);
		sql.append("and (ccm.is_del = ? or ccm.is_del is null) ");
		param.add(Boolean.FALSE);

		// 删选条件
		if (StrKit.notBlank(courseName)) {
			sql.append("and cc.name like '" + StringEscapeUtils.escapeSql(courseName) + "%' ");
		}
		if(StrKit.notBlank(name)){
			sql.append("and cco.name like '" + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		if(StrKit.notBlank(majorName)){
			sql.append("and so.name like '" + StringEscapeUtils.escapeSql(majorName) + "%' ");
		}

		if (StrKit.notBlank(pageable.getOrderProperty())) {
			sql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}else{
			sql.append("order by cco.status, cv.id, cco.name ");
		}

		return CcCourseOutline.dao.paginate(pageable, selectString, sql.toString(), param.toArray());
	}

	/**
	 * 查询某个版本下的大纲
	 * @param pageable
	 * @param planId
	 * @param courseName
	 * @param name
	 * @param status
	 * @return
	 */
	public Page<CcCourseOutline> pageByPlanId(Pageable pageable, Long planId, String courseName, String name, Integer status) {
		List<Object> param = Lists.newArrayList();
		String selectString = "select cco.id, so.name majorName, cc.code code, cc.name courseName, cco.name name, ccot.name outlineTypeName, ct.name authorName, ctr.name auditorName, cco.status ";
		StringBuilder sql = new StringBuilder("from " + tableName + " cco ");
		sql.append("inner join " +  CcCourse.dao.tableName  + " cc on cc.id = cco.course_id and cc.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join " + CcVersion.dao.tableName + " cv on cv.id = cc.plan_id and cv.is_del = ? ");
        param.add(DEL_NO);
        sql.append("inner join " + Office.dao.tableName + " so on so.id = cv.major_id and so.is_del = ? ");
        param.add(DEL_NO);
        sql.append("inner join " + CcCourseOutlineType.dao.tableName + " ccot on ccot.id = cco.outline_type_id and ccot.is_del = ? ");
        param.add(DEL_NO);
        sql.append("left join " + CcTeacher.dao.tableName + " ct on ct.id = cco.author_id ");
        sql.append("left join " + CcTeacher.dao.tableName + " ctr on ctr.id = cco.auditor_id ");
        sql.append("where cco.is_del = ? ");
        param.add(DEL_NO);
        sql.append("and cc.plan_id = ? ");
        param.add(planId);
        sql.append("and (ct.is_del = ? or ct.is_del is null ) ");
        param.add(DEL_NO);
        sql.append("and (ctr.is_del = ? or ctr.is_del is null ) ");
        param.add(DEL_NO);

		// 删选条件
		if (StrKit.notBlank(courseName)) {
			sql.append("and cc.name like '" + StringEscapeUtils.escapeSql(courseName) + "%' ");
		}
		if(StrKit.notBlank(name)){
			sql.append("and cco.name like '" + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		if(status != null){
			sql.append("and cco.status = ? ");
			param.add(status);
		}

		if (StrKit.notBlank(pageable.getOrderProperty())) {
			sql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}else{
			sql.append("order by cco.status, cc.name, cco.author_id, cco.auditor_id ");
		}

		return CcCourseOutline.dao.paginate(pageable, selectString, sql.toString(), param.toArray());
	}

	/**
	 * 查找版本下的课程大纲
	 * @return
	 */
	public List<CcCourseOutline> findByVersionIdAndStatus(Long versionId, Integer[] status) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select cco.*, major.name majorName from " + tableName + " cco ");
		sql.append("inner join " + CcCourse.dao.tableName + " cc on cc.id = cco.course_id and cc.is_del = ? ");
		params.add(DEL_NO);
		sql.append("inner join " + CcVersion.dao.tableName + " cv on cv.id = cc.plan_id and cv.is_del = ? ");
		params.add(DEL_NO);
		sql.append("inner join " + Office.dao.tableName + " major on major.id = cv.major_id and major.is_del = ? ");
		params.add(DEL_NO);
		sql.append("where cc.plan_id = ? ");
		params.add(versionId);
		sql.append("and cco.is_del = ? ");
		params.add(DEL_NO);
//		sql.append("and cco.status in (" + CollectionKit.convert(status, ",") + ") ");

		return find(sql.toString(), params.toArray());
	}

	/**
	 * 某门课程下某些大纲类型的大纲
	 * @param courseId
	 * @param courseOutlineTypeIdArray
	 * @return
	 */
	public List<CcCourseOutline> findByCourseOutlineTypeIds(Long courseId, Long[] courseOutlineTypeIdArray) {
		String sql = "select * from " + tableName + " where is_del = ? and course_id = ? and outline_type_id in (" + CollectionKit.convert(courseOutlineTypeIdArray, ",") + ")";
		return find(sql, DEL_NO, courseId);
	}
}
