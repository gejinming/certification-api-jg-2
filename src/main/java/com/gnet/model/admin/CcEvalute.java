package com.gnet.model.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;


/**
 * 
 * @type model
 * @table cc_evalute
 * @author sll
 * @version 1.0
 * @date 2016年07月04日 15:58:01
 *
 */
@TableBind(tableName = "cc_evalute")
public class CcEvalute extends DbModel<CcEvalute> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcEvalute dao = new CcEvalute();
	
	/**
	 * 同一指标点的比例系数满值
	 */
	public static final BigDecimal MAX_WEIGHT = BigDecimal.valueOf(1);
	
	/**
	 * 同一指标点的比例系数最小值
	 */
	public static final BigDecimal MIN_WEIGHT = BigDecimal.valueOf(0);
	
	/**
	 * 指标点编号是否存在判断
	 * 
	 * @param indicationId
	 * @param originValue  排除值，一般用于编辑校验
	 * @return
	 */
	public boolean isExisted(Long indicationId, Long originValue) {
		if (originValue != null) {
			return Db.queryLong("select count(1) from cc_evalute where indicationId = ? and indicationId != ? and is_del = ?  ", indicationId, originValue, Boolean.FALSE) > 0;
		} else {
			return Db.queryLong("select count(1) from cc_evalute where indicationId = ? and is_del = ? ", indicationId, Boolean.FALSE) > 0;
		}
	}
	
	/**
	 * 
	 * 是否存在此指标点编号
	 * 
	 * @description 根据指标点编号查询是否存在该CcEvalute
	 * @sql select count(1) from cc_evalute where indicationId=?
	 * @version 1.0
	 * @param indicationId
	 * @return
	 */
	public boolean isExisted(Long indicationId) {
		return isExisted(indicationId, null);
	}
	
	/**
	 * 考评点序号是否存在判断
	 * 
	 * @param indexNum
	 * @param originValue  排除值，一般用于编辑校验
	 * @param teacherCourseId
	 * 			教师课程编号
	 * @param indicationId
	 * 			指标点编号
	 * @param evaluteTypeId  
	 * 			考评点类型编号
	 * @return
	 */
	public boolean isExisted(Integer indexNum, Integer originValue, Long teacherCourseId, Long indicationId, Long evaluteTypeId) {
		if (originValue != null) {
			return Db.queryLong("select count(1) from cc_evalute where index_num = ? and teacher_course_id and indication_id = ? and evalute_type_id = ? and index_num != ? and is_del = ?  ", indexNum, teacherCourseId, indicationId, evaluteTypeId, originValue, Boolean.FALSE) > 0;
		} else {
			return Db.queryLong("select count(1) from cc_evalute where index_num = ? and teacher_course_id and indication_id = ? and evalute_type_id = ? and is_del = ? ", indexNum, teacherCourseId, indicationId, evaluteTypeId, Boolean.FALSE) > 0;
		}
	}
	
	/**
	 * 
	 * 是否存在此序号
	 * 
	 * @version 1.0
	 * @param indexNum
	 * @param teacherCourseId
	 * 			教师课程编号
	 * @param indicationId
	 * 			指标点编号
	 * @param evaluteTypeId  
	 * 			考评点类型编号
	 * @return
	 */
	public boolean isExisted(Integer indexNum, Long teacherCourseId, Long indicationId, Long evaluteTypeId) {
		return isExisted(indexNum, null, teacherCourseId, indicationId, evaluteTypeId);
	}

	/**
	 * 获得教师课程考评点列表(EM00360)
	 * @param pageable 
	 * @param indicationId
	 * @param teacherCourseId
	 * @return
	 */
	public Page<CcEvalute> page(Pageable pageable, Long indicationId, Long teacherCourseId) {

		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("from " + CcEvalute.dao.tableName + " ce ");
		sql.append(" inner join " + CcEvaluteType.dao.tableName + " cet on cet.id = ce.evalute_type_id ");
		sql.append(" inner join " + CcTeacherCourse.dao.tableName + " ctc on ctc.id = ce.teacher_course_id ");
		sql.append(" left join " + CcTeacher.dao.tableName + " ct on ct.id = ctc.teacher_id ");
		sql.append(" left join " + CcCourse.dao.tableName + " cc on cc.id = ctc.course_id ");
		sql.append(" left join " + CcIndicatorPoint.dao.tableName + " ci on ci.id = ce.indication_id ");
		
		sql.append("where ce.is_del = ? and ctc.is_del = ? and ci.is_del = ? and cet.is_del = ? ");
		params.add(DEL_NO);
		params.add(DEL_NO);
		params.add(DEL_NO);
		params.add(DEL_NO);
		
		// 删选条件
		if (indicationId != null) {
			sql.append("and ce.indication_id = ? ");
			params.add(indicationId);
		}
		if (teacherCourseId != null) {
			sql.append("and ctc.id = ? ");
			params.add(teacherCourseId);
		}
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			sql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		} else {
			sql.append("order by ce.evalute_type_id, ce.index_num ");
		}
		
		return CcEvalute.dao.paginate(pageable, "select ce.*,cc.name course_name,ct.name teacher_name, ci.content indication_content, cet.percentage, cet.type ", sql.toString(), params.toArray());
	}
	
	/**
	 * 根据编号查看详细
	 * 
	 * @param id
	 * @return
	 */
	public CcEvalute findById(Long id){
		StringBuilder exceptSql = new StringBuilder("select ce.*,ct.name teacher_name,cc.name course_name from " + CcEvalute.dao.tableName + " ce ");
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append(" left join " + CcTeacherCourse.dao.tableName + " ctc on ctc.id = ce.teacher_course_id ");
		exceptSql.append(" left join " + CcTeacher.dao.tableName + " ct on ct.id = ctc.teacher_id ");
		exceptSql.append(" left join " + CcCourse.dao.tableName + " cc on cc.id = ctc.course_id ");
		
		exceptSql.append(" where ce.is_del = ? and ctc.is_del = ? and ct.is_del = ? and  cc.is_del = ? ");
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		
		return findFirst(exceptSql.toString(), params.toArray());
	}
	
	/**
	 * 获得该班级所属课程所有考评点
	 * 
	 * @param eduClassId
	 * @return
	 */
	public List<CcEvalute> findAllByEduClass(Long eduClassId) {
		StringBuilder sql = new StringBuilder("select ce.*, cet.type, cet.percentage type_percentage from " + tableName + " ce ");
		sql.append("inner join " + CcEvaluteType.dao.tableName + " cet on cet.id = ce.evalute_type_id ");
		sql.append("left join cc_educlass cec on cec.teacher_course_id = ce.teacher_course_id ");
		sql.append("where cec.id=? and ce.is_del=?");
		return find(sql.toString(), eduClassId, DEL_NO);
	}
	
	/**
	 * 根据indexNum来对考评点进行排序
	 * 
	 * @param indicationId
	 * @return
	 */
	public List<CcEvalute> findAllOrderByIndexNum(Long indicationId){
		StringBuilder exceptSql = new StringBuilder("select ce.*,cc.name course_name,ct.name teacher_name, ci.content indication_content from " + CcEvalute.dao.tableName + " ce ");
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append(" left join " + CcTeacherCourse.dao.tableName + " ctc on ctc.id = ce.teacher_course_id ");
		exceptSql.append(" left join " + CcTeacher.dao.tableName + " ct on ct.id = ctc.teacher_id ");
		exceptSql.append(" left join " + CcCourse.dao.tableName + " cc on cc.id = ctc.course_id ");
		exceptSql.append(" left join " + CcIndicatorPoint.dao.tableName + " ci on ci.id = ce.indication_id ");
		
		exceptSql.append("where ce.is_del = ? and ctc.is_del = ? and ct.is_del = ? and  cc.is_del = ?");
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		
		// 删选条件
		if (indicationId != null) {
			exceptSql.append("and indication_id = ? ");
			params.add(indicationId);
		}
		//按照indexNum进行升序排序
		exceptSql.append(" order by index_num ");
		
		return find(exceptSql.toString(), params.toArray());
	}
	
	/**
	 * 查找未删除考评点中最大的序号
	 * 
	 * @return
	 */
	public Integer findMaxIndexNum(Long teacherCourseId, Long indicationId, Long evaluteTypeId){
		StringBuilder sql = new StringBuilder("select max(index_num) from " + CcEvalute.dao.tableName + " ce ");
		sql.append("where ce.is_del = ? and teacher_course_id = ? and indication_id = ? and evalute_type_id = ? ");
		return Db.queryInt(sql.toString(), DEL_NO, teacherCourseId, indicationId, evaluteTypeId);
	}

	/**
	 * 通过：指标点课程关系表ids，获取有关联的考评点表(EM00274，EM00277)
	 * @param indicationCourseIds
	 * 			指标点与课程关系表ids
	 * @return
	 */
	public boolean existGradecompose(Long[] indicationCourseIds) {
		if(indicationCourseIds == null || indicationCourseIds.length == 0) {
			return false;
		}
		List<Object> params = new ArrayList<>();
		StringBuilder sb = new StringBuilder("select count(1) from " + tableName + " ce ");
		sb.append("inner join " + CcIndicatorPoint.dao.tableName + " ci on ce.indication_id = ci.id ");
		sb.append("inner join " + CcIndicationCourse.dao.tableName + " cic on cic.indication_id = ci.id ");
		sb.append("inner join " + CcTeacherCourse.dao.tableName + " ctc on ctc.id = ce.teacher_course_id and ctc.course_id = cic.course_id ");
		sb.append("where cic.id in (" + CollectionKit.convert(indicationCourseIds, ",") + ") ");
		sb.append("and ce.is_del = ? ");
		params.add(Boolean.FALSE);
		return Db.queryLong(sb.toString(), params.toArray()) > 0;
	}

	/**
	 * 某个指标点已有的权重
	 * @param indicationId
	 * @param evaluteId
	 * @param teacherCourseId
	 * @param evaluteTypeId
	 * @return
	 */
	public BigDecimal findIndicationExistWeight(Long indicationId, Long evaluteId, Long teacherCourseId, Long evaluteTypeId) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select sum(ce.weight) from " + tableName + " ce ");
		sql.append("where ce.indication_id = ? ");
		param.add(indicationId);
		sql.append("and ce.teacher_course_id = ? ");
		param.add(teacherCourseId);
		sql.append("and ce.evalute_type_id = ? ");
		param.add(evaluteTypeId);
		if(evaluteId != null){
			sql.append("and ce.id != ? ");
			param.add(evaluteId);
		}
		sql.append("and ce.is_del = ? ");
		param.add(Boolean.FALSE);
		return Db.queryBigDecimal(sql.toString(), param.toArray());
	}
	
	/**
	 * 获取课程下的所有指标点与考评点信息(EM00552)
	 * @param courseId
	 * @param eduClassId
	 * @param indicationId
	 * @return
	 */
	public List<CcEvalute> findByEduClassIdAndIndicationId(Long courseId, Long eduClassId, Long indicationId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select cge.index_num graduateIndexNum, cet.type evalute_type, ci.id indication_id, ci.index_num index_num, ci.content content, ci.remark remark, cic.weight indication_weight, ce.index_num evalute_index_num, ce.content evalute_content, ce.remark evalute_remark from " + CcEvalute.dao.tableName + " ce ");		
		sql.append("inner join cc_educlass ces on ces.teacher_course_id = ce.teacher_course_id and ces.id = ? ");
		sql.append("inner join cc_indication ci on ci.id = ce.indication_id ");
		sql.append("inner join " + CcEvaluteType.dao.tableName + " cet on cet.id = ce.evalute_type_id and cet.is_del = ? ");
		sql.append("left join " + CcGraduate.dao.tableName + " cge on cge.id = ci.graduate_id ");
		sql.append("left join cc_indication_course cic on cic.indication_id = ci.id and cic.course_id = ? ");
		sql.append("where ces.id = ? and ce.is_del = ? ");
		params.add(eduClassId);
		params.add(DEL_NO);
		params.add(courseId);
		params.add(eduClassId);
		params.add(DEL_NO);
		if (indicationId != null) {
			sql.append("and ci.id = ? ");
			params.add(indicationId);
		}
		sql.append("order by ci.index_num asc, ce.evalute_type_id asc, ce.index_num asc");
		
		return find(sql.toString(), params.toArray());
	}

	/**
	 * 通过教师课程编号和指标点考评点编号的列表，获取考评点列表。(EM00364)
	 * 排序规则：教师课程编号，指标点编号，类型，排序字段
	 * @param teacherCourseIdAndIndicationIds
	 * 			list<Map<teacherCourseId + "," + indicationId  ，   这个教师课程和指标点下考评点的数量>>
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年1月23日 下午7:00:35 
	 */
	public List<CcEvalute> findFilteredInTeacherCourseIdAndIndicationId(List<Map<String, Long>> teacherCourseIdAndIndicationIds) {
		if(teacherCourseIdAndIndicationIds.isEmpty()) {
			return new ArrayList<>();
		}
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select ce.*, cet.type from " + CcEvalute.dao.tableName + " ce ");
		sql.append("inner join " + CcEvaluteType.dao.tableName + " cet on cet.id = ce.evalute_type_id and cet.is_del = ? ");
		params.add(DEL_NO);
		sql.append("where ce.is_del = ? ");
		params.add(DEL_NO);
		sql.append("and (");
		Integer size = teacherCourseIdAndIndicationIds.size();
		for(Integer i = 0; i < size - 1; i++) {
			Map<String, Long> temp = teacherCourseIdAndIndicationIds.get(i);
			sql.append("(ce.teacher_course_id = ? and ce.indication_id = ? ) or ");
			params.add(temp.get("teacherCourseId"));
			params.add(temp.get("indicationId"));
		}
		// 最后一下
		Map<String, Long> lastTemp = teacherCourseIdAndIndicationIds.get(size - 1);
		sql.append("(ce.teacher_course_id = ? and ce.indication_id = ? )");
		params.add(lastTemp.get("teacherCourseId"));
		params.add(lastTemp.get("indicationId"));
		sql.append(") ");
		sql.append("order by ce.teacher_course_id, ce.indication_id, ce.evalute_type_id, ce.index_num asc ");
		return find(sql.toString(), params.toArray());
	}

	/**
	 * 是否允许修改开课课程年级
	 * @param teacherCourseId
	 * @return
	 */
	public boolean isAllowEditGrade(Long teacherCourseId) {
		String sql = "select count(1) from " + tableName + " where teacher_course_id = ? and is_del = ? ";
		return Db.queryLong(sql, teacherCourseId, DEL_NO) <= 0;
	}

	/**
	 * 通过教师排课获取，并且按照指标点和需要排序(EM00764)
	 * @param teacherCourseId
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年10月24日 下午5:17:44 
	 */
	public List<CcEvalute> findByTeacherCourse(Long teacherCourseId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select ce.* from " + CcEvalute.dao.tableName + " ce ");
		sql.append("inner join cc_indication ci on ci.id = ce.indication_id ");
		sql.append("left join " + CcGraduate.dao.tableName + " cg on cg.id = ci.graduate_id ");
		sql.append("where ce.teacher_course_id = ? and ce.is_del = ? ");
		params.add(teacherCourseId);
		params.add(DEL_NO);
		sql.append("order by cg.index_num asc, ci.index_num asc, ce.index_num asc");
		
		return find(sql.toString(), params.toArray());
	}
	
}
