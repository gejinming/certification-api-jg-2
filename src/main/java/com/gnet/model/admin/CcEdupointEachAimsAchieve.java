package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;

import java.util.ArrayList;

/**
 * 教学班指标点下各个课程目标达成度
 * @type model
 * @table cc_edupoint_each_aims_achieve
 * @author xzl
 * @version 1.0
 * @date 2017年11月17日
 *
 */
@TableBind(tableName = "cc_edupoint_each_aims_achieve")
public class CcEdupointEachAimsAchieve extends DbModel<CcEdupointEachAimsAchieve> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcEdupointEachAimsAchieve dao = new CcEdupointEachAimsAchieve();

	/*
	 * @param edclassId
		 * @param indicationId
	 * @return com.gnet.model.admin.CcEdupointEachAimsAchieve
	 * @author Gejm
	 * @description: 根据教学班id 和课程目标id 查询达成度
	 * @date 2020/9/1 16:34
	 */
	public CcEdupointEachAimsAchieve  findClassIndicationAchieve(Long edclassId,Long indicationId){
		ArrayList<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select * from cc_edupoint_each_aims_achieve ");
		sql.append("where indication_id=? and educlass_id=? ");
		params.add(indicationId);
		params.add(edclassId);
		return findFirst(sql.toString(),params.toArray());

	}

}
