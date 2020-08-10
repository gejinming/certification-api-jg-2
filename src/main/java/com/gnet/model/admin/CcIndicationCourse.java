package com.gnet.model.admin;


import java.math.BigDecimal;
import java.util.List;

import com.sun.tools.corba.se.idl.toJavaPortable.InterfaceGen;
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
 * @description 指标点与课程关系操作，包括对数据的增删改查与列表
 * @table cc_indication_course
 * @author SY
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_indication_course")
public class CcIndicationCourse extends DbModel<CcIndicationCourse> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcIndicationCourse dao = new CcIndicationCourse();
	
	public final static BigDecimal MAX_WEIGHT = BigDecimal.valueOf(1);
	
	public final static BigDecimal MIN_WEIGHT = BigDecimal.valueOf(0);

	/**
	 * 获得指标点课程关系详情
	 * @param id
	 * 			编号
	 * @return
	 */
	public CcIndicationCourse findFilteredById(Long id) {
		StringBuilder exceptSql = new StringBuilder("select cic.*, cc.name courseName, ci.content indicationContent, ci.index_num indicationIndexNum, cg.index_num graduateIndexNum, cg.id graduateId, cg.content graduateContent from " + CcIndicationCourse.dao.tableName + " cic ");
		exceptSql.append("left join " + CcCourse.dao.tableName + " cc on cc.id = cic.course_id ");
		exceptSql.append("left join " + CcIndicatorPoint.dao.tableName + " ci on ci.id = cic.indication_id ");
		exceptSql.append("left join " + CcGraduate.dao.tableName + " cg on cg.id = ci.graduate_id ");
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append("where cic.id = ? ");
		params.add(id);
		exceptSql.append("and cic.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cc.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and ci.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cg.is_del = ? ");
		params.add(Boolean.FALSE);
		
		return findFirst(exceptSql.toString(), params.toArray());
	}
	public Page<CcIndicationCourse> page(Pageable pageable, Long indicationId, Long courseId, String courseName, String indicationContent, Long planId, Long directionId, Boolean ignoreDirection, Long teacherId,Integer grade) {
		List<Object> params = Lists.newArrayList();
		StringBuilder exceptSql = new StringBuilder("from " + CcIndicationCourse.dao.tableName + " cic ");
		exceptSql.append("inner join " + CcCourse.dao.tableName + " cc on cc.id = cic.course_id ");
		exceptSql.append("inner join " + CcIndicatorPoint.dao.tableName + " ci on ci.id = cic.indication_id ");
		exceptSql.append("inner join " + CcGraduate.dao.tableName + " cg on cg.id = ci.graduate_id ");
		//TODO 2020/06/26配置课程目标只能由课程大纲的执笔人才可以
		if(teacherId != null){
			exceptSql.append("inner join "+CcCourseOutline.dao.tableName+ " cco on cco.course_id=cc.id and cco.is_del= ? and cco.author_id=? and cco.status>=3 ");
			params.add(DEL_NO);
			params.add(teacherId);
		}
        exceptSql.append("inner join cc_version cv on cv.id=cc.plan_id ");
		exceptSql.append("where cic.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cc.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and ci.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cg.is_del = ? ");
		params.add(Boolean.FALSE);
		//TODO 只要是执笔人不管是哪个学院的都可以设置课程目标
		if (planId!=null){
			exceptSql.append("and cc.plan_id = ? ");
			params.add(planId);
		}
		// 删选条件
		//TODO 因为目前是先设置目标再排课，那就不能用任课老师任课的年级筛选了
		if (grade!=null){
			exceptSql.append("and cv.enable_grade = ? ");
			params.add(grade);
		}
		// 删选条件
		if (indicationId != null) {
			exceptSql.append("and cic.indication_id = ? ");
			params.add(indicationId);
		}
		if (courseId != null) {
			exceptSql.append("and cic.course_id = ? ");
			params.add(courseId);
		}

		if(!ignoreDirection){
			if(directionId != null){
				exceptSql.append("and ( cc.direction_id = ? or cc.direction_id is null ) ");
				params.add(directionId);
			}else{
				exceptSql.append("and cc.direction_id is null ");
			}
		}
		if(StrKit.notBlank(courseName)) {
			exceptSql.append("and cc.name like '%" + StringEscapeUtils.escapeSql(courseName) + "%' ");
		}
		if(StrKit.notBlank(indicationContent)) {
			exceptSql.append("and ci.content like '%" + StringEscapeUtils.escapeSql(indicationContent) + "%' ");
		}

		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			if(pageable.getOrderProperty().equals("convert(graduateIndicationIndexNum using gbk )")){
				exceptSql.append("order by cg.index_num " + pageable.getOrderDirection() + " , ci.index_num " +  pageable.getOrderDirection());
			}else{
				exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
			}
		}else{
			exceptSql.append("order by cg.index_num, ci.index_num ");
		}

		return CcIndicationCourse.dao.paginate(pageable, "select distinct cic.*, cc.name courseName, ci.content indicationContent, ci.index_num, cg.index_num graduateIndexNum, cg.id graduateId, cg.content graduateContent,cv.state ", exceptSql.toString(), params.toArray());
	}
	/*public Page<CcIndicationCourse> page(Pageable pageable, Long indicationId, Long courseId, String courseName, String indicationContent, Long planId, Long directionId, Boolean ignoreDirection, Long teacherId,Integer grade) {
		List<Object> params = Lists.newArrayList();
		StringBuilder exceptSql = new StringBuilder("from " + CcIndicationCourse.dao.tableName + " cic ");
		exceptSql.append("inner join " + CcCourse.dao.tableName + " cc on cc.id = cic.course_id ");
		exceptSql.append("inner join " + CcIndicatorPoint.dao.tableName + " ci on ci.id = cic.indication_id ");
		exceptSql.append("inner join " + CcGraduate.dao.tableName + " cg on cg.id = ci.graduate_id ");
		if(teacherId != null){
			exceptSql.append("inner join " + CcTeacherCourse.dao.tableName + " ctc on ctc.course_id = cc.id and ctc.is_del = ? and ctc.teacher_id = ? ");
			params.add(DEL_NO);
			params.add(teacherId);
		}
		exceptSql.append("where cic.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cc.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and ci.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cg.is_del = ? ");
		params.add(Boolean.FALSE);
		if (planId!=null){
			exceptSql.append("and cc.plan_id = ? ");
			params.add(planId);
		}


		// 删选条件
		if (grade!=null){
			exceptSql.append("and ctc.grade = ? ");
			params.add(grade);
		}
		if (indicationId != null) {
			exceptSql.append("and cic.indication_id = ? ");
			params.add(indicationId);
		}
		if (courseId != null) {
			exceptSql.append("and cic.course_id = ? ");
			params.add(courseId);
		}

		if(!ignoreDirection){
			if(directionId != null){
				exceptSql.append("and ( cc.direction_id = ? or cc.direction_id is null ) ");
				params.add(directionId);
			}else{
				exceptSql.append("and cc.direction_id is null ");
			}
		}
		if(StrKit.notBlank(courseName)) {
			exceptSql.append("and cc.name like '%" + StringEscapeUtils.escapeSql(courseName) + "%' ");
		}
		if(StrKit.notBlank(indicationContent)) {
			exceptSql.append("and ci.content like '%" + StringEscapeUtils.escapeSql(indicationContent) + "%' ");
		}

		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			if(pageable.getOrderProperty().equals("convert(graduateIndicationIndexNum using gbk )")){
				exceptSql.append("order by cg.index_num " + pageable.getOrderDirection() + " , ci.index_num " +  pageable.getOrderDirection());
			}else{
				exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
			}
		}else{
			exceptSql.append("order by cg.index_num, ci.index_num ");
		}

		return CcIndicationCourse.dao.paginate(pageable, "select cic.*, cc.name courseName, ci.content indicationContent, ci.index_num, cg.index_num graduateIndexNum, cg.id graduateId, cg.content graduateContent ", exceptSql.toString(), params.toArray());
	}*/
	
	/**
	 * 获取版本下的所有课程指标点信息
	 * 
	 * @param versionId
	 * @return
	 */
	public List<CcIndicationCourse> findByVersionId(Long versionId) {
		StringBuilder sql = new StringBuilder("select cic.*, cc.direction_id direction_id, cc.course_group_id course_group_id from " + tableName + " cic ");
		sql.append("left join cc_course cc on cc.id = cic.course_id ");
		sql.append("where cc.plan_id = ? and cic.is_del = ? ");
		sql.append("order by cc.direction_id asc");
		return find(sql.toString(), versionId, DEL_NO);
	}
	
	/**
	 * 同一指标点不能关联同一门课程2次
	 * @param courseId
	 * @param id
	 * @param indicationId
	 * @return
	 */
	public boolean isExisted(Long courseId, Long id, Long indicationId) {
		if (id != null) {
			return Db.queryLong("select count(1) from cc_indication_course where course_id = ? and id != ? and is_del = ? and indication_id = ? ", courseId, id, Boolean.FALSE, indicationId) > 0;
		} else {
			return Db.queryLong("select count(1) from cc_indication_course where course_id = ? and is_del = ? and indication_id = ? ", courseId, Boolean.FALSE, indicationId) > 0;
		}
	}
	
	/**
	 * 同一指标点不能关联同一门课程2次
	 * @param courseId
	 * @param indicationId
	 * @return
	 */
	public boolean isExisted(Long courseId, Long indicationId) {
		return isExisted(courseId, null, indicationId);
	}
	
	/**某个指标点的权重和
	 * @param indicationId
	 * @param courseGroupId
	 * @param courseId
	 * @return
	 */
	public List<CcIndicationCourse> getIndicationWeight(Long indicationId, Long courseGroupId, Long courseId, Long directionId) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select cic.*, cc.course_group_id course_group_id, cmd.name direction_name,ccgmg.mange_group_id,ccgtm.teach_group_id from " + tableName + " cic " );
		sql.append("left join " + CcCourse.dao.tableName + " cc on cc.id = cic.course_id ");
		//2020/6/21加课程分组
		sql.append("left join cc_course_group_mange_group ccgmg on cic.course_id=ccgmg.course_id ");
		sql.append("left join cc_course_group_teach_mange ccgtm on ccgmg.mange_group_id=ccgtm.group_id ");
		sql.append("left join " + CcMajorDirection.dao.tableName + " cmd on cmd.id = cc.direction_id ");
		sql.append("where cic.indication_id = ? ");
		param.add(indicationId);
		if(courseGroupId != null){
			sql.append("and ( cc.course_group_id != ? or cc.course_group_id is null ) ");
			param.add(courseGroupId);
		}
		if(courseId != null){
			sql.append("and cc.id != ? ");
			param.add(courseId);
		}
		if (directionId != null) {
			sql.append("and (cc.direction_id = ? or cc.direction_id is null) ");
			param.add(directionId);
		}
		
	    sql.append("and cic.is_del = ? ");
	    param.add(Boolean.FALSE);
	    sql.append("and cc.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("order by cc.direction_id asc ");
		return find(sql.toString(), param.toArray());
	}

	/**
	 * 毕业要求下的指标点权重
	 * @param ccGraduateIds
	 * @param directionId
	 * @return
	 */
	public List<CcIndicationCourse> findByGraduateIdsAndDirectionId(Long[] ccGraduateIds, Long directionId) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select cic.*, cc.course_group_id courseGroupId  ,group_id groupId,ccgtm.teach_group_id  teachGroupId from " + tableName + " cic ");
		sql.append("inner join " + CcIndicatorPoint.dao.tableName + " ci on ci.id = cic.indication_id and ci.graduate_id in ( " + CollectionKit.convert(ccGraduateIds, ",") + " ) and ci.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join " + CcCourse.dao.tableName + " cc on cc.id = cic.course_id and cc.is_del = ? ");
		param.add(DEL_NO);
		sql.append("left join cc_course_group_mange_group ccgmg on ccgmg.course_id=cc.id ");
		sql.append("left join cc_course_group_teach_mange ccgtm on ccgtm.group_id=ccgmg.mange_group_id ");
		sql.append("where cic.is_del = ? ");
		param.add(DEL_NO);
		if(directionId != null){
			sql.append("and ( cc.direction_id = ? or cc.direction_id is null ) ");
			param.add(directionId);
		}else{
			sql.append("and cc.direction_id is null ");
		}

		sql.append("order by ci.id, cc.course_group_id ");
		return find(sql.toString(), param.toArray());
	}
	
	
	/**
	 * 指标点权重
	 * @param indicationId
	 * @param directionId
	 * @return
	 */
	public List<CcIndicationCourse> findByIndicationIdAndDirectionId(Long indicationId, Long directionId) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select cic.*, cc.course_group_id courseGroupId,ccgmg.mange_group_id groupId,ccgtm.teach_group_id teachGroupId from " + tableName + " cic ");
		sql.append("inner join " + CcIndicatorPoint.dao.tableName + "  ci on ci.id = cic.indication_id and ci.id = ? and ci.is_del = ? ");
		param.add(indicationId);
		param.add(DEL_NO);
		sql.append("inner join " + CcCourse.dao.tableName + " cc on cc.id = cic.course_id and cc.is_del = ? ");
		param.add(DEL_NO);
		sql.append("left join cc_course_group_mange_group ccgmg on cic.course_id=ccgmg.course_id ");
		sql.append("left join cc_course_group_teach_mange ccgtm on ccgmg.mange_group_id=ccgtm.group_id ");
		sql.append("where cic.is_del = ? ");
		param.add(DEL_NO);
		if(directionId != null){
			sql.append("and ( cc.direction_id = ? or cc.direction_id is null ) ");
			param.add(directionId);
		}else{
			sql.append("and cc.direction_id is null ");
		}
		
		sql.append("order by ci.index_num, cc.course_group_id ");
		return find(sql.toString(), param.toArray());
	}
	
	/**
	 * 同一指标点同一多选一组的课程
	 * @param courseGroupId
	 * @param indicationId
	 * @return
	 */
	public List<CcIndicationCourse> getSameGroupSameIndicationCourse(Long indicationId, Long courseGroupId) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select cic.* from " + tableName + " cic ");
		sql.append("left join " + CcCourse.dao.tableName + " cc on cc.id = cic.course_id ");
		sql.append("where cic.indication_id = ? ");
		param.add(indicationId);
		sql.append("and cc.course_group_id = ? ");
		param.add(courseGroupId);
		sql.append("and cic.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and cc.is_del = ? ");
		param.add(Boolean.FALSE);
		return find(sql.toString(), param.toArray());
	}
	/*
	 * @param indicationId
		 * @param groupIds
	 * @return java.util.List<com.gnet.model.admin.CcIndicationCourse>
	 * @author Gejm
	 * @description: 根据课程分组统计权重
	 * @date 2020/6/20 22:50
	 */
	public List<CcIndicationCourse> getGroupCourseIndication(Long indicationId, Long teachgroupIds,Integer type){
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select mange_group_id,sum(weight) weight from " + tableName + " cic ");
		sql.append("inner join " + CcCourse.dao.tableName + " cc on cc.id = cic.course_id ");
		sql.append("inner join cc_course_group_mange_group ccgmg on cic.course_id=ccgmg.course_id ");
		sql.append("inner join cc_course_group_teach_mange ccgtm on  ccgmg.mange_group_id=ccgtm.group_id ");
		sql.append("where cic.indication_id = ? ");
		param.add(indicationId);
		sql.append("and ccgtm.teach_group_id=? ");
		param.add(teachgroupIds);
		//sql.append("and mange_group_id in (" + CollectionKit.convert(groupIds, ",")+ ")" );
		sql.append("and cic.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and cc.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("group by mange_group_id order by sum(weight) desc ");
		if (type==1){
			sql.append("limit 1");
		}
		return find(sql.toString(), param.toArray());
	}

	/*
	 * @param teachGroupId
	 * @return boolean
	 * @author Gejm
	 * @description: 判断分级教学里的课程是否全部录入权重
	 * @date 2020/6/20 23:17
	 */
	public List<CcIndicationCourse> teachGroupState(Long teachGroupId,Long indicationId){
		StringBuilder sql = new StringBuilder("select a.*, c.weight from cc_course_group_mange_group a  ");
		sql.append("inner join cc_course_group_teach_mange b on a.mange_group_id=b.group_id ");
		sql.append("left join cc_indication_course c on a.course_id=c.course_id  and c.indication_id="+indicationId);
		sql.append(" where b.teach_group_id= "+teachGroupId);
		sql.append(" and c.weight is null ");
		return find(sql.toString());
	}

	public Page<CcIndicationCourse> page(Pageable pageable, Long indicationId, Long courseGroupId){
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("from " + CcIndicationCourse.dao.tableName + " cic ");
		sql.append("left join " + CcCourse.dao.tableName + " cc on cc.id = cic.course_id ");
		sql.append("left join " + CcIndicatorPoint.dao.tableName + " ci on ci.id = cic.indication_id ");
		sql.append("where cic.indication_id = ? ");
		param.add(indicationId);
		sql.append("and cc.course_group_id = ? ");
		param.add(courseGroupId);
		sql.append("and cic.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and cc.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ci.is_del = ? ");
		param.add(Boolean.FALSE);
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			sql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcIndicationCourse.dao.paginate(pageable, "select cic.*, cc.name courseName, ci.content indicationContent ", sql.toString(), param.toArray());
	}


	/**
	 * 通过课程编号和指标点号返回课程和指标点关系
	 * @param courseId
	 * @param indicationId
	 * @return
	 */
	public CcIndicationCourse findByCourseIdAndIndicationId(Long courseId, Long indicationId) {
		String sql = "select * from " + tableName + " where course_id = ? and indication_id = ? and is_del = ? ";
		return findFirst(sql, courseId, indicationId, Boolean.FALSE);
	}

	/**
	 * 获取指定课程的指标点权重和
	 * @param courseId
	 * @return
	 */
	public BigDecimal getCourseWeight(Long courseId) {
		String sql = "select sum(weight) from " + tableName + " where course_id = ? and is_del = ? ";
		return Db.queryBigDecimal(sql, courseId, DEL_NO);
	}

	/**
	 * 考核分析法下没有学生成绩的课程指标点
	 * @param versionId
	 * @param grade
	 * @return
	 */
	public List<CcIndicationCourse> findNotExistStudentGrade(Long versionId, Integer grade) {
		List<Object> param =Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select cic.* from cc_indication_course cic ");
		sql.append("inner join cc_course cc on cc.id = cic.course_id and cc.plan_id = ? and cc.is_del = ? ");
		param.add(versionId);
		param.add(DEL_NO);
		sql.append("inner join cc_teacher_course ctc on ctc.course_id = cc.id and ctc.grade = ? and ctc.is_del = ? ");
		param.add(grade);
		param.add(DEL_NO);
		sql.append("inner join cc_course_gradecompose ccg on ccg.teacher_course_id = ctc.id and ccg.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_course_gradecompose_indication ccgi on ccgi.course_gradecompose_id = ccg.id and ccgi.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_score_stu_indigrade cssi on cssi.gradecompose_indication_id = ccgi.id and cssi.is_del = ? ");
		param.add(DEL_YES);
		sql.append("where cic.is_del = ? group by cic.id ");
		param.add(DEL_NO);
		return find(sql.toString(), param.toArray());
	}

	/**
	 * 考评点分析法下没有学生成绩的课程指标点
	 * @param versionId
	 * @param grade
	 * @return
	 */
	public List<CcIndicationCourse> findNotExistStudentEvalute(Long versionId, Integer grade) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select cic.* from cc_indication_course cic ");
		sql.append("inner join cc_course cc on cc.id = cic.course_id and cc.plan_id = ? and cc.is_del = ? ");
		param.add(versionId);
		param.add(DEL_NO);
		sql.append("inner join cc_teacher_course ctc on ctc.course_id = cc.id and ctc.grade = ? and ctc.is_del = ? ");
		param.add(grade);
		param.add(DEL_NO);
		sql.append("inner join cc_evalute ce on ce.indication_id = cic.indication_id and ce.teacher_course_id = ctc.id and ce.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_student_evalute cse on cse.evalute_id = ce.id and cse.is_del = ? ");
		param.add(DEL_YES);
		sql.append("where cic.is_del = ? group by cic.id ");
		param.add(DEL_NO);
		
		return find(sql.toString(), param.toArray());
	}

	/**
	 * 通过课程编号，找到其以及其他信息(EM00742)
	 * @param courseId
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年10月11日 上午11:21:50 
	 */
	public List<CcIndicationCourse> findDetailByCourseId(Long courseId) {
		StringBuilder exceptSql = new StringBuilder("select cic.*, cc.name courseName,ci.id indicationId, ci.content indicationContent, ci.index_num, cg.index_num graduateIndexNum, cg.id graduateId, cg.content graduateContent ");
		exceptSql.append("from " + CcIndicationCourse.dao.tableName + " cic ");
		exceptSql.append("left join " + CcCourse.dao.tableName + " cc on cc.id = cic.course_id ");
		exceptSql.append("left join " + CcIndicatorPoint.dao.tableName + " ci on ci.id = cic.indication_id ");
		exceptSql.append("left join " + CcGraduate.dao.tableName + " cg on cg.id = ci.graduate_id ");
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append("where cic.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cc.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and ci.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cg.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cic.course_id = ? ");
		params.add(courseId);
		exceptSql.append("order by cg.index_num, ci.index_num ");
		
		return find(exceptSql.toString(), params.toArray());
	}
}
