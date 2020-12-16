package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @type model
 * @description 开课课程成绩组成元素与课程目标关联表
 * @table cc_course_gradecompose_indication
 * @author SY
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course_gradecompose_indication")
public class CcCourseGradecomposeIndication extends DbModel<CcCourseGradecomposeIndication> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcCourseGradecomposeIndication dao = new CcCourseGradecomposeIndication();

	/**
	 * 指标点最大权重（可以小于）
	 */
	public final static BigDecimal MAX_WEIGHT = BigDecimal.valueOf(1);
	
	/**
	 * 指标点最大权重（必须大于）
	 */
	public final static BigDecimal MIN_WEIGHT = BigDecimal.valueOf(0);
	
	/**
	 * 根据开课课程成绩组成元素指标点编号获取课程下成绩组成与指标点的关系编号获取关系详细信息(EM00547)
	 *
	 * updated: 根据开课成绩组成元素课程目标标号，获取对应的详细信息(EM00547)
	 * @param id
	 *       开课课程成绩组成元素指标点关联编号
	 * @return
	 */
	public CcCourseGradecomposeIndication findInfoById(Long id) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select ccgi.*, ci.content indicationContent, ci.sort indicationSort, cg.name gradecomposeName "
				+ " from " + tableName + " ccgi ");
		sql.append("inner join " + CcIndication.dao.tableName + " ci on ci.id = ccgi.indication_id ");
		sql.append("inner join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.id = ccgi.course_gradecompose_id ");
		sql.append("inner join " + CcGradecompose.dao.tableName + " cg on cg.id = ccg.gradecompose_id ");
		sql.append("where ccgi.id = ? ");
		param.add(id);
		sql.append("and ccgi.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ci.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ccg.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and cg.is_del = ? ");
		param.add(Boolean.FALSE);
		return findFirst(sql.toString(), param.toArray());
	}
	
	/**
	 * 获取教学班的成绩组成元素与指标点的关联信息
	 * 
	 * @param eduClassId
	 * @return
	 */
	public List<CcCourseGradecomposeIndication> findByEduClassId(Long eduClassId) {
		StringBuilder sql = new StringBuilder("select ccgci.* from " + tableName + " ccgci ");
		sql.append("left join cc_course_gradecompose ccg on ccg.id = ccgci.course_gradecompose_id ");
		sql.append("left join cc_educlass ce on ce.teacher_course_id = ccg.teacher_course_id ");
		sql.append("where ce.id = ? and ccgci.is_del = ?");
		return find(sql.toString(), eduClassId, DEL_NO);
	}
	
	/**
	 * 获取课程下的成绩组成元素与指标点的关联信息
	 * 
	 * @param teacherCourseId
	 * @return
	 */
	public List<CcCourseGradecomposeIndication> findByTeacherCourseId(Long teacherCourseId) {
		StringBuilder sql = new StringBuilder("select ccgci.* from " + tableName + " ccgci ");
		sql.append("left join cc_course_gradecompose ccg on ccg.id = ccgci.course_gradecompose_id ");
		sql.append("where  ccg.teacher_course_id = ? and ccgci.is_del = ?");
		return find(sql.toString(), teacherCourseId, DEL_NO);
	}
	
	/**
	 * 获取课程下的成绩组成元素与指标点的关联信息，以及指标点名称信息(EM00742，EM00746)
	 * 
	 * @param teacherCourseId
	 * @author SY
	 * @return
	 */
	public List<CcCourseGradecomposeIndication> findDetailByTeacherCourseId(Long teacherCourseId) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select ccgci.*, ci.content, ci.sort sort, cgc.name gradecomposeName from " + tableName + " ccgci ");
		sql.append("left join cc_course_gradecompose ccg on ccg.id = ccgci.course_gradecompose_id ");
		sql.append("left join " + CcIndication.dao.tableName + " ci on ci.id = ccgci.indication_id ");
		sql.append("left join " + CcGradecompose.dao.tableName + " cgc on cgc.id = ccg.gradecompose_id ");
		sql.append("where  ccg.teacher_course_id = ? and ccgci.is_del = ? and ci.is_del = ? and cgc.is_del = ? ");
		params.add(teacherCourseId);
		params.add(DEL_NO);
		params.add(DEL_NO);
		params.add(DEL_NO);
		sql.append("order by ci.sort, cgc.id ");
		return find(sql.toString(), params.toArray());
	}
	
	/**
	 * @param teacherCousreId
	 *          
	 * @param indicationId      
	 *  
	 * @return 同一课程同一指标点下成绩组成元素权重和
	 */
	public BigDecimal calculateSumWeights(Long teacherCousreId, Long indicationId) {
        List<Object> param =  Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select ifnull(sum(ccgi.weight),0) from " + tableName + " ccgi ");
        sql.append("left join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.id = ccgi.course_gradecompose_id ");
        
        sql.append("where ccgi.indication_id = ? ");
        param.add(indicationId);
       
        sql.append("and ccg.teacher_course_id = ? ");
        param.add(teacherCousreId);
        
        sql.append("and ccgi.is_del = ? ");
		param.add(Boolean.FALSE);
		
		sql.append("and ccg.is_del = ? ");
		param.add(Boolean.FALSE);
        
		return Db.queryBigDecimal(sql.toString(), param.toArray());
	}

	/**
	 * @param teacherCousreId
	 *          
	 * @param indicatorPointId
	 *  
	 * @return 同一课程同一指标点下成绩组成元素
	 */
	public List<CcCourseGradecomposeIndication> findByTeacherCousreIdAndIndicationId(Long teacherCousreId, Long indicatorPointId) {
		 List<Object> param = Lists.newArrayList();
		 StringBuffer sql = new StringBuffer("select ccgi.*, ccg.gradecompose_id gradecomposeId, cic.indication_id indicatorPointId "
		 		+ ", ci.expected_value expectedValue, ci.sort, ci.content from " + tableName + " ccgi ");
	     sql.append("left join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.id = ccgi.course_gradecompose_id ");
	     sql.append("inner join " + CcIndication.dao.tableName + " ci on ci.id = ccgi.indication_id and ci.is_del = ? ");
	     param.add(DEL_NO);
		 sql.append("inner join " + CcCourseTargetIndication.dao.tableName + " ccti on ci.id = ccti.indication_id ");
		 sql.append("inner join " + CcIndicationCourse.dao.tableName + " cic on cic.id = ccti.indication_course_id and cic.is_del = ? ");
		 param.add(DEL_NO);
	     sql.append("where cic.indication_id = ? ");
	     param.add(indicatorPointId);
	    
	     sql.append("and ccg.teacher_course_id = ? ");
	     param.add(teacherCousreId);
	     
	     sql.append("and ccgi.is_del = ? ");
		 param.add(Boolean.FALSE);
	     
		 return find(sql.toString(), param.toArray());
	}

	
	/**
	 * 查看开课课程成绩组成元素指标点关联表列表分页
	 *
	 * updated: 查看开课课程成绩组成元素和课程目标关联表列表分页
	 * @param indicationId
	 * @param teacherCourseId 
	 * 
	 * @return
	 */
	public Page<CcCourseGradecomposeIndication> page(Pageable pageable, Long teacherCourseId, Long indicationId) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql =  new StringBuffer("from " + CcCourseGradecomposeIndication.dao.tableName + " ccgi ");
		sql.append("inner join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.id = ccgi.course_gradecompose_id ");
		sql.append("inner join " + CcGradecompose.dao.tableName + " cg on cg.id = ccg.gradecompose_id " );
		sql.append("inner join " + CcIndication.dao.tableName + " ci on ci.id = ccgi.indication_id " );
		
		sql.append("where ccg.teacher_course_id = ? ");
		param.add(teacherCourseId);
		
		sql.append("and ccgi.indication_id = ? ");
		param.add(indicationId);
		
		sql.append("and ccgi.is_del = ? ");
		param.add(Boolean.FALSE);
		
		sql.append("and ccg.is_del = ? ");
		param.add(Boolean.FALSE);
		
		sql.append("and cg.is_del  = ? ");
		param.add(Boolean.FALSE);

		sql.append("and ci.is_del  = ? ");
		param.add(Boolean.FALSE);

		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			sql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcCourseGradecomposeIndication.dao.paginate(pageable, "select ccgi.*, cg.name gradecomposeName, ci.content indicationContent ", sql.toString(), param.toArray());
	}


	
	/**
	 * @param idsArry
	 *          开课课程成绩组成元素指标点编号数组
	 * @return 权重之和
	 *       
	 */
	public BigDecimal getWeightsByIds(Long[] idsArry) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select ifnull(sum(ccgi.weight),0) from " + tableName + " ccgi ");
		sql.append("where id in ( " + CollectionKit.convert(idsArry, "," ) + " ) ");
		sql.append("and ccgi.is_del = ? ");
		param.add(Boolean.FALSE);
		return Db.queryBigDecimal(sql.toString(), param.toArray());
	}

	/**
	 * @param idsArry
	 *          开课课程成绩组成元素指标点编号数组
	 * @return 
	 *       
	 */
	public List<CcCourseGradecomposeIndication> findByIds(Long[] idsArry) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select ccgi.* from " + tableName + " ccgi ");
		sql.append("where id in ( " + CollectionKit.convert(idsArry, "," ) + " ) ");
		sql.append("and ccgi.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("order by ccgi.id ");
		return find(sql.toString(), param.toArray());
	}
	
	/**
	 * 通过教学班编号和指标点编号，返回这门开课课程的这个指标点下，有多少成绩组成
	 * @param educlassId
	 *           教学班编号
	 * @param indicationId
	 *          指标点编号
	 * @return
	 */
	public List<CcCourseGradecomposeIndication> findByEduclassIdAndIndicationId(Long educlassId, Long indicationId) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select ccgi.id courseGradecomposeIndicationId, cg.name gradecomposeName, "
				+ "count(ccgd.id) courseGradecomposeDetailIdCount, ccg.id courseGradecomposeId from " + tableName + " ccgi ");
		sql.append("left join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.id = ccgi.course_gradecompose_id ");
		sql.append("left join " + CcGradecompose.dao.tableName + " cg on cg.id =  ccg.gradecompose_id ");
		sql.append("left join " + CcEduclass.dao.tableName + " ce on ce.teacher_course_id = ccg.teacher_course_id ");
		sql.append("left join " + CcCourseGradeComposeDetail.dao.tableName + " ccgd on ccgd.course_gradecompose_id = ccg.id ");
		sql.append("and ccgd.is_del = ? ");
		param.add(Boolean.FALSE);
		
		sql.append("where ce.id = ? ");
		param.add(educlassId);
		
		sql.append("and ccgi.indication_id = ? ");
		param.add(indicationId);
		
		sql.append("and ccgi.is_del = ? ");
		param.add(Boolean.FALSE);
		
		sql.append("and ccg.is_del = ? ");
		param.add(Boolean.FALSE);
		
		sql.append("and cg.is_del = ? ");
		param.add(Boolean.FALSE);
		
		sql.append("and ce.is_del = ? ");
		param.add(Boolean.FALSE);
		
		sql.append("group by ccgi.id, cg.name, ccg.id ");
		sql.append("order by ccg.sort desc, cg.id asc");
		
		return find(sql.toString(), param.toArray());
	}
	
	/**
	 * 获取教师开课课程下的成绩组成与指标点关系列表
	 * @param pageable
	 * @param teacherCourseId
	 * @param courseGradecomposeId 
	 * @return
	 */
	public List<CcCourseGradecomposeIndication> findInfoByTeacherCourseId(Pageable pageable, Long teacherCourseId, Long courseGradecomposeId) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select ccgi.*, cg.name gradecomposeName, ci.content indicationContent,"
				+ " ci.index_num indicationIndexNum from " +  tableName + " ccgi ");
		sql.append("left join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.id = ccgi.course_gradecompose_id ");
		sql.append("left join " + CcGradecompose.dao.tableName + " cg on cg.id = ccg.gradecompose_id ");
		sql.append("left join " + CcIndication.dao.tableName + " ci on ci.id =  ccgi.indication_id ");
		sql.append("where ccgi.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ccg.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and cg.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ci.is_del = ? ");
		param.add(Boolean.FALSE);
        if(courseGradecomposeId != null){
        	sql.append("and ccgi.course_gradecompose_id = ? ");
        	param.add(courseGradecomposeId);
        }
		if(teacherCourseId != null){
			sql.append("and ccg.teacher_course_id = ? ");
			param.add(teacherCourseId);
		}
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			sql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return find(sql.toString(), param.toArray());
	}


	/**
	 * 通过题目编号和开课程成绩组成编号查找开课课程成绩组成关联指标点编号
	 * @param courseGradecomposeId
	 * @param detailIds
	 * @return
	 */
	public List<CcCourseGradecomposeIndication> findByDetailIdAndCourseGradecomposeId(Long courseGradecomposeId, Long[] detailIds){
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select ccgi.id from " + tableName + " ccgi ");
		sql.append("inner join cc_course_gradecompose_detail_indication ccgdi on ccgdi.indication_id = ccgi.indication_id and ccgdi.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_course_gradecompose_detail ccgd on ccgd.id = ccgdi.course_gradecompose_detail_id and ccgd.id in ("+ CollectionKit.convert(detailIds, ",")+") and ccgd.course_gradecompose_id = ? and ccgd.is_del = ? " );
		param.add(courseGradecomposeId);
		param.add(DEL_NO);
		sql.append("where ccgi.course_gradecompose_id = ? and ccgi.is_del = ? ");
		param.add(courseGradecomposeId);
		param.add(DEL_NO);
		return find(sql.toString(), param.toArray());
	}


	/**
	 * 获取某个开课课程成绩组成支持的指标点列表(EM00567)
	 *
	 * updated: 获取 开课成绩组成ID 对应的课程目标列表(EM00567)
	 * @param courseGradecomposeId
	 * @return
	 */
	public List<CcCourseGradecomposeIndication> findByCourseGradecomposeId(Long courseGradecomposeId) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select ci.content indicationContent, ci.sort indicationSort, ci.id indicationId, "
				+ " ccgi.id courseGradecomposeIndicationId, ccgi.max_score maxScore from " + tableName + " ccgi ");
		sql.append("inner join " + CcIndication.dao.tableName + " ci on ci.id =  ccgi.indication_id ");
		sql.append("where ccgi.course_gradecompose_id = ? ");
		param.add(courseGradecomposeId);
		sql.append("and ccgi.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ci.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("order by ci.sort asc ");
		return find(sql.toString(), param.toArray());
	}
	
	/**
	 * 获取某个开课课程成绩组成支持的开课目标列表
	 * @param courseGradecomposeIds
	 * @return
	 * @author SY
	 * @date 2017年10月20日
	 */
	public List<CcCourseGradecomposeIndication> findByCourseGradecomposeIds(Long[] courseGradecomposeIds) {
		if(courseGradecomposeIds == null || courseGradecomposeIds.length == 0) {
			return new ArrayList<>();
		}
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select ccgi.* from " + tableName + " ccgi ");
		sql.append("left join " + CcIndication.dao.tableName + " ci on ci.id =  ccgi.indication_id ");
		sql.append("where ccgi.course_gradecompose_id in ("+ CollectionKit.convert(courseGradecomposeIds, ",")+") ");
		sql.append("and ccgi.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ci.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("order by ci.sort ");
		return find(sql.toString(), param.toArray());
	}

	/**
	 * 返回未删除的开课课程成绩组成元素指标点关联信息
	 * @param id
	 *         开课课程成绩组成元素指标点关联编号
	 * @return
	 */
	public CcCourseGradecomposeIndication findDetailById(Long id) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select ccgi.*, ccg.teacher_course_id from " + tableName + " ccgi ");
		sql.append("left join " + CcIndication.dao.tableName + " ci on ci.id = ccgi.indication_id ");
		sql.append("left join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.id = ccgi.course_gradecompose_id ");
		sql.append("left join " + CcGradecompose.dao.tableName + " cg on cg.id = ccg.gradecompose_id ");
		sql.append("where ccgi.id = ? ");
		param.add(id);
		sql.append("and ccgi.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ci.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ccg.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and cg.is_del = ? ");
		param.add(Boolean.FALSE);
		return findFirst(sql.toString(), param.toArray());
	}

	/**
	 * 返回权重
	 * @param courseGradecomposeId
	 *                开课课程成绩组成元素编号
	 * @param indicationId
	 *                指标点编号
	 * @return
	 */
	public BigDecimal getWeightByIndicationIdAndCourseGradecomposeId(Long courseGradecomposeId, Long indicationId) {
		StringBuffer sql = new StringBuffer("select ifnull(weight, 0) from " + tableName + " ");
		sql.append("where indication_id = ? and course_gradecompose_id = ? and is_del  = ? ");
		return Db.queryBigDecimal(sql.toString(), indicationId, courseGradecomposeId, Boolean.FALSE);
	}

/**
	 * 通过：指标点课程关系表ids，获取有关联的开课课程成绩组成元素指标点关联表(EM00274，EM00277)
	 * @param indicationCourseIds
	 * 			指标点与课程关系表ids
	 * @return
	 */
	public boolean existGradecompose(Long[] indicationCourseIds) {
		if(indicationCourseIds == null || indicationCourseIds.length == 0) {
			return false;
		}
		List<Object> params = new ArrayList<>();
		StringBuilder sb = new StringBuilder("select count(1) from " + tableName + " ccgi ");
		sb.append("inner join " + CcIndication.dao.tableName + " ci on ccgi.indication_id = ci.id ");
		sb.append("inner join " + CcIndicationCourse.dao.tableName + " cic on cic.indication_id = ci.id ");
		sb.append("inner join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.id = ccgi.course_gradecompose_id ");
		sb.append("inner join " + CcTeacherCourse.dao.tableName + " ctc on ctc.id = ccg.teacher_course_id and ctc.course_id = cic.course_id ");
		sb.append("where cic.id in (" + CollectionKit.convert(indicationCourseIds, ",") + ") ");
		sb.append("and ccgi.is_del = ? ");
		params.add(Boolean.FALSE);
		return Db.queryLong(sb.toString(), params.toArray()) > 0;
	}


	/**
	 * 通过教学班编号和指标点返回该教学班指标点和开课课程成绩组成关系
	 * @param courseId
	 * @param eduClassId
	 * 			教学班编号（可以为空）
	 * @param indicatorPointId
	 * 			指标点编号（可以为空）
	 * @return
	 */
	public List<CcCourseGradecomposeIndication> findByEduClassIdAndIndicationId(Long courseId, Long eduClassId, Long indicatorPointId) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select  ccgi.*, cg.name gradecomposeName, cip.index_num, cgd.index_num graduateIndexNum, "
				+ " cip.content, cip.remark indicationRemark, cic.weight indicationWeight, cip.id indicatorPointId from " + tableName + " ccgi ");
		sql.append("inner join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.id = ccgi.course_gradecompose_id and ccg.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join " + CcGradecompose.dao.tableName + " cg on cg.id =  ccg.gradecompose_id and cg.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join " + CcEduclass.dao.tableName + " ce on ce.teacher_course_id = ccg.teacher_course_id and ce.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join " + CcTeacherCourse.dao.tableName + " ctc on ctc.id = ccg.teacher_course_id and ctc.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join " + CcIndicationCourse.dao.tableName + " cic on cic.course_id = ctc.course_id and ctc.course_id = ? and cic.is_del = ? ");
		param.add(courseId);
		param.add(DEL_NO);
		sql.append("inner join " + CcIndicatorPoint.dao.tableName + " cip on cip.id = cic.indication_id and cip.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join " + CcGraduate.dao.tableName + " cgd on cgd.id = cip.graduate_id and cgd.is_del = ? ");
		param.add(DEL_NO);
		sql.append("where ccgi.is_del = ? ");
		param.add(DEL_NO);
		
		if(indicatorPointId != null){
			sql.append("and cic.indication_id = ? ");
			param.add(indicatorPointId);
		}
		if(eduClassId != null){
			sql.append("and ce.id = ? ");
			param.add(eduClassId);
		}
		sql.append("group by ccgi.id, cg.name, ccg.id, cip.id ");
		sql.append("order by ccg.sort desc, cg.id asc");
		
		return find(sql.toString(), param.toArray());
	}

	/**
	 * 开课课程下的成绩组成以及指标点情况(EM00763)
	 * @param teacherCourseId
	 * @returns
	 */
	public List<CcCourseGradecomposeIndication> findDetailsByTeacherCourseId(Long teacherCourseId){
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select ccgi.weight, ccgi.max_score, ci.sort indicationIndexNum, ci.content indicationContent, cg.index_num graduateIndexNum, ci.id indicationId, cg.content graduateContent, " +
//		StringBuilder sql = new StringBuilder("select ccgi.weight, ccgi.max_score, ci.index_num indicationIndexNum, ci.content indicationContent, cg.index_num graduateIndexNum, ci.id indicationId, cg.content graduateContent, " +
				"cge.name gradecomposeName,ccg.id courseGradecomposeId, ccgi.id gradecompose_indication_id from " + tableName  + " ccgi ");
		sql.append("inner join cc_course_gradecompose ccg on ccg.id = ccgi.course_gradecompose_id and ccg.teacher_course_id = ? and ccg.is_del = ? ");
		param.add(teacherCourseId);
		param.add(DEL_NO);
		sql.append("inner join cc_indication ci on ci.id = ccgi.indication_id and ci.is_del = ? ");
		param.add(DEL_NO);
		/** add start **/
		sql.append("inner join cc_course_target_indication ccti on ccti.indication_id = ci.id ");
		sql.append("inner join cc_indication_course cic on cic.id = ccti.indication_course_id and cic.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_indicator_point cip on cip.id = cic.indication_id and cip.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_graduate cg on cg.id = cip.graduate_id and cg.is_del = ? ");
		/** add end **/
//		sql.append("inner join cc_graduate cg on cg.id = ci.graduate_id and cg.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_gradecompose cge on cge.id = ccg.gradecompose_id and cge.is_del = ? ");
		param.add(DEL_NO);
		sql.append("where ccgi.is_del = ? ");
		param.add(DEL_NO);
		sql.append("order by cg.index_num, ci.sort ");
//		sql.append("order by cg.index_num, ci.index_num ");

		return find(sql.toString(), param.toArray());

	}

	/**
	 * 获取课程下的成绩组成元素与指标点的关联信息，以及指标点名称信息
	 *
	 * @param teacherCourseId
	 * @param courseGradeComposeIds
	 * @author SY
	 * @return
	 */
	public List<CcCourseGradecomposeIndication> findDetailByTeacherCourseIdAndCourseGradeComposeIds(Long teacherCourseId, List<Long> courseGradeComposeIds) {
		StringBuilder sql = new StringBuilder("select ccgci.*, ci.content, ci.sort, cgc.name gradecomposeName from " + tableName + " ccgci ");
		sql.append("inner join cc_course_gradecompose ccg on ccg.id = ccgci.course_gradecompose_id ");
		sql.append("inner join " + CcIndication.dao.tableName + " ci on ci.id = ccgci.indication_id ");
		sql.append("inner join " + CcGradecompose.dao.tableName + " cgc on cgc.id = ccg.gradecompose_id ");
		sql.append("where  ccg.teacher_course_id = ? and ccgci.is_del = ? and ci.is_del = ? and cgc.is_del = ? ");
		if(courseGradeComposeIds != null && !courseGradeComposeIds.isEmpty()) {
			sql.append("and ccg.id in ("+ CollectionKit.convert(courseGradeComposeIds, ",")+") ");
		}
		sql.append("order by cgc.id, ccg.sort, ci.sort ");
		return find(sql.toString(), teacherCourseId, DEL_NO, DEL_NO, DEL_NO);
	}

	/**
	 * 通过课程目标编号列表查找数据
	 * @param indicationIdList
	 * 			课程目标编号列表
	 * @return
	 * @author SY
	 * @version 创建时间：2017年11月29日 下午3:32:55
	 */
	public List<CcCourseGradecomposeIndication> findByIndicationIds(List<Long> indicationIdList) {
		if(indicationIdList == null || indicationIdList.isEmpty()) {
			return new ArrayList<>();
		}
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select distinct ccgi.*, cg.name gradecomposeName, cess.total_score totalScore"
				+ ", cess.avg_score avgScore, cess.except_total_score exceptTotalScore"
				+ ", cess.except_avg_score exceptAvgScore, ce.id educlassId ");
		sql.append("from " + tableName + " ccgi ");
		sql.append("inner join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.id = ccgi.course_gradecompose_id and ccg.is_del = ? ");
		sql.append("inner join " + CcEduindicationStuScore.dao.tableName + " cess on cess.gradecompose_indication_id = ccgi.id ");
		params.add(DEL_NO);
		sql.append("inner join " + CcGradecompose.dao.tableName + " cg on cg.id = ccg.gradecompose_id and cg.is_del = ? ");
		params.add(DEL_NO);
		sql.append("inner join " + CcEduclass.dao.tableName + " ce on ce.teacher_course_id = ccg.teacher_course_id and ce.is_del = ? ");
		params.add(DEL_NO);
		sql.append("where ccgi.is_del = ? ");
		params.add(DEL_NO);
		sql.append("and ccgi.indication_id in (" + CollectionKit.convert(indicationIdList, ",") + ") ");
		// cess.gradecompose_indication_id = ccgi.id 这个条件导致教学班不一定一致，所以现在加个条件
		sql.append("and cess.educlass_id = ce.id order by cg.id ");
		return find(sql.toString(), params.toArray());
	}

	/**
	 *  查找这些开课课程下的成绩组成课程目标
	 * @param teacherCourseIds
	 * @return
	 */
	public List<CcCourseGradecomposeIndication> findByTeacherCourseIds(List<Long> teacherCourseIds) {
		StringBuilder sql = new StringBuilder("select ccgi.*, ctc.id  teacherCourseId ,cg.name,cg.id gradecomposeId,ccg.id courseGradecomposeId from " + tableName + " ccgi ");
		sql.append("inner join " + CcCourseGradecompose.dao.tableName + " ccg on ccgi.course_gradecompose_id = ccg.id and ccg.is_del = ? ");
		sql.append("inner join " + CcTeacherCourse.dao.tableName + " ctc on ccg.teacher_course_id = ctc.id and ctc.is_del = ? and ctc.id in (" + CollectionKit.convert(teacherCourseIds, ",") + ") ");
		sql.append("inner join " + CcGradecompose.dao.tableName + " cg on cg.id= ccg.gradecompose_id and cg.is_del=0 ");
		sql.append("where ccgi.is_del = ? ");
		sql.append("order by ccgi.indication_id,cg.id ");
		return find(sql.toString(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
	}

    /**
     * 与该题目有关联的开课课程成绩组成元素指标目标关联数据
     * @param courseGradeComposeDetailId
     * @return
     */
    public List<CcCourseGradecomposeIndication> findByCourseGradeComposeDetailId(Long courseGradeComposeDetailId) {
        List<Object> param = Lists.newArrayList();
        StringBuilder sql = new StringBuilder("select ccgi.* from " + tableName + " ccgi ");
        sql.append("inner join " + CcCourseGradeComposeDetail.dao.tableName + " ccgd on ccgd.course_gradecompose_id = ccgi.course_gradecompose_id and ccgd.id = ? and ccgd.is_del = ? ");
        param.add(courseGradeComposeDetailId);
        param.add(DEL_NO);
        sql.append("inner join " + CcCourseGradecomposeDetailIndication.dao.tableName + " ccgdi on ccgdi.course_gradecompose_detail_id = ccgd.id and ccgdi.indication_id = ccgi.indication_id and ccgdi.is_del = ? ");
        param.add(DEL_NO);
        sql.append("where ccgi.is_del = ? ");
        param.add(DEL_NO);
        return find(sql.toString(), param.toArray());
    }


    /**
     * 与题目有关联的开课课程成绩组成元素课程目标关联数据
     * @param idsArray
     * @return
     */
    public List<CcCourseGradecomposeIndication> getByIds(Long[] idsArray) {
        List<Object> param = Lists.newArrayList();
        StringBuilder sql = new StringBuilder("select ccgi.* from " + tableName + " ccgi ");
        sql.append("inner join " + CcCourseGradeComposeDetail.dao.tableName + " ccgd on ccgd.course_gradecompose_id = ccgi.course_gradecompose_id and ccgd.is_del = ? ");
        param.add(DEL_NO);
        sql.append("inner join " + CcCourseGradecomposeDetailIndication.dao.tableName + " ccgdi on ccgdi.course_gradecompose_detail_id = ccgd.id and ccgdi.indication_id = ccgi.indication_id and ccgdi.is_del = ? ");
        param.add(DEL_NO);
        sql.append("where ccgi.is_del = ? and ccgi.id in (" + CollectionKit.convert(idsArray, ",") + ")");
        param.add(DEL_NO);
        return find(sql.toString(), param.toArray());
    }
    /*
     * @param teacherCourseId
     * @return java.util.List<com.gnet.model.admin.CcCourseGradecomposeIndication>
     * @author Gejm
     * @description: 查询期末成绩各个课程目标的的总分
     * @date 2020/8/17 15:55
     */
	public List<CcCourseGradecomposeIndication> findFinalIndictionScore(Long teacherCourseId) {
		StringBuilder sql = new StringBuilder("select ccgci.*, ci.id indicationId,ci.content, ci.sort, cgc.name gradecomposeName from " + tableName + " ccgci ");
		sql.append("inner join cc_course_gradecompose ccg on ccg.id = ccgci.course_gradecompose_id ");
		sql.append("inner join " + CcIndication.dao.tableName + " ci on ci.id = ccgci.indication_id ");
		sql.append("inner join " + CcGradecompose.dao.tableName + " cgc on cgc.id = ccg.gradecompose_id ");
		sql.append("where  ccg.teacher_course_id = ? and ccgci.is_del = ? and ci.is_del = ? and cgc.is_del = ? and cgc.name='期末成绩' ");

		sql.append("order by cgc.id, ccg.sort, ci.sort ");
		return find(sql.toString(), teacherCourseId, DEL_NO, DEL_NO, DEL_NO);
	}
	/*
	 * @param courseGradeComposeId
		 * @param indicationId
	 * @return java.util.List<com.gnet.model.admin.CcCourseGradecomposeIndication>
	 * @author Gejm
	 * @description: 查询课程目标权重满分
	 * @date 2020/8/27 15:34
	 */
	public CcCourseGradecomposeIndication findGradecomposeIndication(Long courseGradeComposeId,Long indicationId){
		StringBuilder sql = new StringBuilder(" select * from cc_course_gradecompose_indication ");
		sql.append("where is_del=? and course_gradecompose_id=? and indication_id=? ");
		return findFirst(sql.toString(), DEL_NO,courseGradeComposeId,indicationId);
	}
	/*
	 * @param edClassId
		 * @param indicationId
		 * @param gradeComposeId
	 * @return com.gnet.model.admin.CcCourseGradecomposeIndication
	 * @author Gejm
	 * @description: 根据教学班id和课程目标id、成绩组成id，获取权重和满分
	 * @date 2020/9/7 17:23
	 */
	public CcCourseGradecomposeIndication findGradecomposeMaxscoreAndWeight(Long edClassId,Long indicationId,Long gradeComposeId){
		ArrayList<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select cgi.*  from cc_course_gradecompose_indication cgi ");
		sql.append("left join cc_course_gradecompose ccg on cgi.course_gradecompose_id=ccg.id and ccg.is_del=0 ");
		sql.append("left join cc_teacher_course ctc on ctc.id=ccg.teacher_course_id and ctc.is_del=0 ");
		sql.append("left join cc_educlass ce on ce.teacher_course_id=ctc.id and ce.is_del=0 ");
		sql.append("where ce.id=? and cgi.is_del=0 and cgi.indication_id=? and ccg.gradecompose_id=? ");
		params.add(edClassId);
		params.add(indicationId);
		params.add(gradeComposeId);

		return findFirst(sql.toString(),params.toArray());

	}
	public List<CcCourseGradecomposeIndication> findcCourseGradecomposeIndicationList(Long courseGradeComposeId){
		StringBuilder sql = new StringBuilder("select * from cc_course_gradecompose_indication where course_gradecompose_id =? and is_del=0 ");
		return find(sql.toString(),courseGradeComposeId);

	}
}
