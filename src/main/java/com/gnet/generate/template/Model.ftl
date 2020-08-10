package com.gnet.model.admin;

import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.gnet.pager.Pageable;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;

import org.apache.commons.lang3.StringUtils;


/**
 * 
 * @type model
 * @table ${tableName}
 * @author ${author}
 * @version 1.0
 * @date ${currentDate?string("yyyy年MM月dd日 HH:mm:ss")}
 *
 */
@TableBind(tableName = "${tableName}")
public class ${moduleName} extends DbModel<${moduleName}> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final ${moduleName} dao = new ${moduleName}();
	
	/**
	 * ${uniqueCName}是否存在判断
	 * 
	 * @param ${uniqueNameCamel}
	 * @param originValue  排除值，一般用于编辑校验
	 * @return
	 */
	public boolean isExisted(${uniqueType} ${uniqueNameCamel}, ${uniqueType} originValue) {
		<#if uniqueType == "String">
		if (StrKit.notBlank(originValue)) {
			return Db.queryLong("select count(1) from ${tableName} where ${uniqueName} = ? and ${uniqueName} != ? and is_del = ? ", ${uniqueNameCamel}, originValue, Boolean.FALSE) > 0;
		} else {
			return Db.queryLong("select count(1) from ${tableName} where ${uniqueName} = ? and is_del = ? ", ${uniqueNameCamel}, Boolean.FALSE) > 0;
		}
		<#else>
		if (originValue == null) {
			return Db.queryLong("select count(1) from ${tableName} where ${uniqueName} = ? and ${uniqueName} != ? and is_del = ?  ", ${uniqueNameCamel}, originValue, Boolean.FALSE) > 0;
		} else {
			return Db.queryLong("select count(1) from ${tableName} where ${uniqueName} = ? and is_del = ? ", ${uniqueNameCamel}, Boolean.FALSE) > 0;
		}
		</#if>
	}
	
	/**
	 * 
	 * 是否存在此${uniqueCName}
	 * 
	 * @description 根据${uniqueCName}查询是否存在该${moduleName}
	 * @sql select count(1) from ${tableName} where ${uniqueName}=?
	 * @version 1.0
	 * @param ${uniqueNameCamel}
	 * @return
	 */
	public boolean isExisted(${uniqueType} ${uniqueNameCamel}) {
		return isExisted(${uniqueNameCamel}, null);
	}

	/**
	 * 获得${moduleCName}列表(不分页)
	 * @param pageable 
	<#list records as record>
	 <#if record.isSearch>
	 * @param ${record.COLUMN_CAMEL_NAME}
	 </#if>
	 </#list>
	 * @return
	 */
	 <#assign num = 0>
	public List<${moduleName}> findAll(Pageable pageable, <#list records as record><#if record.isSearch><#if num != 0>, </#if><#assign num = num + 1>${record.searchProps.type} ${record.COLUMN_CAMEL_NAME}</#if></#list>) {
		StringBuilder exceptSql = new StringBuilder("select * from " + ${moduleName}.dao.tableName + " ");
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append("where is_del = ? ");
		params.add(Boolean.FALSE);
		// 删选条件
		<#list records as record>
		<#if record.isSearch>
		<#if record.searchProps.type == "String">
		if (!StrKit.isBlank(${record.COLUMN_CAMEL_NAME})) {
			exceptSql.append("and ${record.COLUMN_NAME} like '" + org.apache.commons.lang.StringEscapeUtils.escapeSql(${record.COLUMN_CAMEL_NAME}) + "%' ");
		}
		<#else>
		if (${record.COLUMN_CAMEL_NAME} != null) {
			exceptSql.append("and ${record.COLUMN_NAME} = ? ");
			params.add(${record.COLUMN_CAMEL_NAME});
		}
		</#if>
		</#if>
		</#list>
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return find(exceptSql.toString(), params.toArray());
	}

}
