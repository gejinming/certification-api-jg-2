package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @type model
 * @table cc_ranking_level
 * @author GJM
 * @version 1.0 评分表分析法采用当前这个，之前的所有都作废
 * @date 2020.12.08
 *
 */
@TableBind(tableName = "cc_ranking_level")
public class CcRankingLevel extends DbModel<CcRankingLevel> {


	public static final CcRankingLevel dao = new CcRankingLevel();

	private static final long serialVersionUID = -1128792054791150743L;

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
	public static final BigDecimal LEVEL_TOW_A_VALUE = BigDecimal.valueOf(100);
	/**
	 * 二级制-不及格
	 */
	public static final Integer LEVEL_TOW_B = 2;
	/**
	 * 二级制-不及格-分数
	 */
	public static final BigDecimal LEVEL_TOW_B_VALUE = BigDecimal.valueOf(50);
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
	public static final BigDecimal LEVEL_FIVE_A_VALUE = BigDecimal.valueOf(90);
	/**
	 * 五级制-良
	 */
	public static final Integer LEVEL_FIVE_B = 2;
	/**
	 * 五级制-良-分数
	 */
	public static final BigDecimal LEVEL_FIVE_B_VALUE = BigDecimal.valueOf(80);
	/**
	 * 五级制-中
	 */
	public static final Integer LEVEL_FIVE_C = 3;
	/**
	 * 五级制-中-分数
	 */
	public static final BigDecimal LEVEL_FIVE_C_VALUE = BigDecimal.valueOf(70);
	/**
	 * 五级制-及格
	 */
	public static final Integer LEVEL_FIVE_D = 4;
	/**
	 * 五级制-及格-分数
	 */
	public static final BigDecimal LEVEL_FIVE_D_VALUE = BigDecimal.valueOf(60);
	/**
	 * 五级制-不及格
	 */
	public static final Integer LEVEL_FIVE_E = 5;
	/**
	 * 五级制-不及格-分数
	 */
	public static final BigDecimal LEVEL_FIVE_E_VALUE = BigDecimal.valueOf(50);

	/**
	 * 层级名称是否存在判断
	 * 
	 * @param levelName
	 * @param originValue  排除值，一般用于编辑校验
	 * @return
	 */
	public boolean isExisted(Long majorId, String levelName, String originValue) {
		if (StrKit.notBlank(originValue)) {
			return Db.queryLong("select count(1) from cc_evalute_level where major_id = ? and level_name = ? and level_name != ? and is_del = ? ", majorId, levelName, originValue, Boolean.FALSE) > 0;
		} else {
			return Db.queryLong("select count(1) from cc_evalute_level where major_id = ?  and level_name = ? and is_del = ? ", majorId, levelName, Boolean.FALSE) > 0;
		}
	}
	/*
	 * @param majorId
		 * @param Level
	 * @return java.util.List<com.gnet.model.admin.CcRankingLevel>
	 * @author Gejm
	 * @description: 查询等级制度
	 * @date 2020/12/8 15:25
	 */
	public List<CcRankingLevel> findLevelList(Long majorId,Integer Level){
		ArrayList<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select id,level_name levelName,score,remark from cc_ranking_level where major_id = ? and is_del =0 ");
		params.add(majorId);
		if (Level != null){
			sql.append("and level=? ");
			params.add(Level);
		}
		return find(sql.toString(),params.toArray());

	}
	/*
	 * @param majorId
		 * @param Level
	 * @return com.gnet.model.admin.CcRankingLevel
	 * @author Gejm
	 * @description: 找到等级的最大分
	 * @date 2020/12/9 16:07
	 */
	public CcRankingLevel finLevelMaxScore(Long majorId,Integer Level){
		StringBuilder sql = new StringBuilder("select * from cc_ranking_level where major_id=? and level=? and is_del=0 HAVING  max(score) ");
		return findFirst(sql.toString(),majorId,Level);
	}

}
