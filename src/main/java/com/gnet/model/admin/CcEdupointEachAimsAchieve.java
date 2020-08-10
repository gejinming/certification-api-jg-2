package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;

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

}
