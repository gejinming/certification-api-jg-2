package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;

import java.util.List;

/**
 * @program: certification-api-jg-2
 * @description: 持续报告课程目标成绩组成的数据
 * @author: Gjm
 * @create: 2020-11-20 15:44
 **/
@TableBind(tableName ="cc_educlass_indication_result")
public class CcEduclassIndicationResult extends DbModel<CcEduclassIndicationResult> {

    private static final long serialVersionUID = -7682946497357968282L;
    public static  final CcEduclassIndicationResult dao = new CcEduclassIndicationResult();

    public CcEduclassIndicationResult findIndicationResult(Long classId,Long indicationId,Long gradecomposeId){
        StringBuilder sql = new StringBuilder("select * from cc_educlass_indication_result ");
        sql.append("where class_id=? and indication_id=? and gradecompose_id =? order by indication_id, gradecompose_id");
        return findFirst(sql.toString(),classId,indicationId,gradecomposeId);

    }

}
