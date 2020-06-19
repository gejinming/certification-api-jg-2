package com.gnet.service;

import com.jfinal.log.Logger;
import org.springframework.stereotype.Component;

/**
 * @program: certification-api-jg-2
 * @description: 课程达成度生成报表
 * @author: Gjm
 * @create: 2020-06-12 14:25
 **/
@Component("CcourseAchievementService")
public class CcourseAchievementService {
    private static final Logger logger = Logger.getLogger(CcourseAchievementService.class);
    /*
     * @param null
     * @return
     * @author Gejm
     * @description:某个版本某个年级下这个课程的课程达成度（包括了多个班级的数据）
     * 平均分数：	多个教学班所有学生分数相加/多个教学班学生总数
     * @date 2020/6/12 14:27
     */

}
