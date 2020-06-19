package com.gnet.service;

import com.gnet.model.admin.CcCourseGradecompose;
import com.gnet.model.admin.CcCourseGradecomposeIndication;
import com.gnet.model.admin.CcGradecomposeIndicationScoreRemark;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.response.ServiceResponse;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开课课程成绩组成元素指标点关联的分数范围备注表
 * 
 * @author xzl
 * 
 * @date
 */
@Component("ccGradecomposeIndicationScoreRemarkService")
public class CcGradecomposeIndicationScoreRemarkService {

	public ServiceResponse batchSave(List<HashMap> scoreSectionRemarks, Long gradecomposeIndicationId){
		Date date = new Date();
		List<CcGradecomposeIndicationScoreRemark> ccGradecomposeIndicationScoreRemarks = Lists.newArrayList();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		for(Map<String, String> map : scoreSectionRemarks){
			String scoreSection = map.get("scoreSection");
			String scoreRemark = map.get("scoreRemark");

			if(StrKit.isBlank(scoreSection)){
                return ServiceResponse.error("每一格分数区间不能为空");
			}
			if(StrKit.isBlank(scoreRemark)){
				return ServiceResponse.error("每一格分数说明不能为空");
			}

			CcGradecomposeIndicationScoreRemark ccGradecomposeIndicationScoreRemark = new CcGradecomposeIndicationScoreRemark();
			ccGradecomposeIndicationScoreRemark.set("id", idGenerate.getNextValue());
			ccGradecomposeIndicationScoreRemark.set("create_date", date);
			ccGradecomposeIndicationScoreRemark.set("modify_date", date);
			ccGradecomposeIndicationScoreRemark.set("gradecompose_indication_id", gradecomposeIndicationId);
			ccGradecomposeIndicationScoreRemark.set("score_section", scoreSection);
			ccGradecomposeIndicationScoreRemark.set("score_remark", scoreRemark);
			ccGradecomposeIndicationScoreRemarks.add(ccGradecomposeIndicationScoreRemark);
		}

		if(!CcGradecomposeIndicationScoreRemark.dao.batchSave(ccGradecomposeIndicationScoreRemarks)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return ServiceResponse.error("保存分数区间和分数说明失败");
		}

		return ServiceResponse.succ(true);
	}

}
