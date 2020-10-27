package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.gnet.pager.Pageable;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.plugin.activerecord.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自评报告按标题查询内容
 * 
 * @author GJM
 * @Date 2020年09月10日
 */
@Service("EM01219")
public class EM01219 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		HashMap<Object, Object> result = new HashMap<>();
		//标题id
		Long titleId = paramsLongFilter(params.get("titleId"));
		//自评报告id
		Long selfReportId = paramsLongFilter(params.get("selfReportId"));
		if ( titleId ==null || selfReportId==null){
			return renderFAIL("2574", response, header);
		}
		CcSelfreport selfReportInfo = CcSelfreport.dao.findSelfReport(selfReportId);
		Long majorId = selfReportInfo.getLong("major_id");
		Long graduateVerId = selfReportInfo.getLong("version_id");
		CcSelfreportContent ccSelfreportContent = CcSelfreportContent.dao.findSelfReportContent(titleId,selfReportId);
		//下面是如果没有数据那就给前端传需要的数据
		if (ccSelfreportContent==null){
			//标题为背景信息时传的内容
			if (titleId.equals(1l)){
				Office majorInfo = Office.dao.findMajorInfo(majorId,null);
				result.put("majorInfo",majorInfo);
			}
			//毕业指标点
			if (titleId.equals(19l) || titleId.equals(20l)|| titleId.equals(21l)|| titleId.equals(22l) ){
				//调用120接口
				IApi em00120 = SpringContextHolder.getBean("EM00120");
				Request requests = new Request();
				HashMap<Object, Object> map = new HashMap<>();
				map.put("graduateVerId",graduateVerId);
				requests.setData(map);
				Response em00120Result = em00120.excute(requests, response, header, method);
				Object em00120ResultData = em00120Result.getData();
				result.put("graduateAndindicationList",em00120ResultData);
			}
			//课程体系设计总体思路
			if (titleId.equals(68l)){
				Pageable pageable = new Pageable(null, null);
				Page<CcGraduate> ccGraduatePage = CcGraduate.dao.page(pageable, graduateVerId, null, null);
				List<CcGraduate> ccGraduateList = ccGraduatePage.getList();
				ArrayList<Object> graduatList = new ArrayList<>();

				for (CcGraduate temp: ccGraduateList){
					HashMap<Object, Object> map = new HashMap<>();
					Long graduateId = temp.getLong("id");
					Integer indexNum = temp.getInt("index_num");
					String content = temp.getStr("content");
					String courseNames="";
					List<CcIndicationCourse> indicationCourses = CcIndicationCourse.dao.findByFenjiCourseVersionId(graduateVerId, graduateId);
					for (int i=0;i<indicationCourses.size();i++){
						CcIndicationCourse course = indicationCourses.get(i);
						String name = course.getStr("name");
						if (i==0){
							courseNames=name+"、";
						}else {
							courseNames=courseNames+name+"、";
						}

					}
					map.put("indexNum",indexNum);
					map.put("content",content);
					map.put("courseNames",courseNames);
					graduatList.add(map);
					result.put("graduatCourseList",graduatList);

				}
			}
		}else{
			result.put("ccSelfreport", ccSelfreportContent);
		}

		return renderSUC(result, response, header);
	}


}
