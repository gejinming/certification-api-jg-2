package com.gnet.model.admin;

import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * @type model
 * @description 对学生转入、转出专业表操作，包括对数据的增删改查与列表
 * @table cc_student_transfer
 * @author XZL
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_student_transfer")
public class CcStudentTransfer extends DbModel<CcStudentTransfer>{
	
	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcStudentTransfer dao = new CcStudentTransfer();
	
	/**
	 * 专业转入学生类型
	 */
	public final static Integer TYPE_IN = 1;
	/**
	 * 专业转出学生类型
	 */
	public final static Integer TYPE_OUT = 2;

	/**
	 * 年级和年份差值不能大于4
	 */
	public final static Integer  DIFFERENCE = 4;
	
	
	/**
	 * 学生不能重复转入同一个专业(或从同一个专业转出)
	 * @param majorId
     *           所属专业编号
	 * @param type
	 *           类型，1：转入，2：转出
	 * @param studentNo
	 *          学号
	 * @param originValue
	 *          原先学号(可以为空)
	 * @return
	 */
	public boolean isRepeat(Long majorId, Integer type, String studentNo, String originValue){
		if(StrKit.notBlank(originValue)){
			return Db.queryLong("select count(1) from cc_student_transfer where student_no = ? and student_no != ? and major_id = ? and type = ? and is_del = ? ", studentNo, originValue, majorId, type, Boolean.FALSE) > 0;
		}else{
			return Db.queryLong("select count(1) from cc_student_transfer where student_no = ? and major_id = ? and type = ? and is_del = ? ", studentNo, majorId, type, Boolean.FALSE) > 0;
		}
		
	}
	
	
	/**
	 * 学生不能重复转入同一个专业(或从同一个专业转出)
	 * @param majorId
	 *          所属专业编号
	 * @param type
	 *          类型，1：转入，2：转出
	 * @param studentNo
	 *          学号
	 * @return
	 */
	public boolean isRepeat(Long majorId, Integer type, String studentNo){
		return isRepeat(majorId, type, studentNo, null);
	}


	/**
	 * 某专业近几年转入（转出）学生情况列表分页
	 * 
	 * @return
	 */
	public Page<CcStudentTransfer> page(Pageable pageable, Long majorId, Integer type, Integer startYear, Integer endYear) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("from " + CcStudentTransfer.dao.tableName + " cst ");
		sql.append("where cst.major_id = ? ");
		param.add(majorId);
		sql.append("and cst.type = ? ");
		param.add(type);
		if(startYear != null){
			sql.append("and cst.year >= ? ");
			param.add(startYear);
		}
		if(endYear != null){
			sql.append("and cst.year <= ? ");
			param.add(endYear);
		}
		sql.append("and cst.is_del = ? ");
		param.add(Boolean.FALSE);
		
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			sql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcStudentTransfer.dao.paginate(pageable, "select cst.* ", sql.toString(), param.toArray());
	}	
}
