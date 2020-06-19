package com.gnet.certification;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradecompose;
import com.gnet.model.admin.CcCourseGradecomposeIndication;
import com.gnet.model.admin.CcEduclass;
import com.gnet.model.admin.CcEvalute;
import com.gnet.model.admin.CcEvaluteType;
import com.gnet.model.admin.CcIndicationCourse;
import com.gnet.model.admin.CcStudent;
import com.gnet.model.admin.CcTeacherCourse;
import com.gnet.plugin.poi.ExcelUtil;
import com.gnet.service.CcStudentService;
import com.gnet.utils.DictUtils;
import com.jfinal.log.Logger;


/**
 * 考核成绩分析法教学班学生批量成绩录入Excel模板下载
 * 
 * @author SY
 * @Date 2017年10月6日
 */
@Transactional(readOnly = false)
@Service("EM00742")
@Deprecated
public class EM00742 extends BaseApi implements IApi {
	
	private static final Logger logger = Logger.getLogger(EM00742.class);
	
	@Autowired
	private CcStudentService ccStudentService;
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		// 教学班编号
		Long eduClassId = paramsLongFilter(param.get("eduClassId"));
		// 教学班编号为空过滤
		if (eduClassId == null) {
			return renderFAIL("0500", response, header);
		}
		// 获得教师开课课程
		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findCourseByClassId(eduClassId);
		if (ccTeacherCourse == null) {
			return renderFAIL("0501", response, header);
		}
//		ExportStudentScoreHelper exportStudentScoreHelper = SpringContextHolder.getBean(ExportStudentScoreHelper.class);
		// Map<第几行，Map<第几列,Map<当前成绩组成细啊有几个考评点/考评点名称数组, 对应的数值>>>
		Map<Integer, Map<Integer, Map<String, Object>>> createHeadMap = new HashMap<>();
		
		// 教师开课类型,1:考核成绩分析法，2：评分表分析法
		Integer resultType = ccTeacherCourse.getInt("result_type");
		// 而外参数是从第几列开始的
		Integer startNaturalColumnIndex = 5;
		// 而外参数是从第几行开始的
		Integer startNaturalRowIndex = 1;
		if(CcTeacherCourse.RESULT_TYPE_SCORE == resultType) {
			// 考核
			createHeadMap = getScoreMap(ccTeacherCourse, startNaturalRowIndex, startNaturalColumnIndex);
		} else {
			// 评分表
			createHeadMap = getEvaluateMap(ccTeacherCourse, startNaturalRowIndex, startNaturalColumnIndex);
		}
				
    	// 获取学生数
    	CcEduclass ccEduclass = CcEduclass.dao.findFilteredById(eduClassId);
    	String educlassName = ccEduclass.getStr("educlass_name");
    	List<CcStudent> studentList = CcStudent.dao.findByEduclassId(eduClassId);
    	for(int i = 0; i < studentList.size();) {
    		CcStudent temp = studentList.get(i);
    		i++;
    		temp.put("educlass_name", educlassName);
    		temp.put("index", i);
    	}
        Map<String,String> headMap = new LinkedHashMap<String,String>();
        headMap.put("index","序号");
        headMap.put("student_no","学号");
        headMap.put("name","姓名");
        headMap.put("educlass_name","教学班名称");
        
        String title = "注意：1.名单要求从第3行开始录入（例如：姓名为林木）2.学号以文本形式填写（左上角应该有个绿色三角才对）";
        String exportUrl = "F://_Use_One//output//student"+new Date().getTime()+".xls";
        try {
	        OutputStream outXlsx = new FileOutputStream(exportUrl);
	        System.out.println("正在导出xlsx....");
	        Date d2 = new Date();
	        ExcelUtil.exportExcel(title, headMap, createHeadMap, studentList, null, 0, outXlsx);
	        System.out.println("共"+studentList.size()+"条数据,执行"+(new Date().getTime()-d2.getTime())+"ms");
	        outXlsx.close();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return renderFILE(new File(exportUrl), response, header);
	}
	
	
	/**
	 * 获得考评点分析法的层次结构
	 * @param ccTeacherCourse
	 * 			教师开课信息
	 * @param startNaturalRowIndex
	 * 			第几行开始
	 * @param startNaturalColumnIndex 
	 * 			第几列开始
	 * @author SY 
	 * @version 创建时间：2017年10月10日 上午11:15:38
	 * return 
	 * 	Map<第几行，Map<第几列,Map<当前成绩组成细啊有几个考评点/考评点名称数组, 对应的数值>>> 
	 */
	private Map<Integer, Map<Integer, Map<String, Object>>> getEvaluateMap(CcTeacherCourse ccTeacherCourse, Integer startNaturalRowIndex, Integer startNaturalColumnIndex) {
		
		// Map<第几行，Map<第几列,Map<当前成绩组成细啊有几个考评点/考评点名称数组, 对应的数值>>>
		Map<Integer, Map<Integer, Map<String, Object>>> createHeadMap = new HashMap<>();
		Long teacherCourseId = ccTeacherCourse.getLong("id");
		Long courseId = ccTeacherCourse.getLong("course_id");
		Map<Integer, Map<String, Object>> sencondMap1 = new HashMap<>();
		Map<Integer, Map<String, Object>> sencondMap2 = new HashMap<>();
		Map<Integer, Map<String, Object>> sencondMap3 = new HashMap<>();
		
		List<String> evaluteTypeNameList = new ArrayList<>();
		// Map<考评点类型编号, name>
		Map<Long, String> evaluteTypeMap = new HashMap<>();
		// 获取考评点类型
		List<CcEvaluteType> ccEvaluteTypes = CcEvaluteType.dao.findByTeacherCourseId(teacherCourseId);
		for(CcEvaluteType temp : ccEvaluteTypes) {
			Integer type = temp.getInt("type");
			Long ccEvaluteTypeId = temp.getLong("id");
			String name = DictUtils.findLabelByTypeAndKey("evaluteType", type);
			evaluteTypeNameList.add(name);
			evaluteTypeMap.put(ccEvaluteTypeId, name);
		}
		
		// 获取指标点
		// Map<考评点类型编号， 指标点中文列表>
//		Map<Long, List<String>> ccIndicationMap = new HashMap<>();
		// Map<指标点编号，指标点名称>
		Map<Long, String> ccIndicationMap = new HashMap<>();
		List<CcIndicationCourse> ccIndicationCourses = CcIndicationCourse.dao.findDetailByCourseId(courseId);
		for(CcIndicationCourse temp : ccIndicationCourses) {
			Long indicationId = temp.getLong("indication_id");
			String content = "指标点" + temp.getInt("graduateIndexNum") + "-" + temp.getInt("index_num");
			ccIndicationMap.put(indicationId, content);
		}
		
		// 获取考评点
		// Map<考评点类型编号, List<CcEvalute>>
		Map<Long, List<CcEvalute>> ccEvaluteTypeAndEvaluteMap = new HashMap<>();
		// Map<考评点编号， List<indicationName>>
		Map<Long, List<String>> ccIndicationAndEvaluteMap = new HashMap<>();
		// Map<考评点类型编号-指标点编号， List<CcEvaluteName>>
		Map<String, List<String>> ccTypeAndIndicationMap = new HashMap<>();
		List<CcEvalute> ccEvalutes = CcEvalute.dao.findFilteredByColumn("teacher_course_id", teacherCourseId);
		for(CcEvalute temp : ccEvalutes) {
			Long evaluteTypeId = temp.getLong("evalute_type_id");
			String content = temp.getStr("content");
			
			// 考评点类型关系
			List<CcEvalute> ccEvaluteTypeTemp = ccEvaluteTypeAndEvaluteMap.get(evaluteTypeId);
			if(ccEvaluteTypeTemp == null || ccEvaluteTypeTemp.isEmpty()) {
				ccEvaluteTypeTemp = new ArrayList<>();
			}
			ccEvaluteTypeTemp.add(temp);
			ccEvaluteTypeAndEvaluteMap.put(evaluteTypeId, ccEvaluteTypeTemp);
			
			// 指标点关系
			Long indicationId = temp.getLong("indication_id");
			// TODO 这个可能需要排个序
			List<String> ccEvaluteIndicationTemp = ccIndicationAndEvaluteMap.get(evaluteTypeId);
			if(ccEvaluteIndicationTemp == null || ccEvaluteIndicationTemp.isEmpty()) {
				ccEvaluteIndicationTemp = new ArrayList<>();
			}
			String indicationName = ccIndicationMap.get(indicationId);
//			ccEvaluteIndicationTemp.remove(indicationName);
			if(!ccEvaluteIndicationTemp.contains(indicationName)) {
				ccEvaluteIndicationTemp.add(indicationName);				
			}
			ccIndicationAndEvaluteMap.put(evaluteTypeId, ccEvaluteIndicationTemp);
			
			// 考评点类型And指标点关系
			String key = evaluteTypeId + "-" + indicationId;
			List<String> ccEvaluteTypeAndIndicationTemp = ccTypeAndIndicationMap.get(key);
			if(ccEvaluteTypeAndIndicationTemp == null || ccEvaluteTypeAndIndicationTemp.isEmpty()) {
				ccEvaluteTypeAndIndicationTemp = new ArrayList<>();
			}
			ccEvaluteTypeAndIndicationTemp.add(content);
			ccTypeAndIndicationMap.put(key, ccEvaluteTypeAndIndicationTemp);
		}
		
		Integer firstNaturalColumnIndex = startNaturalColumnIndex;
		Integer secondNaturalColumnIndex = startNaturalColumnIndex;
		Integer thirdNaturalColumnIndex = startNaturalColumnIndex;
		List<Long> evaluteTypeAdded = new ArrayList<>();
		for(Map.Entry<String, List<String>> entry : ccTypeAndIndicationMap.entrySet()) {
			String key = entry.getKey();
			String keys [] = key.split("-");
			Long evaluteTypeId = Long.valueOf(keys[0]);
			Long indicationId = Long.valueOf(keys[1]);
			// 当前考评点类型和指标点下，所有的考评点
			List<String> ccEvalutesTemp = entry.getValue();
			Integer number = 0;
			if(!evaluteTypeAdded.contains(evaluteTypeId)) {
				// 此考评点类型下的所有考评点
				List<CcEvalute> ccEvaluteTypeTemp = ccEvaluteTypeAndEvaluteMap.get(evaluteTypeId);
				number = ccEvaluteTypeTemp.size();
				Map<String, Object> thirdMap1 = new HashMap<>();
				thirdMap1.put("value", evaluteTypeNameList.toArray(new String[evaluteTypeNameList.size()]));
				thirdMap1.put("number", number);
				sencondMap1.put(firstNaturalColumnIndex, thirdMap1);
				// 因为只有第二层，所以直接加上去就好
				firstNaturalColumnIndex = firstNaturalColumnIndex + number;
				evaluteTypeAdded.add(evaluteTypeId);
			}
			
			
			for(;secondNaturalColumnIndex != firstNaturalColumnIndex;) {
				// sencondMap2 赋值	
				// 数量是当前考评点类型和指标点下的个数
				number = ccEvalutesTemp.size();
				List<String> evaluteTypeIdAndIndicationNameList = ccIndicationAndEvaluteMap.get(evaluteTypeId);
				Map<String, Object> thirdMap2 = new HashMap<>();
				thirdMap2.put("value", evaluteTypeIdAndIndicationNameList.toArray(new String[evaluteTypeIdAndIndicationNameList.size()]));
				thirdMap2.put("number", number);
				sencondMap2.put(secondNaturalColumnIndex, thirdMap2);
				secondNaturalColumnIndex = secondNaturalColumnIndex + number;
				for(;secondNaturalColumnIndex != thirdNaturalColumnIndex;) {
					// sencondMap3 赋值	
					Map<String, Object> thirdMap3 = new HashMap<>();
					thirdMap3.put("value", ccEvalutesTemp.toArray(new String[ccEvalutesTemp.size()]));
					thirdMap3.put("number", 1);
					sencondMap3.put(thirdNaturalColumnIndex, thirdMap3);
					thirdNaturalColumnIndex++;
				}
			}
		}
		// 获取s
		
		createHeadMap.put(startNaturalRowIndex, sencondMap1);
		createHeadMap.put(startNaturalRowIndex + 1, sencondMap2);
		createHeadMap.put(startNaturalRowIndex + 2, sencondMap3);
		// 获取这些成绩组成的指标点
		return createHeadMap;
	}
	
	/**
	 * 获得考核分析法的层次结构
	 * @param ccTeacherCourse
	 * 			教师开课信息
	 * @param startNaturalRowIndex
	 * 			第几行开始
	 * @param startNaturalColumnIndex 
	 * 			第几列开始
	 * @author SY 
	 * @version 创建时间：2017年10月10日 上午11:15:38
	 * return 
	 * 	Map<第几行，Map<第几列,Map<当前成绩组成细啊有几个考评点/考评点名称数组, 对应的数值>>> 
	 */
	private Map<Integer, Map<Integer, Map<String, Object>>> getScoreMap(CcTeacherCourse ccTeacherCourse, Integer startNaturalRowIndex, Integer startNaturalColumnIndex) {
		
		// Map<第几行，Map<第几列,Map<当前成绩组成细啊有几个考评点/考评点名称数组, 对应的数值>>>
		Map<Integer, Map<Integer, Map<String, Object>>> createHeadMap = new HashMap<>();
		Long teacherCourseId = ccTeacherCourse.getLong("id");
		Map<Integer, Map<String, Object>> sencondMap1 = new HashMap<>();
		Map<Integer, Map<String, Object>> sencondMap2 = new HashMap<>();
		
		// Map<开课课程成绩组成元素编号, 成绩组成名称>
		Map<Long, String> courseGradecomposeMap = new HashMap<>();
		// Map<开课课程成绩组成元素编号, 指标点名称>
		Map<Long, List<String>> ccCourseGradecomposeIndicationMap = new HashMap<>();
				
		// 获取教师开课的成绩组成
		List<CcCourseGradecompose> courseGradecomposeList = CcCourseGradecompose.dao.findByTeacherCourseIdOrderBySort(teacherCourseId);
		String[] gradecomposes = new String[courseGradecomposeList.size()];
		for(int i = 0; i < courseGradecomposeList.size(); i++) {
			CcCourseGradecompose temp = courseGradecomposeList.get(i);
			gradecomposes[i] = temp.getStr("name");
			Long courseGradecomposeId = temp.getLong("id");
			courseGradecomposeMap.put(courseGradecomposeId, gradecomposes[i]);
			ccCourseGradecomposeIndicationMap.put(courseGradecomposeId, new ArrayList<String>());
		}
		List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndicationList = CcCourseGradecomposeIndication.dao.findDetailByTeacherCourseId(teacherCourseId);
		for(CcCourseGradecomposeIndication temp : ccCourseGradecomposeIndicationList) {
			Long courseGradecomposeId = temp.getLong("course_gradecompose_id");
			String content = "指标点" + temp.getInt("graduateNum") + "-" + temp.getInt("indicationIndexNum");
			List<String> list = ccCourseGradecomposeIndicationMap.get(courseGradecomposeId);
			list.add(content);
		}
		
		
		Integer firstNaturalColumnIndex = startNaturalColumnIndex;
		Integer secondNaturalColumnIndex = startNaturalColumnIndex;
		for(int i = 0; i < courseGradecomposeList.size(); i++) {
			// sencondMap1 赋值
			CcCourseGradecompose temp = courseGradecomposeList.get(i);
			Long courseGradecomposeId = temp.getLong("id");
			
			List<String> indicationList = ccCourseGradecomposeIndicationMap.get(courseGradecomposeId);
			Integer number = indicationList.size();
			
			Map<String, Object> thirdMap1 = new HashMap<>();
			thirdMap1.put("value", gradecomposes);
			thirdMap1.put("number", number);
			sencondMap1.put(firstNaturalColumnIndex, thirdMap1);
			// 因为只有第二层，所以直接加上去就好
			firstNaturalColumnIndex = firstNaturalColumnIndex + number;
			
			for(;secondNaturalColumnIndex != firstNaturalColumnIndex;) {
				// sencondMap2 赋值	
				Map<String, Object> thirdMap2 = new HashMap<>();
				thirdMap2.put("value", indicationList.toArray(new String[indicationList.size()]));
				thirdMap2.put("number", 1);
				sencondMap2.put(secondNaturalColumnIndex, thirdMap2);
				secondNaturalColumnIndex++;
			}
		}
		
		
		createHeadMap.put(startNaturalRowIndex, sencondMap1);
		createHeadMap.put(startNaturalRowIndex + 1, sencondMap2);
		// 获取这些成绩组成的指标点
		return createHeadMap;
	}


	class Student {
	    private String name;
	    private int age;
	    private Date birthday;
	    private float height;
	    private double weight;
	    private boolean sex;

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public Integer getAge() {
	        return age;
	    }

	    public Date getBirthday() {
	        return birthday;
	    }

	    public void setBirthday(Date birthday) {
	        this.birthday = birthday;
	    }

	    public float getHeight() {
	        return height;
	    }

	    public void setHeight(float height) {
	        this.height = height;
	    }

	    public double getWeight() {
	        return weight;
	    }

	    public void setWeight(double weight) {
	        this.weight = weight;
	    }

	    public boolean isSex() {
	        return sex;
	    }

	    public void setSex(boolean sex) {
	        this.sex = sex;
	    }

	    public void setAge(Integer age) {
	        this.age = age;
	    }
	}
	
	
}