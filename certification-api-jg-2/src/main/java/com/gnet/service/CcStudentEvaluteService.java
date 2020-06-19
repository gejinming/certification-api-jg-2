package com.gnet.service;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.gnet.model.admin.CcStudentEvalute;

/**
 * 学生考评点成绩
 * 
 * @author sll
 * 
 * @date 2016年7月4日19:09:40
 */
@Component("ccStudentEvalute")
public class CcStudentEvaluteService {

	/**
	 * 根据考评点编号删除学生的考评成绩
	 * 
	 * @param evaluteIds
	 * @param date
	 * @return
	 */
	public Boolean deleteByEvaluteId(Long[] evaluteIds, Date date){
		return CcStudentEvalute.dao.deleteByEvaluteId(evaluteIds, date);
	}

}
