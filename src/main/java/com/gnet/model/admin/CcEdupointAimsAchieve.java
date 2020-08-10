package com.gnet.model.admin;

import java.util.ArrayList;
import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;

/**
 * 教学班指标点下课程目标达成度
 * @type model
 * @table cc_edupoint_aims_achieve
 * @author xzl
 * @version 1.0
 * @date 2017年11月17日
 *
 */
@TableBind(tableName = "cc_edupoint_aims_achieve")
public class CcEdupointAimsAchieve extends DbModel<CcEdupointAimsAchieve> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcEdupointAimsAchieve dao = new CcEdupointAimsAchieve();
	
	/**
	 * 通过教学班编号获当前教学班下面指标点的达成度
	 * @param eduClassId
	 * 			教学班编号
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年11月29日 下午3:58:03 
	 */
	public List<CcEdupointAimsAchieve> findByEduclassId(Long eduClassId) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select ceaa.*, cic.indication_id indicatorPointId ");
		sql.append("from " + tableName + " ceaa ");
		sql.append("inner join " + CcIndicationCourse.dao.tableName + " cic on cic.id = ceaa.indication_course_id ");
		sql.append("where ceaa.educlass_id = ? ");
		params.add(eduClassId);
		return find(sql.toString(), params.toArray());
	}

}
