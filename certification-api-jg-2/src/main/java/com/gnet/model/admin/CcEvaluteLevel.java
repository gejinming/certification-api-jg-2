package com.gnet.model.admin;

import java.math.BigDecimal;
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
 * 
 * @type model
 * @table cc_evalute_level
 * @author sll
 * @version 1.0
 * @date 2016年07月05日 18:32:10
 *
 */
@TableBind(tableName = "cc_evalute_level")
public class CcEvaluteLevel extends DbModel<CcEvaluteLevel> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcEvaluteLevel dao = new CcEvaluteLevel();
	
	/**
	 * 二级制
	 */
	public static final Integer LEVEL_TOW = 2;
	/**
	 * 二级制-及格
	 */
	public static final Integer LEVEL_TOW_A = 1;
	/**
	 * 二级制-及格-分数
	 */
	public static final BigDecimal LEVEL_TOW_A_VALUE = BigDecimal.valueOf(1);
	/**
	 * 二级制-不及格
	 */
	public static final Integer LEVEL_TOW_B = 2;
	/**
	 * 二级制-不及格-分数
	 */
	public static final BigDecimal LEVEL_TOW_B_VALUE = BigDecimal.valueOf(0.5);
	/**
	 * 五级制
	 */
	public static final Integer LEVEL_FIVE = 5;
	/**
	 * 五级制-优
	 */
	public static final Integer LEVEL_FIVE_A = 1;
	/**
	 * 五级制-优-分数
	 */
	public static final BigDecimal LEVEL_FIVE_A_VALUE = BigDecimal.valueOf(1);
	/**
	 * 五级制-良
	 */
	public static final Integer LEVEL_FIVE_B = 2;
	/**
	 * 五级制-良-分数
	 */
	public static final BigDecimal LEVEL_FIVE_B_VALUE = BigDecimal.valueOf(0.8);
	/**
	 * 五级制-中
	 */
	public static final Integer LEVEL_FIVE_C = 3;
	/**
	 * 五级制-中-分数
	 */
	public static final BigDecimal LEVEL_FIVE_C_VALUE = BigDecimal.valueOf(0.6);
	/**
	 * 五级制-及格
	 */
	public static final Integer LEVEL_FIVE_D = 4;
	/**
	 * 五级制-及格-分数
	 */
	public static final BigDecimal LEVEL_FIVE_D_VALUE = BigDecimal.valueOf(0.4);
	/**
	 * 五级制-不及格
	 */
	public static final Integer LEVEL_FIVE_E = 5;
	/**
	 * 五级制-不及格-分数
	 */
	public static final BigDecimal LEVEL_FIVE_E_VALUE = BigDecimal.valueOf(0.2);
	
	/**
	 * 层级名称是否存在判断
	 * 
	 * @param levelName
	 * @param originValue  排除值，一般用于编辑校验
	 * @return
	 */
	public boolean isExisted(Long teacherCourseId, Long indicationId, String levelName, String originValue) {
		if (StrKit.notBlank(originValue)) {
			return Db.queryLong("select count(1) from cc_evalute_level where teacher_course_id = ? and indication_id = ? and level_name = ? and level_name != ? and is_del = ? ", teacherCourseId, indicationId, levelName, originValue, Boolean.FALSE) > 0;
		} else {
			return Db.queryLong("select count(1) from cc_evalute_level where teacher_course_id = ? and indication_id = ? and level_name = ? and is_del = ? ", teacherCourseId, indicationId, levelName, Boolean.FALSE) > 0;
		}
	}
	
	/**
	 * 
	 * 是否存在此层级名称
	 * 
	 * @description 根据层级名称查询是否存在该CcEvaluteLevel
	 * @sql select count(1) from cc_evalute_level where level_name=?
	 * @version 1.0
	 * @param levelName
	 * @return
	 */
	public boolean isExisted(Long teacherCourseId, Long indicationId, String levelName) {
		return isExisted(teacherCourseId, indicationId, levelName, null);
	}

	/**
	 * 查看考评点得分层次关系表列表分页
	 * 
	 * @param levelName
	 * @return
	 */
	public Page<CcEvaluteLevel> page(Pageable pageable, String levelName, Long teacherCourseId, Long indicationId) {
		StringBuilder exceptSql = new StringBuilder("from " + CcEvaluteLevel.dao.tableName + " ");
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append("where is_del = ? ");
		params.add(Boolean.FALSE);
		
		// 删选条件
		if (!StrKit.isBlank(levelName)) {
			exceptSql.append("and level_name like '" + StringEscapeUtils.escapeSql(levelName) + "%' ");
		}
		
		if (teacherCourseId != null) {
			exceptSql.append("and teacher_course_id = ? ");
			params.add(teacherCourseId);
		}
		
		if (indicationId != null) {
			exceptSql.append("and indication_id = ? ");
			params.add(indicationId);
		}
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcEvaluteLevel.dao.paginate(pageable, "select * ", exceptSql.toString(), params.toArray());
	}

	/**
	 * 根据编号查找详情
	 * 
	 * @param id
	 * @return
	 */
	public CcEvaluteLevel findById(Long id){
		
		StringBuilder exceptSql = new StringBuilder(" select cel.*,ct.name teacher_name,cc.name course_name,ci.content indication_content from " + CcEvaluteLevel.dao.tableName + " cel ");
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append(" left join " + CcTeacherCourse.dao.tableName + " ctc on ctc.id = cel.teacher_course_id ");
		exceptSql.append(" left join " + CcTeacher.dao.tableName + " ct on ct.id = ctc.teacher_id ");
		exceptSql.append(" left join " + CcCourse.dao.tableName + " cc on cc.id = ctc.course_id ");
		exceptSql.append(" left join " + CcIndication.dao.tableName + " ci on ci.id = cel.indication_id " );
		
		exceptSql.append(" where cel.is_del = ? and ctc.is_del = ? and ct.is_del = ? and  cc.is_del = ? ");
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		
		return findFirst(exceptSql.toString(), params.toArray());
	}
	

	/**
	 * 通过：指标点课程关系表ids，获取有关联的考评点得分层次表(EM00274，EM00277)
	 * @param indicationCourseIds
	 * 			指标点与课程关系表ids
	 * @return
	 */
	public boolean existGradecompose(Long[] indicationCourseIds) {
		if(indicationCourseIds == null || indicationCourseIds.length == 0) {
			return false;
		}
		List<Object> params = new ArrayList<>();
		StringBuilder sb = new StringBuilder("select count(1) from " + tableName + " cel ");
		sb.append("inner join " + CcIndication.dao.tableName + " ci on cel.indication_id = ci.id ");
		sb.append("inner join " + CcIndicationCourse.dao.tableName + " cic on cic.indication_id = ci.id ");
		sb.append("inner join " + CcTeacherCourse.dao.tableName + " ctc on ctc.id = cel.teacher_course_id and ctc.course_id = cic.course_id ");
		sb.append("where cic.id in (" + CollectionKit.convert(indicationCourseIds, ",") + ") ");
		sb.append("and cel.is_del = ? ");
		params.add(Boolean.FALSE);
		return Db.queryLong(sb.toString(), params.toArray()) > 0;
	}

	/**
	 * 该门课程开课的考评点得分层次
	 * @param courseId
	 * @return
	 */
    public List<CcEvaluteLevel> findByCourseId(Long courseId) {
    	List<Object> param = Lists.newArrayList();
    	StringBuffer sql = new StringBuffer("select cel.* from " + tableName + " cel ");
    	sql.append("inner join cc_teacher_course ctc on ctc.id = cel.teacher_course_id and ctc.is_del = ? ");
    	param.add(DEL_NO);
    	sql.append("inner join cc_course cc on cc.id = ctc.course_id and cc.is_del = ? and cc.id = ? ");
    	param.add(DEL_NO);
    	param.add(courseId);
    	sql.append(" where cel.is_del = ? ");
    	param.add(DEL_NO);
    	sql.append("group by ctc.id ");
    	return find(sql.toString(), param.toArray());
    }
}
