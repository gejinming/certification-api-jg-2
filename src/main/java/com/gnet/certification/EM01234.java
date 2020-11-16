package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;


/**
 * 教学班课程目标达成度分析数据
 *
 * @author GJM
 * @Date 2020年11月09日
 */
@Transactional(readOnly = false)
@Service("EM01234")
public class EM01234 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		HashMap<Object, Object> result = new HashMap<>();
		Map<String, Object> param = request.getData();
		Long eduClassId = paramsLongFilter(param.get("eduClassId"));
		Long indicatorPointId = paramsLongFilter(param.get("indicatorPointId"));
		// 教学班编号为空过滤
		if (eduClassId == null) {
			return renderFAIL("0500", response, header);
		}
		CcTeacherCourse teacherCourse = CcTeacherCourse.dao.findCourseByClassId(eduClassId);
		if (teacherCourse == null) {
			return renderFAIL("0251", response, header);
		}
		if (indicatorPointId == null) {
			return renderFAIL("0230", response, header);
		}
		//这个课程当前老师的近三年的教学班
		ArrayList<Long> eduClassIdList = new ArrayList<>();
		Long teacherCourseId = teacherCourse.getLong("id");
		Long courseId = teacherCourse.getLong("course_id");
		Long teacherId = teacherCourse.getLong("teacher_id");
		Integer grade = teacherCourse.getInt("grade");
		ArrayList<Long> indicationIdLists = new ArrayList<>();
		//获取此指标点下的课程目标id
		List<CcCourseTargetIndication> indicationIdList = CcCourseTargetIndication.dao.findByIndicatorPointId(indicatorPointId, courseId);
		if (indicationIdList.size()==0){
			return renderFAIL("2578", response, header);
		}
		for (CcCourseTargetIndication temp : indicationIdList){
			Long indicationId = temp.getLong("indication_id");
			if (!indicationIdLists.contains(indicationId)){
				indicationIdLists.add(indicationId);
			}
		}

		//当前教学班开始学年
		Integer startYear = teacherCourse.getInt("start_year");
		//同一个老师可能有多个教学班
		List<CcEduclass> educlassList = CcEduclass.dao.findAllByCourseId(teacherCourseId);

		for (CcEduclass temp : educlassList ){
			Long classId = temp.getLong("id");
			if (!eduClassIdList.contains(classId)){
				eduClassIdList.add(classId);
			}
		}
		//其余两年的教学班,先预设当年学年是中间学年，所以secondYear=startYear+1，threeYear=startYear-1
		Integer secondYear=startYear+1;
		Integer threeYear=startYear-1;
		//预设
		Integer forYear=startYear+2;
		List<CcEduclass> ccEduclasseSecondYearList ;
		ccEduclasseSecondYearList = CcEduclass.dao.findAllByEduclass(courseId, secondYear, teacherId);
		List<CcEduclass> ccEduclasseThreeYearList;
		if (ccEduclasseSecondYearList.size()==0){
			//也就是当前学年为最大学年了，那就往下面数,secondYear就是第三学年的了threeYear就是第二学年的了
			secondYear=startYear-2;
			ccEduclasseSecondYearList = CcEduclass.dao.findAllByEduclass(courseId, secondYear, teacherId);
			for (CcEduclass temp : ccEduclasseSecondYearList){
				Long classId = temp.getLong("id");
				if (!eduClassIdList.contains(classId)){
					eduClassIdList.add(classId);
				}
			}

			ccEduclasseThreeYearList = CcEduclass.dao.findAllByEduclass(courseId, threeYear, teacherId);
			//startYear-1年
			for (CcEduclass temp : ccEduclasseThreeYearList){
				Long classId = temp.getLong("id");
				if (!eduClassIdList.contains(classId)){
					eduClassIdList.add(classId);
				}
			}



		}else{
			for (CcEduclass temp : ccEduclasseSecondYearList){
				Long classId = temp.getLong("id");
				if (!eduClassIdList.contains(classId)){
					eduClassIdList.add(classId);
				}
			}
			//往上一年存在数据，那就再往上一年forYear=startYear+2
			 ccEduclasseThreeYearList = CcEduclass.dao.findAllByEduclass(courseId, forYear, teacherId);
			 //如果再往上一年没有数据，那就往下一年
			if (ccEduclasseThreeYearList.size()==0){
				ccEduclasseThreeYearList = CcEduclass.dao.findAllByEduclass(courseId, threeYear, teacherId);
				//startYear-1年
				for (CcEduclass temp : ccEduclasseThreeYearList){
					Long classId = temp.getLong("id");
					if (!eduClassIdList.contains(classId)){
						eduClassIdList.add(classId);
					}
				}


			}else{

				for (CcEduclass temp : ccEduclasseThreeYearList){
					Long classId = temp.getLong("id");
					if (!eduClassIdList.contains(classId)){
						eduClassIdList.add(classId);
					}
				}


			}

		}


		ArrayList<Object> indicationeduClassList = new ArrayList<>();
		for (Long educlassId: eduClassIdList){
			List<CcIndication> courseIndicationList = CcIndication.dao.findCourseIndicationList(educlassId, courseId,indicationIdLists);
			indicationeduClassList.add(courseIndicationList);
		}
		/*List<CcIndication> courseIndicationList = CcIndication.dao.findCourseIndicationList(eduClassIdList, courseId);
		for (CcIndication temp : courseIndicationList){
			Long indicationId = temp.getLong("id");

		}*/
		result.put("data",indicationeduClassList);
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}


}