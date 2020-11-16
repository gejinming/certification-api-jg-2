package com.gnet.certification;

import com.gnet.FileInfo;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.gnet.plugin.poi.ExcelDefinition;
import com.gnet.plugin.poi.ExcelParser;
import com.gnet.plugin.poi.Header2ExcelParser;
import com.gnet.plugin.poi.RowDefinition;
import com.gnet.plugin.poi.RowDefinition.ColumnDefinition;
import com.gnet.plugin.poi.RowDefinition.GroupColumnDefinition;
import com.gnet.service.CcCourseGradecomposeDetailService;
import com.gnet.utils.DictUtils;
import com.gnet.utils.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;


/**
 * 题目成绩导入上传解析接口
 * 
 * @author xzl
 * @Date 2018年2月11日15:31:00
 *
 * */


@Transactional(readOnly = false)
@Service("EM00981")
public class EM00981 extends BaseApi implements IApi {
	
	private static final Logger logger = Logger.getLogger(EM00981.class);

	@Autowired
	private CcCourseGradecomposeDetailService ccCourseGradecomposeDetailService;
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		
		Object fileInfoObject = param.get("fileInfo");
		FileInfo fileInfo = (FileInfo) fileInfoObject;
		Map<String, Object> result = Maps.newHashMap();

		// 开课课程成绩组成元素编号
		Long courseGradeComposeId = paramsLongFilter(param.get("courseGradeComposeId"));
		Long eduClassId = paramsLongFilter(param.get("eduClassId"));
		Long batchId = paramsLongFilter(param.get("batchId"));

		// 课程成绩组成为空过滤
		if (courseGradeComposeId == null) {
			return renderFAIL("0490", response, header);
		}

		// 教学班编号为空过滤
		if (eduClassId == null) {
			return renderFAIL("0500", response, header);
		}

		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findByCourseGradeComposeId(courseGradeComposeId);
		if(ccTeacherCourse == null){
			return renderFAIL("0501", response, header);
		}

		CcEduclass ccEduclass = CcEduclass.dao.findFilteredById(eduClassId);
		if(ccEduclass == null){
			return renderFAIL("0381",response, header);
		}

		// 判断录入成绩类型是否是由题目明细计算获得,1:指标点成绩直接输入,2:由题目明细计算获得
		if(!CcCourseGradecompose.SUMMARY_INPUT_SCORE.equals(ccTeacherCourse.getInt("input_score_type")) && !CcCourseGradecompose.SUMMARY_MANYINPUT_SCORE.equals(ccTeacherCourse.getInt("input_score_type"))){
			return renderFAIL("2102", response, header);
		}

		List<CcStudent> studentList = CcStudent.dao.findByEduclassIdOrderByStudentNo(eduClassId);
        if(studentList.isEmpty()){
			return renderFAIL("2104", response, header);
		}

		//List<CcCourseGradeComposeDetail> ccCourseGradeComposeDetails = CcCourseGradeComposeDetail.dao.findFilteredByColumn("course_gradecompose_id", courseGradeComposeId);
		List<CcCourseGradeComposeDetail> ccCourseGradeComposeDetails = CcCourseGradeComposeDetail.dao.topicList0(courseGradeComposeId, batchId);
		if(ccCourseGradeComposeDetails.isEmpty()){
			return renderFAIL("2105", response, header);
		}

		//题目key题号(分数)value编号map
		Map<String, Long> subjectMap = new HashMap<>();
		Map<String, BigDecimal> subjectScoreMap = new HashMap<>();
		for(CcCourseGradeComposeDetail ccCourseGradeComposeDetail : ccCourseGradeComposeDetails){
			String detail = String.format("%s(%s)", ccCourseGradeComposeDetail.getStr("name"), ccCourseGradeComposeDetail.getBigDecimal("score"));
			subjectMap.put(detail, ccCourseGradeComposeDetail.getLong("id"));
			subjectScoreMap.put(detail, ccCourseGradeComposeDetail.getBigDecimal("score"));
		}

        Set<Long> studentIds = new HashSet<>();
		Set<Long> detailIds = new HashSet<>();

		//需要新增的学生题目分数
        List<CcCourseGradecomposeStudetail> addList = Lists.newArrayList();
        //需要更新的学生题目分数
        List<CcCourseGradecomposeStudetail> editList = Lists.newArrayList();

        List<Map<String, Object>> subjects = Lists.newArrayList();

        try {

			Map<String, Object> header2 = new HashMap<>();
			//通过配置文件得到对应列的列名，如果想改变列名直接修改配置文件即可
			String index = DictUtils.findLabelByTypeAndKey("subjectScoreImport", 1);
			String no =  DictUtils.findLabelByTypeAndKey("subjectScoreImport", 2);
            String name =  DictUtils.findLabelByTypeAndKey("subjectScoreImport", 3);
            String className =  DictUtils.findLabelByTypeAndKey("subjectScoreImport", 4);

			String url = fileInfo.getTempUrl();
			File file = new File(url);
	        InputStream inputStream = new FileInputStream(file); 
	        ExcelParser excelParser = new Header2ExcelParser();
	        ExcelDefinition excelDefinition = excelParser.parse(inputStream, header2, 1);

            //header中的数据直接返回
			RowDefinition prHeaderOne = excelDefinition.getHeader();
			GroupColumnDefinition prHeaderTow = prHeaderOne.getGroupColumnDefinition();
			//解析hear中的数据验证是否合理
			List<ColumnDefinition> preHeader = prHeaderTow.getColumns();

			ColumnDefinition first = preHeader.get(0);
			ColumnDefinition second = preHeader.get(1);
			ColumnDefinition third  = preHeader.get(2);
			ColumnDefinition fourth  = preHeader.get(3);

			if(!index.equals(first.getName())){
                return renderFAIL("2103", response, header, String.format("excel中的第1列列名不是%s,请检查", index));
			}
			if(!no.equals(second.getName())){
				return renderFAIL("2103", response, header, String.format("excel中的第2列列名不是%s,请检查", no));
			}
			if(!name.equals(third.getName())){
				return renderFAIL("2103", response, header, String.format("excel中的第3列列名不是%s,请检查", name));
			}
            if(!className.equals(fourth.getName())){
				return renderFAIL("2103", response, header, String.format("excel中的第4列列名不是%s,请检查", className));
			}

            if(preHeader.size() < 5){
				return renderFAIL("2106", response, header);
			}

			List<String> nos = Lists.newArrayList();
			Map<String, String> sameNoMap = new HashMap<>();
			for(int i = 4; i < preHeader.size(); i++){
				ColumnDefinition columnDefinition = preHeader.get(i);
				String subjectName = columnDefinition.getName();
				//验证头中的题目是否正确
				if(!subjectMap.keySet().contains(subjectName)){
					return renderFAIL("2103", response, header, String.format("该成绩组成下不存在第%s列的题目，请检查", i+1));
				}
				//验证题号是否重复
				String column = sameNoMap.get(subjectName);
				if(StrKit.isBlank(column)){
					sameNoMap.put(subjectName, String.format("%s", i+1));
				}else{
					sameNoMap.put(subjectName, String.format("%s,%s", column, i+1));
				}
				if(!nos.contains(subjectName)){
					nos.add(subjectName);
				}else{
					return renderFAIL("2103", response, header, String.format("excel中的第%s列的题目重复了", sameNoMap.get(subjectName)));
				}
			}

            //解析excel中的body数据
			List<RowDefinition> preBody = excelDefinition.getBody();
			for(int rowIndex = 1; rowIndex <= preBody.size(); rowIndex++) {
				Map<String, Object> map = new HashMap<>();
				RowDefinition rowDefinition = preBody.get(rowIndex-1);
				Map<Integer, ColumnDefinition> indexMap = rowDefinition.getIndexs();
				for(Map.Entry<Integer, ColumnDefinition> entry : indexMap.entrySet()) {
					ColumnDefinition column = entry.getValue();
					map.put(column.getName(), column.getValue());
				}
				subjects.add(map);
			}

			ccCourseGradecomposeDetailService.validateImportSubjectScore(subjectMap, studentIds, detailIds, addList, editList, subjects, eduClassId, courseGradeComposeId, studentList, ccEduclass, subjectScoreMap,batchId);
			result.put("header", prHeaderTow);
		} catch (Exception e) {
			e.printStackTrace();
			if (logger.isErrorEnabled()) {
				logger.error("批量导入成绩失败", e);
			}
			return renderFAIL("400", response, header, StringUtil.judgeContainsStr(e.getMessage()) ? "请检查模板的是否正确。包括：1.题目是否是该成绩组成下存在的题目。2.是否修改过结构。3.得分是否超过题目满分值。4.得分最多只保留小数点后2位。解决方案：重新下载一份。" : e.getMessage());
		}

		result.put("subjects", subjects);
		return renderSUC(result, response, header);
	}

}
