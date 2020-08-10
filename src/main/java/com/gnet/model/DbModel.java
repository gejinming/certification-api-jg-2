package com.gnet.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.gnet.pager.Pageable;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("rawtypes")
public abstract class DbModel<M extends Model> extends Model<M> {

	private static final long serialVersionUID = 3853396861705692013L;

	/***************************************************************************
	 * 用来当 缓存名字 也用来 生成 简单sql
	 */
	public String tableName;

	/**
	 * 标志删除
	 */
	public static final Boolean DEL_YES = Boolean.TRUE;
	/**
	 * 标志未删除
	 */
	public static final Boolean DEL_NO = Boolean.FALSE;

	/**
	 * 是否删除字段
	 */
	public static final String IS_DEL_LABEL = "is_del";

	/***************************************************************************
	 * 反射获取 注解获得 tablename
	 */
	public DbModel() {
		TableBind table = this.getClass().getAnnotation(TableBind.class);
		if (table != null)
			tableName = table.tableName();
	}

	/**
	 * 获取page，通过判断内部的number和size，判断是否需要分页，不分页就是返回为一页的page。
	 * @param pageable
	 * @param selectString
	 * @param sql
	 * @param param
	 * @return
	 * @author SY 
	 * @version 创建时间：2016年10月26日 上午10:46:00 
	 */
	public Page<M> paginate (Pageable pageable, String selectString, String sql, Object[] param) {
		Boolean isPaging = pageable.isPaging();
		
		if(!isPaging){
			// 不分页
			List<M> list = find(selectString + sql, param);
			Integer size = list.size();
			return new Page<>(list, 1, size, 1, size);
		} else {
			return paginateExceptOverstep(pageable, selectString, sql, param);
		}
	}
	
	/**
	 * 计数 v1.2.1 增加
	 * 
	 * @param column
	 *            列名
	 * @param value
	 *            列值
	 * @return 计数结果
	 */
	public Long count(String column, Object value) {
		return count(column, value, false);
	}

	/**
	 * 计数（过滤已删除） v1.2.1 增加
	 * 
	 * @param column
	 *            列名
	 * @param value
	 *            列值
	 * @return 计数结果
	 */
	public Long countFiltered(String column, Object value) {
		return count(column, value, true);
	}

	/**
	 * 计数 v1.2.1 增加
	 * 
	 * @param column
	 *            列名
	 * @param value
	 *            列值
	 * @param isFiltered
	 *            是否过滤已删除
	 * @return 计数结果
	 */
	public Long count(String column, Object value, Boolean isFiltered) {
		Map<String, Object> paras = new HashMap<String, Object>();
		paras.put(column, value);

		return count(paras, isFiltered);
	}

	/**
	 * 计数 v1.2.1 增加
	 * 
	 * @param paras
	 *            参数
	 * @return 计数结果
	 */
	public Long count(Map<String, Object> paras) {
		return count(paras, false);
	}

	/**
	 * 计数（已过滤删除） v1.2.1 增加
	 * 
	 * @param paras
	 *            参数
	 * @return 计数结果
	 */
	public Long countFiltered(Map<String, Object> paras) {
		return count(paras, true);
	}

	/**
	 * 计数 v1.2.1 增加
	 * 
	 * @param paras
	 *            参数
	 * @param isFiltered
	 *            是否过滤已删除
	 * @return 计数结果
	 */
	public Long count(Map<String, Object> paras, Boolean isFiltered) {
		StringBuilder sql = new StringBuilder("select count(1) from " + tableName + " where 1=1 ");

		List<Object> params = new ArrayList<Object>();
		Iterator<Map.Entry<String, Object>> iter = paras.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, Object> para = iter.next();
			sql.append("and " + para.getKey() + "=? ");
			params.add(para.getValue());
		}

		if (isFiltered) {
			sql.append("and " + IS_DEL_LABEL + "=0 ");
		}

		return Db.queryLong(sql.toString(), params.toArray());
	}

	/**
	 * 获得非删除记录
	 * 
	 * @return 记录
	 * @history v1.2.1 修改：使用通用方法findFirstByColumn替代自写方法
	 */
	public M findFilteredById(Long id) {
		return findFirstByColumn(getTable().getPrimaryKey(), id, true);
	}

	/**
	 * 通过列查找第一个记录 v1.2.1 增加
	 * 
	 * @param column
	 *            列名
	 * @param value
	 *            列值
	 * @return 结果记录
	 */
	public M findFirstByColumn(String column, Object value) {
		return findFirstByColumn(column, value, false);
	}

	/**
	 * 通过列查找未删除的第一个记录 v1.2.1 增加
	 * 
	 * @param column
	 *            列名
	 * @param value
	 *            列值
	 * @return 结果记录
	 */
	public M findFirstFilteredByColumn(String column, Object value) {
		return findFirstByColumn(column, value, true);
	}

	/**
	 * 通过列查找第一个记录 v1.2.1 增加
	 * 
	 * @param column
	 *            列名
	 * @param value
	 *            列值
	 * @param isFiltered
	 *            是否过滤
	 * @return 结果记录
	 */
	public M findFirstByColumn(String column, Object value, Boolean isFiltered) {
		Map<String, Object> paras = new HashMap<String, Object>();
		paras.put(column, value);
		return findFirstByColumn(paras, isFiltered);
	}

	/**
	 * 通过列查找第一个记录 v1.2.1 增加
	 * 
	 * @param paras
	 *            参数
	 * @param isFiltered
	 *            是否过滤
	 * @return 结果记录
	 */
	public M findFirstByColumn(Map<String, Object> paras, Boolean isFiltered) {
		StringBuilder sql = new StringBuilder("select * from " + tableName + " where 1=1 ");

		List<Object> params = new ArrayList<Object>();
		Iterator<Map.Entry<String, Object>> iter = paras.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, Object> para = iter.next();
			sql.append("and " + para.getKey() + "=? ");
			params.add(para.getValue());
		}

		if (isFiltered) {
			sql.append("and " + IS_DEL_LABEL + "=0 ");
		}
		return findFirst(sql.toString(), params.toArray());
	}

	/**
	 * 查找所有的记录 v1.2.1 增加
	 * 
	 * @return 返回所有的查询记录
	 */
	public List<M> findAll() {
		return findAll("*", false);
	}

	/**
	 * 查找所有未删除的记录 v1.2.1 增加
	 * 
	 * @return 返回所有未删除记录
	 */
	public List<M> findFilteredAll() {
		return findAll("*", true);
	}

	/**
	 * 查找出特定的列记录 v1.2.1 增加
	 * 
	 * @param columnsName
	 *            列名（多列以逗号隔开）
	 * @return 返回特定列数据记录集合
	 */
	public List<M> findAll(String columnsName) {
		return findAll(columnsName, false);
	}

	/**
	 * 查找出未删除的特定的列记录 v1.2.1 增加
	 * 
	 * @param columnsName
	 *            列名（多列以逗号隔开）
	 * @return 返回未删除特定列数据记录集合
	 */
	public List<M> findFilteredAll(String columnsName) {
		return findAll(columnsName, true);
	}

	/**
	 * 查询所有的记录，查询列为columnsName
	 * 
	 * @param columnsName
	 *            需要查询的列
	 * @param isFiltered
	 *            是否过滤未删除
	 * @return 返回所有的查询记录
	 * @history v1.2.1 修改：增加是否过滤未删除的参数
	 */
	public List<M> findAll(String columnsName, Boolean isFiltered) {
		if (StrKit.isBlank(columnsName)) {
			throw new RuntimeException("can't put columnsName empty");
		}

		// v1.2.1增加自有方法过滤拥有是否删除标志的记录
		StringBuilder sql = new StringBuilder("select " + columnsName.trim() + " from " + tableName);
		if (isFiltered) {
			if (getTable().hasColumnLabel(IS_DEL_LABEL)) {
				sql.append(" where " + IS_DEL_LABEL + "=0 ");
			} else {
				throw new RuntimeException("there is no " + IS_DEL_LABEL + " field in table:" + tableName);
			}
		}

		return find(sql.toString());
	}

	/**
	 * 通过列查询记录 v1.2.1 增加
	 * 
	 * @param columnName
	 *            列名
	 * @param columnValue
	 *            列值
	 * @return 返回条件的记录
	 */
	public List<M> findByColumn(String columnName, Object columnValue) {
		return findByColumn("*", columnName, columnValue, false);
	}

	/**
	 * 通过列查询未删除记录 v1.2.1 增加
	 * 
	 * @param columnName
	 *            列名
	 * @param columnValue
	 *            列值
	 * @return 返回条件的记录
	 */
	public List<M> findFilteredByColumn(String columnName, Object columnValue) {
		return findByColumn("*", columnName, columnValue, true);
	}

	/**
	 * 通过列查询制定输出列的记录 v1.2.1 增加
	 * 
	 * @param showColumnsName
	 *            显示的列
	 * @param columnName
	 *            列名
	 * @param columnValue
	 *            列值
	 * @return 返回条件的记录
	 */
	public List<M> findByColumn(String showColumnsName, String columnName, Object columnValue) {
		return findByColumn(showColumnsName, columnName, columnValue, false);
	}

	/**
	 * 通过列查询制定输出列的未删除记录 v1.2.1 增加
	 * 
	 * @param showColumnsName
	 *            显示的列
	 * @param columnName
	 *            列名
	 * @param columnValue
	 *            列值
	 * @return 返回条件的记录
	 */
	public List<M> findFilteredByColumn(String showColumnsName, String columnName, Object columnValue) {
		return findByColumn(showColumnsName, columnName, columnValue, true);
	}

	/**
	 * 根据列查询需要查询的记录列表 v1.2.1 增加
	 * 
	 * @param paras
	 *            参数
	 * @return 对应的查询记录
	 */
	public List<M> findByColumn(Map<String, Object> paras) {
		return findByColumn("*", paras, false);
	}

	/**
	 * 根据列查询需要查询的记录列表(已过滤删除) v1.2.1 增加
	 * 
	 * @param paras
	 *            参数
	 * @return 对应的查询记录
	 */
	public List<M> findFilteredByColumn(Map<String, Object> paras) {
		return findByColumn("*", paras, true);
	}

	/**
	 * 根据列查询需要查询的记录列表 v1.2.1 增加
	 * 
	 * @param showColumnsName
	 *            需要展示的记录列表
	 * @param paras
	 *            参数
	 * @return 对应的查询记录
	 */
	public List<M> findByColumn(String showColumnsName, Map<String, Object> paras) {
		return findByColumn(showColumnsName, paras, false);
	}

	/**
	 * 根据列查询需要查询的记录列表 v1.2.1 增加
	 * 
	 * @param showColumnsName
	 *            需要展示的记录列表
	 * @param paras
	 *            参数
	 * @return 对应的查询记录
	 */
	public List<M> findFilteredByColumn(String showColumnsName, Map<String, Object> paras) {
		return findByColumn(showColumnsName, paras, true);
	}

	/**
	 * 根据列查询需要查询的记录列表
	 * 
	 * @param showColumnsName
	 *            需要展示的记录列表
	 * @param columnName
	 *            列名
	 * @param columnValue
	 *            列值
	 * @param isFiltered
	 *            是否过滤未删除
	 * @return 对应的查询记录
	 * @history v1.2.1 修改：增加是否过滤未删除的参数
	 */
	public List<M> findByColumn(String showColumnsName, String columnName, Object columnValue, Boolean isFiltered) {
		if (StrKit.isBlank(columnName)) {
			throw new RuntimeException("Can't access sql using empty columnName !");
		}
		if (!getTable().hasColumnLabel(columnName)) {
			throw new RuntimeException("There is no columnName named :" + columnName);
		}
		if (!getTable().getColumnType(columnName).equals(columnValue.getClass())) {
			throw new RuntimeException("The columnValue class should be " + getTable().getColumnType(columnName));
		}

		Map<String, Object> paras = new HashMap<String, Object>();
		paras.put(columnName, columnValue);
		return findByColumn(showColumnsName, paras, isFiltered);

	}

	/**
	 * 根据列查询需要查询的记录列表
	 * 
	 * @param showColumnsName
	 *            需要展示的记录列表
	 * @param paras
	 *            参数
	 * @param isFiltered
	 *            是否过滤未删除
	 * @return 对应的查询记录
	 */
	public List<M> findByColumn(String showColumnsName, Map<String, Object> paras, Boolean isFiltered) {
		if (paras == null) {
			throw new RuntimeException("Parameters can't be null!");
		}

		// v1.2.1增加自有方法过滤拥有是否删除标志的记录
		StringBuilder sql = new StringBuilder("select " + showColumnsName.trim() + " from " + tableName + " where 1=1 ");

		List<Object> params = new ArrayList<Object>();
		Iterator<Map.Entry<String, Object>> iter = paras.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, Object> para = iter.next();
			sql.append("and " + para.getKey() + "=? ");
			params.add(para.getValue());
		}

		if (isFiltered) {
			if (getTable().hasColumnLabel(IS_DEL_LABEL)) {
				sql.append("and " + IS_DEL_LABEL + "=0 ");
			} else {
				throw new RuntimeException("there is no " + IS_DEL_LABEL + " field in table:" + tableName);
			}
		}

		return find(sql.toString(), params.toArray());
	}

	/**
	 * IN查询所有列
	 * 
	 * @param key
	 *            查询的列
	 * @param values
	 *            查询的IN结果
	 * @return 查询结果
	 */
	public List<M> findByColumnIn(String key, String... values) {
		return findByColumnIn("*", false, key, values);
	}

	/**
	 * IN查询已过滤的所有列
	 * 
	 * @param isFiltered
	 *            是否过滤
	 * @param key
	 *            查询的列
	 * @param values
	 *            查询的IN结果
	 * @return 查询结果
	 */
	public List<M> findFilteredByColumnIn(String key, String... values) {
		return findByColumnIn("*", true, key, values);
	}

	/**
	 * IN查询所有列
	 * 
	 * @param isFiltered
	 *            是否过滤
	 * @param key
	 *            查询的列
	 * @param values
	 *            查询的IN结果
	 * @return 查询结果
	 */
	public List<M> findByColumnIn(Boolean isFiltered, String key, String... values) {
		return findByColumnIn("*", isFiltered, key, values);
	}

	/**
	 * IN查询指定列
	 * 
	 * @param showColumnsName
	 *            显示的列
	 * @param key
	 *            查询的列
	 * @param values
	 *            查询的IN结果
	 * @return 查询结果
	 */
	public List<M> findByColumnIn(String showColumnsName, String key, String... values) {
		return findByColumnIn(showColumnsName, false, key, values);
	}

	/**
	 * IN查询指定列
	 * 
	 * @param showColumnsName
	 *            显示的列
	 * @param key
	 *            查询的列
	 * @param values
	 *            查询的IN结果
	 * @return 查询结果
	 */
	public List<M> findFilteredByColumnIn(String showColumnsName, String key, String... values) {
		return findByColumnIn(showColumnsName, true, key, values);
	}

	/**
	 * IN查询指定列
	 * 
	 * @param showColumnsName
	 *            显示的列
	 * @param isFiltered
	 *            是否过滤
	 * @param key
	 *            查询的列
	 * @param values
	 *            查询的IN结果
	 * @return 查询结果
	 */
	public List<M> findByColumnIn(String showColumnsName, Boolean isFiltered, String key, String... values) {
		StringBuilder sql = new StringBuilder("select " + showColumnsName.trim() + " from " + tableName + " where ");

		sql.append(key + " in (" + CollectionKit.convert(values, ",", true) + ") ");

		return findByColumnIn(sql, isFiltered);
	}

	/**
	 * IN查询所有列
	 * 
	 * @param key
	 *            查询的列
	 * @param values
	 *            查询的IN结果
	 * @return 查询结果
	 */
	public List<M> findByColumnIn(String key, Long... values) {
		return findByColumnIn("*", false, key, values);
	}

	/**
	 * IN查询已过滤的所有列
	 * 
	 * @param isFiltered
	 *            是否过滤
	 * @param key
	 *            查询的列
	 * @param values
	 *            查询的IN结果
	 * @return 查询结果
	 */
	public List<M> findFilteredByColumnIn(String key, Long... values) {
		return findByColumnIn("*", true, key, values);
	}

	/**
	 * IN查询所有列
	 * 
	 * @param isFiltered
	 *            是否过滤
	 * @param key
	 *            查询的列
	 * @param values
	 *            查询的IN结果
	 * @return 查询结果
	 */
	public List<M> findByColumnIn(Boolean isFiltered, String key, Long... values) {
		return findByColumnIn("*", isFiltered, key, values);
	}

	/**
	 * IN查询指定列
	 * 
	 * @param showColumnsName
	 *            显示的列
	 * @param key
	 *            查询的列
	 * @param values
	 *            查询的IN结果
	 * @return 查询结果
	 */
	public List<M> findByColumnIn(String showColumnsName, String key, Long... values) {
		return findByColumnIn(showColumnsName, false, key, values);
	}

	/**
	 * IN查询指定列
	 * 
	 * @param showColumnsName
	 *            显示的列
	 * @param key
	 *            查询的列
	 * @param values
	 *            查询的IN结果
	 * @return 查询结果
	 */
	public List<M> findFilteredByColumnIn(String showColumnsName, String key, Long... values) {
		return findByColumnIn(showColumnsName, true, key, values);
	}

	/**
	 * IN查询指定列
	 * 
	 * @param showColumnsName
	 *            显示的列
	 * @param isFiltered
	 *            是否过滤
	 * @param key
	 *            查询的列
	 * @param values
	 *            查询的IN结果
	 * @return 查询结果
	 */
	public List<M> findByColumnIn(String showColumnsName, Boolean isFiltered, String key, Long... values) {
		StringBuilder sql = new StringBuilder("select " + showColumnsName.trim() + " from " + tableName + " where ");

		sql.append(key + " in (" + CollectionKit.convert(values, ",") + ") ");

		return findByColumnIn(sql, isFiltered);
	}

	/**
	 * IN查询
	 * 
	 * @param sql
	 *            SQL语句
	 * @param isFiltered
	 *            是否过滤
	 * @return 查询结果
	 */
	private List<M> findByColumnIn(StringBuilder sql, Boolean isFiltered) {
		if (isFiltered) {
			if (getTable().hasColumnLabel(IS_DEL_LABEL)) {
				sql.append("and " + IS_DEL_LABEL + "=0 ");
			} else {
				throw new RuntimeException("there is no " + IS_DEL_LABEL + " field in table:" + tableName);
			}
		}

		return find(sql.toString());
	}

	/*
	 * 覆盖父类save方法
	 */
	@Override
	public boolean save() {
		if (get(getTable().getPrimaryKey()) == null) {
			set(getTable().getPrimaryKey(), SpringContextHolder.getBean(IdGenerate.class).getNextValue());
		}
		return super.save();
	}

	/**
	 * 数据更新 v1.2.1 增加
	 * 
	 * @param columns
	 *            更新列
	 * @param column
	 *            列名
	 * @param value
	 *            列值
	 * @return 是否更新成功
	 */
	@Deprecated
	public int update(String columns, String column, Object value) {
		Map<String, Object> paras = new HashMap<String, Object>();
		paras.put(column, value);
		return update(columns, paras, false);
	}

	/**
	 * 数据更新（过滤已删除） v1.2.1 增加
	 * 
	 * @param columns
	 *            更新列
	 * @param column
	 *            列名
	 * @param value
	 *            列值
	 * @return 是否更新成功
	 */
	@Deprecated
	public int updateFiltered(String columns, String column, Object value) {
		Map<String, Object> paras = new HashMap<String, Object>();
		paras.put(column, value);
		return update(columns, paras, true);
	}

	/**
	 * 数据更新 v1.2.1 增加
	 * 
	 * @param columns
	 *            更新列
	 * @param paras
	 *            条件参数
	 * @return 是否更新成功
	 */
	@Deprecated
	public int update(String columns, Map<String, Object> paras) {
		return update(columns, paras, false);
	}

	/**
	 * 数据更新过滤已删除 v1.2.1 增加
	 * 
	 * @param columns
	 *            更新列
	 * @param paras
	 *            条件参数
	 * @return 是否更新成功
	 */
	@Deprecated
	public int updateFiltered(String columns, Map<String, Object> paras) {
		return update(columns, paras, true);
	}

	/**
	 * 数据更新 v1.2.1 增加
	 * 
	 * @param columns
	 *            更新列
	 * @param paras
	 *            条件参数
	 * @param isFiltered
	 *            是否过滤已删除
	 * @return 是否更新成功
	 */
	@Deprecated
	public int update(String columns, Map<String, Object> paras, Boolean isFiltered) {
		StringBuffer sql = new StringBuffer("update " + tableName + " set ");

		List<Object> params = new ArrayList<Object>();
		String[] columnArray = columns.split(",");
		for (int i = 0; i < columnArray.length; i++) {
			String column = columnArray[i];
			sql.append(column + "=? ");
			params.add(this.get(column));
		}

		Iterator<Map.Entry<String, Object>> iter = paras.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, Object> para = iter.next();
			sql.append("and " + para.getKey() + "=? ");
			params.add(para.getValue());
		}

		if (isFiltered) {
			sql.append("and " + IS_DEL_LABEL + "=0 ");
		}

		return Db.update(sql.toString(), params.toArray());
	}

	/**
	 * 批量保存
	 * 
	 * @param <M>
	 * @param modelList
	 *            保存的list数据
	 * @return 是否保存成功
	 */
	public boolean batchSave(List<M> modelList) {
		return batchSave(modelList, tableName, "*");
	}

	/**
	 * 批量保存
	 * 
	 * @param <T>
	 * @param modelOrRecordList
	 *            保存的list数据
	 * @param tableName
	 *            保存目标表
	 * @return 是否保存成功
	 */
	public <T> boolean batchSave(List<T> modelOrRecordList, String tableName) {
		return batchSave(modelOrRecordList, tableName, "*");
	}

	/**
	 * 批量保存
	 * 
	 * @param <T>
	 * @param modelOrRecordList
	 *            保存的list数据
	 * @param tableName
	 *            保存目标表
	 * @param columns
	 *            需要保存的列
	 * @return 是否保存成功
	 */
	public <T> boolean batchSave(List<T> modelOrRecordList, String tableName, String columns) {
		if (StrKit.isBlank(tableName)) {
			throw new RuntimeException("The tableName can't be empty");
		}
		if (StrKit.isBlank(columns)) {
			throw new RuntimeException("There can't be empty columns");
		}
		if (modelOrRecordList == null) {
			throw new RuntimeException("The modelOrRecordList is null");
		}

		columns = "*".equals(columns.trim()) ? StringUtils.join(getTable().getColumns(), ",") : columns;
		String[] columnArray = columns.split(",");

		StringBuilder sql = new StringBuilder("insert into " + tableName + " ");
		sql.append("(" + columns + ") values( ");
		for (int i = 0; i < columnArray.length; i++) {
			sql.append("?");
			if (i < columnArray.length - 1) {
				sql.append(",");
			}
		}
		sql.append(") ");

		TableBind table = this.getClass().getAnnotation(TableBind.class);
		int result[] = null;
		if (StringUtils.isEmpty(table.configName())) {
			result = Db.batch(sql.toString(), columns, modelOrRecordList);
		} else {
			result = Db.use(table.configName()).batch(sql.toString(), columns, modelOrRecordList);
		}
		if (ArrayUtils.contains(result, 0)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 批量更新
	 * 
	 * @param <T>
	 * @param modelOrRecordList
	 *            保存的list数据
	 * @param columns
	 *            需要保存的列
	 * @return 是否保存成功
	 */
	public <T> boolean batchUpdate(List<T> modelOrRecordList, String columns) {
		return batchUpdate(modelOrRecordList, tableName, columns);
	}
	
	/**
	 * 批量更新
	 * 
	 * @param <T>
	 * @param modelOrRecordList
	 *            保存的list数据
	 * @param tableName
	 *            保存目标表
	 * @param columns
	 *            需要保存的列
	 * @return 是否保存成功
	 */
	public <T> boolean batchUpdate(List<T> modelOrRecordList, String tableName, String columns) {
		if (StrKit.isBlank(tableName)) {
			throw new RuntimeException("The tableName can't be empty");
		}
		if (StrKit.isBlank(columns)) {
			throw new RuntimeException("There can't be empty columns");
		}
		if (modelOrRecordList == null) {
			throw new RuntimeException("The modelOrRecordList is null");
		}
		
		columns = "*".equals(columns.trim()) ? StringUtils.join(getTable().getColumns(), ",") : columns;
		String[] columnArray = columns.split(",");
		
		StringBuilder sql = new StringBuilder("update " + tableName + " set ");
		for (int i = 0; i < columnArray.length; i++) {
			sql.append(columnArray[i] + "=?");
			if (i < columnArray.length - 1) {
				sql.append(",");
			}
		}
		columns = columns + "," + getTable().getPrimaryKey();
		sql.append(" where " + getTable().getPrimaryKey() + "=? ");

		int result[] = Db.batch(sql.toString(), columns, modelOrRecordList);
		if (ArrayUtils.contains(result, 0)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 删除所有
	 * 
	 * @param ids
	 *            目标编号
	 * @param date
	 *            日期
	 * @return 是否删除成功
	 */
	@Deprecated
	public boolean deleteAll(String[] ids, Date date) {
		int result;
		if (getTable().hasColumnLabel(IS_DEL_LABEL)) {
			result = Db.update("update " + tableName + " set is_del=?, modify_date=? where id in (" + CollectionKit.convert(ids, ",", false) + ")", Boolean.TRUE, date);
		} else {
			result = Db.update("delete from " + tableName + " where " + getTable().getPrimaryKey() + " in (" + CollectionKit.convert(ids, ",", false) + ") ");
		}
		return result >= 0;
	}

	/**
	 * 删除所有
	 * 
	 * @param ids
	 *            目标编号
	 * @param date
	 *            日期
	 * @return 是否删除成功
	 */
	public boolean deleteAll(Long[] ids, Date date) {
		int result;
		if (getTable().hasColumnLabel(IS_DEL_LABEL)) {
			result = Db.update("update " + tableName + " set is_del=?, modify_date=? where id in (" + CollectionKit.convert(ids, ",") + ")", Boolean.TRUE, date);
		} else {
			result = Db.update("delete from " + tableName + " where " + getTable().getPrimaryKey() + " in (" + CollectionKit.convert(ids, ",") + ") ");
		}
		return result >= 0;
	}


	/**
	 * 根据主键删除所有
	 * 
	 * @param value
	 *            列值
	 * @param date
	 *            日期
	 * @return 是否删除成功
	 */
	public boolean deleteAllById(Object value, Date date) {
		return deleteAllByColumn(getTable().getPrimaryKey(), value, date);
	}

	/**
	 * 根据列删除所有
	 * 
	 * @param column
	 *            列
	 * @param value
	 *            列值
	 * @param date
	 *            日期
	 * @return 是否删除成功
	 */
	public boolean deleteAllByColumn(String column, Object value, Date date) {
		int result;

		if (getTable().hasColumnLabel(IS_DEL_LABEL)) {
			result = Db.update("update " + tableName + " set is_del=?, modify_date=? where " + column + " =? ", Boolean.TRUE, date, value);
		} else {
			result = Db.update("delete from " + tableName + " where " + column + "=? ", value);
		}
		return result >= 0;
	}

	/**
	 * 根据列删除所有
	 * 
	 * @param column
	 *            列
	 * @param values
	 *            列的值列表
	 * @param date
	 *            日期
	 * @return 是否删除成功
	 */
	public boolean deleteAllByColumn(String column, String[] values, Date date) {
		int result;

		if (getTable().hasColumnLabel(IS_DEL_LABEL)) {
			result = Db.update("update " + tableName + " set is_del=?, modify_date=? where " + column + " in (" + CollectionKit.convert(values, ",", true) + ")", Boolean.TRUE, date);
		} else {
			result = Db.update("delete from " + tableName + " where " + column + " in (" + CollectionKit.convert(values, ",", true) + ") ");
		}
		return result >= 0;
	}

	/**
	 * 根据列删除所有
	 * 
	 * @param column
	 *            列
	 * @param values
	 *            列的值列表
	 * @param date
	 *            日期
	 * @return 是否删除成功
	 */
	public boolean deleteAllByColumn(String column, Long[] values, Date date) {
		int result;

		if (getTable().hasColumnLabel(IS_DEL_LABEL)) {
			result = Db.update("update " + tableName + " set is_del=?, modify_date=? where " + column + " in (" + CollectionKit.convert(values, ",") + ")", Boolean.TRUE, date);
		} else {
			result = Db.update("delete from " + tableName + " where " + column + " in (" + CollectionKit.convert(values, ",") + ") ");
		}
		return result >= 0;
	}

}
