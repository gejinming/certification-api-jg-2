package com.gnet.model.admin;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * 
 * @type model
 * @table cc_graduate
 * @author SY
 * @version 1.0
 * @date 2016年06月24日 19:26:29
 *
 */
@TableBind(tableName = "cc_graduate")
public class CcGraduate extends DbModel<CcGraduate> {
	
	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcGraduate dao = new CcGraduate();
	
	/**
	 * 毕业要求序号是否存在判断
	 * @param graduateVerId
	 * 			毕业要求版本编号
	 * @param indexNum
	 * @param originValue  排除值，一般用于编辑校验
	 * @return
	 */
	public boolean isExisted(Long graduateVerId, Integer indexNum, Integer originValue) {
		if(null == originValue) {
			return Db.queryLong("select count(1) from cc_graduate where graduate_ver_id = ? and index_num = ? and is_del = ? ", graduateVerId, indexNum, Boolean.FALSE) > 0;
		} else {
			return Db.queryLong("select count(1) from cc_graduate where graduate_ver_id = ? and index_num = ? and index_num != ? and is_del = ?", graduateVerId, indexNum, originValue, Boolean.FALSE) > 0;
		}
		
	}
	
	/**
	 * 
	 * 是否存在此毕业要求序号
	 * 
	 * @description 根据毕业要求序号查询是否存在该CcGraduate
	 * @sql select count(1) from cc_graduate where index_num=?
	 * @version 1.0
	 * @param graduateVerId
	 * 			毕业要求版本编号
	 * @param indexNum
	 * @return
	 */
	public boolean isExisted(Long graduateVerId, Integer indexNum) {
		return isExisted(graduateVerId, indexNum, null);
	}

	/**
	 * 查看毕业要求列表分页
	 * 
	 * @param graduateVerId
	 * 			毕业要求版本编号
	 * @param indexNum
	 * @param content
	 * @return
	 */
	public Page<CcGraduate> page(Pageable pageable, Long graduateVerId, Integer indexNum, String content) {
		StringBuilder exceptSql = new StringBuilder("from " + CcGraduate.dao.tableName + " cg ");
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append("where cg.graduate_ver_id = ? ");
		params.add(graduateVerId);
		exceptSql.append("and cg.is_del = ? ");
		params.add(Boolean.FALSE);
		// 删选条件
		if (indexNum != null) {
			exceptSql.append("and cg.index_num = ? ");
			params.add(indexNum);
		}
		if (!StrKit.isBlank(content)) {
			exceptSql.append("and cg.content like '" + StringEscapeUtils.escapeSql(content) + "%' ");
		}
		// 增加条件，为非软删除的
		exceptSql.append("and cg.is_del=? ");
		params.add(Boolean.FALSE);
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}else{
			exceptSql.append("order by cg.index_num ");
		}
		
		return CcGraduate.dao.paginate(pageable, "select cg.* ", exceptSql.toString(), params.toArray());
	}
}
