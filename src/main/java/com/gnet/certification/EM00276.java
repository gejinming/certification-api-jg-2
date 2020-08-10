package com.gnet.certification;

import java.math.BigDecimal;
import java.util.*;

import com.gnet.model.admin.*;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.plugin.push.kit.DecimalKit;
import com.gnet.utils.DictUtils;
import org.apache.bcel.generic.IF_ACMPEQ;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.service.CcIndicationCourseService;
import com.gnet.utils.PriceUtils;
import com.gnet.utils.SpringContextHolder;

/**
 * 增加或编辑课程指标点关系
 * 
 * @author xzl
 * 
 * @date 2016年10月28日10:25:50
 *
 */
@Service("EM00276")
@Transactional(readOnly=false)
public class EM00276 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Map<String, Object> result = new HashMap<>();
		
		Long indicationId = paramsLongFilter(param.get("indicationId"));
		Long courseId = paramsLongFilter(param.get("courseId"));
		BigDecimal weight = paramsBigDecimalFilter(param.get("weight"));
		String eduAim = paramsStringFilter(param.get("eduAim"));
		String means = paramsStringFilter(param.get("means"));
		String source = paramsStringFilter(param.get("source"));
		String way = paramsStringFilter(param.get("way"));
		Long courseGroupIds = paramsLongFilter(param.get("courseGroupId"));
		BigDecimal allWeights = paramsBigDecimalFilter(param.get("allWeight"));
		//TODO 2020/06/07 由于急得演示这里判断权重是否大于1比较多，有重复的逻辑比较复杂，如果有时间可以优化下
		if (allWeights==null){
			allWeights=new BigDecimal("0");
		}
		//获取课程分组id
		Long courseGroupMangeId = paramsLongFilter(param.get("courseGroupMangeId"));
		//BigDecimal sumWights=new BigDecimal("1");
		//分级教学Id
		Long courseTeachGroupId = paramsLongFilter(param.get("courseTeachGroupId"));
		if (indicationId == null) {
			return renderFAIL("0362", response, header);
		}
		if (courseId == null) {
			return renderFAIL("0363", response, header);
		}
		if (weight == null) {
			return renderFAIL("0364", response, header);
		}
		BigDecimal maxweight=new BigDecimal("0") ;
		Integer updateState=0;
		BigDecimal  isWeight=null;
		BigDecimal oldweights=null;
		//查询现在录入的课程是否已经存在了权重
		CcIndicationCourse oldCourseWight = CcIndicationCourse.dao.findByCourseIdAndIndicationId(courseId, indicationId);
		if (oldCourseWight !=null){
			 oldweights = oldCourseWight.getBigDecimal("weight");
			//判断修改的数据有没有原来的大
			if (DecimalKit.greaterEqThan(weight,oldweights)){
				allWeights = allWeights.subtract(oldweights);
				allWeights=allWeights.add(weight);
				updateState=1;
			}
			/*if (DecimalKit.greaterEqThan(weight,oldweights) && courseTeachGroupId==null){
				allWeights = allWeights.subtract(oldweights);
				allWeights=allWeights.add(weight);
			}*/
			if (DecimalKit.greaterThan(allWeights,CcIndicationCourse.MAX_WEIGHT)){
				return renderFAIL("0760", response, header);
			}
		}else {
			if  (courseTeachGroupId ==null && courseGroupIds==null){
				allWeights= allWeights.add(weight);
				if (DecimalKit.greaterThan(allWeights,CcIndicationCourse.MAX_WEIGHT)){
					return renderFAIL("0760", response, header);
				}
			}
			updateState=1;
		}



		if (courseTeachGroupId !=null){
			//因为课程分组之间有课程重复
			HashMap<Object, Object> groupCourseMap = new HashMap<>();
			//获取分级教学中的课程id
            List<CcCourseGroupTeachMange> groupMangeCourseIds = CcCourseGroupTeachMange.dao.getTeachGroupMangeIds(courseTeachGroupId,courseId);
			for (CcCourseGroupTeachMange temp:groupMangeCourseIds){
				Long groupId = temp.getLong("group_id");
				groupCourseMap.put(groupId,courseId);
			}
            //判断分级教学里的课程是否全部录入权重
			List<CcIndicationCourse> ccIndicationCourses = CcIndicationCourse.dao.teachGroupState(courseTeachGroupId, indicationId);
			boolean state=true;
			//如果就存在一个未录入的课程，那么再判断未录入的课程是不是现在录入的。如果是则完全录入
			if (ccIndicationCourses.size()==1){
				for (CcIndicationCourse temp: ccIndicationCourses){
					Long courseId0 = temp.getLong("course_id");
					Long groupId0 = temp.getLong("mange_group_id");
					//因为课程分组里可以存在courseID相同的，改变分组里其中一个课程另一个也会改变，所以不用判断&& groupId0==courseGroupMangeId ，
					if (courseId.equals(courseId0) ){
						state=false;

					}
				}
			}
			//全部录入了
			if (ccIndicationCourses.size()==0){
                state=false;
            }
			//如果存在没有录入的，则不判断分级教学里的课程组是否权重相等
			List<CcIndicationCourse> teachGroupWeightList = CcIndicationCourse.dao.getGroupCourseIndication(indicationId, courseTeachGroupId,0);

			if (state){
				if (teachGroupWeightList.size()==0){
						isWeight=allWeights.add(weight);
						if (DecimalKit.greaterThan(isWeight,CcIndicationCourse.MAX_WEIGHT)){
							return renderFAIL("0760", response, header);
						}
				}
				//找出分级教学里课程组的最大的总权重
				for (int i=0;i<teachGroupWeightList.size();i++){
					CcIndicationCourse temp = teachGroupWeightList.get(i);
					Long groupId = temp.getLong("mange_group_id");
					BigDecimal weight0 = temp.getBigDecimal("weight");
					//不存在课程权重
					if (oldCourseWight==null){
						//如果本次录入的课程分组id=这次循环的那么相加权重比较大小
						if (courseGroupMangeId.equals(groupId)){
							weight0.add(weight);
						}

					}else {
						//存在课程权重，先在这个课程组里减去这个权重，再加上本次录入的权重
						BigDecimal weight1 = oldCourseWight.getBigDecimal("weight");

						if (groupCourseMap.get(groupId) !=null){

							//先减去原来的权重再相加现在录入的权重
							 weight0=weight0.subtract(weight1);
							weight0 = weight0.add(weight);

							System.out.println(weight0);


						}


					}


					if (i==0){
						maxweight=weight0;
					}else {
						//比较取最大值 maxweight.max (wight0)
						maxweight=maxweight.max(weight0);


					}
				}
				if (oldCourseWight==null){
					BigDecimal a = maxweight.subtract(weight);
					BigDecimal b = allWeights.subtract(a);
					BigDecimal c = b.add(maxweight);
					if (DecimalKit.greaterThan(c,CcIndicationCourse.MAX_WEIGHT)){
						return renderFAIL("0760", response, header);
					}
				}

			}else {
				//如果教学分级全部录入了，那么需要比较教学分级里各个课程组的权重是否相等
				//CcCourseGroupTeachMange.dao.getTeachGroupMangeIds();
				for (int i=0;i<teachGroupWeightList.size();i++){
					CcIndicationCourse temp = teachGroupWeightList.get(i);
					Long groupId = temp.getLong("mange_group_id");
					BigDecimal weight0 = temp.getBigDecimal("weight");


					if (oldCourseWight==null){
						//如果本次录入的课程分组id=这次循环的那么相加权重比较大小
						if (courseGroupMangeId.equals(groupId) || groupCourseMap.get(groupId) !=null){
							weight0=weight0.add(weight);
						}
					}else {
						//如果本次录入的课程已经存在课程权重那就是修改权重，需要先在这个课程组里减去这个权重，再加上本次录入的权重
						//课程分组之间的课程有可能会重复，所以如果这次修改的是重复的课程，那么就要把有这个课程的课程分组都要进行处理再比较
						if (groupCourseMap.get(groupId) !=null){
							BigDecimal weight1 = oldCourseWight.getBigDecimal("weight");
							System.out.println(weight1);
							weight0=weight0.subtract(weight1);
							weight0=weight0.add(weight);

						}
					}

					if (i==0){
						maxweight=weight0;
					}
					//比较各个课程分组里的权重是否相等maxweight!=weight0
					else if (maxweight.compareTo(weight0)!=0){
						return renderFAIL("2554", response, header);
					}

				}
			}
		}

		//单个权重不能超过1
		//如果分级教学id不为null,那么按照分级教学里最大课程分组里的权重判断
		if (DecimalKit.greaterThan(allWeights,CcIndicationCourse.MAX_WEIGHT) && updateState==1){
			return renderFAIL("0760", response, header);
		}
		if (courseTeachGroupId !=null){
			if (PriceUtils.greaterThan(maxweight, CcIndicationCourse.MAX_WEIGHT)){
				return renderFAIL("0760", response, header);
			}
		}

		else {
			if (PriceUtils.greaterThan(weight, CcIndicationCourse.MAX_WEIGHT)){
				return renderFAIL("0760", response, header);
			}
		}
		
		CcCourse course = CcCourse.dao.findFilteredById(courseId);
		if(course == null){
			return renderFAIL("0251", response, header);
		}

		BigDecimal twos[] = {CcEvaluteLevel.LEVEL_TOW_A_VALUE, CcEvaluteLevel.LEVEL_TOW_B_VALUE};
		BigDecimal fives[] = {CcEvaluteLevel.LEVEL_FIVE_A_VALUE, CcEvaluteLevel.LEVEL_FIVE_B_VALUE, CcEvaluteLevel.LEVEL_FIVE_C_VALUE, CcEvaluteLevel.LEVEL_FIVE_D_VALUE, CcEvaluteLevel.LEVEL_FIVE_E_VALUE};

		Date date = new Date();
		List<CcEvaluteLevel> ccEvaluteLevels = CcEvaluteLevel.dao.findByCourseId(courseId);
		List<CcEvaluteLevel> evaluteLevels = new ArrayList<>();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		if(!ccEvaluteLevels.isEmpty()){
			for(CcEvaluteLevel evaluteLevel : ccEvaluteLevels){
				Integer level = evaluteLevel.getInt("level");
				Long teacherCourseId = evaluteLevel.getLong("teacher_course_id");
				if(CcEvaluteLevel.LEVEL_TOW.equals(level)){
					for(int i=1; i<= CcEvaluteLevel.LEVEL_TOW; i++){
						CcEvaluteLevel ccEvaluteLevel = new CcEvaluteLevel();
						ccEvaluteLevel.set("id", idGenerate.getNextValue());
						ccEvaluteLevel.set("create_date", date);
						ccEvaluteLevel.set("modify_date", date);
						ccEvaluteLevel.set("level_name", DictUtils.findLabelByTypeAndKey("evaluteLevelTow", i));
						ccEvaluteLevel.set("score", twos[i-1]);
						ccEvaluteLevel.set("teacher_course_id", teacherCourseId);
						ccEvaluteLevel.set("indication_id", indicationId);
						ccEvaluteLevel.set("level", CcEvaluteLevel.LEVEL_TOW);
						ccEvaluteLevel.set("is_del", Boolean.FALSE);
						evaluteLevels.add(ccEvaluteLevel);
					}
				}else if(CcEvaluteLevel.LEVEL_FIVE.equals(level)){
					for(int i=1; i<= CcEvaluteLevel.LEVEL_FIVE; i++ ){
						CcEvaluteLevel ccEvaluteLevel = new CcEvaluteLevel();
						ccEvaluteLevel.set("id", idGenerate.getNextValue());
						ccEvaluteLevel.set("create_date", date);
						ccEvaluteLevel.set("modify_date", date);
						ccEvaluteLevel.set("level_name", DictUtils.findLabelByTypeAndKey("evaluteLevelFive", i));
						ccEvaluteLevel.set("score", fives[i-1]);
						ccEvaluteLevel.set("teacher_course_id", teacherCourseId);
						ccEvaluteLevel.set("indication_id", indicationId);
						ccEvaluteLevel.set("is_del", Boolean.FALSE);
						ccEvaluteLevel.set("level", CcEvaluteLevel.LEVEL_FIVE);
						evaluteLevels.add(ccEvaluteLevel);
					}
				}
			}
			if(!CcEvaluteLevel.dao.batchSave(evaluteLevels)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				result.put("isSuccess", true);
			}
		}

		Long courseGroupId = course.getLong("course_group_id");
		// 专业方向编号
		Long directionId = course.getLong("direction_id");
		CcIndicationCourseService ccIndicationCourseService = SpringContextHolder.getBean(CcIndicationCourseService.class);
		CcIndicationCourse ccIndicationCourse = CcIndicationCourse.dao.findByCourseIdAndIndicationId(courseId, indicationId);
		//此次录入的课程是否已录入，不为null则为修改
		if(ccIndicationCourse != null){
			//修改后权重差值加上原本的指点的权重和
			CcMajorDirection ccMajorDirection = null;
			if (directionId != null) {
				ccMajorDirection = CcMajorDirection.dao.findById(directionId);
			}
			
			Map<String, BigDecimal> weightDirectionMap = ccIndicationCourseService.getIndicationWeightDirection(indicationId, courseGroupId, courseId, directionId,courseGroupMangeId,maxweight);
			
			BigDecimal updateWeight = null;
			if (ccMajorDirection != null && weightDirectionMap.get(ccMajorDirection.getStr("name")) != null) {
                //因为分级教学的权重已经在 weightDirectionMap.get("无方向")里了，不需要再加weight了，每个课程分组的权重都是相同的
			    if (courseTeachGroupId==null) {
                    updateWeight = PriceUtils.add(weight, weightDirectionMap.get(ccMajorDirection.getStr("name")));
                }else{
					if (DecimalKit.greaterThan(allWeights,CcIndicationCourse.MAX_WEIGHT) && updateState==1){
						return renderFAIL("0760", response, header);
					}else {
                    	updateWeight=weightDirectionMap.get(ccMajorDirection.getStr("name"));
					}
                }
			} else if (ccMajorDirection == null && !weightDirectionMap.isEmpty() && weightDirectionMap.get("无方向") == null) {
				for (Map.Entry<String, BigDecimal> entry : weightDirectionMap.entrySet()) {
					updateWeight = (updateWeight == null || PriceUtils.greaterThan(entry.getValue(), updateWeight)) ? entry.getValue() : updateWeight;
				}
				
				updateWeight = PriceUtils.add(updateWeight, weight);
			} else if (weightDirectionMap.get("无方向") != null) {
			    //因为分级教学的权重已经在 weightDirectionMap.get("无方向")里了，不需要再加weight了，每个课程分组的权重都是相同的
			    if (courseTeachGroupId==null){
						updateWeight = PriceUtils.add(weight, weightDirectionMap.get("无方向"));

                }else {
					if (DecimalKit.greaterThan(allWeights,CcIndicationCourse.MAX_WEIGHT) && updateState==1){
						return renderFAIL("0760", response, header);
					}else {
						updateWeight=weightDirectionMap.get("无方向");
					}

                }


			}
			//如果这个课程为分级教学里的那么判断时就判断课程分组总和最大的权重
			else if(courseTeachGroupId!=null){
				if (DecimalKit.greaterThan(allWeights,CcIndicationCourse.MAX_WEIGHT) && updateState==1){
					return renderFAIL("0760", response, header);
				}else{

					updateWeight=maxweight;
				}
			} else {
				updateWeight = weight;
			}
			
			if(PriceUtils.greaterThan(updateWeight, CcIndicationCourse.MAX_WEIGHT)){
				return renderFAIL("0762", response, header);
			}
			//2020/06/20 GJM 在此加注释，如果课程属于多选一课程那么这个多选一组的课程全部更新
			if(courseGroupId != null){
				if(!ccIndicationCourseService.batchUpdate(courseGroupId, indicationId, weight)){
					result.put("isSuccess", false);
					return renderSUC(result, response, header);
				}
			}
			
			ccIndicationCourse.set("modify_date", date);
			ccIndicationCourse.set("indication_id", indicationId);
			ccIndicationCourse.set("course_id", courseId);
			ccIndicationCourse.set("weight", weight);
			ccIndicationCourse.set("edu_aim", eduAim);
			ccIndicationCourse.set("means", means);
			ccIndicationCourse.set("source", source);
			ccIndicationCourse.set("way", way);
			
			if(!ccIndicationCourse.update()){
				if(courseGroupId != null){
				    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}	
			result.put("isSuccess", true);
		}else{
			
			// 获取各个方向的权重值
            Map<String, BigDecimal> weightDirectionMap = ccIndicationCourseService.getIndicationWeightDirection(indicationId, courseGroupId, null, directionId,courseGroupMangeId,maxweight);
			
			CcMajorDirection ccMajorDirection = null;
			if (directionId != null) {
				ccMajorDirection = CcMajorDirection.dao.findById(directionId);
			}
			
			BigDecimal allWeight = null;
			if (ccMajorDirection != null && weightDirectionMap.get(ccMajorDirection.getStr("name")) != null) {
                if (courseTeachGroupId==null) {
                    allWeight = PriceUtils.add(weight, weightDirectionMap.get(ccMajorDirection.getStr("name")));
                }else{
					if (DecimalKit.greaterThan(allWeights,CcIndicationCourse.MAX_WEIGHT) && updateState==1){
						return renderFAIL("0760", response, header);
					}else {

						allWeight=weightDirectionMap.get(ccMajorDirection.getStr("name"));
					}
                }

				
			} else if (ccMajorDirection == null && !weightDirectionMap.isEmpty() && weightDirectionMap.get("无方向") == null) {
				// 维护一门无专业方向的课程且该指标点下存在方向课
				for (Map.Entry<String, BigDecimal> entry : weightDirectionMap.entrySet()) {
					allWeight = (allWeight == null || PriceUtils.greaterThan(entry.getValue(), allWeight)) ? entry.getValue() : allWeight;
					if(PriceUtils.greaterThan(PriceUtils.add(allWeight, weight), CcIndicationCourse.MAX_WEIGHT)){
						return renderFAIL("0760", response, header, "课程指标点在" + entry.getKey() + "下的权重已为" + entry.getValue() + "加上" + weight + "就超过1了");
					}
				}
				
				allWeight = PriceUtils.add(allWeight, weight);
			} else if (weightDirectionMap.get("无方向") != null) {
			    if (courseTeachGroupId==null){
                    allWeight = PriceUtils.add(weight, weightDirectionMap.get("无方向"));
                }else{
					if (DecimalKit.greaterThan(allWeights,CcIndicationCourse.MAX_WEIGHT) && updateState==1){
						return renderFAIL("0760", response, header);
					}else {

						allWeight=weightDirectionMap.get("无方向");
					}
                }

				
			} else {
				allWeight = weight;
			}
			
			//判断增加后的权重和是否大于1
			/*if(PriceUtils.greaterThan(allWeight, CcIndicationCourse.MAX_WEIGHT)){
				return renderFAIL("0760", response, header);
			}*/
			
			if(courseGroupId != null){
				if(!ccIndicationCourseService.batchUpdate(courseGroupId, indicationId, weight)){
					result.put("isSuccess", false);
					return renderSUC(result, response, header);
				}
			}
			
			ccIndicationCourse = new CcIndicationCourse();
			ccIndicationCourse.set("create_date", date);
			ccIndicationCourse.set("modify_date", date);
			ccIndicationCourse.set("indication_id", indicationId);
			ccIndicationCourse.set("course_id", courseId);
			ccIndicationCourse.set("weight", weight);
			ccIndicationCourse.set("edu_aim", eduAim);
			ccIndicationCourse.set("means", means);
			ccIndicationCourse.set("source", source);
			ccIndicationCourse.set("way", way);
			ccIndicationCourse.set("is_del", Boolean.FALSE);
			if(!ccIndicationCourse.save()){
				if(courseGroupId != null){
				    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}	
			result.put("isSuccess", true);
			result.put("id", ccIndicationCourse.getLong("id"));
		}

		return renderSUC(result, response, header);
	}
}
