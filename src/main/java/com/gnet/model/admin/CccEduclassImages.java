package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;

import java.util.List;

/**
 * @program: certification-api-jg-2
 * @description: 教学班的一些转为B64的图片
 * @author: Gjm
 * @create: 2020-11-24 14:34
 **/
@TableBind(tableName = "cc_educlass_images")
public class CccEduclassImages extends DbModel<CccEduclassImages> {

    private static final long serialVersionUID = -7980669046238687342L;
    public static  final CccEduclassImages dao=new CccEduclassImages();


    public List<CccEduclassImages> findEduclassImages(Long classId) {
        StringBuilder sql = new StringBuilder("select * from cc_educlass_images where class_id = ? ");
        return find(sql.toString(),classId);
    }
}
