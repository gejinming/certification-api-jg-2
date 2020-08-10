package com.gnet.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.model.admin.CcIndicationCourse;
import com.gnet.model.admin.CcMajorDirection;
import com.gnet.utils.PriceUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;

/**
 * 指标点课程关系
 * 
 * @author xzl
 * 
 * @date 2016年10月24日
 */
@Component("ccIndicationCourseService")
public class CcIndicationCourseService {
	
	
	
	/**
	 * 返回某个指标点的权重和
	 * @param indicationId
	 * @param courseGroupId 
	 * @return
	 */
	public BigDecimal getIndicationWeight(Long indicationId, Long courseGroupId, Long courseId, Long directionId){
		BigDecimal exsitWeight = BigDecimal.valueOf(0);
		//权重可能为空，因为没有课程支持该指标点
		List<CcIndicationCourse> indicationCourses = CcIndicationCourse.dao.getIndicationWeight(indicationId, courseGroupId, courseId, directionId);
		Map<Long, BigDecimal> map = Maps.newHashMap();
		if(!indicationCourses.isEmpty()){
			for(CcIndicationCourse temp : indicationCourses){	
				Long courseGroup = temp.getLong("course_group_id");
				BigDecimal weight = temp.getBigDecimal("weight");
				if(courseGroup != null){
					if(map.get(courseGroup) == null){
						map.put(courseGroup, weight);
						exsitWeight = PriceUtils.add(exsitWeight, weight);
					}   
				}else{
					exsitWeight = PriceUtils.add(exsitWeight, weight);
				}
			}		
		}		
		return exsitWeight;
	}
	
	/**
	 * 返回某个指标点的各个方向上的权重和(方向上加无方向)
	 * @param indicationId
	 * @param courseGroupId 
	 * @return
	 */
	public Map<String, BigDecimal> getIndicationWeightDirection(Long indicationId, Long courseGroupId, Long courseId, Long directionId,Long mangeGroupId,BigDecimal maxweight){
		Map<String, BigDecimal> existWeightMap = Maps.newHashMap();
		
		List<CcIndicationCourse> indicationCourses = CcIndicationCourse.dao.getIndicationWeight(indicationId, courseGroupId, courseId, directionId);
		List<String> courseGroupIdList = Lists.newArrayList();
		Long prvoTeachGroup=null;
		//权重可能为空，因为没有课程支持该指标点
		if (indicationCourses.isEmpty()) {
			return existWeightMap;
		}
		
		BigDecimal noDirectionWeight = new BigDecimal(0);
		for (CcIndicationCourse ccIndicationCourse : indicationCourses) {
			Long courseGroupIdFlag = ccIndicationCourse.getLong("course_group_id");
			String directionName = ccIndicationCourse.getStr("direction_name");
			BigDecimal weight = ccIndicationCourse.getBigDecimal("weight");
			//课程分组id
            Long mangeGroupIds = ccIndicationCourse.getLong("mange_group_id");
            if (mangeGroupIds!=null){
            	System.out.println("sdd");
			}
            //分级教学id
            Long teachGroupIds = ccIndicationCourse.getLong("teach_group_id");

            // 初始化方向权重数值
			if (StrKit.notBlank(directionName) && existWeightMap.get(directionName) == null) {
				existWeightMap.put(directionName, new BigDecimal(0));
			}

			// 课程组处理，默认只加第一个
			if (courseGroupIdFlag != null) {
				String courseGroupIdStr = courseGroupIdFlag.toString();
				// 无方向上多选一组是否加过，若加过一次则有方向无方向都不用再增加(查询结果已排序过，无方向的课程放在前面)
				if (!courseGroupIdList.contains(courseGroupIdStr)) {
					if (StrKit.isBlank(directionName)) {
						noDirectionWeight = PriceUtils.add(noDirectionWeight, weight);
						courseGroupIdList.add(courseGroupIdStr);
					} else {
						// 无方向未增加过，进行有方向判断，若加过就不用加
						String courseGroupIdDireFlag = directionName + courseGroupIdStr;
						if (!courseGroupIdList.contains(courseGroupIdDireFlag)) {
							existWeightMap.put(directionName, PriceUtils.add(existWeightMap.get(directionName), weight));
							courseGroupIdList.add(courseGroupIdDireFlag);
						}
						
					}
					
				}
				
			} //teachGroupIds不为null则属于分级教学里的课程分组,
            else if (teachGroupIds!=null){
            	//mangeGroupId不为null说明是在修改分级教学里的课程
            	if (mangeGroupId==null ) {
					List<CcIndicationCourse> groupCourseIndication = ccIndicationCourse.dao.getGroupCourseIndication(indicationId, teachGroupIds, 1);
					for (CcIndicationCourse temp:groupCourseIndication){
						BigDecimal weight1 = temp.getBigDecimal("weight");
						maxweight=weight1;
					}
				}

					String teachGroupIdsStr = teachGroupIds.toString();
					//maxweight就是分级教学里的其中一个课程组的权重，每个课程组的权重是相等的
					// 无方向上课程分组组是否加过，若加过一次则有方向无方向都不用再增加(查询结果已排序过，无方向的课程放在前面)
					if (!courseGroupIdList.contains(teachGroupIdsStr)) {
						if (StrKit.isBlank(directionName)) {
							noDirectionWeight = PriceUtils.add(noDirectionWeight, maxweight);
							courseGroupIdList.add(teachGroupIdsStr);
						} else {
							// 无方向未增加过，进行有方向判断，若加过就不用加
							String mangGroupIdStrDireFlag = directionName + teachGroupIdsStr;
							if (!courseGroupIdList.contains(mangGroupIdStrDireFlag)) {
								existWeightMap.put(directionName, PriceUtils.add(existWeightMap.get(directionName), maxweight));
								courseGroupIdList.add(mangGroupIdStrDireFlag);
							}

						}
					}

            	//修改其他的课程，需要处理的分级教学的数据

            }else {
				// 无课程组直接加
				if (StrKit.isBlank(directionName)) {
					noDirectionWeight = PriceUtils.add(noDirectionWeight, weight);
				} else {
					existWeightMap.put(directionName, PriceUtils.add(existWeightMap.get(directionName), weight));
				}
				
			}
		}
		
		if (!existWeightMap.isEmpty()) {
			// 方向上权重加上无方向
			for (Map.Entry<String, BigDecimal> entry : existWeightMap.entrySet()) {
				existWeightMap.put(entry.getKey(), PriceUtils.add(noDirectionWeight, entry.getValue()));
			}
			
		} else {
			// 当加入的课程有指定的方向，则附上指定方向的权重
			if (directionId != null) {
				CcMajorDirection ccMajorDirection = CcMajorDirection.dao.findById(directionId);
				existWeightMap.put(ccMajorDirection.getStr("name"), noDirectionWeight);
			} else {
				existWeightMap.put("无方向", noDirectionWeight);
			}
			
		}
		
			
		return existWeightMap;
	}
	
	/**
	 * 返回某个指标点的各个方向上的剩余权重
	 * 
	 * @param indicationId
	 * @param courseGroupId
	 * @param courseId
	 * @return
	 */
	public Map<String, BigDecimal> getIndicationWeightRest(Long indicationId, Long courseGroupId, Long courseId, Long directionId,Long mangeGroupId) {
		Map<String, BigDecimal> restWeightMap = getIndicationWeightDirection(indicationId, courseGroupId, courseId, directionId,mangeGroupId,null);
		// 方向上权重加上无方向
		for (Map.Entry<String, BigDecimal> entry : restWeightMap.entrySet()) {
			restWeightMap.put(entry.getKey(), PriceUtils.sub(CcIndicationCourse.MAX_WEIGHT, entry.getValue()));
		}
					
		return restWeightMap;
	}

   
     /**
      * 判断是否批量更新课程成功
     * @param courseGroupId
     * @param indicationId
     * @return
     */
    public boolean batchUpdate(Long courseGroupId, Long indicationId, BigDecimal weight){
		Date date = new Date();
    	List<CcIndicationCourse> indicationCourses = CcIndicationCourse.dao.getSameGroupSameIndicationCourse(indicationId, courseGroupId);
		if(!indicationCourses.isEmpty()){
			for(CcIndicationCourse temp : indicationCourses){
				temp.set("modify_date", date);
				temp.set("weight", weight);
			}
			if(!CcIndicationCourse.dao.batchUpdate(indicationCourses, "modify_date,weight")){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}		
		}
		return true;	
	}
	

    /**
     * 返回指标点的权重和
     * @param indicationCourseList
     * @return
     */
    public Map<Long, BigDecimal> getIndicationWeight(List<CcIndicationCourse> indicationCourseList){
		Map<Long, BigDecimal> indicationWeightMap = Maps.newHashMap();
		Long prevCourseGroupId = null;
		Long prevTeachGroupId=null;
		Long prevgroupId=null;
		BigDecimal sumWeight=null;
		Long preIndcatId=null;
		for(CcIndicationCourse indicationCourse : indicationCourseList){

			Long indicationId = indicationCourse.getLong("indication_id");
			Long courseGroupId = indicationCourse.getLong("courseGroupId");
			BigDecimal weight = indicationCourse.getBigDecimal("weight");
			//课程分组Id
            Long groupId = indicationCourse.getLong("groupId");
            //分级教学Id
            Long teachGroupId = indicationCourse.getLong("teachGroupId");

            if (!indicationId.equals(preIndcatId)){
				preIndcatId=indicationId;
				prevTeachGroupId=null;
			}
			boolean isSameGroup = courseGroupId == null || (courseGroupId != null && !courseGroupId.equals(prevCourseGroupId));
			if(isSameGroup && teachGroupId==null){
				if(indicationWeightMap.get(indicationId) == null){
					indicationWeightMap.put(indicationId, weight);
				}else{
					indicationWeightMap.put(indicationId, PriceUtils._add(indicationWeightMap.get(indicationId), weight));
				}
				prevCourseGroupId = courseGroupId;		
				
			}

			if(teachGroupId!=null){
                //找到分级教学中课程分组里合计最大的权重
                List<CcIndicationCourse> teachGroupWeightList = CcIndicationCourse.dao.getGroupCourseIndication(indicationId, teachGroupId,1);
			    //分级教学有多个课程分组，只取其中课程分组中最大的权重
			    if (!teachGroupId.equals(prevTeachGroupId)){
                    //第一次进来肯定不相等，就把这些赋值
                    prevTeachGroupId=teachGroupId;
                    prevgroupId=groupId;
                    sumWeight=null;
                    for (CcIndicationCourse temp:teachGroupWeightList){
                        BigDecimal teachweight = temp.getBigDecimal("weight");
                        if(indicationWeightMap.get(indicationId) == null){
                            indicationWeightMap.put(indicationId, teachweight);
                        }else {
                            indicationWeightMap.put(indicationId, PriceUtils._add(indicationWeightMap.get(indicationId), teachweight));
                        }


                    }





                }

            }
		}
		
    	return indicationWeightMap;
    }
    
}
