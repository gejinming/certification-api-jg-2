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
import com.gnet.service.CcStudentService;
import com.gnet.utils.DictUtils;
import com.gnet.utils.PriceUtils;
import com.gnet.utils.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.log.Logger;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 题目导入上传解析接口
 * 
 * @author xzl
 * @Date 2018年2月7日11:32:20
 */
@Transactional(readOnly = false)
@Service("EM00971")
public class EM00971 extends BaseApi implements IApi {
	
	private static final Logger logger = Logger.getLogger(EM00971.class);

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

		// 课程成绩组成为空过滤
		if (courseGradeComposeId == null) {
			return renderFAIL("0490", response, header);
		}

		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findByCourseGradeComposeId(courseGradeComposeId);
		if(ccTeacherCourse == null){
			return renderFAIL("0501", response, header);
		}

		// 判断录入成绩类型是否是由题目明细计算获得,1:指标点成绩直接输入,2:由题目明细计算获得
		if(!CcCourseGradecompose.SUMMARY_INPUT_SCORE.equals(ccTeacherCourse.getInt("input_score_type"))){
			return renderFAIL("2102", response, header);
		}

		//需要导入的题目数据
		List<Map<String, Object>> subjects = Lists.newArrayList();
		//需要增加的题目列表
		List<CcCourseGradeComposeDetail> ccCourseGradeComposeDetails = Lists.newArrayList();
		//课程目标序号和编号对应map
		Map<String, Long> indicationIdMap = new HashMap<>();
		List<CcCourseGradecomposeDetailIndication> ccCourseGradecomposeDetailIndications = Lists.newArrayList();

		try {

			Map<String, Object> header2 = new HashMap<>();
			//开课课程下成绩组成关联的课程目标
			List<CcIndication> ccIndicationList = CcIndication.dao.findCourseGradeComposeId(courseGradeComposeId);
			List<String> ccIndications = Lists.newArrayList();
			for(CcIndication ccIndication : ccIndicationList){
				String content = String.format("CO%s", ccIndication.getInt("sort"));
				ccIndications.add(content);
				indicationIdMap.put(content, ccIndication.getLong("id"));
			}

			//通过配置文件得到对应列的列名，如果想改变列名直接修改配置文件即可
			String no = DictUtils.findLabelByTypeAndKey("subjectImport", 1);
			String score = DictUtils.findLabelByTypeAndKey("subjectImport", 2);
			String supportIndication = DictUtils.findLabelByTypeAndKey("subjectImport", 3);
			String detail = DictUtils.findLabelByTypeAndKey("subjectImport", 4);
			String remark = DictUtils.findLabelByTypeAndKey("subjectImport", 5);

			if(!ccIndicationList.isEmpty()){
				header2.put(supportIndication, ccIndications);
			}

			Integer headerHeight = ccIndicationList.isEmpty() ? 1 : 2;
			String url = fileInfo.getTempUrl();
			File file = new File(url);
	        InputStream inputStream = new FileInputStream(file); 
	        ExcelParser excelParser = new Header2ExcelParser();
	        ExcelDefinition excelDefinition = excelParser.parse(inputStream, header2, headerHeight);

            //header中的数据直接返回
			RowDefinition prHeaderOne = excelDefinition.getHeader();
			GroupColumnDefinition prHeaderTow = prHeaderOne.getGroupColumnDefinition();
			//解析hear中的数据验证是否合理
			List<ColumnDefinition> preHeader = prHeaderTow.getColumns();

			Integer index = ccIndications.isEmpty() ? 0 : 1;
			//前面2列以及最后2列都是确定的
			Integer size = ccIndications.size();
			ColumnDefinition first = preHeader.get(0);
			ColumnDefinition second = preHeader.get(1);
			ColumnDefinition secondToLast = preHeader.get(index + 2);
			ColumnDefinition last = preHeader.get(index + 3);
			if(!no.equals(first.getName())){
                return renderFAIL("2103", response, header, String.format("excel中的第1列列名不是%s,请检查", no));
			}
			if(!score.equals(second.getName())){
				return renderFAIL("2103", response, header, String.format("excel中的第2列列名不是%s,请检查", score));
			}

			//如果存在课程目标，验证一级列名以及二级列名是否正确
			if(!ccIndications.isEmpty()){
				ColumnDefinition group = preHeader.get(2);
				// 课程目标列表
				Integer startIndex = 3,
						endIndex = size + 2;
				List<ColumnDefinition> indicationHeader = ((GroupColumnDefinition) group).getColumns();
				if(indicationHeader.size() == 1){
					if(!supportIndication.equals(group.getName())){
						return renderFAIL("2103", response, header, String.format("excel中的第%s列的一级列名不是%s,请检查", endIndex, supportIndication));
					}
				}else{
					if(!supportIndication.equals(group.getName())){
						return renderFAIL("2103", response, header, String.format("excel中的第%s列到第%s列的的一级列名不是%s,请检查", startIndex, endIndex, supportIndication));
					}
				}

				//验证2级列名是否合理
				for(ColumnDefinition columnDefinition : indicationHeader){
					if(!ccIndications.contains(columnDefinition.getName())){
						return renderFAIL("2103", response, header, String.format("当前的成绩组成下不存在第%s列的课程目标,请检查", columnDefinition.getIndex() + 1));
					}
				}
			}

			if(!detail.equals(secondToLast.getName())){
				return renderFAIL("2103", response, header, String.format("excel中的第%s列列名不是%s,请检查", size + 3, detail));
			}
            if(!remark.equals(last.getName())){
				return renderFAIL("2103", response, header, String.format("excel中的第%s列列名不是%s,请检查", size + 4, remark));
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

			// 上传文件内容为空过滤
			if(subjects.isEmpty()){
				return renderFAIL("2100", response, header);
			}

			ccCourseGradecomposeDetailService.validateImportSubject(subjects, ccCourseGradeComposeDetails, courseGradeComposeId, indicationIdMap, ccCourseGradecomposeDetailIndications, ccIndications);
			result.put("header", prHeaderTow);
		} catch (Exception e) {
			e.printStackTrace();
			if (logger.isErrorEnabled()) {
				logger.error("批量导入题目失败", e);
			}
			return renderFAIL("400", response, header, StringUtil.judgeContainsStr(e.getMessage()) ? "请检查模板的是否正确。包括：1.题目是否存在重复。2.是否修改过结构。3.支持选择课程目标是否重复。解决方案：重新下载一份。" : e.getMessage());
		}

		result.put("subjects", subjects);
		return renderSUC(result, response, header);
	}

}