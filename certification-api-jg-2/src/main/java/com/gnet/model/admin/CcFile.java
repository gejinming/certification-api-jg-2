package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;
import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 文件Model
 *
 * @author yuhailun
 * @date 2018/01/29
 * @description
 **/
@TableBind(tableName = "cc_file")
public class CcFile extends DbModel<CcFile> {

    private static final long serialVersionUID = -3958125598237390759L;
    public static final CcFile dao = new CcFile();

    /**
     * 文件管理-文件列表分页
     *
     * @param majorId  专业编号
     * @param fileName 文件名称
     * @param fileType 文件类型
     * @return 文件列表
     */
    public List<CcFile> find(Pageable pageable, Long majorId, Long parentId, String fileName, String fileType, Boolean isDel) {
        StringBuilder sql = new StringBuilder("select cf.id, cf.modify_date, su2.name modifyUserName, cf.name, cf.type, cf.size, cf.is_file, cf.is_first from " + tableName + " cf ");
        sql.append("left join " + User.dao.tableName + " su on su.id = cf.create_user ");
        sql.append("left join " + User.dao.tableName + " su2 on su2.id = cf.modify_user ");

        List<Object> params = Lists.newArrayList();
        sql.append("where cf.major_id = ? ");
        params.add(majorId);

        if (parentId != null) {
            sql.append("and cf.parent_id = ? ");
            params.add(parentId);
        } else {
            sql.append("and cf.is_first = ? ");
            params.add(true);
        }

        if (StringUtils.isNotBlank(fileName)) {
            sql.append("and cf.name like '%" + StringEscapeUtils.escapeSql(fileName) + "%'");
        }

        if (StringUtils.isNotBlank(fileType)) {
            sql.append("and cf.type like '%" + StringEscapeUtils.escapeSql(fileType) + "%'");
        }

        if (isDel != null) {
            sql.append("and cf.is_del = ? ");
            params.add(isDel);
        }

        sql.append("order by cf.is_file asc ");

        if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
            sql.append(", " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
        }

        sql.append(", cf.modify_date desc");

        return find(sql.toString(), params.toArray());
    }
    /**
     * 文件管理-文件列表分页
     *
     * @param majorId  专业编号
     * @param fileName 文件名称
     * @param fileType 文件类型
     * @return 文件列表
     */
    public Page<CcFile> page(Pageable pageable, Long majorId, Long parentId, String fileName, String fileType, Boolean isDel) {
        StringBuilder sql = new StringBuilder(" from " + tableName + " cf ");
        sql.append("left join " + User.dao.tableName + " su on su.id = cf.create_user ");
        sql.append("left join " + User.dao.tableName + " su2 on su2.id = cf.modify_user ");

        List<Object> params = Lists.newArrayList();
        sql.append("where cf.major_id = ? ");
        params.add(majorId);

        if (parentId != null) {
            sql.append("and cf.parent_id = ? ");
            params.add(parentId);
        } else {
            sql.append("and cf.is_first = ? ");
            params.add(true);
        }

        if (StringUtils.isNotBlank(fileName)) {
            sql.append("and cf.name like '%" + StringEscapeUtils.escapeSql(fileName) + "%'");
        }

        if (StringUtils.isNotBlank(fileType)) {
            sql.append("and cf.type like '%" + StringEscapeUtils.escapeSql(fileType) + "%'");
        }

        if (isDel != null) {
            sql.append("and cf.is_del = ? ");
            params.add(isDel);
        }

        sql.append("order by cf.is_file asc ");

        if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
            sql.append(", " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
        }

        sql.append(", cf.modify_date desc");

        return CcFile.dao.paginate(pageable,"select cf.id, cf.modify_date, su2.name modifyUserName, cf.name, cf.type, cf.size, cf.is_file, cf.is_first",sql.toString(), params.toArray());
    }
 /*   *//**
     * 更新文件夹大小
     *
     * @param dirList 文件夹集合
     * @param size    大小
     * @param isAdd   是否增加
     * @return
     *//*
    public boolean updateSize(List<String> dirList, Long size, Long parentId, boolean isAdd) {
        StringBuilder sql = new StringBuilder("update " + tableName + " set size=size");
        if (isAdd) {
            sql.append("+");
        } else {
            sql.append("-");
        }
        sql.append(size.toString());
        sql.append(" where is_del = 0 and path in (" + CollectionKit.convert(dirList.toArray(new String [dirList.size()]), ",", true) + ") ");
        return Db.update(sql.toString()) == dirList.size();
    }*/
    /**
     * 更新文件夹大小
     *
     * @param dirList 文件夹集合
     * @param size    大小
     * @param isAdd   是否增加
     * @return
     */
    public boolean updateSize2(List<String> dirList, Long size, Long parentId, boolean isAdd) {
        StringBuilder sql = new StringBuilder("update " + tableName + " set size=size");
        if (isAdd) {
            sql.append("+");
        } else {
            sql.append("-");
        }
        sql.append(size.toString());//and path in (" + CollectionKit.convert(dirList.toArray(new String [dirList.size()]), ",", true) + ") ");
        sql.append(" where is_del = 0 and id="+parentId);
        return Db.update(sql.toString()) == dirList.size();
    }
    /*
     * @param size
    	 * @param parentId
     * @return int
     * @author Gejm
     * @description: 删除文件减大小
     * @date 2020/6/2 14:31
     */
    public int updateSize3(Long size, Long parentId){
        StringBuilder sql = new StringBuilder("update " + tableName + " set size=size-"+size);
        sql.append(" where is_del = 0 and id="+parentId);
        return Db.update(sql.toString());
    }

}