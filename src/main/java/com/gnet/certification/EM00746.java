package com.gnet.certification;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.gnet.model.admin.CcCourseGradecomposeIndication;
import com.gnet.model.admin.CcEduclass;
import com.gnet.model.admin.CcEvalute;
import com.gnet.model.admin.CcScoreStuIndigrade;
import com.gnet.model.admin.CcStudent;
import com.gnet.model.admin.CcStudentEvalute;
import com.gnet.model.admin.CcTeacherCourse;
import com.gnet.plugin.configLoader.ConfigUtils;
import com.gnet.plugin.poi.ExcelExporter;
import com.gnet.plugin.poi.RowDefinition;
import com.gnet.service.CcStudentService;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;


/**
 * 教学班学生成绩Excel数据下载
 * 
 * @author SY
 * @Date 2017年11月3日
 */
@Transactional(readOnly = false)
@Service("EM00746")
public class EM00746 extends BaseApi implements IApi {
	
	private static final Logger logger = Logger.getLogger(EM00746.class);
	
	@Autowired
	private CcStudentService ccStudentService;
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		// 教学班编号
		final Long eduClassId = paramsLongFilter(param.get("eduClassId"));
		// 教学班编号为空过滤
		if (eduClassId == null) {
			return renderFAIL("0500", response, header);
		}
		// 获得教师开课课程
		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findCourseByClassId(eduClassId);
		if (ccTeacherCourse == null) {
			return renderFAIL("0501", response, header);
		}
		
				
//        String title = "注意：1.名单要求从第3行开始录入（例如：姓名为林木）2.学号以文本形式填写（左上角应该有个绿色三角才对）";
//        String exportUrl = "F://_Use_One//output//student"+new Date().getTime()+".xls";
        String fileUrl = PathKit.getWebRootPath() + ConfigUtils.getStr("excel", "createPath");
        String exportUrl = PathKit.getWebRootPath() + ConfigUtils.getStr("excel", "createPath") + "学生成绩备份-"+new Date().getTime()+".xls";
        try {
        	// 判断是否存在路径，不存在就创建
        	File dir = new File(fileUrl);
        	if (!dir.exists()) {
    			dir.mkdirs();
        	}
        	// 教师开课类型,1:考核成绩分析法，2：评分表分析法
    		Integer resultType = ccTeacherCourse.getInt("result_type");
    		// 而外参数是从第几列开始的
    		Integer startNaturalColumnIndex = 5;
//    		// 而外参数是从第几行开始的
//    		Integer startNaturalRowIndex = 1;
    		Long teacherCourseId = ccTeacherCourse.getLong("id");
    		if(CcTeacherCourse.RESULT_TYPE_SCORE == resultType) {
    			// 考核
    			RowDefinition rowDefinition = ccStudentService.getScoreDefinition(ccTeacherCourse, startNaturalColumnIndex);
    			
    			// 获取按照index排序
    			final List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndicationList = CcCourseGradecomposeIndication.dao.findDetailByTeacherCourseId(teacherCourseId);
    			
    			ExcelExporter.exportToExcel(2, rowDefinition, new ExcelExporter.ExcelExporterDataProcessor() {
                    @Override
                    public List<List<String>> invoke() {
                        List<List<String>> result = new ArrayList<>();

                        CcEduclass ccEduclass = CcEduclass.dao.findFilteredById(eduClassId);
                    	String educlassName = ccEduclass.getStr("educlass_name");
                    	
                        List<CcStudent> studentList = CcStudent.dao.findByEduclassIdOrderByStudentNo(eduClassId);
                        // 获取学生考核分析法学生成绩，按照index排序
            			List<CcScoreStuIndigrade> ccScoreStuIndigradeList = CcScoreStuIndigrade.dao.findAllByEduClassOrderByIndex(eduClassId);
                        // Map<studentNo+","+gradecompose_indication_id, grade>
            			Map<String, BigDecimal> ccScoreStuIndigradeMap = new HashMap<>();
            			for(CcScoreStuIndigrade temp : ccScoreStuIndigradeList) {
            				String studentNo = temp.getStr("student_no");
            				Long gradecomposeIndicationId = temp.getLong("gradecompose_indication_id");
            				BigDecimal grade = temp.getBigDecimal("grade");
            				String key = studentNo + "," + gradecomposeIndicationId;
            				ccScoreStuIndigradeMap.put(key, grade);
            			}
            			
                        for(int i = 0; i < studentList.size();) {
                    		CcStudent temp = studentList.get(i);
                    		List<String> data1 = new ArrayList<>();
                    		String studentNo = temp.getStr("student_no");
                    		i++;
                    		temp.put("index", i);
                    		data1.add(i+"");
                    		data1.add(studentNo);
                    		data1.add(temp.getStr("name"));
                    		data1.add(educlassName);
                    		
                    		// 具体的分数 
                    		// 循环开课课程成绩组成元素指标点关联表
                    		for(CcCourseGradecomposeIndication ccCourseGradecomposeIndication : ccCourseGradecomposeIndicationList) {
                    			Long courseGradecomposeIndicationId = ccCourseGradecomposeIndication.getLong("id");
                    			String key = studentNo + "," + courseGradecomposeIndicationId;
                    			BigDecimal score = ccScoreStuIndigradeMap.get(key);
                    			if(score != null) {
                    				data1.add(score.toString());
                    			} else {
                    				data1.add("");
                    			}
                    		}
                    		
                    		result.add(data1);
                    	}
                        

                        return result;
                    }
                }, exportUrl);
                //}, "/Users/xuqiang/Work/temp/output2.xls");
    		} else {
    			// 评分表
    			RowDefinition rowDefinition = ccStudentService.getEvaluateDefinition(ccTeacherCourse, startNaturalColumnIndex,null);
    			// 获取考评点
    			// 按照index排序的数据
    			final List<CcEvalute> ccEvalutes = CcEvalute.dao.findByTeacherCourse(teacherCourseId);
    			
    			try {
    			ExcelExporter.exportToExcel(3, rowDefinition, new ExcelExporter.ExcelExporterDataProcessor() {
                    @Override
                    public List<List<String>> invoke() {
                        List<List<String>> result = new ArrayList<>();

                        CcEduclass ccEduclass = CcEduclass.dao.findFilteredById(eduClassId);
                    	String educlassName = ccEduclass.getStr("educlass_name");
                    	
                        List<CcStudent> studentList = CcStudent.dao.findByEduclassIdOrderByStudentNo(eduClassId);
                        // 找到当前教学班所有学生的考评点成绩
                        List<CcStudentEvalute> ccStudentEvaluteList = CcStudentEvalute.dao.findAllByEduClassOrderByIndex(eduClassId);
                        // 由于上述list已经按照index排序，和rowDefinition的排序方式一样，所以直接按次序增加到map中即可，等待下次使用。此处耦合性很高。以后可以改 TODO SY
                        // Map<studentNo, List<evaluteId>>
                        Map<String, List<Long>> ccStudentEvaluteMap = new HashMap<>();
                        // Map<studentNo+","+evaluteId, levelName>
                        Map<String, String> studentEvaluteIdAndName = new HashMap<>();
                        for(CcStudentEvalute temp : ccStudentEvaluteList) {
                        	String studentNo = temp.getStr("student_no");
                        	Long evaluteId = temp.getLong("evalute_id");
                			String levelName = temp.getStr("level_name").replace("(", "")
                        												.replace("0", "")
                        												.replace(".", "")
                        												.replace("1", "")
                        												.replace("2", "")
                        												.replace("4", "")
                        												.replace("5", "")
                        												.replace("6", "")
                        												.replace("8", "")
                        												.replace(")", "")
                        												;
                			studentEvaluteIdAndName.put(studentNo + "," + evaluteId, levelName);
                        	List<Long> evaluteIdist = ccStudentEvaluteMap.get(studentNo);
                        	if(evaluteIdist == null) {
                        		evaluteIdist = new ArrayList<>();
                        	}
                        	evaluteIdist.add(evaluteId);
                        	ccStudentEvaluteMap.put(studentNo, evaluteIdist);
                        }
                        
                        for(int i = 0; i < studentList.size();) {
                    		CcStudent temp = studentList.get(i);
                    		List<String> data1 = new ArrayList<>();
                    		String studentNo = temp.getStr("student_no");
                    		i++;
                    		temp.put("index", i);
                    		data1.add(i+"");
                    		data1.add(studentNo);
                    		data1.add(temp.getStr("name"));
                    		data1.add(educlassName);
                    		
                    		// 具体的分数
                    		List<Long> evaluteIdList = ccStudentEvaluteMap.get(studentNo);
                    		if(evaluteIdList != null) {
                    			// 按照我们获取的次序进行曾家
                    			for(CcEvalute ccEvalute : ccEvalutes) {
                    				Long evaluteId = ccEvalute.getLong("id");
                    				// 学生是否存在着数据
                    				if(evaluteIdList.contains(evaluteId)) {
                    					String levelName = studentEvaluteIdAndName.get(studentNo + "," + evaluteId);
                						data1.add(levelName);
                					} else {
                						data1.add("");
                					}
                        		}
                    		}
                    		
                    		result.add(data1);
                    	}
                        

                        return result;
                    }
                }, exportUrl);
    			} catch (Throwable t) {
    				t.printStackTrace();
    			}
    		}

		} catch (Exception e) {
			System.out.println(e);
		}
		return renderFILE(new File(exportUrl), response, header);
	}
	
	
}